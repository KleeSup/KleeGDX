package com.github.kleesup.kleegdx.core.serialize;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;

/**
 * Simple class that manages some kleegdx library based serialization to Kryo.
 * Included are the following operations:
 * <l>
 *     <li>registering default gdx classes via {@link #registerDefaults()}</li>
 *     <li>when registering classes without specifying a {@link Serializer}, {@link #checkBaseSerializer(Class)} is
 *     called to find inner {@link Serializer} classes to use.</li>
 * </l>
 */
@Getter
public class KryoRegisterer {

    private final Kryo kryo;
    public KryoRegisterer(Kryo kryo){
        this.kryo = kryo;
        registerDefaults();
    }

    private void registerDefaults(){
        kryo.register(Vector2.class, new Serializer<Vector2>() {
            @Override
            public void write(Kryo kryo, Output output, Vector2 object) {
                output.writeFloat(object.x);
                output.writeFloat(object.y);
            }
            @Override
            public Vector2 read(Kryo kryo, Input input, Class type) {
                return new Vector2(input.readFloat(), input.readFloat());
            }
        });
        kryo.register(Vector3.class, new Serializer<Vector3>() {
            @Override
            public void write(Kryo kryo, Output output, Vector3 object) {
                output.writeFloat(object.x);
                output.writeFloat(object.y);
                output.writeFloat(object.z);
            }
            @Override
            public Vector3 read(Kryo kryo, Input input, Class type) {
                return new Vector3(input.readFloat(), input.readFloat(), input.readFloat());
            }
        });
        kryo.register(Rectangle.class, new Serializer<Rectangle>() {
            @Override
            public void write(Kryo kryo, Output output, Rectangle object) {
                output.writeFloat(object.x);
                output.writeFloat(object.y);
                output.writeFloat(object.width);
                output.writeFloat(object.height);
            }
            @Override
            public Rectangle read(Kryo kryo, Input input, Class type) {
                return new Rectangle(input.readFloat(), input.readFloat(), input.readFloat(), input.readFloat());
            }
        });
    }

    /**
     * Registers a class to the kryo instance. Before calling {@link Kryo#register(Class)}, the method will try to find
     * an inner class that inherits {@link Serializer}. If so, that class is used as serializer for registration.
     * @param clazz The class to register.
     */
    public void register(Class<?> clazz){
        if(!checkBaseSerializer(clazz))kryo.register(clazz);
    }

    /**
     * See {@link Kryo#register(Class, Serializer)}.
     */
    public void register(Class clazz, Serializer serializer){
        kryo.register(clazz, serializer);
    }

    /**
     * Checks if a class has any inner class that inherits {@link Serializer}. If so, that class will be used as
     * serializer for the specified class.
     * @param clazz The class to register.
     * @return Whether an inner serializer was found and could be registered to that class or not.
     */
    private boolean checkBaseSerializer(Class<?> clazz){
        for(Class<?> under : clazz.getDeclaredClasses()){
            if(under.isAssignableFrom(Serializer.class)){
                try {
                    Serializer serializer = (Serializer) under.getConstructor().newInstance();
                    kryo.register(clazz, serializer);
                    return true;
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    /**
     * Registers a class by acknowledging that it extends {@link IDataTransferable} and will therefore use the build dto
     * instance for serialization and deserialization.
     * @param clazz The class to register. Needs to extend {@link IDataTransferable}.
     * @param dtoClass The class of the data transfer object. If not {@code null}, the dto class will also be tried to
     *                 be registered via {@link #register(Class)}.
     */
    public <T extends IDataTransferable<?>> void registerWithDTO(Class<T> clazz, Class<?> dtoClass){
        if(dtoClass != null)register(dtoClass);
        kryo.register(clazz, new Serializer<T>() {
            @Override
            public void write(Kryo kryo, Output output, T object) {
                kryo.writeObject(output, object.toDataTransferObject());
            }
            @Override
            public T read(Kryo kryo, Input input, Class type) {
                return IDataTransferable.fromDataTransferObject(clazz, kryo.readObject(input, dtoClass));
            }
        });
    }

    /**
     * See {@link #registerWithDTO(Class, Class)}.
     */
    public <T extends IDataTransferable<?>> void registerWithDTO(Class<T> clazz){
        registerWithDTO(clazz,null);
    }



}
