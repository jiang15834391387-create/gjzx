package org.smartlink.web.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.domain.invoice.DataMotorVehicleSale;
import org.smartlink.web.domain.invoice.DataOcrInfo;
import org.smartlink.web.domain.invoice.DataUsedCarSales;
import org.smartlink.web.domain.invoice.dto.InvoiceCheckParamDTO;
import org.smartlink.web.domain.modle.BaseEntity;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @author L
 * @title 获取五要素
 * @description 获取五要素
 * @date
 */
@Slf4j
public class GetRequestUtils {
    public static InvoiceCheckParamDTO getRequest(BaseEntity t) {
        InvoiceCheckParamDTO invoiceCheckParamDTO = new InvoiceCheckParamDTO();
        if(t instanceof DataUsedCarSales){
            DataUsedCarSales dataUsedCarSales = (DataUsedCarSales) t;
            invoiceCheckParamDTO.setId(dataUsedCarSales.getId());
            invoiceCheckParamDTO.setInvoiceCode(dataUsedCarSales.getInvoiceCode());
            invoiceCheckParamDTO.setInvoiceNumber(dataUsedCarSales.getInvoiceNumber());
            invoiceCheckParamDTO.setInvoiceDate(dataUsedCarSales.getInvoiceDate());
            invoiceCheckParamDTO.setCheckCode("");
            invoiceCheckParamDTO.setTotal(StrUtil.toString(StringUtils.isNull(dataUsedCarSales.getInvoiceTotal())));
            invoiceCheckParamDTO.setTotalAmount(StrUtil.toString(StringUtils.isNull(dataUsedCarSales.getInvoiceTotal())));
        }else if(t instanceof DataMotorVehicleSale){
            DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) t;
            invoiceCheckParamDTO.setId(dataMotorVehicleSale.getId());
            invoiceCheckParamDTO.setInvoiceCode(dataMotorVehicleSale.getInvoiceCode());
            invoiceCheckParamDTO.setInvoiceNumber(dataMotorVehicleSale.getInvoiceNumber());
            invoiceCheckParamDTO.setInvoiceDate(dataMotorVehicleSale.getInvoiceDate());
            invoiceCheckParamDTO.setCheckCode("");
            invoiceCheckParamDTO.setTotal(StrUtil.toString(StringUtils.isNull(dataMotorVehicleSale.getPreTaxAmount())));
            invoiceCheckParamDTO.setTotalAmount(StrUtil.toString(StringUtils.isNull(dataMotorVehicleSale.getInvoiceTotal())));
        }else if(t instanceof DataOcrInfo){
            DataOcrInfo dataOcrInfo = (DataOcrInfo) t;
            invoiceCheckParamDTO.setId(dataOcrInfo.getId());
            invoiceCheckParamDTO.setInvoiceCode(dataOcrInfo.getInvoiceCode());
            invoiceCheckParamDTO.setInvoiceNumber(dataOcrInfo.getInvoiceNumber());
            invoiceCheckParamDTO.setInvoiceDate(dataOcrInfo.getInvoiceDate());
            invoiceCheckParamDTO.setCheckCode(dataOcrInfo.getCheckCode());
            invoiceCheckParamDTO.setTotal(StrUtil.toString(StringUtils.isNull(dataOcrInfo.getSumAmount())));
            invoiceCheckParamDTO.setTotalAmount(StrUtil.toString(StringUtils.isNull(dataOcrInfo.getTotalLowercase())));
            invoiceCheckParamDTO.setTotalLowercase(StrUtil.toString(StringUtils.isNull(dataOcrInfo.getTotalLowercase())));
            invoiceCheckParamDTO.setElectronicNumber(dataOcrInfo.getElectronicNumber());
        }else{
            Class<?> invoiceClass = null;
            try {
                invoiceClass = Class.forName(InvoiceConstants.INVOICE_ClASS_TYPE.get(t.getOcrFileType()));
            } catch (ClassNotFoundException e) {
                log.error("ClassNotFoundException:{}",t.getOcrFileType());
            }
            try {
                Field invoiceNumberField = invoiceClass.getDeclaredField("invoiceNumber");
                invoiceNumberField.setAccessible(true);
                invoiceCheckParamDTO.setInvoiceNumber((String) invoiceNumberField.get(t));
            } catch (Exception e) {
                log.error("invoiceNumber: {}",e.getMessage());
            }
            try {
                Field invoiceCodeField = invoiceClass.getDeclaredField("invoiceCode");
                invoiceCodeField.setAccessible(true);
                invoiceCheckParamDTO.setInvoiceCode((String) invoiceCodeField.get(t));
            } catch (Exception e) {
                log.error("invoiceCode: {}",e.getMessage());
            }
            try {
                Field invoiceCodeField = invoiceClass.getDeclaredField("invoiceDate");
                invoiceCodeField.setAccessible(true);
                invoiceCheckParamDTO.setInvoiceDate((Date) invoiceCodeField.get(t));
            } catch (Exception e) {
                log.error("invoiceDate: {}", e.getMessage());
            }
            try {
                Field invoiceCodeField = invoiceClass.getDeclaredField("invoiceTotal");
                invoiceCodeField.setAccessible(true);
                invoiceCheckParamDTO.setTotal((String) invoiceCodeField.get(t));
            } catch (Exception e) {
                log.error("invoiceDate: {}", e.getMessage());
            }
            try {
                Field invoiceCodeField = invoiceClass.getDeclaredField("name");
                invoiceCodeField.setAccessible(true);
                invoiceCheckParamDTO.setName((String) invoiceCodeField.get(t));
            } catch (Exception e) {
                log.error("name: {}", e.getMessage());
            }
            try {
                Field invoiceCodeField = invoiceClass.getDeclaredField("saveToken");
                invoiceCodeField.setAccessible(true);
                invoiceCheckParamDTO.setSaveToken((String) invoiceCodeField.get(t));
            } catch (Exception e) {
                log.error("saveToken: {}", e.getMessage());
            }
            try {
                Field invoiceCodeField = invoiceClass.getDeclaredField("stationGetOn");
                invoiceCodeField.setAccessible(true);
                invoiceCheckParamDTO.setExit((String) invoiceCodeField.get(t));
            } catch (Exception e) {
                log.error("stationGetOn: {}", e.getMessage());
            }
            try {
                Field invoiceCodeField = invoiceClass.getDeclaredField("ticketNum");
                invoiceCodeField.setAccessible(true);
                invoiceCheckParamDTO.setTicketNum((String) invoiceCodeField.get(t));
            } catch (Exception e) {
                log.error("ticketNum: {}", e.getMessage());
            }
        }
        return invoiceCheckParamDTO;
    }
}
