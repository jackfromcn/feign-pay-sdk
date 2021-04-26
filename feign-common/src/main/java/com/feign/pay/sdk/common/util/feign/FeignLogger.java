package com.feign.pay.sdk.common.util.feign;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

/**
 *
 * @author wencheng
 * @date 2019/5/1
 */
@SuppressWarnings("all")
public class FeignLogger extends Logger {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(FeignLogger.class);

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        this.log(configKey, "---> %s %s HTTP/1.1", new Object[]{request.method(), request.url()});
        if(logLevel.ordinal() >= Level.HEADERS.ordinal()) {
            Iterator bodyLength = request.headers().keySet().iterator();

            StringBuilder headerBuilder = new StringBuilder();
            String bodyText;
            while(bodyLength.hasNext()) {
                bodyText = (String)bodyLength.next();
                Iterator var6 = Util.valuesOrEmpty(request.headers(), bodyText).iterator();

                headerBuilder.append(bodyText).append(":");
                boolean hasValue = false;
                while(var6.hasNext()) {
                    hasValue = true;
                    String value = (String)var6.next();
                    headerBuilder.append(value).append(",");
                }
                if (hasValue) {
                    headerBuilder = headerBuilder.deleteCharAt(headerBuilder.length() - 1);
                }
                headerBuilder.append(";");
            }

            if (StringUtils.isNotBlank(headerBuilder.toString())) {
                this.log(configKey, "header: %s", new Object[]{headerBuilder.toString()});
            }

            int bodyLength1 = 0;
            if(request.body() != null) {
                bodyLength1 = request.body().length;
                if(logLevel.ordinal() >= Level.FULL.ordinal()) {
                    bodyText = request.charset() != null?new String(request.body(), request.charset()):null;
                    this.log(configKey, "%s", new Object[]{bodyText != null?bodyText:"Binary data"});
                }
            }

            this.log(configKey, "---> END HTTP (%s-byte body)", new Object[]{Integer.valueOf(bodyLength1)});
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime)
            throws IOException {
        if (LOG.isInfoEnabled()) {
            String reason = response.reason() != null && logLevel.compareTo(Level.NONE) > 0?" " + response.reason():"";
            int status = response.status();
            this.log(configKey, "<--- HTTP/1.1 %s%s (%sms)", new Object[]{Integer.valueOf(status), reason, Long.valueOf(elapsedTime)});
            if(logLevel.ordinal() >= Level.HEADERS.ordinal()) {
                Iterator bodyLength = response.headers().keySet().iterator();

                StringBuilder headerBuilder = new StringBuilder();
                String bodyText;
                while(bodyLength.hasNext()) {
                    bodyText = (String)bodyLength.next();
                    Iterator var6 = Util.valuesOrEmpty(response.headers(), bodyText).iterator();

                    boolean hasValue = false;
                    headerBuilder.append(bodyText).append(":");
                    while(var6.hasNext()) {
                        hasValue = true;
                        String value = (String)var6.next();
                        headerBuilder.append(value).append(",");
                    }
                    if (hasValue) {
                        headerBuilder = headerBuilder.deleteCharAt(headerBuilder.length() - 1);
                    }
                    headerBuilder.append(";");
                }

                if (StringUtils.isNotBlank(headerBuilder.toString())) {
                    this.log(configKey, "header: %s", new Object[]{headerBuilder.toString()});
                }

                byte bodyLength1 = 0;
                if(response.body() != null && status != 204 && status != 205) {

                    byte[] bodyData1 = Util.toByteArray(response.body().asInputStream());
                    int bodyLength2 = bodyData1.length;
                    if(logLevel.ordinal() >= Level.FULL.ordinal() && bodyLength2 > 0) {
                        this.log(configKey, "%s", new Object[]{Util.decodeOrDefault(bodyData1, Util.UTF_8, "Binary data")});
                    }

                    this.log(configKey, "<--- END HTTP (%s-byte body)", new Object[]{Integer.valueOf(bodyLength2)});
                    return response.toBuilder().body(bodyData1).build();
                }

                this.log(configKey, "<--- END HTTP (%s-byte body)", new Object[]{Integer.valueOf(bodyLength1)});
            }

            return response;
        }
        return response;
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        return super.logIOException(configKey, logLevel, ioe, elapsedTime);
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        // Not using SLF4J's support for parameterized messages (even though it
        // would be more efficient) because it would
        // require the incoming message formats to be SLF4J-specific.
        if (LOG.isInfoEnabled()) {
            String log = String.format(methodTag(configKey) + format, args);
            log = log.replace("\n", "");
            FeignLogger.LOG.info(log);
        }
    }
}
