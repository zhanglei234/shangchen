package com.zscat.mallplus.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.oms.entity.OmsCartItem;
import com.zscat.mallplus.oms.mapper.OmsCartItemMapper;
import com.zscat.mallplus.oms.service.IOmsCartItemService;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.entity.PmsSkuStock;
import com.zscat.mallplus.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.pms.service.IPmsSkuStockService;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.utils.ValidatorUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 */
@Service
public class OmsCartItemServiceImpl extends ServiceImpl<OmsCartItemMapper, OmsCartItem> implements IOmsCartItemService {
    @Resource
    private OmsCartItemMapper cartItemMapper;
    @Resource
    private IUmsMemberService memberService;
    @Resource
    private PmsProductMapper pmsProductMapper;
    @Autowired
    private IPmsSkuStockService pmsSkuStockService;

    @Override
    public OmsCartItem add(OmsCartItem cartItem) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        cartItem.setMemberId(currentMember.getId());
        cartItem.setMemberNickname(currentMember.getNickname());
        cartItem.setDeleteStatus(0);
        PmsProduct pmsProduct = pmsProductMapper.selectById(cartItem.getProductId());
        if (org.apache.commons.lang.StringUtils.isBlank(cartItem.getProductPic())) {
            cartItem.setProductPic(pmsProduct.getPic());
        }
        cartItem.setProductBrand(pmsProduct.getBrandName());
        cartItem.setProductName(pmsProduct.getName());
        cartItem.setProductSn(pmsProduct.getProductSn());
        cartItem.setProductSubTitle(pmsProduct.getSubTitle());
        cartItem.setProductCategoryId(pmsProduct.getProductCategoryId());
        OmsCartItem existCartItem = getCartItem(cartItem);
        if (existCartItem == null) {
            cartItem.setCreateDate(new Date());
            cartItemMapper.insert(cartItem);
        } else {
            cartItem.setModifyDate(new Date());
            existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
            cartItemMapper.updateById(existCartItem);
            return existCartItem;
        }
        return cartItem;
    }

    /**
     * 根据会员id,商品id和规格获取购物车中商品
     */
    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        OmsCartItem example = new OmsCartItem();
        example.setProductId(cartItem.getProductId());
        example.setDeleteStatus(0);

        if (!StringUtils.isEmpty(cartItem.getSp1())) {
            example.setSp1(cartItem.getSp1());
        }
        if (!StringUtils.isEmpty(cartItem.getSp2())) {
            example.setSp2(cartItem.getSp2());
        }
        if (!StringUtils.isEmpty(cartItem.getSp3())) {
            example.setSp3(cartItem.getSp3());
        }
        List<OmsCartItem> cartItemList = cartItemMapper.selectList(new QueryWrapper<>(example));
        if (!CollectionUtils.isEmpty(cartItemList)) {
            return cartItemList.get(0);
        }
        return null;
    }

    @Override
    public List<OmsCartItem> list(Long memberId, List<Long> ids) {

        OmsCartItem example = new OmsCartItem();
        example.setMemberId(memberId);
        example.setDeleteStatus(0);
        // example.
        if (ids != null && ids.size() > 0) {
            return cartItemMapper.selectList(new QueryWrapper<>(example).in("id", ids));
        }
        return cartItemMapper.selectList(new QueryWrapper<>(example));
    }



    @Override
    public int updateAttr(OmsCartItem cartItem) {
        //删除原购物车信息
        OmsCartItem updateCart = new OmsCartItem();
        updateCart.setId(cartItem.getId());
        updateCart.setModifyDate(new Date());
        updateCart.setDeleteStatus(1);
        cartItemMapper.updateById(updateCart);
        cartItem.setId(null);
        add(cartItem);
        return 1;
    }










}
