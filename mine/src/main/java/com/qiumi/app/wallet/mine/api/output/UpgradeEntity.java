package com.qiumi.app.wallet.mine.api.output;

import com.appframe.framework.http.HttpResult;

public class UpgradeEntity extends HttpResult<UpgradeEntity> {
    public String latestver = "", force = "", downloadUrl = "", browserUrl = "";
    public int latestVerCode;
}
