package com.feign.pay.sdk.wechat.v3.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.feign.pay.sdk.common.util.SpringContextUtil;
import com.feign.pay.sdk.wechat.v3.spi.context.V3Context;
import com.wechat.pay.contrib.apache.httpclient.util.RsaCryptoUtil;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import java.io.IOException;

/**
 *
 * @author wencheng
 * @date 2021/1/5
 */
public class DecryptDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String str = jsonParser.getText();
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            WechatProperties properties = SpringContextUtil.getBean(WechatProperties.class);
            V3Context config = V3Context.getConfig(properties);
            return RsaCryptoUtil.decryptOAEP(str, config.getPrivateKey());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
