package com.feign.pay.sdk.common.util.feign;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wencheng
 */
public class MultipartFileHttpMessageConverter extends AbstractHttpMessageConverter<MultipartFile> {

    public MultipartFileHttpMessageConverter() {
        super(MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return MultipartFile.class.isAssignableFrom(clazz);
    }

    @Override
    protected MultipartFile readInternal(Class<? extends MultipartFile> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new HttpMessageNotReadableException("Not supportted for read.");
    }

    @Override
    protected MediaType getDefaultContentType(MultipartFile multipartFile) {
        try {
            String contentType = multipartFile.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            if (mediaType != null) {
                return mediaType;
            }
        } catch (Exception ex) {
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    @Override
    protected Long getContentLength(MultipartFile multipartFile, MediaType contentType) throws IOException {
        long contentLength = multipartFile.getSize();
        return (contentLength < 0 ? null : contentLength);
    }

    @Override
    protected void writeInternal(MultipartFile multipartFile, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        writeContent(multipartFile, outputMessage);
    }

    protected void writeContent(MultipartFile multipartFile, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        try {
            InputStream in = multipartFile.getInputStream();
            try {
                StreamUtils.copy(in, outputMessage.getBody());
            }
            catch (NullPointerException ex) {
                // ignore, see SPR-13620
            }
            finally {
                try {
                    in.close();
                }
                catch (Throwable ex) {
                    // ignore, see SPR-12999
                }
            }
        }
        catch (FileNotFoundException ex) {
            // ignore, see SPR-12999
        }
    }

}
