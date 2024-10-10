package com.github.kleesup.kleegdx.client.net;

import com.badlogic.gdx.utils.SnapshotArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.github.kleesup.kleegdx.core.net.GameServer;
import lombok.Getter;
import lombok.Setter;

/**
 * An implementation of {@link GameServer} that can be used for SinglePlayer and localhost implementations.
 * To make network games less complicated, it is mostly easier to use an integrated server into the application.
 * @param <H> The type of host object that is wanted. Representing the entity in control of this server.
 */
public abstract class AbstractIntegratedServer<H> extends GameServer {

    @Setter @Getter
    private H host;
    private final Object lock = new Object();
    /** Keep track of all listeners **/
    protected final SnapshotArray<Listener> registeredListeners = new SnapshotArray<>(2);
    public AbstractIntegratedServer(boolean useUDP) {
        super(useUDP);
    }

    /**
     * Processes a net object directly. Can be used from any client related content. Therefore, no packet send over
     * network and no serialization/is required.
     * @param obj The object to process.
     */
    public void processDirectly(Object obj){
        Listener[] listeners = registeredListeners.begin();
        for(Listener listener : listeners)
            listener.received(host instanceof Connection ? (Connection) host : null, obj);
        registeredListeners.end();
    }

    @Override
    public void addListener(Listener listener) {
        super.addListener(listener);
        synchronized (lock){
            registeredListeners.add(listener);
        }
    }

    @Override
    public void removeListener(Listener listener) {
        synchronized (lock){
            super.removeListener(listener);
        }
    }

    @Override
    public abstract void update(float delta);
}
