package com.qiumi.app.wallet.mine.api.input;

import java.io.Serializable;

public class SetPayPassBody implements Serializable {
    public String payPass, smsCode, timestamp, nonce, sig;
}
