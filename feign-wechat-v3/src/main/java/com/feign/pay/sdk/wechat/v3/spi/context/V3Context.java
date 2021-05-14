package com.feign.pay.sdk.wechat.v3.spi.context;

import com.feign.pay.sdk.common.util.SpringContextUtil;
import com.feign.pay.sdk.wechat.v3.config.WechatProperties;
import com.feign.pay.sdk.wechat.v3.spi.DownLoadSpi;
import com.feign.pay.sdk.wechat.v3.spi.context.sdk.util.PemUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.security.PrivateKey;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author wencheng
 * @date 2021/5/7
 */
@Slf4j
public class V3Context {
    private AutoUpdateCertificatesVerifier verifier;
    private PrivateKey privateKey;
    private String privateKeyStr;

    private static final LoadingCache<WechatProperties, V3Context> verifierLoadingCache = CacheBuilder.newBuilder().maximumSize(100)
            //定时刷新
            .refreshAfterWrite(1, TimeUnit.DAYS)
            //移除监听
            .removalListener((RemovalListener<WechatProperties, V3Context>) removalNotification
                    -> log.info("V3Context[cacheBuilder] key {} msg {} ", removalNotification.getKey(), "移除缓存"))
            .build(new CacheLoader<WechatProperties, V3Context>() {
                @Override
                public V3Context load(WechatProperties config) {
                    try {
                        String mchId = config.getMchId();
                        String mchSerialNo = config.getMchSerialNo();
                        String apiV3Key = config.getApiV3Key();

                        DownLoadSpi loadSpi = SpringContextUtil.getBean(DownLoadSpi.class);
                        // TODO: 2021/5/8
                        String privateKeyStr = loadSpi.load(new URI(config.getCertStreamUrl()));
                        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(privateKeyStr);

                        //使用自动更新的签名验证器，不需要传入证书
                        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                                new WechatPay2Credentials(mchId, new PrivateKeySigner(mchSerialNo, merchantPrivateKey)),
                                apiV3Key.getBytes("utf-8"));
                        V3Context context = new V3Context();
                        context.privateKey = merchantPrivateKey;
                        context.privateKeyStr = privateKeyStr;
                        context.verifier = verifier;
                        return context;
                    } catch (Exception e) {
                        return null;
                    }
                }
            });

    public static V3Context getConfig(WechatProperties properties) {
        return verifierLoadingCache.getUnchecked(properties);
    }

    public AutoUpdateCertificatesVerifier getVerifier() {
        return verifier;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public String getPrivateKeyStr() {
        return privateKeyStr;
    }
}
