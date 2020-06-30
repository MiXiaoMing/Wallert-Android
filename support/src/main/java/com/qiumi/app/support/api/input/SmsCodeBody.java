package com.qiumi.app.support.api.input;

import java.io.Serializable;

public class SmsCodeBody implements Serializable {
    public String sig;
    public String mobile, countryCode;
}
