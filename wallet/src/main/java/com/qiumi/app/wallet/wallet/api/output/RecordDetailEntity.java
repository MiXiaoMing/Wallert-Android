package com.qiumi.app.wallet.wallet.api.output;

import java.util.Map;

public class RecordDetailEntity {
    public String tranNO, statusMsg, dateTime, TXID, blockNo, relatedTransNo, status, userMeg;
    public Account amount;
    public Fee fee;
    public Address from, to;
    public Map<String, String> opType;

    public static class Account {
        public String coinId, coinSymbol, dailyAmount;
        public Float amount;
    }

    public static class Fee {
        public String coinId, coinSymbol, fee;
    }

    public static class Address {
        public String addr, mobile;
    }
}
