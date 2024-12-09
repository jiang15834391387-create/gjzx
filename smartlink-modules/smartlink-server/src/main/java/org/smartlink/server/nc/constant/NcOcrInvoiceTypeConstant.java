package org.smartlink.server.nc.constant;

/**
 * @description: NCC识别返回发票类型常量
 * @author: L
 * @create:
 **/
public interface NcOcrInvoiceTypeConstant {

    /**
     * 增值税发票
     */
    public static final String INVOICE = "invoice";

    /**
     * 过路费发票
     */
    public static final String TOLLS = "tolls";

    /**
     * 火车票
     */
    public static final String TRAIN = "train";

    /**
     * 定额发票
     */
    public static final String QUOTA = "quota";

    /**
     * 机打发票
     */
    public static final String MACHINE = "machine";

    /**
     * 出租车发票
     */
    public static final String TAXI = "taxi";

    /**
     * 客运发票
     */
    public static final String PASSENGER = "passenger";

    /**
     * 航空电子行程单
     */
    public static final String AIR = "air";

    /**
     * 其他发票
     */
    public static final String OTHER_INVOICE = "other";

}
