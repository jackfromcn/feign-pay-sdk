package com.feign.pay.sdk.wechat.v3.spi.context.sdk;

import com.feign.pay.sdk.wechat.v3.spi.context.sdk.auth.CertificatesVerifier;
import com.feign.pay.sdk.wechat.v3.spi.context.sdk.auth.PrivateKeySigner;
import com.feign.pay.sdk.wechat.v3.spi.context.sdk.auth.WechatPay2Credentials;
import com.feign.pay.sdk.wechat.v3.spi.context.sdk.auth.WechatPay2Validator;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.execchain.ClientExecChain;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author wencheng
 */
public class WechatPayHttpClientBuilder extends HttpClientBuilder {
  private Credentials credentials;
  private Validator validator;

  static final String os = System.getProperty("os.name") + "/" + System.getProperty("os.version");
  static final String version = System.getProperty("java.version");

  private WechatPayHttpClientBuilder() {
    super();

    String userAgent = String.format(
        "WechatPay-Apache-HttpClient/%s (%s) Java/%s",
        getClass().getPackage().getImplementationVersion(),
        os,
        version == null ? "Unknown" : version);
    setUserAgent(userAgent);
  }

  public static WechatPayHttpClientBuilder create() {
    return new WechatPayHttpClientBuilder();
  }

  public WechatPayHttpClientBuilder withMerchant(String merchantId, String serialNo, PrivateKey privateKey) {
    this.credentials =
        new WechatPay2Credentials(merchantId, new PrivateKeySigner(serialNo, privateKey));
    return this;
  }

  public WechatPayHttpClientBuilder withCredentials(Credentials credentials) {
    this.credentials = credentials;
    return this;
  }

  public WechatPayHttpClientBuilder withWechatpay(List<X509Certificate> certificates) {
    this.validator = new WechatPay2Validator(new CertificatesVerifier(certificates));
    return this;
  }

  public WechatPayHttpClientBuilder withValidator(Validator validator) {
    this.validator = validator;
    return this;
  }

  @Override
  public CloseableHttpClient build() {
    if (credentials == null) {
      throw new IllegalArgumentException("????????????????????????");
    }
    if (validator == null) {
      throw new IllegalArgumentException("????????????????????????");
    }

    return super.build();
  }

  @Override
  protected ClientExecChain decorateProtocolExec(final ClientExecChain requestExecutor) {
    return new SignatureExec(this.credentials, this.validator, requestExecutor);
  }
}
