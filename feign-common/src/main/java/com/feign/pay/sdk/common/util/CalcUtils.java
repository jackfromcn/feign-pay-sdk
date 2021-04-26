package com.feign.pay.sdk.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @program: baking-apiserver
 * @description: + - x %
 * @author: Savey
 * @create: 2020-12-31 20:05
 */
public class CalcUtils {

    /**
     * 加法
     * @param leftOperand
     * @param rightOperand
     * @return
     */
    public static String add(String leftOperand, String rightOperand) {
        if (Objects.isNull(leftOperand) || Objects.isNull(rightOperand)) {
            return null;
        }
        BigDecimal left = new BigDecimal(leftOperand);
        BigDecimal right = new BigDecimal(rightOperand);

        return left.add(right).setScale(2, RoundingMode.DOWN).toString();
    }

    /**
     * 减法
     * @param leftOperand
     * @param rightOperand
     * @return
     */
    public static String sub(String leftOperand, String rightOperand) {
        if (Objects.isNull(leftOperand) || Objects.isNull(rightOperand)) {
            return null;
        }

        BigDecimal left = new BigDecimal(leftOperand);
        BigDecimal right = new BigDecimal(rightOperand);

        return left.subtract(right).setScale(2, RoundingMode.DOWN).toString();
    }

    /**
     * 乘法
     * @param leftOperand
     * @param rightOperand
     * @return
     */
    public static String multi(String leftOperand, String rightOperand) {

        BigDecimal left = new BigDecimal(leftOperand);
        BigDecimal right = new BigDecimal(rightOperand);

        return left.multiply(right).setScale(2, RoundingMode.DOWN).toString();
    }

    /**
     * 除法
     * @param leftOperand
     * @param rightOperand
     * @return
     */
    public static String div(String leftOperand, String rightOperand) {
        BigDecimal left = new BigDecimal(leftOperand);
        BigDecimal right = new BigDecimal(rightOperand);

        return left.divide(right, 2, RoundingMode.DOWN).toString();
    }
}
