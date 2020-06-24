package com.zscat.mallplus.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class BaseEntity implements Serializable {
    @TableField("store_id")
    private Integer storeId;
}
