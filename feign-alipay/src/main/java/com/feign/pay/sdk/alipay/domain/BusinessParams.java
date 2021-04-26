package com.feign.pay.sdk.alipay.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/22
 */
@Data
public class BusinessParams {

    /**
     * 实际订单时间，在乘车码场景，传入的是用户刷码乘车时间
     */
    @JsonProperty("actual_order_time")
    private String actualOrderTime;

    /**
     * 校园卡编号
     */
    @JsonProperty("campus_card")
    private String campusCard;

    /**
     * 虚拟卡卡类型
     */
    @JsonProperty("card_type")
    private String cardType;

}
