package com.feign.pay.sdk.wechat.v3.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wp
 * @date 2021/05/11
 * @description 微信进件返回使用
 */

@Data
public class WeChatApplyMentResponse implements Serializable {

    /**
     * 微信支付申请单号
     */
    @JsonProperty(value = "applyment_id")
    private Integer applyMentId;
}
