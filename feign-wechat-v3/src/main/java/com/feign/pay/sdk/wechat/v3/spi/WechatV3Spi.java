package com.feign.pay.sdk.wechat.v3.spi;

import com.feign.pay.sdk.common.util.feign.AbstractConfig;
import com.feign.pay.sdk.common.util.feign.BaseBaseMultipartFileModel;
import com.feign.pay.sdk.common.util.feign.FormHttpMessageConverter;
import com.feign.pay.sdk.wechat.v3.config.WechatProperties;
import com.feign.pay.sdk.wechat.v3.spi.context.V3Context;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import feign.Logger;
import feign.RequestInterceptor;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.PrivateKey;
import java.util.Collections;
import java.util.List;

/**
 * @seehttp://coupon-center.zvcms.com/doc.html
 * @author wencheng
 * @date 2020/12/22
 */
@FeignClient(name = "wechatV3Spi",
        url = "https://api.mch.weixin.qq.com",
        configuration = WechatV3Spi.Config.class
)
public interface WechatV3Spi {

    /**
     * 付款码支付
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_10&index=1
     * @param model
     * @return
     */
    @PostMapping(value = "/v3/merchant/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String upload(BaseBaseMultipartFileModel model);

    class Config extends AbstractConfig {

        @Value("${sand-box:false}")
        private Boolean isSandBox;

        @Autowired
        private WechatProperties properties;

        @Bean
        public RequestInterceptor requestInterceptor() {
            return requestTemplate -> {
                if (isSandBox) {
                    requestTemplate.insert(0, "/sandboxnew");
                }
            };
        }

        @Bean
        public Logger.Level feignLoggerLevel() {
            return Logger.Level.FULL;
        }

        @Override
        protected List<HttpMessageConverter<?>> getEncoderConverters() {
            FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
            return Collections.singletonList(formHttpMessageConverter);
        }

        @Override
        protected CloseableHttpClient getHttpClient() {
            try {
                PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
                int maxTotalConnection = 1000;
                int defaultMaxPerRoute = 20;
                connectionManager.setMaxTotal(maxTotalConnection);
                connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

                SocketConfig socketConfig = SocketConfig.custom()
                        .setTcpNoDelay(true)
                        .setSoReuseAddress(true)
                        .setSoTimeout(3500)
                        .setSoLinger(60)
                        .setSoKeepAlive(true)
                        .build();
                connectionManager.setDefaultSocketConfig(socketConfig);

                V3Context context = V3Context.getConfig(properties);
                String mchId = properties.getMchId();
                String mchSerialNo = properties.getMchSerialNo();

                PrivateKey merchantPrivateKey = context.getPrivateKey();
                //使用自动更新的签名验证器，不需要传入证书
                AutoUpdateCertificatesVerifier verifier = context.getVerifier();
                return WechatPayHttpClientBuilder.create()
                        .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                        .withValidator(new WechatPay2Validator(verifier))
                        .setConnectionManager(connectionManager)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
