package com.feign.pay.sdk.wechat.spi;

import com.feign.pay.sdk.common.util.feign.AbstractConfig;
import com.feign.pay.sdk.wechat.dto.request.QueryRequest;
import com.feign.pay.sdk.wechat.dto.request.RefundQueryRequest;
import com.feign.pay.sdk.wechat.dto.response.QueryResponse;
import feign.Logger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @seehttp://coupon-center.zvcms.com/doc.html
 * @author wencheng
 * @date 2020/12/22
 */
@FeignClient(name = "wechatPaySpi",
        url = "https://api.mch.weixin.qq.com",
        configuration = WechatPaySpi.Config.class
)
public interface WechatPaySpi {

    /**
     * 查询订单
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_02
     * @param request
     * @return
     */
    @PostMapping(value = "/pay/orderquery", consumes = MediaType.APPLICATION_XML_VALUE)
    QueryResponse queryOrder(QueryRequest request);

    /**
     * 查询退款
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_5
     * @param request
     * @return
     */
    @PostMapping(value = "/pay/refundquery", consumes = MediaType.APPLICATION_XML_VALUE)
    Map<String, Object> queryRefund(RefundQueryRequest request);

    class Config extends AbstractConfig {

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
