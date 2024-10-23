package com.github.kleesup.kleegdx.client.net;

import com.badlogic.gdx.utils.PooledLinkedList;
import com.badlogic.gdx.utils.SnapshotArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.github.kleesup.kleegdx.core.net.GameServer;
import com.github.kleesup.kleegdx.core.net.packet.PacketQueue;
import com.github.kleesup.kleegdx.core.util.Updateable;
import lombok.Getter;
import lombok.Setter;

/**
 * An implementation of {@link GameServer} that can be used for SinglePlayer and localhost implementations.
 * To make network games less complicated, it is mostly easier to use an integrated server into the application.
 */
public abstract class AbstractIntegratedServer extends GameServer {

    /**
     * A class that functions like {@link PacketQueue} but will work completely different in the
     * {@link HostQueue#update(float)} because it will use each packet to notify the listeners.
     */
    private static class HostQueue extends PacketQueue {
        private final AbstractIntegratedServer server;
        public HostQueue(AbstractIntegratedServer server, int maxPacketsPerRead) {
            super(null, maxPacketsPerRead);
            this.server = server;
        }
        @Override
        public void update(float delta) {
            Listener[] listeners = server.allListeners.items;
            Object obj;
            int reads = 0;
            Connection host = server.host instanceof Connection ? (Connection) server.host : null;
            while (reads <= getMaxPacketsPerRead() && (obj = poll()) != null) {
                reads++;
                for(Listener listener : listeners) listener.received(host, obj);
            }
        }
        @Override
        protected void handle(Connection owner, Object obj) {}
    }

    @Setter @Getter
    private Object host;
    private final HostQueue hostQueue;
    public AbstractIntegratedServer(boolean useUDP, int maxHostPacketsPerRead) {
        super(useUDP);
        this.hostQueue = new HostQueue(this, maxHostPacketsPerRead);
    }
    public AbstractIntegratedServer(boolean useUDP) {
        this(useUDP, 100);
    }

    @Override
    protected synchronized void setUpdateAutomatically(boolean enable) {
        boolean wasAutomatic = doesUpdateAutomatically();
        super.setUpdateAutomatically(enable);
        if(wasAutomatic && !enable){ //if it was just using the queue, clear it
            hostQueue.update(0);
        }
    }

    /* -- Listeners and packet processing -- */

    /**
     * Processes a net object directly. Can be used from any client related content. Therefore, no packet send over
     * network and no serialization/is required.
     * <p>
     * If the server is updating automatically via the {@link #update(int)} method from KryoNet, the packets will be
     * queued and handled later. Otherwise they will be handled directly. This is to ensure no
     * </p>
     * @param obj The object to process.
     */
    public void processDirectly(Object obj){
        if(updatesAutomatically){ //give to queue
            hostQueue.add(obj);
        }else{
            for(Listener listener : allListeners.items)
                listener.received(host instanceof Connection ? (Connection) host : null, obj);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(updatesAutomatically){
            hostQueue.update(delta);
        }
    }


}
