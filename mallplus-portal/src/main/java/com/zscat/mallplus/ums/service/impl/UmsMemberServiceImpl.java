package com.zscat.mallplus.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.component.UserUtils;
import com.zscat.mallplus.ums.entity.*;
import com.zscat.mallplus.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.ums.service.*;
import com.zscat.mallplus.util.*;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.MatrixToImageWriter;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 */
@Slf4j
@Service
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements IUmsMemberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsMemberServiceImpl.class);
    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private UmsMemberMapper memberMapper;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private RedisService redisService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${authCode.expire.seconds}")
    private Long AUTH_CODE_EXPIRE_SECONDS;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${aliyun.sms.expire-minute:1}")
    private Integer expireMinute;
    @Value("${aliyun.sms.day-count:30}")
    private Integer dayCount;


    @Override
    public UmsMember getNewCurrentMember() {
        return (UmsMember) this.getCurrentMember();
    }


    @Override
    public Object getCurrentMember() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String requestType = ((HttpServletRequest) request).getMethod();
            if ("OPTIONS".equals(requestType)) {
                return null;
            }
            UmsMember member = UserUtils.getCurrentMember();
            if (member != null && member.getId() != null) {
                return member;
            }

            String tokenPre = "authorization";
            String authHeader = request.getParameter(tokenPre);
            if (ValidatorUtils.empty(authHeader)) {
                authHeader = request.getHeader(tokenPre);
            }
            if (authHeader != null && authHeader.startsWith("Bearer")) {
                String authToken = authHeader.substring("Bearer".length());
                String username = jwtTokenUtil.getUserNameFromToken(authToken);
                if (ValidatorUtils.notEmpty(username)) {
                    member = JsonUtils.jsonToPojo(redisService.get(String.format(Rediskey.MEMBER, username)), UmsMember.class);
                    if (member == null || member.getId() == null) {
                        member = getByUsername(username);
                    }
                    return member;
                }
            } else {
                return new CommonResult().fail(100);
            }
            return new CommonResult().fail(100);
        } catch (Exception e) {
            e.printStackTrace();
            return new UmsMember();
        }
    }


    @Override
    public UmsMember getByUsername(String username) {
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);

        return memberMapper.selectOne(new QueryWrapper<>(umsMember));
    }


    @Override
    public CommonResult register(String phone, String password, String confim, String authCode, String invitecode) {

        //没有该用户进行添加操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(phone);
        umsMember.setPhone(phone);
        umsMember.setSourceType(3);
        umsMember.setPassword(password);
        umsMember.setConfimpassword(confim);
        umsMember.setPhonecode(authCode);
        umsMember.setInvitecode(invitecode);
        if (ValidatorUtils.notEmpty(umsMember.getPhonecode()) && !verifyAuthCode(umsMember.getPhonecode(), umsMember.getPhone())) {
            return new CommonResult().failed("验证码错误");
        }
        return this.register(umsMember);
    }


    /**
     * 获取当天发送验证码次数
     * 限制号码当天次数
     *
     * @param phone
     * @return
     */
    private void checkTodaySendCount(String phone) {
        String value = redisService.get(countKey(phone));
        if (value != null) {
            Integer count = Integer.valueOf(value);
            if (count > dayCount) {
                throw new IllegalArgumentException("已超过当天最大次数");
            }
        }

    }

    private String countKey(String phone) {
        return "sms:count:" + LocalDate.now().toString() + ":" + phone;
    }

    @Override
    public CommonResult register(UmsMember user) {
        //验证验证码
        if (ValidatorUtils.notEmpty(user.getPhonecode()) && !verifyAuthCode(user.getPhonecode(), user.getPhone())) {
            return new CommonResult().failed("验证码错误");
        }
        if (!user.getPassword().equals(user.getConfimpassword())) {
            return new CommonResult().failed("密码不一致");
        }
        //查询是否已有该用户

        UmsMember queryM = new UmsMember();
        queryM.setUsername(user.getUsername());
        UmsMember umsMembers = memberMapper.selectOne(new QueryWrapper<>(queryM));
        if (umsMembers != null) {
            return new CommonResult().failed("该用户已经存在");
        }
        //没有该用户进行添加操作

        UmsMember umsMember = new UmsMember();
        umsMember.setMemberLevelId(4L);
        umsMember.setMemberLevelName("普通会员");
        umsMember.setUsername(user.getUsername());
        umsMember.setNickname(user.getUsername());
        umsMember.setSourceType(user.getSourceType());
        umsMember.setPhone(user.getPhone());
        umsMember.setPassword(passwordEncoder.encode(user.getPassword()));
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        umsMember.setBuyCount(0);
        umsMember.setBuyMoney(BigDecimal.ZERO);
        umsMember.setBlance(new BigDecimal(10000));
        umsMember.setIntegration(10000);
        if (ValidatorUtils.notEmpty(user.getInvitecode())) {
            umsMember.setInvitecode(user.getInvitecode());
        }
        try {
            String defaultIcon = "http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20190830/uniapp.jpeg";
            umsMember.setIcon(defaultIcon);
            //这是要生成二维码的url
            String url = "http://www.yjlive.cn:8082/?invitecode=" + user.getUsername();
            //要添加到二维码下面的文字
            String words = user.getUsername() + "的二维码";
            //调用刚才的工具类
            ByteArrayResource qrCode = MatrixToImageWriter.createQrCode(url, words);
            InputStream inputStream = new ByteArrayInputStream(qrCode.getByteArray());

//            umsMember.setAvatar(aliyunOSSUtil.upload("png", inputStream));
        } catch (Exception e) {

        }

        memberMapper.insert(umsMember);

        redisService.set(String.format(Rediskey.MEMBER, umsMember.getUsername()), JsonUtils.objectToJson(umsMember));

        umsMember.setPassword(null);
        return new CommonResult().success("注册成功", null);
    }


    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);
        return new CommonResult().success("获取验证码成功", sb.toString());
    }

    @Override
    public CommonResult updatePassword(String telephone, String password, String authCode) {
        UmsMember example = new UmsMember();
        example.setPhone(telephone);
        UmsMember member = memberMapper.selectOne(new QueryWrapper<>(example));
        if (member == null) {
            return new CommonResult().failed("该账号不存在");
        }
        //验证验证码
        if (!verifyAuthCode(authCode, telephone)) {
            return new CommonResult().failed("验证码错误");
        }

        member.setPassword(passwordEncoder.encode(password));
        memberMapper.updateById(member);
        return new CommonResult().success("密码修改成功", null);
    }


    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode, String telephone) {
        if (StringUtils.isEmpty(authCode)) {
            return false;
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        return authCode.equals(realAuthCode);
    }


    @Override
    public Map<String, Object> login(String username, String password) {

        Map<String, Object> tokenMap = new HashMap<>();
        String token = null;
        try {
            MemberDetails userDetails = (MemberDetails) userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UmsMember member = this.getByUsername(username);
            //验证验证码
           /* if (!verifyAuthCode(user.getCode(), member.getPhone())) {
                throw  new ApiMallPlusException("验证码错误");
            }*/

            //   Authentication authentication = authenticationManager.authenticate(authenticationToken);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            tokenMap.put("userInfo", member);
        } catch (Exception e) {
            LOGGER.warn("登录异常:{}", e.getMessage());

        }

        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        return tokenMap;

    }


    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        if (jwtTokenUtil.canRefresh(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

}

