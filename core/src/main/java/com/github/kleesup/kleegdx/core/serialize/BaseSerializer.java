package com.github.kleesup.kleegdx.core.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Base class for serialization abstraction with kryo. Can be further reimplemented and reimplemented for deep
 * serialization of many inheriting classes (e.g. Entity -> LivingEntity -> Player).
 */
public abstract class BaseSerializer<T> extends Serializer<T> {

    /* -- Implementation -- */

    @Override
    public void write(Kryo kryo, Output output, T t) {
        serialize(kryo, output, t);
    }

    @Override
    public T read(Kryo kryo, Input input, Class<? extends T> aClass) {
        T instance = buildNewInstance();
        deserialize(kryo,input,aClass,instance);
        return instance;
    }

    /* -- Serialization abstraction -- */

    /**
     * Builds a new instance to write to on {@link #read(Kryo, Input, Class)}.
     * @return The new build instance.
     */
    protected abstract T buildNewInstance();

    /**
     * Serializes the data of the class to the {@link Kryo} object.
     * <p>NOTE: If this class was already implemented by a
     * super class, {@code super.serialize(kryo,output,obj)} has to be called first! Otherwise,
     * the deserialization will later fail as the supers values will be missing.</p>
     * @param kryo The kryo object which operates the serialization.
     * @param output The output stream to write to.
     * @param obj The object to serialize.
     */
    protected abstract void serialize(Kryo kryo, Output output, T obj);

    /**
     * Deserializes the data of an {@link Input} stream into a given instance.
     * <p>NOTE: In this instance will be further written, also from super classes. Therefore, it is necessary to
     * call {@code super.deserialize(kryo,input,clazz,instance)} first!</p>
     * @param kryo The kryo object which operates the deserialization.
     * @param input The input stream to read from.
     * @param clazz The class of the object.
     * @param instance The instance to write the read values to.
     */
    protected abstract void deserialize(Kryo kryo, Input input, Class<? extends T> clazz, T instance);

}
