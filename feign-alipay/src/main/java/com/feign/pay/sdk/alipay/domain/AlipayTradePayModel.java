package com.feign.pay.sdk.alipay.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @author wencheng
 * @date 2021/4/22
 */
@Data
public class AlipayTradePayModel {


    /**
     * 支付模式类型,若值为ENJOY_PAY_V2表示当前交易允许走先享后付2.0垫资
     */
    @JsonProperty("advance_payment_type")
    private String advancePaymentType;

    /**
     * 代扣业务需要传入协议相关信息
     */
    @JsonProperty("agreement_params")
    private AgreementParams agreementParams;

    /**
     * 支付宝的店铺编号
     */
    @JsonProperty("alipay_store_id")
    private String alipayStoreId;

    /**
     * 支付授权码，25~30开头的长度为16~24位的数字，实际字符串长度以开发者获取的付款码长度为准
     */
    @JsonProperty("auth_code")
    private String authCode;

    /**
     * 预授权确认模式，授权转交易请求中传入，适用于预授权转交易业务使用，目前只支持PRE_AUTH(预授权产品码)
     COMPLETE：转交易支付完成结束预授权，解冻剩余金额; NOT_COMPLETE：转交易支付完成不结束预授权，不解冻剩余金额
     */
    @JsonProperty("auth_confirm_mode")
    private String authConfirmMode;

    /**
     * 预授权号，预授权转交易请求中传入，适用于预授权转交易业务使用，目前只支持FUND_TRADE_FAST_PAY（资金订单即时到帐交易）、境外预授权产品（OVERSEAS_AUTH_PAY）两个产品。
     */
    @JsonProperty("auth_no")
    private String authNo;

    /**
     * 订单描述
     */
    @JsonProperty("body")
    private String body;

    /**
     * 商户传入业务信息，具体值要和支付宝约定，应用于安全，营销等参数直传场景，格式为json格式
     */
    @JsonProperty("business_params")
    private BusinessParams businessParams;

    /**
     * 买家的支付宝用户 id，如果为空，会从传入的码值信息中获取买家 ID
     */
    @JsonProperty("buyer_id")
    private String buyerId;

    /**
     * 禁用支付渠道,多个渠道以逗号分割，如同时禁用信用支付类型和积分，则disable_pay_channels="credit_group,point"
     */
    @JsonProperty("disable_pay_channels")
    private String disablePayChannels;

    /**
     * 参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]。
     如果该值未传入，但传入了【订单总金额】和【不可打折金额】，则该值默认为【订单总金额】-【不可打折金额】
     */
    @JsonProperty("discountable_amount")
    private String discountableAmount;

    /**
     * 外部指定买家
     */
    @JsonProperty("ext_user_info")
    private ExtUserInfo extUserInfo;

    /**
     * 业务扩展参数
     */
    @JsonProperty("extend_params")
    private ExtendParams extendParams;

    /**
     * 订单包含的商品列表信息，json格式，其它说明详见商品明细说明
     */
    @JsonProperty("goods_detail")
    private List<GoodsDetail> goodsDetail;

    /**
     * 是否异步支付，传入true时，表明本次期望走异步支付，会先将支付请求受理下来，再异步推进。商户可以通过交易的异步通知或者轮询交易的状态来确定最终的交易结果
     */
    @JsonProperty("is_async_pay")
    private Boolean isAsyncPay;

    /**
     * 商户的原始订单号
     */
    @JsonProperty("merchant_order_no")
    private String merchantOrderNo;

    /**
     * 商户操作员编号
     */
    @JsonProperty("operator_id")
    private String operatorId;

    /**
     * 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /**
     * 销售产品码
     */
    @JsonProperty("product_code")
    private String productCode;

    /**
     * 优惠明细参数，通过此属性补充营销参数
     */
    @JsonProperty("promo_params")
    private PromoParam promoParams;

    /**
     * 返回查询选项，商户通过上送该参数来定制同步需要额外返回的信息字段，数组格式。如：["fund_bill_list","voucher_detail_list","discount_goods_detail"]
     */
    @JsonProperty("query_options")
    private List<String> queryOptions;

    /**
     * 收单机构(例如银行）的标识，填写该机构在支付宝的pid。只在机构间联场景下传递该值。
     */
    @JsonProperty("request_org_pid")
    private String requestOrgPid;

    /**
     * 描述分账信息，json格式，其它说明详见分账说明
     */
    @JsonProperty("royalty_info")
    private RoyaltyInfo royaltyInfo;

    /**
     * 支付场景
     条码支付，取值：bar_code
     声波支付，取值：wave_code
     */
    @JsonProperty("scene")
    private String scene;

    /**
     * 如果该值为空，则默认为商户签约账号对应的支付宝用户ID
     */
    @JsonProperty("seller_id")
    private String sellerId;

    /**
     * 商户指定的结算币种，支持英镑：GBP、港币：HKD、美元：USD、新加坡元：SGD、日元：JPY、加拿大元：CAD、澳元：AUD、欧元：EUR、新西兰元：NZD、韩元：KRW、泰铢：THB、瑞士法郎：CHF、瑞典克朗：SEK、丹麦克朗：DKK、挪威克朗：NOK、马来西亚林吉特：MYR、印尼卢比：IDR、菲律宾比索：PHP、毛里求斯卢比：MUR、以色列新谢克尔：ILS、斯里兰卡卢比：LKR、俄罗斯卢布：RUB、阿联酋迪拉姆：AED、捷克克朗：CZK、南非兰特：ZAR、人民币：CNY
     */
    @JsonProperty("settle_currency")
    private String settleCurrency;

    /**
     * 描述结算信息，json格式，详见结算参数说明
     */
    @JsonProperty("settle_info")
    private SettleInfo settleInfo;

    /**
     * 商户门店编号
     */
    @JsonProperty("store_id")
    private String storeId;

    /**
     * 间连受理商户信息体，当前只对特殊银行机构特定场景下使用此字段
     */
    @JsonProperty("sub_merchant")
    private SubMerchant subMerchant;

    /**
     * 订单标题
     */
    @JsonProperty("subject")
    private String subject;

    /**
     * 商户机具终端编号
     */
    @JsonProperty("terminal_id")
    private String terminalId;

    /**
     * 商户传入终端设备相关信息，具体值要和支付宝约定
     */
    @JsonProperty("terminal_params")
    private String terminalParams;

    /**
     * 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m
     */
    @JsonProperty("timeout_express")
    private String timeoutExpress;

    /**
     * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
     如果同时传入【可打折金额】和【不可打折金额】，该参数可以不用传入；
     如果同时传入了【可打折金额】，【不可打折金额】，【订单总金额】三者，则必须满足如下条件：【订单总金额】=【可打折金额】+【不可打折金额】
     */
    @JsonProperty("total_amount")
    private String totalAmount;

    /**
     * 标价币种,  total_amount 对应的币种单位。支持英镑：GBP、港币：HKD、美元：USD、新加坡元：SGD、日元：JPY、加拿大元：CAD、澳元：AUD、欧元：EUR、新西兰元：NZD、韩元：KRW、泰铢：THB、瑞士法郎：CHF、瑞典克朗：SEK、丹麦克朗：DKK、挪威克朗：NOK、马来西亚林吉特：MYR、印尼卢比：IDR、菲律宾比索：PHP、毛里求斯卢比：MUR、以色列新谢克尔：ILS、斯里兰卡卢比：LKR、俄罗斯卢布：RUB、阿联酋迪拉姆：AED、捷克克朗：CZK、南非兰特：ZAR、人民币：CNY
     */
    @JsonProperty("trans_currency")
    private String transCurrency;

    /**
     * 不参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]。如果该值未传入，但传入了【订单总金额】和【可打折金额】，则该值默认为【订单总金额】-【可打折金额】
     */
    @JsonProperty("undiscountable_amount")
    private String undiscountableAmount;

}
