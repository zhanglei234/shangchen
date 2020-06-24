package com.zscat.mallplus.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 */
@Slf4j
@RestController
@Api(tags = "oms", description = "订单表管理")
@RequestMapping("/oms/OmsOrder")
public class OmsOrderController {
    @Resource
    private IOmsOrderService IOmsOrderService;


    @SysLog(MODULE = "oms", REMARK = "根据条件查询所有订单表列表")
    @ApiOperation("根据条件查询所有订单表列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('oms:OmsOrder:read')")
    public Object getOmsOrderByPage(OmsOrder entity,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IOmsOrderService.page(new Page<OmsOrder>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc("create_time").select(ConstansValue.sampleOrderList)));
        } catch (Exception e) {
            log.error("根据条件查询所有订单表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }


    @SysLog(MODULE = "oms", REMARK = "删除订单表")
    @ApiOperation("删除订单表")
    @GetMapping(value = "/delete/{id}")
//    @PreAuthorize("hasAuthority('oms:OmsOrder:delete')")
    public Object deleteOmsOrder(@ApiParam("订单表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("订单表id");
            }
            if (IOmsOrderService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除订单表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }


    @ApiOperation(value = "批量删除订单表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除订单表")
    @PreAuthorize("hasAuthority('oms:OmsOrder:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = IOmsOrderService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }



    @ApiOperation(value = "查询订单列表")
    @GetMapping(value = "/order/list")
    public Object orderList(OmsOrder order,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "30") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        IPage<OmsOrder> page = null;
        if (order.getStatus() != null && order.getStatus() == 0) {
            page = IOmsOrderService.page(new Page<OmsOrder>(pageNum, pageSize), new QueryWrapper<OmsOrder>().orderByDesc("create_time").select(ConstansValue.sampleOrderList));
        } else {
            page = IOmsOrderService.page(new Page<OmsOrder>(pageNum, pageSize), new QueryWrapper<>(order).orderByDesc("create_time").select(ConstansValue.sampleOrderList));

        }
        for (OmsOrder omsOrder : page.getRecords()) {
//            List<OmsOrderItem> itemList = orderItemService.list(new QueryWrapper<OmsOrderItem>().eq("order_id", omsOrder.getId()).eq("type", AllEnum.OrderItemType.GOODS.code()));
//            omsOrder.setOrderItemList(itemList);
        }
        return new CommonResult().success(page);
    }


    public static void main(String[] args){
        try {

            URL url = new URL("http://www.baidu.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inString;
            File outfile = new File("test.html");
            PrintWriter out = new PrintWriter(new FileWriter(outfile));
            while ((inString = in.readLine()) != null) {
                out.println(inString);
            }
            in.close();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
     }
}
