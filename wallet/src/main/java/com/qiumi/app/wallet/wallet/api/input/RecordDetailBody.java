package com.qiumi.app.wallet.wallet.api.input;

import java.io.Serializable;

public class RecordDetailBody implements Serializable {
    public String opType = "";
    public Long tranNo = 0L;
    public String sig = "";
}