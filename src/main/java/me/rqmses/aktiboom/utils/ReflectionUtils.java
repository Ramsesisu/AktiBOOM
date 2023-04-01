package me.rqmses.aktiboom.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

    @SuppressWarnings("unchecked")
    public static <T, V> T getValue(V object, Class<T> type) throws IllegalAccessException {

        Field field = getField(object.getClass(), type);
        if (field == null)
            return null;

        return (T) field.get(object);
    }

    public static Field getField(Class<?> clazz, Class<?> type) {
        for (Field declaredField : clazz.getDeclaredFields()) {
            if (declaredField.getType().equals(type)) {
                makeAccessible(declaredField);
                return declaredField;
            }
        }

        if (clazz.getSuperclass() != null) {
            return getField(clazz.getSuperclass(), type);
        }

        return null;
    }

    public static void makeAccessible(Field field) {
        field.setAccessible(true);

        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}