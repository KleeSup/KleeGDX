package com.github.kleesup.kleegdx.core;

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

}
