package com.qiumi.app.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    /**
     * 预置 随机值
     */
    public final static String PREKEY = "caa6fa1d-e115-4908-9f6a-326cc4c50c4d";

    /**
     * ID Name基类
     */
    public static class IDNamePair implements Serializable {
        public String id, name;

        public IDNamePair(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    /**
     * 操作类型
     */
    public static final List<IDNamePair> operTypes;

    static {
        operTypes = new ArrayList<>();
        operTypes.add(new IDNamePair("3", "提款"));
        operTypes.add(new IDNamePair("4", "充值"));
        operTypes.add(new IDNamePair("1", "转出"));
        operTypes.add(new IDNamePair("2", "转入"));
    }

    public static String getOperName(String operType) {
        for (int i = 0; i < operTypes.size(); ++i) {
            if (operTypes.get(i).id.equals(operType)) {
                return operTypes.get(i).name;
            }
        }
        return "";
    }

    /**
     * ID类型
     */
    public static final List<IDNamePair> IDTypes;

    static {
        IDTypes = new ArrayList<>();
        IDTypes.add(new IDNamePair("1", "身份证"));
        IDTypes.add(new IDNamePair("2", "护照"));
        IDTypes.add(new IDNamePair("3", "驾照"));
    }

    public static String getIDName(String idType) {
        for (int i = 0; i < IDTypes.size(); ++i) {
            if (IDTypes.get(i).id.equals(idType)) {
                return IDTypes.get(i).name;
            }
        }
        return "";
    }

    /**
     * 语言类型
     */
    public static final List<IDNamePair> languages;

    static {
        languages = new ArrayList<>();
        languages.add(new IDNamePair("1", "跟随系统"));
        languages.add(new IDNamePair("2", "中文"));
        languages.add(new IDNamePair("3", "英文"));
    }

    public static String getLanguage(String type) {
        for (int i = 0; i < languages.size(); ++i) {
            if (languages.get(i).id.equals(type)) {
                return languages.get(i).name;
            }
        }
        return "";
    }

    public static String getLanguageID(String name) {
        for (int i = 0; i < languages.size(); ++i) {
            if (languages.get(i).name.equals(name)) {
                return languages.get(i).id;
            }
        }
        return "1";
    }

    /**
     * 金币类型
     */
    public static final List<IDNamePair> currency;
    public static final String currency_rmb = "10000";
    public static final String currency_usdt = "10001";

    static {
        currency = new ArrayList<>();
        currency.add(new IDNamePair(currency_rmb, "人民币"));
        currency.add(new IDNamePair(currency_usdt, "美元"));
    }

    public static String getCurrency(String type) {
        for (int i = 0; i < currency.size(); ++i) {
            if (currency.get(i).id.equals(type)) {
                return currency.get(i).name;
            }
        }
        return "";
    }

    public static String getCurrencyID(String name) {
        for (int i = 0; i < currency.size(); ++i) {
            if (currency.get(i).name.equals(name)) {
                return currency.get(i).id;
            }
        }
        return currency_rmb;
    }

}
