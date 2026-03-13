package org.smartlink.common.ocr.xml;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuqi.accds.sign.utils.XmlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.ocr.abstractd.AbstractOcrStrategy;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.core.IdentificationFactory;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.properties.OcrProperties;
import org.smartlink.common.ocr.xml.config.XmlOcrProperties;
import org.smartlink.common.ocr.xml.conversion.XmlConversionStrategyFactory;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * XML发票识别策略类
 *
 * @author lqm
 * @date 2025-01-08
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class XmlStrategy extends AbstractOcrStrategy implements IdentificationFactory<JsonNode> {

    private static XmlOcrProperties xmlOcrProperties = new XmlOcrProperties();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(OcrProperties properties) {
        super.init(properties);
        // 初始化配置
        try {
            xmlOcrProperties = properties.getXmlDetailInfo();
            log.info("XML发票OCR初始化完成！初始化参数：{}", properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        isInit = true;
    }

    @Override
    public List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, String base64, String fileSuffix) throws OcrException {
        log.info("进入XML发票OCR识别");
        try {
            // 1. 解码base64获取XML字符串
            String xmlString = new String(Base64.decode(base64), StandardCharsets.UTF_8);
            log.info("Base64解码成功，XML长度: {}", xmlString.length());
            log.debug("XML内容: {}", xmlString);

            // 2. 使用XmlUtils.XmlToJson将XML转换为JSON (accds测试方法集成)
            org.json.JSONObject json = XmlUtils.XmlToJson(xmlString);
            log.info("XML解析为JSON成功");

            // 3. 将org.json.JSONObject转换为hutool的JSONObject
            JSONObject jsonObject = JSONUtil.parseObj(json.toString());

            // 获取xbrl节点数据
            JSONObject xbrlData = jsonObject.getJSONObject("xbrli:xbrl");
            if (xbrlData == null) {
                throw new OcrException("XML数据格式错误，缺少xbrli:xbrl节点");
            }

            // 根据XML内容自动获取转换策略
            ChangeIdentifyInfo<JSONObject> conversionStrategy = XmlConversionStrategyFactory.getConversionStrategyByContent(jsonObject);

            // 调用转换策略进行转换
            return conversionStrategy.changeInfo(dataImageFilesInfo, jsonObject);
        } catch (Exception e) {
            log.error("XML发票识别失败: {}", e.getMessage(), e);
            throw new OcrException("XML发票识别失败: " + e.getMessage());
        }
    }

    @Override
    public ChangeIdentifyInfo conversionInfo(DataImageFilesInfo dataImageFilesInfo, List identifyResults) throws OcrException {
        // XML策略不使用此方法，因为它直接处理JSON数据
        return null;
    }

    /**
     * 从JSON对象中获取指定key的content值
     */
    private String getContent(JSONObject data, String key) {
        if (data == null || !data.containsKey(key)) {
            return null;
        }

        Object value = data.get(key);
        if (value instanceof JSONObject) {
            return ((JSONObject) value).getStr("content");
        }

        return value != null ? value.toString() : null;
    }
}
