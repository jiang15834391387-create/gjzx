package org.smartlink.server.nc.token.response;

import lombok.Data;

/**
 * @description: NCC获取token接收对象
 * @author: L
 * @create:
 **/
@Data
public class Token {

    /**
     * 是否成功
     */
    private String success;

    /**
     * 状态码    eq：0000为成功；其余为失败
     */
    private String code;

    /**
     * 返回提示信息
     */
    private String message;

    /**
     * 接受返回data对象
     */
    private NccTokenData data;
}
