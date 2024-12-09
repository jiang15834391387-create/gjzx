package org.smartlink.server.nc.exception;



/**
 * <p>Title: AppException</p>
 * <p>Description:根异常类 </p>
 * root Exception继承自RuntimeException，但在方法接口上显式声明这些异常。<br>
 * <ol>
 * <li>类似checked exception，从接口即可看出会发生哪些异常</li>
 * <li>调用方不强制处理异常。而且，接口的异常声明改变也不会影响到调用方代码</li>
 * <ol>
 *
 * @author L
 * @date
 **/
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 2376627903957963613L;


    /**
     * 创建新的根异常类 <codeAppException</code>.
     */
    public AppException() {

    }

    /**
     * 创建新的根异常类 <code>CacheException</code>.
     *
     * @param message 异常原因信息 the reason for the exception.
     */
    public AppException(String message) {

        super(message);

    }

    /**
     * 创建新的根异常类 <code>CacheException</code>.
     *
     * @param cause 底层根本原因 the underlying cause of the exception.
     */
    public AppException(Throwable cause) {

        super(cause);

    }

    /**
     *  创建新的根异常类 <code>CacheException</code>.
     *
     * @param message 异常原因信息  the reason for the exception.
     * @param cause   底层根本原因  the underlying cause of the exception.
     */
    public AppException(String message, Throwable cause) {

        super(message, cause);

    }

}
