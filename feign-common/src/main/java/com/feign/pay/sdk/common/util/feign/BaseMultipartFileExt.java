package com.feign.pay.sdk.common.util.feign;

import java.util.LinkedHashMap;

/**
 *
 * @author wencheng
 * @date 2021/5/7
 */
public interface BaseMultipartFileExt {

    default LinkedHashMap<String, ?> extParamMap() {
        return null;
    };

}
