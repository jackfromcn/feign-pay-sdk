package com.feign.pay.sdk.wechat.dto.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.feign.pay.sdk.wechat.spi.context.WXPayConstants.SignType;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.*;

/**
 *
 * @author wencheng
 * @date 2021/4/20
 */
@Data
@JacksonXmlRootElement(localName = "xml")
public abstract class BaseRequest {

    /**
     * 公众账号ID
     * 微信支付分配的公众账号ID（企业号corpid即为此appId）
     */
    @JacksonXmlProperty(localName = "appid")
    private String appId;

    /**
     * 商户号
     * 微信支付分配的商户号
     */
    @JacksonXmlProperty(localName = "mch_id")
    private String mchId;

    /**
     * 随机字符串
     * 随机字符串，不长于32位。推荐随机数生成算法
     */
    @JacksonXmlProperty(localName = "nonce_str")
    private String nonceStr;

    /**
     * 签名
     * 通过签名算法计算得出的签名值，详见签名生成算法
     */
    @JacksonXmlProperty(localName = "sign")
    private String sign;

    /**
     * 签名类型
     * HMAC-SHA256	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
     */
    @JacksonXmlProperty(localName = "sign_type")
    private String signType = WxPayConstants.MD5;

    public BaseRequest() {
        this.nonceStr = WxPayUtil.generateNonceStr();
    }

    public String getSign() {
        try {
            Map<String, String> params = convertObjToMap();
            SignType signType = SignType.valueOf(this.signType);
            return WxPayUtil.generateSignature(params, "zhimaikejigufenyouxiangongsi2017", signType);
        } catch (Exception e) {}
        return null;
    }

    public Map<String, String> convertObjToMap() {
        Map<String, String> reMap = new HashMap<>(16);
        List<Field> fields = new ArrayList<>(10);
        List<Field> childFields;
        List<String> fieldsName = new ArrayList<>(10);
        Class tempClass = this.getClass();

        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            //得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }
        childFields = Arrays.asList(this.getClass().getDeclaredFields());
        for (Field field : childFields) {
            fieldsName.add(field.getName());
        }
        try {
            for (Field field : fields) {
                try {
                    JacksonXmlProperty annotation = field.getDeclaredAnnotation(JacksonXmlProperty.class);
                    if (Objects.isNull(annotation)) {
                        continue;
                    }
                    if (fieldsName.contains(field.getName())) {
                        Field f = this.getClass().getDeclaredField(
                                field.getName());
                        f.setAccessible(true);
                        Object o = f.get(this);
                        if (Objects.isNull(o)) {
                            continue;
                        }
                        reMap.put(annotation.localName(), o.toString());
                    } else {
                        Field f = this.getClass().getSuperclass().getDeclaredField(
                                field.getName());
                        f.setAccessible(true);
                        Object o = f.get(this);
                        if (Objects.isNull(o)) {
                            continue;
                        }
                        reMap.put(annotation.localName(), o.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return reMap;
    }

}
