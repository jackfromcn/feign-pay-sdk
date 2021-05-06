package com.feign.pay.sdk.wechat.spi.context;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.feign.pay.sdk.common.util.AnnotationScanner;
import com.feign.pay.sdk.wechat.dto.response.BaseResponse;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author wencheng
 * @date 2021/4/27
 */
@Slf4j
@Component
public class RespModelConfig {

    @Autowired
    private AnnotationScanner scanner;

    private static final Table<Class, String, Field> FIELD_TABLE = HashBasedTable.create();

    @PostConstruct
    public void init() {
        try {
            List<Class> result = scanner.scanByAnnotation(JacksonXmlRootElement.class, "classpath*:com/feign/pay/sdk/wechat/**/*.class");
            for (Class clazz: result) {
                boolean isAbstract = Modifier.isAbstract(clazz.getModifiers());
                if (isAbstract) {
                    continue;
                }
                if (!BaseResponse.class.isAssignableFrom(clazz)) {
                    continue;
                }

                List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, JacksonXmlProperty.class);
                for (Field field: fields) {
                    JacksonXmlProperty annotation = field.getDeclaredAnnotation(JacksonXmlProperty.class);
                    if (Objects.isNull(annotation)) {
                        continue;
                    }
                    field.setAccessible(true);
                    FIELD_TABLE.put(clazz, annotation.localName(), field);
                }
            }
        } catch (Exception e) {
            log.error("RespModelConfig[init] 异常e:", e);
        }
    }

    public static Field getField(Class clazz, String fieldProperty) {
        return FIELD_TABLE.get(clazz, fieldProperty);
    }

}
