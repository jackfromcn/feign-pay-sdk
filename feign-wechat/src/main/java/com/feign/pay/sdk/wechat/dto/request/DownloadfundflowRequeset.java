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
public class DownloadfundflowRequeset extends BaseRequest {
    /**
     * 对账单日期
     * 下载对账单的日期，格式：20140603
     */
    @JacksonXmlProperty(localName = "bill_date")
    private String billDate;

    /**
     * 资金账户类型
     * 账单的资金来源账户：
     * Basic  基本账户
     * Operation 运营账户
     * Fees 手续费账户
     */
    @JacksonXmlProperty(localName = "account_type")
    private String accountType;

    /**
     * 压缩账单
     * 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
     */
    @JacksonXmlProperty(localName = "tar_type")
    private String tarType;

}
