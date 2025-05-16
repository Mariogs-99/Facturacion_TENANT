package sv.gov.cnr.factelectrcnrservice.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

}
