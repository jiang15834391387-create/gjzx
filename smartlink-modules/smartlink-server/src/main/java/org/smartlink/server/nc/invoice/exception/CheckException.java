package org.smartlink.server.nc.invoice.exception;

/**
 * 查验异常类
 *
 * @author 马旭辉
 */
public class CheckException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CheckException(String msg) {
        super(msg);
    }

}
