package com.zscat.mallplus.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.sys.entity.SysPermission;
import com.zscat.mallplus.sys.entity.SysRole;
import com.zscat.mallplus.sys.entity.SysUser;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 */
public interface ISysUserService extends IService<SysUser> {

    String refreshToken(String token);

    String login(String username, String password);

    int updateUserRole(Long adminId, List<Long> roleIds);

    List<SysRole> getRoleListByUserId(Long adminId);

    List<SysPermission> getPermissionListByUserId(Long adminId);

    List<SysPermission> listMenuByUserId(Long adminId);

    boolean saves(SysUser entity);

    boolean updates(Long id, SysUser admin);

    List<SysPermission> listUserPerms(Long id);

    void removePermissRedis(Long id);

    List<SysPermission> listPerms();


    int updateShowStatus(List<Long> ids, Integer showStatus);


    void updatePassword(String password, String newPassword);
}
