package com.feign.pay.sdk.wechat.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/20
 */
@Data
@JacksonXmlRootElement(localName = "xml")
public class ReverseResponse extends BaseResponse {

    /**
     * 是否重调
     * 是否需要继续调用撤销，Y-需要，N-不需要
     */
    @JacksonXmlProperty(localName = "recall")
    private String recall;

}
