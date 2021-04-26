package com.feign.pay.sdk.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * TypeCast class file
 * 类型转换类，避免NumberFormatException
 * Obj2Int、Obj2Long、Obj2Float、Obj2Double、ByteArr2Hex、Str2ByteArr、ByteArr2Str
 *
 * @author 宋欢
 */
public class TypeCast {

    private static final Logger logger = LoggerFactory.getLogger(TypeCast.class);

    /**
     * 字符编码，"US-ASCII"
     */
    public static final String CHARSET = "UTF-8";

    /**
     * Convert Object to Integer
     * 注：
     * TypeCast.toInt(null, -1)); // Return：-1，Not：0
     * TypeCast.toInt("", -1)); // Return：-1，Not：0
     * TypeCast.toInt(4.56, -1)); // Return：4
     * TypeCast.toInt("4.56", -1)); // Return：4
     *
     * @param value        an Object
     * @param defaultValue Value to return if value is null、empty or convert failure.
     * @return Returns the converted value if it exists, or defaultValue
     */
    public static int toInt(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e1) {
            logger.error("toInt() Convert String to Integer failure, value: {}, throwable: {}", value, e1.getMessage());
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (NumberFormatException e2) {
                logger.error("toInt() Convert String to Double failure, value: {}, throwable: {}", value, e2.getMessage());
                return defaultValue;
            }
        }
    }

    /**
     * Convert Object to Long
     * 注：
     * TypeCast.toLong(null, -1)); // Return：-1，Not：0
     * TypeCast.toLong("", -1)); // Return：-1，Not：0
     * TypeCast.toLong(4.56, -1)); // Return：4
     * TypeCast.toLong("4.56", -1)); // Return：4
     *
     * @param value        an Object
     * @param defaultValue Value to return if value is null、empty or convert failure.
     * @return Returns the converted value if it exists, or defaultValue
     */
    public static long toLong(Object value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e1) {
            logger.error("toLong() Convert String to Long failure, value: {}, throwable: {}", value, e1.getMessage());
            try {
                return Double.valueOf(value.toString()).longValue();
            } catch (NumberFormatException e2) {
                logger.error("toLong() Convert String to Double failure, value: {}, throwable: {}", value, e2.getMessage());
                return defaultValue;
            }
        }
    }

    /**
     * Convert Object to Float
     * 注：
     * TypeCast.toFloat(null, -1.0f)); // Return：-1.0f，Not：0
     * TypeCast.toFloat("", -1.0f)); // Return：-1.0f，Not：0
     *
     * @param value        an Object
     * @param defaultValue Value to return if value is null、empty or convert failure.
     * @return Returns the converted value if it exists, or defaultValue
     */
    public static float toFloat(Object value, float defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            return Float.parseFloat(value.toString());
        } catch (NumberFormatException e) {
            logger.error("toFloat() Convert String to Float failure, value: {}, throwable: {}", value, e.getMessage());
            return defaultValue;
        }
    }

    /**
     * Convert Object to Double
     * 注：
     * TypeCast.toDouble(null, -1.0)); // Return：-1.0，Not：0
     * TypeCast.toDouble("", -1.0)); // Return：-1.0，Not：0
     *
     * @param value        an Object
     * @param defaultValue Value to return if value is null、empty or convert failure.
     * @return Returns the converted value if it exists, or defaultValue
     */
    public static double toDouble(Object value, double defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            logger.error("toDouble() Convert String to Double failure, value: {}, throwable: {}", value, e.getMessage());
            return defaultValue;
        }
    }

    /**
     * Convert Bytes to Hex String
     *
     * @param value a Byte Array
     * @return Returns the converted value if it exists, or null
     */
    public static String toHex(byte[] value) {
        if (value == null) {
            logger.error("toHex() Convert Bytes to Hex failure, value is null");
            return null;
        }

        BigInteger bInteger = new BigInteger(1, value);
        return bInteger.toString(16);
    }

    /**
     * Convert String to Bytes
     *
     * @param value a String
     * @return Returns the converted value if it exists, or null
     */
    public static byte[] toBytes(String value) {
        if (value == null) {
            logger.error("toBytes() Convert String to Bytes failure, value is null");
            return null;
        }

        try {
            return value.getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            logger.error("toBytes() Convert String to Bytes failure, value: {}, throwable: {}", value, e.getMessage());
        }

        return null;
    }

    /**
     * Convert Bytes to String
     *
     * @param value a Byte Array
     * @return Returns the converted value if it exists, or null
     */
    public static String toString(byte[] value) {
        if (value == null) {
            logger.error("toString() Convert Bytes to String failure, value is null");
            return null;
        }

        try {
            return new String(value, CHARSET);
        } catch (UnsupportedEncodingException e) {
            logger.error("toString() Convert Bytes to String failure, throwable: {}", e.getMessage());
        }

        return null;
    }

}
