package org.smartlink.server.nc.constant;

/**
 * 推送附件状态常量
 *
 * @author chenJiangHong
 */
public interface PushAttachmentConstant {

    /**
     * 待推送状态
     */
    String PUSH_STATUS = "push";

    /**
     * 推送完成状态
     */
    String FINISH_STATUS = "finish";

    /**
     * 待删除状态
     */
    String DELETE_STATUS = "delete";

    /**
     * 推送失败状态
     */
    String PUSH_FAIL = "fail";

    /**
     * 获取文件流失败
     */
    String GET_FILE_FAIL = "file_fail";
    /**
     * 影像报错
     */
    String YX_FAIL = "yx_fail";

    /**
     * 多次推送失败状态
     */
    String FAIL_DELETE = PUSH_FAIL+"_"+DELETE_STATUS;
}
