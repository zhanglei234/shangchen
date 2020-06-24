package com.zscat.mallplus.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.pms.entity.PmsComment;

import java.util.List;

/**
 * <p>
 * 商品评价表 Mapper 接口
 * </p>
 *
 */
public interface PmsCommentMapper extends BaseMapper<PmsComment> {
    List<PmsComment> getByProductId(Long productId);
}
