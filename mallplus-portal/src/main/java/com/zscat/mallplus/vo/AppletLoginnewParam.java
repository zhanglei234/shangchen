package com.zscat.mallplus.vo;

import lombok.Data;

@Data
public class AppletLoginnewParam {

    private String code;

    private String encryptedData;
    private String iv;

    private String signature;

    private UserInfo userInfo;
    private String cloudID;


}
