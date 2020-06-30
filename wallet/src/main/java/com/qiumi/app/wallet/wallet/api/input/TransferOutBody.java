package com.qiumi.app.wallet.wallet.api.input;

import java.io.Serializable;

public class TransferOutBody implements Serializable {
    public String toCountryCode = "", toMobile = "";
    public String smsCode, idCode = "", payPass = "";
    public String userMessage;
    public String timestamp = "", nonce = "", sig = "";
    public Amount amount = new Amount();


    public class Amount implements Serializable {
        public String coinId = "", coinSymbol = "";
        public String amount = "", dailyAmount = "";
    }
}