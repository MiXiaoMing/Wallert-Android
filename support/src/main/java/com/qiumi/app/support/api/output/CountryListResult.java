package com.qiumi.app.support.api.output;

import com.appframe.framework.http.HttpArrayResult;

public class CountryListResult extends HttpArrayResult<CountryListResult> {
    public String code = "", zh = "", en = "";
}
