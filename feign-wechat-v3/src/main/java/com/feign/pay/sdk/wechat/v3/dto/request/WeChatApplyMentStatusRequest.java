package com.feign.pay.sdk.wechat.v3.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * @author wp
 * @date 2021/05/11
 * @description 微信进件状态查询使用
 */

@Data
public class WeChatApplyMentStatusRequest {

    /**
     * 业务申请编号
     * 二选一
     */
    @JsonProperty(value = "business_code")
    private String businessCode;
    /**
     * 申请单号
     * 二选一
     */
    @JsonProperty(value = "applyment_id")
    private Long applyMentId;
}
