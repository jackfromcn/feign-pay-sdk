package com.feign.pay.sdk.common.util.feign;

import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 参考 FormHttpMessageConverter
 */
public class FormHttpMessageConverter implements HttpMessageConverter<BaseBaseMultipartFileModel> {


    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");


    private List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();

    private List<HttpMessageConverter<?>> partConverters = new ArrayList<HttpMessageConverter<?>>();

    private Charset charset = DEFAULT_CHARSET;

    private Charset multipartCharset;


    public FormHttpMessageConverter() {
        this.supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        // see SPR-7316
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        this.partConverters.add(new ByteArrayHttpMessageConverter());
        this.partConverters.add(stringHttpMessageConverter);
        this.partConverters.add(new ResourceHttpMessageConverter());

        MultipartFileHttpMessageConverter multipartFileHttpMessageConverter = new MultipartFileHttpMessageConverter();
        this.partConverters.add(multipartFileHttpMessageConverter);

        applyDefaultCharset();
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.unmodifiableList(this.supportedMediaTypes);
    }

    public void setPartConverters(List<HttpMessageConverter<?>> partConverters) {
        Assert.notEmpty(partConverters, "'partConverters' must not be empty");
        this.partConverters = partConverters;
    }

    public void addPartConverter(HttpMessageConverter<?> partConverter) {
        Assert.notNull(partConverter, "'partConverter' must not be null");
        this.partConverters.add(partConverter);
    }

    public void setCharset(Charset charset) {
        if (charset != this.charset) {
            this.charset = (charset != null ? charset : DEFAULT_CHARSET);
            applyDefaultCharset();
        }
    }

    private void applyDefaultCharset() {
        for (HttpMessageConverter<?> candidate : this.partConverters) {
            if (candidate instanceof AbstractHttpMessageConverter) {
                AbstractHttpMessageConverter<?> converter = (AbstractHttpMessageConverter<?>) candidate;
                // Only override default charset if the converter operates with a charset to begin with...
                if (converter.getDefaultCharset() != null) {
                    converter.setDefaultCharset(this.charset);
                }
            }
        }
    }

    public void setMultipartCharset(Charset charset) {
        this.multipartCharset = charset;
    }


    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!BaseBaseMultipartFileModel.class.isAssignableFrom(clazz)) {
            return false;
        }
        if (mediaType == null || MediaType.ALL.equals(mediaType)) {
            return false;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.isCompatibleWith(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BaseBaseMultipartFileModel read(Class<? extends BaseBaseMultipartFileModel> clazz,
                                           HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        throw new HttpMessageNotReadableException("Not supportted for read.");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(BaseBaseMultipartFileModel model, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        LinkedHashMap<String, ?> map = model.paramMap();
        writeMultipart((LinkedHashMap<String, Object>) map, outputMessage);
    }

    private void writeMultipart(final LinkedHashMap<String, Object> parts, HttpOutputMessage outputMessage) throws IOException {
        final byte[] boundary = generateMultipartBoundary();
        Map<String, String> parameters = Collections.singletonMap("boundary", new String(boundary, "US-ASCII"));

        MediaType contentType = new MediaType(MediaType.MULTIPART_FORM_DATA, parameters);
        HttpHeaders headers = outputMessage.getHeaders();
        headers.setContentType(contentType);

        if (outputMessage instanceof StreamingHttpOutputMessage) {
            StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage) outputMessage;
            streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body() {
                @Override
                public void writeTo(OutputStream outputStream) throws IOException {
                    writeParts(outputStream, parts, boundary);
                    writeEnd(outputStream, boundary);
                }
            });
        }
        else {
            writeParts(outputMessage.getBody(), parts, boundary);
            writeEnd(outputMessage.getBody(), boundary);
        }
    }

    private void writeParts(OutputStream os, LinkedHashMap<String, Object> parts, byte[] boundary) throws IOException {
        for (Map.Entry<String, Object> entry : parts.entrySet()) {
            String name = entry.getKey();
            Object part = entry.getValue();
            if (part != null) {
                writeBoundary(os, boundary);
                writePart(name, getHttpEntity(part), os);
                writeNewLine(os);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void writePart(String name, HttpEntity<?> partEntity, OutputStream os) throws IOException {
        Object partBody = partEntity.getBody();
        Class<?> partType = partBody.getClass();
        HttpHeaders partHeaders = partEntity.getHeaders();
        MediaType partContentType = partHeaders.getContentType();
        for (HttpMessageConverter<?> messageConverter : this.partConverters) {
            if (messageConverter.canWrite(partType, partContentType)) {
                HttpOutputMessage multipartMessage = new MultipartHttpOutputMessage(os);
                multipartMessage.getHeaders().setContentDispositionFormData(name, getFilename(partBody));
                if (!partHeaders.isEmpty()) {
                    multipartMessage.getHeaders().putAll(partHeaders);
                }
                ((HttpMessageConverter<Object>) messageConverter).write(partBody, partContentType, multipartMessage);
                return;
            }
        }
        throw new HttpMessageNotWritableException("Could not write request: no suitable HttpMessageConverter " +
                "found for request type [" + partType.getName() + "]");
    }

    protected byte[] generateMultipartBoundary() {
        return MimeTypeUtils.generateMultipartBoundary();
    }

    protected HttpEntity<?> getHttpEntity(Object part) {
        return (part instanceof HttpEntity ? (HttpEntity<?>) part : new HttpEntity<Object>(part));
    }

    protected String getFilename(Object part) {
        if (part instanceof Resource) {
            Resource resource = (Resource) part;
            String filename = resource.getFilename();
            if (filename != null && this.multipartCharset != null) {
                filename = MimeDelegate.encode(filename, this.multipartCharset.name());
            }
            return filename;
        } else if (part instanceof MultipartFile) {
            MultipartFile multipartFile = (MultipartFile) part;
            String filename = multipartFile.getName();
            if (filename == null || filename.isEmpty()) {
                filename = multipartFile.getOriginalFilename();
            }
            return filename;
        }
        else {
            return null;
        }
    }


    private void writeBoundary(OutputStream os, byte[] boundary) throws IOException {
        os.write('-');
        os.write('-');
        os.write(boundary);
        writeNewLine(os);
    }

    private static void writeEnd(OutputStream os, byte[] boundary) throws IOException {
        os.write('-');
        os.write('-');
        os.write(boundary);
        os.write('-');
        os.write('-');
        writeNewLine(os);
    }

    private static void writeNewLine(OutputStream os) throws IOException {
        os.write('\r');
        os.write('\n');
    }

    private static class MultipartHttpOutputMessage implements HttpOutputMessage {

        private final OutputStream outputStream;

        private final HttpHeaders headers = new HttpHeaders();

        private boolean headersWritten = false;

        public MultipartHttpOutputMessage(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public HttpHeaders getHeaders() {
            return (this.headersWritten ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers);
        }

        @Override
        public OutputStream getBody() throws IOException {
            writeHeaders();
            return this.outputStream;
        }

        private void writeHeaders() throws IOException {
            if (!this.headersWritten) {
                for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
                    byte[] headerName = getAsciiBytes(entry.getKey());
                    for (String headerValueString : entry.getValue()) {
                        byte[] headerValue = getAsciiBytes(headerValueString);
                        this.outputStream.write(headerName);
                        this.outputStream.write(':');
                        this.outputStream.write(' ');
                        this.outputStream.write(headerValue);
                        writeNewLine(this.outputStream);
                    }
                }
                writeNewLine(this.outputStream);
                this.headersWritten = true;
            }
        }

        private byte[] getAsciiBytes(String name) {
            try {
                return name.getBytes("US-ASCII");
            }
            catch (UnsupportedEncodingException ex) {
                // Should not happen - US-ASCII is always supported.
                throw new IllegalStateException(ex);
            }
        }
    }

    private static class MimeDelegate {

        public static String encode(String value, String charset) {
            try {
                return MimeUtility.encodeText(value, charset, null);
            }
            catch (UnsupportedEncodingException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }
}