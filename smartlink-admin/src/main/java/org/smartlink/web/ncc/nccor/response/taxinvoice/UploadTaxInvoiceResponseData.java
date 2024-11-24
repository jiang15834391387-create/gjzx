package org.smartlink.web.ncc.nccor.response.taxinvoice;

import cn.hutool.core.convert.Convert;
import lombok.Data;
import org.smartlink.web.domain.invoice.DataOcrInfo;
import org.smartlink.web.ncc.nccverify.request.VerifyTaxInvoiceRequestData;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: ocr结果返回data
 * @author: chenJiangHong
 * @create: 2022-06-13 07:41
 **/
@Data
public class UploadTaxInvoiceResponseData {

    /**
     * 状态code
     */
    private String code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 发票代码
     */
    private String fpDm;

    /**
     * 发票号码
     */
    private String fpHm;

    /**
     * 开票日期
     */
    private String kprq;

    /**
     * 合计金额
     */
    private String hjje;

    /**
     * 校验码
     */
    private String jym;

    /**
     * NCC返回TOKEN
     */
    private String verifyToken;


    /**
     * 组装OCR信息
     * @param fileId 图片表ID
     * @return DataOcrInfo
     */
    public DataOcrInfo getTaxInvoiceInfo(String fileId){
        // 默认所有发票信息都存DataOcrInfo表中 ，因为OCR接口不返回发票类别
        DataOcrInfo dataOcrInfo = new DataOcrInfo();
        dataOcrInfo.setOcrFileId(fileId);
        dataOcrInfo.setFileId(fileId);
        dataOcrInfo.setInvoiceCode(this.fpDm);
        dataOcrInfo.setInvoiceNumber(this.fpHm);
        dataOcrInfo.setSumAmount(Convert.toBigDecimal(this.hjje));
        dataOcrInfo.setInvoiceDate(Convert.toDate(this.kprq));
        dataOcrInfo.setCheckCode(this.jym);
        dataOcrInfo.setSaveToken(this.verifyToken);
        return dataOcrInfo;
    }

    /**
     * 组装NCC查验接口所需的data对象
     * @param businessSerialNo
     * @return
     */
    public List<VerifyTaxInvoiceRequestData> getDataByTaxResult(String businessSerialNo){
        List<VerifyTaxInvoiceRequestData> dataList = new ArrayList<>();
        VerifyTaxInvoiceRequestData data = new VerifyTaxInvoiceRequestData();
        data.setFpHm(this.fpHm);
        data.setFpDm(this.fpDm);
        data.setBillid(businessSerialNo);
        data.setKprq(this.kprq);
        data.setHjje(this.hjje);
        data.setJshj("");
        data.setJym(this.jym);
        data.setVerifyToken(this.verifyToken);
        dataList.add(data);
        return dataList;
    }


}
