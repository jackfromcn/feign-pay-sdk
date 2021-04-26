package com.feign.pay.sdk.common.util;

import java.util.Base64;

/**
 * UrlSafeBase64 class file
 * URL安全的Base64加解密类
 * 还原和去掉Base64密文中的+、/、=
 * 注：
 * java.util.Base64.getUrlDecoder().decode(debug.zvcms.com-测试token) 正常
 * java.util.Base64.getUrlDecoder().decode(网关-token) 报 Illegal base64 character 2b 错误
 *
 * @author 宋欢
 */
public class UrlSafeBase64 {
    /**
     * 用于填充Base64长度的等于号
     */
    static final String EQUAL_SIGN_4 = "====";

    static final Base64.Encoder ENCODER = Base64.getEncoder();

    static final Base64.Decoder DECODER = Base64.getDecoder();

    /**
     * Base64解密，URL安全转义
     *
     * @param value 密文
     * @return 明文，字节数组，或null
     */
    public static byte[] decode(String value) {
        value = urlDecode(value);
        if (value == null) {
            return null;
        }

        return DECODER.decode(value);
    }

    /**
     * Base64加密，URL安全转义
     *
     * @param value 明文
     * @return 密文，字符串，或null
     */
    public static String encode(byte[] value) {
        String result = ENCODER.encodeToString(value);
        if (result == null) {
            return null;
        }

        return urlEncode(result);
    }

    /**
     * 转义URL
     * 还原Base64密文中的+、/、=
     *
     * @param value 待转义字符串
     * @return 转义后字符串，或null
     */
    public static String urlDecode(String value) {
        if (value == null) {
            return null;
        }

        int size = value.length() % 4;
        if (size > 0) {
            // 增加 (4 - size) 个 "="
            value += EQUAL_SIGN_4.substring(size);
        }

        return value.replace('-', '+').replace('_', '/');
    }

    /**
     * 转义URL
     * 去掉Base64密文中的+、/、=
     *
     * @param value 待转义字符串
     * @return 转义后字符串，或null
     */
    public static String urlEncode(String value) {
        if (value == null) {
            return null;
        }

        return value
                .replace('+', '-')
                .replace('/', '_')
                .replace("=", "");
    }

}
