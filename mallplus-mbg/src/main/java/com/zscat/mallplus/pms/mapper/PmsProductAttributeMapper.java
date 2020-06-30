package com.zscat.mallplus.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.pms.entity.PmsProductAttribute;
import com.zscat.mallplus.pms.vo.PmsProductAttr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 Mapper 接口
 * </p>
 */
public interface PmsProductAttributeMapper extends BaseMapper<PmsProductAttribute> {


    List<PmsProductAttr> getProductAttrById(@Param("productId") Long productId, @Param("type") int type);
}
