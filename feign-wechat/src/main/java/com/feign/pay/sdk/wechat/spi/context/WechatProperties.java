package com.feign.pay.sdk.wechat.spi.context;

import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/25
 */
@Data
public class WechatProperties {

    private String mchId;

    private String certStreamUrl;

}
