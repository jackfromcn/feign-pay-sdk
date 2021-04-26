package com.feign.pay.sdk.alipay.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author wencheng
 * @date 2021/4/22
 */
@Data
public class GoodsDetail {


    /**
     * 支付宝定义的统一商品编号
     */
    @JsonProperty("alipay_goods_id")
    private String alipayGoodsId;

    /**
     * 商品描述信息
     */
    @JsonProperty("body")
    private String body;

    /**
     * 商品类目树，从商品类目根节点到叶子节点的类目id组成，类目id值使用|分割
     */
    @JsonProperty("categories_tree")
    private String categoriesTree;

    /**
     * 商品类目
     */
    @JsonProperty("goods_category")
    private String goodsCategory;

    /**
     * 商品的编号
     */
    @JsonProperty("goods_id")
    private String goodsId;

    /**
     * 商品名称
     */
    @JsonProperty("goods_name")
    private String goodsName;

    /**
     * 商品单价，单位为元
     */
    @JsonProperty("price")
    private String price;

    /**
     * 商品数量
     */
    @JsonProperty("quantity")
    private Long quantity;

    /**
     * 商品的展示地址
     */
    @JsonProperty("show_url")
    private String showUrl;
    
}
