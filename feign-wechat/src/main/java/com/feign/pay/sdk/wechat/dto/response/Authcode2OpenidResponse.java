package com.feign.pay.sdk.wechat.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/29
 */
@Data
@JacksonXmlRootElement(localName = "xml")
public class Authcode2OpenidResponse extends BaseResponse {

    /**
     * 用户标识
     * 用户在商户appid 下的唯一标识
     */
    @JacksonXmlProperty(localName = "openid")
    private String openid;

}
