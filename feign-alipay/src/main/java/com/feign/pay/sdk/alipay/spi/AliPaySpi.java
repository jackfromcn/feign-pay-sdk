package com.feign.pay.sdk.alipay.spi;

import com.alipay.api.internal.util.AlipayHashMap;
import com.alipay.api.internal.util.RequestParametersHolder;
import com.alipay.api.internal.util.WebUtils;
import com.feign.pay.sdk.alipay.domain.AlipayTradePayModel;
import com.feign.pay.sdk.alipay.spi.context.AlipayClient;
import com.feign.pay.sdk.alipay.spi.context.AlipayRequest;
import com.feign.pay.sdk.common.util.feign.AbstractConfig;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;


@FeignClient(name = "aliPaySpi",
        url = "https://openapi.alipay.com/gateway.do",
        configuration = AliPaySpi.Config.class
)
public interface AliPaySpi {

    /**
     * alipay.trade.pay(统一收单交易支付接口)
     * 文档: https://opendocs.alipay.com/apis/api_1/alipay.trade.pay
     * @param request 请求参数
     * @param authToken token 不需要时,可以删除
     * @param appAuthToken token, 不需要时,可以删除
     * @param params 扩展参数, 不需要时可以删除
     * @return
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            headers = {
                    "method=alipay.trade.pay",
                    "needEncrypt=false"
            })
    String barCodePay(@RequestBody AlipayTradePayModel request, @RequestParam("auth_token") String authToken, @RequestParam("app_auth_token") String appAuthToken, @RequestParam Map<String, String> params);

    class Config extends AbstractConfig {

        @Autowired
        private AlipayClient client;

        @Bean
        public RequestInterceptor requestInterceptor() {
            return requestTemplate -> {
                if (Objects.isNull(client)) {
                    throw new RuntimeException("配置异常");
                }
                try {
                    requestTemplate.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

                    RequestParametersHolder requestHolder = client.getRequestHolderWithSign(requestTemplate, defaultApiConfig(), null, null);

                    // sysMustQuery
                    AlipayHashMap protocalMustParams = requestHolder.getProtocalMustParams();
                    // sysOptQuery "auth_token" -> "accessToken"
                    AlipayHashMap protocalOptParams = requestHolder.getProtocalOptParams();

                    Map<String, Collection<String>> queries = new HashMap<>(protocalMustParams.size() + protocalOptParams.size());
                    for (Map.Entry<String, String> entry: protocalMustParams.entrySet()) {
                        queries.put(entry.getKey(), Collections.singletonList(entry.getValue()));
                    }
                    for (Map.Entry<String, String> entry: protocalOptParams.entrySet()) {
                        queries.put(entry.getKey(), Collections.singletonList(entry.getValue()));
                    }
                    requestTemplate.queries(null);
                    requestTemplate.queries(queries);
                    // body "app_auth_token" -> "appAuthToken"
                    requestTemplate.body(WebUtils.buildQuery(requestHolder.getApplicationParams(), requestTemplate.request().charset().toString()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        }

        private AlipayRequest defaultApiConfig() {
            AlipayRequest request = new AlipayRequest();
            request.setApiVersion("1.0");
            request.setNeedEncrypt(false);
            return request;
        }

        @Bean
        public Logger.Level feignLoggerLevel() {
            return Logger.Level.FULL;
        }

        @Override
        protected List<HttpMessageConverter<?>> getDecoderConverters() {
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = getSupportedMediaTypeConverter(MappingJackson2HttpMessageConverter.class, MediaType.TEXT_HTML);
            StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
            return Arrays.asList(stringHttpMessageConverter, mappingJackson2HttpMessageConverter);
        }
    }

}
