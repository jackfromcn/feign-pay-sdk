package com.feign.pay.sdk.alipay.spi.context;

import lombok.Data;

/**
 * 请求接口。
 *
 * @author carver.gu
 * @since 1.0, Sep 12, 2009
 */
@Data
public class AlipayRequest {

    /**
     * 得到当前接口的版本
     *
     * @return API版本
     */
    private String apiVersion;

    /**
     * 获取终端类型
     *
     * @return 终端类型
     */
    private String terminalType;

    /**
     * 获取终端信息
     *
     * @return 终端信息
     */
    private String terminalInfo;

    /**
     * 获取产品码
     *
     * @return 产品码
     */
    private String prodCode;

    /**
     * 返回通知地址
     *
     * @return
     */
    private String notifyUrl;

    /**
     * 返回回跳地址
     *
     * @return
     */
    private String returnUrl;

    /**
     * 判断是否需要加密
     *
     * @return
     */
    private boolean needEncrypt;
}
