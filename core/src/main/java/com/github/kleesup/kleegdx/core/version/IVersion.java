package com.github.kleesup.kleegdx.core.version;

/**
 * Interface that can be implemented in some kind of context that represents project versions.
 * Implementation could be an enum holding release versions using
 * the {@link Enum#ordinal()} as {@link #get()} identifier.
 */
public interface IVersion extends Comparable<Integer> {

    /**
     * @return {@code true} if this version is the newest currently available, {@code false} otherwise.
     */
    boolean isNewest();

    /**
     * @return Gets the name of this version e.g. <code>1.23.5</code>.
     */
    String getName();

    /**
     * @return The numeral (actual) version. This could be represented by some static counter variable like an
     * {@link Enum#ordinal()}.
     */
    int get();

    /**
     * Checks whether this version is newer than a specified one.
     * @param other The version to check for.
     * @return {@code true} if <code>this</code> version is newer than the specified, {@code false} otherwise.
     */
    default boolean isNewerThan(int other){
        return get() > other;
    }
    /** See {@link #isNewerThan(int)}. */
    default boolean isNewerThan(IVersion other){
        return other == null || isNewerThan(other.get());
    }

    /**
     * Checks whether this version is older than a specified one.
     * @param other The version to check for.
     * @return {@code true} if <code>this</code> version is older than the specified, {@code false} otherwise.
     */
    default boolean isOlderThan(int other){
        return get() < other;
    }
    /** See {@link #isOlderThan(int)}. */
    default boolean isOlderThan(IVersion other){
        return other != null && isOlderThan(other.get());
    }

    /**
     * Compares two versions and returns the following:
     * <l>
     *     <li>{@code 0} if they are equal.</li>
     *     <li>{@code -1} if <code>this</code> version is older.</li>
     *     <li>{@code 1} if <code>this</code> version is newer.</li>
     * </l>
     * @param o the version to be compared.
     * @return The compared result.
     */
    @Override
    default int compareTo(Integer o) {
        return Integer.compare(get(),o);
    }
}
