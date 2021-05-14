package com.feign.pay.sdk.wechat.v3.spi.context.sdk.auth;

/**
 * @author wencheng
 */
public interface Signer {
  SignatureResult sign(byte[] message);

  class SignatureResult {
    String sign;
    String certificateSerialNumber;

    public SignatureResult(String sign, String serialNumber) {
      this.sign = sign;
      this.certificateSerialNumber = serialNumber;
    }
  }
}
