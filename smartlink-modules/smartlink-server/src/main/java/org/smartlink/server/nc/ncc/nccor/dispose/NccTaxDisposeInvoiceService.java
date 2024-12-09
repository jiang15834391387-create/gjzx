package org.smartlink.server.nc.ncc.nccor.dispose;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.dto.UploadInvoiceForNccRequest;
import org.smartlink.server.nc.domain.invoice.DataOcrInfo;
import org.smartlink.server.nc.enums.NcCodeEnum;
import org.smartlink.server.nc.ncc.nccor.NccOcrRecognitionService;
import org.smartlink.server.nc.ncc.nccor.request.taxinvoice.UploadTaxInvoiceRequest;
import org.smartlink.server.nc.ncc.nccor.request.taxinvoice.UploadTaxInvoiceRequestData;
import org.smartlink.server.nc.ncc.nccor.response.taxinvoice.UploadTaxInvoiceResponse;
import org.smartlink.server.nc.ncc.nccor.response.taxinvoice.UploadTaxInvoiceResponseData;
import org.smartlink.server.nc.ncc.nccverify.dispose.NccTaxVerifyDisposeInvoiceService;
import org.smartlink.server.nc.service.nc.IDataOcrService;
import org.smartlink.server.nc.token.response.Token;
import org.smartlink.server.nc.utils.file.FileUtils;
import org.smartlink.server.nc.utils.file.MimeTypeUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author L
 * @Description NCC 增值税发票逻辑处理类
 * @create:
 */
@Slf4j
@Component
@AllArgsConstructor
public class NccTaxDisposeInvoiceService {

    private NccTaxVerifyDisposeInvoiceService nccVerifyDisposeInvoiceService;
    private IDataOcrService dataOcrService;

    public static void main(String[] args) throws NoSuchMethodException {
        Method getTaxInvoiceInfo = NccTaxDisposeInvoiceService.class.getDeclaredMethod("getTaxInvoiceInfo", UploadInvoiceForNccRequest.class, Token.class);
        System.out.println(getTaxInvoiceInfo.getGenericReturnType());
    }

    /**
     * NCC增值税发票上传处理逻辑
     * @param uploadInvoiceForNCCRequest 请求实体对象
     * @param token NCC token
     * @return DataImageFilesInfo
     */
    public DataImageFilesInfo getTaxInvoiceInfo(UploadInvoiceForNccRequest uploadInvoiceForNCCRequest, Token token) throws Exception {
        DataImageFilesInfo dataImageFilesInfo = uploadInvoiceForNCCRequest.getDataImageFilesInfo();
        UploadTaxInvoiceRequest uploadTaxRequest = UploadTaxInvoiceRequest.builder()
                .billtype(uploadInvoiceForNCCRequest.getBillType())
                .datasource(uploadInvoiceForNCCRequest.getParamProperties().getDataSource())
                .factorycode(uploadInvoiceForNCCRequest.getParamProperties().getFactoryCode())
                //.pk_org(uploadInvoiceForNCCRequest.getPk_org())
                .orgCode(uploadInvoiceForNCCRequest.getOrgCode())
                .transitype(uploadInvoiceForNCCRequest.getPkBillType())
                .userid(uploadInvoiceForNCCRequest.getUserId())
                .data(UploadTaxInvoiceRequestData.builder()
                        .billid(uploadInvoiceForNCCRequest.getBillId())
                        .file(uploadInvoiceForNCCRequest.getFile())
                        .build())
                .build();
        // 判断如果不是图片类型，直接返回
        String suffix = FileUtils.getFileSuffix(dataImageFilesInfo.getFileName());
        if(!ArrayUtil.containsIgnoreCase(MimeTypeUtils.IMAGE_EXTENSION,suffix)){
            log.info(dataImageFilesInfo.getFileName()+":非增值税纸质发票图片，不识别查验");
            return dataImageFilesInfo;
        }
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("file");
        log.info(dataImageFilesInfo.getFileName()+"：NCC增值税发票识别接口请求报文："+ JSONObject.toJSONString(uploadTaxRequest));
        UploadTaxInvoiceResponse uploadTaxResponse = NccOcrRecognitionService.getTaxInvoiceInfo(uploadTaxRequest, token, uploadInvoiceForNCCRequest.getParamProperties());
        log.info(dataImageFilesInfo.getFileName()+"：NCC增值税发票识别接口返回报文："+JSONObject.toJSONString(uploadTaxResponse));
        UploadTaxInvoiceResponseData taxResponseData = uploadTaxResponse.getData();
        if(StrUtil.equals(NcCodeEnum.NC_NOT_OCR_INFO.getCode(),taxResponseData.getCode())){
            // 未识别到ocr信息的发票设置为保存失败
            throw new Exception("业务系统OCR识别失败："+NcCodeEnum.NC_NOT_OCR_INFO.getCodeName());
        }else if(StrUtil.equals(NcCodeEnum.NC_SUCCESS_STATE.getCode(),taxResponseData.getCode())){
            // 获取OCR对象并存储
            DataOcrInfo invoiceInfo = taxResponseData.getTaxInvoiceInfo(dataImageFilesInfo.getFileId());
            dataOcrService.ocrInsertOrUpdateByBaseEntity(InvoiceConstants.TAX_INVOICE,invoiceInfo);
            dataImageFilesInfo.setFileType(InvoiceConstants.TAX_INVOICE);
            dataImageFilesInfo.insertOrUpdate();
        }else{
            // NCC OCR接口返回未知错误
            throw new Exception("业务系统OCR识别失败："+taxResponseData.getMsg());
        }
        return dataImageFilesInfo;
    }

}
