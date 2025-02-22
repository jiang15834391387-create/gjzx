package org.smartlink.common.check.enumd;

import lombok.Getter;

@Getter
public enum ResponseCodeEnum {
    SUCCESS(10000, "查验成功"),
    NO_SUCH_TICKET(10001, "查无此票"),
    INCONSISTENT_INFO(10002, "查验信息不一致(一般是专票未税金额不正确)"),
    VERIFICATION_LIMIT_EXCEEDED(10003, "验真次数超过限制，同一张票一天最多可以查验5次"),
    UNSUPPORTED_TICKET_TYPE(10004, "不支持验真发票类型"),
    INVALID_PARAMETERS(10005, "无效参数（发票代码，号码，开票日期等为空或者格式错误，普票验证码为空，专票税前金额为空等不合法参数错误）"),
    OTHER_ERRORS(10006, "其它错误");

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
