package com.feign.pay.sdk.wechat.v3.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wp
 * @date 2021/05/11
 * @description 文件上传
 */

@Data
public class WechatMediaUploadResponse implements Serializable {

    @JsonProperty(value = "media_id")
    private String mediaId;
}
