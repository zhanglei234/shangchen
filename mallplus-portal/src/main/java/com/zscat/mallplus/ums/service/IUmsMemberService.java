package com.zscat.mallplus.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.utils.CommonResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * 会员表 服务类
 * </p>
 */
public interface IUmsMemberService extends IService<UmsMember> {


    /**
     * 根据用户名获取会员
     */
    UmsMember getByUsername(String username);


    /**
     * 用户注册
     */
    @Transactional
    CommonResult register(String phone, String password, String confim, String authCode, String invitecode);

    /**
     * 生成验证码
     */
    CommonResult generateAuthCode(String telephone);


    /**
     * 修改密码
     */
    @Transactional
    CommonResult updatePassword(String telephone, String password, String authCode);


    Map<String, Object> login(String username, String password);

    String refreshToken(String token);

    Object register(UmsMember umsMember);


    Object getCurrentMember();

    UmsMember getNewCurrentMember();


}

