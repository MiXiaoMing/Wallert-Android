package com.qiumi.app.support.common;

public enum UserCenterFieldDefinition {

    WALLET_BALANCE_STATUS_NORMAL(1, "状态正常"),

    USER_SECURITY_LEVEL_REGISTER(0, "手机号注册"),
    USER_SECURITY_LEVEL_KYC1(1,"已进行过KYC1认证"),
    USER_SECURITY_LEVEL_KYC2(2,"已进行过KYC2认证"),

    USER_SECURITY_PAY_PASS_SET(1,"支付密码已设置"),
    USER_SECURITY_PAY_PASS_NOTSET(0,"支付密码未设置"),

    CURRENCY_EXCHANGEABLE(1,"可转账交易"),
    CURRENCY_NOT_EXCHANGEABLE(0,"不可转账交易"),

    CURRENCY_TRANSFERABLE(1,"可转账交易"),
    CURRENCY_NOT_TRANSFERABLE(0,"不可转账交易"),

    CURRENCY_WITHDRAWABLE(1,"可提现交易"),
    CURRENCY_NOT_WITHDRAWABLE(0,"不可提现交易"),

    USER_STATUS_NORMAL(1,"用户状态正常"),
    USER_STATUS_LOCK(0,"用户状态锁定");


	/*
	//security_Level define
	public static final Integer  = 0;
	public static final Integer SECURITY_MOBILE_AUTH = 1; //Only the mobile SMS verify
	public static final Integer SECURITY_ID_AUTH =2;//ID CARD AUTH
	public static final Integer SECURITY_SENIOR_AUTH = 3;//senior auth
	*/



    private UserCenterFieldDefinition(Integer code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    private Integer code;
    private String detail;


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
