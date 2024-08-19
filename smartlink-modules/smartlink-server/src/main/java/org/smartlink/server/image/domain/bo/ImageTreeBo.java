package org.smartlink.server.image.domain.bo;

import lombok.Data;
import org.smartlink.server.image.momain.DataImage;

/**
 * @author zhangshuai
 * @title
 * @description 返回的文件树
 * @date 2024/8/19
 */
@Data
public class ImageTreeBo extends DataImage {


    //节点下数量
    private Long total;

    //节点类型
    private String type;



}
