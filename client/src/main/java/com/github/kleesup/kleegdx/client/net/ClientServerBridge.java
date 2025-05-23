package com.github.kleesup.kleegdx.client.net;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.github.kleesup.kleegdx.core.net.packet.IPacketQueueable;
import com.github.kleesup.kleegdx.core.net.packet.TypePacketProcessor;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * A class that can be used for cases where an application supports both remote and integrated servers. Therefore, this
 * class manages the required {@link Communicator} for each use case. It requires a packet registration for the cases
 * where received packets are queued and later handled. This bridge needs to be updated via {@link #update(float)}
 */
@Getter
public abstract class ClientServerBridge implements Communicator, IPacketQueueable {

    private final Communicator communicator;
    private final boolean canReceivePackets;

    // -- Remote server constructors --

    /**
     * Builds a new bridge that connects to a host remote server. This will use the {@link RemoteCommunicator}.
     * @param clientObj A (custom) client object that will be given to the {@link RemoteCommunicator}.
     * @param host The hostname to connect to.
     * @param port The port for the hostname.
     * @param useUdp Whether to enable udp on the client.
     * @param maxPacketsPerRead The max amount of packets that will be read per {@link #update(float)}.
     */
    public ClientServerBridge(Client clientObj, String host, int port, boolean useUdp, int maxPacketsPerRead){
        RemoteCommunicator remote = new RemoteCommunicator(clientObj, host, port, useUdp, maxPacketsPerRead){
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
                onConnect(fun -> ClientServerBridge.this.onConnect());
            }
            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                onConnect(fun -> ClientServerBridge.this.onDisconnect());
            }
        };
        this.communicator = remote;
        registerIncomingPackets(remote);
        this.canReceivePackets = true;
    }
    public ClientServerBridge(String host, int port, boolean useUdp, int maxPacketsPerRead){
        this(new Client(), host, port, useUdp, maxPacketsPerRead);
    }

    // -- Local integrated constructors --

    /**
     * Builds a bridge that connects to a local integrated server. This bridge will queue incoming packets. This will
     * use the {@link QueuedLocalCommunicator}.
     * @param server The local server instance to use.
     * @param maxPacketsPerRead The max amount of packets that will be read per {@link #update(float)}.
     */
    public ClientServerBridge(AbstractIntegratedServer server, int maxPacketsPerRead){
        QueuedLocalCommunicator communicator = new QueuedLocalCommunicator(server,maxPacketsPerRead);
        this.communicator = communicator;
        registerIncomingPackets(communicator.getListener());
        this.canReceivePackets = true;
    }

    /**
     * Builds a new bridge that connects to a local integrated server. This will use the {@link LocalCommunicator}.
     * @param server The local server instance to use.
     */
    public ClientServerBridge(AbstractIntegratedServer server){
        this.communicator = new LocalCommunicator(server);
        this.canReceivePackets = false;
    }



    /* -- Packets -- */

    /**
     * Required when the bridge was constructed via
     * {@link #ClientServerBridge(String, int, boolean, int)},
     * {@link #ClientServerBridge(Client, String, int, boolean, int)} or
     * {@link #ClientServerBridge(AbstractIntegratedServer, int)}. These types of bridge connections read packets when
     * received instead of manually understanding them in some sort of {@link AbstractIntegratedServer}. Therefore,
     * all packets need to be registered to the specified processor.
     * @param processor The packet processor to register packets to.
     */
    protected abstract void registerIncomingPackets(TypePacketProcessor processor);

    @Override
    public void queuePacket(Object obj) {
        processFromServer(obj);
    }

    /* -- Integrated -- */

    /**
     * Processed when an integrated server sends a packet to the host client.
     * @param obj The object from the server.
     */
    public void processFromServer(Object obj){
        if(canReceivePackets)((IPacketQueueable) communicator).queuePacket(obj);
    }

    /* -- Remote -- */

    /**
     * Called when {@link RemoteCommunicator#connected(Connection)} is invoked.
     * This is only possible if this class was instantiated as a remote connector via
     * {@link #ClientServerBridge(String, int, boolean, int)} or
     * {@link #ClientServerBridge(Client, String, int, boolean, int)} and therefore the connector instance is a
     * {@link RemoteCommunicator}.
     */
    public void onConnect(){

    }
    /**
     * Called when {@link RemoteCommunicator#connected(Connection)} is invoked.
     * This is only possible if this class was instantiated as a remote connector via
     * {@link #ClientServerBridge(String, int, boolean, int)} or
     * {@link #ClientServerBridge(Client, String, int, boolean, int)} and therefore the connector instance is a
     * {@link RemoteCommunicator}.
     */
    public void onDisconnect(){

    }

    /* -- Implementation of Communicator -- */

    @Override
    public void sendTCP(Object obj) {
        communicator.sendTCP(obj);
    }

    @Override
    public void sendUPD(Object obj) {
        communicator.sendUPD(obj);
    }

    @Override
    public boolean isConnected() {
        return communicator.isConnected();
    }

    @Override
    public void dispose() {
        communicator.dispose();
    }

    @Override
    public void update(float delta) {
        communicator.update(delta);
    }
}
