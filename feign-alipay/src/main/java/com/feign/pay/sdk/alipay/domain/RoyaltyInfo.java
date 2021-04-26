package com.feign.pay.sdk.alipay.domain;

import com.alipay.api.domain.RoyaltyDetailInfos;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @author wencheng
 * @date 2021/4/22
 */
@Data
public class RoyaltyInfo {

    /**
     * 分账明细的信息，可以描述多条分账指令，json数组。
     */
    @JsonProperty("royalty_detail_infos")
    private List<RoyaltyDetailInfos> royaltyDetailInfos;

    /**
     * 分账类型
     卖家的分账类型，目前只支持传入ROYALTY（普通分账类型）。
     */
    @JsonProperty("royalty_type")
    private String royaltyType;

}
