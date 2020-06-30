package com.qiumi.app.wallet.mine.api.output;

import com.appframe.framework.http.HttpResult;

public class SecurityLevelEntity extends HttpResult<SecurityLevelEntity> {
    public String nation = "", securityLevel, KYC1 = "", KYC2 = "", payPass = "";
    public WithdrawAmount withdrawAmount;

    public static class WithdrawAmount {
        public String coinId = "", coinSymbol = "", amount = "", dayAmount = "";
    }
}
