package com.qiumi.app.wallet.wallet.api.output;

import com.appframe.framework.http.HttpResult;

import java.io.Serializable;

public class DepositeAddressEntity extends HttpResult<DepositeAddressEntity> {
    public CoinType coinType;
    public String mobile, addr;
}
