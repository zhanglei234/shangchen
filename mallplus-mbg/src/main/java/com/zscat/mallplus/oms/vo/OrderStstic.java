package com.zscat.mallplus.oms.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderStstic {
    private Long memberId;
    private int totalCount;
    private BigDecimal totalPayAmount;
    private int memberCount;
    private Long objId;
}
