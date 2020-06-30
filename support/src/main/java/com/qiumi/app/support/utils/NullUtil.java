package com.qiumi.app.support.utils;

/**
 * 判断空或非空。
 *
 * @author jiangkun
 * @date 2019/9/6
 */

public class NullUtil {

    /**
     * 判断为null。
     *
     * @param t   泛型参数
     * @param <T> 泛型标识
     * @return true：null；false：不为null
     */
    public static <T> boolean isNull(T t) {
        return t == null;
    }

    /**
     * 判断不为null。
     *
     * @param t   泛型参数
     * @param <T> 泛型标识
     * @return true：不为null；false：null
     */
    public static <T> boolean isNotNull(T t) {
        return !isNull(t);
    }
}
