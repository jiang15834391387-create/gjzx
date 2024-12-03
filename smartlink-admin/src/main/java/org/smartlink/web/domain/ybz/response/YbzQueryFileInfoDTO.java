package org.smartlink.web.domain.ybz.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 友报账查询文件信息DTO
 *
 * @author L
 */
@Data
public class YbzQueryFileInfoDTO implements Serializable {
    /**
     * 缩略图地址
     */
    private String smallimage;
    /**
     * 原图地址
     */
    private String pointimage;
    /**
     * 图片名称
     */
    private String name;
    /**
     * 图片唯一值
     */
    private String imgkey;
    /**
     * 图片大小
     */
    private String filesize;

    private String imagewid;

    private String imagehei;

    private String index;

    private YbzQueryOcrInfoDTO info;
}
