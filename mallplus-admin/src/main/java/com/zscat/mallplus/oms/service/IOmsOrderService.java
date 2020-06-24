package com.zscat.mallplus.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.oms.entity.OmsOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
     * 订单日统计
     *
     * @param date
     * @return
     */
    Map orderDayStatic(String date);

    /**
     * 订单月统计
     *
     * @param date
     * @return
     */
    Map orderMonthStatic(String date);

    Object dayStatic(String date, Integer type);
}
