package org.smartlink.web.ocr.service.nccbip.conversion;

import cn.hutool.core.convert.Convert;
import org.smartlink.common.core.utils.DateUtils;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.invoice.DataCustomsInvoice;
import org.smartlink.web.domain.modle.BaseEntity;
import org.smartlink.web.ocr.exception.OcrException;
import org.smartlink.web.ocr.service.ChangeIdentifyInfo;
import org.smartlink.web.ocr.service.bean.IdentificationData;
import org.smartlink.web.ocr.service.nccbip.BIPCoordinateUtil;
import org.smartlink.web.ocr.service.nccbip.response.NccBipOcrResponse;


/**
 * <p>Title: CustomsInvoiceConversion</p>
 * <p>
 * <p>Description:海关进口增值税专用缴款书实体类 </p>
 *
 * @author L
 * @date
 **/
public class CustomsInvoiceConversion implements ChangeIdentifyInfo<NccBipOcrResponse.OneDataDTO.DatasDTO> {


    private static class LazyHolder {

        private static final CustomsInvoiceConversion INSTANCE = new CustomsInvoiceConversion();
    }

    private CustomsInvoiceConversion() {
    }

    public static CustomsInvoiceConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, NccBipOcrResponse.OneDataDTO.DatasDTO datasDTO) throws OcrException {
        DataCustomsInvoice dataCustomsInvoice = new DataCustomsInvoice();
        dataCustomsInvoice.setInvoiceDate(Convert.toDate(datasDTO.getData().getDate()));
        dataCustomsInvoice.setFileId(dataImageFilesInfo.getFileId());
        dataCustomsInvoice.setInvoiceTotal(Convert.toBigDecimal(datasDTO.getData().getTotal()));
        dataCustomsInvoice.setMoneyUppercase(Convert.digitToChinese(Convert.toBigDecimal(datasDTO.getData().getTotal())));
        dataCustomsInvoice.setInvoiceNumber(datasDTO.getData().getFpHm());
        dataCustomsInvoice.setCorporateAccountNo(datasDTO.getData().getCorporateAccountNo());
        dataCustomsInvoice.setCustomsNo(datasDTO.getData().getCustomsNo());
        dataCustomsInvoice.setCorporateBank(datasDTO.getData().getCorporateBank());
        dataCustomsInvoice.setCustomsName(datasDTO.getData().getCustomsName());
        dataCustomsInvoice.setSubject(datasDTO.getData().getSubject());
        dataCustomsInvoice.setRevenueOrg(datasDTO.getData().getRevenueOrg());
        dataCustomsInvoice.setRevenueSys(datasDTO.getData().getRevenueSys());
        dataCustomsInvoice.setCurrencyCode(datasDTO.getData().getCurrencyCode());
        dataCustomsInvoice.setCustomsBillNo(datasDTO.getData().getCustomsBillNo());
        dataCustomsInvoice.setContractNo(datasDTO.getData().getContractNo());
        dataCustomsInvoice.setCorporateName(datasDTO.getData().getCorporateName());
        dataCustomsInvoice.setInvoiceType(datasDTO.getData().getInvoiceType());
        dataImageFilesInfo.setFileType(InvoiceConstants.INVOICE_CUSTBOOK_NCC);
        dataCustomsInvoice.setSaveToken(datasDTO.getImgOcrToken());
        dataCustomsInvoice.setCoordinateStr(BIPCoordinateUtil.getCoordinateStr(datasDTO.getRegion()));
        dataCustomsInvoice.setIsStaging(dataImageFilesInfo.getIsStaging());
        return new IdentificationData<>(dataImageFilesInfo.getFileType(), dataCustomsInvoice);
    }

    @Override
    public NccBipOcrResponse.OneDataDTO.DatasDTO.DataDTO reverseConversion(BaseEntity baseEntity) {
        NccBipOcrResponse.OneDataDTO.DatasDTO.DataDTO dataDTO = new NccBipOcrResponse.OneDataDTO.DatasDTO.DataDTO();
        DataCustomsInvoice dataCustomsInvoice = (DataCustomsInvoice) baseEntity;
        dataDTO.setFpHm(dataCustomsInvoice.getInvoiceNumber());
        dataDTO.setTotal(Convert.toStr(dataCustomsInvoice.getInvoiceTotal()));
        dataDTO.setDate(DateUtils.parseDateToStr("yyyy年MM月dd日",dataCustomsInvoice.getInvoiceDate()));
        dataDTO.setCorporateAccountNo(dataCustomsInvoice.getCorporateAccountNo());
        dataDTO.setCustomsNo(dataCustomsInvoice.getCustomsNo());
        dataDTO.setCorporateBank(dataCustomsInvoice.getCorporateBank());
        dataDTO.setCustomsName(dataCustomsInvoice.getCustomsName());
        dataDTO.setSubject(dataCustomsInvoice.getSubject());
        dataDTO.setRevenueSys(dataCustomsInvoice.getRevenueSys());
        dataDTO.setRevenueOrg(dataCustomsInvoice.getRevenueOrg());
        dataDTO.setCurrencyCode(dataCustomsInvoice.getCurrencyCode());
        dataDTO.setLoadBillNo(dataCustomsInvoice.getLoadBillNo());
        dataDTO.setCustomsNo(dataCustomsInvoice.getCustomsNo());
        dataDTO.setCorporateName(dataCustomsInvoice.getCorporateName());
        dataDTO.setInvoiceType(dataCustomsInvoice.getInvoiceType());
        dataDTO.setCustomsBillNo(dataCustomsInvoice.getCustomsBillNo());
        dataDTO.setImgOcrToken(dataCustomsInvoice.getSaveToken());
        return dataDTO;
    }
}
