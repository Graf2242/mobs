package graf.server.Utils.Reflection;

import java.lang.reflect.Field;

public class ReflectionHelper {
    public static Object createInstance(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setField(Object o, String fieldName, String value) {
        Field field = null;
        try {
            String name = o.getClass().getName();
            field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (field.getType().equals(String.class)) {
                field.set(o, value);
            } else if (field.getType().equals(String.class)) {
                field.set(o, Integer.parseInt(value));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null) {
                field.setAccessible(false);
            }
        }
    }

    public static Field[] getFields(Object o) {
        return o.getClass().getDeclaredFields();
    }

    public static Object getFieldValue(Object o, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object result = field.get(o);
        field.setAccessible(false);
        return result;
    }
}
