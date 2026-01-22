package org.smartlink.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.nio.file.Paths;
import java.util.Date;


/**
 * 模板附件表
 */
@Data
@TableName("test_template_oss_file")
public class TemplateOssFile {

    @TableId(value = "id")
    private Long id;

    private Long workFlowId;
    /**
     * 附件ID
     */
    private String fileId;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件URL
     */
    private String fileUrl;
    /**
     * 上传时间，对应文档中的 upload_Time
     */
    private Date createTime;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件后缀
     */
    private String fileSuffix;

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = getFileSuffixWithDot(fileSuffix);
    }

    public static String getFileSuffixWithDot(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }

        // 去掉查询参数
        String cleanUrl = url.split("[?#]")[0];

        // 获取文件名
        String fileName = Paths.get(cleanUrl).getFileName().toString();

        // 获取最后一个点及其后面的内容
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0) ? fileName.substring(dotIndex) : "";
    }
}
