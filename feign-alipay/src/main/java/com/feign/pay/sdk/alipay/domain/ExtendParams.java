package com.feign.pay.sdk.alipay.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/22
 */
@Data
public class ExtendParams {

    /**
     * 卡类型
     */
    @JsonProperty("card_type")
    private String cardType;

    /**
     * 使用花呗分期要进行的分期数
     */
    @JsonProperty("hb_fq_num")
    private String hbFqNum;

    /**
     * 使用花呗分期需要卖家承担的手续费比例的百分值，传入100代表100%
     */
    @JsonProperty("hb_fq_seller_percent")
    private String hbFqSellerPercent;

    /**
     * 行业数据回流信息, 详见：地铁支付接口参数补充说明
     */
    @JsonProperty("industry_reflux_info")
    private String industryRefluxInfo;

    /**
     * 系统商编号
     该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID
     */
    @JsonProperty("sys_service_provider_id")
    private String sysServiceProviderId;

}
