package com.rowland.lims_demo;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class LimsHelper {

    // Updates a stored entity given a list of fields and proposed updated entity.
    // Any null fields are skipped
    public static <T> T copyFields(T dst, T source, Class<T> clazz, String... fields) {
        for (String fieldName : fields) {
            Field field = ReflectionUtils.findField(clazz, fieldName);

            if (field != null) {
                ReflectionUtils.makeAccessible(field);
                Object sourceValue = ReflectionUtils.getField(field, source);
                if (sourceValue != null) {
                    ReflectionUtils.setField(field, dst, sourceValue);
                }
            }
        }

        return dst;
    }
}
