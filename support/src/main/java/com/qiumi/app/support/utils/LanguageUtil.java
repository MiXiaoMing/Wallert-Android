package com.qiumi.app.support.utils;

import android.text.TextUtils;
import android.view.TextureView;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by ThinkPad on 2019/9/9.
 */

public class LanguageUtil {

    public static boolean isCNLanguage() {
        String language = getLanguageEnv();

        if (language != null && (language.trim().equals("zh-CN") || language.trim().equals("zh-TW"))){
            return true;
        }else {
            return false;
        }
    }

    public static String getLanguageEnv() {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String country = locale.getCountry().toLowerCase();
        if ("zh".equals(language)) {
            if ("cn".equals(country)) {
                return "zh-CN";
            } else if ("tw".equals(country)) {
                return "zh-TW";
            }
        }
        return "en";
    }
}
