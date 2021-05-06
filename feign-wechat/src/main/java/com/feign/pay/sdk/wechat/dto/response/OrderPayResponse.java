package com.feign.pay.sdk.wechat.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/29
 */
@Data
@JacksonXmlRootElement(localName = "xml")
public class OrderPayResponse extends BaseResponse {

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
     * 付款银行
     * 银行类型，采用字符串类型的银行标识，详见银行类型
     */
    @JacksonXmlProperty(localName = "bank_type")
    private String bankType;

    /**
     * 货币类型
     * 符合ISO 4217标准的三位字母代码，默认人民币：CNY，详见货币类型
     */
    @JacksonXmlProperty(localName = "fee_type")
    private String feeType;

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
     * 代金券金额
     * “代金券”金额<=订单金额，订单金额-“代金券”金额=现金支付金额，详见支付金额
     */
    @JacksonXmlProperty(localName = "coupon_fee")
    private Integer couponFee;

    /**
     * 现金支付货币类型
     * 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @JacksonXmlProperty(localName = "cash_fee_type")
    private String cashFeeType;

    /**
     * 现金支付金额
     * 订单现金支付金额，详见支付金额
     */
    @JacksonXmlProperty(localName = "cash_fee")
    private Integer cashFee;

    /**
     * 微信支付订单号
     * 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @JacksonXmlProperty(localName = "transaction_id")
    private String transactionId;

    /**
     * 商户订单号
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。
     */
    @JacksonXmlProperty(localName = "out_trade_no")
    private String outTradeNo;

    /**
     * 附加数据
     * 商家数据包，原样返回
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
     * 营销详情
     * 新增返回，单品优惠功能字段，需要接入请见详细说明
     */
    @JacksonXmlProperty(localName = "promotion_detail")
    private String promotionDetail;

}
