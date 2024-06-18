package com.fithub.workout_service.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {

    public static boolean isNullOrEmpty(String value) {
        return value == null || "".equals(value);
    }

    public static boolean isNullOrEmpty(Character value) {
        return value == null || value.equals(' ');
    }

    public static boolean isNullOrEmpty(Map value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNullOrEmpty(List value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNullOrEmpty(Set value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNullOrEmpty(Object[] value) {
        return value == null || value.length == 0;
    }

    public static boolean isNullOrVazia(List<Object> listObject) {
        return listObject == null || listObject.isEmpty();
    }

    public static boolean isNullOrZero(Integer value) {
        return value == null || value == 0;
    }

    public static boolean isNullOrZero(BigDecimal value) {
        return value == null || (value.compareTo(BigDecimal.ZERO) != 0);
    }

    public static boolean isNullOrZero(Double value) {
        return value == null || value == 0;
    }

    public static boolean isNullOrZero(Long value) {
        return value == null || value == 0;
    }
}
