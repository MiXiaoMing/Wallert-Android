package com.qiumi.app.wallet.login.api.input;

import java.io.Serializable;

public class SignUpBody implements Serializable {
    public String mobile, password, countryCode, sMSCode;
    public String invitor;
    public String timestamp, nonce, sig;

}
