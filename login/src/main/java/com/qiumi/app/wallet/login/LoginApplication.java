package com.qiumi.app.wallet.login;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appframe.framework.application.FrameworkApplication;

/**
 * @author jiangkun
 * @date 2019/8/22
 */

public class LoginApplication extends FrameworkApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化ARouter。
        initARouter();
    }

    /**
     * 初始化ARouter。
     */
    private void initARouter() {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this);
    }
}
