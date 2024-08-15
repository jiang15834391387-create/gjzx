package org.smartlink.server.image.domain.bo;

import lombok.Data;

/**
 * @author zhangshuai
 * @title
 * @description
 * @date 2024/8/15
 */
@Data
public class UpdateImageBo {

    private String businessSerialNo;

    private String fileId;

    private String parentId;
}
