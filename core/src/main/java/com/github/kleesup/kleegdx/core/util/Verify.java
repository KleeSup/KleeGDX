package com.github.kleesup.kleegdx.core.util;

import com.github.kleesup.kleegdx.core.exception.InvalidCollectionException;

import java.util.Collection;

/**
 * Utility class for detecting invalid inputs or arguments.
 */
public final class Verify {
    private Verify(){}

    /**
     * Verifies if an object is not {@code null}. If it is indeed {@code null},
     * a {@link IllegalArgumentException} is being thrown.
     * @param obj The object to test for.
     * @param msg The message to print with the exception.
     * @throws IllegalArgumentException If the given argument is {@code null}.
     */
    public static void nonNullArg(Object obj, String msg){
        if(obj == null)throw new IllegalArgumentException(msg);
    }

    /**
     * Checks whether a given collection is seen as <i>valid</i>.
     * A collection considered valid is:
     * <l>
     *     <li>not {@code null}</li>
     *     <li>not empty</li>
     * </l>
     * @param col The collection to test for.
     * @param msg The message to print if an error is going to be thrown.
     */
    public static void collectionValid(Collection<?> col, String msg){
        if(col == null || col.isEmpty())throw new InvalidCollectionException(msg);
    }

    /* -- Arrays -- */
    /** See {@link #collectionValid(Collection, String)}. */
    public static <T> void arrayValid(T[] array, String msg){
        if(array == null || array.length == 0)throw new InvalidCollectionException(msg);
    }

    /**
     * Checks whether an array is not {@code null} and has a minimal length.
     * @param array The array to check for.
     * @param min The min length required.
     * @param msg The message to print if an error is going to be thrown.
     */
    public static <T> void arrayMinLength(T[] array, int min, String msg){
        if(array == null || array.length < min)throw new InvalidCollectionException(msg);
    }
    /** See {@link #arrayMinLength(Object[], int, String)}. */
    public static <T> void arrayMinLength(T[] array, int min){
        arrayMinLength(array,min,"Specified array is either null or smaller than "+min+"!");
    }

}
