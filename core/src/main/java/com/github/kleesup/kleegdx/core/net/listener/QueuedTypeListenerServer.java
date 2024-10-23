package com.github.kleesup.kleegdx.core.net.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.github.kleesup.kleegdx.core.net.packet.PacketQueue;
import com.github.kleesup.kleegdx.core.net.packet.TypePacketProcessor;
import com.github.kleesup.kleegdx.core.net.packet.TypePacketQueue;
import com.github.kleesup.kleegdx.core.util.Updateable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of {@link Listener} which has the same feature as
 * {@link com.esotericsoftware.kryonet.Listener.TypeListener} but is furthermore extended to queue packets into an
 * {@link ConcurrentHashMap}.
 */
public class QueuedTypeListenerServer extends TypePacketProcessor implements Listener, Updateable {

    private final ConcurrentHashMap<Connection, PacketQueue> queue;
    private final int maxPacketsPerRead;
    public QueuedTypeListenerServer(int maxPacketsPerRead) {
        queue = new ConcurrentHashMap<>();
        this.maxPacketsPerRead = maxPacketsPerRead;
    }

    @Override
    public void connected(Connection connection) {
        queue.put(connection, new TypePacketQueue(connection, maxPacketsPerRead, this));
    }

    @Override
    public void disconnected(Connection connection) {
        queue.remove(connection);
    }

    @Override
    public void update(float delta) {
        if(queue.isEmpty())return;
        for (Connection connection : queue.keySet()) queue.get(connection).update(delta);
    }

    @Override
    public void received(Connection con, Object msg) {
        if(msg instanceof FrameworkMessage)return;
        queue.get(con).add(msg);
    }

}
