package org.smartlink.server.nc.ocr.service.ccint.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.invoice.*;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.bean.Others;
import org.smartlink.server.nc.ocr.service.ccint.service.DealWithService;
import org.springframework.stereotype.Service;

/**
 * @description： 处理service
 * @author： L
 * @create：
 */
@Service
public class DealWithServiceImpl implements DealWithService {

    /**
     * 二次处理服务
     *
     * @param identificationData 识别k v
     * @return 识别 k v
     */
    @Override
    public IdentificationData dealIdentificationData(IdentificationData identificationData) {
        if (StrUtil.equals(InvoiceConstants.TAX_SPECIAL_INVOICE, identificationData.k) || StrUtil.equals(InvoiceConstants.ELECTRONIC_OFD_INVOICE, identificationData.k) || StrUtil.equals(InvoiceConstants.ELECTRONIC_INVOICE_ITINERARY, identificationData.k) || StrUtil.equals(InvoiceConstants.MOTOR_VEHICLE_SALE, identificationData.k) || StrUtil.equals(InvoiceConstants.USED_CAR_SALES, identificationData.k) || StrUtil.equals(InvoiceConstants.TAX_INVOICE, identificationData.k) || StrUtil.equals(InvoiceConstants.ELECTRONIC_INVOICE, identificationData.k)) {
            if (identificationData.t instanceof DataOcrInfo) {
                DataOcrInfo ocrInfo = (DataOcrInfo) identificationData.t;
                if (StrUtil.isBlank(ocrInfo.getInvoiceNumber())) {
                    Others others = new Others();
                    others.setFileId(ocrInfo.getFileId());
                    others.setCoordinate(Convert.toStrArray(ocrInfo.getCoordinate()));
                    others.setOption("base");
                    others.setBase64(ocrInfo.getBase64());
                    identificationData = new IdentificationData(InvoiceConstants.IMAGE_OTHERS, others);
                }
            }
        } else if (StrUtil.equals(InvoiceConstants.PASSENGER_TICKET, identificationData.k)) {
            //客运汽车票
            if (identificationData.t instanceof DataPassengerTicket) {
                DataPassengerTicket ocrInfo = (DataPassengerTicket) identificationData.t;
                if (StrUtil.isBlank(ocrInfo.getInvoiceCode()) || StrUtil.isBlank(ocrInfo.getInvoiceNumber())) {
                    Others others = new Others();
                    others.setFileId(ocrInfo.getFileId());
                    others.setCoordinate(Convert.toStrArray(ocrInfo.getCoordinate()));
                    others.setOption("base");
                    others.setBase64(ocrInfo.getBase64());
                    identificationData = new IdentificationData(InvoiceConstants.IMAGE_OTHERS, others);
                }
            }
        } else if (StrUtil.equals(InvoiceConstants.TAXI_TICKETS, identificationData.k)) {
            //出租车发票
            if (identificationData.t instanceof DataTaxiTickets) {
                DataTaxiTickets taxiTickets = (DataTaxiTickets) identificationData.t;
                if (StrUtil.isBlank(taxiTickets.getInvoiceCode()) || StrUtil.isBlank(taxiTickets.getInvoiceNumber())) {
                    Others others = new Others();
                    others.setFileId(taxiTickets.getFileId());
                    others.setCoordinate(Convert.toStrArray(taxiTickets.getCoordinate()));
                    others.setOption("base");
                    others.setBase64(taxiTickets.getBase64());
                    identificationData = new IdentificationData(InvoiceConstants.IMAGE_OTHERS, others);
                }
            }
        } else if (StrUtil.equals(InvoiceConstants.QUOTA_INVOICE, identificationData.k)) {
            //定额发票
            if (identificationData.t instanceof DataQuotaInvoice) {
                DataQuotaInvoice taxiTickets = (DataQuotaInvoice) identificationData.t;
                if (StrUtil.isBlank(taxiTickets.getInvoiceCode()) || StrUtil.isBlank(taxiTickets.getInvoiceNumber())) {
                    Others others = new Others();
                    others.setFileId(taxiTickets.getFileId());
                    others.setCoordinate(Convert.toStrArray(taxiTickets.getCoordinate()));
                    others.setOption("base");
                    others.setBase64(taxiTickets.getBase64());
                    identificationData = new IdentificationData(InvoiceConstants.IMAGE_OTHERS, others);
                }
            }
        } else if (StrUtil.equals(InvoiceConstants.RECEIPT, identificationData.k)) {
            //115 小票
            if (identificationData.t instanceof DataReceipt) {
                DataReceipt receipt = (DataReceipt) identificationData.t;
                if (StrUtil.isBlank(Convert.toStr(receipt.getInvoiceDate())) || StrUtil.isBlank(receipt.getTips())) {
                    Others others = new Others();
                    others.setFileId(receipt.getFileId());
                    others.setCoordinate(Convert.toStrArray(receipt.getCoordinate()));
                    others.setOption("base");
                    others.setBase64(receipt.getBase64());
                    identificationData = new IdentificationData(InvoiceConstants.IMAGE_OTHERS, others);
                }
            }
        } else if (StrUtil.equals(InvoiceConstants.TOLL_ROADS, identificationData.k)) {
            //113 过路费
            if (identificationData.t instanceof DataTollRoads) {
                DataTollRoads receipt = (DataTollRoads) identificationData.t;
                if (StrUtil.isBlank(receipt.getInvoiceNumber()) || StrUtil.isBlank(receipt.getInvoiceCode())) {
                    Others others = new Others();
                    others.setFileId(receipt.getFileId());
                    others.setCoordinate(Convert.toStrArray(receipt.getCoordinate()));
                    others.setOption("base");
                    others.setBase64(receipt.getBase64());
                    identificationData = new IdentificationData(InvoiceConstants.IMAGE_OTHERS, others);
                }
            }
        }
        return identificationData;
    }
}
