package com.feign.pay.sdk.common.util.feign;

import com.feign.pay.sdk.common.util.ReflectUtil;
import feign.Client;
import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.optionals.OptionalDecoder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wencheng on 2020/11/5.
 * @author wencheng
 */
public abstract class AbstractConfig {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * 返回值转换器
     * @return
     */
    protected List<HttpMessageConverter<?>> getDecoderConverters() {
        return Collections.emptyList();
    }

    protected List<HttpMessageConverter<?>> getEncoderConverters()  {
        return Collections.emptyList();
    }

    protected List<HttpMessageConverter<?>> getDefaultConverters() {
        return new ArrayList<>(this.messageConverters.getObject().getConverters());
    }

    @Bean
    public Decoder feignDecoder() {
        List<HttpMessageConverter<?>> httpMessageConverters = getDecoderConverters();
        List<HttpMessageConverter<?>> converters = getDefaultConverters();
        converters.addAll(0, httpMessageConverters);
        return new OptionalDecoder(new ResponseEntityDecoder(new CustDecoder(converters)));
    }

    @Bean
    public Encoder feignFormEncoder() {
        List<HttpMessageConverter<?>> httpMessageConverters = getEncoderConverters();
        List<HttpMessageConverter<?>> converters = getDefaultConverters();
        converters.addAll(0, httpMessageConverters);
        return new CustEncoder(converters);
    }


    @Bean
    public Request.Options options() {
        return new Request.Options(3000, 3000);
    }

    @Bean
    public Client client() {
        return new ApacheHttpClient(getHttpClient());
    }

    @Bean
    public Logger infoLogFeign() {
        return new FeignLogger();
    }

    /**
     * 功能：获取连接
     * @return
     */
    protected CloseableHttpClient getHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpClient;
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
        return httpClient;
    }

    /**
     *
     * @param clazz
     * @param mediaType
     * @param <T>
     * @return
     */
    protected <T extends HttpMessageConverter> T getSupportedMediaTypeConverter(Class<T> clazz, MediaType mediaType) {
        try {
            T converter = clazz.newInstance();
            Field field = ReflectUtil.getField(clazz, "supportedMediaTypes");
            field.setAccessible(true);
            List<MediaType> supportedMediaTypes = new ArrayList<>(10);
            if (CollectionUtils.isNotEmpty(converter.getSupportedMediaTypes())) {
                supportedMediaTypes.addAll(converter.getSupportedMediaTypes());
            }
            supportedMediaTypes.add(mediaType);
            field.set(converter, supportedMediaTypes);
            return converter;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
