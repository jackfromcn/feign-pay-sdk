package com.feign.pay.sdk.alipay.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/22
 */
@Data
public class SubMerchant {

    /**
     * 间连受理商户的支付宝商户编号，通过间连商户入驻后得到。间连业务下必传，并且需要按规范传递受理商户编号。
     */
    @JsonProperty("merchant_id")
    private String merchantId;

    /**
     * 商户id类型，
     */
    @JsonProperty("merchant_type")
    private String merchantType;

}
