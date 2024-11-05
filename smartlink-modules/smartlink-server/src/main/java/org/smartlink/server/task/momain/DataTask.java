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
     * 单据类型编号
     */
    private String billType;

    /**
     * 单据类型名称
     */
    private String billTypeName;


    /**
     * 父单据类型编号
     */
    private String pkBillType;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 单据标题
     */
    private String billTitle;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 机构名称
     */
    private String orgName;


    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 制单日期
     */
    private String billDate;


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
