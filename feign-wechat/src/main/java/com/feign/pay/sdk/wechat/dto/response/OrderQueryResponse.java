package com.feign.pay.sdk.wechat.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/20
 */
@Data
@JacksonXmlRootElement(localName = "xml")
public class OrderQueryResponse extends BaseExtResponse {

    /**
     * 设备号
     * 调用接口提交的终端设备号
     */
    @JacksonXmlProperty(localName = "device_info")
    private String deviceInfo;

    /**
     * 用户标识
     * 用户在商户appid 下的唯一标识
     */
    @JacksonXmlProperty(localName = "openid")
    private String openid;

    /**
     * 是否关注公众账号
     * 用户是否关注公众账号，仅在公众账号类型支付有效，取值范围：Y或N;Y-关注;N-未关注
     */
    @JacksonXmlProperty(localName = "is_subscribe")
    private String isSubscribe;

    /**
     * 交易类型
     * MICROPAY 付款码支付
     */
    @JacksonXmlProperty(localName = "trade_type")
    private String tradeType;

    /**
     * 交易状态
     * SUCCESS--支付成功
     * REFUND--转入退款
     * NOTPAY--未支付
     * CLOSED--已关闭
     * REVOKED--已撤销(刷卡支付)
     * USERPAYING--用户支付中
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     * ACCEPT--已接收，等待扣款
     * 支付状态机请见下单API页面
     */
    @JacksonXmlProperty(localName = "trade_state")
    private String tradeState;

    /**
     * 付款银行
     * 银行类型，采用字符串类型的银行标识，详见银行类型
     */
    @JacksonXmlProperty(localName = "bank_type")
    private String bankType;

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
     * 现金支付货币类型
     * 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @JacksonXmlProperty(localName = "cash_fee_type")
    private String cashFeeType;

    /**
     * 代金券金额
     * “代金券”金额<=订单金额，订单金额-“代金券”金额=现金支付金额，详见支付金额
     */
    @JacksonXmlProperty(localName = "coupon_fee")
    private Integer couponFee;

    /**
     * 代金券使用数量
     */
    @JacksonXmlProperty(localName = "coupon_count")
    private Integer couponCount;

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
     * 代金券ID
     * 代金券ID, $n为下标，从0开始编号
     * @param suffix
     */
    public String couponId$N(String suffix) {
        return getString("coupon_id_", suffix);
    }

    /**
     * 单个代金券支付金额, $n为下标，从0开始编号
     * @param suffix
     */
    public String couponFee$N(String suffix) {
        return getString("coupon_fee_", suffix);
    }

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
     * 附加数据
     * 说明	附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
     */
    @JacksonXmlProperty(localName = "attach")
    private String attach;

    /**
     * 支付完成时间
     * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。详见时间规则
     */
    @JacksonXmlProperty(localName = "time_end")
    private String timeEnd;

    /**
     * 交易状态描述
     * 对当前查询订单状态的描述和下一步操作的指引
     */
    @JacksonXmlProperty(localName = "trade_state_desc")
    private String tradeStateDesc;
}
