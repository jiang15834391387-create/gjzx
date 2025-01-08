package org.smartlink.business.enumd;

import lombok.Getter;

/**
 * 发票查验枚举
 */
@Getter
public enum CheckInvoiceStatusEnumd {

    /**
     * 发票待查验
     */
    TO_BE_VERIFIED_CODE("0","发票待查验"),
    /**
     * 发票查验中
     */
    IN_PROGRESS_CODE("1","发票查验中"),
    /**
     * 发票查验成功
     */
    VERIFICATION_SUCCESSFUL_CODE("2","发票查验成功"),
    /**
     * 发票查验失败
     */
    VERIFICATION_FAILED_CODE("3","发票查验失败"),
    /**
     * 发票已作废
     */
    INVALIDATED_CODE("4","发票已作废"),
    /**
     * 发票已红冲
     */
    RED_INVOICED_CODE("5","发票已红冲"),
    /**
     * 发票已过期
     */
    EXPIRED_CODE("6","发票已过期"),
    /**
     * 无效发票
     */
    INVALID_INVOICE_CODE("7","无效发票"),
    ;

    CheckInvoiceStatusEnumd(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    private String code;
    private String desc;

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public static CheckInvoiceStatusEnumd getByCode(String code){
        CheckInvoiceStatusEnumd[] values = CheckInvoiceStatusEnumd.values();
        for (CheckInvoiceStatusEnumd typeEnum:values) {
            if(typeEnum.code.equals(code)){
                return typeEnum;
            }
        }
        return null;
    }
}
