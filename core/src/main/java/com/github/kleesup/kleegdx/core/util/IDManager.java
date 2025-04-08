package com.github.kleesup.kleegdx.core.util;

import java.util.UUID;

/**
 * Interface that can be implemented for classes that use {@link UUID} for some sort of internal ID management.
 */
public interface IDManager {

    /**
     * Generates a new {@link UUID} instance for an object.
     * @param o The object ot generate a unique id for.
     * @return The newly created id.
     */
    default UUID generateNew(Object o){
        UUID id;
        do{
            id = UUID.randomUUID();
        }while (isTaken(id));
        return id;
    }

    /**
     * Checks whether an uuid is already assigned to an object.
     * @param id The id to check for.
     * @return {@code true} if the id is already registered, {@code false} otherwise.
     */
    boolean isTaken(UUID id);



}
