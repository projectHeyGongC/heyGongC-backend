package com.heygongc.global.utils;

public class EnumUtils {
    public static boolean hasNoEnumConstant(Class<? extends Enum<?>> enumClass, String value) {
        for (Enum<?> e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) {
                return false;
            }
        }
        return true;
    }

    public static <T extends Enum<T>> T getEnumConstant(Class<T> enumClass, String value) {
        for (T e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
