package com.qiumi.app.wallet.login.api.input;

import java.io.Serializable;

public class SignInBody implements Serializable {
    public String countryCode, mobile, password;
    public String timestamp, nonce, sig;
}
