package com.feign.pay.sdk.common.util.feign;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.encoding.HttpEncoding;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by wencheng on 2020/11/6.
 * @author wencheng
 */
@Slf4j
public class CustEncoder extends SpringEncoder {

    private List<HttpMessageConverter<?>> converters;

    private final SpringFormEncoder springFormEncoder = new SpringFormEncoder();

    public CustEncoder(List<HttpMessageConverter<?>> converters) {
        super(null);
        this.converters = converters;
    }

    @Override
    public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
        // template.body(conversionService.convert(object, String.class));
        if (requestBody != null) {
            Collection<String> contentTypes = request.headers().get(HttpEncoding.CONTENT_TYPE);

            MediaType requestContentType = null;
            if (contentTypes != null && !contentTypes.isEmpty()) {
                String type = contentTypes.iterator().next();
                requestContentType = MediaType.valueOf(type);
            }

            if (isMultipartType(requestContentType)) {
                this.springFormEncoder.encode(requestBody, bodyType, request);
                return;
            }
            else {
                if (bodyType == MultipartFile.class) {
                    log.warn("For MultipartFile to be handled correctly, the 'consumes' parameter of @RequestMapping "
                            + "should be specified as MediaType.MULTIPART_FORM_DATA_VALUE");
                }
            }
            encodeWithMessageConverter(requestBody, bodyType, request, requestContentType);
        }
    }

    private void encodeWithMessageConverter(Object requestBody, Type bodyType, RequestTemplate request,
                                            MediaType requestContentType) {
        for (HttpMessageConverter messageConverter : this.converters) {
            FeignOutputMessage outputMessage;
            try {
                if (messageConverter instanceof GenericHttpMessageConverter) {
                    outputMessage = checkAndWrite(requestBody, bodyType, requestContentType,
                            (GenericHttpMessageConverter) messageConverter, request);
                }
                else {
                    outputMessage = checkAndWrite(requestBody, requestContentType, messageConverter, request);
                }
            }
            catch (IOException | HttpMessageConversionException ex) {
                throw new EncodeException("Error converting request body", ex);
            }
            if (outputMessage != null) {
                // clear headers
                request.headers(null);
                // converters can modify headers, so update the request
                // with the modified headers
                request.headers(FeignUtils.getHeaders(outputMessage.getHeaders()));

                // do not use charset for binary data and protobuf
                Charset charset;

                MediaType contentType = outputMessage.getHeaders().getContentType();
                Charset charsetFromContentType = contentType != null ? contentType.getCharset() : null;

                if (shouldHaveNullCharset(messageConverter, outputMessage)) {
                    charset = null;
                }
                else {
                    charset = StandardCharsets.UTF_8;
                }
                request.body(outputMessage.getOutputStream().toByteArray(), charset);
                return;
            }
        }
        String message = "Could not write request: no suitable HttpMessageConverter " + "found for request type ["
                + requestBody.getClass().getName() + "]";
        if (requestContentType != null) {
            message += " and content type [" + requestContentType + "]";
        }
        throw new EncodeException(message);
    }

    private boolean shouldHaveNullCharset(HttpMessageConverter messageConverter, FeignOutputMessage outputMessage) {
        return binaryContentType(outputMessage) || messageConverter instanceof ByteArrayHttpMessageConverter
                || messageConverter instanceof ProtobufHttpMessageConverter && ProtobufHttpMessageConverter.PROTOBUF
                .isCompatibleWith(outputMessage.getHeaders().getContentType());
    }

    @SuppressWarnings("unchecked")
    private FeignOutputMessage checkAndWrite(Object body, MediaType contentType, HttpMessageConverter converter,
                                             RequestTemplate request) throws IOException {
        if (converter.canWrite(body.getClass(), contentType)) {
            logBeforeWrite(body, contentType, converter);
            FeignOutputMessage outputMessage = new FeignOutputMessage(request);
            converter.write(body, contentType, outputMessage);
            return outputMessage;
        }
        else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private FeignOutputMessage checkAndWrite(Object body, Type genericType, MediaType contentType,
                                             GenericHttpMessageConverter converter, RequestTemplate request) throws IOException {
        if (converter.canWrite(genericType, body.getClass(), contentType)) {
            logBeforeWrite(body, contentType, converter);
            FeignOutputMessage outputMessage = new FeignOutputMessage(request);
            converter.write(body, genericType, contentType, outputMessage);
            return outputMessage;
        }
        else {
            return null;
        }
    }

    private void logBeforeWrite(Object requestBody, MediaType requestContentType,
                                HttpMessageConverter messageConverter) {
        if (log.isDebugEnabled()) {
            if (requestContentType != null) {
                log.debug("Writing [" + requestBody + "] as \"" + requestContentType + "\" using [" + messageConverter
                        + "]");
            }
            else {
                log.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
            }
        }
    }

    private boolean isMultipartType(MediaType requestContentType) {
        return Arrays.asList(MediaType.MULTIPART_FORM_DATA, MediaType.MULTIPART_MIXED, MediaType.MULTIPART_RELATED)
                .contains(requestContentType);
    }

    private boolean binaryContentType(FeignOutputMessage outputMessage) {
        MediaType contentType = outputMessage.getHeaders().getContentType();
        return contentType == null || Stream
                .of(MediaType.APPLICATION_CBOR, MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_PDF,
                        MediaType.IMAGE_GIF, MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG)
                .anyMatch(mediaType -> mediaType.includes(contentType));
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
