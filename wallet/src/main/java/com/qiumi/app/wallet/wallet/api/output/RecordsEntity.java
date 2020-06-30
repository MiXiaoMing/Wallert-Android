package com.qiumi.app.wallet.wallet.api.output;

import com.appframe.framework.http.HttpResult;

import java.util.List;

public class RecordsEntity extends HttpResult<RecordsEntity> {
    public List<Asset> assets;
    public List<Record> records;
}
