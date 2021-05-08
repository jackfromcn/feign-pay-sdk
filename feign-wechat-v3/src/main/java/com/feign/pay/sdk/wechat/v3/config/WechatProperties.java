package com.feign.pay.sdk.wechat.v3.config;

import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/25
 */
@Data
public class WechatProperties {

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 证书地址
     * v3: 对应 privateKey(商户私钥) 的下载地址
     * v1: 证书地址
     */
    private String certStreamUrl;

    /**
     * 商户证书序列号
     */
    private String mchSerialNo;

    /**
     * api密钥
     */
    private String apiV3Key;

}
