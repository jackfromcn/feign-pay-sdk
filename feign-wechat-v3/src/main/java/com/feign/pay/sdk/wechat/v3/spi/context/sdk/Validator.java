package com.feign.pay.sdk.wechat.v3.spi.context.sdk;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

/**
 * @author wencheng
 */
public interface Validator {
  boolean validate(CloseableHttpResponse response) throws IOException;
}
