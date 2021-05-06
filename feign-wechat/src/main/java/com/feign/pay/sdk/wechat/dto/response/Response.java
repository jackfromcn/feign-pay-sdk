package com.feign.pay.sdk.wechat.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author wencheng
 * @date 2021/4/20
 */
@Slf4j
@Data
@JacksonXmlRootElement(localName = "xml")
public class Response {

    private static final String SUCCESS = "SUCCESS";

    private static final String FAIL = "FAIL";

    /**
     * 返回状态码
     * SUCCESS/FAIL
     * 此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断
     */
    @JacksonXmlProperty(localName = "return_code")
    private String returnCode;

    /**
     * 返回信息
     * 当return_code为FAIL时返回信息为错误原因 ，例如
     * 签名失败
     * 参数格式校验错误
     */
    @JacksonXmlProperty(localName = "return_msg")
    private String returnMsg;
}
