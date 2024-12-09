package org.smartlink.server.nc.ocr.service.bean;


import lombok.Data;
import org.smartlink.server.nc.domain.modle.BaseEntity;

/**
 * <p>Description:没有识别出来的图片类型 </p>
 * @author L
 **/
@Data
public class Others extends BaseEntity {

    /**
     * 文件id
     */
    private String fileId;

    @Override
    public String toString() {
        return "Others{" +
                "fileId='" + fileId + '\'' +
                '}';
    }

}
