package com.feign.pay.sdk.wechat.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author wencheng
 * @date 2021/4/20
 */
@Data
@JacksonXmlRootElement(localName = "xml")
public class OrderPayRequest extends BaseRequest {

    /**
     * 商品详情
     * [{
     "goods_detail":[
     {
     "goods_id":"iphone6s_16G",
     "wxpay_goods_id":"1001",
     "goods_name":"iPhone6s 16G",
     "quantity":1,
     "price":528800,
     "goods_category":"123456",
     "body":"苹果手机"
     },
     {
     "goods_id":"iphone6s_32G",
     "wxpay_goods_id":"1002",
     "goods_name":"iPhone6s 32G",
     "quantity":1,
     "price":608800,
     "goods_category":"123789",
     "body":"苹果手机"
     }
     ]
     }]
     * 单品优惠功能字段，需要接入详见单品优惠详细说明
     */
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "detail")
    private String detail;

    /**
     * 附加数据
     * 说明	附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
     */
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "attach")
    private String attach;

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
     * 货币类型
     * 符合ISO4217标准的三位字母代码，默认人民币：CNY，详见货币类型
     */
    @JacksonXmlProperty(localName = "fee_type")
    private String feeType;

    /**
     * 终端IP
     * 支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
     */
    @JacksonXmlProperty(localName = "spbill_create_ip")
    private String spbillCreateIp;

    /**
     * 订单优惠标记
     * 订单优惠标记，代金券或立减优惠功能的参数，详见代金券或立减优惠
     */
    @JacksonXmlProperty(localName = "goods_tag")
    private String goodsTag;

    /**
     * 指定支付方式
     * no_credit--指定不能使用信用卡支付
     */
    @JacksonXmlProperty(localName = "limit_pay")
    private String limitPay;

    /**
     * 交易起始时间
     * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
     */
    @JacksonXmlProperty(localName = "time_start")
    private String timeStart;

    /**
     * 交易结束时间
     * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。注意：最短失效时间间隔需大于1分钟
     */
    @JacksonXmlProperty(localName = "time_expire")
    private String timeExpire;

    /**
     * 电子发票入口开放标识
     * 传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效
     */
    @JacksonXmlProperty(localName = "receipt")
    private String receipt;

    /**
     * 付款码
     * 扫码支付付款码，设备读取用户微信中的条码或者二维码信息
     * （注：用户付款码条形码规则：18位纯数字，以10、11、12、13、14、15开头）
     */
    @JacksonXmlProperty(localName = "auth_code")
    private String authCode;

    /**
     * 是否需要分账
     * Y-是，需要分账
     * N-否，不分账
     * 字母要求大写，不传默认不分账
     */
    @JacksonXmlProperty(localName = "profit_sharing")
    private String profitSharing;

    /**
     * 场景信息
     * 该字段用于上报场景信息，目前支持上报实际门店信息。该字段为JSON对象数据，对象格式为{"store_info":{"id": "门店ID","name": "名称","area_code": "编码","address": "地址" }} ，字段详细说明请点击行前的+展开
     */
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "scene_info")
    private String sceneInfo;

    @NoArgsConstructor
    @Data
    public static class Detail {

        @JsonProperty("goods_detail")
        private List<GoodsDetail> goodsDetail;

        @NoArgsConstructor
        @Data
        public static class GoodsDetail {
            /**
             * goods_id : iphone6s_16G
             * wxpay_goods_id : 1001
             * goods_name : iPhone6s 16G
             * quantity : 1
             * price : 528800
             * goods_category : 123456
             * body : 苹果手机
             */

            @JsonProperty("goods_id")
            private String goodsId;
            @JsonProperty("wxpay_goods_id")
            private String wxpayGoodsId;
            @JsonProperty("goods_name")
            private String goodsName;
            @JsonProperty("quantity")
            private int quantity;
            @JsonProperty("price")
            private int price;
            @JsonProperty("goods_category")
            private String goodsCategory;
            @JsonProperty("body")
            private String body;
        }
    }

    @Data
    public static class SceneInfo {
        /**
         * 门店id
         * 门店唯一标识
         */
        private String id;

        /**
         * 门店名称
         */
        private String name;

        /**
         * 门店行政区划码
         * 门店所在地行政区划码，详细见《最新县及县以上行政区划代码》
         */
        @JsonProperty("area_code")
        private String areaCode;

        /**
         * 门店详细地址
         */
        private String address;
    }

}
