package com.feign.pay.sdk.alipay.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/22
 */
@Data
public class AgreementParams {

    /**
     * 支付宝系统中用以唯一标识用户签约记录的编号（用户签约成功后的协议号 ）
     */
    @JsonProperty("agreement_no")
    private String agreementNo;

    /**
     * 鉴权申请token，其格式和内容，由支付宝定义。在需要做支付鉴权校验时，该参数不能为空。
     */
    @JsonProperty("apply_token")
    private String applyToken;

    /**
     * 鉴权确认码，在需要做支付鉴权校验时，该参数不能为空
     */
    @JsonProperty("auth_confirm_no")
    private String authConfirmNo;
    
}
