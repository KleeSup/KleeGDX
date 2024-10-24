package com.github.kleesup.kleegdx.core.net.packet;

/**
 * Small interfaces for manager class that accept packets to be queued or possibly also processed directly.
 */
public interface IPacketQueueable {

    /**
     * Queues a packet into this class.
     * @param obj The packet to queue.
     */
    void queuePacket(Object obj);
    
}
