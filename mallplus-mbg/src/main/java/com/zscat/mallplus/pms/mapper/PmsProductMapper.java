package com.zscat.mallplus.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.vo.PmsProductResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 */
public interface PmsProductMapper extends BaseMapper<PmsProduct> {


    PmsProductResult getUpdateInfo(Long id);

    List<PmsProduct> listByDate(@Param("date") String date, @Param("type") Integer type);
}
