package com.github.kleesup.kleegdx.client.net;

import com.esotericsoftware.kryonet.Client;
import com.github.kleesup.kleegdx.core.net.listener.QueuedTypeListenerClient;
import com.github.kleesup.kleegdx.core.util.Verify;

import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * Implementation of {@link Communicator} which is used when the server instance to connect to is a remote server and
 * can therefore only be access by a network socket.
 * <p>
 * In contrary to {@link LocalCommunicator}, this class extends from a listener, the {@link QueuedTypeListenerClient}.
 * This means to be able to read packets, they need to be registered first via {@link #register(Class, BiConsumer)}.
 * </p>
 */
public class RemoteCommunicator extends QueuedTypeListenerClient implements Communicator {

    private final Client client;
    public RemoteCommunicator(Client clientObj, String host, int port, boolean udp, int maxPacketsPerRead){
        super(maxPacketsPerRead);
        Verify.nonNullArg(clientObj, "Client cannot be null!");
        this.client = clientObj;
        this.client.addListener(this);
        this.client.start();
        try {
            if(udp)this.client.connect(5000,host,port,port);
            else this.client.connect(host,port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public RemoteCommunicator(String host, int port, boolean udp, int maxPacketsPerRead){
        this(new Client(), host, port, udp, maxPacketsPerRead);
    }

    /* -- Communicator implementation -- */

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
        return client.isConnected();
    }

    @Override
    public void dispose() {
        try {
            client.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
