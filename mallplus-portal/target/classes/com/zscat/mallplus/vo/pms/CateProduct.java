package com.zscat.mallplus.vo.pms;

import lombok.Data;

import java.util.List;

@Data
public class CateProduct {
    private Long categoryId;//分类Id
    private String categoryName;//分类名字
    private String categoryImage;//分类图片路径
}
