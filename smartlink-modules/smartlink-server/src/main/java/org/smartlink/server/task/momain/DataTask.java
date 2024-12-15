package org.smartlink.server.task.momain;


import com.anwen.mongo.annotation.collection.CollectionName;
import com.anwen.mongo.model.BaseModelID;
import lombok.Data;
import org.smartlink.server.image.momain.DataImage;

import java.util.List;

/**
 * @author zs
 * @title 单据信息 元数据实体
 * @description
 * @date 2024-06
 */
@Data
@CollectionName("dataTask")
public class DataTask extends BaseModelID {

    /**
     * 流水号
     */
    private String businessSerialNo;

    /**
     * 单据号
     */
    private String billNum;

    /**
     * 单据类型名称
     */
    private String billTypeName;

    /**
     * 单据类型编号
     */
    private String billType;

    /**
     * 父单据类型编号
     */
    private String pkBillType;
    /**
     * 父单据类型编号
     */
    private String tradeTypeName;

    /**
     * 金额
     */
    private String cash;

    /**
     * 制单人id
     */
    private String userId;

    /**
     * 渠道系统代码
     */
    private String systemCode;

    /**
     * 机构号
     */
    private String orgCode;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构名称
     */
    private String ocrType;

    /**
     * 制单人名称
     */
    private String userName;

    /**
     * 制单日期
     */
    private String billDate;

    /**
     * 扫描方式1单扫2批扫
     */
    private String scanType;

    /**
     * 扫描方式中文
     */
    private String scanTypeName;

    /**
     * 粘贴单张数
     */
    private String pasteListCount;

    /**
     * 附件张数
     */
    private String accessorCount;

    /**
     * 所属集团id
     */
    private String groupId;

    /**
     * 制单人编号如
     */
    private String userNum;

    /**
     * 最后操作人
     */
    private String lastOperater;

    /**
     * 最后操作时间
     */
    private String lastOperateTime;

    /**
     * 驳回原因
     */
    private String operateSuggest;

    /**
     * 单据状态0待登记、1待扫描、2扫描完成、3驳回修改、4驳回重扫、5修改完成、6补扫完成、7未装册、8已装册
     */
    private String taskState;

    /**
     * 单据状态中文名
     */
    private String taskStateName;


    private List<DataImage> images;
}
