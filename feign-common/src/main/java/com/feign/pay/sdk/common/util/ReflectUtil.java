package com.feign.pay.sdk.common.util;

import java.lang.reflect.Field;
import java.util.*;

/**
 *
 * @author wencheng
 * @date 2021/4/23
 */
public class ReflectUtil {

    public static Field getField(Class clazz, String fieldName) {
        Map<String, Field> fieldMap = getAllFieldMap(clazz);
        return fieldMap.get(fieldName);
    }

    public static Map<String, Field> getAllFieldMap(Class clazz) {
        Map<String, Field> result = new HashMap<>(16);
        List<Field> allFields = getAllFields(clazz);
        for (Field field: allFields) {
            result.put(field.getName(), field);
        }
        return result;
    }

    public static List<Field> getAllFields(Class clazz) {
        List<Field> fields = new ArrayList<>(10);
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            //得到父类,然后赋给自己
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

}
