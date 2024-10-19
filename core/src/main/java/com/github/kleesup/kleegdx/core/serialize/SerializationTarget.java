package com.github.kleesup.kleegdx.core.serialize;

/**
 * Denotes what a class (or method) in a serialization context should be used for. This could be either:
 * <l>
 *     <li>NET: Should only be used for packet serialization, often version dependent</li>
 *     <li>STORING: Should only be used for storing data to drives or databases, usually version independent</li>
 *     <li>BOTH: Most optimal, freely to use for both storing and net packets, should be version independent</li>
 * </l>
 */
public @interface SerializationTarget {

    Type target();

    enum Type{
        NET,
        STORING,
        BOTH
    }

}
