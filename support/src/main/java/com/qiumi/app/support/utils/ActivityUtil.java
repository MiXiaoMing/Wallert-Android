package com.qiumi.app.support.utils;

import android.app.Activity;

import java.util.Locale;

/**
 * 页面 工具类
 */

public class ActivityUtil {

    public static boolean isAvailable(Activity activity) {
        if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
            return true;
        }
        return false;
    }
}
