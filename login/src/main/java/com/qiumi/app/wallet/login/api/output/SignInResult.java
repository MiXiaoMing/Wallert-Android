package com.qiumi.app.wallet.login.api.output;

import com.appframe.framework.http.HttpResult;

import java.io.Serializable;

public class SignInResult extends HttpResult<SignInResult> {
    public String token, userId, secret;
    public String redirect_url, tmp_code;
}
