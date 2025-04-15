package com.github.kleesup.kleegdx.client.ui;

import com.badlogic.gdx.utils.SnapshotArray;
import lombok.Getter;

/**
 * A very simple class managing multiple {@link Resizeable} objects by calling all {@link Resizeable#resize(int, int)}
 * methods whenever the own one is called.
 */
public class ResizeablePool implements Resizeable {

    @Getter
    private static ResizeablePool instance = new ResizeablePool();

    public static void setInstance(ResizeablePool instance) {
        if(instance==null)return;
        ResizeablePool.instance = instance;
    }

    // -- Internal code --

    private final SnapshotArray<Resizeable> resizeables = new SnapshotArray<>(
            true, 8, Resizeable.class);

    @Override
    public void resize(int width, int height) {
        if(resizeables.isEmpty())return;
        Resizeable[] item = resizeables.begin();
        for(int i = 0; i < resizeables.size; i++){
            item[0].resize(width,height);
        }
        resizeables.end();
    }

    /**
     * Adds one or multiple objects of {@link Resizeable} to the pool.
     * @param resizeables The resizeables to add.
     */
    public void add(Resizeable... resizeables){
        this.resizeables.addAll(resizeables);
    }

    /**
     * Removes a {@link Resizeable} object from the pool.
     * @param resizeable The resizeable to remove.
     */
    public void remove(Resizeable resizeable){
        this.resizeables.removeValue(resizeable, true);
    }

}
