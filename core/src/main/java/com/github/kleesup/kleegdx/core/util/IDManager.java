package com.github.kleesup.kleegdx.core.util;

import java.util.UUID;

/**
 * Interface that can be implemented for classes that use {@link UUID} for some sort of internal ID management.
 */
public interface IDManager {

    default UUID generateNew(Object o){
        UUID id;
        do{
            id = UUID.randomUUID();
        }while (isTaken(id));
        return id;
    }

    boolean isTaken(UUID id);



}
