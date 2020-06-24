package com.zscat.mallplus.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 */
@Data
@TableName("sys_web_log")
public class SysWebLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long timeMin;
    /**
     * 用户编号
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 模块名
     */
    @TableField("service_name")
    private String serviceName;

    /**
     * 记录类名+方法名
     */
    private String method;

    /**
     * 描述
     */
    @TableField("operation_desc")
    private String operationDesc;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * IP
     */
    private String ip;

    /**
     * 参数
     */
    private String params;


}
