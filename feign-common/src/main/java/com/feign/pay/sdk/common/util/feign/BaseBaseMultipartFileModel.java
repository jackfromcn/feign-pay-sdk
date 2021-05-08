package com.feign.pay.sdk.common.util.feign;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.feign.pay.sdk.common.util.JsonUtil;
import lombok.Data;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;

/**
 *
 * @author wencheng
 * @date 2021/5/7
 */
@Data
public abstract class BaseBaseMultipartFileModel implements BaseMultipartFileExt {

    @JsonIgnore
    private MultipartFile file;

    public LinkedHashMap<String, ?> paramMap() {
        String json = JsonUtil.toJson(this);
        LinkedHashMap<String, Object> paramMap = JsonUtil.toObject(json, new TypeReference<LinkedHashMap<String, Object>>() {});
        for (String fieldName: paramMap.keySet()) {
            try {
                Object o = FieldUtils.readDeclaredField(this, fieldName, true);
                paramMap.put(fieldName, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        LinkedHashMap<String, ?> extParamMap = extParamMap();
        if (!CollectionUtils.isEmpty(extParamMap)) {
            paramMap.putAll(extParamMap);
        }
        paramMap.put(file.getName(), file);
        return paramMap;
    }

}
