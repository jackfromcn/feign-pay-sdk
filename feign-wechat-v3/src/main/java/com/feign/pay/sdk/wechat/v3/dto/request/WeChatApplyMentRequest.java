package com.feign.pay.sdk.wechat.v3.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.feign.pay.sdk.wechat.v3.config.EncryptSerializer;
import lombok.Data;

import java.util.List;


/**
 * @author wp
 * @date 2021/05/11
 * @description 微信进件请求使用
 */

@Data
public class WeChatApplyMentRequest {
    /**
     * 业务申请编号
     * 是否必须-是
     */
    @JsonProperty(value = "business_code")
    private String businessCode;
    /**
     * 超级管理员信息
     * 是否必须-是
     */
    @JsonProperty(value = "contact_info")
    private ContactInfoBean contactInfo;
    /**
     * -主体资料
     * 是否必须-是
     */
    @JsonProperty(value = "subject_info")
    private SubjectInfoBean subjectInfo;
    /**
     * 经营资料
     * 是否必须-是
     */
    @JsonProperty(value = "business_info")
    private BusinessInfoBean businessInfo;
    /**
     * -结算规则
     * 是否必须-是
     */
    @JsonProperty(value = "settlement_info")
    private SettlementInfoBean settlementInfo;
    /**
     * -结算银行账户
     * 是否必须-是
     */
    @JsonProperty(value = "bank_account_info")
    private BankAccountInfoBean bankAccountInfo;
    /**
     * 补充材料
     * 是否必须-否
     */
    @JsonProperty(value = "addition_info")
    private AdditionInfoBean additionInfo;

    @Data
    public static class ContactInfoBean {
        /**
         * 超级管理员姓名
         * 是否必须-是
         */
        @JsonProperty(value = "contact_name")
        @JsonSerialize(using = EncryptSerializer.class)
        private String contactName;
        /**
         * 超级管理员身份证件号码
         * 是否必须-否
         */
        @JsonProperty(value = "contact_id_number")
        @JsonSerialize(using = EncryptSerializer.class)
        private String contactIdNumber;
        /**
         * 超级管理员微信openid
         * 是否必须-否
         */
        @JsonSerialize(using = EncryptSerializer.class)
        private String openid;
        /**
         * 联系手机
         * 是否必须-是
         */
        @JsonProperty(value = "mobile_phone")
        @JsonSerialize(using = EncryptSerializer.class)
        private String mobilePhone;
        /**
         * 联系邮箱
         * 是否必须-是
         */
        @JsonProperty(value = "contact_email")
        @JsonSerialize(using = EncryptSerializer.class)
        private String contactEmail;

    }

    @Data
    public static class SubjectInfoBean {
        /**
         * 主体类型需与营业执照/登记证书上一致
         * 是否必须-是
         */
        @JsonProperty(value = "subject_type")
        private String subjectType;
        /**
         * +小微辅助证明材料
         * 是否必须-是
         */
        @JsonProperty(value = "micro_biz_info")
        private MicroBizInfo microBizInfo;
        /**
         * 1、主体为个体户/企业，必填。
         * 2、请上传“营业执照”，需年检章齐全，当年注册除外。
         */
        @JsonProperty(value = "business_license_info")
        private BusinessLicenseInfoBean businessLicenseInfo;
        /**
         * 主体为党政、机关及事业单位/其他组织，必填。
         * 1、党政、机关及事业单位：请上传相关部门颁发的证书，如：事业单位法人证书、统一社会信用代码证书。
         * 2、其他组织：请上传相关部门颁发的证书，如：社会团体法人登记证书、民办非企业单位登记证书、基金会法人登记证书。
         */
        @JsonProperty(value = "certificate_info")
        private CertificateInfoBean certificateInfo;
        /**
         * 主体为企业/党政、机关及事业单位/其他组织，且证件号码不是18位时必填。
         */
        @JsonProperty(value = "organization_info")
        private OrganizationInfoBean organizationInfo;
        /**
         * 1、主体类型为党政、机关及事业单位选传;
         * （1）若上传，则审核通过后即可签约，无需汇款验证。
         * （2）若未上传，则审核通过后，需汇款验证。
         * 2、主体为个体户、企业、其他组织等，不需要上传本字段。
         * 3、请参照示例图打印单位证明函，全部信息需打印，不支持手写商户信息，并加盖公章。
         * 4、可上传1张图片，请填写通过图片上传API预先上传图片生成好的MediaID。
         */
        @JsonProperty(value = "certificate_letter_copy")
        private String certificateLetterCopy;
        /**
         * 1、个体户：请上传经营者的身份证件。
         * 2、企业/党政、机关及事业单位/其他组织：请上传法人的身份证件。
         */
        @JsonProperty(value = "identity_info")
        private IdentityInfoBean identityInfo;
        /**
         * 若经营者/法人不是最终受益所有人，则需提填写受益所有人信息。
         * 根据国家相关法律法规，需要提供公司受益所有人信息，受益所有人需符合至少以下条件之一：
         * 1、直接或者间接拥有超过25%公司股权或者表决权的自然人。
         * 2、通过人事、财务等其他方式对公司进行控制的自然人。
         * 3、公司的高级管理人员，包括公司的经理、副经理、财务负责人、上市公司董事会秘书和公司章程规定的其他人员
         */
        @JsonProperty(value = "ubo_info")
        private UboInfoBean uboInfo;

        @Data
        public static class MicroBizInfo {


            /**
             * 请选择实际的小微经营场景，单选
             * 枚举值：
             * 1、MICRO_TYPE_STORE：门店场所
             * 2、MICRO_TYPE_MOBILE：流动经营/便民服务
             * 3、MICRO_TYPE_ONLINE：线上商品/服务交易
             *
             */
            @JsonProperty(value = "micro_biz_type")
            private String microBizType;
            /**
             * 经营类型为“门店场所”时填写
             */
            @JsonProperty(value = "micro_store_info")
            private MicroStoreInfoBean microStoreInfo;
            /**
             * 经营类型为“流动经营/便民服务”时填写
             */
            @JsonProperty(value = "micro_mobile_info")
            private MicroMobileInfoBean microMobileInfo;
            /**
             * 经营场景为“线上商品/服务交易”时填写
             */
            @JsonProperty(value = "micro_online_info")
            private MicroOnlineInfoBean microOnlineInfo;

            @Data
            public static class MicroStoreInfoBean {

                @JsonProperty(value = "micro_name")
                private String microName;
                @JsonProperty(value = "micro_address_code")
                private String microAddressCode;
                @JsonProperty(value = "micro_address")
                private String microAddress;
                @JsonProperty(value = "store_entrance_pic")
                private String storeEntrancePic;
                @JsonProperty(value = "micro_indoor_copy")
                private String microIndoorCopy;
                @JsonProperty(value = "store_longitude")
                private String storeLongitude;
                @JsonProperty(value = "store_latitude")
                private String storeLatitude;
                @JsonProperty(value = "address_certification")
                private String addressCertification;

            }

            @Data
            public static class MicroMobileInfoBean {
                @JsonProperty(value = "micro_mobile_name")
                private String microMobileName;
                @JsonProperty(value = "micro_mobile_city")
                private String microMobileCity;
                @JsonProperty(value = "micro_mobile_address")
                private String microMobileAddress;
                @JsonProperty(value = "micro_mobile_pics")
                private List<String> microMobilePics;


            }

            @Data
            public static class MicroOnlineInfoBean {
                @JsonProperty(value = "micro_online_store")
                private String microOnlineStore;
                @JsonProperty(value = "micro_ec_name")
                private String microEcName;
                @JsonProperty(value = "micro_qrcode")
                private String microQrCode;
                @JsonProperty(value = "micro_link")
                private String microLink;

            }
        }


        @Data
        public static class BusinessLicenseInfoBean {
            @JsonProperty(value = "license_copy")
            private String licenseCopy;
            @JsonProperty(value = "license_number")
            private String licenseNumber;
            @JsonProperty(value = "merchant_name")
            private String merchantName;
            @JsonProperty(value = "legal_person")
            private String legalPerson;

        }

        @Data
        public static class CertificateInfoBean {

            @JsonProperty(value = "cert_copy")
            private String certCopy;
            @JsonProperty(value = "cert_type")
            private String certType;
            @JsonProperty(value = "cert_number")
            private String certNumber;
            @JsonProperty(value = "merchant_name")
            private String merchantName;
            @JsonProperty(value = "company_address")
            private String companyAddress;
            @JsonProperty(value = "legal_person")
            private String legalPerson;
            @JsonProperty(value = "period_begin")
            private String periodBegin;
            @JsonProperty(value = "period_end")
            private String periodEnd;

        }

        @Data
        public static class OrganizationInfoBean {

            @JsonProperty(value = "organization_copy")
            private String organizationCopy;
            @JsonProperty(value = "organization_code")
            private String organizationCode;
            @JsonProperty(value = "org_period_begin")
            private String orgPeriodBegin;
            @JsonProperty(value = "org_period_end")
            private String orgPeriodEnd;

        }

        @Data
        public static class IdentityInfoBean {
            @JsonProperty(value = "id_doc_type")
            private String idDocType;
            @JsonProperty(value = "id_card_info")
            private IdCardInfoBean idCardInfo;
            @JsonProperty(value = "id_doc_info")
            private IdDocInfoBean idDocInfo;
            private Boolean owner;

            @Data
            public static class IdCardInfoBean {
                @JsonProperty(value = "id_card_copy")
                private String idCardCopy;
                @JsonProperty(value = "id_card_national")
                private String idCardNational;
                @JsonSerialize(using = EncryptSerializer.class)
                @JsonProperty(value = "id_card_name")
                private String idCardName;
                @JsonSerialize(using = EncryptSerializer.class)
                @JsonProperty(value = "id_card_number")
                private String idCardNumber;
                @JsonProperty(value = "card_period_begin")
                private String cardPeriodBegin;
                @JsonProperty(value = "card_period_end")
                private String cardPeriodEnd;

            }

            @Data
            public static class IdDocInfoBean {
                @JsonProperty(value = "id_doc_copy")
                private String idDocCopy;
                @JsonProperty(value = "id_doc_name")
                private String idDocName;
                @JsonProperty(value = "id_doc_number")
                private String idDocNumber;
                @JsonProperty(value = "doc_period_begin")
                private String docPeriodBegin;
                @JsonProperty(value = "doc_period_end")
                private String docPeriodEnd;

            }
        }

        @Data
        public static class UboInfoBean {
            @JsonProperty(value = "id_type")
            private String idType;
            @JsonProperty(value = "id_card_copy")
            private String idCardCopy;
            @JsonProperty(value = "id_card_national")
            private String idCardNational;
            @JsonProperty(value = "id_doc_copy")
            private String idDocCopy;
            @JsonSerialize(using = EncryptSerializer.class)
            private String name;
            @JsonSerialize(using = EncryptSerializer.class)
            @JsonProperty(value = "id_number")
            private String idNumber;
            @JsonProperty(value = "id_period_begin")
            private String idPeriodBegin;
            @JsonProperty(value = "id_period_end")
            private String idPeriodEnd;
        }
    }

    @Data
    public static class BusinessInfoBean {
        @JsonProperty(value = "merchant_shortname")
        private String merchantShortName;
        @JsonProperty(value = "service_phone")
        private String servicePhone;
        @JsonProperty(value = "sales_info")
        private SalesInfoBean salesInfo;


        @Data
        public static class SalesInfoBean {
            @JsonProperty(value = "biz_store_info")
            private BizStoreInfoBean bizStoreInfo;
            @JsonProperty(value = "mp_info")
            private MpInfoBean mpInfo;
            @JsonProperty(value = "mini_program_info")
            private MiniProgramInfoBean miniProgramInfo;
            @JsonProperty(value = "app_info")
            private AppInfoBean appInfo;
            @JsonProperty(value = "web_info")
            private WebInfoBean webInfo;
            @JsonProperty(value = "wework_info")
            private WeworkInfoBean weworkInfo;
            @JsonProperty(value = "sales_scenes_type")
            private List<String> salesScenesType;

            @Data
            public static class BizStoreInfoBean {
                @JsonProperty(value = "biz_store_name")
                private String bizStoreName;
                @JsonProperty(value = "biz_address_code")
                private String bizAddressCode;
                @JsonProperty(value = "biz_store_address")
                private String bizStoreAddress;
                @JsonProperty(value = "biz_sub_appid")
                private String bizSubAppid;
                @JsonProperty(value = "store_entrance_pic")
                private List<String> storeEntrancePic;
                @JsonProperty(value = "indoor_pic")
                private List<String> indoorPic;

            }

            @Data
            public static class MpInfoBean {
                @JsonProperty(value = "mp_appid")
                private String mpAppid;
                @JsonProperty(value = "mp_sub_appid")
                private String mpSubAppid;
                @JsonProperty(value = "mp_pics")
                private List<String> mpPics;

            }

            @Data
            public static class MiniProgramInfoBean {
                @JsonProperty(value = "mini_program_appid")
                private String miniProgramAppid;
                @JsonProperty(value = "mini_program_sub_appid")
                private String miniProgramSubAppid;
                @JsonProperty(value = "mini_program_pics")
                private List<String> miniProgramPics;

            }

            @Data
            public static class AppInfoBean {
                @JsonProperty(value = "mp_pics")
                private String appAppid;
                @JsonProperty(value = "mp_pics")
                private String appSubAppid;
                @JsonProperty(value = "mp_pics")
                private List<String> appPics;

            }

            @Data
            public static class WebInfoBean {

                private String domain;
                @JsonProperty(value = "web_authorisation")
                private String webAuthorisation;
                @JsonProperty(value = "web_appid")
                private String webAppid;

            }

            @Data
            public static class WeworkInfoBean {
                @JsonProperty(value = "corp_id")
                private String corpId;
                @JsonProperty(value = "sub_corp_id")
                private String subCorpId;
                @JsonProperty(value = "wework_pics")
                private List<String> weworkPics;

            }
        }
    }

    @Data
    public static class SettlementInfoBean {
        @JsonProperty(value = "settlement_id")
        private String settlementId;
        @JsonProperty(value = "qualification_type")
        private String qualificationType;
        @JsonProperty(value = "activities_id")
        private String activitiesId;
        @JsonProperty(value = "activities_rate")
        private String activitiesRate;

        private List<String> qualifications;
        @JsonProperty(value = "activities_additions")
        private List<String> activitiesAdditions;


    }

    @Data
    public static class BankAccountInfoBean {

        @JsonProperty(value = "bank_account_type")
        private String bankAccountType;
        @JsonProperty(value = "account_name")
        @JsonSerialize(using = EncryptSerializer.class)
        private String accountName;
        @JsonProperty(value = "account_bank")
        private String accountBank;
        @JsonProperty(value = "bank_address_code")
        private String bankAddressCode;
        @JsonProperty(value = "bank_branch_id")
        private String bankBranchId;
        @JsonProperty(value = "bank_name")
        private String bankName;
        @JsonProperty(value = "account_number")
        private String accountNumber;


    }

    @Data
    public static class AdditionInfoBean {
        @JsonProperty(value = "legal_person_commitment")
        private String legalPersonCommitment;
        @JsonProperty(value = "legal_person_video")
        private String legalPersonVideo;
        @JsonProperty(value = "business_addition_msg")
        private String businessAdditionMsg;
        @JsonProperty(value = "business_addition_pics")
        private List<String> businessAdditionPics;

    }
}
