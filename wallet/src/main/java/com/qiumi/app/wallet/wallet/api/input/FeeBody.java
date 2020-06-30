package com.qiumi.app.wallet.wallet.api.input;

import java.io.Serializable;

public class FeeBody implements Serializable {
    public CoinType coinType = new CoinType();
    public String amount;
    public String sig = "";

    public class CoinType implements Serializable{
        public String coinId, coinSymbol;
    }
}