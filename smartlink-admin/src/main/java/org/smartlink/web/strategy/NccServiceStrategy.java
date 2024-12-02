package org.smartlink.web.strategy;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.smartlink.common.core.utils.SpringUtils;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.web.constant.*;
import org.smartlink.web.domain.DataCurrentTask;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.SysDept;
import org.smartlink.web.domain.dto.*;
import org.smartlink.web.domain.imagefilesinfo.DataImageTree;
import org.smartlink.web.domain.invoice.DataMotorVehicleSale;
import org.smartlink.web.domain.invoice.DataOcrDetails;
import org.smartlink.web.domain.invoice.DataOcrInfo;
import org.smartlink.web.domain.modle.BaseEntity;
import org.smartlink.web.enums.NcCodeEnum;
import org.smartlink.web.exception.NCServiceException;
import org.smartlink.web.ncc.deleteocr.NccDeleteOcrService;
import org.smartlink.web.ncc.deleteocr.request.allinvoice.DeleteAllInvoiceData;
import org.smartlink.web.ncc.deleteocr.request.allinvoice.DeleteAllInvoiceRequest;
import org.smartlink.web.ncc.deleteocr.request.allinvoice.DeleteElectronicInvoiceData;
import org.smartlink.web.ncc.deleteocr.request.allinvoice.DeleteElectronicInvoiceRequest;
import org.smartlink.web.ncc.deleteocr.request.taxinvoice.DeleteElectronicNonStandardRequest;
import org.smartlink.web.ncc.deleteocr.request.taxinvoice.DeleteElectronicNonStandardRequestData;
import org.smartlink.web.ncc.deleteocr.request.taxinvoice.DeleteTaxInvoiceData;
import org.smartlink.web.ncc.deleteocr.request.taxinvoice.DeleteTaxInvoiceRequest;
import org.smartlink.web.ncc.deleteocr.response.DeleteInvoiceResponse;
import org.smartlink.web.ncc.nccor.NccOcrRecognitionService;
import org.smartlink.web.ncc.nccor.dispose.NccAllDisposeInvoiceService;
import org.smartlink.web.ncc.nccor.dispose.NccTaxDisposeInvoiceService;
import org.smartlink.web.ncc.nccor.request.allinvoice.UploadElectronicInvoiceRequest;
import org.smartlink.web.ncc.nccor.request.allinvoice.UploadElectronicInvoiceRequestData;
import org.smartlink.web.ncc.nccor.request.taxinvoice.UploadElectronicInvoiceNonStandardRequest;
import org.smartlink.web.ncc.nccor.response.allinvoice.UploadElectronicInvoiceResponse;
import org.smartlink.web.ncc.nccor.response.taxinvoice.UploadElectronicInvoiceNonStandardResponse;
import org.smartlink.web.ncc.nccor.response.taxinvoice.UploadElectronicInvoiceNonStandardResponseData;
import org.smartlink.web.ncc.nccverify.dispose.NccTaxVerifyDisposeInvoiceService;
import org.smartlink.web.properties.NcProperties;
import org.smartlink.web.properties.NccParamProperties;
import org.smartlink.web.service.nc.*;
import org.smartlink.web.token.NccToken;
import org.smartlink.web.token.request.NccTokenRequest;
import org.smartlink.web.token.response.Token;
import org.smartlink.web.utils.ExceptionUtil;
import org.smartlink.web.utils.NcTypeConvertUtil;
import org.smartlink.web.utils.file.MimeTypeUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: NCC端业务实现类
 * @author: L
 * @create:
 **/
@Component
@Slf4j
public class NccServiceStrategy extends AbstractNCStrategy{

    private NccParamProperties paramProperties = new NccParamProperties();
    private IDataImageFilesInfoService dataImageFilesInfoService;
    private IDataImageTreeService imageTreeService;
    private IDataCurrentTaskService dataCurrentTaskService;
    private ISysDeptService sysDeptService;
    private IDataOcrService dataOcrService;
    private NccTaxVerifyDisposeInvoiceService nccVerifyDisposeInvoiceService;
    private NcService ncService;
    @Override
    public void deleteNcInvoiceDataBusinessService(NcDeleteServiceDTO ncDeleteServiceDTO) throws Exception {
        DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(ncDeleteServiceDTO.getBusinessSerialNo());
        // 判断NCC开关是否启用
        String status = NcProperties.getDefaultPropertiesStatus();
        Boolean nccCheck = NcProperties.getDefaultPropertiesInfo().getBoolean("nccCheck");
        String interFaceType = NcProperties.getDefaultPropertiesInfo().getString("interFaceType");
        // NCC OCR查验开关开启才代表发票是保存过NCC台账的
        if(StrUtil.equals(status,Constants.CONFIG_OFF_STATUS) && nccCheck){
            NccTokenRequest nccTokenRequest = new NccTokenRequest();
            nccTokenRequest.setNccUserName(paramProperties.getNccUserName());
            nccTokenRequest.setNccPassword(paramProperties.getNccPassword());
            nccTokenRequest.setBaseUrl(paramProperties.getBaseUrl());
            nccTokenRequest.setBizCenter(paramProperties.getBizCenter());
            nccTokenRequest.setClientId(paramProperties.getClientId());
            nccTokenRequest.setClientSecret(paramProperties.getClientSecret());
            List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectDataImageFilesInfoListByFileIdList(ncDeleteServiceDTO.getFileIdList());
            List<DataImageTree> dataImageTreeList = imageTreeService.selectDataImageTreeByFileIdList(ncDeleteServiceDTO.getFileIdList());
            List<DataImageTree> filterDataImageTreeList = dataImageTreeList.stream().filter(o -> StrUtil.equals(InvoiceConstants.YOUBAOZHANG_DOC, o.getParentId()) || StrUtil.equals(InvoiceConstants.YOUBAOZHANG_IMG, o.getParentId())).collect(Collectors.toList());
            // 如果包含费用管理上传的发票，则此次删除操作失败
            if(CollectionUtil.isNotEmpty(filterDataImageTreeList) && filterDataImageTreeList.size()>0){
                throw new Exception("此次删除包含友费控或费用管理所上传文件，删除失败");
            }
            if(CollectionUtil.isNotEmpty(dataImageFilesInfoList) && dataImageFilesInfoList.size()>0){
                if(StrUtil.equals(NcConstant.ALL_INVOICE_INTERFACE,interFaceType)){
                    // 电子发票的发票类型则需要调用特殊的电子发票删除接口
                    List<DataImageFilesInfo> electronicImageList = dataImageFilesInfoList.stream().filter((o) -> StrUtil.equals(InvoiceConstants.ELECTRONIC_INVOICE,o.getFileType())||StrUtil.equals(InvoiceConstants.ELECTRONIC_OFD_INVOICE,o.getFileType())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(electronicImageList) && electronicImageList.size()>0){
                        Token token = this.getToken();
                        // 电子发票删除
                        for (DataImageFilesInfo imageFilesInfo : electronicImageList) {
                            BaseEntity baseEntity = dataOcrService.ocrQueryByFileId(imageFilesInfo.getFileId());
                            if(ObjectUtil.isNotEmpty(baseEntity)){
                                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
                                if(StrUtil.equals(dataOcrInfo.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                                    DeleteElectronicInvoiceRequest deleteElectronicInvoiceRequest = new DeleteElectronicInvoiceRequest();
                                    deleteElectronicInvoiceRequest.setDatasource(paramProperties.getDataSource());
                                    deleteElectronicInvoiceRequest.setFactorycode(paramProperties.getFactoryCode());
                                    deleteElectronicInvoiceRequest.setUuid(IdUtil.fastUUID());
                                    deleteElectronicInvoiceRequest.setBillid(ncDeleteServiceDTO.getBusinessSerialNo());
                                    deleteElectronicInvoiceRequest.setUserid(dataCurrentTask.getUserId());
                                    deleteElectronicInvoiceRequest.setPk_org(dataCurrentTask.getOrgCode());
                                    deleteElectronicInvoiceRequest.setBilltype(dataCurrentTask.getBillType());
                                    deleteElectronicInvoiceRequest.setTransitype(dataCurrentTask.getPkBillType());
                                    deleteElectronicInvoiceRequest.setData(DeleteElectronicInvoiceData.builder().fphm(dataOcrInfo.getInvoiceNumber()).fpdm(StrUtil.isNotEmpty(dataOcrInfo.getInvoiceCode())?dataOcrInfo.getInvoiceCode():"").build());
                                    log.info("全票种电子发票删除台账请求报文："+JSONObject.toJSONString(deleteElectronicInvoiceRequest));
                                    DeleteInvoiceResponse deleteInvoiceResponse = NccDeleteOcrService.deleteElectronicInvoiceData(deleteElectronicInvoiceRequest, token, paramProperties);
                                    log.info("全票种电子发票删除台账返回报文:"+JSONObject.toJSONString(deleteInvoiceResponse));
                                }
                            }
                        }
                    }
                    // 全票种删除(不包含电子发票102)
                    List<DataImageFilesInfo> allInvoiceImageList = dataImageFilesInfoList.stream().filter(o->NcConstant.ALL_INVOICE_LIST.contains(o.getFileType())).filter(o->!StrUtil.equals(InvoiceConstants.ELECTRONIC_INVOICE,o.getFileType())).collect(Collectors.toList());
                    List<String> allInvoiceList = NcConstant.ALL_INVOICE_LIST.stream().filter(o -> !StrUtil.equals(o, InvoiceConstants.ELECTRONIC_INVOICE) && !StrUtil.equals(o, InvoiceConstants.ELECTRONIC_OFD_INVOICE) && !StrUtil.equals(o, InvoiceConstants.INVOICE_MUCH_NCC)).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(allInvoiceImageList) && allInvoiceImageList.size()>0){
                        List<BaseEntity> baseEntityList = new ArrayList<>();
                        for (DataImageFilesInfo imageFilesInfo : allInvoiceImageList) {
                            // 对多票据文件判断处理
                            if(StrUtil.equals(imageFilesInfo.getFileType(),InvoiceConstants.INVOICE_MUCH_NCC)){
                                List<String> fileTypeList = StrUtil.split(imageFilesInfo.getIncludeTypeArr(), Constants.CONNECT_COMMA_SYMBOL);
                                if(CollectionUtil.isNotEmpty(fileTypeList) && fileTypeList.size()>0){
                                    for (String fileType : fileTypeList) {
                                        List<BaseEntity> entityList = dataOcrService.queryInvoiceInfoByTypeAndFileId(fileType, imageFilesInfo.getFileId());
                                        if(CollectionUtil.isNotEmpty(entityList) && entityList.size()>0){
                                            baseEntityList.addAll(entityList);
                                        }
                                    }
                                }
                            }else{
                                if(allInvoiceList.contains(imageFilesInfo.getFileType())){
                                    BaseEntity baseEntity = dataOcrService.ocrQueryByFileId(imageFilesInfo.getFileId());
                                    if(ObjectUtil.isNotEmpty(baseEntity)){
                                        baseEntityList.add(baseEntity);
                                    }
                                }
                            }
                        }
                        if(CollectionUtil.isNotEmpty(baseEntityList) && baseEntityList.size()>0){
                            // 对baseEntityList集合进行重复过滤
                            List<BaseEntity> newBaseEntityList = new ArrayList<>();
                            List<String> idList = new ArrayList<>();
                            for (BaseEntity baseEntity : baseEntityList) {
                                Field idObj = baseEntity.getClass().getDeclaredField("id");
                                idObj.setAccessible(true);
                                String id = (String) idObj.get(baseEntity);
                                if(!idList.contains(id)){
                                    newBaseEntityList.add(baseEntity);
                                    idList.add(id);
                                }
                            }
                            List<DeleteAllInvoiceData> deleteAllInvoiceDataList = NccDeleteOcrService.getDeleteAllInvoiceDataByBaseEntity(newBaseEntityList);
                            if(deleteAllInvoiceDataList.size()>0){
                                Token token = this.getToken();
                                DeleteAllInvoiceRequest deleteAllInvoiceRequest = new DeleteAllInvoiceRequest();
                                deleteAllInvoiceRequest.setUserid(dataCurrentTask.getUserId());
                                deleteAllInvoiceRequest.setPk_org(dataCurrentTask.getOrgCode());
                                deleteAllInvoiceRequest.setDatasource(paramProperties.getDataSource());
                                deleteAllInvoiceRequest.setFactorycode(paramProperties.getFactoryCode());
                                deleteAllInvoiceRequest.setBilltype(dataCurrentTask.getBillType());
                                deleteAllInvoiceRequest.setTransitype(dataCurrentTask.getPkBillType());
                                deleteAllInvoiceRequest.setBillid(dataCurrentTask.getBusinessSerialNo());
                                deleteAllInvoiceRequest.setData(deleteAllInvoiceDataList);
                                log.info("全票种发票删除台账请求报文："+JSONObject.toJSONString(deleteAllInvoiceRequest));
                                DeleteInvoiceResponse deleteInvoiceResponse = NccDeleteOcrService.deleteNccAllInvoiceData(deleteAllInvoiceRequest, token, paramProperties);
                                log.info("全票种发票删除台账返回报文："+JSONObject.toJSONString(deleteInvoiceResponse));
                            }
                        }
                    }
                }else{
                    // 增值税票种删除
                    // 电子发票的发票类型则需要调用特殊的电子发票删除接口
                    List<DataImageFilesInfo> electronicImageList = dataImageFilesInfoList.stream().filter((o) -> StrUtil.equals(InvoiceConstants.ELECTRONIC_INVOICE,o.getFileType())||StrUtil.equals(InvoiceConstants.ELECTRONIC_OFD_INVOICE,o.getFileType())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(electronicImageList) && electronicImageList.size()>0){
                        Token token = this.getToken();
                        // 非标准电子发票删除逻辑
                        for (DataImageFilesInfo imageFilesInfo : electronicImageList) {
                            BaseEntity baseEntity = dataOcrService.ocrQueryByFileId(imageFilesInfo.getFileId());
                            if(ObjectUtil.isNotEmpty(baseEntity)){
                                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
                                if(StrUtil.equals(dataOcrInfo.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                                    DeleteElectronicNonStandardRequest deleteElectronicInvoiceRequest = new DeleteElectronicNonStandardRequest();
                                    deleteElectronicInvoiceRequest.setUserid(dataCurrentTask.getUserId());
                                    deleteElectronicInvoiceRequest.setPk_org(dataCurrentTask.getOrgCode());
                                    deleteElectronicInvoiceRequest.setFpDm(dataOcrInfo.getInvoiceCode());
                                    deleteElectronicInvoiceRequest.setFpHm(dataOcrInfo.getInvoiceNumber());
                                    deleteElectronicInvoiceRequest.setDatasource(paramProperties.getDataSource());
                                    deleteElectronicInvoiceRequest.setFactorycode(paramProperties.getFactoryCode());
                                    deleteElectronicInvoiceRequest.setBilltype(dataCurrentTask.getBillType());
                                    deleteElectronicInvoiceRequest.setTransitype(dataCurrentTask.getPkBillType());
                                    deleteElectronicInvoiceRequest.setData(DeleteElectronicNonStandardRequestData.builder().fpDm(dataOcrInfo.getInvoiceCode()).fpHm(dataOcrInfo.getInvoiceNumber()).build());
                                    log.info("非标准电子发票删除台账请求报文："+JSONObject.toJSONString(deleteElectronicInvoiceRequest));
                                    DeleteInvoiceResponse deleteInvoiceResponse = NccDeleteOcrService.deleteNonStandardElectronicInvoiceData(deleteElectronicInvoiceRequest, token, paramProperties);
                                    log.info("非标准电子发票删除台账返回报文:"+JSONObject.toJSONString(deleteInvoiceResponse));
                                }
                            }
                        }
                    }
                    List<DeleteTaxInvoiceData> dataList = new ArrayList<>();
                    // 获取发票类型为增值税发票的fileId集合 eq:不包含电子发票
                    List<DataImageFilesInfo> collect = dataImageFilesInfoList.stream().filter((o) -> NcConstant.TAX_INVOICE_LIST.contains(o.getFileType())).filter((o) -> !StrUtil.equals(o.getFileType(),InvoiceConstants.ELECTRONIC_INVOICE) && !StrUtil.equals(o.getFileType(),InvoiceConstants.ELECTRONIC_OFD_INVOICE)).collect(Collectors.toList());
                    for (DataImageFilesInfo dataImageFilesInfo : collect) {
                        BaseEntity baseEntity = dataOcrService.ocrQueryByFileId(dataImageFilesInfo.getFileId());
                        if(ObjectUtil.isNotEmpty(baseEntity)){
                            DeleteTaxInvoiceData data = new DeleteTaxInvoiceData();
                            if(baseEntity instanceof DataMotorVehicleSale){
                                DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseEntity;
                                if(StrUtil.equals(dataMotorVehicleSale.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                                    data.setFpDm(dataMotorVehicleSale.getInvoiceCode());
                                    data.setFpHm(dataMotorVehicleSale.getInvoiceNumber());
                                    data.setSaveToken(dataMotorVehicleSale.getSaveToken());
                                    dataList.add(data);
                                }
                            }else{
                                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
                                if(StrUtil.equals(dataOcrInfo.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                                    data.setFpDm(dataOcrInfo.getInvoiceCode());
                                    data.setFpHm(dataOcrInfo.getInvoiceNumber());
                                    data.setSaveToken(dataOcrInfo.getSaveToken());
                                    dataList.add(data);
                                }
                            }
                        }
                    }
                    // 调用NCC增值税发票删除台账接口
                    if(dataList.size() > 0){
                        Token token = this.getToken();
                        DeleteTaxInvoiceRequest deleteTaxInvoiceRequest = new DeleteTaxInvoiceRequest();
                        deleteTaxInvoiceRequest.setBilltype(dataCurrentTask.getBillType());
                        deleteTaxInvoiceRequest.setDatasource(paramProperties.getDataSource());
                        deleteTaxInvoiceRequest.setFactorycode(paramProperties.getFactoryCode());
                        deleteTaxInvoiceRequest.setTransitype(dataCurrentTask.getPkBillType());
                        deleteTaxInvoiceRequest.setPk_org(dataCurrentTask.getOrgCode());
                        deleteTaxInvoiceRequest.setUserid(dataCurrentTask.getUserId());
                        deleteTaxInvoiceRequest.setData(dataList);
                        log.info("增值税删除发票台账接口请求报文："+JSONObject.toJSONString(deleteTaxInvoiceRequest));
                        DeleteInvoiceResponse deleteInvoiceResponse = NccDeleteOcrService.deleteNccTaxInvoiceData(deleteTaxInvoiceRequest, token, paramProperties);
                        log.info("增值税删除发票台账接口返回报文"+JSONObject.toJSONString(deleteInvoiceResponse));
                    }
                }
            }
        }

    }

    /**
     * 获取业务系统token
     * @return
     * @throws Exception
     */
    private Token getToken() throws Exception {
        // 判断NCC开关是否启用
        String status = NcProperties.getDefaultPropertiesStatus();
        Boolean nccCheck = NcProperties.getDefaultPropertiesInfo().getBoolean("nccCheck");
        Token token = null;
        // NCC OCR查验开关开启才代表发票是保存过NCC台账的
        if(StrUtil.equals(status,Constants.CONFIG_OFF_STATUS) && nccCheck){
            NccTokenRequest nccTokenRequest = new NccTokenRequest();
            nccTokenRequest.setNccUserName(paramProperties.getNccUserName());
            nccTokenRequest.setNccPassword(paramProperties.getNccPassword());
            nccTokenRequest.setBaseUrl(paramProperties.getBaseUrl());
            nccTokenRequest.setBizCenter(paramProperties.getBizCenter());
            nccTokenRequest.setClientId(paramProperties.getClientId());
            nccTokenRequest.setClientSecret(paramProperties.getClientSecret());
            try {
                token = NccToken.getToken(nccTokenRequest);
            } catch (Exception e) {
                log.error("获取NCC token异常："+ExceptionUtil.getExceptionMessage(e));
                throw new Exception(e.getLocalizedMessage());
            }
        }
        return token;
    }
    @Override
    public DataCurrentTask submitTaskStateToBusinessService(TaskSubmitDTO taskSubmitDTO) throws Exception {
        return ncService.submitTaskStateForNc(taskSubmitDTO.getDataCurrentTask(),taskSubmitDTO.getUserId(),taskSubmitDTO.getSupplementaryScan());
    }

    @Override
    public DataCurrentTask rejectTaskStateBusinessService(UpdateTaskDTO updateTaskDTO) throws Exception {
        return ncService.rejectTaskStateForNc(updateTaskDTO.getDataCurrentTask(),updateTaskDTO.getUserId(),updateTaskDTO.getUpdateState());
    }

    @Override
    public DataImageFilesInfo doBusinessService(NcImageServiceDTO ncImageServiceDTO) {
        boolean nccOcr = Boolean.parseBoolean(paramProperties.getNccOcr());
        boolean nccCheck = Boolean.parseBoolean(paramProperties.getNccCheck());
        DataImageFilesInfo dataImageFilesInfo = ncImageServiceDTO.getDataImageFilesInfo();
        String businessSerialNo = ncImageServiceDTO.getBusinessSerialNo();
        Token token = null;
        log.info(dataImageFilesInfo.getFileName() + "：进入NCC业务逻辑处理！");
        DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(businessSerialNo);
        if (ObjectUtil.isEmpty(dataCurrentTask)) {
            dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
            dataImageFilesInfo.setMessage("对应单据不存在，问题流水号为" + businessSerialNo);
            return dataImageFilesInfo;
        }
        String orgCode = dataCurrentTask.getOrgCode();
        if(StrUtil.isNotEmpty(orgCode) && orgCode.length() > 15){
            SysDept sysDept = sysDeptService.selectDeptById(orgCode);
            if(ObjectUtil.isEmpty(sysDept)){
                dataImageFilesInfo.setMessage("未查询到组织机构信息，请先同步基础数据");
                dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                return dataImageFilesInfo;
            }else{
                orgCode = sysDept.getDeptCode();
            }
        }
        // 若调用NCC OPEN API接口，需先获取token
        if(nccOcr || nccCheck){
            NccTokenRequest tokenRequest = new NccTokenRequest();
            BeanUtil.copyProperties(paramProperties,tokenRequest);
            try {
                token = NccToken.getToken(tokenRequest);
            } catch (Exception e) {
                log.error(ExceptionUtil.getExceptionMessage(e));
                dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                dataImageFilesInfo.setMessage(e.getLocalizedMessage());
                return dataImageFilesInfo;
            }
        }
        // 2207以下版本如果是xml发票，则走修改ocr步骤逻辑
        if (!StrUtil.equals(NcConstant.ALL_INVOICE_INTERFACE, paramProperties.getInterFaceType()) && StrUtil.equalsAnyIgnoreCase(FileUtil.getSuffix(dataImageFilesInfo.getFileName()), MimeTypeUtils.DOCUMENT_XML_TRANSFOTMATION)) {
            ncImageServiceDTO.setUploadBusinessType(NcConstant.MANUAL_UPLOAD_INVOICE);
        }
        if (nccOcr && StrUtil.equals(NcConstant.NORMAL_UPLOAD_INVOICE, ncImageServiceDTO.getUploadBusinessType())) {
            //有发票信息的附件
            String[] invoice = ArrayUtil.addAll(MimeTypeUtils.DOCUMENT_PDF_TRANSFOTMATION,MimeTypeUtils.DOCUMENT_OFD_TRANSFOTMATION);
            String extension = FilenameUtils.getExtension(dataImageFilesInfo.getFileName());
            // 走NCC上传发票代码逻辑
            UploadInvoiceForNccRequest uploadInvoiceForNccRequest = this.getUploadInvoiceForNccRequest(dataCurrentTask, orgCode, dataImageFilesInfo, ncImageServiceDTO.getFile(), ncImageServiceDTO.getUploadBusinessType());
            // 请求接口类型，【0】走增值税发票识别接口，【1】走全票种识别接口
            if (StrUtil.equals(NcConstant.ALL_INVOICE_INTERFACE, paramProperties.getInterFaceType())) {
                // 2207增加上xml文件上传
                invoice = ArrayUtil.addAll(MimeTypeUtils.DOCUMENT_PDF_TRANSFOTMATION,MimeTypeUtils.DOCUMENT_OFD_TRANSFOTMATION,MimeTypeUtils.DOCUMENT_XML_TRANSFOTMATION);
                if (ArrayUtil.containsIgnoreCase(invoice, extension)) {
                    // 如果是PDF，OFD的话走NCC系统的上传电子发票接口
                    UploadElectronicInvoiceRequest uploadElectronicInvoiceRequest = new UploadElectronicInvoiceRequest();
                    uploadElectronicInvoiceRequest.setDatasource(uploadInvoiceForNccRequest.getParamProperties().getDataSource());
                    uploadElectronicInvoiceRequest.setFactorycode(uploadInvoiceForNccRequest.getParamProperties().getFactoryCode());
                    uploadElectronicInvoiceRequest.setUuid(IdUtil.simpleUUID());
                    uploadElectronicInvoiceRequest.setBillid(uploadInvoiceForNccRequest.getBillId());
                    uploadElectronicInvoiceRequest.setUserid(uploadInvoiceForNccRequest.getUserId());
                    uploadElectronicInvoiceRequest.setPk_org(uploadInvoiceForNccRequest.getPk_org());
                    uploadElectronicInvoiceRequest.setBilltype(uploadInvoiceForNccRequest.getBillType());
                    uploadElectronicInvoiceRequest.setTransitype(uploadInvoiceForNccRequest.getPkBillType());
                    UploadElectronicInvoiceRequestData uploadElectronicInvoiceRequestData = new UploadElectronicInvoiceRequestData();
                    uploadElectronicInvoiceRequestData.setFileName(dataImageFilesInfo.getFileName());
                    uploadElectronicInvoiceRequestData.setFileContent(uploadInvoiceForNccRequest.getFile());
                    uploadElectronicInvoiceRequest.setData(uploadElectronicInvoiceRequestData);
                    UploadElectronicInvoiceResponse uploadElectronicInvoiceResponse;
                    try {
                        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
                        filter.getExcludes().add("fileContent");
                        log.info(dataImageFilesInfo.getFileName() + ":业务系统电子发票保存台账接口请求报文:" + JSONObject.toJSONString(uploadElectronicInvoiceRequest,filter));
                        uploadElectronicInvoiceResponse = NccOcrRecognitionService.getElectronicInvoiceInfo(uploadElectronicInvoiceRequest, token, uploadInvoiceForNccRequest.getParamProperties());
                        log.info(dataImageFilesInfo.getFileName() + ":业务系统电子发票保存台账接口返回报文:" + JSONObject.toJSONString(uploadElectronicInvoiceResponse));
                        if(StrUtil.equals(uploadElectronicInvoiceResponse.getCode(), NcCodeEnum.NC_SUCCESS_STATE.getCode())){
                            // 查验成功后将OCR数据保存在数据库中
                            String data = uploadElectronicInvoiceResponse.getData();
                            JSONObject dataObject = JSON.parseObject(data);
                            String msg = dataObject.getString("msg");
                            String message = "不需要OCR服务";
                            if(StrUtil.contains(msg, message)){
                                return dataImageFilesInfo;
                            }
                            dataImageFilesInfo.setFileType(InvoiceConstants.ELECTRONIC_INVOICE);
                            dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);

                            dataImageFilesInfo = this.saveElectronicInvoiceInfo(dataObject,dataImageFilesInfo);
                            return dataImageFilesInfo;
                        }else{
                            // 状态不是0000的code 则设为保存失败
                            throw new NCServiceException("业务系统上传电子发票失败："+uploadElectronicInvoiceResponse.getMessage());
                        }
                    } catch (Exception e) {
                        log.error(dataImageFilesInfo.getFileName()+":业务系统电子发票保存台账接口出现异常："+ExceptionUtil.getExceptionMessage(e));
                        dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                        dataImageFilesInfo.setMessage(e.getLocalizedMessage());
                        return dataImageFilesInfo;
                    }
                }else{
                    try {
                        // NCC全票种发票上传逻辑
                        NccAllDisposeInvoiceService nccAllDisposeInvoiceService = SpringUtils.getBean(NccAllDisposeInvoiceService.class);
                        dataImageFilesInfo = nccAllDisposeInvoiceService.getAllInvoiceInfo(uploadInvoiceForNccRequest, token);
                    } catch (Exception e) {
                        log.error(dataImageFilesInfo.getFileName() + "NCC全票种发票识别接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
                        dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                        dataImageFilesInfo.setMessage(e.getLocalizedMessage());
                        return dataImageFilesInfo;
                    }
                }
            } else {
                boolean nc2111ServiceOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.ELECTRONIC_INVOICE_2111_SERVICE_OFF));
                // 如果开关没开启，则直接作为附件保存
                if(!nc2111ServiceOff){
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                    dataImageFilesInfo.setMessage("未启用电子发票上传模块，作为附件保存成功");
                    return dataImageFilesInfo;
                }
                if (ArrayUtil.containsIgnoreCase(invoice, extension)) {
                    // 如果是PDF，OFD的话走NCC系统的客开上传电子发票接口
                    UploadElectronicInvoiceNonStandardRequest uploadElectronicInvoiceNonStandardRequest = new UploadElectronicInvoiceNonStandardRequest();
                    uploadElectronicInvoiceNonStandardRequest.setPk_org(uploadInvoiceForNccRequest.getPk_org());
                    uploadElectronicInvoiceNonStandardRequest.setBillid(uploadInvoiceForNccRequest.getBillId());
                    uploadElectronicInvoiceNonStandardRequest.setBilltype(uploadInvoiceForNccRequest.getBillType());
                    uploadElectronicInvoiceNonStandardRequest.setTransitype(uploadInvoiceForNccRequest.getPkBillType());
                    uploadElectronicInvoiceNonStandardRequest.setFilename(uploadInvoiceForNccRequest.getDataImageFilesInfo().getFileName());
                    uploadElectronicInvoiceNonStandardRequest.setFilecontent(uploadInvoiceForNccRequest.getFile());
                    UploadElectronicInvoiceNonStandardResponse uploadElectronicInvoiceNonStandardResponse;
                    try {
                        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
                        filter.getExcludes().add("filecontent");
                        log.info(dataImageFilesInfo.getFileName() + ":业务系统非标准电子发票保存台账接口请求报文:" + JSONObject.toJSONString(uploadElectronicInvoiceNonStandardRequest,filter));
                        uploadElectronicInvoiceNonStandardResponse = NccOcrRecognitionService.getNonStandardElectronicInvoiceInfo(uploadElectronicInvoiceNonStandardRequest, token, uploadInvoiceForNccRequest.getParamProperties());
                        log.info(dataImageFilesInfo.getFileName() + ":业务系统非标准电子发票保存台账接口返回报文:" + JSONObject.toJSONString(uploadElectronicInvoiceNonStandardResponse));
                        if(StrUtil.equals(uploadElectronicInvoiceNonStandardResponse.getCode(), NcCodeEnum.NC_SUCCESS_STATE.getCode())){
                            dataImageFilesInfo.setFileType(InvoiceConstants.ELECTRONIC_INVOICE);
                            dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
                            // 查验成功后将OCR数据保存在数据库中
                            List<UploadElectronicInvoiceNonStandardResponseData> data = uploadElectronicInvoiceNonStandardResponse.getData();
                            if(CollectionUtil.isNotEmpty(data) && data.size()>0){
                                UploadElectronicInvoiceNonStandardResponseData uploadElectronicInvoiceNonStandardResponseData = data.get(0);
                                this.saveElectronicInvoiceInfo(uploadElectronicInvoiceNonStandardResponseData,dataImageFilesInfo.getFileId());
                                return dataImageFilesInfo;
                            }
                        }else{
                            // 状态不是0000的code 则设为保存失败
                            throw new NCServiceException("业务系统上传电子发票失败："+uploadElectronicInvoiceNonStandardResponse.getMessage());
                        }
                    }catch (Exception e) {
                        log.error(dataImageFilesInfo.getFileName()+":业务系统非标准电子发票保存台账接口出现异常："+ExceptionUtil.getExceptionMessage(e));
                        dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                        dataImageFilesInfo.setMessage(e.getLocalizedMessage());
                        return dataImageFilesInfo;
                    }
                }
                try {
                    // NCC增值税发票上传处理逻辑
                    NccTaxDisposeInvoiceService nccTaxDisposeInvoiceService = SpringUtils.getBean(NccTaxDisposeInvoiceService.class);
                    dataImageFilesInfo = nccTaxDisposeInvoiceService.getTaxInvoiceInfo(uploadInvoiceForNccRequest, token);
                } catch (Exception e) {
                    log.error(dataImageFilesInfo.getFileName() + "NCC增值税发票识别接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                    dataImageFilesInfo.setMessage(e.getLocalizedMessage());
                    return dataImageFilesInfo;
                }
            }
        }
        // 是否走NCC查验接口逻辑
        if(nccCheck){
            // 发票类型为增值税发票类型则走查验逻辑
            boolean taxCheckFlag = NcConstant.TAX_INVOICE_LIST.contains(dataImageFilesInfo.getFileType()) && !StrUtil.equals(NcConstant.ALL_INVOICE_INTERFACE, paramProperties.getInterFaceType());
            // List<String> excludeElectronicInvoiceList = NcConstant.ALL_INVOICE_LIST.stream().filter(o -> !StrUtil.equals(InvoiceConstants.ELECTRONIC_INVOICE, o)).collect(Collectors.toList());
            boolean allCheckFlag = NcConstant.ALL_INVOICE_LIST.contains(dataImageFilesInfo.getFileType()) && StrUtil.equals(NcConstant.ALL_INVOICE_INTERFACE, paramProperties.getInterFaceType());
            if(taxCheckFlag || allCheckFlag){
                UploadInvoiceForNccRequest uploadInvoiceForNccRequest = this.getUploadInvoiceForNccRequest(dataCurrentTask, orgCode, dataImageFilesInfo, ncImageServiceDTO.getFile(), ncImageServiceDTO.getUploadBusinessType());
                uploadInvoiceForNccRequest.setDataImageFilesInfo(dataImageFilesInfo);
                try {
                    dataImageFilesInfo = nccVerifyDisposeInvoiceService.verifyTaxInfoBusiness(uploadInvoiceForNccRequest,token);
                } catch (Exception e) {
                    log.error(dataImageFilesInfo.getFileName()+"走NCC发票验真保存台账出现异常："+ ExceptionUtil.getExceptionMessage(e));
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                    dataImageFilesInfo.setMessage(e.getLocalizedMessage());
                    return dataImageFilesInfo;
                }
            }
        }
        return dataImageFilesInfo;
    }

    /**
     * 获取request
     * @param dataCurrentTask
     * @param orgCode
     * @param dataImageFilesInfo
     * @param file
     * @return
     */
    private UploadInvoiceForNccRequest getUploadInvoiceForNccRequest(DataCurrentTask dataCurrentTask,String orgCode,DataImageFilesInfo dataImageFilesInfo,String file,String uploadBusinessType){
        return UploadInvoiceForNccRequest.builder()
            .billType(dataCurrentTask.getBillType())
            .pkBillType(dataCurrentTask.getPkBillType())
            .pk_org(dataCurrentTask.getOrgCode())
            .orgCode(orgCode)
            .userId(dataCurrentTask.getUserId())
            .billId(dataCurrentTask.getBusinessSerialNo())
            .dataImageFilesInfo(dataImageFilesInfo)
            .file(file)
            .paramProperties(paramProperties)
            .uploadBusinessType(uploadBusinessType)
            .build();
    }

    /**
     * 保存电子发票ocr数据
     * @param jsonObject
     * @param dataImageFilesInfo
     */
    public DataImageFilesInfo saveElectronicInvoiceInfo(JSONObject jsonObject,DataImageFilesInfo dataImageFilesInfo) throws Exception {
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject parent = data.getJSONObject("parent");
        JSONArray children = data.getJSONArray("children");
        JSONArray jsonArray = children.getJSONArray(0);
        String invoiceType = parent.getString("invoice_type");
        dataImageFilesInfo.setFileType(NcTypeConvertUtil.getSystemFileType(invoiceType));
        log.info("开始组装电子发票OCR数据");
        DataOcrInfo ocrInfo = new DataOcrInfo();
        ocrInfo.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
        ocrInfo.setId(IdUtil.simpleUUID());
        ocrInfo.setFileId(dataImageFilesInfo.getFileId());
        ocrInfo.setBuyerName(parent.getString("gmfmc"));
        ocrInfo.setSumAmount(Convert.toBigDecimal(parent.getString("hjje")));
        ocrInfo.setSellerAddress(parent.getString("xsfdzdh"));
        ocrInfo.setInvoiceDate(Convert.toDate(parent.getString("kprq")));
        ocrInfo.setSellerNo(parent.getString("xsfnsrsbh"));
        ocrInfo.setInvoiceCode(parent.getString("fpdm"));
        ocrInfo.setSumTax(Convert.toBigDecimal(parent.getString("hjse")));
        ocrInfo.setBuyerNo(parent.getString("gmfnsrsbh"));
        ocrInfo.setSellerName(parent.getString("xsfmc"));
        ocrInfo.setSellerAccount(parent.getString("xsfyhzh"));
        ocrInfo.setTotalLowercase(Convert.toBigDecimal(parent.getString("jshj")));
        ocrInfo.setInvoiceNumber(parent.getString("fphm"));
        ocrInfo.setCheckCode(parent.getString("jym"));
        ocrInfo.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
        log.info("组装电子发票OCR数据DataOcrInfo完成");
        List<DataOcrDetails> dataOcrDetailsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject detailJson = jsonArray.getJSONObject(i);
            DataOcrDetails ocrDetails = new DataOcrDetails();
            ocrDetails.setId(IdUtil.simpleUUID());
            ocrDetails.setFileId(dataImageFilesInfo.getFileId());
            ocrDetails.setTax(Convert.toBigDecimal(detailJson.getString("se")));
            ocrDetails.setName(detailJson.getString("xmmc"));
            ocrDetails.setUnit(detailJson.getString("dw"));
            ocrDetails.setDetailsCount(Convert.toBigDecimal(detailJson.getString("xmsl")));
            ocrDetails.setDetailAmount(Convert.toBigDecimal(detailJson.getString("xmje")));
            dataOcrDetailsList.add(ocrDetails);
        }
        ocrInfo.setDetails(dataOcrDetailsList);
        dataOcrService.ocrInsertOrUpdateByBaseEntity(InvoiceConstants.ELECTRONIC_INVOICE,ocrInfo);
        return dataImageFilesInfo;
    }

    /**
     * 2111以下保存电子发票ocr数据
     * @param data
     * @param fileId
     */
    public void saveElectronicInvoiceInfo(UploadElectronicInvoiceNonStandardResponseData data,String fileId) throws Exception {
        log.info("非电子发票接口开始组装电子发票OCR数据");
        DataOcrInfo ocrInfo = new DataOcrInfo();
        ocrInfo.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
        ocrInfo.setId(IdUtil.simpleUUID());
        ocrInfo.setFileId(fileId);
        ocrInfo.setBuyerName(data.getGmfmc());
        ocrInfo.setSumAmount(Convert.toBigDecimal(data.getHjje()));
        ocrInfo.setPretaxAmount(Convert.toBigDecimal(data.getHjje()));
        ocrInfo.setSellerAddress(data.getXsfdzdh());
        ocrInfo.setInvoiceDate(Convert.toDate(data.getKprq()));
        ocrInfo.setSellerNo(data.getXsfnsrsbh());
        ocrInfo.setInvoiceCode(data.getFpdm());
        ocrInfo.setSumTax(Convert.toBigDecimal(data.getHjse()));
        ocrInfo.setBuyerNo(data.getGmfnsrsbh());
        ocrInfo.setSellerName(data.getXsfmc());
        ocrInfo.setSellerAccount(data.getXsfyhzh());
        ocrInfo.setTotalLowercase(Convert.toBigDecimal(data.getJshj()));
        ocrInfo.setInvoiceNumber(data.getFphm());
        ocrInfo.setCheckCode(data.getJym());
        ocrInfo.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
        log.info("组装电子发票OCR数据DataOcrInfo完成");
//        List<DataOcrDetails> dataOcrDetailsList = new ArrayList<>();
//        for (int i = 0; i < jsonArray.size(); i++) {
//            JSONObject detailJson = jsonArray.getJSONObject(i);
//            DataOcrDetails ocrDetails = new DataOcrDetails();
//            ocrDetails.setId(IdUtil.simpleUUID());
//            ocrDetails.setFileId(fileId);
//            ocrDetails.setTax(Convert.toBigDecimal(detailJson.getString("se")));
//            ocrDetails.setName(detailJson.getString("xmmc"));
//            ocrDetails.setUnit(detailJson.getString("dw"));
//            ocrDetails.setDetailsCount(Convert.toBigDecimal(detailJson.getString("xmsl")));
//            ocrDetails.setDetailAmount(Convert.toBigDecimal(detailJson.getString("xmje")));
//            dataOcrDetailsList.add(ocrDetails);
//        }
//        ocrInfo.setDetails(dataOcrDetailsList);
        dataOcrService.ocrInsertOrUpdateByBaseEntity(InvoiceConstants.ELECTRONIC_INVOICE,ocrInfo);
    }
}
