package org.smartlink.server.nc.domain.imagefilesinfo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;


/**
 * 图片树节点对象 data_image_tree
 *
 * @author L
 * @date
 */
@Data
@TableName("data_image_tree")
public class DataImageTree extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 子节点ID
     */
    private String productId;
    /**
     * 父菜单ID
     */
    private String parentId;
    /**
     * 子节点名称
     */
    private String productName;
    /**
     * 节点层级
     */
    private Long productLevel;
    /**
     * 节点type
     */
    private String productType;
    /**
     * 显示顺序
     */
    private Long orderNum;
    /**
     * imageid
     */
    private String imageId;
    /**
     * batchid
     */
    private String batchId;
    /**
     * 0=正常,1=停用
     */
    private String status;
    /**
     * 租户id
     */
    @FieldName(value="租户id")
    private String tenantId;
    /**
     * 匹配文件规则
     */
    private String matchingFileRules;
    /**
     * 匹配单据规则
     */
    private String matchingBillRules;

    /**
     * 0=不必须,1=必须上传
     */
    private String mustUpload;
    /**
     * 0=不能删除,1=可以删除
     */
    private String deleteOrNot;

    /**
     * 空节点是否显示 0=不,1=显示
     */
    private String blankShow;
    /**
     * 批扫业务标识
     */
    private String batchBusinessQuote;
    /**
     * 是否允许拖动 1允许
     */
    private String isMove;

}
