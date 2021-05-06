package com.feign.pay.sdk.wechat.spi;

import com.feign.pay.sdk.common.util.JsonUtil;
import com.feign.pay.sdk.wechat.dto.request.OrderRequest;
import com.feign.pay.sdk.wechat.dto.request.RefundQueryRequest;
import com.feign.pay.sdk.wechat.dto.response.OrderQueryResponse;
import com.feign.pay.sdk.wechat.dto.response.RefundQueryResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by wencheng on 2020/12/22.
 *
 * @author wencheng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatSpiTest {

    @Autowired
    private WechatSpi wechatSpi;

    @Test
    public void test() {
        OrderRequest request = new OrderRequest();
        request.setAppId("wx95f37bd995875ae0");
        request.setMchId("1495139482");
        request.setTransactionId("1008450740201411110005820873");
        request.setOutTradeNo(null);
        request.setSignType("MD5");

        OrderQueryResponse response = wechatSpi.queryOrder(request);
        System.out.println(JsonUtil.toJson(response));
    }

    @Test
    public void refund() {
        RefundQueryRequest request = new RefundQueryRequest();
        request.setTransactionId("1008450740201411110005820873");
        request.setOutTradeNo(null);
        request.setOutRefundNo(null);
        request.setRefundId(null);
        request.setOffset(null);
        request.setAppId("wx95f37bd995875ae0");
        request.setMchId("1495139482");
        request.setSignType("MD5");
        RefundQueryResponse map = wechatSpi.queryRefund(request);
        System.out.println(JsonUtil.toJson(map));
    }


}
