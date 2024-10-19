package com.github.kleesup.kleegdx.core.serialize;

public @interface SerializationTarget {

    Type target() default Type.NET;

    enum Type{
        NET,
        STORING,
        BOTH;
    }

}
