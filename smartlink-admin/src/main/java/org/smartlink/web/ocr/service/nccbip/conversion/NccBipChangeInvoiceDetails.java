package org.smartlink.web.ocr.service.nccbip.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import org.smartlink.web.domain.invoice.DataOcrDetails;
import org.smartlink.web.ocr.service.nccbip.response.NccBipCheckResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * OCR 详情转换
 *
 * @author L
 */
@Component
public class NccBipChangeInvoiceDetails {


    public List<DataOcrDetails> changeInvoiceDetails(String fileId, NccBipCheckResponse response, String ocrId) {
        List<DataOcrDetails> ocrDetailsList = new ArrayList<>(16);
        List<NccBipCheckResponse.DatasDTO.InvoiceDTO.ItemsDTO> items = response.getDataBip().get(0).getInvoice().getItems();
        for (NccBipCheckResponse.DatasDTO.InvoiceDTO.ItemsDTO item : items) {
            DataOcrDetails ocrDetails = new DataOcrDetails();
            ocrDetails.setId(IdUtil.simpleUUID());
            ocrDetails.setFileId(fileId);
            ocrDetails.setName(item.getXmmc());
            ocrDetails.setTax(Convert.toBigDecimal(item.getSe()));
            ocrDetails.setTaxRate(String.valueOf(item.getSl()));
            ocrDetails.setDetailAmount(Convert.toBigDecimal(item.getXmjshj()));
            ocrDetails.setPrice(Convert.toBigDecimal(item.getXmdj()));
            ocrDetails.setDetailsCount(Convert.toBigDecimal(item.getSl()));
            ocrDetails.setUnit(item.getDw());
            ocrDetails.setStandard(item.getGgxh());
            ocrDetails.setOcrId(ocrId);
            ocrDetailsList.add(ocrDetails);
        }
        return ocrDetailsList;
    }
}
