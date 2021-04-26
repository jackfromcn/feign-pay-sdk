package com.feign.pay.sdk.common.util.feign;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;

/**
 * Created by wencheng on 2020/11/5.
 * @author wencheng
 */
public class CustDecoder extends SpringDecoder {

    private List<HttpMessageConverter<?>> converters;

    public CustDecoder(List<HttpMessageConverter<?>> converters) {
        super(null);
        this.converters = converters;
    }

    @Override
    public Object decode(final Response response, Type type)
            throws IOException, FeignException {
        if (type instanceof Class || type instanceof ParameterizedType
                || type instanceof WildcardType) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            HttpMessageConverterExtractor<?> extractor = new HttpMessageConverterExtractor(
                    type, converters);

            return extractor.extractData(new FeignResponseAdapter(response));
        }
        throw new DecodeException(
                "type is not an instance of Class or ParameterizedType: " + type);
    }

    private class FeignResponseAdapter implements ClientHttpResponse {

        private final Response response;

        private FeignResponseAdapter(Response response) {
            this.response = response;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.valueOf(this.response.status());
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.response.status();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.response.reason();
        }

        @Override
        public void close() {
            try {
                this.response.body().close();
            }
            catch (IOException ex) {
                // Ignore exception on close...
            }
        }

        @Override
        public InputStream getBody() throws IOException {
            return this.response.body().asInputStream();
        }

        @Override
        public HttpHeaders getHeaders() {
            return FeignUtils.getHttpHeaders(this.response.headers());
        }

    }

}
