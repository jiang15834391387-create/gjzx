package org.smartlink.common.check.invoice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;


/**
 * 增值税发票明细对象 data_ocr_details
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_ocr_details")
public class DataOcrDetails extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 主表id
     */
    private String ocrId;

    /**
     * 图片表id
     */
    private String fileId;

    /**
     * 金额
     */
    private String detailAmount;

    /**
     * 数量
     */
    private String detailsCount;

    /**
     * 明细编号
     */
    private String detailNo;

    /**
     * 明细名称
     */
    private String name;

    /**
     * 商品编码
     */
    private String commodityCode;

    /**
     * 货物或应税劳务名称
     */
    private String commodityName;

    /**
     * 单价
     */
    private String price;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 规格型号
     */
    private String standard;

    /**
     * 税额
     */
    private String tax;

    /**
     * 单位
     */
    private String unit;

    /**
     * 通行日起止
     */
    private String currentDateEnd;

    /**
     * 通行日起
     */
    private String currentDateStart;

    /**
     * 车牌号
     */
    private String licensePlateNum;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 用车时间
     */
    private String usageTime;

    /**
     * 特殊政策标识（0-正常票 1-免税 2-不征税  3-零税率）
     */
    private String specialMark;

    /**
     * 建筑服务发生地（ service_type为建筑服务，返回此字段）
     */
    private String placeOfBuildingService;

    /**
     * 建筑项目名称（ service_type为建筑服务，返回此字段）
     */
    private String buildingName;

    /**
     * 产权证书/不动产权证号（service_type为不动产经营租赁服务，返回字段）
     */
    private String titleCertificateNumber;

    /**
     * 面积单位（service_type为不动产经营租赁服务，返回字段）
     */
    private String areaUnit;

    /**
     * 运输工具类型（service_type为货物运输服务，返回此字段）
     */
    private String transportType;

    /**
     * 运输工具牌号（service_type为货物运输服务，返回此字段）
     */
    private String transportNumber;

    /**
     * 起始地（service_type为货物运输服务，返回此字段）
     */
    private String from;

    /**
     * 到达地（service_type为货物运输服务，返回此字段）
     */
    private String to;

    /**
     * 运输货物名称（service_type为货物运输服务，返回此字段）
     */
    private String goodsName;

    /**
     * 出行人（service_type为旅客运输服务，返回此字段）
     */
    private String passenger;

    /**
     * 有效身份证号（service_type为旅客运输服务，返回此字段）
     */
    private String userId;

    /**
     * 出行日期（service_type为旅客运输服务，返回此字段）
     */
    private String travelDate;

    /**
     * 等级（service_type为旅客运输服务，返回此字段）
     */
    private String seat;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本号
     */
    @Version
    private Long version;
}
