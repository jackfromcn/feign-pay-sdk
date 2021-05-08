package com.feign.pay.sdk.wechat.v3.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.feign.pay.sdk.common.util.SpringContextUtil;
import com.feign.pay.sdk.wechat.v3.spi.context.V3Context;
import com.wechat.pay.contrib.apache.httpclient.util.RsaCryptoUtil;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;

/**
 * @program: baking-apiserver
 * @description:
 * @author: Savey
 * @create: 2021-01-21 21:17
 */
public class EncryptSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isBlank(s)) {
            return;
        }
        try {
            WechatProperties properties = SpringContextUtil.getBean(WechatProperties.class);
            V3Context config = V3Context.getConfig(properties);
            String text = RsaCryptoUtil.encryptOAEP(s, config.getVerifier().getValidCertificate());
            jsonGenerator.writeString(text);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
