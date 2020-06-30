package com.qiumi.app.support.component;

import android.content.Context;

/**
 * View继承该接口，实现公共接口方法，规范初始化方法。
 *
 * @author jiangkun
 * @date 2019/9/6
 */

public interface BaseViewInterface {
    /**
     * 初始化数据，一般认为在初始化View前。
     * <p>
     * 内部可执行getIntent()等。
     */
    void initData();

    /**
     * 初始化View。
     * <p>
     * findViewById()等。
     *
     * @param context context
     */
    void initView(Context context);

    /**
     * 初始化监听事件。
     */
    void initListener();

    /**
     * 为View设置数据，网络请求等。
     */
    void setDataToView();
}
