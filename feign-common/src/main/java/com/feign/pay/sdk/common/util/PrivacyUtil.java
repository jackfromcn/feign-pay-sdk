package com.feign.pay.sdk.common.util;


import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: PrivacyUtil
 * @author: Wu.bo
 * @date: 2021/3/16 13:49
 * @version: 1.0
 * @since:
 * @description: 身份证和手机号加星
 */
public class PrivacyUtil {

    /**
     * 用户身份证号码的打码隐藏加星号加*
     *
     * @return 处理完成的身份证
     */
    public static String idCardMask(String idCardNum) {
        String res = "";
        if (StringUtils.isNotEmpty(idCardNum)) {
            StringBuilder stringBuilder = new StringBuilder(idCardNum);
            res = stringBuilder.replace(6, 14, "********").toString();
        }
        return res;
    }

    /**
     * 用户电话号码的打码隐藏加星号加*
     *
     * @return 处理完成的身份证
     */
    public static String phoneMask(String phone) {
        String res = "";
        if (StringUtils.isNotEmpty(phone)) {
            StringBuilder stringBuilder = new StringBuilder(phone);
            res = stringBuilder.replace(3, 7, "****").toString();
        }
        return res;
    }
}
