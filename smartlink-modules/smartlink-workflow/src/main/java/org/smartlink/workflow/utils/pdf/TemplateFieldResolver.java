package org.smartlink.workflow.utils.pdf;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.workflow.domain.TemplateFieldConfig;
import org.smartlink.workflow.mapper.TemplateFieldConfigMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 86158
 */
@Component
@RequiredArgsConstructor
public class TemplateFieldResolver {

    private final TemplateFieldConfigMapper configMapper;
    private final JdbcUtil jdbcUtil;

    private final Map<QueryKey, Map<String, Object>> queryCache = new HashMap<>();

    public Map<String, Object> resolveTemplateFields(String templateCode, Map<String, Object> templateData) {
        List<TemplateFieldConfig> configList = configMapper.selectList(new QueryWrapper<TemplateFieldConfig>().eq("template_code", templateCode));
        Map<String, Object> result = new HashMap<>(templateData);
        if(CollectionUtils.isEmpty(configList)){
            for (TemplateFieldConfig config : configList) {
                try {
                    QueryKey key = new QueryKey(
                        config.getConfigType(),
                        config.getTableName(),
                        config.getQueryRelation(),
                        config.getJoinConfig()
                    );

                    Map<String, Object> valueMap = queryCache.computeIfAbsent(key, k -> doQuery(config));
                    String extractField = getExtractField(config);
                    Object value = valueMap.getOrDefault(extractField, null);

                    if (StringUtils.hasText(config.getFormatField()) && value != null) {
                        value = invokeFormatMethod(config.getFormatField(), value);
                    }

                    result.put(config.getFieldName(), value);

                } catch (Exception e) {
                    result.put(config.getFieldName(), "ERROR: " + e.getMessage());
                }
            }
        }
        return result;
    }

    private Map<String, Object> doQuery(TemplateFieldConfig config) {
        String configType = config.getConfigType();

        if ("1".equals(configType)) {
            return queryDirect(config);
        } else if ("2".equals(configType)) {
            return querySingleJoin(config);
        } else if ("3".equals(configType)) {
            return queryMultiJoin(config);
        }

        return Collections.emptyMap();
    }

    private Map<String, Object> queryDirect(TemplateFieldConfig config) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(config.getTableName())
                .append(" WHERE ").append(config.getQueryRelation()).append(" LIMIT 1");

        List<Map<String, Object>> resultList = jdbcUtil.queryForList(sql.toString());
        return resultList.isEmpty() ? Collections.emptyMap() : resultList.get(0);
    }

    private Map<String, Object> querySingleJoin(TemplateFieldConfig config) {
        List<JSONObject> joins = JSON.parseArray(config.getJoinConfig(), JSONObject.class);
        if (joins.isEmpty()) {
            return Collections.emptyMap();
        }

        JSONObject join = joins.get(0);
        String sql = "SELECT b.* FROM " + config.getTableName() + " a " +
                "JOIN " + join.getString("joinTableName") + " b ON " + join.getString("joinRelation") +
                " WHERE " + config.getQueryRelation() + " LIMIT 1";

        List<Map<String, Object>> resultList = jdbcUtil.queryForList(sql);
        return resultList.isEmpty() ? Collections.emptyMap() : resultList.get(0);
    }

    private Map<String, Object> queryMultiJoin(TemplateFieldConfig config) {
        List<JSONObject> joins = JSON.parseArray(config.getJoinConfig(), JSONObject.class);
        if (joins.isEmpty()) {
            return Collections.emptyMap();
        }

        StringBuilder sql = new StringBuilder("SELECT ");
        JSONObject last = joins.get(joins.size() - 1);
        sql.append(last.getString("joinTableName")).append(".* FROM ").append(config.getTableName()).append(" ");

        for (JSONObject join : joins) {
            sql.append("JOIN ")
                    .append(join.getString("joinTableName"))
                    .append(" ON ")
                    .append(join.getString("joinRelation"))
                    .append(" ");
        }

        sql.append("WHERE ").append(config.getQueryRelation()).append(" LIMIT 1");

        List<Map<String, Object>> resultList = jdbcUtil.queryForList(sql.toString());
        return resultList.isEmpty() ? Collections.emptyMap() : resultList.get(0);
    }

    private String getExtractField(TemplateFieldConfig config) {
        if ("1".equals(config.getConfigType())) {
            return config.getFieldValue();
        }

        List<JSONObject> joins = JSON.parseArray(config.getJoinConfig(), JSONObject.class);
        return joins.isEmpty() ? null : joins.get(joins.size() - 1).getString("joinFieldName");
    }

    private Object invokeFormatMethod(String methodCall, Object value) throws Exception {
        // 示例：org.smartlink.workflow.utils.pdf.DateUtil.getFormatTime
        int lastDot = methodCall.lastIndexOf(".");
        String className = methodCall.substring(0, lastDot);
        String methodName = methodCall.substring(lastDot + 1);

        Class<?> clazz = Class.forName(className);
        Method method = clazz.getMethod(methodName, Object.class);
        return method.invoke(null, value);
    }
}
