package com.github.kleesup.kleegdx.core.net.packet;

import com.esotericsoftware.kryonet.Connection;
import com.github.kleesup.kleegdx.core.util.Updateable;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A simple packet queue based on {@link ConcurrentLinkedQueue} that can receive packets and handle/clear the queue
 * when updated via {@link #update(float)}.
 */
@Getter
public abstract class PacketQueue extends ConcurrentLinkedQueue<Object> implements Updateable {

    private final Connection owner;
    @Setter
    private int maxPacketsPerRead;
    public PacketQueue(Connection owner, int maxPacketsPerRead){
        this.owner = owner;
        this.maxPacketsPerRead = maxPacketsPerRead >= 0 ? maxPacketsPerRead : Integer.MAX_VALUE;
    }

    @Override
    public void update(float delta) {
        if(isEmpty())return;
        Object obj;
        int reads = 0;
        while (reads <= maxPacketsPerRead && (obj = poll()) != null) {
            reads++;
            handle(owner, obj);
        }
    }

    protected abstract void handle(Connection owner, Object obj);

}
