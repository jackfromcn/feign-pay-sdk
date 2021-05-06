package com.feign.pay.sdk.wechat.spi;

import com.feign.pay.sdk.common.util.feign.AbstractConfig;
import com.feign.pay.sdk.wechat.dto.request.DownloadfundflowRequeset;
import com.feign.pay.sdk.wechat.dto.request.RefundQueryRequest;
import com.feign.pay.sdk.wechat.dto.request.RefundRequest;
import com.feign.pay.sdk.wechat.dto.response.RefundResponse;
import com.feign.pay.sdk.wechat.dto.response.ReverseResponse;
import com.feign.pay.sdk.wechat.spi.context.WechatProperties;
import feign.Logger;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

/**
 * @seehttp://coupon-center.zvcms.com/doc.html
 * @author wencheng
 * @date 2020/12/22
 */
@FeignClient(name = "wechatRefundSpi",
        url = "https://api.mch.weixin.qq.com",
        configuration = WechatCertSpi.Config.class
)
public interface WechatCertSpi {

    /**
     * 查询订单
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_02
     * @param request
     * @return
     */
    @PostMapping(value = "/secapi/pay/refund", consumes = MediaType.APPLICATION_XML_VALUE)
    RefundResponse refund(RefundRequest request);

    /**
     * 查询退款
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_5
     * @param request
     * @return
     */
    @PostMapping(value = "/secapi/pay/reverse", consumes = MediaType.APPLICATION_XML_VALUE)
    ReverseResponse reverse(RefundQueryRequest request);

    /**
     * 下载资金账单
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_18&index=7
     * @param request
     * @return
     */
    @PostMapping(value = "/pay/downloadfundflow", consumes = MediaType.APPLICATION_XML_VALUE)
    String downloadfundflow(DownloadfundflowRequeset request);

    class Config extends AbstractConfig {

        @Autowired
        private WechatProperties properties;

        /**
         * 功能：获取连接
         * @return
         */
        @Override
        protected CloseableHttpClient getHttpClient() {
            CloseableHttpClient httpClient;
            try {
                // 证书
                char[] password = properties.getMchId().toCharArray();
                InputStream certStream = new FileInputStream(properties.getCertStreamUrl());
                KeyStore ks = KeyStore.getInstance("PKCS12");
                ks.load(certStream, password);

                // 实例化密钥库 & 初始化密钥工厂
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(ks, password);

                // 创建 SSLContext
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

                SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                        sslContext,
                        new String[]{"TLSv1"},
                        null,
                        new DefaultHostnameVerifier());

                PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                        RegistryBuilder.<ConnectionSocketFactory>create()
                                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                                .register("https", sslConnectionSocketFactory)
                                .build());
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
                httpClient = HttpClients.custom().setConnectionManager(connectionManager)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return httpClient;
        }

        @Bean
        public Logger.Level feignLoggerLevel() {
            return Logger.Level.FULL;
        }

        @Override
        protected List<HttpMessageConverter<?>> getDecoderConverters() {
            return Collections.singletonList(getSupportedMediaTypeConverter(MappingJackson2XmlHttpMessageConverter.class, MediaType.TEXT_PLAIN));
        }
    }

}
