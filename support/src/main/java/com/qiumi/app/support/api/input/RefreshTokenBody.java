package com.qiumi.app.support.api.input;

import java.io.Serializable;

public class RefreshTokenBody implements Serializable {
    public String username;
    public String timestamp, nonce, sig;
}
