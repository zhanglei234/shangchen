package com.zscat.mallplus.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.entity.OmsOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 */
public interface OmsOrderMapper extends BaseMapper<OmsOrder> {


    /**
     * 订单日统计
     *
     * @param date
     * @return
     */
    Map orderDayStatic(String date);

    /**
     * 订单 月统计
     *
     * @param date
     * @return
     */
    Map orderMonthStatic(String date);

    List<OmsOrder> listByDate(@Param("date") String date, @Param("type") Integer type);
}
