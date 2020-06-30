package com.zscat.mallplus.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.vo.ApplyRefundVo;
import com.zscat.mallplus.vo.CartParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface IOmsOrderService extends IService<OmsOrder> {


    /**
     * 根据提交信息生成订单
     */
    @Transactional
    CommonResult createOrder(OmsOrder orderParam);

}
