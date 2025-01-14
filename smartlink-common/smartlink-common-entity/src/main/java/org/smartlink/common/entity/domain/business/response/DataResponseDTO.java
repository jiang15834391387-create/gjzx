package org.smartlink.common.entity.domain.business.response;

import lombok.Data;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

@Data
public class DataResponseDTO {
    private DataImageFilesInfo img;
    private Object info;
    private Object detaiInfo;

    // 构造方法
    public DataResponseDTO(DataImageFilesInfo img, Object info, Object detaiInfo) {
        this.img = img;
        this.info = info;
        this.detaiInfo = detaiInfo;
    }

}

