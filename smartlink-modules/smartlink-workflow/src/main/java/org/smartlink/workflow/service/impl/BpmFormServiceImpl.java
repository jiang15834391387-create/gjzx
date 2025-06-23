package org.smartlink.workflow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONString;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jodd.bean.BeanUtil;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.workflow.domain.BpmFormDO;
import org.smartlink.workflow.domain.TestExpenseReimbursement;
import org.smartlink.workflow.domain.TestFormManage;
import org.smartlink.workflow.domain.vo.TestExpenseReimbursementVo;
import org.smartlink.workflow.domain.vo.form.BpmFormVo;
import org.smartlink.workflow.mapper.BpmFormMapper;
import org.smartlink.workflow.mapper.TestExpenseReimbursementMapper;
import org.smartlink.workflow.mapper.TestFormManageMapper;
import org.smartlink.workflow.service.BpmFormService;
import org.smartlink.workflow.service.ITestExpenseReimbursementService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.*;

/**
 * 动态表单 Service 实现类
 *
 * @author 风里雾里
 */
@Service
@Validated
public class BpmFormServiceImpl implements BpmFormService {

    @Resource
    private BpmFormMapper formMapper;
    @Resource
    private TestFormManageMapper formManageMapper;
    @Resource
    private TestExpenseReimbursementMapper expenseReimbursementMapper;
    @Resource
    private ITestExpenseReimbursementService iTestExpenseReimbursementService;

    @Override
    public Long createForm(BpmFormVo createReqVO) {
        // 插入
        BpmFormDO bpmFormDO = new BpmFormDO();
        BeanUtils.copyProperties(createReqVO, bpmFormDO);
        List<String> fields = createReqVO.getFields();
        bpmFormDO.setFields(JsonUtils.toJsonString(fields));
        formMapper.insert(bpmFormDO);
        // 返回
        return createReqVO.getId();
    }

    @Override
    public void updateForm(BpmFormVo updateReqVO) {
        // 校验存在
        BpmFormDO bpmFormDO = new BpmFormDO();
        BeanUtils.copyProperties(updateReqVO, bpmFormDO);
        List<String> fields = updateReqVO.getFields();
        bpmFormDO.setFields(JsonUtils.toJsonString(fields));
        formMapper.updateById(bpmFormDO);
    }

    @Override
    public void deleteForm(Long id) {
        // 校验存在
        BpmFormDO bpmFormDO = this.validateFormExists(id);

        List<TestExpenseReimbursement> fromId = expenseReimbursementMapper.selectList(
            new QueryWrapper<TestExpenseReimbursement>()
                .eq("from_id", id));
        if (fromId != null && fromId.size() > 0) {
            throw new ServiceException("该表单已被使用无法删除");
        }


        // 删除
        // formMapper.deleteById(id);
        bpmFormDO.setIsDeleted(1);
        formMapper.updateById(bpmFormDO);

    }

    private BpmFormDO validateFormExists(Long id) {
        BpmFormDO bpmFormDO = formMapper.selectById(id);
        if (bpmFormDO == null) {
            throw new RuntimeException("动态表单不存在");
        }
        return bpmFormDO;
    }

    @Override
    public BpmFormVo getForm(Long id) {
        BpmFormDO bpmFormDO = formMapper.selectById(id);
        if (bpmFormDO != null) {
            BpmFormVo vo = new BpmFormVo();
            if (!StringUtils.isEmpty(bpmFormDO.getFields())) {
                List<String> fields = JsonUtils.parseArray(bpmFormDO.getFields(), String.class);
                vo.setFields(fields);
            }
            BeanUtils.copyProperties(bpmFormDO, vo);
            return vo;
        }
        return null;
    }

    @Override
    public Map<String, String> getExpenseAccount(Long id) {
        Map<String, String> result = new LinkedHashMap<>();

        TestExpenseReimbursementVo testExpenseReimbursementVo = iTestExpenseReimbursementService.queryById(id);
        if (ObjectUtil.isNotNull(testExpenseReimbursementVo) && ObjectUtil.isNotNull(testExpenseReimbursementVo.getFromId())) {
            Long fromId = testExpenseReimbursementVo.getFromId();
            BpmFormVo form = this.getForm(fromId);

            if (ObjectUtil.isNotNull(form) && CollUtil.isNotEmpty(form.getFields())) {
                // 获取表单字段定义
                List<String> fields = form.getFields();

                // 解析JSON
                String consumptionDetailsStr = testExpenseReimbursementVo.getConsumptionDetails();
                JSONObject consumptionDetails = JSONUtil.parseObj(consumptionDetailsStr);

                for (String fieldJson : fields) {
                    JSONObject fieldObj = JSONUtil.parseObj(fieldJson);
                    String fieldKey = fieldObj.getStr("field");
                    String fieldTitle = fieldObj.getStr("title");

                    // 统一转换为字符串
                    Object value = consumptionDetails.get(fieldKey);
                    String stringValue = "";

                    if (value instanceof JSONArray) {
                        stringValue = ((JSONArray) value).join(","); // 多个用逗号连接
                    } else if (ObjectUtil.isNotNull(value)) {
                        stringValue = value.toString();
                    }

                    result.put(fieldTitle, stringValue);
                }
            }
        }

        return result;
    }


    @Override
    public List<BpmFormVo> getFormList() {
        QueryWrapper<BpmFormDO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.orderByDesc("create_time");
        objectQueryWrapper.eq("is_deleted", 0);
        List<BpmFormDO> bpmFormDOS = formMapper.selectList(objectQueryWrapper);
        List<BpmFormVo> bpmFormVos = new ArrayList<>();
        if (bpmFormDOS != null && bpmFormDOS.size() > 0) {
            bpmFormDOS.forEach(bpmFormDO -> {
                if (!StringUtils.isEmpty(bpmFormDO.getFields())) {
                    BpmFormVo vo = new BpmFormVo();
                    if (!StringUtils.isEmpty(bpmFormDO.getFields())) {
                        List<String> fields = JsonUtils.parseArray(bpmFormDO.getFields(), String.class);
                        vo.setFields(fields);
                    }
                    BeanUtils.copyProperties(bpmFormDO, vo);
                    bpmFormVos.add(vo);
                }
            });
        }

        return bpmFormVos;
    }

    @Override
    public Page<BpmFormDO> getFormPage(BpmFormDO pageReqVO, PageQuery pageQuery) {
        Page<BpmFormDO> pageParam = new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize());
        QueryWrapper<BpmFormDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (!StringUtils.isEmpty(pageReqVO.getName())) {
            queryWrapper.like("name", pageReqVO.getName());

        }
        queryWrapper.eq("is_deleted", 0);
        if (!StringUtils.isEmpty(pageReqVO.getIsDeleted())) {
            queryWrapper.eq("is_deleted", pageReqVO.getIsDeleted());
        }
        formMapper.selectPage(pageParam, queryWrapper);

        return pageParam;


    }


}
