package com.github.kleesup.kleegdx.client.net;

import com.badlogic.gdx.utils.Disposable;
import com.github.kleesup.kleegdx.core.util.Updateable;

/**
 * Simple base interface for a client implementation that can communicate to a server instance.
 * It can be implemented to send both TCP and UDP packets.
 */
public interface Communicator extends Updateable, Disposable {

    /**
     * Tries to send an object via TCP.
     * @param obj The object to send.
     */
    void sendTCP(Object obj);

    /**
     * Tries to send an object via UDP.
     * @param obj The object to send.
     */
    void sendUPD(Object obj);

    /**
     * @return Whether the client is currently connected to some server instance.
     */
    boolean isConnected();

}
