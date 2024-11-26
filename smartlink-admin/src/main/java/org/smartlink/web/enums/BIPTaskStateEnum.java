package org.smartlink.web.enums;

/**
 * 单据状态
 *
 * @author L
 */
public enum BIPTaskStateEnum {

    /**
     * 待扫描
     */
    TASK_STATE_REGISTER("0","待扫描"),

    /**
     * 扫描完成
     */
    TASK_STATE_COMPLETE("1","扫描完成"),

    /**
     * 待补扫
     */
    TASK_STATE_MAKEUP("2","待补扫"),

    /**
     * 补扫完成
     */
    TASK_STATE_MAKEUP_DOWN("3","补扫完成"),

    /**
     * 待重扫
     */
    TASK_STATE_RESCAN("4","待重扫"),

    /**
     * 重扫完成
     */
    TASK_STATE_RESCAN_DOWN("5","重扫完成");


    private final String state;
    private final String stateName;

    BIPTaskStateEnum(String state, String stateName) {
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
