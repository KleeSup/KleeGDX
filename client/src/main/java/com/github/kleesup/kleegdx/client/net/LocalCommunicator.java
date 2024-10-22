package com.github.kleesup.kleegdx.client.net;

/**
 * An implementation of {@link Communicator} that is used it the server instance to connect to is locally installed by
 * the same program running the client. Therefore, there should be some instance to access the server which is used
 * to directly send packets via this communicator.
 */
public class LocalCommunicator implements Communicator {

    private final AbstractIntegratedServer<?> server;
    public LocalCommunicator(AbstractIntegratedServer<?> server){
        this.server = server;
    }

    /* -- Implementation -- */

    @Override
    public void sendTCP(Object obj) {
        server.processDirectly(obj);
    }

    @Override
    public void sendUPD(Object obj) {
        server.processDirectly(obj);
    }

    @Override
    public boolean isConnected() {
        return server.isServerRunning();
    }

    @Override
    public void update(float delta) {}

    @Override
    public void dispose() {}
}
