package com.feign.pay.sdk.wechat.spi;

import com.feign.pay.sdk.common.util.JsonUtil;
import com.feign.pay.sdk.wechat.dto.request.QueryRequest;
import com.feign.pay.sdk.wechat.dto.request.RefundQueryRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * Created by wencheng on 2020/12/22.
 *
 * @author wencheng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatSpiTest {

    @Autowired
    private WechatPaySpi wechatPaySpi;

    @Test
    public void test() {
        QueryRequest request = new QueryRequest();
        request.setAppId("wx95f37bd995875ae0");
        request.setMchId("1495139482");
        request.setTransactionId("1008450740201411110005820873");
        request.setOutTradeNo(null);
        request.setSignType("MD5");

        QueryResponse response = wechatPaySpi.queryOrder(request);
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
        Map<String, Object> map = wechatPaySpi.queryRefund(request);
        System.out.println(JsonUtil.toJson(map));
    }


}
