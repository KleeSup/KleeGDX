package com.github.kleesup.kleegdx.core.net.packet;

import com.esotericsoftware.kryonet.Connection;
import lombok.Getter;
import lombok.Setter;

/**
 * An implementation of {@link PacketQueue} that uses a {@link TypePacketProcessor} to handle packets being processed.
 */
@Getter
@Setter
public class TypePacketQueue extends PacketQueue {

    private final TypePacketProcessor processor;
    public TypePacketQueue(Connection owner, int maxPacketsPerRead, TypePacketProcessor processor) {
        super(owner, maxPacketsPerRead);
        this.processor = processor;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void handle(Connection owner, Object obj) {
        processor.get(obj.getClass()).accept(owner, obj);
    }
}
