package org.smartlink.common.ocr.ofd;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuqi.accds.sign.utils.AnalysisBillUtils;
import com.jiuqi.accds.sign.utils.XmlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.utils.file.OfdUtils;
import org.smartlink.common.ocr.abstractd.AbstractOcrStrategy;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.core.IdentificationFactory;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.ofd.conversion.OfdConversionStrategyFactory;
import org.smartlink.common.ocr.ofd.config.OfdOcrProperties;
import org.smartlink.common.ocr.properties.OcrProperties;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * OFD发票识别策略类
 *
 * @author lqm
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OfdStrategy extends AbstractOcrStrategy implements IdentificationFactory<JsonNode> {

    private static OfdOcrProperties ofdOcrProperties = new OfdOcrProperties();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(OcrProperties properties) {
        super.init(properties);
        // 初始化配置
        try {
            ofdOcrProperties = properties.getOfdDetailInfo();
            log.info("OFD发票OCR初始化完成！初始化参数：{}", properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        isInit = true;
    }

    @Override
    public List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, String base64, String fileSuffix) throws OcrException {
        log.info("进入OFD发票OCR识别");
        try {
            // 1. 解码base64获取OFD文件字节数组
            byte[] ofdBytes = Base64.decode(base64);
            log.info("Base64解码成功，OFD文件大小: {}", ofdBytes.length);
            
            // 2. 从OFD文件中提取XML内容，优先使用久其的方法
            String xmlString = extractXmlFromOfd(ofdBytes);
            log.info("从OFD中提取XML成功，XML长度: {}", xmlString.length());
            log.debug("XML内容: {}", xmlString);

            // 3. 使用XmlUtils.XmlToJson将XML转换为JSON (accds测试方法集成)
            org.json.JSONObject json = XmlUtils.XmlToJson(xmlString);
            if (json == null) {
                throw new OcrException("XML解析为JSON失败：返回null");
            }
            log.info("XML解析为JSON成功");

            // 4. 将org.json.JSONObject转换为hutool的JSONObject
            String jsonStr = json.toString();
            if (jsonStr == null || jsonStr.isEmpty()) {
                throw new OcrException("JSON字符串为空");
            }
            JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
            if (jsonObject == null) {
                throw new OcrException("JSON转换失败：转换后为null");
            }

            // OFD发票的根节点是eInvoice
            JSONObject eInvoiceData = jsonObject.getJSONObject("eInvoice");
            if (eInvoiceData == null) {
                throw new OcrException("OFD数据格式错误，缺少eInvoice节点");
            }

            // 根据OFD内容自动获取转换策略
            ChangeIdentifyInfo<JSONObject> conversionStrategy = OfdConversionStrategyFactory.getConversionStrategyByContent(jsonObject);

            // 调用转换策略进行转换
            return conversionStrategy.changeInfo(dataImageFilesInfo, jsonObject);
        } catch (Exception e) {
            log.error("OFD发票识别失败: {}", e.getMessage(), e);
            throw new OcrException("OFD发票识别失败: " + e.getMessage());
        }
    }
    
    /**
     * 从OFD文件中提取XML内容，优先使用久其的方法，失败后回退到自己的方法
     * @param ofdBytes OFD文件字节数组
     * @return XML字符串
     * @throws Exception 当提取失败时抛出异常
     */
    private String extractXmlFromOfd(byte[] ofdBytes) throws Exception {
        // 优先使用久其的方法
        try {
            // 创建临时文件
            java.io.File tempFile = java.io.File.createTempFile("ofd", ".ofd");
            tempFile.deleteOnExit();
            
            // 写入OFD内容
            java.nio.file.Files.write(tempFile.toPath(), ofdBytes);
            
            // 使用久其的AnalysisBillUtils.getXbrlFromBill方法提取XML
            AnalysisBillUtils analysisBillUtils = new AnalysisBillUtils();
            String xmlString = analysisBillUtils.getXbrlFromBill(tempFile.getAbsolutePath());
            
            if (xmlString != null && !xmlString.isEmpty()) {
                log.info("使用久其的方法成功从OFD中提取XML");
                return xmlString;
            } else {
                log.warn("久其的方法提取XML为空，将使用自己的方法");
            }
        } catch (Exception e) {
            log.warn("久其的方法提取XML失败: {}", e.getMessage(), e);
        }
        
        // 回退到自己的方法
        log.info("使用自己的方法从OFD中提取XML");
        return OfdUtils.extractXmlFromOfdWithTemp(ofdBytes);
    }

    @Override
    public ChangeIdentifyInfo conversionInfo(DataImageFilesInfo dataImageFilesInfo, List identifyResults) throws OcrException {
        // OFD策略不使用此方法，因为它直接处理JSON数据
        return null;
    }
}