package com.feign.pay.sdk.wechat.v3.spi.context.sdk.auth;

import java.security.cert.X509Certificate;

/**
 * @author wencheng
 */
public interface Verifier {

  boolean verify(String serialNumber, byte[] message, String signature);

  X509Certificate getValidCertificate();
}
