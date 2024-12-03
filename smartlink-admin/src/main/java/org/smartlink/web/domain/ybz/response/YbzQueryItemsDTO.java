package org.smartlink.web.domain.ybz.response;

import lombok.Data;

import java.util.List;

/**
 * <p>Title: QueryItems</p>
 * <p>
 * <p>Description:查看影像列表接口返回Items</p>
 *
 * @author L
 * @date
 **/
@Data
public class YbzQueryItemsDTO {
    /**
     * 业务流水号
     */
    private String barcode;
    /**
     * 图片信息
     */
    private List<YbzQueryFileInfoDTO> images;
}
