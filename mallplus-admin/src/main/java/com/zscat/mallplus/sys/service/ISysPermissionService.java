package com.zscat.mallplus.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.bo.Tree;
import com.zscat.mallplus.sys.entity.SysPermission;

import java.util.List;

/**
 * <p>
 * 后台用户权限表 服务类
 * </p>
 */
public interface ISysPermissionService extends IService<SysPermission> {

    List<Tree<SysPermission>> getPermissionsByUserId(Long id);


    List<Tree<SysPermission>> getAllPermission();
}
