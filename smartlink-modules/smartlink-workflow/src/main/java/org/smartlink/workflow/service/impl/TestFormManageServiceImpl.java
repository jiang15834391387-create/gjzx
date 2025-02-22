package org.smartlink.workflow.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import kotlin.collections.LongIterator;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.service.ConfigService;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.system.domain.SysDictData;
import org.smartlink.system.domain.vo.SysDictDataVo;
import org.smartlink.system.mapper.SysDictDataMapper;
import org.smartlink.workflow.domain.TestFormConfig;
import org.smartlink.workflow.domain.vo.HtmlVo;
import org.smartlink.workflow.domain.vo.TestFormConfigVo;
import org.smartlink.workflow.mapper.TestFormConfigMapper;
import org.smartlink.workflow.utils.QueryUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.smartlink.workflow.domain.bo.TestFormManageBo;
import org.smartlink.workflow.domain.vo.TestFormManageVo;
import org.smartlink.workflow.domain.TestFormManage;
import org.smartlink.workflow.mapper.TestFormManageMapper;
import org.smartlink.workflow.service.ITestFormManageService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 单管理Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-11
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TestFormManageServiceImpl implements ITestFormManageService {

    private final TestFormManageMapper baseMapper;
    private final SysDictDataMapper dictDataMapper;
    private final TestFormConfigMapper formConfigMapper;
    private final ConfigService configService;

    /**
     * 查询单管理
     *
     * @param id 主键
     * @return 单管理
     */
    @Override
    public TestFormManageVo queryById(Long id){
        TestFormManageVo testFormManageVo = baseMapper.selectVoById(id);
        if(testFormManageVo!=null){
                List<TestFormConfig> list = formConfigMapper.selectList(new QueryWrapper<TestFormConfig>().eq("form_id", id));
                if(list!=null&&list.size()>0){
                    testFormManageVo.setFormConfigList(list);
                }
        }
        return testFormManageVo;
    }

    /**
     * 分页查询单管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 单管理分页列表
     */
    @Override
    public TableDataInfo<TestFormManageVo> queryPageList(TestFormManageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TestFormManage> lqw = buildQueryWrapper(bo);

        Page<TestFormManageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的单管理列表
     *
     * @param bo 查询条件
     * @return 单管理列表
     */
    @Override
    public List<TestFormManageVo> queryList(TestFormManageBo bo) {
        LambdaQueryWrapper<TestFormManage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<TestFormManage> buildQueryWrapper(TestFormManageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<TestFormManage> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getFormName()), TestFormManage::getFormName, bo.getFormName());
        lqw.like(StringUtils.isNotBlank(bo.getFormBindName()), TestFormManage::getFormBindName, bo.getFormBindName());
        lqw.eq(StringUtils.isNotBlank(bo.getFormBindType()), TestFormManage::getFormBindType, bo.getFormBindType());
        lqw.eq(TestFormManage::getIsDeleted, 0);
        return lqw;
    }

    /**
     * 新增单管理
     *
     * @param bo 单管理
     * @return 是否新增成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> insertByBo(TestFormManageBo bo) {
        try {
        TestFormManage add = MapstructUtils.convert(bo, TestFormManage.class);
        boolean flag = false;
            String s = validEntityBeforeSave(add);
            if(s!=null){
                return R.fail(s);
            }
            add.setCreateDept(LoginHelper.getDeptId());
            add.setTenantId(LoginHelper.getTenantId());
            flag = baseMapper.insert(add) > 0;
            if (flag) {
                bo.setId(add.getId());
            }
            synConfigList(bo);
        } catch (Exception e) {
            log.info("添加失败{}",e);
            throw new RuntimeException("失败");
        }
        return R.ok();
    }

    private void synConfigList(TestFormManageBo bo) {
        List<TestFormConfig> formConfigList = bo.getFormConfigList();
        if(formConfigList!=null && formConfigList.size()>0){
            formConfigList.forEach(formConfig->{formConfig.setFormId(bo.getId());formConfig.setTenantId(LoginHelper.getTenantId());});
            formConfigMapper.delete(new QueryWrapper<TestFormConfig>().eq("form_id", bo.getId()));
            formConfigMapper.insertBatch(formConfigList);
        }
    }

    /**
     * 修改单管理
     *
     * @param bo 单管理
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> updateByBo(TestFormManageBo bo) {
        try {
            TestFormManage update = MapstructUtils.convert(bo, TestFormManage.class);
            String s = validEntityBeforeSave(update);
            if(s!=null){
                return R.fail(s);
            }
            baseMapper.updateById(update);
            synConfigList(bo);
        } catch (Exception e) {
            log.info("修改失败{}",e);
            throw new RuntimeException("失败");
        }
        return R.ok();
    }

    /**
     * 保存前的数据校验
     */
    private String validEntityBeforeSave(TestFormManage entity){
        String bindType = entity.getFormBindType();
        if(!StringUtils.isEmpty(bindType)){
            List<SysDictData> sysDictDataVos = dictDataMapper.selectList(new QueryWrapper<SysDictData>().eq("dict_value",bindType));
            if(sysDictDataVos==null || sysDictDataVos.size()==0){
                return "绑定类型不存在";
            }
        }
        return null;
    }

    /**
     * 校验并批量删除单管理信息
     *
     * @param ids     待删除的主键集合
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> deleteWithValidByIds(Collection<Long> ids) {

        try {
            baseMapper.deletedFromManage(ids,LoginHelper.getUserId(),LoginHelper.getTenantId());
            formConfigMapper.deletedFromConfig(ids);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return R.ok();
    }

    @Override
    public List<TestFormManageVo> selectBy(String type) {
        List<TestFormManage> formPurposeType = baseMapper.selectList(new QueryWrapper<TestFormManage>().eq("form_bind_type", type));
        if(formPurposeType!=null&&formPurposeType.size()>0){
            List<TestFormManageVo> objects = new ArrayList<>(formPurposeType.size());
            formPurposeType.forEach(formManage->{
                TestFormManageVo testFormManageVo = new TestFormManageVo();
                BeanUtils.copyProperties(formManage,testFormManageVo);
                List<TestFormConfig> list = formConfigMapper.selectList(new QueryWrapper<TestFormConfig>().eq("form_id", formManage.getId()));
                if(list!=null&&list.size()>0){
                    /*StringBuilder stringBuilder = QueryUtils.parseValue(list);
                    if(stringBuilder!=null){
                        HtmlVo htmlVo = new HtmlVo();
                        htmlVo.setFormManage(formManage);
                        htmlVo.setHtmlContent(stringBuilder.toString());
                        objects.add(htmlVo);
                    }*/
                    testFormManageVo.setFormConfigList(list);
                }
                objects.add(testFormManageVo);
            });

            return objects;
        }
        return null;
    }
}
