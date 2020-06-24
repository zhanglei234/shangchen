package com.zscat.mallplus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
 import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.pms.entity.*;
import com.zscat.mallplus.pms.mapper.PmsCommentMapper;
import com.zscat.mallplus.pms.mapper.PmsProductAttributeMapper;
import com.zscat.mallplus.pms.service.*;
import com.zscat.mallplus.pms.vo.PmsProductAttr;
import com.zscat.mallplus.pms.vo.PmsProductCategoryWithChildrenItem;
import com.zscat.mallplus.pms.vo.PmsProductResult;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页内容管理Controller
 */
@RestController
@Api(tags = "GoodsController", description = "商品相关管理")
@RequestMapping("/api/pms")
public class PmsGoodsController {

    @Autowired
    private IPmsProductAttributeCategoryService productAttributeCategoryService;
     @Autowired
    private IPmsProductService pmsProductService;
    @Autowired
    private IPmsProductCategoryService pmsProductCategoryService;
    @Autowired
    private IPmsProductAttributeService productAttributeService;

    @Autowired
    private IPmsProductCategoryService productCategoryService;
     @Autowired
    private IUmsMemberService memberService;
    @Autowired
    private IPmsProductConsultService pmsProductConsultService;
    @Autowired
    private RedisService redisService;

    @Resource
    private PmsProductAttributeMapper pmsProductAttributeMapper;
    @Resource
    private PmsCommentMapper pmsCommentMapper;


    @IgnoreAuth
    @GetMapping(value = "/product/queryProductList")
    @ApiOperation(value = "查询商品列表")
    public Object queryProductList() {
        PmsProduct productQueryParam = new PmsProduct();
        productQueryParam.setPublishStatus(1);
        productQueryParam.setRecommandStatus(1);
        productQueryParam.setVerifyStatus(1);
        return new CommonResult().success(pmsProductService.list(new QueryWrapper<>(productQueryParam)));
    }

    @IgnoreAuth
    @GetMapping(value = "/product/queryProductCateList")
    @ApiOperation(value = "查询商品分类列表")
    public Object queryProductCateList() {
        PmsProductCategory pmsProductCategory = new PmsProductCategory();
        pmsProductCategory.setShowStatus(1);
        return new CommonResult().success(pmsProductCategoryService.list(new QueryWrapper<>(pmsProductCategory)));
    }

    @IgnoreAuth
    @GetMapping(value = "/product/queryProductList1")
    public Object queryProductList1(PmsProduct productQueryParam) {
        productQueryParam.setPublishStatus(1);
        productQueryParam.setVerifyStatus(1);
        return new CommonResult().success(pmsProductService.list(new QueryWrapper<>(productQueryParam)));
    }

    /**
     * 或者分类和分类下的商品
     *
     * @return
     */
    @IgnoreAuth
    @ApiOperation(value = "分类和分类下的商品")
    @GetMapping("/getProductCategoryDto")
    public Object getProductCategoryDtoByPid() {

        List<PmsProductAttributeCategory> productAttributeCategoryList = productAttributeCategoryService.list(null);
        for (PmsProductAttributeCategory gt : productAttributeCategoryList) {
            PmsProduct productQueryParam = new PmsProduct();
            productQueryParam.setProductAttributeCategoryId(gt.getId());

            productQueryParam.setPublishStatus(1);
            productQueryParam.setVerifyStatus(1);
            gt.setGoodsList(pmsProductService.list(new QueryWrapper<>(productQueryParam).select(ConstansValue.sampleGoodsList)));
        }
        return new CommonResult().success(productAttributeCategoryList);
    }

    /**
     * 查询所有一级分类及子分类
     *
     * @return
     */
    @IgnoreAuth
    @ApiOperation(value = "查询所有一级分类及子分类")
    @GetMapping("/listWithChildren")
    public Object listWithChildren() {
        List<PmsProductCategoryWithChildrenItem> list = productCategoryService.listWithChildren();
        return new CommonResult().success(list);
    }


    @IgnoreAuth
    @GetMapping(value = "/product/queryProductDetail")
    @ApiOperation(value = "查询商品详情信息")
    public Object queryProductDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        PmsProductResult productResult = new PmsProductResult();

        //获取商品基础属性
        PmsProduct product = pmsProductService.getById(id);
        //获取商品其他属性值
        List<PmsProductAttr> attrList = pmsProductAttributeMapper.getProductAttrById(id, 0);//获取规格数据
        //获取商品评价
        List<PmsComment> pmsCommentList = pmsCommentMapper.getByProductId(id);
        productResult.setProduct(product);
        productResult.setPmsProductAttrList(attrList);
        productResult.setPmsComments(pmsCommentList);

//        UmsMember umsMember = memberService.getNewCurrentMember();与用户关联再议
        /*if (umsMember != null && umsMember.getId() != null && productResult != null) {
            MemberProductCollection findCollection = productCollectionRepository.findByMemberIdAndProductId(
                    umsMember.getId(), id);
            if (findCollection != null) {
                productResult.setIs_favorite(1);
            } else {
                productResult.setIs_favorite(2);
            }
        }*/
        return new CommonResult().success(productResult);
    }

    @IgnoreAuth
    @ApiOperation(value = "查询所有一级分类及子分类")
    @GetMapping(value = "/attr/list")
    public Object getList(@RequestParam(value = "cid", required = false, defaultValue = "0") Long cid,
                          @RequestParam(value = "type") Integer type,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        PmsProductAttribute q = new PmsProductAttribute();
        q.setType(type);
        q.setProductAttributeCategoryId(cid);
        IPage<PmsProductAttribute> productAttributeList = productAttributeService.page(new Page<>(pageSize, pageNum), new QueryWrapper<>(q));
        return new CommonResult().success(productAttributeList);
    }



    /**
     * 分类下的商品
     *
     * @return
     */
    @IgnoreAuth
    @ApiOperation(value = "分类下的商品")
    @GetMapping("/goods")
    public Object goods(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
             PmsProduct productQueryParam = new PmsProduct();
            productQueryParam.setProductAttributeCategoryId(id);

            productQueryParam.setPublishStatus(1);
            productQueryParam.setVerifyStatus(1);
           List<PmsProduct> list= pmsProductService.list(new QueryWrapper<>(productQueryParam).select(ConstansValue.sampleGoodsList));
         return new CommonResult().success(list);
    }



}
