package com.feign.pay.sdk.wechat.v3.spi;

import com.feign.pay.sdk.common.util.feign.AbstractConfig;
import com.feign.pay.sdk.common.util.feign.ApacheHttpClient;
import com.feign.pay.sdk.wechat.v3.config.WechatProperties;
import com.feign.pay.sdk.wechat.v3.dto.request.WeChatApplyMentRequest;
import com.feign.pay.sdk.wechat.v3.dto.response.WeChatApplyMentResponse;
import com.feign.pay.sdk.wechat.v3.dto.response.WeChatApplyMentStatusResponse;
import com.feign.pay.sdk.wechat.v3.dto.response.WechatMediaUploadResponse;
import com.feign.pay.sdk.wechat.v3.spi.context.V3Context;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import feign.*;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.PrivateKey;

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
     * 图片上传
     * @param file
     * @return
     */
    @PostMapping(value = "/v3/merchant/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    WechatMediaUploadResponse upload(@RequestPart("file")MultipartFile file, @RequestPart("meta") String meta, @RequestHeader("meta") String header);

    /**
     * 进件提交申请单API
     *
     * @param request 请求
     * @return WeChatApplyMentResponse
     */
    @PostMapping(value = "/v3/applyment4sub/applyment/", consumes = MediaType.APPLICATION_JSON_VALUE)
    WeChatApplyMentResponse applyMent(WeChatApplyMentRequest request, @RequestHeader("Wechatpay-Serial") String mchSerialNo);

    /**
     * 根据business_code查询进件状态
     *
     * @param businessCode 参数
     * @return WeChatApplyMentStatusResponse
     */
    @PostMapping(value = "/v3/applyment4sub/applyment/business_code/{business_code}", consumes = MediaType.APPLICATION_JSON_VALUE)
    WeChatApplyMentStatusResponse applyMentStatusByCode(@PathVariable("business_code") String businessCode);

    /**
     * applyment_id
     *
     * @param applyMentId 参数
     * @return WeChatApplyMentStatusResponse
     */
    @PostMapping(value = "/v3/applyment4sub/applyment/applyment_id/{applyment_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    WeChatApplyMentStatusResponse applyMentStatusById(@PathVariable("applyment_id") Integer applyMentId);

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
        public Client client() {
            return new ApacheHttpClient(getHttpClient()) {
                @Override
                public Response execute(Request request, Request.Options options) throws IOException {
                    Response response = super.execute(request, options);
                    return response.toBuilder()
                            .status(200)
                            .build();
                }
            };
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
