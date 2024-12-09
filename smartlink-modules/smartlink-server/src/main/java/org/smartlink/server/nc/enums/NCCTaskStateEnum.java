package org.smartlink.server.nc.enums;

/**
 * 单据状态
 *
 * @author L
 */
public enum NCCTaskStateEnum {
    /**
     * 待登记
     */
    TASK_STATE_REGISTER("0", "待登记接收"),
    /**
     * 扫描完成
     */
    TASK_STATE_COMPLETE("1", "扫描完成"),
    /**
     * 已退单
     */
    TASK_STATE_BILL_BACK("2", "已退单"),
    /**
     * 待扫描
     */
    TASK_STATE_SCAN("3", "待扫描"),
    /**
     * 影像系统待补扫，NC系统包含两层含义：待重扫和待补扫
     */
    TASK_STATE_BS("4", "驳回补扫"),

    /**
     * 补扫完成  NC系统包含两层含义：补扫完成，重扫完成
     */
    TASK_STATE_BS_COMPLETE("5", "补扫完成"),

    /**
     * 已存档
     */
    TASK_STATE_ARCHIVED("6", "已存档"),
    /**
     * 驳回重扫
     */
    TASK_STATE_CS("7", "驳回重扫"),
    /**
     * 重扫完成
     */
    TASK_STATE_CS_COMPLETE("8", "重扫完成");

    private final String state;
    private final String stateName;

    NCCTaskStateEnum(String state, String stateName) {
        this.state = state;
        this.stateName = stateName;
    }

    public String getState() {
        return state;
    }

    public String getStateName() {
        return stateName;
    }
}
