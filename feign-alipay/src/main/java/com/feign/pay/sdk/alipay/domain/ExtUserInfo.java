package com.feign.pay.sdk.alipay.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/22
 */
@Data
public class ExtUserInfo {

    /**
     * 证件号

     注：need_check_info=T时该参数才有效
     */
    @JsonProperty("cert_no")
    private String certNo;

    /**
     * 身份证：IDENTITY_CARD、护照：PASSPORT、军官证：OFFICER_CARD、士兵证：SOLDIER_CARD、户口本：HOKOU等。如有其它类型需要支持，请与蚂蚁金服工作人员联系。

     注： need_check_info=T时该参数才有效
     */
    @JsonProperty("cert_type")
    private String certType;

    /**
     * 是否强制校验付款人身份信息
     T:强制校验，F：不强制
     */
    @JsonProperty("fix_buyer")
    private String fixBuyer;

    /**
     * 允许的最小买家年龄，买家年龄必须大于等于所传数值 
     注：
     1. need_check_info=T时该参数才有效
     2. min_age为整数，必须大于等于0
     */
    @JsonProperty("min_age")
    private String minAge;

    /**
     * 手机号
     注：该参数暂不校验
     */
    @JsonProperty("mobile")
    private String mobile;

    /**
     * 姓名

     注： need_check_info=T时该参数才有效
     */
    @JsonProperty("name")
    private String name;

    /**
     * 是否强制校验身份信息
     T:强制校验，F：不强制
     */
    @JsonProperty("need_check_info")
    private String needCheckInfo;
    
}
