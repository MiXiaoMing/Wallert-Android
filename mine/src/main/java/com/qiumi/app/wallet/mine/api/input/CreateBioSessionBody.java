package com.qiumi.app.wallet.mine.api.input;

import java.io.Serializable;

public class CreateBioSessionBody implements Serializable {
    public String timestamp, nonce, sig;
}
