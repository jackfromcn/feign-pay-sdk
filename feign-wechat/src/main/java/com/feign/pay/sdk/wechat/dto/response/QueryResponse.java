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
public class QueryResponse {

    /**
     * 公众账号ID
     * 微信支付分配的公众账号ID（企业号corpid即为此appId）
     */
    @JacksonXmlProperty(localName = "appid")
    private String appId;

    /**
     * 商户号
     * 微信支付分配的商户号
     */
    @JacksonXmlProperty(localName = "mch_id")
    private String mchId;

    /**
     * 随机字符串
     * 随机字符串，不长于32位。推荐随机数生成算法
     */
    @JacksonXmlProperty(localName = "nonce_str")
    private String nonceStr;

    /**
     * 签名
     * 通过签名算法计算得出的签名值，详见签名生成算法
     */
    @JacksonXmlProperty(localName = "sign")
    private String sign;

    /**
     * 签名类型
     * HMAC-SHA256	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
     */
    @JacksonXmlProperty(localName = "sign_type")
    private String signType;

    /**
     * 业务结果
     * SUCCESS/FAIL
     */
    @JacksonXmlProperty(localName = "result_code")
    private String resultCode;

    /**
     * 错误代码
     * 详细参见错误列表
     */
    @JacksonXmlProperty(localName = "err_code")
    private String errCode;

    /**
     * 错误描述
     * 结果信息描述
     */
    @JacksonXmlProperty(localName = "err_code_des")
    private String errCodeDes;

    /**
     * 是否重调
     * 是否需要继续调用撤销，Y-需要，N-不需要
     */
    @JacksonXmlProperty(localName = "recall")
    private String recall;

}
