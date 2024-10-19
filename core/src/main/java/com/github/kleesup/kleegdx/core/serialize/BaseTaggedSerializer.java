package com.github.kleesup.kleegdx.core.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.kleesup.kleegdx.core.util.Verify;

import java.util.HashMap;
import java.util.Map;

/**
 * This implementation of {@link BaseSerializer} uses internal {@link VarSupplier} wrapper objects to create a
 * serialization environment around single fields. Since Kryo reads/writes primitives, this behaviour is copied here
 * and therefore {@link VarSupplier} doesn't use any generics to avoid unnecessary wrapping.
 * <p>
 * Primarily, each field that should be serialized receives a own {@link VarSupplier} which has
 * different implementations. Then, a tag is applied to the field with supplier via
 * {@link #register(String, VarSupplier)}. A full functional implementation could look like this:
 * </p>
 * <pre>{@code
 * public class MyClass{
 *     private int xp;
 *     private float health;
 *     private String name;
 *
 *     protected static class MyClassSerializer extends BaseTaggedSerializer<MyClass>{
 *         @Override
 *         protected abstract void init(){
 *             register("xp", new VarSupplierInt() {
 *                  @Override
 *                  public void set(MyClass instance, int value) {
 *                      instance.xp = value;
 *                  }
 *                  @Override
 *                  public int get(MyClass instance) {
 *                      return instance.xp;
 *                  }
 *             });
 *             register("health", new VarSupplierFloat() {
 *                  @Override
 *                  public void set(MyClass instance, float value) {
 *                      instance.health = value;
 *                  }
 *                  @Override
 *                  public float get(MyClass instance) {
 *                      return instance.health;
 *                  }
 *             });
 *             register("name", new VarSupplierString() {
 *                  @Override
 *                  public void set(MyClass instance, String value) {
 *                      instance.name = value;
 *                  }
 *                  @Override
 *                  public String get(MyClass instance) {
 *                      return instance.name;
 *                  }
 *             });
 *         }
 *     }}
 * }</pre>
 * <p>
 * You can quickly see how this class can rapidly create a lot of boilerplate code easily.
 * Therefore this serialization method shouldn't be used on classes that have more than 8+ fields to serialize.
 * The use in this class is in small serialization processes that need <b>version independent</b>
 * tagged data to be stored. It avoids boxing primitives and should only be used for data storage.</p>
 * <p>
 * For big abstract class serialization which is still version independent and networking compatible, use
 * {@link BaseMapSerializer}.
 * </p>
 */
@SerializationTarget(target = SerializationTarget.Type.STORING)
public abstract class BaseTaggedSerializer<V> extends BaseSerializer<V> {

    public BaseTaggedSerializer() {init();}

    private final HashMap<String, VarSupplier> suppliers = new HashMap<>();

    /**
     * Registers a supplier for a field with a specific tag.
     * @param tag The tag for the field (could be the name of the field to keep it simple).
     * @param supplier The supplier object for that field.
     */
    protected void register(String tag, VarSupplier supplier){
        Verify.nonNullArg(tag, "Tag cannot be null!");
        Verify.nonNullArg(supplier, "Supplier cannot be null!");
        if(suppliers.containsKey(tag))throw new KryoException("Tag '"+tag+"' is already registered!");
        suppliers.put(tag, supplier);
    }

    /**
     * Here should all suppliers be registered.
     */
    protected abstract void init();

    @SuppressWarnings("unchecked")
    @Override
    protected void serialize(Kryo kryo, Output output, V obj) {
        for(Map.Entry<String, VarSupplier> entry : suppliers.entrySet()){
            //format
            //1: tag
            //2: type
            //3: value
            output.writeString(entry.getKey());
            VarSupplier supplier = entry.getValue();
            output.writeByte((byte)supplier.getType().ordinal());
            switch (supplier.getType()){
                case OBJECT:
                    kryo.writeObject(output, ((VarSupplierObject) supplier).get(obj));
                    break;
                case STRING:
                    output.writeString(((VarSupplierString) supplier).get(obj));
                    break;
                case BYTE:
                    output.writeByte(((VarSupplierByte) supplier).get(obj));
                    break;
                case SHORT:
                    output.writeShort(((VarSupplierShort) supplier).get(obj));
                    break;
                case INT:
                    output.writeInt(((VarSupplierInt) supplier).get(obj));
                    break;
                case LONG:
                    output.writeLong(((VarSupplierLong) supplier).get(obj));
                    break;
                case FLOAT:
                    output.writeFloat(((VarSupplierFloat) supplier).get(obj));
                    break;
                case DOUBLE:
                    output.writeDouble(((VarSupplierDouble) supplier).get(obj));
                    break;
                case CHAR:
                    output.writeChar(((VarSupplierChar) supplier).get(obj));
                    break;
                case BOOL:
                    output.writeBoolean(((VarSupplierBool) supplier).get(obj));
                    break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void deserialize(Kryo kryo, Input input, Class<? extends V> clazz, V instance) {
        while (!input.end()){
            String tag = input.readString();
            byte type = input.readByte();
            switch (VarType.values()[type]){
                case OBJECT:
                    ((VarSupplierObject) suppliers.get(tag)).set(instance, kryo.readClassAndObject(input));
                    break;
                case STRING:
                    ((VarSupplierString) suppliers.get(tag)).set(instance, input.readString());
                    break;
                case BYTE:
                    ((VarSupplierByte) suppliers.get(tag)).set(instance, input.readByte());
                    break;
                case SHORT:
                    ((VarSupplierShort) suppliers.get(tag)).set(instance, input.readShort());
                    break;
                case INT:
                    ((VarSupplierInt) suppliers.get(tag)).set(instance, input.readInt());
                    break;
                case LONG:
                    ((VarSupplierLong) suppliers.get(tag)).set(instance, input.readLong());
                    break;
                case FLOAT:
                    ((VarSupplierFloat) suppliers.get(tag)).set(instance, input.readFloat());
                    break;
                case DOUBLE:
                    ((VarSupplierDouble) suppliers.get(tag)).set(instance, input.readDouble());
                    break;
                case CHAR:
                    ((VarSupplierChar) suppliers.get(tag)).set(instance, input.readChar());
                    break;
            }
        }
    }

    /* -- Implementation of suppliers -- */

    private enum VarType{
        OBJECT, STRING, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, CHAR, BOOL;
    }

    protected static abstract class VarSupplier{
        abstract VarType getType();
    }
    public abstract class VarSupplierObject extends VarSupplier{
        public abstract <T> void set(V instance, T value);
        public abstract Object get(V instance);
        @Override
        VarType getType() {
            return VarType.OBJECT;
        }
    }
    public abstract class VarSupplierString extends VarSupplier{
        public abstract void set(V instance, String value);
        public abstract String get(V instance);
        @Override
        VarType getType() {
            return VarType.STRING;
        }
    }
    public abstract class VarSupplierByte extends VarSupplier{
        public abstract void set(V instance, byte value);
        public abstract byte get(V instance);
        @Override
        VarType getType() {
            return VarType.BYTE;
        }
    }
    public abstract class VarSupplierShort extends VarSupplier{
        public abstract void set(V instance, short value);
        public abstract short get(V instance);
        @Override
        VarType getType() {
            return VarType.SHORT;
        }
    }
    public abstract class VarSupplierInt extends VarSupplier{
        public abstract void set(V instance, int value);
        public abstract int get(V instance);
        @Override
        VarType getType() {
            return VarType.INT;
        }
    }
    public abstract class VarSupplierLong extends VarSupplier{
        public abstract void set(V instance, long value);
        public abstract long get(V instance);
        @Override
        VarType getType() {
            return VarType.LONG;
        }
    }
    public abstract class VarSupplierFloat extends VarSupplier{
        public abstract void set(V instance, float value);
        public abstract float get(V instance);
        @Override
        VarType getType() {
            return VarType.FLOAT;
        }
    }
    public abstract class VarSupplierDouble extends VarSupplier{
        public abstract void set(V instance, double value);
        public abstract double get(V instance);
        @Override
        VarType getType() {
            return VarType.DOUBLE;
        }
    }
    public abstract class VarSupplierChar extends VarSupplier{
        public abstract void set(V instance, char value);
        public abstract char get(V instance);
        @Override
        VarType getType() {
            return VarType.CHAR;
        }
    }
    public abstract class VarSupplierBool extends VarSupplier{
        public abstract void set(V instance, boolean value);
        public abstract boolean get(V instance);
        @Override
        VarType getType() {
            return VarType.BOOL;
        }
    }

}
