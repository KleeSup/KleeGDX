package com.github.kleesup.kleegdx.core.serialize;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public interface IDataTransferable<T> {

    /**
     * Tries to deserialize a data transfer object (dto) into its full instance.
     * Two methods are tried here to achieve this:
     * <l>
     *     <li>1) Search for any constructor that takes one argument being the data transfer object</li>
     *     <li>2) if 1) fails, search for a static method named 'deserialize' taking the dto as param to call. </li>
     * </l>
     * <p>For the static method the following conditions must be met. The method must be:</p>
     * <l>
     *     <li>static</li>
     *     <li>named 'deserialize'</li>
     *     <li>have only one parameter</li>
     *     <li>that parameter being the data transfer object</li>
     *     <li>the return type must be an instance of the specified class</li>
     * </l>
     * The method could look something like this:
     * <pre><code>
     *     public class MyEntity implements IDataTransferable<MyDTO>{
     *
     *         public static MyEntity deserialize(MyDTO dto){
     *             //do something to deserialize here
     *             return myEntityInstance;
     *         }
     *
     *         public MyDTO toDataTransferObject(){
     *             return new MyDTO(this); //some implementation for data transfer object here
     *         }
     *     }
     * </code></pre>
     * If all conditions are met, the method is resolved and called.
     * @param clazz The class of the object to build a new instance of.
     * @param dto The data transfer object to deserialize.
     * @return The deserialized object instance.
     * @param <R> The desired object type to build.
     */
    @SuppressWarnings("unchecked")
    static <R extends IDataTransferable<?>> R fromDataTransferObject(Class<R> clazz, Object dto){
        //search for constructor
        try {
            Constructor<R> constructor = clazz.getDeclaredConstructor(dto.getClass());
            boolean noAccess = !constructor.isAccessible();
            if(noAccess)constructor.setAccessible(true);
            R obj = constructor.newInstance(dto);
            if(noAccess)constructor.setAccessible(false);
            return obj;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException ignored) {
        }
        //search for static method that takes in the dto instance.
        for(Method method : clazz.getDeclaredMethods()){
            if(!Modifier.isStatic(method.getModifiers()))continue; //has to be static
            if(method.getParameterCount() != 1)continue; //only takes in dto parameter
            if(!method.getParameterTypes()[0].isAssignableFrom(dto.getClass()))continue; //parameter is dto object
            if(!method.getReturnType().isAssignableFrom(clazz))continue; //the return value is the deserialized object
            if(!method.getName().equals("deserialize"))continue; //the methods name is 'deserialize'
            try {
                boolean noAccess = !method.isAccessible();
                if(noAccess)method.setAccessible(true);
                R obj = (R) method.invoke(null, dto);
                if(noAccess)method.setAccessible(false);
                return obj;
            } catch (IllegalAccessException | InvocationTargetException ignored) {}
        }
        throw new RuntimeException("Neither found a static 'deserialize' method nor " +
                "any constructor taking in the dto object for class "+
                clazz.getSimpleName()+" and dto "+dto.getClass().getSimpleName()+"!");
    }

    /**
     * Transforms the contents of the implementing class to a data transfer object.
     * @return The build data transfer object.
     */
    T toDataTransferObject();

}
