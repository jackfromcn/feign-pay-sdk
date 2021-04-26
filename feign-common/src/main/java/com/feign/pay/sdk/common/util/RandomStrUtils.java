package com.feign.pay.sdk.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @program: baking-apiserver
 * @description: 订单号随机
 * @author: Savey
 * @create: 2020-12-25 16:33
 */
public class RandomStrUtils {

    /**
     * 烘焙订单号
     * @return String
     */
    public static String uniqueOrderNo(Long userId) {
        // 用户ID后两位
        int length = 2;
        String first = Long.toString(userId).length() >= length ? Long.toString(userId).substring(Long.toString(userId).length() - length) : String.format("%02d", userId);

        // 时间位 13 位
        SimpleDateFormat df = new SimpleDateFormat("MMddHHmmssSSS");
        String second = df.format(new Date());

        // 随机数位 5 位
        String third = "";
        int thirdLength = 5;
        char[] str = "0123456789".toCharArray();
        for(int i = 0; i < thirdLength; i++) {
            int indexNumber = (int) (Math.random() * str.length);
            third += str[indexNumber];
        }

        // 共20位单号
        return first + second + third;
    }

    /**
     * 烘焙退款订单号
     * @return String
     */
    public static String uniqueRefundNo(Long userId) {
        return uniqueOrderNo(userId);
    }

    /**
     * 随机数
     * @return String
     */
    public static String uniqueTakeCode() {
        int first = new Random(10).nextInt(1) + 1;
        int hashCodeV = Math.abs(UUID.randomUUID().toString().hashCode());
        return first + String.format("%011d", hashCodeV);
    }



    /**
     *  提货码
     * @return String
     */
    public static String uniqueTakeCode(Long sellerId) {
        int first = new Random(10).nextInt(1) + 1;
        int hashCodeV = Math.abs(UUID.randomUUID().toString().hashCode());
        return "2350" + first + String.format("%011d", hashCodeV);
    }
}
