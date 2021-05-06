package com.feign.pay.sdk.wechat.spi;

import com.feign.pay.sdk.common.util.feign.AbstractConfig;
import com.feign.pay.sdk.wechat.dto.request.*;
import com.feign.pay.sdk.wechat.dto.response.*;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;

/**
 * @seehttp://coupon-center.zvcms.com/doc.html
 * @author wencheng
 * @date 2020/12/22
 */
@FeignClient(name = "wechatPaySpi",
        url = "https://api.mch.weixin.qq.com",
        configuration = WechatSpi.Config.class
)
public interface WechatSpi {

    /**
     * 付款码支付
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_10&index=1
     * @param request
     * @return
     */
    @PostMapping(value = "/pay/micropay", consumes = MediaType.APPLICATION_XML_VALUE)
    OrderPayResponse micropay(OrderPayRequest request);

    /**
     * 查询订单
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_02
     * @param request
     * @return
     */
    @PostMapping(value = "/pay/orderquery", consumes = MediaType.APPLICATION_XML_VALUE)
    OrderQueryResponse queryOrder(OrderRequest request);

    /**
     * 查询退款
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_5
     * @param request
     * @return
     */
    @PostMapping(value = "/pay/refundquery", consumes = MediaType.APPLICATION_XML_VALUE)
    RefundQueryResponse queryRefund(RefundQueryRequest request);

    /**
     * 下载交易账单
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_6
     * @param request
     * @return
     */
    @PostMapping(value = "/pay/downloadbill", consumes = MediaType.APPLICATION_XML_VALUE)
    String downloadbill(DownloadbillRequest request);

    /**
     * 下载交易账单
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_6
     * @param request
     * @return
     */
    @PostMapping(value = "/payitil/report", consumes = MediaType.APPLICATION_XML_VALUE)
    Response report(TradeReportRequest request);

    /**
     * 付款码查询openid
     * 文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_13&index=9
     * @param request
     * @return
     */
    @PostMapping(value = "/tools/authcodetoopenid", consumes = MediaType.APPLICATION_XML_VALUE)
    Authcode2OpenidResponse authcode2Openid(BaseRequest request);

    class Config extends AbstractConfig {

        @Value("${sand-box:true}")
        private Boolean isSandBox;

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
        protected List<HttpMessageConverter<?>> getDecoderConverters() {
            return Collections.singletonList(getSupportedMediaTypeConverter(MappingJackson2XmlHttpMessageConverter.class, MediaType.TEXT_PLAIN));
        }
    }

}
