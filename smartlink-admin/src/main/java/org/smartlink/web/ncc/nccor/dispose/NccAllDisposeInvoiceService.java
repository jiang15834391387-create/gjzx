package org.smartlink.web.ncc.nccor.dispose;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.web.constant.Constants;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.dto.UploadInvoiceForNccRequest;
import org.smartlink.web.enums.NcCodeEnum;
import org.smartlink.web.ncc.nccor.NccOcrRecognitionService;
import org.smartlink.web.ncc.nccor.request.allinvoice.UploadAllInvoiceRequest;
import org.smartlink.web.ncc.nccor.request.allinvoice.UploadAllInvoiceRequestData;
import org.smartlink.web.ncc.nccor.response.allinvoice.UploadAllInvoiceResponse;
import org.smartlink.web.ncc.nccor.response.allinvoice.UploadAllInvoiceResponseData;
import org.smartlink.web.ocr.service.bean.IdentificationData;
import org.smartlink.web.ocr.service.yesfp.YesfpConverter;
import org.smartlink.web.service.nc.IDataOcrService;
import org.smartlink.web.token.response.Token;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

/**
 * @author L
 * @Description NCC 增值税发票逻辑处理类
 * @create:
 */
@Slf4j
@Component
@AllArgsConstructor
public class NccAllDisposeInvoiceService {

//    private NccTaxVerifyDisposeInvoiceService nccVerifyDisposeInvoiceService;
    private IDataOcrService dataOcrService;

    /**
     * NCC全票种发票上传处理逻辑
     * @param uploadInvoiceForNCCRequest 请求实体对象
     * @param token NCC token
     * @return DataImageFilesInfo
     */
    public DataImageFilesInfo getAllInvoiceInfo(UploadInvoiceForNccRequest uploadInvoiceForNCCRequest, Token token) throws Exception {
        DataImageFilesInfo dataImageFilesInfo = uploadInvoiceForNCCRequest.getDataImageFilesInfo();
        UploadAllInvoiceRequest uploadAllInvoiceRequest = UploadAllInvoiceRequest.builder()
                .billtype(uploadInvoiceForNCCRequest.getBillType())
                .datasource(uploadInvoiceForNCCRequest.getParamProperties().getDataSource())
                .factorycode(uploadInvoiceForNCCRequest.getParamProperties().getFactoryCode())
                //.pk_org(uploadInvoiceForNCCRequest.getPk_org())
                .orgCode(uploadInvoiceForNCCRequest.getOrgCode())
                .transitype(uploadInvoiceForNCCRequest.getPkBillType())
                .userid(uploadInvoiceForNCCRequest.getUserId())
                .billid(uploadInvoiceForNCCRequest.getBillId())
                .data(UploadAllInvoiceRequestData.builder()
                        .file(uploadInvoiceForNCCRequest.getFile())
                        .build())
                .build();
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("file");
        log.info(dataImageFilesInfo.getFileName()+"：NCC全票种识别接口请求报文："+ JSONObject.toJSONString(uploadAllInvoiceRequest,filter));
        UploadAllInvoiceResponse uploadAllInvoiceResponse = NccOcrRecognitionService.getAllInvoiceInfo(uploadAllInvoiceRequest, token, uploadInvoiceForNCCRequest.getParamProperties());
        log.info(dataImageFilesInfo.getFileName()+"：NCC全票种发票识别接口返回报文："+JSONObject.toJSONString(uploadAllInvoiceResponse));
        UploadAllInvoiceResponseData responseData = uploadAllInvoiceResponse.getData();
        String code = responseData.getCode();
        String msg = responseData.getMsg();
        if(StrUtil.equals(NcCodeEnum.NC_NOT_OCR_INFO.getCode(), code)){
            // 未识别到ocr信息的发票设置为保存失败
            throw new Exception("业务系统OCR识别失败："+NcCodeEnum.NC_NOT_OCR_INFO.getCodeName());
        }else if(StrUtil.equals(NcCodeEnum.NC_SUCCESS_STATE.getCode(), code)){
            String message = "不需要OCR服务";
            if(StrUtil.contains(msg, message)){
                return dataImageFilesInfo;
            }
            // 获取OCR对象并存储
            JSONArray datas = responseData.getDatas();
            List<IdentificationData> identificationDataList = YesfpConverter.convertInvoiceInfo(dataImageFilesInfo,datas);
            if(identificationDataList.size() > 1){
                log.info("多票据文件识别ocr条数："+identificationDataList.size());
                // 多OCR信息数据处理
                dataImageFilesInfo.setFileType(InvoiceConstants.INVOICE_MUCH_NCC);
                HashSet<String> typeSet = new HashSet<>();
                for (int i = 0; i < identificationDataList.size(); i++) {
                    IdentificationData identificationData = identificationDataList.get(i);
                    typeSet.add(identificationData.k);
                    log.info(i+"   key:"+identificationData.k+" value:"+identificationData.t);
                    //保存ocr信息
                    dataOcrService.ocrInsertBaseEntity(identificationData.k,identificationData.t);
                }
                String typeArr = String.join(Constants.CONNECT_COMMA_SYMBOL,typeSet);
                dataImageFilesInfo.setIncludeTypeArr(typeArr);
            }else if(identificationDataList.size() == 1){
                IdentificationData identificationData = identificationDataList.get(0);
                dataImageFilesInfo.setFileType(identificationData.k);
                //保存ocr信息
                dataOcrService.ocrInsertOrUpdateByBaseEntity(identificationData.k,identificationData.t);
            }
            dataImageFilesInfo.insertOrUpdate();
        }else{
            // NCC OCR接口返回未知错误
            throw new Exception("业务系统OCR识别失败："+ msg);
        }
        return dataImageFilesInfo;
    }

}
