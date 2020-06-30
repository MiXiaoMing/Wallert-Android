package com.qiumi.app.wallet.wallet.api.output;

import com.appframe.framework.http.HttpResult;

import java.io.Serializable;

public class AmountEntity extends HttpResult<AmountEntity> {
    public Amount amount;
    public Fee fee;

    public class Amount {
        public String coinSymbol = "", coinId = "", amount = "", dailyAmount = "";
    }

    public class Fee {
        public String coinSymbol = "", coinId = "", fee = "";
    }
}
