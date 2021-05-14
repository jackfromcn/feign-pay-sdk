package com.feign.pay.sdk.wechat.v3.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wp
 * @date 2021/05/12
 * @description 微信v3上传图片请求使用
 */

@Data
public class WeChatUploadRequest implements Serializable {

    /**
     * 文件路径
     */
    private String filePath;
}
