package com.github.kleesup.kleegdx.client.ui;

/**
 * Classes implementing this interface denote that they should be resized whenever
 * {@link com.badlogic.gdx.ApplicationListener#resize(int, int)} was invoked.
 */
public interface Resizeable {

    /**
     * Resizes this classes graphical contents accordingly.
     * @param width The new screen-width.
     * @param height The new screen-height.
     */
    void resize(int width, int height);

}
