package com.zscat.mallplus.vo.timeline;

import lombok.Data;

import java.util.Date;

/**
 * TimeLineData
 */
@Data
public class TimelinePost {

    private Integer id;

    private String title;

    private String description;

    private String postType;

    private Date createTime;

}
