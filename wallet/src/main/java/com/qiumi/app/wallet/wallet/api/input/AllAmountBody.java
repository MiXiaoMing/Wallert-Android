package com.qiumi.app.wallet.wallet.api.input;

import java.io.Serializable;

public class AllAmountBody implements Serializable {
    public CoinType coinType = new CoinType();
    public String sig = "";

    public class CoinType implements Serializable{
        public String coinId, coinSymbol;
    }
}