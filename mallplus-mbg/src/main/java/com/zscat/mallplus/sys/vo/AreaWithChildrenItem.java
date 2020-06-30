package com.zscat.mallplus.sys.vo;


import com.zscat.mallplus.sys.entity.SysArea;

import java.util.List;

/**
 *
 */
public class AreaWithChildrenItem extends SysArea {
    private List<SysArea> children;

    public List<SysArea> getChildren() {
        return children;
    }

    public void setChildren(List<SysArea> children) {
        this.children = children;
    }
}
