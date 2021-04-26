/*
 * Copyright 2016 uncle5.com All right reserved. This software is the
 * confidential and proprietary information of uncle5.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with uncle5.com .
 */
package com.feign.pay.sdk.common.util;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * bean转换工具
 * @author wencheng
 */
public class BeanConverter {

    public static MapperFacade MAPPER = new DefaultMapperFactory.Builder().build().getMapperFacade();

    private BeanConverter() {
    }

    /**
     * 将原对象转换为目标类型的对象
     *
     * @param sourceObject 原对象
     * @param targetClass  目标对象的类型对象
     * @param <T>          目标对象的类型
     * @return 目标对象
     */
    public static <T> T map(Object sourceObject, Class<T> targetClass) {
        return sourceObject == null ? null : MAPPER.map(sourceObject, targetClass);
    }

    /**
     * 将原对象的集合转换为目标类型的对象列表
     *
     * @param sourceObjects 原对象列表
     * @param targetClass   目标对象的类型对象
     * @param <T>           目标对象的类型
     * @return 目标对象列表
     */
    public static <T> List<T> asList(Iterable<?> sourceObjects, Class<T> targetClass) {
        if (sourceObjects == null) {
            return Collections.emptyList();
        }
        return MAPPER.mapAsList(sourceObjects, targetClass);
    }

    /**
     * 将原对象的集合转换为目标类型的对象Set
     *
     * @param sourceObjects 原对象列表
     * @param targetClass   目标对象的类型对象
     * @param <T>           目标对象的类型
     * @return 目标对象Set
     */
    public static <T> Set<T> asSet(Iterable<?> sourceObjects, Class<T> targetClass) {
        if (sourceObjects == null) {
            return Collections.emptySet();
        }
        return MAPPER.mapAsSet(sourceObjects, targetClass);
    }

}
