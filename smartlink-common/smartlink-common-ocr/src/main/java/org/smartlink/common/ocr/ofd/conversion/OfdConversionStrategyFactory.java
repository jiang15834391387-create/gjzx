package org.smartlink.common.ocr.ofd.conversion;

import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.exception.OcrException;
import cn.hutool.json.JSONObject;

/**
 * OFD发票转换策略工厂
 */
public class OfdConversionStrategyFactory {

    private OfdConversionStrategyFactory() {
    }

    /**
     * 根据OFD数据内容自动判断并获取对应的转换策略
     * @param ofdData OFD数据
     * @return 转换策略实例
     * @throws OcrException 当不支持的OFD发票类型时抛出异常
     */
    public static ChangeIdentifyInfo<JSONObject> getConversionStrategyByContent(JSONObject ofdData) throws OcrException {
        if (ofdData == null) {
            return OfdVatInvoiceConversion.getInstance();
        }

        // 目前只支持增值税电子专票（TypeCode=0）
        return OfdVatInvoiceConversion.getInstance();
    }
}