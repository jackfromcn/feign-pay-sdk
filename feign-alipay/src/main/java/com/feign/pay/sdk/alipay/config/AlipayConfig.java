package com.feign.pay.sdk.alipay.config;

import com.feign.pay.sdk.alipay.spi.context.AlipayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author wencheng
 * @date 2021/4/26
 */
@Configuration
public class AlipayConfig {

    @Autowired
    private AliPayProperties aliPayProperties;

    @Bean
    public AlipayClient alipayClient() {
        return new AlipayClient(aliPayProperties.getAppId(), aliPayProperties.getPrivateKey(), "json", "utf-8", aliPayProperties.getPublicKey(), "RSA2");
    }

}
