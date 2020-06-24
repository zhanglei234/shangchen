package com.zscat.mallplus.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderStatusCount implements Serializable {
    int statusAll = 0;
    BigDecimal payAmount = BigDecimal.ZERO;
    // 12->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
    private int status0;
    private int status1;
    private int status2;
    private int status3;
    private int status4;
    private int status5;
    private int status14;
}
