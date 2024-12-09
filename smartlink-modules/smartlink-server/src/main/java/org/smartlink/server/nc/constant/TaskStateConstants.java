package org.smartlink.server.nc.constant;

/**
 * 任务状态常量
 *
 * @author L
 */
public interface TaskStateConstants {

    /**
     * 待扫描
     */
    String TASK_STATE_SCAN = "1";

    /**
     * 扫描完成
     */
    String TASK_STATE_COMPLETE = "2";

    /**
     * 驳回补扫
     */
    String TASK_STATE_BH_BS = "3";

    /**
     * 驳回重扫
     */
    String TASK_STATE_BH_CS = "4";

    /**
     * 补扫完成
     */
    String TASK_STATE_BS_WC = "5";

    /**
     * 重扫完成
     */
    String TASK_STATE_CS_WC = "6";

    /**
     * 退单回收
     */
    String CHARGE_BACK = "7";

    /**
     * 归档完成
     */
    String ARCHIVE_COMPLETE = "8";
}
