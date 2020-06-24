package com.zscat.mallplus.vo;

import lombok.Data;

@Data
public class AppletLoginParam {

    private String code;

    private String encrypted_data;
    private String iv;

    private String signature;

    private String userInfo;
    private String cloudID;

    private String phone;
    private String openid;
}
