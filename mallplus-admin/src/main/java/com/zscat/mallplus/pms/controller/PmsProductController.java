package com.zscat.mallplus.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.pms.vo.PmsProductParam;
import com.zscat.mallplus.pms.vo.PmsProductResult;
import com.zscat.mallplus.sys.util.BeanUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.IdStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品信息
 * </p>
 *
 */
@Slf4j
@RestController
@Api(tags = "PmsProductController", description = "商品信息管理")
@RequestMapping("/pms/PmsProduct")
public class PmsProductController {
    @Resource
    private IPmsProductService IPmsProductService;

    @SysLog(MODULE = "pms", REMARK = "根据条件查询所有商品信息列表")
    @ApiOperation("根据条件查询所有商品信息列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public Object getPmsProductByPage(PmsProduct entity,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            IPage<PmsProduct> page = null;
            if (ValidatorUtils.notEmpty(entity.getKeyword())) {
                page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>().eq("name", entity.getKeyword()).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
            } else {
                page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));

            }
            return new CommonResult().success(page);
        } catch (Exception e) {
            log.error("根据条件查询所有商品信息列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("根据商品名称或货号模糊查询")
    @RequestMapping(value = "/simpleList", method = RequestMethod.GET)
    @ResponseBody
    public Object getList(String keyword) {
        List<PmsProduct> productList = IPmsProductService.list(keyword);
        return new CommonResult().success(productList);
    }



    @SysLog(MODULE = "pms", REMARK = "删除商品信息")
    @ApiOperation("删除商品信息")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProduct:delete')")
    public Object deletePmsProduct(@ApiParam("商品信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品信息id");
            }
            if (IPmsProductService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除商品信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "给商品信息分配商品信息")
    @ApiOperation("查询商品信息明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public Object getPmsProductById(@ApiParam("商品信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品信息id");
            }
            PmsProduct coupon = IPmsProductService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询商品信息明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除商品信息")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除商品信息")
    @PreAuthorize("hasAuthority('pms:PmsProduct:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = IPmsProductService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("根据商品id获取商品编辑信息")
    @RequestMapping(value = "/updateInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "根据商品id获取商品编辑信息")
    @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public Object getUpdateInfo(@PathVariable Long id) {
        PmsProductResult productResult = IPmsProductService.getUpdateInfo(id);
        return new CommonResult().success(productResult);
    }




    @ApiOperation("批量上下架")
    @RequestMapping(value = "/update/publishStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量上下架")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public Object updatePublishStatus(@RequestParam("ids") List<Long> ids,
                                      @RequestParam("publishStatus") Integer publishStatus) {
        int count = IPmsProductService.updatePublishStatus(ids, publishStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量上下架")
    @RequestMapping(value = "/publishStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量上下架")
    public Object updatePublishStatu(@RequestBody IdStatus ids, BindingResult result) {
        PmsProduct product = new PmsProduct();
        product.setId(ids.getId());
        product.setPublishStatus(ids.getStatus());
        Boolean count = IPmsProductService.updateById(product);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量推荐商品")
    @RequestMapping(value = "/update/recommendStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量推荐商品")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public Object updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                        @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = IPmsProductService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量设为新品")
    @RequestMapping(value = "/update/newStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量设为新品")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public Object updateNewStatus(@RequestParam("ids") List<Long> ids,
                                  @RequestParam("newStatus") Integer newStatus) {
        int count = IPmsProductService.updateNewStatus(ids, newStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量设为分销")
    @RequestMapping(value = "/update/isFenxiao", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量设为分销")
    public Object updateisFenxiao(@RequestParam("ids") List<Long> ids,
                                  @RequestParam("newStatus") Integer newStatus) {
        int count = IPmsProductService.updateisFenxiao(ids, newStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("创建商品")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "创建商品")
    public Object create(@RequestBody PmsProduct entity) {
        try {
            entity.setDeleteStatus(0);
            entity.setVerifyStatus(1);
            if (IPmsProductService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存订单中所包含的商品：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @ApiOperation("批量修改删除状态")
    @RequestMapping(value = "/update/deleteStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量修改删除状态")
    @PreAuthorize("hasAuthority('pms:PmsProduct:delete')")
    public Object updateDeleteStatus(@RequestParam("ids") List<Long> ids,
                                     @RequestParam("deleteStatus") Integer deleteStatus) {
        int count = IPmsProductService.updateDeleteStatus(ids, deleteStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @GetMapping(value = "/goods/list")
    public Object getPmsProductListByPage(PmsProduct entity,
                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            if (entity.getType() == 1) {
                return new CommonResult().success(IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>().eq("publish_status", 1).gt("stock", 0).select(ConstansValue.sampleGoodsList).orderByDesc("create_time")));
            }
            if (entity.getType() == 2) {
                return new CommonResult().success(IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>().eq("publish_status", 0).gt("stock", 0).select(ConstansValue.sampleGoodsList).orderByDesc("create_time")));
            }
            if (entity.getType() == 3) {
                return new CommonResult().success(IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>().lt("stock", 1).select(ConstansValue.sampleGoodsList).orderByDesc("create_time")));
            }

            return new CommonResult().success(IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>().select(ConstansValue.sampleGoodsList)));
        } catch (Exception e) {
            log.error("根据条件查询所有商品信息列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }


    @ApiOperation("修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "修改")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public Object updateProduct(@RequestBody PmsProduct pmsProduct) {
        try {
            PmsProductParam pmsProductParam = new PmsProductParam();
            BeanUtils.copyProperties(pmsProduct, pmsProductParam);
            if (IPmsProductService.update(pmsProduct.getId(), pmsProductParam) == 1) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
}
