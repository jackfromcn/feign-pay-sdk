package com.feign.pay.sdk.wechat.v3.spi.context.sdk;

import org.apache.http.client.methods.HttpRequestWrapper;

import java.io.IOException;

/**
 * @author wencheng
 */
public interface Credentials {

  String getSchema();

  String getToken(HttpRequestWrapper request) throws IOException;
}
