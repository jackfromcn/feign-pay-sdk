package com.feign.pay.sdk.wechat.v3.spi.context.sdk;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.execchain.ClientExecChain;

import java.io.IOException;

/**
 * @author wencheng
 */
@Slf4j
public class SignatureExec implements ClientExecChain {
  final ClientExecChain mainExec;
  final Credentials credentials;
  final Validator validator;

  SignatureExec(Credentials credentials, Validator validator, ClientExecChain mainExec) {
    this.credentials = credentials;
    this.validator = validator;
    this.mainExec = mainExec;
  }

  protected void convertToRepeatableResponseEntity(CloseableHttpResponse response)
      throws IOException {
    HttpEntity entity = response.getEntity();
    if (entity != null) {
      response.setEntity(new BufferedHttpEntity(entity));
    }
  }

  protected void convertToRepeatableRequestEntity(HttpRequestWrapper request) throws IOException {
    if (request instanceof HttpEntityEnclosingRequest) {
      HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
      if (entity != null) {
        ((HttpEntityEnclosingRequest) request).setEntity(new BufferedHttpEntity(entity));
      }
    }
  }

  @Override
  public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request,
      HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
    if (request.getTarget().getHostName().endsWith(".mch.weixin.qq.com")) {
      return executeWithSignature(route, request, context, execAware);
    } else {
      return mainExec.execute(route, request, context, execAware);
    }
  }

  private CloseableHttpResponse executeWithSignature(HttpRoute route, HttpRequestWrapper request,
      HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
    // ?????????????????????????????????????????????
    if (request.getOriginal().getHeaders("Content-Type").length == 0 ||
            !request.getOriginal().getHeaders("Content-Type")[0].getValue().contains("multipart")) {
      convertToRepeatableRequestEntity(request);
    }
    // ??????????????????
    request.addHeader("Authorization",
        credentials.getSchema() + " " + credentials.getToken(request));

    // ??????
    CloseableHttpResponse response = mainExec.execute(route, request, context, execAware);

    // ?????????????????????
    StatusLine statusLine = response.getStatusLine();
    if (statusLine.getStatusCode() >= 200 && statusLine.getStatusCode() < 300) {
      convertToRepeatableResponseEntity(response);
      if (!validator.validate(response)) {
        throw new HttpException("???????????????????????????????????????");
      }
    }
    return response;
  }
}
