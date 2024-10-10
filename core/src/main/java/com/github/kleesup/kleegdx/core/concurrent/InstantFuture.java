package com.github.kleesup.kleegdx.core.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * An implementation of {@link Future} which has its object instantly and
 * always available through its constructor. This object is static and will therefore not change.
 * <p>The methods {@link #get()} and {@link #get(long, TimeUnit)} will always return the object set by creation.</p>
 * <p>The method {@link #isDone()} will always return {@code true}.</p>
 */
public class InstantFuture<V> implements Future<V> {

    private final V value;
    public InstantFuture(V value){
        this.value = value;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public V get() {
        return value;
    }

    @Override
    public V get(long timeout, TimeUnit unit) {
        return value;
    }
}
