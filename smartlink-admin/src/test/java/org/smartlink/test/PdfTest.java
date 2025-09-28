package org.smartlink.test;

import org.junit.jupiter.api.Test;
import org.smartlink.system.domain.vo.SysUserVo;
import org.smartlink.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PdfTest {

    @Autowired
    ISysUserService sysUserService;

    @Test
    public void testAssertThrows() {
        List<SysUserVo> sysUserVos = sysUserService.selectUserListByRole();
        sysUserService.addRole(sysUserVos);
    }

}
