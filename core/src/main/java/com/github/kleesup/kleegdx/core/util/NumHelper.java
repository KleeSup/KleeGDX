package com.github.kleesup.kleegdx.core.util;

import java.util.Optional;

/**
 * Helper class for number contexts.
 */
public final class NumHelper {
    private NumHelper(){}

    public static Optional<Byte> isByte(String str){
        try {
            return Optional.of(Byte.parseByte(str));
        }catch (NumberFormatException e){
            return Optional.empty();
        }
    }

    public static Optional<Short> isShort(String str){
        try {
            return Optional.of(Short.parseShort(str));
        }catch (NumberFormatException e){
            return Optional.empty();
        }
    }

    public static Optional<Integer> isInteger(String str){
        try {
            return Optional.of(Integer.parseInt(str));
        }catch (NumberFormatException e){
            return Optional.empty();
        }
    }

    public static Optional<Long> isLong(String str){
        try {
            return Optional.of(Long.parseLong(str));
        }catch (NumberFormatException e){
            return Optional.empty();
        }
    }

    public static Optional<Float> isFloat(String str){
        try {
            return Optional.of(Float.parseFloat(str));
        }catch (NumberFormatException e){
            return Optional.empty();
        }
    }

    public Optional<Double> isDouble(String str){
        try {
            return Optional.of(Double.parseDouble(str));
        }catch (NumberFormatException e){
            return Optional.empty();
        }
    }

}
