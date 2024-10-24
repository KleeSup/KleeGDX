package com.github.kleesup.kleegdx.core.net.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.github.kleesup.kleegdx.core.net.packet.IPacketQueueable;
import com.github.kleesup.kleegdx.core.net.packet.PacketQueue;
import com.github.kleesup.kleegdx.core.net.packet.TypePacketProcessor;
import com.github.kleesup.kleegdx.core.net.packet.TypePacketQueue;
import com.github.kleesup.kleegdx.core.util.Updateable;

/**
 * Similar to the {@link QueuedTypeListenerServer} but it will just hold one single packet queue for the client.
 */
public class QueuedTypeListenerClient extends TypePacketProcessor implements Listener, Updateable, IPacketQueueable {

    private final PacketQueue queue;
    public QueuedTypeListenerClient(int maxPacketsPerRead){
        this.queue = new TypePacketQueue(null, maxPacketsPerRead, this);
    }

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
        queue.update(delta);
    }
}
