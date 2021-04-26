package com.feign.pay.sdk.alipay.domain;

import com.alipay.api.domain.SettleDetailInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @author wencheng
 * @date 2021/4/22
 */
@Data
public class SettleInfo {

    /**
     * 结算详细信息，json数组，目前只支持一条。
     */
    @JsonProperty("settle_detail_infos")
    private List<SettleDetailInfo> settleDetailInfos;

    /**
     * 该笔订单的超期自动确认结算时间，到达期限后，将自动确认结算。此字段只在签约账期结算模式时有效。取值范围：1d～365d。d-天。 该参数数值不接受小数点。
     */
    @JsonProperty("settle_period_time")
    private String settlePeriodTime;

}
