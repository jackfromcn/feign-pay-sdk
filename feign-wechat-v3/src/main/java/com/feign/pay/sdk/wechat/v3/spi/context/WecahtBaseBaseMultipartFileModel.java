package com.feign.pay.sdk.wechat.v3.spi.context;

import com.feign.pay.sdk.common.util.feign.BaseBaseMultipartFileModel;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;

/**
 *
 * @author wencheng
 * @date 2021/5/7
 */
@Data
public class WecahtBaseBaseMultipartFileModel extends BaseBaseMultipartFileModel {

    @Override
    public LinkedHashMap<String, ?> extParamMap() {
        try {
            MultipartFile file = getFile();
            String fileSha256 = DigestUtils.sha256Hex(file.getBytes());
            String fileName = file.getOriginalFilename();
            String meta = String.format("{\"filename\":\"%s\",\"sha256\":\"%s\"}", fileName, fileSha256);
            LinkedHashMap<String, Object> paramMap = new LinkedHashMap<>();
            paramMap.put("meta", meta);
            return paramMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
