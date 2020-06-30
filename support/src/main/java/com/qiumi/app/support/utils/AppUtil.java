package com.qiumi.app.support.utils;

import com.appframe.library.storage.SharePreferences;
import com.qiumi.app.support.Constants;

public class AppUtil {

    private static String key_currency_type = "currencyType";

    public static void setCurrencyType(String id) {
        SharePreferences.putString(key_currency_type, id);
    }

    public static String getCurrencyType() {
        return SharePreferences.getStringWithDefault(key_currency_type, Constants.currency_rmb);
    }

}
