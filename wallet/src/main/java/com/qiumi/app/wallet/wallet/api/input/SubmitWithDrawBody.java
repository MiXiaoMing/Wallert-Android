package com.qiumi.app.wallet.wallet.api.input;

import java.io.Serializable;

public class SubmitWithDrawBody implements Serializable {
    public String toAddr = "", smsCode = "", idCode = "", payPass = "";
    public String timestamp = "", nonce = "", sig = "";
    public Amount amount = new Amount();


    public class Amount implements Serializable {
        public String coinId = "", coinSymbol = "";
        public String amount = "", dailyAmount = "";
    }
}