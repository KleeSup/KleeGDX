package com.github.kleesup.kleegdx.core.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Implementation of {@link ExecutorService} that will execute any tasks instantly and on the thread it is
 * called from.
 */
public class CurrentThreadExecutor implements ExecutorService {

    /* -- Current thread execution -- */

    @Override
    public void execute(Runnable command) {
        command.run();
    }

    /* -- ExecutorService implementation -- */

    @Override
    public void shutdown() {}

    @Override
    public List<Runnable> shutdownNow() {
        return Collections.emptyList();
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        try {
            return new InstantFuture<>(task.call());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        task.run();
        return new InstantFuture<>(result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        task.run();
        return new InstantFuture<>(null);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        if(tasks == null || tasks.isEmpty())return Collections.emptyList();
        ArrayList<Future<T>> out = new ArrayList<>(tasks.size());
        for(Callable<T> task : tasks){
            out.add(submit(task));
        }
        return out;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return invokeAll(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) {
        for(Callable<T> task : tasks){
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        return invokeAny(tasks);
    }
}
