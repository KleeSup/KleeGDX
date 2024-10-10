package com.github.kleesup.kleegdx.core.net;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.github.kleesup.kleegdx.core.util.Updateable;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * An implementation of {@link Listener} which has the same feature as
 * {@link com.esotericsoftware.kryonet.Listener.TypeListener} but is furthermore extended to queue packets into an
 * {@link ConcurrentHashMap}.
 */
public class QueuedTypeListener implements Listener, Updateable {

    @SuppressWarnings("rawtypes")
    private final HashMap<Class<?>, BiConsumer> listeners = new HashMap<>();
    private final ConcurrentHashMap<Connection, Array<Object>> queue;
    public QueuedTypeListener() {
        queue = new ConcurrentHashMap<>();
    }

    @Override
    public void connected(Connection connection) {
        queue.put(connection, new Array<>(20));
    }

    @Override
    public void disconnected(Connection connection) {
        queue.remove(connection);
    }

    @Override
    public void update(float delta) {
        if(queue.isEmpty())return;
        for (Connection connection : queue.keySet()) {
            Array<Object> packets = queue.get(connection);
            synchronized (packets) {
                if(packets.isEmpty())continue;
                for (Object obj : packets.items) {
                    if (listeners.containsKey(obj.getClass()))//noinspection unchecked
                        listeners.get(obj.getClass()).accept(connection, obj);
                }
                packets.clear();
            }
        }
    }

    @Override
    public void received(Connection con, Object msg) {
        if(msg instanceof FrameworkMessage)return;
        Array<Object> packets = queue.get(con);
        synchronized (packets){
            packets.add(msg);
        }
    }

    /**
     * Adds a handler for a specific type.
     *
     * @param clazz
     *            The class of the type.
     * @param listener
     *            The listener.
     */
    public <T> void addTypeHandler(Class<T> clazz,
                                   BiConsumer<? super Connection, ? super T> listener) {
        listeners.put(clazz, listener);
    }

    public <T> void removeTypeHandler(Class<T> clazz) {
        listeners.remove(clazz);
    }

    public int size() {
        return listeners.size();
    }

    public void clear() {
        listeners.clear();
    }

}
