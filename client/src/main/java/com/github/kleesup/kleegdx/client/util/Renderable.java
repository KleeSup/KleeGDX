package com.github.kleesup.kleegdx.client.util;

/**
 * Interfaces for classes that can be rendered or render some sort of graphics.
 */
public interface Renderable {

    /**
     * Renders needed graphics.
     * @param delta The delta time since the last render.
     */
    void render(float delta);

}
