package com.zscat.mallplus.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.utils.ValidatorUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 */
@Service
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements IOmsOrderService {

    @Resource
    private OmsOrderMapper orderMapper;
    @Resource
    private UmsMemberMapper memberMapper;
    @Resource
    private PmsProductMapper productMapper;



    @Override
    public Map orderDayStatic(String date) {
        Map list = orderMapper.orderDayStatic(date);
        return list;
    }

    @Override
    public Map orderMonthStatic(String date) {
        Map list = orderMapper.orderMonthStatic(date);
        return list;
    }

    @Override
    public Object dayStatic(String date, Integer type) {
        List<OmsOrder> orders = orderMapper.listByDate(date, type);
        List<UmsMember> members = memberMapper.listByDate(date, type);
        List<PmsProduct> products = productMapper.listByDate(date, type);
        int nowOrderCount = 0; // 今日订单
        BigDecimal nowOrderPay = new BigDecimal(0); //今日销售总额
        for (OmsOrder order : orders) {
            if (order.getStatus() < 9) {
                nowOrderCount++;
                nowOrderPay = nowOrderPay.add(order.getPayAmount());
            }
        }
        int mallCount = 0; // 当日
        int femallount = 0; // 当日
        for (UmsMember member : members) {
            if (member.getGender() == null || member.getGender() == 1) {
                mallCount++;
            } else {
                femallount++;
            }
        }
        int onCount = 0;
        int offCount = 0;

        int noStock = 0;

        for (PmsProduct goods : products) {
            if (goods.getPublishStatus() == 1) { // 上架状态：0->下架；1->上架
                onCount++;
            }
            if (goods.getPublishStatus() == 0) { // 上架状态：0->下架；1->上架
                offCount++;
            }
            if (ValidatorUtils.empty(goods.getStock()) || goods.getStock() < 1) { // 上架状态：0->下架；1->上架
                noStock++;
            }
        }
        Map<String, Object> map = new HashMap();
        map.put("nowOrderCount", nowOrderCount);
        map.put("nowOrderPay", nowOrderPay);
        map.put("mallCount", mallCount);
        map.put("femallount", femallount);
        map.put("onCount", onCount);
        map.put("offCount", offCount);
        map.put("noStock", noStock);

        map.put("memberCount", memberMapper.selectCount(new QueryWrapper<>()));
        map.put("goodsCount", productMapper.selectCount(new QueryWrapper<>()));
        map.put("orderCount", orderMapper.selectCount(new QueryWrapper<>()));
        return map;

    }






}
