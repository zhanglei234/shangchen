package com.zscat.mallplus.vo;

import lombok.Data;

@Data
public class LogParam {
    private String startTime;
    private String endTime;
    private String keyword;
    private Integer current = 1;
    private Integer size = 10;
}
