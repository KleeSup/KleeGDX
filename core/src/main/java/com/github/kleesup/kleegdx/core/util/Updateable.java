package com.github.kleesup.kleegdx.core.util;

/**
 * Interfaces useful for classes that require periodical updates such as entities or server clocks.
 * Uses a time <code>delta</code> which represents the time passed since last {@link #update(float)} call
 * in seconds.
 */
public interface Updateable {

    /** Empty updateable with no containing function. */
    Updateable EMPTY = new Updateable() {
        @Override
        public void update(float delta) {}
    };

    /** Inverted milliseconds to seconds. */
    float MILLIS_TO_SEC = 1f / 1000;

    /**
     * Updates this object with a given elapsed time.
     * @param delta The time since last update in seconds.
     */
    void update(float delta);

}
