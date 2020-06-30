package com.qiumi.app.wallet.login.api.input;

import java.io.Serializable;

public class UpdatePassBody implements Serializable {
    public String token;
    public String org_password, new_password;
    public String code;
    public String timestamp, nonce, sig, invitor;
}
