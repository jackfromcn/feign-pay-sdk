package com.feign.pay.sdk.wechat.dto.response;

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
public class RefundResponse extends BaseExtResponse {

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
     * 微信退款单号
     * 微信生成的退款单号，在申请退款接口有返回
     */
    @JacksonXmlProperty(localName = "refund_id")
    private String refundId;

    /**
     * 退款总金额,单位为分,可以做部分退款
     */
    @JacksonXmlProperty(localName = "refund_fee")
    private Integer refundFee;

    /**
     * 应结退款金额
     * 去掉非充值代金券退款金额后的退款金额，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     */
    @JacksonXmlProperty(localName = "settlement_refund_fee")
    private Integer settlementRefundFee;

    /**
     * 标价金额
     * 订单总金额，单位为分，只能为整数，详见支付金额
     */
    @JacksonXmlProperty(localName = "total_fee")
    private Integer totalFee;

    /**
     * 应结订单金额
     * 去掉非充值代金券金额后的订单总金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     */
    @JacksonXmlProperty(localName = "settlement_total_fee")
    private Integer settlementTotalFee;

    /**
     * 标价币种
     * CNY	订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @JacksonXmlProperty(localName = "fee_type")
    private String feeType;

    /**
     * 现金支付金额
     * 现金支付金额，单位为分，只能为整数，详见支付金额
     */
    @JacksonXmlProperty(localName = "cash_fee")
    private Integer cashFee;

    /**
     * 现金支付币种
     * CNY	货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @JacksonXmlProperty(localName = "cash_fee_type")
    private String cashFeeType;

    /**
     * 现金退款金额
     * 现金退款金额，单位为分，只能为整数，详见支付金额
     */
    @JacksonXmlProperty(localName = "cash_refund_fee")
    private Integer cashRefundFee;

    /**
     * 代金券类型	coupon_type_$n	否	String(8)	CASH
     * CASH--充值代金券
     * NO_CASH---非充值代金券
     * 订单使用代金券时有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
     * @param suffix
     */
    public String couponType$N(String suffix) {
        return getString("coupon_type_", suffix);
    }
    /**
     * 代金券退款总金额
     * 代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     */
    @JacksonXmlProperty(localName = "coupon_refund_fee")
    private Integer couponRefundFee;

    /**
     * 单个代金券退款金额	coupon_refund_fee_$n	否	Int	100	代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     * @param suffix
     */
    public Integer couponRefundFee$N(String suffix) {
        return getInt("coupon_refund_fee_", suffix);
    }

    /**
     * 退款代金券使用数量
     */
    @JacksonXmlProperty(localName = "coupon_refund_count")
    private Integer couponRefundCount;

    /**
     * 退款代金券ID	coupon_refund_id_$n	否	String(20)	10000 	退款代金券ID, $n为下标，从0开始编号
     * @param suffix
     */
    public String couponRefundId$N(String suffix) {
        return getString("coupon_refund_id_", suffix);
    }


}
