package com.zscat.mallplus.vo;

import lombok.Data;

@Data
public class CartParam {
    private Long cartId;
    private Long skuId;
    private Long goodsId;
    private Integer total;
    private Long memberId;
    private String ids;
    // 1 选中 2 未选中
    private Integer isSelected;
    // 1sku 2spu
    private Integer type;
    private Integer orderType;
}
