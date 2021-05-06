package com.feign.pay.sdk.wechat.dto.request;

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
public class TradeReportRequest extends BaseRequest {

    /**
     * 接口URL
     * 刷卡支付终端上报统一填：https://api.mch.weixin.qq.com/pay/batchreport/micropay/total
     */
    @JacksonXmlProperty(localName = "interface_url")
    private String interfaceUrl;

    /**
     * 访问接口IP
     * 发起接口调用时的机器IP
     */
    @JacksonXmlProperty(localName = "user_ip")
    private String userIp;

    /**
     * 上报数据包
     * POS机采集的交易信息列表，使用JSON格式的数组，每条交易包含：
     * 1. out_trade_no 商户订单号
     * 2. begin_time 交易开始时间（扫码时间）
     * 3. end_time 交易完成时间
     * 4. state 交易结果
     *  OK   -成功
     *  FAIL -失败
     *  CANCLE-取消
     * 5. err_msg 自定义的错误描述信息
     *注意，将JSON数组的文本串放到XML节点中时，需要使用!CDATA[]标签将JSON文本串保护起来
     */
    @JacksonXmlProperty(localName = "trades")
    private String trades;
}
