package org.smartlink.common.tenant.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.mybatis.core.domain.BaseEntity;


/**
 * 租户基类
 *
 * @author Michelle.Chung
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantEntity<T> extends BaseEntity {

    /**
     * 租户编号
     */
    private String tenantId;


}
