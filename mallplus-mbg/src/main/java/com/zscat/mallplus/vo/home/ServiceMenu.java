package com.zscat.mallplus.vo.home;

import lombok.Data;

/**
 */
@Data
public class ServiceMenu {

    private String name;
    private String pic;
    private String url;
    private String wapUrl;

    public ServiceMenu() {
    }

    public ServiceMenu(String name, String pic, String url, String wapUrl) {
        this.name = name;
        this.pic = pic;
        this.url = url;
        this.wapUrl = wapUrl;
    }
}
