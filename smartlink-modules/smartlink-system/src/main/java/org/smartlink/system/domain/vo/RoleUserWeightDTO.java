package org.smartlink.system.domain.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleUserWeightDTO {

    @NotNull
    private Long roleId;

    @NotNull
    private Long userId;

    private Integer weight;
}
