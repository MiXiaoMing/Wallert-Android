package com.qiumi.app.wallet.mine.identification.bean;

import java.io.Serializable;

/**
 * 国家bean。
 *
 * @author jiangkun
 * @date 2019/9/10
 */

public class MineCountryBean implements Serializable {
    private int code;
    private String name;

    public MineCountryBean(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
