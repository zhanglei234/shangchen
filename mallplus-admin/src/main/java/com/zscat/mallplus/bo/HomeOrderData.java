package com.zscat.mallplus.bo;

import com.zscat.mallplus.vo.OrderStatusCount;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class HomeOrderData implements Serializable {
    int nowOrderCount = 0; // 今日订单
    BigDecimal nowOrderPay = new BigDecimal(0); //今日销售总额
    int yesOrderCount = 0; // 昨日订单
    BigDecimal yesOrderPay = new BigDecimal(0); //日销售总额
    int qiOrderCount = 0; // 7日订单
    BigDecimal qiOrderPay = new BigDecimal(0); //7日销售总额
    int monthOrderCount = 0; // 本月订单
    BigDecimal monthOrderPay = new BigDecimal(0); //本月销售总额
    int weekOrderCount = 0; // 本月订单
    BigDecimal weekOrderPay = new BigDecimal(0); //本月销售总额
    private OrderStatusCount orderStatusCount;
}
