package com.qiumi.app.wallet.wallet.api.input;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AssetsRecordBody implements Serializable {
    public Map<String, Integer> assets = new HashMap<>();
    public Map<String, Integer> opType = new HashMap<>();
    public String latestTranNo = "", recordsNo = "";
    public String startTimestamp = "", endTimestamp = "";
    public String sig = "";
}