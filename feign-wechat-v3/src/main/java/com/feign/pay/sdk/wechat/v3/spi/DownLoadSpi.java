package com.feign.pay.sdk.wechat.v3.spi;

import com.feign.pay.sdk.common.util.feign.AbstractConfig;
import feign.Logger;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;

/**
 * @seehttp://coupon-center.zvcms.com/doc.html
 * @author wencheng
 * @date 2020/12/22
 */
@FeignClient(name = "downLoadSpi",
        url = "https://images.qmai.cn",
        configuration = DownLoadSpi.Config.class
)
public interface DownLoadSpi {

    /**
     * 文件下载
     * @param uri
     * @return
     */
    @GetMapping(value = "")
    Response download(URI uri);

    /**
     *
     * @param uri
     * @return
     */
    @GetMapping(value = "")
    String load(URI uri);

    class Config extends AbstractConfig {

        @Bean
        public Logger.Level feignLoggerLevel() {
            return Logger.Level.FULL;
        }
    }

}
