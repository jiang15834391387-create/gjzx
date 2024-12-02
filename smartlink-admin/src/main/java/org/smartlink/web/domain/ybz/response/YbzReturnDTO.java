package org.smartlink.web.domain.ybz.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回给友报账的数据格式
 *
 * @author L
 */
@Data
public class YbzReturnDTO<T> implements Serializable {
    /**
     * 返回结果，0成功，1失败
     */
    private String result;

    /**
     * 执行成功返回的提示语
     */
    private String success;

    /**
     * 执行失败返回的语句
     */
    private String errormsg;

    /**
     * 执行成功返回的数据
     */
    private T items;

    public YbzReturnDTO(boolean result, String msg,T items) {
        if (result) {
            //返回成功
            this.result = "0";
            this.success = msg;
        } else {
            //返回失败
            this.result = "1";
            this.success = "";
            this.errormsg = msg;
        }
        this.items=items;
    }
}
