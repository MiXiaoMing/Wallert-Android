package com.qiumi.app.support.api.output;

import com.appframe.framework.http.HttpResult;

public class RefreshTokenResult extends HttpResult<RefreshTokenResult> {
    public String token = "";
    public long expire;
}
