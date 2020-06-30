package com.qiumi.app.wallet.mine.api.output;

import com.appframe.framework.http.HttpResult;

public class SettingInfoEntity extends HttpResult<SettingInfoEntity> {
    public String language = "", currency = "", rate = "";
}
