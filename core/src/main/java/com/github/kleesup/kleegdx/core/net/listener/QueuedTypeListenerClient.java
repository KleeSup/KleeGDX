package com.github.kleesup.kleegdx.core.net.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.github.kleesup.kleegdx.core.net.packet.IPacketQueueable;
import com.github.kleesup.kleegdx.core.net.packet.PacketQueue;
import com.github.kleesup.kleegdx.core.net.packet.TypePacketProcessor;
import com.github.kleesup.kleegdx.core.net.packet.TypePacketQueue;
import com.github.kleesup.kleegdx.core.util.Updateable;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * Similar to the {@link QueuedTypeListenerServer} but it will just hold one single packet queue for the client.
 */
public class QueuedTypeListenerClient extends TypePacketProcessor implements Listener, Updateable, IPacketQueueable {

    private Consumer<Connection> connectFunction, disconnectFunction;
    private final ConcurrentLinkedQueue<Connection> connections = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Connection> disconnects = new ConcurrentLinkedQueue<>();
    private final PacketQueue queue;
    public QueuedTypeListenerClient(int maxPacketsPerRead){
        this.queue = new TypePacketQueue(null, maxPacketsPerRead, this);
    }

    /* -- Connect/Disconnect -- */

    public void onConnect(Consumer<Connection> function){
        this.connectFunction = function;
    }
    public void onDisconnect(Consumer<Connection> function) {
        this.disconnectFunction = function;
    }

    @Override
    public void connected(Connection connection) {
        connections.add(connection);
    }

    @Override
    public void disconnected(Connection connection) {
        disconnects.add(connection);
    }

    /* -- Queueing -- */

    @Override
    public void queuePacket(Object obj){
        if(obj == null)return;
        queue.add(obj);
    }

    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof FrameworkMessage)return;
        queue.add(object);
    }

    @Override
    public void update(float delta) {
        if(connectFunction != null && !connections.isEmpty()){
            Connection obj;
            while ((obj = connections.poll()) != null) {
                connectFunction.accept(obj);
            }
        }
        if(disconnectFunction != null && !disconnects.isEmpty()){
            Connection obj;
            while ((obj = disconnects.poll()) != null) {
                disconnectFunction.accept(obj);
            }
        }
        queue.update(delta);
    }
}
