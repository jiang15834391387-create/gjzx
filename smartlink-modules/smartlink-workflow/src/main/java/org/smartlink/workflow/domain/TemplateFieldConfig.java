package org.smartlink.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 情况一：
 *      例如fieldName是field_1为模板字段需要使用sys_user表中是username，
 *      这时候configType=1，tableName=sys_user,fieldValue=username,queryRelation=user_id=1111,user_type=普通员工，user_status=正常
 *      查询出来之后可能是多条，取第一条的username
 * 情况二：以此类推为单表关联
 *
 * 情况三：为多表关联
 *
 * @author 86158
 */
@Data
@TableName("test_template_field_config")
public class TemplateFieldConfig {

    private String id;

    /**
     * 对应template_info模板表中的templateCode
     */
    private String templateCode;
    /**
     * 配置类型：
     *  1.直接取值：(取tableName表中的fieldValue字段的值查询条件为queryRelation)
     *  2.单级关联：(通过关联表查询对应字段 (tableName去关联joinTableName表，关联关系为joinRelation ,这时候就取的字段为joinFieldName字段的值)
     *  3 多级关联
     */
    private String configType;
    /**
     * 对应pdf模板中的配置字段例如:field_1,field_2
     */
    private String fieldName;

    /**
     * 主表名
     *
     */
    private String tableName;
    /**
     * 取值时的字段名
     */
    private String fieldValue;
    /**
     * 查询条件
     */
    private String queryRelation;

    /**
     * 示例joinConfig JSON结构：
     * joinTableName:  关联的表名
     * joinFieldName: 字段表示的是 最终要查询的目标字段名
     * joinRelation: 关联关系，例如：user.dept_id = department.id
     *
     * 当类型是多级关联的时候则是查询最后级关联关系的joinFieldName值
     * [
     *   {
     *     "joinTableName": "sys_department",
     *     "joinFieldName": "dept_id",
     *     "joinRelation": "sys_user.dept_id = sys_department.id"
     *   },
     *   {
     *     "joinTableName": "sys_company",
     *     "joinFieldName": "company_name",
     *     "joinRelation": "sys_department.company_id = sys_company.id"
     *   }
     * ]
     */
    private String joinConfig;
    /**
     * 格式化字段：这个存的是类对应的方法名字，
     * 例如:当查询处理的fieldName为时间，字段进行格式化为yyyy-MM-dd HH:mm:ss 的时候，
     * 这个字段为存的为org.smartlink.workflow.utils.DateUtil.getFormatTime(fieldName)
     */
    private String formatField;

    private Date createTime;

}
