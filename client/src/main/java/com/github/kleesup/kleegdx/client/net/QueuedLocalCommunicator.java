package com.github.kleesup.kleegdx.client.net;

import com.github.kleesup.kleegdx.core.net.NetParticipant;
import com.github.kleesup.kleegdx.core.net.listener.QueuedTypeListenerClient;
import lombok.Getter;

/**
 * An implementation of {@link LocalCommunicator} that queues packets incoming from the integrated server directly
 * without net overhead. This class also implements {@link NetParticipant} so it can be registered somewhere on the
 * server site to send packets to. Packets send to this participant will be queued instantly.
 */
@Getter
public class QueuedLocalCommunicator extends LocalCommunicator implements NetParticipant {

    protected final QueuedTypeListenerClient listener;
    public QueuedLocalCommunicator(AbstractIntegratedServer server, int maxPacketsPerRead) {
        super(server);
        this.listener = new QueuedTypeListenerClient(maxPacketsPerRead);
    }

    @Override
    public void update(float delta) {
        listener.update(delta);
    }

    /**
     * This overridden method from {@link NetParticipant} can be used on the <b>integrated servers site</b> to send
     * packets to a client that isn't technically a net client.
     * @param obj The object to send to this communicator.
     * @param udp Whether to use UDP.
     */
    @Override
    public void send(Object obj, boolean udp) {
        listener.queuePacket(obj);
    }
}
