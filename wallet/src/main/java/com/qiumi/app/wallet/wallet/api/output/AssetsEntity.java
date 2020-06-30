package com.qiumi.app.wallet.wallet.api.output;

import com.appframe.framework.http.HttpResult;

import java.util.List;

public class AssetsEntity extends HttpResult<AssetsEntity> {
    public String currencyId, name, price, priceTans;
    public List<Asset> asserts;
}
