package com.feign.pay.sdk.wechat.v3.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wp
 * @date 2021/05/11
 * @description 微信进件状态查询返回使用
 */

@Data
public class WeChatApplyMentStatusResponse implements Serializable {

    /**
     *业务申请编号
     */
    @JsonProperty(value = "business_code")
    private String businessCode;

    /**
     *微信支付申请单号
     */
    @JsonProperty(value = "applyment_id")
    private long applyMentId;

    /**
     *特约商户号
     */
    @JsonProperty(value = "sub_mchid")
    private String subMchid;

    /**
     *超级管理员签约链接
     */
    @JsonProperty(value = "sign_url")
    private String signUrl;

    /**
     *申请单状态
     * 1、APPLYMENT_STATE_EDITTING（编辑中）：提交申请发生错误导致，请尝试重新提交。
     * 2、APPLYMENT_STATE_AUDITING（审核中）：申请单正在审核中，超级管理员用微信打开“签约链接”，完成绑定微信号后，申请单进度将通过微信公众号通知超级管理员，引导完成后续步骤。
     * 3、APPLYMENT_STATE_REJECTED（已驳回）：请按照驳回原因修改申请资料，超级管理员用微信打开“签约链接”，完成绑定微信号，后续申请单进度将通过微信公众号通知超级管理员。
     * 4、APPLYMENT_STATE_TO_BE_CONFIRMED（待账户验证）：请超级管理员使用微信打开返回的“签约链接”，根据页面指引完成账户验证。
     * 5、APPLYMENT_STATE_TO_BE_SIGNED（待签约）：请超级管理员使用微信打开返回的“签约链接”，根据页面指引完成签约。
     * 6、APPLYMENT_STATE_SIGNING（开通权限中）：系统开通相关权限中，请耐心等待。
     * 7、APPLYMENT_STATE_FINISHED（已完成）：商户入驻申请已完成。
     * 8、APPLYMENT_STATE_CANCELED（已作废）：申请单已被撤销。
     * 示例值：APPLYMENT_STATE_FINISHED
     */
    @JsonProperty(value = "applyment_state")
    private String applyMentState;

    /**
     *申请状态描述
     */
    @JsonProperty(value = "applyment_state_msg")
    private String applyMentStateMsg;

    /**
     *驳回原因详情
     */
    @JsonProperty(value = "audit_detail")
    private List<AuditDetailBean> auditDetail;

    @Data
    public static class AuditDetailBean implements Serializable{
        /**
         * field : id_card_copy
         * field_name : 身份证复印件
         * reject_reason : 身份证背面识别失败，请上传更清晰的身份证图片。
         */
        /**
         * 字段名
         */
        private String field;
        /**
         * 字段名称
         */
        @JsonProperty(value = "field_name")
        private String fieldName;
        /**
         * 驳回原因
         */
        @JsonProperty(value = "reject_reason")
        private String rejectReason;


    }
}
