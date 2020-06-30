package com.qiumi.app.wallet.login.api.input;

import java.io.Serializable;

public class RetrieveSmsCodeBody implements Serializable {
    public String token, mobile;
    public String timestamp, nonce, sig;
}
