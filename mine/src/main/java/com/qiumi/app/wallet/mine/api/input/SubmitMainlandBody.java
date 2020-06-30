package com.qiumi.app.wallet.mine.api.input;

import java.io.Serializable;

public class SubmitMainlandBody implements Serializable {
    public String fileCode, smsCode;
    public String timestamp, nonce, sig;
}
