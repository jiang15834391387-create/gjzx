package org.smartlink.server.nc.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.smartlink.common.core.xss.Xss;
import org.smartlink.server.nc.domain.modle.BaseEntity;

import java.util.Date;

/**
 * 使用者信息对象 system_tenant
 *
 * @author ruoyi
 * @date 2022-09-06
 */
@Data
@TableName("sys_tenant")
@ApiModel("租户使用者对象")
public class SysTenant extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 租户编号，自增
     */
    @ApiModelProperty(value = "租户编号")
    @TableId(value = "id")
    private String id;

    /**
     * 租户编号，自增
     */
    @ApiModelProperty(value = "租户id")
    private String tenantId;
    /**
     * 租户密钥
     */
    @ApiModelProperty(value = "租户密钥")
    private String appSecret;//
    /**
     * 租户名，唯一
     */
    @ApiModelProperty(value = "租户名")
    @Xss(message = "租户名不能包含脚本字符")
    private String name;//
    /**
     * 联系人的用户编号
     */
    @ApiModelProperty(value = "联系人的用户编号")
    private String contactUserId;//
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactName;
    /**
     * 联系手机
     */
    @ApiModelProperty(value = "联系手机")
    private String contactMobile;
    /**
     * 租户状态
     */
    @ApiModelProperty(value = "租户状态")
    private String status;
    /**
     * 租户套餐编号
     */
    @ApiModelProperty(value = "租户套餐编号")
    private String packageId;
    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    private Date expireTime;
    /**
     * 账号数量
     */
    @ApiModelProperty(value = "账号数量")
    private String accountCount;


}
