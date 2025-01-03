package com.github.kleesup.kleegdx.core.collection;

import com.github.kleesup.kleegdx.core.util.Verify;

import java.util.*;
import java.util.function.Supplier;

/**
 * A map that holds a collection for each key. This class simplifies inner-value collection accesses.
 * <p>Created on 30.01.2023</p>
 * @author KleeSup
 * @version 1.0
 * @since 1.1.3
 */
public class MappedCollection<K, V> {

    public static <K,V> MappedCollection<K, V> buildHashMapLinkedList(){
        return new MappedCollection<>(new HashMap<>(), LinkedList::new);
    }

    private final Map<K, Collection<V>> elements;
    private final Supplier<Collection<V>> collectionBuilder;
    public MappedCollection(Map<K, Collection<V>> map, Supplier<Collection<V>> collectionBuilder){
        Verify.nonNullArg(map, "Map cannot be null!");
        this.elements = map;
        this.collectionBuilder = collectionBuilder;
    }
    public MappedCollection(Supplier<Collection<V>> collectionBuilder){
        this(new HashMap<>(), collectionBuilder);
    }
    public MappedCollection(){
        this(LinkedList::new);
    }

    /**
     * Prepares a collection for adding to it. If the map does not contain a collection for the given key, it will create
     * a new one and then put it into the map to register the key for it.
     * @param key The key to prepare the collection for.
     * @return The collection of the key.
     */
    private Collection<V> prepareAdd(K key){
        Collection<V> collection;
        if(containsCollection(key)){
            collection = elements.get(key);
        }else{
            collection = collectionBuilder.get();
            elements.put(key, collection);
        }
        return collection;
    }

    /**
     * Adds an object to the collection of a given key.
     * @param key The key of the collection.
     * @param value The object to add to the collection.
     * @return {@code true} if the object was added successfully, {@code false} otherwise.
     */
    public boolean add(K key, V value){
        return prepareAdd(key).add(value);
    }

    /**
     * Adds a collection of objects to the collection of a given key.
     * @param key The key of the collection.
     * @param values The collection of objects to add to the collection.
     * @return {@code true} if the collection was added successfully, {@code false} otherwise.
     */
    public boolean addAll(K key, Collection<V> values) {
        return prepareAdd(key).addAll(values);
    }

    /**
     * Removes an object from a collection of a given key.
     * @param key The key of the collection.
     * @param value The value to remove.
     * @param removeEmptyCollection Determines whether an empty collection should be completely removed from the map.
     * @return {@code true} if the object was successfully removed, {@code false} otherwise.
     */
    public boolean remove(K key, V value, boolean removeEmptyCollection){
        if(!containsCollection(key))return false;
        Collection<V> collection = getCollection(key);
        boolean success = collection.remove(value);
        if(removeEmptyCollection && collection.isEmpty())removeCollection(key);
        return success;
    }

    /**
     * Removes an object from a collection of a given key.
     * @param key The key of the collection.
     * @param value The value to remove.
     * @return {@code true} if the object was successfully removed, {@code false} otherwise.
     */
    public boolean remove(K key, V value){
        return remove(key,value,true);
    }

    /**
     * Removes the collection of a given key from the map.
     * @param key The key to remove the collection from.
     * @return The removed collection or {@code null} if the given key was not registered.
     */
    public Collection<V> removeCollection(K key){
        if(!containsCollection(key))return null;
        return elements.remove(key);
    }

    /**
     * Returns the collection of a specified key.
     * @param key The key of the collection.
     * @return The collection of the key or {@code null} if the given key was not registered.
     */
    public Collection<V> getCollection(K key){
        return elements.get(key);
    }

    public Collection<V> getCollectionOrEmpty(K key){
        return elements.getOrDefault(key, Collections.emptyList());
    }

    /**
     * Clears the collection for a given key.
     * @param key The key to clear the collection for.
     */
    public void clear(K key){
        if(!containsCollection(key))return;
        elements.get(key).clear();
    }

    /**
     * Clears all collections of all registered keys.
     * @param clearKeys Determines whether all collections should be removed.
     */
    public void clearAll(boolean clearKeys){
        if(clearKeys){
            elements.clear();
        }else{
            for(Collection<V> collection : elements.values()){
                collection.clear();
            }
        }
    }

    /**
     * Clears all collections of all registered keys.
     */
    public void clearAll(){
        clearAll(false);
    }

    /**
     * Checks whether a given key has a collection registered for it.
     * @param key The key of the collection.
     * @return {@code true} if the key has a collection registered, {@code false} otherwise.
     */
    public boolean containsCollection(K key){
        return elements.containsKey(key);
    }

    /**
     * @return {@code true} if the root map is empty, {@code false} otherwise.
     */
    public boolean isEmpty(){
        return elements.isEmpty();
    }

}
