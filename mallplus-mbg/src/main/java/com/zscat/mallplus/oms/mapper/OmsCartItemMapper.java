package com.zscat.mallplus.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.oms.entity.OmsCartItem;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 */
public interface OmsCartItemMapper extends BaseMapper<OmsCartItem> {

    Integer countCart(Long id);
}
