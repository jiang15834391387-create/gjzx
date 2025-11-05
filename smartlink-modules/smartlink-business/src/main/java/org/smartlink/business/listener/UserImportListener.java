package org.smartlink.business.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.core.utils.SpringUtils;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.entity.domain.business.mapper.DataImageFilesInfoMapper;
import org.smartlink.common.entity.domain.business.mapper.DataOcrDetailsMapper;
import org.smartlink.common.entity.domain.business.mapper.DataOcrInfoMapper;
import org.smartlink.common.excel.core.ExcelListener;
import org.smartlink.common.excel.core.ExcelResult;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.system.domain.SysDept;
import org.smartlink.system.domain.SysRole;
import org.smartlink.system.domain.SysUser;
import org.smartlink.system.domain.SysUserRole;
import org.smartlink.system.domain.vo.SysUserImportVo;
import org.smartlink.system.mapper.SysDeptMapper;
import org.smartlink.system.mapper.SysRoleMapper;
import org.smartlink.system.mapper.SysUserMapper;
import org.smartlink.system.mapper.SysUserRoleMapper;
import org.smartlink.system.service.ISysConfigService;
import org.smartlink.system.service.ISysUserService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户自定义导入
 *
 * @author Lion Li
 */
@Slf4j
public class UserImportListener extends AnalysisEventListener<SysUserImportVo> implements ExcelListener<SysUserImportVo> {
    private final ISysUserService userService;

    private final Boolean isUpdateSupport;

    private final String password;

    private final SysUserRoleMapper sysUserRoleMapper;

    private final SysRoleMapper sysRoleMapper;

    private final SysDeptMapper sysDeptMapper;

    private final SysUserMapper sysUserMapper;

    private final DataImageFilesInfoMapper imageFilesInfoMapper;

    private final DataOcrInfoMapper dataOcrInfoMapper;

    private final DataOcrDetailsMapper detailsMapper;

    private int successNum = 0;
    private int failureNum = 0;
    private final StringBuilder successMsg = new StringBuilder();
    private final StringBuilder failureMsg = new StringBuilder();

    public UserImportListener(Boolean isUpdateSupport) {
        this.imageFilesInfoMapper =  SpringUtils.getBean(DataImageFilesInfoMapper.class);
        this.dataOcrInfoMapper =SpringUtils.getBean(DataOcrInfoMapper.class);
        this.sysUserRoleMapper = SpringUtils.getBean(SysUserRoleMapper.class);
        this.sysRoleMapper = SpringUtils.getBean(SysRoleMapper.class);
        this.sysDeptMapper = SpringUtils.getBean(SysDeptMapper.class);
        this.sysUserMapper = SpringUtils.getBean(SysUserMapper.class);
        this.detailsMapper = SpringUtils.getBean(DataOcrDetailsMapper.class);
        String initPassword = SpringUtils.getBean(ISysConfigService.class).selectConfigByKey("sys.user.initPassword");
        this.userService = SpringUtils.getBean(ISysUserService.class);
        this.password = BCrypt.hashpw(initPassword);
        this.isUpdateSupport = isUpdateSupport;
    }

    @Override
    public void invoke(SysUserImportVo userVo, AnalysisContext context) {
        SysDept dept = null;
        if (StrUtil.isNotBlank(userVo.getClassName())) {
            // 查询班级对应的部门
            dept = sysDeptMapper.selectOne(new LambdaQueryWrapper<SysDept>().eq(SysDept::getDeptName, userVo.getClassName()));
            // 不存在则创建新部门
            if (dept == null) {
                dept = new SysDept();
                dept.setDeptName(userVo.getClassName());
                dept.setParentId(1L);
                dept.setStatus("0");
                sysDeptMapper.insert(dept);
            }
        } else {
            failureNum++;
            failureMsg.append("第" + context.readRowHolder().getRowIndex() + "行：班级名称不能为空<br/>");
            return;
        }

        // 角色关联
        Long roleId = null;
        if (StrUtil.isNotBlank(userVo.getRoleName())) {
            // 查询单个角色
            SysRole role = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleName, userVo.getRoleName()));
            if (role == null) {
                failureNum++;
                failureMsg.append("第" + context.readRowHolder().getRowIndex() + "行：角色【" + userVo.getRoleName() + "】不存在<br/>");
                return;
            }
            roleId = role.getRoleId();
        } else {
            failureNum++;
            failureMsg.append("第" + context.readRowHolder().getRowIndex() + "行：角色不能为空<br/>");
            return;
        }

        // 用户新增
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhonenumber, userVo.getPhonenumber()));
        try {
            if (ObjectUtil.isNull(sysUser)) {
                // 新增用户
                SysUser user = new SysUser();
                user.setUserName(userVo.getUserName());
                user.setNickName(userVo.getNickName());
                user.setEmail(userVo.getEmail());
                user.setPhonenumber(userVo.getPhonenumber());
                user.setSex(userVo.getSex());
                user.setStatus(userVo.getStatus());
                user.setDeptId(dept.getDeptId());
                user.setPassword(password); // 初始密码加密
                user.setCreateBy(LoginHelper.getUserId());
                user.setCreateTime(new Date());
                sysUserMapper.insert(user);

                // 关联单个角色
                if (roleId != null) {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(user.getUserId());
                    userRole.setRoleId(roleId);
                    sysUserRoleMapper.insert(userRole);
                }
                //分配管理员导入的发票数据（先图片表，后结构化表）
                assignAdminInvoiceData(user);
                successNum++;
                successMsg.append("第" + context.readRowHolder().getRowIndex() + "行：用户【" + userVo.getNickName() + "】导入成功<br/>");
            } else {
                successNum++;
                successMsg.append("第" + context.readRowHolder().getRowIndex() + "行：用户【" + userVo.getNickName() + "】已经存在不能重复导入<br/>");
            }

        } catch (Exception e) {
            failureNum++;
            String msg = "第" + context.readRowHolder().getRowIndex() + "行：用户【" + userVo.getNickName() + "】导入失败：";
            if (e instanceof ConstraintViolationException) {
                msg += ((ConstraintViolationException) e).getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            } else {
                msg += e.getMessage();
            }
            failureMsg.append(msg).append("<br/>");
            log.error(msg, e);
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public ExcelResult<SysUserImportVo> getExcelResult() {
        return new ExcelResult<>() {

            @Override
            public String getAnalysis() {
                if (failureNum > 0) {
                    failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                    throw new ServiceException(failureMsg.toString());
                } else {
                    successMsg.insert(0, "恭喜您，数据已导入成功！共 " + successNum + " 条，数据如下：");
                }
                return successMsg.toString();
            }

            @Override
            public List<SysUserImportVo> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }
        };
    }
    // 分配管理员的发票数据
    private void assignAdminInvoiceData(SysUser user) {
        List<DataImageFilesInfo> adminImages = imageFilesInfoMapper.selectList(
            new LambdaQueryWrapper<DataImageFilesInfo>()
                .eq(DataImageFilesInfo::getCreateBy, "1")
        );

        // 批量插入图片表
        List<DataImageFilesInfo> userImageBatch = new ArrayList<>();
        List<String> adminFileIds = new ArrayList<>();
        for (DataImageFilesInfo adminImage : adminImages) {
            DataImageFilesInfo userImage = new DataImageFilesInfo();
            BeanUtil.copyProperties(adminImage, userImage);
            BeanUtil.toBean(adminImage, DataImageFilesInfo.class);
            userImage.setFileId(IdUtil.simpleUUID());
            userImage.setCreateBy(user.getUserId());
            userImage.setCreateTime(new Date());
            userImageBatch.add(userImage);
            adminFileIds.add(adminImage.getFileId());
        }
        imageFilesInfoMapper.insertBatch(userImageBatch);

        // 批量查询所有管理员的结构化数据
        List<DataOcrInfo> allAdminOcrList = dataOcrInfoMapper.selectList(
            new LambdaQueryWrapper<DataOcrInfo>()
                .eq(DataOcrInfo::getCreateBy, "1")
        );
        // 批量查询所有管理员的结构化详情数据
        List<DataOcrDetails> dataOcrDetails = detailsMapper.selectList(
            new LambdaQueryWrapper<DataOcrDetails>()
                .eq(DataOcrDetails::getCreateBy, "1")
        );

        //file_id到结构化数据的映射
        Map<String, List<DataOcrInfo>> ocrMap = allAdminOcrList.stream()
            .collect(Collectors.groupingBy(DataOcrInfo::getFileId));

        // 5. 遍历图片，内存中匹配结构化数据
        List<DataOcrInfo> userOcrBatch = new ArrayList<>();
        for (int i = 0; i < adminImages.size(); i++) {
            DataImageFilesInfo adminImage = adminImages.get(i);
            DataImageFilesInfo userImage = userImageBatch.get(i);

            //获取对应的结构化数据
            List<DataOcrInfo> adminOcrList = ocrMap.getOrDefault(adminImage.getFileId(), Collections.emptyList());

            for (DataOcrInfo adminOcr : adminOcrList) {
                DataOcrInfo userOcr = new DataOcrInfo();
                BeanUtil.copyProperties(adminOcr, userOcr);
                userOcr.setId(IdUtil.simpleUUID());
                userOcr.setFileId(userImage.getFileId());
                userOcr.setCreateBy(user.getUserId());
                userOcr.setCreateTime(new Date());
                userOcrBatch.add(userOcr);
            }
        }
        dataOcrInfoMapper.insertBatch(userOcrBatch);
        //添加明细
        addInvoiceDetails(dataOcrDetails,adminImages,userImageBatch,user);
    }

    private void addInvoiceDetails(List<DataOcrDetails> dataOcrDetails, List<DataImageFilesInfo> adminImages, List<DataImageFilesInfo> userImageBatch, SysUser user) {
        //file_id到结构化数据的映射
        Map<String, List<DataOcrDetails>> ocrMap = dataOcrDetails.stream()
            .collect(Collectors.groupingBy(DataOcrDetails::getFileId));

        // 遍历图片，内存中匹配结构化数据
        List<DataOcrDetails> userDetailsList = new ArrayList<>();
        for (int i = 0; i < adminImages.size(); i++) {
            DataImageFilesInfo adminImage = adminImages.get(i);
            DataImageFilesInfo userImage = userImageBatch.get(i);

            //获取对应的结构化数据
            List<DataOcrDetails> adminOcrList = ocrMap.getOrDefault(adminImage.getFileId(), Collections.emptyList());

            for (DataOcrDetails details : adminOcrList) {
                DataOcrDetails ocrDetails = new DataOcrDetails();
                BeanUtil.copyProperties(details, ocrDetails);
                ocrDetails.setId(IdUtil.simpleUUID());
                ocrDetails.setFileId(userImage.getFileId());
                ocrDetails.setCreateBy(user.getUserId());
                ocrDetails.setCreateTime(new Date());
                userDetailsList.add(ocrDetails);
            }
        }
        detailsMapper.insertBatch(userDetailsList);
    }
}
