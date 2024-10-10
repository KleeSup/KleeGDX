package com.github.kleesup.kleegdx.client.net;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

/**
 * Implementation of {@link Communicator} which is used when the server instance to connect to is a remote server and
 * can therefore only be access by a network socket.
 */
public abstract class AbstractRemoteCommunicator implements Communicator, Listener {

    private final Client client;
    private Connection connection;
    private final Array<Object> receivedPackets = new Array<>(20);
    public AbstractRemoteCommunicator(String host, int port, boolean udp){
        this.client = buildClient();
        this.client.addListener(this);
        this.client.start();
        try {
            if(udp)this.client.connect(5000,host,port,port);
            else this.client.connect(host,port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Builds a client object to use internally for networking.
     * @return The build client object.
     */
    protected abstract Client buildClient();

    /* -- Communicator implementation -- */

    @Override
    public void connected(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof FrameworkMessage)return;
        synchronized (receivedPackets) {
            receivedPackets.add(object);
        }
    }

    @Override
    public void sendTCP(Object obj) {
        client.sendTCP(obj);
    }

    @Override
    public void sendUPD(Object obj) {
        client.sendUDP(obj);
    }

    @Override
    public boolean isConnected() {
        return connection.isConnected();
    }

    @Override
    public void dispose() {
        try {
            client.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* -- Packet reading -- */

    @Override
    public void update(float delta) {
        //reading packets
        synchronized (receivedPackets){
            for(Object obj : receivedPackets.items)handle(obj);
            receivedPackets.clear();
        }
    }

    /**
     * Called when the {@link #update(float)} method found an object in the queue and can now handle it.
     * @param obj The object to handle.
     */
    protected abstract void handle(Object obj);

}
