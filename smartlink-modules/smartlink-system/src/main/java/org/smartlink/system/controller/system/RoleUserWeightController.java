package org.smartlink.system.controller.system;

import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.system.domain.bo.ParticipantQueryBo;
import org.smartlink.system.domain.vo.RoleUserWeightDTO;
import org.smartlink.system.domain.vo.RoleUserWeightVO;
import org.smartlink.system.domain.vo.SysUserVo;
import org.smartlink.system.service.RoleUserWeightService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role/weight")
@RequiredArgsConstructor
public class RoleUserWeightController {

    private final RoleUserWeightService roleUserWeightService;

    /**
     * 角色 → 用户权重列表
     */
    @GetMapping("/list")
    public R<List<RoleUserWeightVO>> list(@RequestParam Long roleId) {
        return R.ok(roleUserWeightService.listRoleUserWeight(roleId));
    }

    /**
     * 保存权重
     */
    @PostMapping("/save")
    public R<Void> save(@RequestBody List<RoleUserWeightDTO> list) {
        roleUserWeightService.saveRoleUserWeight(list);
        return R.ok();
    }

    /**
     * 查询当前节点（taskId）所有有权办理的用户
     */
    @GetMapping("/participants")
    public R<List<SysUserVo>> listParticipants(ParticipantQueryBo bo) {
        return R.ok(roleUserWeightService.listParticipants(bo));
    }
}
