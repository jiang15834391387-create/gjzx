package org.smartlink.server.image.momain;

import com.anwen.mongo.annotation.ID;
import com.anwen.mongo.annotation.collection.CollectionName;
import com.anwen.mongo.model.BaseModelID;
import lombok.Data;

@Data
@CollectionName("dataImage")
public class DataImage {

    /**
     * 文件ID
     */
    @ID
    private String fileId;

    /**
     * 源文件地址
     */
    private String sourceFileUrl;

    /**
     * 预览地址
     */
    private String previewUrl;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 对象存储主键
     */
    private Long ossId;
}
