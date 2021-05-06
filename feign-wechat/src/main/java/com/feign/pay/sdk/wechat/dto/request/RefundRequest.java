package com.feign.pay.sdk.wechat.dto.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/26
 */
@Data
@JacksonXmlRootElement(localName = "xml")
public class RefundRequest extends BaseRequest {

    /**
     * 微信订单号
     * 微信的订单号，建议优先使用
     */
    @JacksonXmlProperty(localName = "transaction_id")
    private String transactionId;

    /**
     * 商户订单号
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。 详见商户订单号
     */
    @JacksonXmlProperty(localName = "out_trade_no")
    private String outTradeNo;

    /**
     * 商户退款单号
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔
     */
    @JacksonXmlProperty(localName = "out_refund_no")
    private String outRefundNo;

    /**
     * 标价金额
     * 订单总金额，单位为分，只能为整数，详见支付金额
     */
    @JacksonXmlProperty(localName = "total_fee")
    private Integer totalFee;

    /**
     * 退款总金额,单位为分,可以做部分退款
     */
    @JacksonXmlProperty(localName = "refund_fee")
    private Integer refundFee;

    /**
     * 退款货币种类
     * 退款货币类型，需与支付一致，或者不填。符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @JacksonXmlProperty(localName = "refund_fee_type")
    private String refundFeeType;

    /**
     * 退款原因
     * 若商户传入，会在下发给用户的退款消息中体现退款原因
     * 注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因
     */
    @JacksonXmlProperty(localName = "refund_desc")
    private String refundDesc;

    /**
     * 退款资金来源
     * 仅针对老资金流商户使用
     * REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
     * REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款
     */
    @JacksonXmlProperty(localName = "refund_account")
    private String refundAccount;

    /**
     * 退款结果通知url
     * 异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数
     * 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效。
     */
    @JacksonXmlProperty(localName = "notify_url")
    private String notifyUrl;


}
