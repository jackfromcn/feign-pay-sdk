package com.feign.pay.sdk.alipay.spi.context;

import com.alipay.api.*;
import com.alipay.api.internal.util.*;
import com.alipay.api.internal.util.codec.Base64;
import com.alipay.api.internal.util.file.FileUtils;
import com.feign.pay.sdk.common.util.feign.FeignUtils;
import feign.RequestTemplate;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wencheng
 * @date 2021/4/21
 */
@Data
public abstract class AbstractAlipayClient {

    /**
     * 批量API默认分隔符
     **/
    private static final String BATCH_API_DEFAULT_SPLIT = "#S#";

    static {
        //清除安全设置
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");

    }

    protected boolean                                    loadTest       = false;
    private   String                                     appId;
    private   String                                     prodCode;
    private   String                                     format         = AlipayConstants.FORMAT_JSON;
    private   String                                     signType       = AlipayConstants.SIGN_TYPE_RSA;
    private   String                                     encryptType    = AlipayConstants.ENCRYPT_TYPE_AES;
    private   String                                     charset;
    private   int                                        connectTimeout = 3000;
    private   int                                        readTimeout    = 15000;
    private   String                                     proxyHost;
    private   int                                        proxyPort;
    private SignChecker signChecker;
    private   String                                     appCertSN;
    private   String                                     alipayCertSN;
    private   String                                     alipayRootCertSN;
    private   String                                     alipayRootSm2CertSN;
    private   String                                     rootCertContent;
    private X509Certificate cert;
    private ConcurrentHashMap<String, X509Certificate> alipayPublicCertMap;
    private   ConcurrentHashMap<String, String>          alipayPublicKeyMap;

    public AbstractAlipayClient(String appId, String format,
                                String charset, String signType) {
        this.appId = appId;
        if (!StringUtils.isEmpty(format)) {
            this.format = format;
        }
        this.charset = charset;
        if (!StringUtils.isEmpty(signType)) {
            this.signType = signType;
        }
    }

    public AbstractAlipayClient(String appId, String format,
                                String charset, String signType, String proxyHost,
                                int proxyPort) {
        this(appId, format, charset, signType);
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    public AbstractAlipayClient(String appId, String format,
                                String charset, String signType, String encryptType) {
        this(appId, format, charset, signType);
        if (!StringUtils.isEmpty(encryptType)) {
            this.encryptType = encryptType;
        }
    }

    public AbstractAlipayClient(String appId, String format,
                                String charset, String signType,
                                String certPath, String certContent,
                                String alipayPublicCertPath, String alipayPublicCertContent,
                                String rootCertPath, String rootCertContent,
                                String proxyHost, int proxyPort, String encryptType) throws AlipayApiException {
        this(appId, format, charset, signType);
        if (!StringUtils.isEmpty(encryptType)) {
            this.encryptType = encryptType;
        }
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        //读取根证书（用来校验本地支付宝公钥证书失效后自动从网关下载的新支付宝公钥证书是否有效）
        this.rootCertContent = StringUtils.isEmpty(rootCertContent) ? readFileToString(rootCertPath) : rootCertContent;
        //alipayRootCertSN根证书序列号
        if (AlipayConstants.SIGN_TYPE_SM2.equals(signType)) {
            this.alipayRootSm2CertSN = AntCertificationUtil.getRootCertSN(this.rootCertContent, "SM2");
        } else {
            this.alipayRootCertSN = AntCertificationUtil.getRootCertSN(this.rootCertContent);
            if (StringUtils.isEmpty(this.alipayRootCertSN)) {
                throw new AlipayApiException("AlipayRootCert Is Invalid");
            }
        }
        //获取应用证书
        this.cert = StringUtils.isEmpty(certContent) ? AntCertificationUtil.getCertFromPath(certPath)
                : AntCertificationUtil.getCertFromContent(certContent);
        //获取支付宝公钥证书
        X509Certificate alipayPublicCert = StringUtils.isEmpty(alipayPublicCertContent) ?
                AntCertificationUtil.getCertFromPath(alipayPublicCertPath) :
                AntCertificationUtil.getCertFromContent(alipayPublicCertContent);

        //appCertSN为最终发送给网关的应用证书序列号
        this.appCertSN = AntCertificationUtil.getCertSN(cert);
        if (StringUtils.isEmpty(this.appCertSN)) {
            throw new AlipayApiException("AppCert Is Invalid");
        }
        //alipayCertSN为支付宝公钥证书序列号
        this.alipayCertSN = AntCertificationUtil.getCertSN(alipayPublicCert);
        //将公钥证书以序列号为key存入map
        ConcurrentHashMap<String, X509Certificate> alipayPublicCertMap = new ConcurrentHashMap<String, X509Certificate>();
        alipayPublicCertMap.put(alipayCertSN, alipayPublicCert);
        this.alipayPublicCertMap = alipayPublicCertMap;
        //获取支付宝公钥以序列号为key存入map
        PublicKey publicKey = alipayPublicCert.getPublicKey();
        ConcurrentHashMap<String, String> alipayPublicKeyMap = new ConcurrentHashMap<String, String>();
        alipayPublicKeyMap.put(alipayCertSN, Base64.encodeBase64String(publicKey.getEncoded()));
        this.alipayPublicKeyMap = alipayPublicKeyMap;
    }

    private String readFileToString(String rootCertPath) throws AlipayApiException {
        try {
            return FileUtils.readFileToString(new File(rootCertPath));
        } catch (IOException e) {
            throw new AlipayApiException(e);
        }
    }

    /**
     * 组装接口参数，处理加密、签名逻辑
     *
     * @param appCertSN    应用证书序列号
     * @return
     * @throws AlipayApiException
     */
    public RequestParametersHolder getRequestHolderWithSign(RequestTemplate requestTemplate, AlipayRequest request,
                                                            String appCertSN, String targetAppId)
            throws AlipayApiException {
        RequestParametersHolder requestHolder = new RequestParametersHolder();
        Map<String, String> queries = FeignUtils.convert(requestTemplate.queries());
        String accessToken = queries.get(AlipayConstants.ACCESS_TOKEN);
        String appAuthToken = queries.get(AlipayConstants.APP_AUTH_TOKEN);
        queries.remove(AlipayConstants.ACCESS_TOKEN);
        queries.remove(AlipayConstants.APP_AUTH_TOKEN);
        AlipayHashMap appParams = new AlipayHashMap(queries);
        appParams.put(AlipayConstants.BIZ_CONTENT_KEY, new String(requestTemplate.body()));

        Optional<String> needEncrypt = getHeader(requestTemplate, "needEncrypt");
        // 只有新接口和设置密钥才能支持加密
        if (needEncrypt.isPresent() && Boolean.valueOf(needEncrypt.get())) {

            if (StringUtils.isEmpty(appParams.get(AlipayConstants.BIZ_CONTENT_KEY))) {

                throw new AlipayApiException("当前API不支持加密请求");
            }

            // 需要加密必须设置密钥和加密算法
            if (StringUtils.isEmpty(encryptType) || getEncryptor() == null) {

                throw new AlipayApiException("API请求要求加密，则必须设置密钥类型和加密器");
            }

            String encryptContent = getEncryptor().encrypt(
                    appParams.get(AlipayConstants.BIZ_CONTENT_KEY), getEncryptType(), charset);

            appParams.put(AlipayConstants.BIZ_CONTENT_KEY, encryptContent);
        }

        if (!StringUtils.isEmpty(appAuthToken)) {
            appParams.put(AlipayConstants.APP_AUTH_TOKEN, appAuthToken);
        }

        requestHolder.setApplicationParams(appParams);

        if (StringUtils.isEmpty(charset)) {
            charset = AlipayConstants.CHARSET_UTF8;
        }

        AlipayHashMap protocalMustParams = new AlipayHashMap();
        protocalMustParams.put(AlipayConstants.APP_ID, appId);
        protocalMustParams.put(AlipayConstants.SIGN_TYPE, signType);
        putParams(requestTemplate, protocalMustParams,
                Pair.of(AlipayConstants.METHOD, null),
                Pair.of(AlipayConstants.VERSION, request.getApiVersion()),
                Pair.of(AlipayConstants.TERMINAL_TYPE, request.getTerminalType()),
                Pair.of(AlipayConstants.TERMINAL_INFO, request.getTerminalInfo()),
                Pair.of(AlipayConstants.NOTIFY_URL, request.getNotifyUrl()),
                Pair.of(AlipayConstants.RETURN_URL, request.getReturnUrl()),
                Pair.of(AlipayConstants.CHARSET, charset));

        if (!StringUtils.isEmpty(targetAppId)) {
            protocalMustParams.put(AlipayConstants.TARGET_APP_ID, targetAppId);
        }

        if (needEncrypt.isPresent() && Boolean.valueOf(needEncrypt.get())) {
            protocalMustParams.put(AlipayConstants.ENCRYPT_TYPE, getEncryptType());
        }
        //如果应用证书序列号非空，添加应用证书序列号
        if (!StringUtils.isEmpty(appCertSN)) {
            protocalMustParams.put(AlipayConstants.APP_CERT_SN, appCertSN);
        }
        //如果根证书序列号非空，添加根证书序列号
        if (!StringUtils.isEmpty(alipayRootCertSN)) {
            protocalMustParams.put(AlipayConstants.ALIPAY_ROOT_CERT_SN, alipayRootCertSN);
        }
        //如果SM2根证书序列号非空，添加SM2根证书序列号
        if (!StringUtils.isEmpty(alipayRootSm2CertSN)) {
            protocalMustParams.put(AlipayConstants.ALIPAY_ROOT_CERT_SN, alipayRootSm2CertSN);
        }

        Long timestamp = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat(AlipayConstants.DATE_TIME_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone(AlipayConstants.DATE_TIMEZONE));
        protocalMustParams.put(AlipayConstants.TIMESTAMP, df.format(new Date(timestamp)));
        requestHolder.setProtocalMustParams(protocalMustParams);

        AlipayHashMap protocalOptParams = new AlipayHashMap();
        protocalOptParams.put(AlipayConstants.FORMAT, format);
        protocalOptParams.put(AlipayConstants.ACCESS_TOKEN, accessToken);
        protocalOptParams.put(AlipayConstants.ALIPAY_SDK, AlipayConstants.SDK_VERSION);

        putParams(requestTemplate, protocalOptParams, Pair.of(AlipayConstants.PROD_CODE, request.getProdCode()));

        requestHolder.setProtocalOptParams(protocalOptParams);

        if (!StringUtils.isEmpty(signType)) {

            String signContent = AlipaySignature.getSignatureContent(requestHolder);
            protocalMustParams.put(AlipayConstants.SIGN,
                    getSigner().sign(signContent, signType, charset));

        } else {
            protocalMustParams.put(AlipayConstants.SIGN, "");
        }
        return requestHolder;
    }

    private static void putParams(RequestTemplate requestTemplate, AlipayHashMap params, Pair<String, String> ...pairs) {
        for (Pair<String, String> headerKey: pairs) {
            Optional<String> optional = getHeader(requestTemplate, headerKey.getKey());
            if (optional.isPresent()) {
                params.put(headerKey.getKey(), optional.get());
            } else {
                params.put(headerKey.getKey(), headerKey.getValue());
            }
        }
    }

    private static Optional<String> getHeader(RequestTemplate requestTemplate, String key) {
        Collection<String> collection = requestTemplate.headers().get(key);
        if (CollectionUtils.isEmpty(collection)) {
            return Optional.empty();
        }
        return collection.stream().findFirst();
    }

    public abstract Signer getSigner();

    public abstract SignChecker getSignChecker();

    public abstract Encryptor getEncryptor();

    public abstract Decryptor getDecryptor();

}
