package com.github.kleesup.kleegdx.core.serialize;

import com.badlogic.gdx.utils.Pool;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.HashMap;

/**
 * An implementation of {@link BaseSerializer} that uses a {@link HashMap} internally to store data into and serialize
 * with. Furthermore, the class features a {@link Pool} to store map objects. Otherwise, a serializing class can also
 * inherit from {@link IMapHolder} to hold an own serialization map for itself.
 * <p>Simple implementation:</p>
 * <pre>{@code
 * public class MyClass{
 *     private int xp;
 *     private float health;
 *     private String name;
 *
 *     public static abstract class MyClassSerializer extends BaseMapSerializer<MyClass>{
 *         @Override
 *         protected void fill(HashMap content, MyClass instance){
 *              content.put("xp", xp);
 *              content.put("health", health);
 *              content.put("name", name);
 *         }
 *         @Override
 *         protected void readMap(HashMap content, MyClass instance){
 *              instance.xp = content.get("xp");
 *              instance.health = content.get("health");
 *              instance.name = content.get("name");
 *              //to add version independence we can use content.getOrDefault()
 *              //instead of normal content.get() to include versions
 *              //were specific variables didn't exist and now have to be loaded
 *              //from a default value.
 *         }
 *     }
 * }
 * }</pre>
 */
@SerializationTarget(target = SerializationTarget.Type.BOTH)
public abstract class BaseMapSerializer<T> extends BaseSerializer<T> {

    public static boolean DEFAULT_POOLING = false;

    private Pool<HashMap> mapPool;
    public BaseMapSerializer(boolean pooled) {
        if(pooled){
            mapPool = new Pool<HashMap>() {
                @Override
                protected HashMap newObject() {
                    return new HashMap();
                }
                @Override
                protected void reset(HashMap object) {
                    object.clear();
                }
                @Override
                protected void discard(HashMap object) {}
            };
        }
    }
    public BaseMapSerializer() {this(DEFAULT_POOLING);}

    @Override
    protected void serialize(Kryo kryo, Output output, T obj) {
        HashMap map;
        if(obj instanceof IMapHolder){
            map = ((IMapHolder) obj).getSerializerMap();
            map.clear();
        }else map = mapPool!=null ? mapPool.obtain() : new HashMap();
        fill(map, obj);
        kryo.writeObject(output, map);
    }

    @Override
    protected void deserialize(Kryo kryo, Input input, Class<? extends T> clazz, T instance) {
        HashMap map = kryo.readObject(input, HashMap.class);
        readMap(map, instance);
        if(mapPool!=null)mapPool.free(map);
    }

    /**
     * Here the map should be loaded for serialization in data storage or sending via network.
     * @param content The map to be filled.
     * @param instance The instance to serialize.
     */
    protected abstract void fill(HashMap content, T instance);

    /**
     * Here the map should be read from for deserialization.
     * @param content The content that was loaded from storage or received over packets.
     * @param instance The instance to deserialize.
     */
    protected abstract void readMap(HashMap content, T instance);

    /**
     * Small interface for classes that hold their own serializer map.
     */
    public interface IMapHolder{
        HashMap getSerializerMap();
    }

}
