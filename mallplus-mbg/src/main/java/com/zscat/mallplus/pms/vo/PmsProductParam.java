package com.zscat.mallplus.pms.vo;


import com.zscat.mallplus.pms.entity.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 创建和修改商品时使用的参数
 */
@Data
public class PmsProductParam extends PmsProduct {
    private List<PmsProductAttributeValue> productCanShuValueList;
    private List<PmsProduct> typeGoodsList;

    private PmsProduct goods;
}
