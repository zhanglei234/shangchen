package com.zscat.mallplus.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.ApiContext;
import com.zscat.mallplus.pms.entity.*;
import com.zscat.mallplus.pms.mapper.*;
import com.zscat.mallplus.pms.service.*;
import com.zscat.mallplus.pms.vo.PmsProductParam;
import com.zscat.mallplus.pms.vo.PmsProductResult;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.vo.Rediskey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 */
@Slf4j
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements IPmsProductService {
    @Resource
    private ApiContext apiContext;
    @Resource
    private PmsProductMapper productMapper;
    @Resource
    private IPmsSkuStockService skuStockDao;
    @Resource
    private PmsSkuStockMapper skuStockMapper;
    @Resource
    private IPmsProductAttributeValueService productAttributeValueDao;
    @Resource
    private PmsProductAttributeValueMapper productAttributeValueMapper;
    @Resource
    private RedisService redisService;

    @Override
    public int create(PmsProductParam productParam) {
        int count;

        return 0;
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, PmsProduct product) {
        if (CollectionUtils.isEmpty(skuStockList)) return;
        int stock = 0;
        for (int i = 0; i < skuStockList.size(); i++) {
            PmsSkuStock skuStock = skuStockList.get(i);
            skuStock.setProductName(product.getName());
            if (StringUtils.isEmpty(skuStock.getSkuCode())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                //日期
                sb.append(sdf.format(new Date()));
                //四位商品id
                sb.append(String.format("%04d", product.getProductCategoryId()));
                //三位索引id
                sb.append(String.format("%03d", i + 1));
                skuStock.setSkuCode(sb.toString());
            }
            if (skuStock.getStock() != null && skuStock.getStock() > 0) {
                stock = stock + skuStock.getStock();

            }

        }
        product.setStock(stock);
    }

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        return productMapper.getUpdateInfo(id);
    }

    @Override
    public int update(Long id, PmsProductParam productParam) {
        int count = productMapper.update(productParam, new QueryWrapper<PmsProduct>().eq("id", id));

        return count;
    }


    @Override
    public int updateisFenxiao(List<Long> ids, Integer newStatus) {
        PmsProduct record = new PmsProduct();
        record.setIsFenxiao(newStatus);
        clerGoodsRedis(ids);
        return productMapper.update(record, new QueryWrapper<PmsProduct>().in("id", ids));
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        PmsProduct record = new PmsProduct();
        record.setPublishStatus(publishStatus);
        clerGoodsRedis(ids);
        return productMapper.update(record, new QueryWrapper<PmsProduct>().in("id", ids));
    }

    public void clerGoodsRedis(List<Long> ids) {
        for (Long id : ids) {
            redisService.remove(String.format(Rediskey.GOODSDETAIL, id));
        }
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        PmsProduct record = new PmsProduct();
        record.setRecommandStatus(recommendStatus);
        clerGoodsRedis(ids);
        return productMapper.update(record, new QueryWrapper<PmsProduct>().in("id", ids));
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        PmsProduct record = new PmsProduct();
        record.setNewStatus(newStatus);
        clerGoodsRedis(ids);
        return productMapper.update(record, new QueryWrapper<PmsProduct>().in("id", ids));
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        PmsProduct record = new PmsProduct();
        record.setDeleteStatus(deleteStatus);
        clerGoodsRedis(ids);
        return productMapper.update(record, new QueryWrapper<PmsProduct>().in("id", ids));
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("delete_status", 0);

        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("name", keyword);

        }
        return productMapper.selectList(queryWrapper);
    }


    /**
     * 建立和插入关系表操作
     *
     * @param dao       可以操作的dao
     * @param dataList  要插入的数据
     * @param productId 建立关系的id
     */
    private void relateAndInsertList(Object dao, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) return;
            for (Object item : dataList) {
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            Method insertList = dao.getClass().getMethod("saveBatch", Collection.class);
            insertList.invoke(dao, dataList);
        } catch (Exception e) {

            log.warn("创建产品出错:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}


