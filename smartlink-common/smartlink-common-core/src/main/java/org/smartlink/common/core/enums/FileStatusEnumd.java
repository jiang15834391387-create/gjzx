package org.smartlink.common.core.enums;

import lombok.Getter;

/**
 * 文件状态枚举
 */
@Getter
public enum FileStatusEnumd {

    /**
     * 文件已上传
     */
    UPLOADED_SUCCESSFUL_CODE("0","文件已上传"),
    /**
     * 文件上传失败
     */
    UPLOAD_FAILED_CODE("1","文件上传失败"),
    /**
     * 文件已归档
     */
    ARCHIVED_CODE("2","文件已归档"),
    /**
     * 文件无效
     */
    INVALID_CODE("3","文件无效"),

    /**
     * 文件修改状态
     */
    UPDATE_YES("4","修改成功"),

    /**
     * 待报销
     */
    TO_BE_REIMBURSED("5","待报销"),
    /**
     * 已报销
     */
    REIMBURSED("6","已报销"),
    /**
     * 已删除
     */
    DELETED("7","已删除"),
    /**
     * 已删除
     */
    OCR_FAILED("8","OCR识别失败！");

    FileStatusEnumd(String code, String desc) {
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


    public static FileStatusEnumd getByCode(String code){
        FileStatusEnumd[] values = FileStatusEnumd.values();
        for (FileStatusEnumd typeEnum:values) {
            if(typeEnum.code.equals(code)){
                return typeEnum;
            }
        }
        return null;
    }
}
