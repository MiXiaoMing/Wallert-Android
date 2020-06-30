package com.qiumi.app.support.common;


public enum BusinessStatusEnum {

	ERR_0(0, "请求失败"),
	ERR_1(1, "请求成功"),
	
	SIGNATURE_FAIL(0, "非法请求"),
	USER_TOKEN_FAIL(0,"用户未登录"),
	
	ERR_70001(0,"接口调用认证失败,请退出重试"),//签名错误
	SIGNUP_SUCCESS(1,"注册成功"),
	SIGNUP_CODE_FAIL(2,"注册失败，验证码错误"),
	SIGNUP_FAIL(0,"注册失败"),
	
	SIMPLE_USERINFO_SUCCESS(1,"获取用户信息成功"),
	SIMPLE_USERINFO_FAIL(0,"获取用户信息失败"),

	ASSERT_SIMPLE_SUCCESS(1,"获取用户资产信息成功"),
	ASSERT_SIMPLE_FAIL(0,"获取用户资产信息失败"),
	
	SMS_CODE_TRANSFER_SUCCESS(1,"短信验证码发送成功"),
	SMS_CODE_TRANSFER_FAIL(0,"短信验证码发送失败"),
	SMS_CODE_SIGNUP_SUCCESS(1,"短信发送成功"),
	SMS_CODE_SIGNUP_FAIL(0,"短信发送失败"),
	SMS_CODE_FREQUENT(2,"已经发送过，请勿频繁发送"),
	SMS_CODE_WITHDRAW_FAIL(1,"短信验证码发送成功"),
	SMS_CODE_WITHDRAW_SUCCESS(0,"短信验证码发送失败"),
	
	SMS_CODE_SET_PAY_PASS_SUCCESS(1,"短信验证码发送成功"),
	SMS_CODE_SET_PAY_PASS_FAIL(0,"短信验证码发送失败"),
	
	
	TRANSFER_FEE_SUCCESS(1,"获取转账费用成功"),
	TRANSFER_FEE_FAIL(1,"获取转账费用失败"),
	TRANSFER_FEE_HIGH(1,"获取转账"),
	TRANSFER_FEE_LOW(2,"转账金额低于系统最低转账金额，无法支持"),
	TRANSFER_FEE_AMOUNT_INVALID(2,"转账金额低于系统最低转账金额，无法支持"),
	
	TRANSFER_AMOUNT_FAIL(0,"获取可转账金额失败"),
	TRANSFER_AMOUNT_SUCCESS(1,"获取可转账金额成功"),
	TRANSFER_AMOUNT_LOWER(2,"获取可转账金额成功，可转账金额小于系统最低转账金额"),
	TRANSFER_AMOUNT_HIGHER(3,"获取可转账金额成功，可转账金额高于最大可转账金额"),
	
	SECURITY_LEVEL_SUCCESS(1,"获取用户安全级别成功"),
	SECURITY_LEVEL_FAIL(0,"获取用户安全级别失败"),
	
	SECURITY_SET_PAY_PASS_SUCCESS(1,"设置用户支付密码成功"),
	SECURITY_SET_PAY_PASS_CODE_FAIL(3,"短信验证码错误"),
	SECURITY_SET_PAY_PASS_EXIST(2,"支付密码已存在"),
	SECURITY_SET_PAY_PASS_FAIL(0,"设置用户支付密码失败"),
	
	
	TRANSFER_SUBMIT_FAIL(0,"转账失败"),
	TRANSFER_SUBMIT_SUCCESS(1,"转账成功"),
	TRANSFER_SUBMIT_AMOUNT_HIGH(2,"转账额度大于最大可转账额度"),
	TRANSFER_SUBMIT_AMOUNT_LOW(3,"转账额度小于最小转账额度"),
	TRANSFER_SUBMIT_AMOUNT_HIGHEST(4,"转账额度大于系统最大转账额度"),
	TRANSFER_SUBMIT_AMOUNT_DAILY(5,"超过当日可转账额度"),
	
	TRANSFER_SUBMIT_TOADDR_ERROR(6,"转出地址不存在或状态异常"),
	TRANSFER_SUBMIT_FROM_BALANCE_ERROR(7,"转出账户状态异常"),
	TRANSFER_SUBMIT_TO_BALANCE_ERROR(8,"转入账户状态异常"),
	TRANSFER_SUBMIT_AMOUNT_SEC_HIGH(9,"今日转账额度已用完"),
	TRANSFER_SUBMIT_SEC_LEVEL(10,"未进行身份认证，无法进行转账"),
	TRANSFER_SUBMIT_NON_PAYPASS(11,"未设定支付密码，无法进行转账"),
	TRANSFER_SUBMIT_COIN_NOT_EXCHANGEABLE(12,"该币种目前不支持转账交易"),
	TRANSFER_SUBMIT_FEE_FAIL(13,"获取转账费用失败"),
	TRANSFER_SUBMIT_PAY_PASS_ERROR(14,"支付密码错误"),
	TRANSFER_SUBMIT_SMS_CODE_ERROR(15,"短信验证码错误"),
	
	USER_ASSERT_RECORDS_SUCCESS(1,"获取账单记录成功"),
	USER_ASSERT_RECORDS_FAIL(0,"获取账单记录失败"),
	
	USER_ASSET_RECORD_DETAIL_SUCCESS(1,"获取账单详情成功"),
	USER_ASSET_RECORD_DETAIL_FAIL(0,"获取账单详情失败"),
	USER_ASSET_RECORD_DETAIL_EXCEPTION(0,"获取账单详情异常"),
	
	NEW_ADDR_CHECK_YES(1,"新地址"),
	NEW_ADDR_CHECK_NO(2,"非新地址"),
	NEW_ADDR_CHECK_NA(3,"地址不存在"),
	NEW_ADDR_CHECK_FAIL(0,"请求失败"),
	
	WITHDRAW_AMOUNT_FAIL(0,"获取可提现金额失败"),
	WITHDRAW_AMOUNT_SUCCESS(1,"获取可提现金额成功"),
	WITHDRAW_AMOUNT_DAILY(1,"可转超过当日限额，返回当日可转账额度"),
	WITHDRAW_AMOUNT_LOWER(2,"获取可提现金额成功，可提现金额小于系统最低提现金额"),
	WITHDRAW_AMOUNT_HIGHER(3,"获取可提现金额成功，可提现金额高于最大可提现金额"),
	
	WITHDRAW_FEE_SUCCESS(1,"获取提现费用成功"),
	WITHDRAW_FEE_FAIL(0,"获取提现费用失败"),
	WITHDRAW_FEE_HIGH(1,"获取提现费用成功，达到封顶手续费"),
	WITHDRAW_FEE_LOW(1,"提现按最低费用计，无法支持"),
	WITHDRAW_FEE_AMOUNT_INVALID(2,"转账金额低于系统最低转账金额，无法支持"),
	
	
	WITHDRAW_SUBMIT_FAIL(0,"提现失败"),
	WITHDRAW_SUBMIT_SUCCESS(1,"提现请求已提交"),
	WITHDRAW_SUBMIT_AMOUNT_HIGH(2,"提现额度大于最大可提现额度"),
	WITHDRAW_SUBMIT_AMOUNT_LOW(3,"提现额度小于最小提现额度"),
	WITHDRAW_SUBMIT_AMOUNT_HIGHEST(4,"提现额度大于系统最大提现额度"),
	WITHDRAW_SUBMIT_AMOUNT_DAILY(5,"超过当日可提现额度"),
	
	WITHDRAW_SUBMIT_TOADDR_ERROR(6,"提现地址不存在或状态异常"),
	WITHDRAW_SUBMIT_FROM_BALANCE_ERROR(7,"提现账户状态异常"),
	//WITHDRAW_SUBMIT_TO_BALANCE_ERROR(8,"转入账户状态异常"),
	WITHDRAW_SUBMIT_AMOUNT_SEC_HIGH(9,"今日提现额度已用完"),
	WITHDRAW_SUBMIT_SEC_LEVEL(10,"未进行身份认证，无法进行提现"),
	WITHDRAW_SUBMIT_NON_PAYPASS(11,"未设定支付密码，无法进行提现"),
	WITHDRAW_SUBMIT_COIN_NOT_EXCHANGEABLE(12,"该币种目前不支持提现交易"),
	WITHDRAW_SUBMIT_FEE_FAIL(13,"获取提现费用失败"),
	WITHDRAW_SUBMIT_PAY_PASS_ERROR(14,"支付密码错误"),
	WITHDRAW_SUBMIT_SMS_CODE_ERROR(15,"短信验证码错误");

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private Integer status;
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	private String message;
	
	BusinessStatusEnum(Integer status, String message) {
		this.status = status;
		this.message = message;
	}
	
}

