package com.zscat.mallplus.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.pms.entity.PmsProductCategory;
import com.zscat.mallplus.pms.vo.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 */
public interface IPmsProductCategoryService extends IService<PmsProductCategory> {

    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
