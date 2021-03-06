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
public class RefundQueryResponse extends BaseExtResponse {

    /**
     * 订单总退款次数
     * 订单总共已发生的部分退款次数，当请求参数传入offset后有返回
     */
    @JacksonXmlProperty(localName = "total_refund_count")
    private Integer totalRefundCount;

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
     * 订单金额
     * 订单总金额，单位为分，只能为整数，详见支付金额
     */
    @JacksonXmlProperty(localName = "total_fee")
    private Integer totalFee;

    /**
     * 应结订单金额
     * 当订单使用了免充值型优惠券后返回该参数，应结订单金额=订单金额-免充值优惠券金额。
     */
    @JacksonXmlProperty(localName = "settlement_total_fee")
    private Integer settlementTotalFee;

    /**
     * 货币类型
     * 符合ISO4217标准的三位字母代码，默认人民币：CNY，详见货币类型
     */
    @JacksonXmlProperty(localName = "fee_type")
    private String feeType;

    /**
     * 现金支付金额
     * 订单现金支付金额，详见支付金额
     */
    @JacksonXmlProperty(localName = "cash_fee")
    private Integer cashFee;

    /**
     * 退款笔数
     * 当前返回退款笔数
     */
    @JacksonXmlProperty(localName = "refund_count")
    private Integer refundCount;


    /**
     * 商户退款单号
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     * @param suffix
     */
    public String outRefundNo$N(String suffix) {
        return getString("out_refund_no_", suffix);
    }


    /**
     * 退款代金券ID	coupon_refund_id_$n	否	String(20)	10000 	退款代金券ID, $n为下标，从0开始编号
     * @param suffix
     */
    public String couponRefundId$N(String suffix) {
        return getString("coupon_refund_id_", suffix);
    }

    /**
     * 退款渠道
     * ORIGINAL—原路退款
     * BALANCE—退回到余额
     * OTHER_BALANCE—原账户异常退到其他余额账户
     * OTHER_BANKCARD—原银行卡异常退到其他银行卡
     * @param suffix
     */
    public String refundChannel$N(String suffix) {
        return getString("refund_channel_", suffix);
    }

    /**
     * 申请退款金额
     * 退款总金额,单位为分,可以做部分退款
     * @param suffix
     */
    public String refundFee$N(String suffix) {
        return getString("refund_fee_", suffix);
    }

    /**
     * 退款金额
     * 退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     * @param suffix
     */
    public String settlementRefundFee$N(String suffix) {
        return getString("settlement_refund_fee_", suffix);
    }

    /**
     * 代金券类型	coupon_type_$n
     * CASH--充值代金券
     * NO_CASH---非充值优惠券
     * 开通免充值券功能，并且订单使用了优惠券后有返回（取值：CASH、NO_CASH）。$n为下标,$m为下标,从0开始编号，举例：coupon_type_$0_$1
     * @param n
     * @param m
     */
    public String couponType$N$M(String n, String m) {
        return getString("coupon_type_", n, m);
    }

    /**
     * 单个代金券退款金额	coupon_refund_fee_$n	否	Int	100	代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     * @param suffix
     */
    public Integer couponRefundFee$N(String suffix) {
        return getInt("coupon_refund_fee_", suffix);
    }

    /**
     * 单个代金券退款金额	coupon_refund_fee_$n	否	Int	100	代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     * @param suffix
     */
    public Integer couponRefundCount$N(String suffix) {
        return getInt("coupon_refund_count_", suffix);
    }

    /**
     * 退款代金券ID
     * 退款代金券ID, $n为下标，$m为下标，从0开始编号
     * @param n
     * @param m
     */
    public String couponRefundId$N$M(String n, String m) {
        return getString("coupon_refund_id_", n, m);
    }

    /**
     * 单个代金券退款金额
     * 单个退款代金券支付金额, $n为下标，$m为下标，从0开始编号
     * @param n
     * @param m
     */
    public Integer couponRefundFee$N$M(String n, String m) {
        return getInt("coupon_refund_fee_", n, m);
    }

    /**
     * 退款状态
     * 退款状态：
     * SUCCESS—退款成功
     * REFUNDCLOSE—退款关闭。
     * PROCESSING—退款处理中
     * CHANGE—退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。$n为下标，从0开始编号。
     * @param n
     */
    public String refundStatus$N(String n) {
        return getString("refund_status_", n);
    }


    /**
     * 退款资金来源
     * REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款/基本账户
     * REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款
     * $n为下标，从0开始编号。
     * @param n
     */
    public String refundAccount$N(String n) {
        return getString("refund_account_", n);
    }

    /**
     * 退款入账账户
     * 取当前退款单的退款入账方
     * 1）退回银行卡：{银行名称}{卡类型}{卡尾号}
     * 2）退回支付用户零钱:支付用户零钱
     * 3）退还商户:
     *  商户基本账户
     *  商户结算银行账户
     * 4）退回支付用户零钱通:支付用户零钱通
     * @param n
     */
    public String refundRecvAccout$N(String n) {
        return getString("refund_recv_accout_", n);
    }

    /**
     * 退款成功时间
     * 退款成功时间，当退款状态为退款成功时有返回。$n为下标，从0开始编号。
     * @param n
     */
    public String refundSuccessTime$N(String n) {
        return getString("refund_success_time_", n);
    }
}
