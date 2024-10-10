package com.github.kleesup.kleegdx.core.util;

/**
 * Classes implementing this interface have some sort of data that needs to be saved into a file etc. before
 * disposing or at given intervals.
 */
public interface Saveable {

    /**
     * Saves the content of implementing class.
     */
    void save();

}
