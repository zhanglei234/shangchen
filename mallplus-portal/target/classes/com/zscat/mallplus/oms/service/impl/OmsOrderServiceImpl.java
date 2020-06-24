package com.zscat.mallplus.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.oms.entity.*;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.oms.service.IOmsOrderService;

import com.zscat.mallplus.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 订单表 服务实现类
 */
@Service
@Slf4j
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements IOmsOrderService {

    @Resource
    private IOmsOrderService orderService;


    @Override
    public CommonResult createOrder(OmsOrder orderParam) {
        orderParam.setCreateTime(new Date());
        orderParam.setDeleteStatus(0);
        orderParam.setOrderType(0);
        orderParam.setStatus(1);
        orderService.save(orderParam);
        return null;
    }
}
