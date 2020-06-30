package com.qiumi.app.support.common;

public enum AssertCenterFieldDefinition {


    NULL(0, "手机号注册"),

    //相关内容进行相关注册形象
    AMOUNT_DAILY(-3, "使用日安全额度"),
    AMOUNT_HIGHER_SYSTEM(-2, "可提现余额高于系统最高额度，使用系统最高额度"),
    AMOUNT_LOWER_SYSTEM(-1, "可提现额度低于系统最低额度，返回0"),
    AMOUNT_NORMAL(1, "额度正常"),
    AMOUNT_FAIL(0, "获取额度失败"),
    AMOUNT_SEC_FAIL(-4, "未进行kyc，无法转账或提现"),
    AMOUNT_HIGHER_TOTAL(-5, "转账额度高于钱包余额"),

    FEE_NORMAL(1, "获取提现费正常"),
    FEE_FAIL(0, "获取提现费用失败"),
    FEE_HIGHER(-2, "费用达到封顶提现费用，使用封顶提现费用"),
    FEE_LOWER(-1, "获取到最低额度手续费"),
    FEE_AMOUNT_INVALID(-3, "提现额度低于最低提现额度，不计算手续费"),

    TRANSFER_TYPE_TRANSFER(1, "转账"),
    TRANSFER_TYPE_WITHDRAW(2, "提现"),
    TRANSFER_TYPE_DEPOSIT(3, "充值"),

    BLOCK_CONFIRM_INIT(0, "区块确认数初始值"),

    TRANSFER_STATUS_DONE(1, "转账完成"),
    TRANSFER_STATUS_DOING(2, "转账进行中"),
    TRANSFER_STATUS_PENDING(3, "转账挂起"),
    TRANSFER_STATUS_LOW_AMOUNT_FAIL(-1, "转账额度小于系统最低转账额度失败"),
    TRANSFER_STATUS_FAIL(0, "转账失败"),

    WITHDRAW_STATUS_DONE(1, "提现完成"),
    WITHDRAW_STATUS_DOING(2, "提现进行中"),
    WITHDRAW_STATUS_PENDING(3, "提现操作挂起"),
    WITHDRAW_STATUS_SUBMIT(4, "提现已提交"),
    WITHDRAW_STATUS_LOW_AMOUNT_FAIL(-1, "提现额度小于系统最低提现额度失败"),
    WITHDRAW_STATUS_FAIL(0, "提现失败"),

    WALLET_BALANCE_STATUS_NORMAL(1, "钱包状态正常"),
    WALLET_BALANCE_STATUS_LOCK(0, "钱包状态锁定"),
    WALLET_BALANCE_STATUS_SYSTEM(9, "系统钱包"),

    RECORD_OP_TYPE_TRANSFER_OUT(1, "转出"),
    RECORD_OP_TYPE_TRANSFER_IN(2, "转入"),
    RECORD_OP_TYPE_WITHDRAW_OUT(3, "提款"),
    RECORD_OP_TYPE_DEPOSITE_IN(4, "充值"),
    RECORD_OP_TYPE_PAY_OUT(5, "支付"),
    RECORD_OP_TYPE_RECIVE_IN(6, "收款"),
    RECORD_OP_TYPE_TRANSFER_FEE_IN(7, "转账费用"),
    RECORD_OP_TYPE_WITHDRAW_FEE_IN(8, "提现费用"),
    RECORD_OP_TYPE_WITHDRAW_FAIL_IN(9, "提款失败退回"),
    RECORD_OP_TYPE_WITHDRAWING_OUT(10, "提款进行中"),
    RECORD_OP_TYPE_WITHDRAWING_FEE_IN(11, "提现费用(进行中)"),

    WITHDRAWING_OP_TYPE_SUBMIT_TO(0, "已向钱包发起提现"),
    WITHDRAWING_OP_TYPE_SIGNING(1, "提现签名中"),
    WITHDRAWING_OP_TYPE_SUBMITTED(2, "已发送至链上"),
    WITHDRAWING_OP_TYPE_SUCCESS_CONFIRMED(3, "钱包已确认提现操作成功"),
    WITHDRAWING_OP_TYPE_SUCCESS_FAIL(4, "钱包已确认提现操作失败，额度回滚"),

    WITHDRAWING_STATUS_DONE(1, "状态正常"),
    WITHDRAWING_STATUS_FAIL(0, "状态异常"),
    //0:提现中;1:签名中;2:已发送; 3:已确认
    WITHDRAWRECORD_FEEDBACK_STATUS_WITHDRAWING(0, "提现中"),
    WITHDRAWRECORD_FEEDBACK_STATUS_SIGNING(1, "签名中"),
    WITHDRAWRECORD_FEEDBACK_STATUS_SUBMITTED(2, "已发送"),
    WITHDRAWRECORD_FEEDBACK_STATUS_CONFIRMED(3, "已确认");


    private AssertCenterFieldDefinition(Integer code, String detail) {
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