package com.github.kleesup.kleegdx.core.util;

import lombok.Getter;
import lombok.Setter;

/**
 * A simple class for managing cooldowns. Offers looping {@link #setLooping(boolean)} and methods such as
 * {@link #getTimesFinished()} or {@link #updateAndGetTimes(float)} to see how often this stopwatch finished until now.
 * This is especially useful for low {@link #max} time and therefore potentially multiple finishes per update.
 */
public class StopWatch {

    private float current, max, inv_max;
    @Getter
    @Setter
    private boolean looping;
    public StopWatch(float max){
        reset(max);
    }

    /**
     * Resets this stopwatch with a new max time.
     * @param newMax The new max time.
     */
    public void reset(float newMax){
        this.max = newMax;
        this.current = 0;
        this.inv_max = 1f / max;
    }

    /**
     * Resets this stopwatch but keeps the old max time.
     */
    public void reset(){
        reset(max);
    }

    /**
     * Update this stopwatch and returns if it is now finished.
     * Will also be restarted if {@link #looping} is {@code true}.
     * @param delta The delta time to update the stopwatch.
     * @return {@code true} if the stopwatch has finished (reached {@link #max}), {@code false} otherwise.
     */
    public boolean update(float delta){
        current += delta;
        boolean finished = current >= max;
        if(finished && looping)current = 0;
        return finished;
    }

    /**
     * Updates this stopwatch and returns how many times the stopwatch has finished in this time period.
     * This method is more useful for use cases where the {@link #max} is relatively low and the stopwatch will
     * finish very often and rather quickly or multiple times with one update.
     * @param delta The delta time to update the stopwatch.
     * @return The times this stopwatch finished with the new delta addition to the counter.
     */
    public int updateAndGetTimes(float delta){
        current += delta;
        int times = current >= max ? (int) (current * inv_max) : 0;
        if(times != 0 && looping)current = 0;
        return times;
    }

    public boolean isFinished(){
        return current >= max;
    }

    public int getTimesFinished(){
        return (int) (current * inv_max);
    }

}
