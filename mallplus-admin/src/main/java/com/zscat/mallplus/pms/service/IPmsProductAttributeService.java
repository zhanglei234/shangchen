package com.zscat.mallplus.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.pms.entity.PmsProductAttribute;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 */
public interface IPmsProductAttributeService extends IService<PmsProductAttribute> {


    boolean saveAndUpdate(PmsProductAttribute entity);
}
