package com.zscat.mallplus.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.oms.entity.OmsCartItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 */
public interface IOmsCartItemService extends IService<OmsCartItem> {

    /**
     * 查询购物车中是否包含该商品，有增加数量，无添加到购物车
     */
    @Transactional
    OmsCartItem add(OmsCartItem cartItem);

    /**
     * 根据会员编号获取购物车列表
     */
    List<OmsCartItem> list(Long memberId, List<Long> ids);



    /**
     * 修改购物车中商品的规格
     */
    @Transactional
    int updateAttr(OmsCartItem cartItem);

 }
