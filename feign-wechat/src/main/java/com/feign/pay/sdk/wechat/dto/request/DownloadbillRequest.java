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
public class DownloadbillRequest extends BaseRequest {

    /**
     * 对账单日期
     * 下载对账单的日期，格式：20140603
     */
    @JacksonXmlProperty(localName = "bill_date")
    private String billDate;

    /**
     * 账单类型
     * ALL（默认值），返回当日所有订单信息（不含充值退款订单）
     * SUCCESS，返回当日成功支付的订单（不含充值退款订单）
     * REFUND，返回当日退款订单（不含充值退款订单）
     * RECHARGE_REFUND，返回当日充值退款订单
     */
    @JacksonXmlProperty(localName = "bill_type")
    private String billType;

    /**
     * 压缩账单
     * 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
     */
    @JacksonXmlProperty(localName = "tar_type")
    private String tarType;

}
