package org.smartlink.web.ncc.nccverify.dispose;//package org.smartlink.web.ncc.nccverify.dispose;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.collection.ListUtil;
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSONObject;
//
//import com.datafly.nc.ncc.nccstand.dispose.NccTaxStandDisposeInvoiceService;
//import com.datafly.nc.ncc.syncocr.NccSyncTaxInvoiceService;
//import com.datafly.nc.ncc.syncocr.request.allinvoice.SyncAllInvoiceRequest;
//import com.datafly.nc.ncc.syncocr.request.taxinvoice.SyncTaxInvoiceRequest;
//import com.datafly.nc.ncc.syncocr.request.taxinvoice.SyncTaxInvoiceRequestData;
//import com.datafly.nc.ncc.syncocr.response.allinvoice.SyncAllInvoiceResponse;
//import com.datafly.nc.ncc.syncocr.response.taxinvoice.SyncTaxInvoiceResponse;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.smartlink.common.mybatis.core.domain.BaseEntity;
//import org.smartlink.common.redis.utils.RedisUtils;
//import org.smartlink.web.constant.InvoiceConstants;
//import org.smartlink.web.constant.NcConstant;
//import org.smartlink.web.ncc.nccverify.NccTaxVerifyService;
//import org.smartlink.web.ncc.nccverify.request.VerifyTaxInvoiceRequest;
//import org.smartlink.web.ncc.nccverify.request.VerifyTaxInvoiceRequestData;
//import org.smartlink.web.ncc.nccverify.response.VerifyDataItem;
//import org.smartlink.web.ncc.nccverify.response.VerifyTaxInvoiceResponse;
//import org.smartlink.web.ncc.nccverify.response.VerifyTaxInvoiceResponseData;
//import org.smartlink.web.ncc.nccverify.response.VerifyTaxInvoiceResponseDataInvoice;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @description: NCC业务逻辑处理类
// * @author: chenJiangHong
// * @create: 2022-06-12 23:16
// **/
//@Slf4j
//@Component
//@AllArgsConstructor
//public class NccTaxVerifyDisposeInvoiceService {
//
//    private IDataOcrInfoService dataOcrInfoService;
//    private IDataOcrService dataOcrService;
//    private NccTaxStandDisposeInvoiceService standDisposeInvoiceService;
//    private IDataOcrDetailsService dataOcrDetailsService;
//    private IDataFlightsService dataFlightsService;
//
//    /**
//     * NCC查验逻辑业务方法
//     * @param uploadInvoiceForNCCRequest NCC业务所需参数
//     * @param token NCC token
//     * @return 结果
//     */
//    public DataImageFilesInfo verifyTaxInfoBusiness(UploadInvoiceForNccRequest uploadInvoiceForNCCRequest,Token token) throws Exception {
//        DataImageFilesInfo dataImageFilesInfo = uploadInvoiceForNCCRequest.getDataImageFilesInfo();
//        // 走查验保存台账方法
//        if(StrUtil.equals(InvoiceConstants.INVOICE_MUCH_NCC,dataImageFilesInfo.getFileType())){
//            // 多票据文件查验保存台账逻辑
//            List<String> fileTypeList = StrUtil.split(dataImageFilesInfo.getIncludeTypeArr(), Constants.CONNECT_COMMA_SYMBOL);
//            Boolean includeErrorInvoice = false;
//            List<BaseEntity> newBaseEntityLit = new ArrayList<>();
//            for (String invoiceType : fileTypeList) {
//                List<BaseEntity> baseEntityList = dataOcrService.queryInvoiceInfoByTypeAndFileId(invoiceType, dataImageFilesInfo.getFileId());
//                if(CollectionUtil.isNotEmpty(baseEntityList) && baseEntityList.size()>0){
//                    for (BaseEntity baseEntity : baseEntityList) {
//                        baseEntity.setOcrFileType(invoiceType);
//                        newBaseEntityLit.add(baseEntity);
//                    }
//                }
//            }
//            if(CollectionUtil.isNotEmpty(newBaseEntityLit) && newBaseEntityLit.size()>0){
//                // 对newBaseEntityLit集合进行重复过滤
//                List<BaseEntity> finalBaseEntityLit = new ArrayList<>();
//                List<String> idList = new ArrayList<>();
//                for (BaseEntity baseEntity : newBaseEntityLit) {
//                    Field idObj = baseEntity.getClass().getDeclaredField("id");
//                    idObj.setAccessible(true);
//                    String id = (String) idObj.get(baseEntity);
//                    if(!idList.contains(id)){
//                        finalBaseEntityLit.add(baseEntity);
//                        idList.add(id);
//                    }
//                }
//                if(CollectionUtil.isNotEmpty(finalBaseEntityLit) && finalBaseEntityLit.size()>0){
//                    for (BaseEntity baseEntity : finalBaseEntityLit) {
//                        try {
//                            Class<?> baseEntityClass = baseEntity.getClass();
//                            Field field = baseEntityClass.getDeclaredField("pushBusinessInfoFlag");
//                            field.setAccessible(true);
//                            String pushBusinessInfoFlag = (String)field.get(baseEntity);
//                            // 没推过台账的数据才能请求接口
//                            if(!StrUtil.equals(pushBusinessInfoFlag, NcConstant.ALL_INVOICE_INTERFACE)){
//                                dataImageFilesInfo = this.verifyTaxInfoForNCC(baseEntity.getOcrFileType(),baseEntity,uploadInvoiceForNCCRequest,token);
//                            }
//                        } catch (Exception e) {
//                            log.error("多票据文件查验保存台账失败："+ ExceptionUtil.getExceptionMessage(e));
//                            baseEntity.setCheckResult("出现异常："+e.getLocalizedMessage());
//                            baseEntity.updateById();
//                            includeErrorInvoice = true;
//                        }
//                    }
//                }
//            }
//            if(includeErrorInvoice){
//                dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
//                dataImageFilesInfo.setMessage("多票据文件中包含状态异常发票，双击查看错误详情");
//            }
//        }else{
//            // 单个文件查验保存台账逻辑
//            if(!StrUtil.equals(dataImageFilesInfo.getFileType(),InvoiceConstants.IMAGE_OTHERS)){
//                List<BaseEntity> baseEntityList = dataOcrService.queryInvoiceInfoByTypeAndFileId(dataImageFilesInfo.getFileType(), dataImageFilesInfo.getFileId());
//                if(CollectionUtil.isNotEmpty(baseEntityList) && baseEntityList.size()>0){
//                    BaseEntity baseEntity = baseEntityList.get(0);
//                    try {
//                        dataImageFilesInfo = this.verifyTaxInfoForNCC(dataImageFilesInfo.getFileType(),baseEntity,uploadInvoiceForNCCRequest,token);
//                    }catch (Exception e){
//                        log.error("单文件查验保存台账失败："+ExceptionUtil.getExceptionMessage(e));
//                        baseEntity.setCheckResult("出现异常："+e.getLocalizedMessage());
//                        baseEntity.updateById();
//                        dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
//                        dataImageFilesInfo.setMessage(e.getLocalizedMessage());
//                    }
//                }
//            }
//        }
//        return dataImageFilesInfo;
//    }
//
//
//    public DataImageFilesInfo verifyTaxInfoForNCC(String invoiceType,BaseEntity baseEntity,UploadInvoiceForNccRequest uploadInvoiceForNCCRequest,Token token) throws Exception {
//        DataImageFilesInfo dataImageFilesInfo = uploadInvoiceForNCCRequest.getDataImageFilesInfo();
//        String interFaceType = uploadInvoiceForNCCRequest.getParamProperties().getInterFaceType();
//        // 增值税验真
//        if(NcConstant.TAX_INVOICE_LIST.contains(invoiceType)){
//            // 成功识别出增值税发票信息后进行发票查验逻辑
//            VerifyTaxInvoiceRequest verifyTaxInvoiceRequest;
//            try {
//                verifyTaxInvoiceRequest = this.getVerifyTaxInvoiceRequest(invoiceType,baseEntity,uploadInvoiceForNCCRequest, token);
//            } catch (Exception e) {
//                log.error("获取业务系统查验接口请求参数异常："+ExceptionUtil.getExceptionMessage(e));
//                throw new Exception("获取业务系统查验接口请求参数异常："+e.getLocalizedMessage());
//            }
//            log.info("业务系统发票验真接口请求报文："+JSONObject.toJSONString(verifyTaxInvoiceRequest));
//            VerifyTaxInvoiceResponse verifyTaxInvoiceResponse;
//            try {
//                verifyTaxInvoiceResponse = NccTaxVerifyService.verifyTaxInvoiceInfo(verifyTaxInvoiceRequest, token, uploadInvoiceForNCCRequest.getParamProperties());
//            } catch (Exception e) {
//                log.error("业务系统发票验真接口出现异常："+ExceptionUtil.getExceptionMessage(e));
//                throw new Exception("业务系统发票验真接口出现异常："+e.getLocalizedMessage());
//            }
//            log.info("业务系统发票验真接口返回报文："+JSONObject.toJSONString(verifyTaxInvoiceResponse));
//            List<VerifyTaxInvoiceResponseData> verifyTaxInvoiceResponseData = verifyTaxInvoiceResponse.getData();
//            if(ObjectUtil.isNotEmpty(verifyTaxInvoiceResponseData)){
//                VerifyTaxInvoiceResponseData responseData = verifyTaxInvoiceResponseData.get(0);
//                // 查验成功后返回的票面信息，需存库
//                VerifyTaxInvoiceResponseDataInvoice invoice = responseData.getInvoice();
//                // NCC查验接口返回的发票类型
//                String responseFileType = invoice.getFplx();
//                String fileType = NcTypeConvertUtil.getSystemFileType(responseFileType);
//                //NCC识别、查验成功后 需删除塞进OcrInfo表的旧数据
//                dataOcrInfoService.deleteByFileId(dataImageFilesInfo.getFileId());
//                if(!StrUtil.equals(InvoiceConstants.INVOICE_MUCH_NCC,dataImageFilesInfo.getFileType())){
//                    dataImageFilesInfo.setFileType(fileType);
//                }
//                // 根据发票类型转成对应的实体对象
//                baseEntity = this.convertVerifyInvoiceInfo(responseData.getSaveToken(),fileType,dataImageFilesInfo.getFileId(),baseEntity, invoice);
//                try {
//                    // dataOcrService.ocrUpdateByBaseEntity(fileType,baseEntity);
//                    dataOcrService.ocrInsertOrUpdateByBaseEntity(fileType,baseEntity);
//                } catch (Exception e) {
//                    log.error("存储OCR数据发生异常："+ExceptionUtil.getExceptionMessage(e));
//                    throw new Exception("存储OCR数据发生异常："+e.getLocalizedMessage());
//                }
//                // 推送结构化数据到业务系统台账
//                dataImageFilesInfo = standDisposeInvoiceService.invoiceInfoStandBusiness(uploadInvoiceForNCCRequest,invoiceType,baseEntity,token);
//            }
//        }else{
//            // 判断是否走2207全票种接口，是的话需要走保存台账接口
//            if(StrUtil.equals(interFaceType,NcConstant.ALL_INVOICE_INTERFACE)){
//                // 如果是从手动修改入口或手动转发票入口的非增值税发票则需要先调用【同步ocr信息接口来获取token】
//                if(StrUtil.equals(uploadInvoiceForNCCRequest.getUploadBusinessType(),NcConstant.MANUAL_UPLOAD_INVOICE)){
//                    String tokenStr = this.getVerifyTokenFromNcc(uploadInvoiceForNCCRequest,token,invoiceType,baseEntity);
//                    baseEntity = this.updateSaveToken(tokenStr,invoiceType,baseEntity);
//                }
//                // 推送结构化数据到业务系统台账
//                dataImageFilesInfo = standDisposeInvoiceService.invoiceInfoStandBusiness(uploadInvoiceForNCCRequest,invoiceType,baseEntity,token);
//            }
//        }
//        return dataImageFilesInfo;
//    }
//
//    /**
//     * 更新saveToken参数
//     * @param saveToken
//     * @param invoiceType
//     * @param baseEntity
//     */
//    private BaseEntity updateSaveToken(String saveToken,String invoiceType,BaseEntity baseEntity){
//        switch (invoiceType){
//            //  增值税票种组装台账保存JSON参数
//            case InvoiceConstants.TAX_SPECIAL_INVOICE:
//            case InvoiceConstants.TAX_INVOICE:
//            case InvoiceConstants.ROLL_TICKET:
//            case InvoiceConstants.ELECTRONIC_INVOICE:
//            case InvoiceConstants.ELECTRONIC_OFD_INVOICE:
//            case InvoiceConstants.INVOICE_OTHERS:
//                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
//                dataOcrInfo.setSaveToken(saveToken);
//                dataOcrInfo.updateById();
//                return dataOcrInfo;
//            case InvoiceConstants.MOTOR_VEHICLE_SALE:
//                DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseEntity;
//                dataMotorVehicleSale.setSaveToken(saveToken);
//                dataMotorVehicleSale.updateById();
//                return dataMotorVehicleSale;
//            //  定额发票
//            case InvoiceConstants.QUOTA_INVOICE:
//                DataQuotaInvoice dataQuotaInvoice = (DataQuotaInvoice) baseEntity;
//                dataQuotaInvoice.setSaveToken(saveToken);
//                dataQuotaInvoice.updateById();
//                return dataQuotaInvoice;
//            //  机打发票
//            case InvoiceConstants.AIRCRAFT_INVOICE:
//                DataAircraftInvoice dataAircraftInvoice = (DataAircraftInvoice) baseEntity;
//                dataAircraftInvoice.setSaveToken(saveToken);
//                dataAircraftInvoice.updateById();
//                return dataAircraftInvoice;
//            //  出租车发票
//            case InvoiceConstants.TAXI_TICKETS:
//                DataTaxiTickets dataTaxiTickets = (DataTaxiTickets) baseEntity;
//                dataTaxiTickets.setSaveToken(saveToken);
//                dataTaxiTickets.updateById();
//                return dataTaxiTickets;
//            //  火车票
//            case InvoiceConstants.RAILWAY_TICKET:
//                DataRailwayTicket dataRailwayTicket = (DataRailwayTicket) baseEntity;
//                dataRailwayTicket.setSaveToken(saveToken);
//                dataRailwayTicket.updateById();
//                return dataRailwayTicket;
//            //  客运汽车票
//            case InvoiceConstants.PASSENGER_TICKET:
//                DataPassengerTicket dataPassengerTicket = (DataPassengerTicket) baseEntity;
//                dataPassengerTicket.setSaveToken(saveToken);
//                dataPassengerTicket.updateById();
//                return dataPassengerTicket;
//            //  航空运输电子客票行程单
//            case InvoiceConstants.FLIGHT_ITINERARY:
//                DataFlightItinerary dataFlightItinerary = (DataFlightItinerary) baseEntity;
//                dataFlightItinerary.setSaveToken(saveToken);
//                dataFlightItinerary.updateById();
//                return dataFlightItinerary;
//            //  过路费
//            case InvoiceConstants.TOLL_ROADS:
//                DataTollRoads dataTollRoads = (DataTollRoads) baseEntity;
//                dataTollRoads.setSaveToken(saveToken);
//                dataTollRoads.updateById();
//                return dataTollRoads;
//            default:
//                DataOcrInfo ocrInfo = (DataOcrInfo) baseEntity;
//                ocrInfo.setSaveToken(saveToken);
//                ocrInfo.updateById();
//                return ocrInfo;
//        }
//    }
//
//    /**
//     * 根据发票类型转成对应的实体
//     * @param verifyToken 查验接口返回token
//     * @param fileType 发票类型
//     * @param fileId 文件id
//     * @param baseEntity ocr对象
//     * @param invoice 接口返回invoice对象
//     * @return 转换结果
//     */
//    private BaseEntity convertVerifyInvoiceInfo(String verifyToken,String fileType,String fileId,BaseEntity baseEntity,VerifyTaxInvoiceResponseDataInvoice invoice){
//        if(StrUtil.equals(fileType,InvoiceConstants.MOTOR_VEHICLE_SALE)){
//            // 转为机动车发票
//            DataMotorVehicleSale motorVehicleSale = new DataMotorVehicleSale();
//            motorVehicleSale.setFileId(fileId);
//            motorVehicleSale.setBuyerId(invoice.getGmfMc());
//            motorVehicleSale.setBuyerName(invoice.getGmfNsrsbh());
//            motorVehicleSale.setSeller(invoice.getXsfMc());
//            motorVehicleSale.setSellerTaxid(invoice.getXsfNsrsbh());
//            motorVehicleSale.setSellerBankAccount(invoice.getXsfYhzh());
//            motorVehicleSale.setSellerAddress(invoice.getXsfDzdh());
//            motorVehicleSale.setCarCode(invoice.getFpDm());
//            motorVehicleSale.setPreTaxAmount(Convert.toBigDecimal(invoice.getHjje()));
//            motorVehicleSale.setInvoiceCode(invoice.getFpDm());
//            motorVehicleSale.setInvoiceDate(Convert.toDate(invoice.getKprq()));
//            motorVehicleSale.setInvoiceNumber(invoice.getFpHm());
//            motorVehicleSale.setInvoiceTotal(Convert.toBigDecimal(invoice.getHjje()));
//            motorVehicleSale.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
//            motorVehicleSale.setSaveToken(verifyToken);
//            motorVehicleSale.setCheckResult("查验成功");
//            return motorVehicleSale;
//        }else{
//            DataOcrInfo ocrInfo = (DataOcrInfo) baseEntity;
//            ocrInfo.setFileId(fileId);
//            ocrInfo.setSaveToken(verifyToken);
//            ocrInfo.setInvoiceNumber(invoice.getFpHm());
//            ocrInfo.setInvoiceCode(invoice.getFpDm());
//            ocrInfo.setSumAmount(Convert.toBigDecimal(invoice.getHjje()));
//            ocrInfo.setPretaxAmount(Convert.toBigDecimal(invoice.getHjje()));
//            ocrInfo.setInvoiceDate(Convert.toDate(invoice.getKprq()));
//            ocrInfo.setCheckCode(invoice.getJym());
//            ocrInfo.setBuyerAddress(invoice.getGmfDzdh());
//            ocrInfo.setBuyerName(invoice.getGmfMc());
//            ocrInfo.setBuyerNo(invoice.getGmfNsrsbh());
//            ocrInfo.setBuyerAccount(invoice.getGmfYhzh());
//            ocrInfo.setTotalLowercase(Convert.toBigDecimal(invoice.getJshj()));
//            ocrInfo.setTotalUppercase(MoneyUtil.change(invoice.getJshj()));
//            ocrInfo.setSumTax(Convert.toBigDecimal(invoice.getHjse()));
//            ocrInfo.setIssuer(invoice.getKpr());
//            ocrInfo.setPayee(invoice.getSkr());
//            ocrInfo.setSellerAddress(invoice.getXsfDzdh());
//            ocrInfo.setSellerName(invoice.getXsfMc());
//            ocrInfo.setSellerNo(invoice.getXsfNsrsbh());
//            ocrInfo.setSellerAccount(invoice.getXsfYhzh());
//            ocrInfo.setMachineCode(invoice.getJqbh());
//            ocrInfo.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
//            ocrInfo.setCheckResult("查验成功");
//            ocrInfo.setRemark(invoice.getBz());
//
//            // 转换发票明细
//            List<VerifyDataItem> items = invoice.getItems();
//            if(ObjectUtil.isNotEmpty(items)){
//                List<DataOcrDetails> detailsList = new ArrayList<>();
//                items.forEach(e -> {
//                    DataOcrDetails ocrDetails = new DataOcrDetails();
//                    ocrDetails.setDetailAmount(Convert.toBigDecimal(e.getXmje()));
//                    ocrDetails.setDetailsCount(Convert.toBigDecimal(e.getXmsl()));
//                    ocrDetails.setFileId(fileId);
//                    ocrDetails.setName(e.getXmmc());
//                    ocrDetails.setPrice(Convert.toBigDecimal(e.getXmdj()));
//                    ocrDetails.setTaxRate(e.getSl());
//                    ocrDetails.setStandard(e.getGgxh());
//                    ocrDetails.setTax(Convert.toBigDecimal(e.getSe()));
//                    ocrDetails.setUnit(e.getDw());
//                    detailsList.add(ocrDetails);
//                });
//                ocrInfo.setDetails(detailsList);
//            }
//            return ocrInfo;
//        }
//    }
//
//    /**
//     * 组装增值税发票验真请求参数
//     * @param uploadInvoiceForNccRequest
//     * @param token
//     * @return
//     * @throws Exception
//     */
//    private VerifyTaxInvoiceRequest getVerifyTaxInvoiceRequest(String invoiceType,BaseEntity baseEntity,UploadInvoiceForNccRequest uploadInvoiceForNccRequest,Token token) throws Exception {
//        VerifyTaxInvoiceRequest verifyTaxInvoiceRequest = new VerifyTaxInvoiceRequest();
//        verifyTaxInvoiceRequest.setBilltype(uploadInvoiceForNccRequest.getBillType());
//        verifyTaxInvoiceRequest.setTransitype(uploadInvoiceForNccRequest.getPkBillType());
//        verifyTaxInvoiceRequest.setDatasource(uploadInvoiceForNccRequest.getParamProperties().getDataSource());
//        verifyTaxInvoiceRequest.setFactorycode(uploadInvoiceForNccRequest.getParamProperties().getFactoryCode());
//        //verifyTaxInvoiceRequest.setPk_org(uploadInvoiceForNCCRequest.getPk_org());
//        verifyTaxInvoiceRequest.setOrgCode(uploadInvoiceForNccRequest.getOrgCode());
//        verifyTaxInvoiceRequest.setUserid(uploadInvoiceForNccRequest.getUserId());
//        verifyTaxInvoiceRequest.setData(ListUtil.of(this.getVerifyTaxInvoiceRequestData(invoiceType,baseEntity,uploadInvoiceForNccRequest,token)));
//        return verifyTaxInvoiceRequest;
//    }
//
//
//    private VerifyTaxInvoiceRequestData getVerifyTaxInvoiceRequestData(String invoiceType, BaseEntity invoiceInfo, UploadInvoiceForNccRequest uploadInvoiceForNccRequest, Token token) throws Exception {
//        boolean ocrOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_OFF));
//        String verifyToken = "";
//        // 判断是走NCC OCR接口还是走数影OCR接口,数影OCR接口的话查验需先调用同步OCR信息接口
//        if(ocrOff || StrUtil.equals(NcConstant.MANUAL_UPLOAD_INVOICE,uploadInvoiceForNccRequest.getUploadBusinessType())){
//            verifyToken = this.getVerifyTokenFromNcc(uploadInvoiceForNccRequest,token,invoiceType,invoiceInfo);
//        }
//        VerifyTaxInvoiceRequestData verifyTaxInvoiceRequestData = new VerifyTaxInvoiceRequestData();
//        if(invoiceInfo instanceof DataMotorVehicleSale){
//            DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) invoiceInfo;
//            verifyTaxInvoiceRequestData.setFpHm(dataMotorVehicleSale.getInvoiceNumber());
//            verifyTaxInvoiceRequestData.setFpDm(dataMotorVehicleSale.getInvoiceCode());
//            verifyTaxInvoiceRequestData.setBillid(uploadInvoiceForNccRequest.getBillId());
//            verifyTaxInvoiceRequestData.setKprq(DateUtil.format(dataMotorVehicleSale.getInvoiceDate(),"yyyyMMdd"));
//            verifyTaxInvoiceRequestData.setHjje(Convert.toStr(dataMotorVehicleSale.getPreTaxAmount()));
//            if(StrUtil.isNotEmpty(verifyToken)){
//                verifyTaxInvoiceRequestData.setVerifyToken(verifyToken);
//            }else{
//                verifyTaxInvoiceRequestData.setVerifyToken(dataMotorVehicleSale.getSaveToken());
//            }
//            verifyTaxInvoiceRequestData.setJshj("");
//            verifyTaxInvoiceRequestData.setJym("");
//        }else{
//            DataOcrInfo dataOcrInfo = (DataOcrInfo) invoiceInfo;
//            verifyTaxInvoiceRequestData.setFpHm(dataOcrInfo.getInvoiceNumber());
//            verifyTaxInvoiceRequestData.setFpDm(dataOcrInfo.getInvoiceCode());
//            verifyTaxInvoiceRequestData.setBillid(uploadInvoiceForNccRequest.getBillId());
//            verifyTaxInvoiceRequestData.setKprq(DateUtil.format(dataOcrInfo.getInvoiceDate(),"yyyyMMdd"));
//            if(StrUtil.equals(InvoiceConstants.ROLL_TICKET,invoiceType)){
//                verifyTaxInvoiceRequestData.setHjje(Convert.toStr(dataOcrInfo.getTotalLowercase()));
//            }else{
//                verifyTaxInvoiceRequestData.setHjje(Convert.toStr(dataOcrInfo.getSumAmount()));
//            }
//            if(StrUtil.equals(invoiceType,InvoiceConstants.ELECTRONIC_INVOICE) || StrUtil.equals(invoiceType,InvoiceConstants.ELECTRONIC_OFD_INVOICE)){
//                verifyTaxInvoiceRequestData.setJshj(Convert.toStr(dataOcrInfo.getTotalLowercase()));
//            }
//            verifyTaxInvoiceRequestData.setJym(dataOcrInfo.getCheckCode());
//            if(StrUtil.isNotEmpty(verifyToken)){
//                verifyTaxInvoiceRequestData.setVerifyToken(verifyToken);
//            }else{
//                verifyTaxInvoiceRequestData.setVerifyToken(dataOcrInfo.getSaveToken());
//            }
//        }
//        return verifyTaxInvoiceRequestData;
//    }
//
//    /**
//     * 获取查验请求token 若数据库有则读库，否则请求NCC接口获取token
//     * @param uploadInvoiceForNccRequest
//     * @param token
//     * @param type
//     * @param baseEntity
//     * @return
//     * @throws Exception
//     */
//    private String getVerifyTokenFromNcc(UploadInvoiceForNccRequest uploadInvoiceForNccRequest,Token token,String type,BaseEntity baseEntity) throws Exception {
//        String tokenStr = "";
//        NccParamProperties paramProperties = uploadInvoiceForNccRequest.getParamProperties();
//        // 根据接口类型参数调用不同的NCC获取token接口
//        if(StrUtil.equals(paramProperties.getInterFaceType(), NcConstant.ALL_INVOICE_INTERFACE)){
//            // 全票种接口获取token
//            SyncAllInvoiceRequest syncAllInvoiceRequest = new SyncAllInvoiceRequest();
//            syncAllInvoiceRequest.setUserid(uploadInvoiceForNccRequest.getUserId());
//            syncAllInvoiceRequest.setOrgCode(uploadInvoiceForNccRequest.getOrgCode());
//            syncAllInvoiceRequest.setDatasource(paramProperties.getDataSource());
//            syncAllInvoiceRequest.setFactorycode(paramProperties.getFactoryCode());
//            syncAllInvoiceRequest.setBilltype(uploadInvoiceForNccRequest.getBillType());
//            syncAllInvoiceRequest.setTransitype(uploadInvoiceForNccRequest.getPkBillType());
//            syncAllInvoiceRequest.setBillid(uploadInvoiceForNccRequest.getBillId());
//            JSONObject requestData = standDisposeInvoiceService.getRequestData(type, uploadInvoiceForNccRequest.getUploadBusinessType(), baseEntity, uploadInvoiceForNccRequest.getDataImageFilesInfo());
//            syncAllInvoiceRequest.setData(ListUtil.of(requestData));
//            log.info("数影调用业务系统全票种同步ocr信息接口请求报文："+JSONObject.toJSONString(syncAllInvoiceRequest));
//            SyncAllInvoiceResponse syncAllInvoiceResponse = NccSyncTaxInvoiceService.syncAllInvoiceOcrResult(syncAllInvoiceRequest, token, paramProperties);
//            log.info("数影调用业务系统全票种同步ocr信息接口返回报文："+JSONObject.toJSONString(syncAllInvoiceResponse));
//            tokenStr = syncAllInvoiceResponse.getData().getDatas().get(0).getString("token");
//        }else{
//            // 增值税票种接口获取token
//            SyncTaxInvoiceRequest syncTaxInvoiceRequest = new SyncTaxInvoiceRequest();
//            syncTaxInvoiceRequest.setUserid(uploadInvoiceForNccRequest.getUserId());
//            syncTaxInvoiceRequest.setOrgCode(uploadInvoiceForNccRequest.getOrgCode());
//            syncTaxInvoiceRequest.setDatasource(paramProperties.getDataSource());
//            syncTaxInvoiceRequest.setFactorycode(paramProperties.getFactoryCode());
//            syncTaxInvoiceRequest.setBilltype(uploadInvoiceForNccRequest.getBillType());
//            syncTaxInvoiceRequest.setTransitype(uploadInvoiceForNccRequest.getPkBillType());
//            SyncTaxInvoiceRequestData syncTaxInvoiceRequestData = new SyncTaxInvoiceRequestData();
//            syncTaxInvoiceRequestData.setBillid(uploadInvoiceForNccRequest.getBillId());
//            if(StrUtil.equals(type,InvoiceConstants.MOTOR_VEHICLE_SALE)){
//                DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseEntity;
//                syncTaxInvoiceRequestData.setFpDm(dataMotorVehicleSale.getInvoiceCode());
//                syncTaxInvoiceRequestData.setFpHm(dataMotorVehicleSale.getInvoiceNumber());
//                syncTaxInvoiceRequestData.setKprq(Convert.toStr(dataMotorVehicleSale.getInvoiceDate()));
//                syncTaxInvoiceRequestData.setHjje(Convert.toStr(dataMotorVehicleSale.getPreTaxAmount()));
//                syncTaxInvoiceRequestData.setJym("");
//            }else{
//                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
//                syncTaxInvoiceRequestData.setFpDm(dataOcrInfo.getInvoiceCode());
//                syncTaxInvoiceRequestData.setFpHm(dataOcrInfo.getInvoiceNumber());
//                syncTaxInvoiceRequestData.setKprq(Convert.toStr(dataOcrInfo.getInvoiceDate()));
//                if(StrUtil.isNotEmpty(Convert.toStr(dataOcrInfo.getSumAmount()))){
//                    syncTaxInvoiceRequestData.setHjje(Convert.toStr(dataOcrInfo.getSumAmount()));
//                }else{
//                    syncTaxInvoiceRequestData.setHjje(Convert.toStr(dataOcrInfo.getTotalLowercase()));
//                }
//                syncTaxInvoiceRequestData.setJym(dataOcrInfo.getCheckCode());
//            }
//            syncTaxInvoiceRequest.setData(ListUtil.of(syncTaxInvoiceRequestData));
//            log.info("影像系统调用业务系统增值税获取同步OCR信息接口请求参数："+JSONObject.toJSONString(syncTaxInvoiceRequest));
//            SyncTaxInvoiceResponse syncTaxInvoiceResponse = NccSyncTaxInvoiceService.syncOcrResult(syncTaxInvoiceRequest, token, paramProperties);
//            log.info("影像系统调用业务系统增值税获取同步OCR信息接口返回报文："+JSONObject.toJSONString(syncTaxInvoiceResponse));
//            tokenStr = syncTaxInvoiceResponse.getData().get(0).getVerifyToken();
//        }
//        return tokenStr;
//    }
//
//}
