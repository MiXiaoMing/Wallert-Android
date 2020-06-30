package com.qiumi.app.wallet.mine.api.input;

import java.io.Serializable;

public class UpdatePassBody implements Serializable {
    public String orgPassword, newPassword, smsCode;
    public String timestamp, nonce, sig;
}
