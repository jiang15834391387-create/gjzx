package org.smartlink.system.domain.vo;

import lombok.Data;

@Data
public class RoleUserWeightVO {

    private Long roleId;
    private String roleName;
    private Long userId;
    private String userName;
    private String nickName;
    private Long deptId;
    private String deptName;
    private Integer weight;
}
