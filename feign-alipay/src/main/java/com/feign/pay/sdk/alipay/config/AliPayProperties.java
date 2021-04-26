package com.feign.pay.sdk.alipay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author wencheng
 * @date 2021/4/22
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayProperties {

    /**
     * 私钥
     */
    private String privateKey;
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * appId
     */
    private String appId;

}
