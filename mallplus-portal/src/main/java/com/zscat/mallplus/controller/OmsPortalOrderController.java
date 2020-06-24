package com.zscat.mallplus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.entity.OmsOrderItem;
import com.zscat.mallplus.oms.service.IOmsOrderService;

import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单管理Controller
 *
 */
@Slf4j
@RestController
@Api(tags = "OmsPortalOrderController", description = "订单管理")
@RequestMapping("/api/order")
public class OmsPortalOrderController {

    @Autowired
    private IOmsOrderService orderService;


    @ApiOperation("生成订单")
    @RequestMapping(value = "/createOrder")
    @ResponseBody
    public Object createOrder(OmsOrder orderParam) {
        return orderService.createOrder(orderParam);
    }
}
