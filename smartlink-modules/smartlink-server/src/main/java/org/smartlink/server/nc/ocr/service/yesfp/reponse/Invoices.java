package org.smartlink.server.nc.ocr.service.yesfp.reponse;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.*;
import org.smartlink.server.nc.domain.invoice.dto.InvoiceCheckParamDTO;


/**
 * <p>Title: Invoices </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoices {

    private String fpDm = "";

    private String fpHm = "";

    private String kprq = "";

    private String hjje = "";

    private String jshj = "";

    private String jym = "";

    private String saveToken = "";

    private String srcBillType = "";

    private String srcBillCode = "";

    private String imageId = "";

    public Invoices setInvoice(InvoiceCheckParamDTO dto){
        return Invoices.builder()
                .fpDm(StrUtil.isNotEmpty(dto.getInvoiceCode())?dto.getInvoiceCode():"")
                .fpHm(StrUtil.isNotEmpty(dto.getInvoiceNumber())?dto.getInvoiceNumber():"")
                .kprq(ObjectUtil.isNotEmpty(dto.getInvoiceDate())?DateUtil.format(dto.getInvoiceDate(), "yyyyMMdd"):"")
                .hjje(StrUtil.isNotEmpty(dto.getTotal())?dto.getTotal():"")
                .jshj(StrUtil.isNotEmpty(dto.getTotalAmount())?dto.getTotalAmount():"")
                .jym(StrUtil.isNotEmpty(dto.getCheckCode())?dto.getCheckCode():"")
                .srcBillType("影像")
                .srcBillCode(dto.getBillNum())
                .build();
    }


}
