package com.zscat.mallplus.vo.timeline;

import lombok.Data;

import java.util.List;

/**
 * TimeLineMonh
 *
 */
@Data
public class TimelineMonth {

    private Integer month;

    private Integer count;

    private List<TimelinePost> posts;

}
