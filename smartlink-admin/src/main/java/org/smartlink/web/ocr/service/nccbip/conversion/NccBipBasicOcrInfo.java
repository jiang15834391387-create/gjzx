package org.smartlink.web.ocr.service.nccbip.conversion;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import org.smartlink.web.domain.invoice.DataMotorVehicleSale;
import org.smartlink.web.domain.invoice.DataOcrInfo;
import org.smartlink.web.domain.invoice.DataUsedCarSales;
import org.smartlink.web.ocr.service.nccbip.response.NccBipCheckResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * OCR信息的转换
 *
 * @author L
 */
@Component
public class NccBipBasicOcrInfo {




    public void setBasicOcrInfo(NccBipCheckResponse jsonObject, DataMotorVehicleSale ocrInfo) {
        NccBipCheckResponse.DatasDTO datasDTO = jsonObject.getDataBip().get(0);
        NccBipCheckResponse.DatasDTO.InvoiceDTO invoice = datasDTO.getInvoice();
        List<NccBipCheckResponse.DatasDTO.InvoiceDTO.ItemsDTO> items = invoice.getItems();
        ocrInfo.setInvoiceCode(datasDTO.getInvoice().getFpDm());
        ocrInfo.setInvoiceNumber(datasDTO.getInvoice().getFpHm());
        ocrInfo.setInvoiceDate(Convert.toDate(datasDTO.getInvoice().getKpr()));
        ocrInfo.setInvoiceTotal(Convert.toBigDecimal(datasDTO.getInvoice().getJshj()));
        ocrInfo.setPreTaxAmount(Convert.toBigDecimal(datasDTO.getInvoice().getHjje()));
        ocrInfo.setTaxAuthorities(datasDTO.getInvoice().getSwjgmc());
        ocrInfo.setTaxAuthoritiesCode(datasDTO.getInvoice().getSwjgdm());
        ocrInfo.setSellerBankName(datasDTO.getInvoice().getXsfYhzh());
        ocrInfo.setSellerTaxid(datasDTO.getInvoice().getXsfNsrsbh());
        ocrInfo.setSeller(datasDTO.getInvoice().getXsfMc());
        ocrInfo.setSellerAddress(datasDTO.getInvoice().getXsfDzdh());
        ocrInfo.setBuyerName(datasDTO.getInvoice().getGmfMc());
        ocrInfo.setBuyerId(datasDTO.getInvoice().getGmfNsrsbh());
        //保存token
        ocrInfo.setSaveToken(datasDTO.getSaveToken());
        if(CollUtil.isNotEmpty(items)){
            NccBipCheckResponse.DatasDTO.InvoiceDTO.ItemsDTO itemsDTO = items.get(0);
            ocrInfo.setVehicleType(itemsDTO.getXmmc());
            ocrInfo.setTaxRate(Convert.toStr(itemsDTO.getSl()));
            ocrInfo.setTax(Convert.toBigDecimal(itemsDTO.getSe()));
            ocrInfo.setCarModel(itemsDTO.getGgxh());
            NccBipCheckResponse.DatasDTO.InvoiceDTO.ItemsDTO.DetailMotorDTO detailMotor = itemsDTO.getDetailMotor();
            if(ObjectUtil.isNotEmpty(detailMotor)){
                ocrInfo.setProduceArea(detailMotor.getCd());
                ocrInfo.setCertificateNumber(detailMotor.getHgzh());
                ocrInfo.setCarEngineCode(detailMotor.getFdjhm());
                ocrInfo.setCarCode(detailMotor.getCjhm());
                ocrInfo.setTonnage(detailMotor.getDunwei());
                ocrInfo.setLimitedPeopleCount(Convert.toLong(detailMotor.getXcrs()));
                ocrInfo.setTonnage(detailMotor.getDunwei());
            }
        }
    }

    public void setBasicOcrInfo(NccBipCheckResponse jsonObject, DataUsedCarSales ocrInfo) {
        NccBipCheckResponse.DatasDTO datasDTO = jsonObject.getDataBip().get(0);
        NccBipCheckResponse.DatasDTO.InvoiceDTO invoice = datasDTO.getInvoice();
        List<NccBipCheckResponse.DatasDTO.InvoiceDTO.ItemsDTO> items = invoice.getItems();
        ocrInfo.setInvoiceCode(datasDTO.getInvoice().getFpDm());
        ocrInfo.setInvoiceNumber(datasDTO.getInvoice().getFpHm());
        ocrInfo.setInvoiceTotal(Convert.toBigDecimal(datasDTO.getInvoice().getJshj()));
        ocrInfo.setSellerName(datasDTO.getInvoice().getXsfMc());
        ocrInfo.setInvoiceDate(Convert.toDate(datasDTO.getInvoice().getKprq()));
        ocrInfo.setBuyerAddress(datasDTO.getInvoice().getGmfDzdh());
        ocrInfo.setSellerAddress(datasDTO.getInvoice().getXsfDzdh());
        ocrInfo.setBuyerId(datasDTO.getInvoice().getGmfNsrsbh());
        ocrInfo.setBuyerName(datasDTO.getInvoice().getGmfMc());
        ocrInfo.setSellerId(datasDTO.getInvoice().getXsfNsrsbh());
        ocrInfo.setMachineCode(datasDTO.getInvoice().getJqbh());
        ocrInfo.setCompanyTaxId(datasDTO.getInvoice().getGmfNsrsbh());
        //保存token
        ocrInfo.setSaveToken(datasDTO.getSaveToken());
        if(CollUtil.isNotEmpty(items)){
            NccBipCheckResponse.DatasDTO.InvoiceDTO.ItemsDTO itemsDTO = items.get(0);
            ocrInfo.setLicensePlate(itemsDTO.getGgxh());
            ocrInfo.setVehicleType(itemsDTO.getXmmc());
            NccBipCheckResponse.DatasDTO.InvoiceDTO.ItemsDTO.DetailMotorDTO detailMotor = itemsDTO.getDetailMotor();
            if(ObjectUtil.isNotEmpty(detailMotor)) {
                ocrInfo.setCarCode(detailMotor.getCjhm());
            }
        }

    }
    public void setBasicOcrInfo(NccBipCheckResponse jsonObject, DataOcrInfo ocrInfo) {
        NccBipCheckResponse.DatasDTO datasDTO = jsonObject.getDataBip().get(0);
        NccBipCheckResponse.DatasDTO.InvoiceDTO invoice = datasDTO.getInvoice();
        ocrInfo.setInvoiceCode(datasDTO.getFpDm());
        ocrInfo.setInvoiceNumber(datasDTO.getFpHm());
        ocrInfo.setInvoiceDate(Convert.toDate(invoice.getKprq()));
        ocrInfo.setSumTax(Convert.toBigDecimal(invoice.getHjse()));
        ocrInfo.setSumAmount(Convert.toBigDecimal(invoice.getHjje()));
        ocrInfo.setTotalLowercase(Convert.toBigDecimal(invoice.getJshj()));
        // 大写 价税合计
        ocrInfo.setTotalUppercase(Convert.digitToChinese(invoice.getHjje()));
        // 收款人
        ocrInfo.setPayee(invoice.getSkr());
        // 复核人
        ocrInfo.setChecker(invoice.getFhr());
        // 开票人
        ocrInfo.setIssuer(invoice.getKpr());
        // 校验码
        ocrInfo.setCheckCode(invoice.getJym());
        // 机器编号
        //ocrInfo.setMachineCode(jsonObject.getStr("machineNo"));
        ocrInfo.setRemark(invoice.getBz());
        // 购方名称
        ocrInfo.setBuyerName(invoice.getGmfMc());
        // 购方开户行及账号
        ocrInfo.setBuyerAccount(invoice.getGmfYhzh());
        // 购方地址、电话
        ocrInfo.setBuyerAddress(invoice.getGmfDzdh());
        // 购方税号
        ocrInfo.setBuyerNo(invoice.getGmfNsrsbh());
        // 销方名称
        ocrInfo.setSellerName(invoice.getXsfMc());
        // 销方开户行及账号
        ocrInfo.setSellerAccount(invoice.getXsfYhzh());
        // 销方地址、电话
        ocrInfo.setSellerAddress(invoice.getXsfDzdh());
        // 销方税号
        ocrInfo.setSellerNo(invoice.getXsfNsrsbh());
        // 作废标志
        //ocrInfo.setCancellationMark(jsonObject.getStr("cancellationMark"));
        //保存token
        ocrInfo.setSaveToken(datasDTO.getSaveToken());
    }
}
