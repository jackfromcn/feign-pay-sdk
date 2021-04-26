package com.feign.pay.sdk.common.util.feign;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

/**
 * Created by wencheng on 2020/11/6.
 * @author wencheng
 */
@Slf4j
public class CustEncoder extends SpringEncoder {

    private List<HttpMessageConverter<?>> converters;

    public CustEncoder(List<HttpMessageConverter<?>> converters) {
        super(null);
        this.converters = converters;
    }

    @Override
    public void encode(Object requestBody, Type bodyType, RequestTemplate request)
            throws EncodeException {
        if (requestBody != null) {
            Class<?> requestType = requestBody.getClass();
            Collection<String> contentTypes = request.headers().get("Content-Type");

            MediaType requestContentType = null;
            if (contentTypes != null && !contentTypes.isEmpty()) {
                String type = contentTypes.iterator().next();
                requestContentType = MediaType.valueOf(type);
            }

            for (HttpMessageConverter<?> messageConverter : this.converters) {
                if (messageConverter.canWrite(requestType, requestContentType)) {
                    if (log.isDebugEnabled()) {
                        if (requestContentType != null) {
                            log.debug("Writing [" + requestBody + "] as \""
                                    + requestContentType + "\" using ["
                                    + messageConverter + "]");
                        }
                        else {
                            log.debug("Writing [" + requestBody + "] using ["
                                    + messageConverter + "]");
                        }

                    }

                    FeignOutputMessage outputMessage = new FeignOutputMessage(request);
                    try {
                        @SuppressWarnings("unchecked")
                        HttpMessageConverter<Object> copy = (HttpMessageConverter<Object>) messageConverter;
                        copy.write(requestBody, requestContentType, outputMessage);
                    }
                    catch (IOException ex) {
                        throw new EncodeException("Error converting request body", ex);
                    }
                    // clear headers
                    request.headers(null);
                    // converters can modify headers, so update the request
                    // with the modified headers
                    request.headers(FeignUtils.getHeaders(outputMessage.getHeaders()));

                    // do not use charset for binary data and protobuf
                    Charset charset;
                    if (messageConverter instanceof ByteArrayHttpMessageConverter) {
                        charset = null;
                    } else if (messageConverter instanceof ProtobufHttpMessageConverter &&
                            ProtobufHttpMessageConverter.PROTOBUF.isCompatibleWith(outputMessage.getHeaders().getContentType())) {
                        charset = null;
                    } else {
                        charset = StandardCharsets.UTF_8;
                    }
                    request.body(outputMessage.getOutputStream().toByteArray(), charset);
                    return;
                }
            }
            String message = "Could not write request: no suitable HttpMessageConverter "
                    + "found for request type [" + requestType.getName() + "]";
            if (requestContentType != null) {
                message += " and content type [" + requestContentType + "]";
            }
            throw new EncodeException(message);
        }
    }

    private class FeignOutputMessage implements HttpOutputMessage {

        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        private final HttpHeaders httpHeaders;

        private FeignOutputMessage(RequestTemplate request) {
            httpHeaders = FeignUtils.getHttpHeaders(request.headers());
        }

        @Override
        public OutputStream getBody() throws IOException {
            return this.outputStream;
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.httpHeaders;
        }

        public ByteArrayOutputStream getOutputStream() {
            return this.outputStream;
        }

    }

}
