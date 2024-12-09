package org.smartlink.server.nc.utils.biptoken.response;

import lombok.Data;

/**
 * @description: BIP获取token接收对象
 * @author: L
 * @create:
 **/
@Data
public class Token {

    public static final String SUCCESS_CODE = "00000";

    /**
     * 状态码    eq：00000为成功；其余为失败
     */
    private String code;

    /**
     * 返回提示信息
     */
    private String message;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }

    /**
     * 接受返回data对象
     */
    private BipTokenData data;
}
