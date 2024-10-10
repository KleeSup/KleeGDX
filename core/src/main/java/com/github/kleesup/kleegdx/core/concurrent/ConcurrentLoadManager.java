package com.github.kleesup.kleegdx.core.concurrent;

import com.badlogic.gdx.utils.Disposable;
import com.github.kleesup.kleegdx.core.util.Updateable;
import lombok.Setter;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * A concurrent object loader and holder which uses {@link ExecutorService} to load objects. Furthermore, it inherits
 * from {@link Updateable} because it internally holds the loaded objects and a queue receiving the last loading
 * tasks. This class is useful for objects that might need a longer time to be loaded (worlds, big files etc.)
 * because it puts them on different threads.
 */
public class ConcurrentLoadManager<Id, T> implements Updateable, Disposable {

    protected final IdentityHashMap<Id, T> loadedObjects = new IdentityHashMap<>();
    protected final IdentityHashMap<Id, Future<T>> loadingQueue = new IdentityHashMap<>();
    protected final ExecutorService service;
    @Setter
    protected Callable<T> defaultLoadFunction;
    public ConcurrentLoadManager(ExecutorService service) {
        this.service = service;
    }
    public ConcurrentLoadManager(ExecutorService service, Callable<T> defaultLoadFunction){
        this(service);
        this.defaultLoadFunction = defaultLoadFunction;
    }

    /**
     * Starts to load an object with an ID through a specified task.
     * @param id The id of the object.
     * @param loadTask The task to load the object.
     * @return A created future from which the object can be received when it is successfully loaded.
     */
    public Future<T> load(Id id, Callable<T> loadTask){
        if(loadTask == null)return null;
        Future<T> future = service.submit(loadTask);
        loadingQueue.put(id, future);
        return future;
    }

    /**
     * Does the same as {@link #load(Object, Callable)} but instead uses the default load function which can be set
     * via the setter.
     * @param id The ID of the object.
     * @return A created future from which the object can be received when it is successfully loaded.
     */
    public Future<T> load(Id id) {
        return load(id, defaultLoadFunction);
    }

    /**
     * Checks whether an objects id has already been loaded.
     * @param id The id to check for.
     * @return {@code true} if the object is already loaded, {@code false} otherwise.
     */
    public boolean isLoaded(Id id){
        return loadedObjects.containsKey(id);
    }

    /**
     * Unloads an object by a given id from the storage.
     * @param id The id of the object.
     * @return The unloaded object or {@code null} if there was none.
     */
    public T unload(Id id){
        return loadedObjects.remove(id);
    }

    @Override
    public void update(float delta) {
        if(loadingQueue.isEmpty())return;
        Iterator<Map.Entry<Id, Future<T>>> itr = loadingQueue.entrySet().iterator();
        while (itr.hasNext()){
            Map.Entry<Id, Future<T>> entry = itr.next();
            if(entry.getValue().isDone()){
                try {
                    loadedObjects.put(entry.getKey(), entry.getValue().get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
                itr.remove();
            }
        }
    }

    @Override
    public void dispose() {
        service.shutdownNow();
    }

}
