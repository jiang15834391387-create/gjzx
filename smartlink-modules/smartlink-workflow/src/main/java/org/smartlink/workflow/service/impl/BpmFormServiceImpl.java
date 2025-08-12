package org.smartlink.workflow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONString;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jodd.bean.BeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.workflow.domain.BpmFormDO;
import org.smartlink.workflow.domain.TestExpenseReimbursement;
import org.smartlink.workflow.domain.TestFormManage;
import org.smartlink.workflow.domain.vo.ConsumptionDetailsVo;
import org.smartlink.workflow.domain.vo.DetailsVo;
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
    private static final ObjectMapper objectMapper = new ObjectMapper();
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
    public List<Map<String, String>> getExpenseAccount(Long id, Long fromId) {
        List<Map<String, String>> result = new ArrayList<>();

        TestExpenseReimbursementVo testExpenseReimbursementVo = iTestExpenseReimbursementService.queryById(id);
        if (ObjectUtil.isNotNull(testExpenseReimbursementVo) && ObjectUtil.isNotNull(testExpenseReimbursementVo.getFromId())) {
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
                    String fieldType = fieldObj.getStr("type");

                    // 跳过 type 为 uploader 的字段
                    if ("uploader".equalsIgnoreCase(fieldType)) {
                        continue;
                    }

                    // 统一转换为字符串
                    Object value = consumptionDetails.get(fieldKey);
                    String stringValue = "";

                    if (value instanceof JSONArray) {
                        stringValue = ((JSONArray) value).join(","); // 多个用逗号连接
                    } else if (ObjectUtil.isNotNull(value)) {
                        stringValue = value.toString();
                    }

                    Map<String, String> item = new HashMap<>();
                    item.put("title", fieldTitle);
                    item.put("value", stringValue);

                    result.add(item);
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

    @Override
    public R<String> getConsumptionDetails(ConsumptionDetailsVo vo) {
        Long fromId = vo.getFromId();
        List<DetailsVo> details = vo.getDetails();
        if (fromId==null|| CollectionUtils.isEmpty(details)){
            return R.fail("参数不全");
        }
        BpmFormVo form = this.getForm(fromId);
        if(form==null){
            return R.fail("表单不存在");
        }



        /**
         * 获取样式
         */
        String detailsTemplate = getDetailsTemplate(fromId);

        return null;
    }


    public String getDetailsTemplate(Long fromId) {
        BpmFormVo form = this.getForm(fromId);
        if(form!=null){
            String getfield = getfield(form);
            return getfield;
        }
       return null;
    }

    public String getfield(BpmFormVo form) {
        List<String> fields = form.getFields();
        for (String fieldJson : fields) {
            cn.hutool.json.JSONObject fieldObj = JSONUtil.parseObj(fieldJson);
            String fieldKey = fieldObj.getStr("field");
            String props = fieldObj.getStr("props");
            if(!StringUtils.isEmpty(props)){
                cn.hutool.json.JSONObject prop = JSONUtil.parseObj(props);
                String rule = prop.getStr("rule");
                if(rule != null && !rule.trim().isEmpty()){
                    com.alibaba.fastjson.JSONArray array = JSON.parseArray(rule);
                    if (!array.isEmpty()) {
                        for (int i = 0; i < array.size(); i++){
                            com.alibaba.fastjson.JSONObject first = array.getJSONObject(i);
                            String type = first.getString("type");
                            if(!StringUtils.isEmpty(type)&& "group".equals(type)){
                                String title = first.getString("title");
                                String field = first.getString("field");
                                String props1 = first.getString("props");
                                if(!StringUtils.isEmpty(props)&&"报销明细".equals(title)){
                                    cn.hutool.json.JSONObject prop2 = JSONUtil.parseObj(props1);
                                    String rule2 = prop2.getStr("rule");
                                    if(rule2 != null && !rule2.trim().isEmpty()){
                                        com.alibaba.fastjson.JSONArray array2 = JSON.parseArray(rule2);
                                        List<HashMap<String, String>> itemArr=new ArrayList<>();
                                        if (!array2.isEmpty()) {
                                            for (int ii = 0; ii < array2.size(); ii++){
                                                cn.hutool.json.JSONObject obj = JSONUtil.parseObj(array2.get( ii));
                                                String fieldTitle = obj.getStr("title");
                                                String fieldField = obj.getStr("field");
                                                if (!StringUtils.isEmpty(fieldTitle)&& !StringUtils.isEmpty(fieldField)){
                                                    HashMap<String, String> item=new HashMap<>();
                                                    switch (fieldTitle){
                                                        case "报销金额":
                                                            item.put(fieldField, "报销金额");
                                                            break;
                                                        case "费用描述":
                                                            item.put(fieldField,"费用描述");
                                                            break;
                                                        case "发票上传":
                                                            item.put(fieldField,"发票上传");
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    if(item != null&& item.size()>0){
                                                        itemArr.add(item);
                                                    }
                                                }
                                            }
                                            if (itemArr!= null&& itemArr.size()>0){
                                                com.alibaba.fastjson.JSONObject jsonObject2 = new com.alibaba.fastjson.JSONObject();
                                                jsonObject2.put(field, itemArr);
                                                com.alibaba.fastjson.JSONObject jsonObject3 = new com.alibaba.fastjson.JSONObject();
                                                jsonObject3.put(fieldKey, jsonObject2);
                                                return jsonObject3.toString();
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return null;
    }

}
