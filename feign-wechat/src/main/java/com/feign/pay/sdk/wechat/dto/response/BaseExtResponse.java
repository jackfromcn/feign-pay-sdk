package com.feign.pay.sdk.wechat.dto.response;

import com.zm.pay.channel.proxy.wechat.spi.context.RespModelConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author wencheng
 * @date 2021/4/27
 */
@Slf4j
@Data
public abstract class BaseExtResponse extends BaseResponse implements Map<String, Object> {

    private Map<String, Object> extParam = new HashMap<>(16);

    protected String getString(String prefix, String... n) {
        String suffix = Arrays.asList(n).stream()
                .map(item -> "$" + item)
                .collect(Collectors.joining("_"));
        Object o = extParam.get(prefix + suffix);
        if (Objects.isNull(o)) {
            return null;
        }
        return o.toString();
    }

    protected Integer getInt(String prefix, String... n) {
        String value = getString(prefix, n);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Integer.valueOf(value);
    }

    @Override
    public int size() {
        return extParam.size();
    }

    @Override
    public boolean isEmpty() {
        return extParam.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return extParam.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return extParam.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return extParam.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        Field field = RespModelConfig.getField(this.getClass(), key);
        if (Objects.isNull(field)) {
            return extParam.put(key, value);
        }
        try {
            Class<?> fieldType = field.getType();
            if (fieldType.isAssignableFrom(Integer.class)) {
                field.set(this, Integer.valueOf(value.toString()));
            } else {
                field.set(this, value);
            }
            return extParam.put(field.getName(), value);
        } catch (Exception e) {
            log.error("BaseResponse[put] 异常, class:{}, field:{}, value:{}, e:", this.getClass(), key, value, e);
        }
        return value;
    }

    @Override
    public Object remove(Object key) {
        return extParam.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        extParam.putAll(m);
    }

    @Override
    public void clear() {
        extParam.clear();
    }

    @Override
    public Set<String> keySet() {
        return extParam.keySet();
    }

    @Override
    public Collection<Object> values() {
        return extParam.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return extParam.entrySet();
    }

}
