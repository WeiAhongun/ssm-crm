package com.sm.cn.common.http;

public enum AxiosStatus {
    OK("20000","操作成功"),
    ERROR("50000","操作失败"),
    EMPLOYEE_NOT_FOUND("40001","员工不存在"),
    EMPLOYEE_NOT_ACTIVE("40002","员工未激活"),
    EMPLOYEE_ACTIVEING("40003","员工已激活，请登入" ),
    EMAIL_SENDER_SUCCESS("40004","邮件发送成功" ), LINK_UNUSABLE("40005","链接失效" ),
    Code_ERROR("40006","验证码错误" );


    private String status;
    private String msg;

    AxiosStatus(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
