package com.github.kleesup.kleegdx.core.net.packet;

import com.esotericsoftware.kryonet.Connection;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * A simple class holding track of registered packets and their processing functions.
 */
@SuppressWarnings("rawtypes")
public class TypePacketProcessor extends HashMap<Class<?>, BiConsumer> {

    public TypePacketProcessor(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TypePacketProcessor(int initialCapacity) {
        super(initialCapacity);
    }

    public TypePacketProcessor() {
    }

    public TypePacketProcessor(Map<? extends Class<?>, ? extends BiConsumer> m) {
        super(m);
    }

    /**
     * Registers a packet with a given processor.
     * @param clazz The packets class.
     * @param processor The processor for this packet.
     */
    public <T> void register(Class<T> clazz,
                                   BiConsumer<? super Connection, ? super T> processor) {
        put(clazz, processor);
    }

    /**
     * Unregisters a packet from the processor map.
     * @param clazz The packets class.
     */
    public <T> void unregister(Class<T> clazz) {
        remove(clazz);
    }

}
