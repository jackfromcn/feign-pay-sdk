package com.feign.pay.sdk.alipay.spi.context;

import com.alipay.api.*;
import lombok.Data;

import java.security.cert.X509Certificate;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wencheng
 * @date 2021/4/21
 */
@Data
public class AlipayClient extends AbstractAlipayClient {

    private String                                     privateKey;
    private String                                     encryptKey;
    private String                                     alipayPublicKey;
    private Signer signer;
    private SignChecker signChecker;
    private Encryptor encryptor;
    private Decryptor decryptor;
    private X509Certificate cert;
    private ConcurrentHashMap<String, X509Certificate> alipayPublicCertMap;

    public AlipayClient(String appId, String privateKey) {
        super(appId, null, null, null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
    }

    public AlipayClient(String appId, String privateKey, String format) {
        super(appId, format, null, null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
    }

    public AlipayClient(String appId, String privateKey, String format,
                               String charset) {
        super(appId, format, charset, null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
    }

    public AlipayClient(String appId, String privateKey, String format,
                               String charset, String alipayPublicKey) {
        super(appId, format, charset, null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.alipayPublicKey = alipayPublicKey;
        this.signChecker = new DefaultSignChecker(alipayPublicKey);
    }

    public AlipayClient(String appId, String privateKey, String format,
                               String charset, String alipayPublicKey, String signType) {
        super(appId, format, charset, signType);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.alipayPublicKey = alipayPublicKey;
        this.signChecker = new DefaultSignChecker(alipayPublicKey);
    }

    public AlipayClient(String appId, String privateKey, String format,
                               String charset, String alipayPublicKey, String signType,
                               String proxyHost, int proxyPort) {
        super(appId, format, charset, signType, proxyHost, proxyPort);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.alipayPublicKey = alipayPublicKey;
        this.signChecker = new DefaultSignChecker(alipayPublicKey);
    }

    public AlipayClient(String appId, String privateKey, String format,
                               String charset, String alipayPublicKey, String signType,
                               String encryptKey, String encryptType) {
        super(appId, format, charset, signType, encryptType);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.alipayPublicKey = alipayPublicKey;
        this.signChecker = new DefaultSignChecker(alipayPublicKey);
        this.encryptor = new DefaultEncryptor(encryptKey);
        this.decryptor = new DefaultDecryptor(encryptKey);
    }

    public AlipayClient(CertAlipayRequest certAlipayRequest) throws AlipayApiException {
        super(certAlipayRequest.getAppId(), certAlipayRequest.getFormat(),
                certAlipayRequest.getCharset(), certAlipayRequest.getSignType(),
                certAlipayRequest.getCertPath(), certAlipayRequest.getCertContent(),
                certAlipayRequest.getAlipayPublicCertPath(), certAlipayRequest.getAlipayPublicCertContent(),
                certAlipayRequest.getRootCertPath(), certAlipayRequest.getRootCertContent(),
                certAlipayRequest.getProxyHost(), certAlipayRequest.getProxyPort(), certAlipayRequest.getEncryptType());
        this.privateKey = certAlipayRequest.getPrivateKey();
        this.signer = new DefaultSigner(certAlipayRequest.getPrivateKey());
        this.encryptor = new DefaultEncryptor(certAlipayRequest.getEncryptor());
        this.decryptor = new DefaultDecryptor(certAlipayRequest.getEncryptor());
    }

}
