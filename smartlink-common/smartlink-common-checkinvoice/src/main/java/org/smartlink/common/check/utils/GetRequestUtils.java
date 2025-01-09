package org.smartlink.common.check.utils;

import cn.hutool.core.util.StrUtil;
import org.smartlink.common.check.doman.InvoiceBaseEntity;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.check.invoice.DataMotorVehicleSale;
import org.smartlink.common.check.invoice.DataOcrInfo;
import org.smartlink.common.check.invoice.DataUsedCarSales;


/**
 * @author shidunkai
 * @title 获取五要素
 * @description 获取五要素
 */
public class GetRequestUtils {
    public static InvoiceCheckParamDTO getRequest(InvoiceBaseEntity t) {
        InvoiceCheckParamDTO invoiceCheckParamDTO = new InvoiceCheckParamDTO();
        if(t instanceof DataUsedCarSales){
            DataUsedCarSales dataUsedCarSales = (DataUsedCarSales) t;
            invoiceCheckParamDTO.setId(dataUsedCarSales.getId());
            invoiceCheckParamDTO.setInvoiceCode(dataUsedCarSales.getInvoiceCode());
            invoiceCheckParamDTO.setInvoiceNumber(dataUsedCarSales.getInvoiceNumber());
            invoiceCheckParamDTO.setInvoiceDate(dataUsedCarSales.getInvoiceDate());
            invoiceCheckParamDTO.setCheckCode("");
            invoiceCheckParamDTO.setTotal(StrUtil.toString(dataUsedCarSales.getInvoiceTotal()));
            invoiceCheckParamDTO.setTotalAmount(StrUtil.toString(dataUsedCarSales.getInvoiceTotal()));
        }else if(t instanceof DataMotorVehicleSale){
            DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) t;
            invoiceCheckParamDTO.setId(dataMotorVehicleSale.getId());
            invoiceCheckParamDTO.setInvoiceCode(dataMotorVehicleSale.getInvoiceCode());
            invoiceCheckParamDTO.setInvoiceNumber(dataMotorVehicleSale.getInvoiceNumber());
            invoiceCheckParamDTO.setInvoiceDate(dataMotorVehicleSale.getInvoiceDate());
            invoiceCheckParamDTO.setCheckCode("");
            invoiceCheckParamDTO.setTotal(StrUtil.toString(dataMotorVehicleSale.getPreTaxAmount()));
            invoiceCheckParamDTO.setTotalAmount(StrUtil.toString(dataMotorVehicleSale.getInvoiceTotal()));
        }else{
            DataOcrInfo dataOcrInfo = (DataOcrInfo) t;
            invoiceCheckParamDTO.setId(dataOcrInfo.getId());
            invoiceCheckParamDTO.setInvoiceCode(dataOcrInfo.getInvoiceCode());
            invoiceCheckParamDTO.setInvoiceNumber(dataOcrInfo.getInvoiceNumber());
            invoiceCheckParamDTO.setInvoiceDate(dataOcrInfo.getInvoiceDate());
            invoiceCheckParamDTO.setCheckCode(dataOcrInfo.getCheckCode());
            invoiceCheckParamDTO.setTotal(StrUtil.toString(dataOcrInfo.getSumAmount()));
            invoiceCheckParamDTO.setTotalAmount(StrUtil.toString(dataOcrInfo.getTotalLowercase()));
            invoiceCheckParamDTO.setTotalLowercase(StrUtil.toString(dataOcrInfo.getTotalLowercase()));
        }
        return invoiceCheckParamDTO;
    }
}
