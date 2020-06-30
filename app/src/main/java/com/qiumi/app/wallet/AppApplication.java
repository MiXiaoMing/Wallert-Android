package com.qiumi.app.wallet;

import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appframe.framework.application.FrameworkApplication;
import com.appframe.framework.config.MetaDataConfig;
import com.qiumi.app.support.utils.UserUtil;
import com.zhy.autolayout.config.AutoLayoutConifg;

public class AppApplication extends FrameworkApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        AutoLayoutConifg.getInstance().useDeviceSize();

        // 初始化配置信息
        initConfig();
        // 初始化ARouter。
        initARouter();

        initAli();

        // TODO: 2019/10/15 测试数据
//        UserUtil.setSecret("83cc8054-e559-4add-aebe-dab5c34c926e");
//        UserUtil.setToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIrODYxMzgxMTg4MDMyMyIsImV4cCI6MTU3NTQ2NDU0NywiaWF0IjoxNTc1NDY0NTQ3LCJqdGkiOiIzYmZmNTBjYy02Mjg2LTQyNTYtYWFlNi05OTVkYTMwNjQ3NmUiLCJ1c2VybmFtZSI6Iis4NjEzODExODgwMzIzIn0.DYU18j1EMOBgD7jDny9R75kVz1LZ2H_jiHUegA-Mau4");
//        UserUtil.setUserID("630705432960172032");
    }

    private void initConfig() {
        new MetaDataConfig()
                .initCacheDirectory("com.qiumi")
                .initSPStoreName("qiumi");
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

    private void initAli() {
        // 初始化实人认证 SDK
//        RPSDK.initialize(appContext);
    }
}
