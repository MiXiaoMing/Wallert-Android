package com.qiumi.app.wallet.login.api.output;

import com.appframe.framework.http.HttpResult;

import java.io.Serializable;

public class SignUpResult extends HttpResult<SignUpResult> implements Serializable {

    public String token, uuid, secret;
}
