package org.smartlink.web.service.nc.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.oss.factory.OssFactory;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.web.constant.*;
import org.smartlink.web.domain.*;
import org.smartlink.web.domain.dto.NcDeleteServiceDTO;
import org.smartlink.web.domain.invoice.bo.DataCurrentTaskBo;
import org.smartlink.web.enums.NcCodeEnum;
import org.smartlink.web.factory.NcConfigFactory;
import org.smartlink.web.ncc.testapi.TestApiService;
import org.smartlink.web.ncc.testapi.response.TestOpenApiResponse;
import org.smartlink.web.ncc.testapi.resqust.TestOpenApiRequest;
import org.smartlink.web.ncc.testapi.resqust.TestOpenApiRequestData;
import org.smartlink.web.properties.NcProperties;
import org.smartlink.web.properties.NccParamProperties;
import org.smartlink.web.service.nc.*;
import org.smartlink.web.token.NccToken;
import org.smartlink.web.token.request.NccTokenRequest;
import org.smartlink.web.token.response.Token;
import org.smartlink.web.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @description: NC系统调用业务类
 * @author: L
 * @create:
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class NcServiceImpl implements NcService {

    private final CallNcService callNcService;
    private final ISysUserService sysUserService;
    private final ISysDeptService deptService;
    private final IDataBillTypeService billTypeService;
    private final IDataCurrentTaskService dataCurrentTaskService;
    private final ExternalTokenService externalTokenService;
    private final IDataCmInfoService dataCmInfoService;
    private final IDataImageFilesInfoService dataImageFilesInfoService;
    private final IDataOcrService iDataOcrService;

    private List invoiceList = Arrays.asList(InvoiceConstants.TAX_SPECIAL_INVOICE,InvoiceConstants.TAX_INVOICE,InvoiceConstants.ELECTRONIC_INVOICE,InvoiceConstants.ROLL_TICKET,InvoiceConstants.ELECTRONIC_OFD_INVOICE,InvoiceConstants.ELECTRONIC_INVOICE_ITINERARY,InvoiceConstants.MOTOR_VEHICLE_SALE,InvoiceConstants.USED_CAR_SALES,InvoiceConstants.QUOTA_INVOICE,InvoiceConstants.AIRCRAFT_INVOICE,InvoiceConstants.TAXI_TICKETS,InvoiceConstants.RAILWAY_TICKET,InvoiceConstants.PASSENGER_TICKET,InvoiceConstants.FLIGHT_ITINERARY,InvoiceConstants.STEAMER_TICKET,InvoiceConstants.TOLL_ROADS,InvoiceConstants.RECEIPT,InvoiceConstants.ELECTRONIC_INVOICE_QUKUAILIAN,InvoiceConstants.INVOICE_MUCH_NCC,InvoiceConstants.INVOICE_OTHERS);

    @Autowired
    public TestOpenApiRequestData testOpenApiRequestData;
    @Autowired
    public NccTokenRequest nccTokenRequest;
    @Autowired
    public NccParamProperties nccParamProperties;
    @Autowired
    public SysUser sysUser;

    @Override
    public String testNcc() {
        log.info("测试影像系统与NCC业务系统 OPEN API接口联通性");
        Token token;
//        JSONObject propertiesInfo = NcProperties.getDefaultPropertiesInfo();
        NccTokenRequest tokenRequest = new NccTokenRequest();
        tokenRequest.setBaseUrl(nccTokenRequest.getBaseUrl());
        tokenRequest.setBizCenter(nccTokenRequest.getBizCenter());
        tokenRequest.setClientId(nccTokenRequest.getClientId());
        tokenRequest.setClientSecret(nccTokenRequest.getClientSecret());
        tokenRequest.setNccUserName(nccTokenRequest.getNccUserName());
        tokenRequest.setNccPassword(nccTokenRequest.getNccPassword());
        try {
            token = NccToken.getToken(tokenRequest);
        } catch (Exception e) {
            log.error("获取NCC token异常：:" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_FAIL_STATE.getCode(), e.getLocalizedMessage(), null);
        }
        log.info("测试接口返回token结果：" + token);
        if (ObjectUtil.isNotEmpty(token)) {
            TestOpenApiResponse testOpenApiResponse;
            try {
                TestOpenApiRequest testOpenApiRequest = new TestOpenApiRequest();
                TestOpenApiRequestData data = new TestOpenApiRequestData();
                data.setFactoryCode(testOpenApiRequestData.getFactoryCode());
                data.setMethodName(testOpenApiRequestData.getMethodName());
                testOpenApiRequest.setData(data);
                testOpenApiResponse = TestApiService.testOpenApi(token, nccTokenRequest.getBaseUrl(), nccTokenRequest.getClientId(), testOpenApiRequest);
            } catch (Exception e) {
                log.error("调用NCC OPEN API接口异常：" + ExceptionUtil.getExceptionMessage(e));
                return ResultUtil.getRespXML(NcCodeEnum.NC_FAIL_STATE.getCode(), e.getLocalizedMessage(), null);
            }
            Map<String, Object> dataResponse = new HashMap<>();
            dataResponse.put("data", testOpenApiResponse.getData().getData());
            String respXML = ResultUtil.getRespXML(NcCodeEnum.NC_SUCCESS_STATE.getCode(), NcCodeEnum.NC_SUCCESS_STATE.getCodeName(), dataResponse);
            log.info("影像系统接口-测试NCC接口连通性返回报文：" + respXML);
            return respXML;
        } else {
            log.error("测试NCC接口异常：获取Token为空");
            return ResultUtil.getRespXML(NcCodeEnum.NC_FAIL_STATE.getCode(), "获取Token为空", null);
        }
    }

    @Override
    @Transactional
    public R<Void> synchronizeUser() {
        String webUrl = testOpenApiRequestData.getBaseUrl() + testOpenApiRequestData.getWsUrl();
        List<SysUser> result;
        try {
            result = callNcService.synchronizeUser(testOpenApiRequestData.getFactoryCode(), testOpenApiRequestData.getDataSource(), webUrl);
        } catch (Exception e) {
            log.error("同步用户失败：" + ExceptionUtil.getExceptionMessage(e));
            return R.fail(e.getLocalizedMessage());
        }

        if (ObjectUtil.isNotEmpty(result)) {
            log.info("从业务系统同步过来的用户数量:" + result.size());
            /*List<SysUser> cacheList = RedisUtils.getCacheList(SystemDataConstants.SYS_USER_KEY);
            List<SysUser> userList = result.stream().filter(o->!cacheList.contains(o)).collect(Collectors.toList());
            Boolean insertAllExternalUser = userService.insertAllExternalUser(userList);
            if(insertAllExternalUser){
                userService.loadingDataCache();
            }*/
            Boolean aBoolean = sysUserService.deleteAllExternalUser();
            if (aBoolean) {
                sysUserService.insertAllExternalUser(result);
            }
        }
        return R.ok("基础用户同步成功");
    }

    @Override
    @Transactional
    public R<Void> synchronizeDepart() {
        String webUrl = testOpenApiRequestData.getBaseUrl() + testOpenApiRequestData.getWsUrl();
        List<SysDept> result;
        try {
            result = callNcService.synchronizeDepartment(testOpenApiRequestData.getFactoryCode(), testOpenApiRequestData.getDataSource(), webUrl);
        } catch (Exception e) {
            log.error("同步组织机构失败：" + ExceptionUtil.getExceptionMessage(e));
            return R.fail(e.getLocalizedMessage());
        }

        if (ObjectUtil.isNotEmpty(result)) {
            log.info("从业务系统同步过来的组织机构数量：" + result.size());
            /*List<SysDept> cacheList = RedisUtils.getCacheList(SystemDataConstants.SYS_DEPT_KEY);
            List<SysDept> deptList = result.stream().filter(o->!cacheList.contains(o)).collect(Collectors.toList());
            Boolean insertAllExternalDept = deptService.insertAllExternalDept(deptList);
            if(insertAllExternalDept){
                deptService.loadingDataCache();
            }*/
            Boolean aBoolean = deptService.deleteAllExternalDept();
            if (aBoolean) {
                deptService.insertAllExternalDept(result);
            }
        }
        return R.ok("组织机构同步成功");
    }

    @Override
    @Transactional
    public R<Void> synchronizeBillType() {
        log.info("开始同步业务系统单据类型");
        String webUrl = testOpenApiRequestData.getBaseUrl() + testOpenApiRequestData.getWsUrl();
        List<DataBillType> result;
        try {
            result = callNcService.synchronizeBillType(testOpenApiRequestData.getFactoryCode(), testOpenApiRequestData.getDataSource(), webUrl);
        } catch (Exception e) {
            log.error("同步业务单据类型失败：" + ExceptionUtil.getExceptionMessage(e));
            return R.fail(e.getLocalizedMessage());
        }

        if (ObjectUtil.isNotEmpty(result)) {
            log.info("从业务系统同步的单据类型数量：" + result.size());
            // 6.21 暂注
            /*List<DataBillType> cacheList = RedisUtils.getCacheList(SystemDataConstants.BILL_TYPE_KEY);
            List<DataBillType> dataBillTypeList = result.stream().filter(o->!cacheList.contains(o)).collect(Collectors.toList());
            Boolean allExternalBillType = billTypeService.insertAllExternalBillType(dataBillTypeList);
            if(allExternalBillType){
                billTypeService.loadingDataCache();
            }*/
            List<DataBillType> dataBillTypes = billTypeService.selectAll();
            List<String> typeCodes = dataBillTypes.stream().map(DataBillType::getTypeCode).collect(Collectors.toList());
            List<DataBillType> newBillTypes = result.stream().filter(e -> !typeCodes.contains(e.getTypeCode())).collect(Collectors.toList());
            log.info("增量同步数量为：" + newBillTypes.size());
            billTypeService.insertAllExternalBillType(newBillTypes);

        }
        return R.ok("单据类型同步成功");
    }

    @Override
    public String getCurrentTaskCount(String xml) {
        log.info("NC获取用户代办任务数量接口请求报文：" + xml);
        String code;
        String message;
        Map<String, Object> dataResponse = null;
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("获取用户代办任务数量接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }
        Element rootElement = document.getRootElement();
        // xml数据格式校验
        boolean xmlDataVerify = XmlUtil.xmlDataVerify(rootElement);
        if (xmlDataVerify) {
            dataResponse = new HashMap<>();
            Element billBody = rootElement.element("BillBody");
            String userId = billBody.elementText("userid");
            String userNo = billBody.elementText("UserNo");
            List<DataCurrentTask> dataCurrentTaskList = dataCurrentTaskService.getTaskListByUserId(userId);
            dataResponse.put("UserId", userId);
            dataResponse.put("UserNo", userNo);
            dataResponse.put("CurrentTaskCount", ObjectUtil.isNotEmpty(dataCurrentTaskList) ? dataCurrentTaskList.size() : 0);
            code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
            message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
        } else {
            code = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCode();
            message = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCodeName();
        }
        String respXML = ResultUtil.getRespXML(code, message, dataResponse);
        log.info("影像系统接口-获取用户代办任务数量接口成功返回报文：" + respXML);
        return respXML;
    }

    @Override
    public String addScanTask(String xml) {
        log.info("NC添加影像任务接口请求报文：" + xml);
        String code;
        String message;
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("添加影像任务接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }

        Element rootElement = document.getRootElement();
        // xml数据格式校验
        boolean xmlDataVerify = XmlUtil.xmlDataVerify(rootElement);
        if (xmlDataVerify) {
            Element billBody = rootElement.element("BillBody");
            Element bill = billBody.element("Bill");
            String clientCode = rootElement.element("ReqHead").elementText("clientcode").trim();
            String businessSerialNo = bill.elementText("Busi_Serial_No");
            String billCode = bill.elementText("BillCode");
            String billType = bill.elementText("BillType");
            String scanType = bill.elementText("ScanType");
            String pkBillType = bill.elementText("pk_billtype");
            Element appImages = bill.element("appimages");
            if (StrUtil.isEmpty(scanType)) {
                scanType = ScanTypeConstants.BATCH_SCAN;
            }
            DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(businessSerialNo);
            List<DataBillType> dataBillTypeList = billTypeService.listDataBillTypeByTypeCode(billType);
            boolean taskIsExists = ObjectUtil.isNotEmpty(dataCurrentTask);
            dataCurrentTask = !taskIsExists ? new DataCurrentTask() : dataCurrentTask;
            String taskState = taskIsExists ? dataCurrentTask.getTaskState() : TaskStateConstants.TASK_STATE_SCAN;
            dataCurrentTask.setScanTypeName(StrUtil.equals(scanType, ScanTypeConstants.SINGLE_SCAN) ? "单笔扫描" : "批量扫描");
            dataCurrentTask.setScanType(scanType);
            dataCurrentTask.setBusinessSerialNo(businessSerialNo);
            dataCurrentTask.setBillNum(billCode);
            dataCurrentTask.setBillType(pkBillType);
            dataCurrentTask.setPkBillType(billType);
            String tradeTypeName = CollectionUtil.isNotEmpty(dataBillTypeList) && dataBillTypeList.size() > 0 ? dataBillTypeList.get(0).getTypeName() : "未知业务单据";
            dataCurrentTask.setBillTypeName(tradeTypeName);
            dataCurrentTask.setTradeTypeName(tradeTypeName);
            dataCurrentTask.setBillDate(Convert.toDate(bill.elementText("BillDate")));
            dataCurrentTask.setCash(bill.elementText("Cash"));
            dataCurrentTask.setUserId(bill.elementText("userid"));
            dataCurrentTask.setGroupId(bill.elementText("DetailInfo"));
            dataCurrentTask.setOrgCode(bill.elementText("OrgNo"));
            dataCurrentTask.setOrgName(bill.elementText("OrgName"));
            dataCurrentTask.setOcrType(bill.elementText("ocrType") != null ? bill.elementText("ocrType") : "0");
            dataCurrentTask.setTaskState(taskState);
            dataCurrentTask.setSystemCode(clientCode);
            dataCurrentTaskService.insertOrUpdateDataCurrentTaskByBusinessSerialNo(dataCurrentTask);
            // 如果appImages有数据，则为友报帐调用接口
            if (ObjectUtil.isNotEmpty(appImages)) {
                this.disposeImages(appImages, businessSerialNo, billType);
            }
            code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
            message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
        } else {
            code = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCode();
            message = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCodeName();
        }
        String respXML = ResultUtil.getRespXML(code, message, null);
        log.info("影像系统接口-添加影像任务接口成功返回报文：" + respXML);
        return respXML;
    }


    @Override
    public String deleteScanTask(String xml) {
        log.info("NC废弃影像任务请求报文：" + xml);
        String code;
        String message;
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("废弃影像任务接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }
        Element rootElement = document.getRootElement();
        // xml数据格式校验
        boolean xmlDataVerify = XmlUtil.xmlDataVerify(rootElement);
        if (xmlDataVerify) {
            // 与NC业务系统相关逻辑开启，则需要调用删除NC台账接口
            boolean ncEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_NC_BUSINESS));
            boolean bipEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_BIP_BUSINESS));
            Element billBody = rootElement.element("BillBody");
            Element service = billBody.element("service");
            String businessSerialNo = service.elementText("Busi_Serial_No");
            DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(businessSerialNo);
            // 废弃任务需删除：1.任务表数据 2.中间表数据 3.图片表数据 4.发票信息表数据 5.源文件数据
            if (ObjectUtil.isNotEmpty(dataCurrentTask)) {
                try {
                    List<DataCmInfo> dataCmInfoList = dataCmInfoService.selectDataCmInfoByBusinessSerialNo(businessSerialNo);
                    for (DataCmInfo dataCmInfo : dataCmInfoList) {
                        List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectByBatchId(dataCmInfo.getBatchId());
                        List<String> fileIdList = dataImageFilesInfoList.stream().map(DataImageFilesInfo::getFileId).collect(Collectors.toList());
                        if (ncEnabled || bipEnabled) {
                            NcDeleteServiceDTO ncDeleteServiceDTO = new NcDeleteServiceDTO();
                            ncDeleteServiceDTO.setFileIdList(fileIdList);
                            ncDeleteServiceDTO.setBusinessSerialNo(businessSerialNo);
                            NcConfigFactory.instance().deleteNcInvoiceDataBusinessService(ncDeleteServiceDTO);
                        }
                        if (CollectionUtil.isNotEmpty(fileIdList)) {
                            // 删除OCR信息
                            this.iDataOcrService.deleteMultipleFile(fileIdList);
                        }
                        for (DataImageFilesInfo dataImageFilesInfo : dataImageFilesInfoList) {
                            // 删除影像文件
                            if (StrUtil.isNotEmpty(dataImageFilesInfo.getIurl())) {
                                OssFactory.instance().delete(dataImageFilesInfo.getIurl());
                            }
                            if (StrUtil.isNotEmpty(dataImageFilesInfo.getLurl())) {
                                OssFactory.instance().delete(dataImageFilesInfo.getLurl());
                            }
                            if (StrUtil.isNotEmpty(dataImageFilesInfo.getPurl())) {
                                OssFactory.instance().delete(dataImageFilesInfo.getPurl());
                            }
                            if (StrUtil.isNotEmpty(dataImageFilesInfo.getSurl())) {
                                OssFactory.instance().delete(dataImageFilesInfo.getSurl());
                            }
                            dataImageFilesInfo.deleteById();
                        }
                        dataCmInfo.deleteById();
                        dataCurrentTask.deleteById();
                    }
                } catch (Exception e) {
                    log.error("废弃影像任务接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
                }
            }
            code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
            message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
        } else {
            code = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCode();
            message = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCodeName();
        }
        String respXML = ResultUtil.getRespXML(code, message, null);
        log.info("影像系统接口-废弃影像任务接口成功返回报文：" + respXML);
        return respXML;
    }


    /**
     * 添加影像任务时处理·友报帐图片
     */
    private void disposeImages(Element element, String businessSerialNo, String pkBillType) {
        if (StrUtil.isNotBlank(pkBillType)) {
            if (pkBillType.startsWith("264")) {
                pkBillType = "264X";
            } else if (pkBillType.startsWith("263")) {
                pkBillType = "263X";
            } else if (pkBillType.startsWith("262")) {
                pkBillType = "262X";
            } else if (pkBillType.startsWith("261")) {
                pkBillType = "261X";
            } else if (pkBillType.startsWith("265")) {
                pkBillType = "265X";
            } else if (pkBillType.startsWith("F1")) {
                pkBillType = "F1";
            } else if (pkBillType.startsWith("F2")) {
                pkBillType = "F2";
            } else if (pkBillType.startsWith("F3")) {
                pkBillType = "F3";
            } else if (pkBillType.startsWith("F4")) {
                pkBillType = "F4";
            }
        }
    }

    @Override
    public String singleLogin(String xml) {
        log.info("NC用户单点登陆接口请求报文：" + xml);
        String code;
        String message;
        String systemIp = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_IP);
        String systemPort = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_PORT);
        Map<String, Object> dataResponse = null;
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("用户单点登陆接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }
        Element rootElement = document.getRootElement();
        // xml数据格式校验
        boolean xmlDataVerify = XmlUtil.xmlDataVerify(rootElement);
        if (xmlDataVerify) {
            dataResponse = new HashMap<>();
            Element billBody = rootElement.element("BillBody");
            String userNo = billBody.elementText("UserNo");
            String userId = billBody.elementText("userid").replace(">", "");
            String userName = billBody.elementText("userName");
            String businessSerialNo = billBody.elementText("Busi_Serial_No");
            String groupId = billBody.elementText("groupid");
            String barCode = billBody.elementText("barcode");
            String scanType = billBody.elementText("ScanType");
            String billType = billBody.elementText("BillType");
            // String pkBillType = billBody.elementText("pk_billtype");
            String pkBillType = billBody.elementText("pk_tradetype");
            String billNum = billBody.elementText("BillCode");
            String cash = billBody.elementText("Cash");
            String treeNode = billBody.elementText("treeNode");
            String isSupplementaryScanning = StrUtil.equals(billBody.elementText("isSupplementaryScanning"), "Y") ? billBody.elementText("isSupplementaryScanning") : "N";
            // 判断是否同步了用户
            List<SysUser> sysUserList = sysUserService.selectListByNcUserId(userId);
            if (CollectionUtil.isEmpty(sysUserList) || sysUserList.size() < 1) {
                code = NcCodeEnum.NC_NOT_SYNC_DATA.getCode();
                message = NcCodeEnum.NC_NOT_SYNC_DATA.getCodeName();
                String respXML = ResultUtil.getRespXML(code, message, dataResponse);
                log.info("影像系统接口-单点登录返回成功报文：" + respXML);
                return respXML;
            }
            // 判断是不是补扫状态，若是 则需手动添加影像任务至数据库
            if (StrUtil.equals(scanType, ScanTypeConstants.RE_PATCH_SCAN)) {
                log.info(scanType + ":单点登录接口补扫状态下逻辑处理");
                DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(businessSerialNo);
                // 如果单据任务不存在 则手动添加至数据库中
                if (ObjectUtil.isEmpty(dataCurrentTask)) {
                    DataCurrentTask currentTask = new DataCurrentTask();
                    currentTask.setScanType(scanType);
                    currentTask.setBusinessSerialNo(businessSerialNo);
                    currentTask.setBillNum(billNum);
                    currentTask.setCash(cash);
                    currentTask.setBillType(billType);
                    currentTask.setPkBillType(pkBillType);
                    List<DataBillType> dataBillTypes = billTypeService.listDataBillTypeByTypeCode(pkBillType);
                    String billTypeName = CollectionUtil.isNotEmpty(dataBillTypes) && dataBillTypes.size() > 0 ? dataBillTypes.get(0).getTypeName() : "未知业务单据";
                    currentTask.setBillTypeName(billTypeName);
                    currentTask.setTradeTypeName(billTypeName);
                    currentTask.setUserId(userId);
                    currentTask.setGroupId(billBody.elementText("DetailInfo"));
                    currentTask.setOrgCode(billBody.elementText("OrgNoV"));
                    currentTask.setOrgName(billBody.elementText("OrgName"));
                    currentTask.setOcrType("1");
                    currentTask.setTaskState(TaskStateConstants.TASK_STATE_SCAN);
                    currentTask.setSystemCode("NCC");
                    DataCurrentTaskBo dataCurrentTaskBo = BeanCopyUtils.copy(currentTask, DataCurrentTaskBo.class);
                    dataCurrentTaskService.insertByBo(dataCurrentTaskBo);
                }
            }
            JSONObject urlResponse = new JSONObject();
            urlResponse.put("ip", systemIp);
            urlResponse.put("port", systemPort);
            Map<String, Object> urlMap = new HashMap<>();
            String token = externalTokenService.getNccToken(String.valueOf(sysUserList.get(0).getUserId()), userNo);
            String url = "";
            //  流水号字段为空为专岗扫描场景，反之为单扫场景
            if (StrUtil.isNotEmpty(businessSerialNo)) {
                url = systemIp + ":" + systemPort + UrlAddressTypeConstant.SCAN_URL_ADDRESS + "?token=" + token;
                DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(businessSerialNo);
                // 如果影像任务不存在，则向业务系统主动拉取影像任务
                if (ObjectUtil.isEmpty(dataCurrentTask)) {
                    String webUrl = NcProperties.getDefaultPropertiesInfo().getString("baseUrl") + NcProperties.getDefaultPropertiesInfo().getString("wsUrl");
                    try {
                        dataCurrentTask = callNcService.synchronizeTaskFromNc(NcProperties.getDefaultPropertiesInfo().getString("factoryCode"), NcProperties.getDefaultPropertiesInfo().getString("dataSource"), groupId, barCode, userId, webUrl);
                    } catch (Exception e) {
                        log.error("singleLogin接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
                        return ResultUtil.getRespXML(NcCodeEnum.NC_FAIL_STATE.getCode(), e.getLocalizedMessage(), null);
                    }
                }
                urlMap.put("businessSerialNo", businessSerialNo);
                urlMap.put("userId", userId);
                urlMap.put("userNo", userNo);
                urlMap.put("billNum", dataCurrentTask.getBillNum());
                urlMap.put("supplementaryScan", isSupplementaryScanning);
            } else {
                url = systemIp + ":" + systemPort + UrlAddressTypeConstant.TASK_URL_ADDRESS + "?token=" + token;
                urlMap.put("userId", userId);
                urlMap.put("linksSource", Constants.LINKS_SOURCE);
            }
            if (ObjectUtil.isNotEmpty(treeNode)) {//自定义节点有值，将&treeNode=放到url中
                urlMap.put("treeNode", treeNode);
            }
            url = ResultUtil.getUrl(url, urlMap);
            urlResponse.put("url_opensoft", url);
            dataResponse.put("RspUrl", JSON.toJSONString(urlResponse));
            code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
            message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
        } else {
            code = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCode();
            message = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCodeName();
        }
        String respXML = ResultUtil.getRespXML(code, message, dataResponse);
        log.info("影像系统接口-单点登录返回成功报文：" + respXML);
        return respXML;
    }

    @Override
    public String getImageShowUrl(String xml) {
        log.info("NC获取影像查看链接请求报文：" + xml);
        String code;
        String message;
        String systemIp = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_IP);
        String systemPort = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_PORT);
        Map<String, Object> dataResponse = null;
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("获取影像查看链接接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }
        Element rootElement = document.getRootElement();
        // xml数据格式校验
        boolean xmlDataVerify = XmlUtil.xmlDataVerify(rootElement);
        if (xmlDataVerify) {
            dataResponse = new HashMap<>();
            int imageCount = 0;
            Element billBody = rootElement.element("BillBody");
            String businessSerialNo = billBody.elementText("Busi_Serial_No");
            String isEdit = billBody.elementText("IsEdit");
            String userName = billBody.elementText("UserName");
            String userId = billBody.elementText("userid").replace(">", "");
            String userNo = billBody.elementText("UserNo");
            // 判断是否同步了用户
            List<SysUser> sysUserList = sysUserService.selectListByNcUserId(userId);
            if (CollectionUtil.isEmpty(sysUserList) || sysUserList.size() < 1) {
                code = NcCodeEnum.NC_NOT_SYNC_DATA.getCode();
                message = NcCodeEnum.NC_NOT_SYNC_DATA.getCodeName();
                String respXML = ResultUtil.getRespXML(code, message, dataResponse);
                log.info("影像系统接口-获取查看链接接口返回成功报文：" + respXML);
                return respXML;
            }
            List<Element> srcBusinessSerialNoList = billBody.elements("Src_Busi_Serial_No");
            List<String> businessSerialNoList = new ArrayList<>();
            List<String> batchIdList = new ArrayList<>();
            // srcBusinessSerialNo属性有值的话为上下游单据查询，反之为单条的单据查询
            if (ObjectUtil.isNotEmpty(srcBusinessSerialNoList)) {
                businessSerialNoList = srcBusinessSerialNoList.stream().map(Element::getText).collect(Collectors.toList());
            }
            businessSerialNoList.add(businessSerialNo);
            List<DataCmInfo> dataCmInfoList = dataCmInfoService.selectDataCmInfoListByBusinessSerialNoList(businessSerialNoList);
            if (ObjectUtil.isNotEmpty(dataCmInfoList)) {
                batchIdList = dataCmInfoList.stream().map(DataCmInfo::getBatchId).collect(Collectors.toList());
            }
            if (batchIdList.size() > 0) {
                List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectAllByBatchIdList(batchIdList);
                if (ObjectUtil.isNotEmpty(dataImageFilesInfoList)) {
                    imageCount = dataImageFilesInfoList.size();
                }
            }
            String token = externalTokenService.getNccToken(String.valueOf(sysUserList.get(0).getUserId()), null);
            String url = systemIp + ":" + systemPort + UrlAddressTypeConstant.SHOW_URL_ADDRESS + "?token=" + token;
            Map<String, Object> urlMap = new HashMap<>();

            urlMap.put("isEdit", isEdit);
            urlMap.put("userId", userId);
            urlMap.put("businessSerialNo", StrUtil.join(Constants.CONNECT_SYMBOL, businessSerialNoList));
            url = ResultUtil.getUrl(url, urlMap);
            dataResponse.put("ImagCount", imageCount);
            dataResponse.put("RspUrl", url);
            code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
            message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
        } else {
            code = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCode();
            message = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCodeName();
        }
        String respXML = ResultUtil.getRespXML(code, message, dataResponse);
        log.info("影像系统接口-获取查看链接接口成功返回报文：" + respXML);
        return respXML;
    }

    @Override
    public String updateBillNo(String xml) {
        log.info("更新单据号接口请求报文：" + xml);
        String code;
        String message;
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("更新单据号接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }
        Element rootElement = document.getRootElement();
        // xml数据格式校验
        boolean xmlDataVerify = XmlUtil.xmlDataVerify(rootElement);
        if (xmlDataVerify) {
            Element billBody = rootElement.element("BillBody");
            // 旧单据号 或 旧流水号
            String billId = billBody.elementText("billID").trim();
            // 新单据号
            String billNo = billBody.elementText("billNo").trim();
            DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(billId);
            if (ObjectUtil.isNotEmpty(dataCurrentTask)) {
                dataCurrentTask.setBillNum(billNo);
                dataCurrentTask.updateById();
                code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
                message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
            } else {
                code = NcCodeEnum.NC_TASK_NO_EXISTS.getCode();
                message = NcCodeEnum.NC_TASK_NO_EXISTS.getCodeName();
            }

        } else {
            code = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCode();
            message = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCodeName();
        }
        String respXML = ResultUtil.getRespXML(code, message, null);
        log.info("影像系统接口-更改单据号接口返回报文：" + respXML);
        return respXML;
    }

    @Override
    public String downloadImages(String xml) {
        log.info("电子档案下载接口请求报文：" + xml);
        String resultStr = "";
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("电子档案下载接口异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }
        Element rootElement = document.getRootElement();
        Integer nccVersion = Integer.valueOf(NcProperties.getDefaultPropertiesInfo().getString("nccVersion"));
        String businessSerialNosStr = rootElement.element("BUSI_SERIAL_NOS").getTextTrim();
        if (StrUtil.isNotEmpty(businessSerialNosStr)) {
            List<String> stringList = StrUtil.split(businessSerialNosStr, ",");
            if (CollectionUtil.isNotEmpty(stringList)) {
                // NCC2411版本开始返回结构有变动
                if(nccVersion.compareTo(2411)>=0){
                    for (int i = 0; i < stringList.size(); i++) {
                        String businessSerialNo = stringList.get(i);
                        List<DataCmInfo> dataCmInfoList = dataCmInfoService.selectDataCmInfoByBusinessSerialNo(businessSerialNo);
                        if (CollectionUtil.isNotEmpty(dataCmInfoList)) {
                            String urlStr = "";
                            DataCmInfo dataCmInfo = dataCmInfoList.get(0);
                            List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectByBatchIdAndCip(dataCmInfo.getBatchId(),Constants.YBZ);
                            if (CollectionUtil.isNotEmpty(dataImageFilesInfoList)) {
                                String items = "<ITEMS>";
                                for (int j = 0; j < dataImageFilesInfoList.size(); j++) {
                                    DataImageFilesInfo dataImageFilesInfo = dataImageFilesInfoList.get(j);
                                    String fileName = dataImageFilesInfo.getFileName();
                                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                                    String url = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_IP) + ":" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_PORT) + dataImageFilesInfo.getIurl() + "?ext=" + suffix;
                                    urlStr += "<URL>" +  url+ "</URL>";
                                    boolean isInvoice = false;
                                    if(invoiceList.contains(dataImageFilesInfo.getFileType())){
                                        isInvoice = true;
                                    }
                                    String item = "<ITEM><URL>"+url+"</URL><FILE_NAME>"+dataImageFilesInfo.getFileName()+"</FILE_NAME><FILE_TYPE>"+suffix+"</FILE_TYPE><IS_INVOICE>"+isInvoice+"</IS_INVOICE></ITEM>";
                                    items += item;
                                }
                                items += "</ITEMS>";
                                String businessS = "<BUSI_SERIAL_NO>" + businessSerialNo + "</BUSI_SERIAL_NO>";
                                resultStr += "<BILL>" + businessS + urlStr + items+"</BILL>";
                            }else {
                                resultStr = buildResponse(businessSerialNo, resultStr);
                            }
                        } else {
                            resultStr = buildResponse(businessSerialNo, resultStr);
                        }
                    }
                }else{
                    for (int i = 0; i < stringList.size(); i++) {
                        String businessSerialNo = stringList.get(i);
                        List<DataCmInfo> dataCmInfoList = dataCmInfoService.selectDataCmInfoByBusinessSerialNo(businessSerialNo);
                        if (CollectionUtil.isNotEmpty(dataCmInfoList)) {
                            String urlStr = "";
                            DataCmInfo dataCmInfo = dataCmInfoList.get(0);
                            List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectByBatchIdAndCip(dataCmInfo.getBatchId(),Constants.YBZ);
                            if (CollectionUtil.isNotEmpty(dataImageFilesInfoList)) {
                                for (int j = 0; j < dataImageFilesInfoList.size(); j++) {
                                    DataImageFilesInfo dataImageFilesInfo = dataImageFilesInfoList.get(j);
                                    String fileName = dataImageFilesInfo.getFileName();
                                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                                    urlStr += "<URL>" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_IP) + ":" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_PORT) + dataImageFilesInfo.getIurl() + "?ext=" + suffix + "</URL>";
                                }
                                String businessS = "<BUSI_SERIAL_NO>" + businessSerialNo + "</BUSI_SERIAL_NO>";
                                resultStr += "<BILL>" + businessS + urlStr + "</BILL>";
                            }else {
                                resultStr = buildResponse(businessSerialNo, resultStr);
                            }
                        } else {
                            resultStr = buildResponse(businessSerialNo, resultStr);
                        }
                    }
                }
            }
        }
        String resultXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<DATA>" + resultStr + "</DATA>";
        log.info("影像系统接口-电子档案下载接口返回报文：" + resultXml);
        return resultXml;
    }

    @NotNull
    private static String buildResponse(String businessSerialNo, String resultStr) {
        String businessStr = "<BUSI_SERIAL_NO>" + businessSerialNo + "</BUSI_SERIAL_NO>";
        resultStr += "<BILL>" + businessStr + "</BILL>";
        return resultStr;
    }

    @Override
    public String rejectImageOnBillReject(String xml) {
        log.info("NC单据驳回影像状态请求报文：" + xml);
        String code;
        String message;
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("单据驳回影像状态接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }
        Element rootElement = document.getRootElement();
        // xml数据格式校验
        boolean xmlDataVerify = XmlUtil.xmlDataVerify(rootElement);
        if (xmlDataVerify) {
            Element billBody = rootElement.element("BillBody");
            Element bill = billBody.element("Bill");
            String businessSerialNo = bill.elementText("Busi_Serial_No");
            DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(businessSerialNo);
            if (ObjectUtil.isEmpty(dataCurrentTask)) {
                // 如果数据在影像系统不存在 提示业务系统单据不存在
                return ResultUtil.getRespXML(NcCodeEnum.NC_TASK_NO_EXISTS.getCode(), NcCodeEnum.NC_TASK_NO_EXISTS.getCodeName(), null);
            } else {
                dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_BH_BS);
                dataCurrentTaskService.updateDataCurrentTask(dataCurrentTask);
                code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
                message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
            }
        } else {
            code = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCode();
            message = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCodeName();
        }
        String respXML = ResultUtil.getRespXML(code, message, null);
        log.info("影像系统接口-驳回影像状态接口成功返回报文：" + respXML);
        return respXML;
    }

    @Override
    public String fileScan(String xml) {
        log.info("NC获取收票节点文件上传链接请求报文：" + xml);
        String code;
        String message;
        String systemIp = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_IP);
        String systemPort = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_PORT);
        Map<String, Object> dataResponse = null;
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("收票节点文件上传链接接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }
        Element rootElement = document.getRootElement();
        // xml数据格式校验
        boolean xmlDataVerify = XmlUtil.xmlDataVerify(rootElement);
        if (xmlDataVerify) {
            dataResponse = new HashMap<>();
            Element billBody = rootElement.element("Body");
            String businessSerialNo = billBody.elementText("New_Busi_Serial_No");
            String ocrType = billBody.elementText("ocrType");
            String token = externalTokenService.getNccToken(null,null);
            String url = systemIp + ":" + systemPort + UrlAddressTypeConstant.SCAN_URL_ADDRESS + "?token=" + token;
            JSONObject urlResponse = new JSONObject();
            Map<String, Object> urlMap = new HashMap<>();
            DataCurrentTask currentTask = new DataCurrentTask();
            currentTask.setBusinessSerialNo(businessSerialNo);
            currentTask.setOcrType(ocrType);
            currentTask.setTaskState(TaskStateConstants.TASK_STATE_SCAN);
            currentTask.setBillTypeName("收票业务节点");
            currentTask.setTradeTypeName("收票业务节点");
            dataCurrentTaskService.insertOrUpdateDataCurrentTaskByBusinessSerialNo(currentTask);
            urlMap.put("businessSerialNo", currentTask.getBusinessSerialNo());
            // isTicket 1：收票节点发票上传 2：收票节点文件上传 其他：非收票节点
            urlMap.put("collectInvoice", "2");
            url = ResultUtil.getUrl(url, urlMap);
            urlResponse.put("ip", systemIp);
            urlResponse.put("port", systemPort);
            urlResponse.put("url_opensoft", url);
            dataResponse.put("RspUrl", JSON.toJSONString(urlResponse));
            code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
            message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
        } else {
            code = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCode();
            message = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCodeName();
        }
        String respXML = ResultUtil.getRespXML(code, message, dataResponse);
        log.info("影像系统接口-获取收票附件上传接口成功返回报文：" + respXML);
        return respXML;
    }

    @Override
    public String getCombineImageShowUrl(String xml) {
        log.info("NC获取凭证影像查看链接请求报文：" + xml);
        String code;
        String message;
        String systemIp = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_IP);
        String systemPort = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_PORT);
        Map<String, Object> dataResponse = null;
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("NC获取凭证影像查看链接接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }
        Element rootElement = document.getRootElement();
        // xml数据格式校验
        boolean xmlDataVerify = XmlUtil.xmlDataVerify(rootElement);
        if (xmlDataVerify) {
            dataResponse = new HashMap<>();
            int imageCount = 0;
            Element billBody = rootElement.element("BillBody");
            List<Element> elementList = billBody.elements("Busi_Serial_No");
            List<String> businessSerialNoList = elementList.stream().map(Element::getText).collect(Collectors.toList());
            List<DataCmInfo> dataCmInfoList = dataCmInfoService.selectDataCmInfoListByBusinessSerialNoList(businessSerialNoList);
            if (ObjectUtil.isNotEmpty(dataCmInfoList)) {
                List<String> batchIdList = dataCmInfoList.stream().map(DataCmInfo::getBatchId).collect(Collectors.toList());
                List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectAllByBatchIdList(batchIdList);
                if (ObjectUtil.isNotEmpty(dataImageFilesInfoList)) {
                    imageCount = dataImageFilesInfoList.size();
                }
            }
            String businessSerialNo = StrUtil.join(Constants.CONNECT_SYMBOL, businessSerialNoList);
            String token = externalTokenService.getNccToken(null,null);
            String url = systemIp + ":" + systemPort + UrlAddressTypeConstant.SHOW_URL_ADDRESS + "?token=" + token;
            Map<String, Object> urlMap = new HashMap<>();
            urlMap.put("businessSerialNo", businessSerialNo);
            url = ResultUtil.getUrl(url, urlMap);
            dataResponse.put("ImagCount", imageCount);
            dataResponse.put("RspUrl", url);
            code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
            message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
        } else {
            code = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCode();
            message = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCodeName();
        }
        String respXML = ResultUtil.getRespXML(code, message, dataResponse);
        log.info("影像系统接口-获取凭证影像查看接口成功返回报文：" + respXML);
        return respXML;
    }

    @Override
    public String invoiceScan(String xml) {
        log.info("NC获取收票节点发票扫描链接请求报文：" + xml);
        String code;
        String message;
        Map<String, Object> dataResponse = null;
        String systemIp = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_IP);
        String systemPort = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_PORT);
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("收票节点发票扫描链接接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }
        Element rootElement = document.getRootElement();
        // xml数据格式校验
        boolean xmlDataVerify = XmlUtil.xmlDataVerify(rootElement);
        if (xmlDataVerify) {
            dataResponse = new HashMap<>();
            Element body = rootElement.element("Body");
            String orgNoV = body.elementText("OrgNoV");
            String billType = body.elementText("BillType");
            String pkBillType = body.elementText("pk_billtype");
            String ocrType = body.elementText("ocrType");
            String userNo = body.elementText("UserNo");
            if (StrUtil.isEmpty(orgNoV)) {
                orgNoV = IdUtil.fastSimpleUUID();
            }
            SysUser sysUser = sysUserService.selectSysUserByUserNo(userNo);
            // 判断系统内是否同步了基础用户数据
            if (ObjectUtil.isEmpty(sysUser)) {
                return ResultUtil.getRespXML(NcCodeEnum.NC_NOT_SYNC_DATA.getCode(), NcCodeEnum.NC_NOT_SYNC_DATA.getCodeName(), null);
            }
            String token = externalTokenService.getNccToken(null,null);
            String url = systemIp + ":" + systemPort + UrlAddressTypeConstant.SCAN_URL_ADDRESS + "?token=" + token;
            JSONObject urlResponse = new JSONObject();
            Map<String, Object> urlMap = new HashMap<>();
            DataCurrentTask currentTask = new DataCurrentTask();
            currentTask.setUserId(sysUser.getNcUserId());
            currentTask.setBusinessSerialNo(orgNoV);
            currentTask.setBillType(pkBillType);
            currentTask.setPkBillType(billType);
            currentTask.setOrgCode(orgNoV);
            currentTask.setOcrType(ocrType);
            currentTask.setBillTypeName("收票业务节点");
            currentTask.setTradeTypeName("收票业务节点");
            currentTask.setTaskState(TaskStateConstants.TASK_STATE_SCAN);
            dataCurrentTaskService.insertOrUpdateDataCurrentTaskByBusinessSerialNo(currentTask);
            urlMap.put("businessSerialNo", currentTask.getBusinessSerialNo());
            // isTicket 1：收票节点发票上传  2：收票节点文件上传  其他：非收票节点
            urlMap.put("collectInvoice", "1");
            url = ResultUtil.getUrl(url, urlMap);
            urlResponse.put("ip", systemIp);
            urlResponse.put("port", systemPort);
            urlResponse.put("url_opensoft", url);
            dataResponse.put("RspUrl", JSON.toJSONString(urlResponse));
            code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
            message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
        } else {
            code = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCode();
            message = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCodeName();
        }
        String respXML = ResultUtil.getRespXML(code, message, dataResponse);
        log.info("影像系统接口-获取收票发票扫描接口成功返回报文：" + respXML);
        return respXML;
    }

    @Override
    public String updateImageState(String xml) {
        log.info("更改影像状态接口请求报文：" + xml);
        Document document;
        String code;
        String message;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("更改影像状态接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXML(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null);
        }
        Element rootElement = document.getRootElement();
        // xml数据格式校验
        boolean xmlDataVerify = XmlUtil.xmlDataVerify(rootElement);
        if (xmlDataVerify) {
            Element billBody = rootElement.element("BillBody");
            Element service = billBody.element("service");
            String businessSerialNo = service.elementText("Busi_Serial_No");
            String imageState = service.elementText("imagastate");
            DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(businessSerialNo);
            dataCurrentTask.setTaskState(imageState);
            dataCurrentTaskService.insertOrUpdateDataCurrentTaskByBusinessSerialNo(dataCurrentTask);
            code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
            message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
        } else {
            code = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCode();
            message = NcCodeEnum.NC_HEAD_CHECK_FAIL_STATE.getCodeName();
        }
        String resXml = ResultUtil.getRespXML(code, message, null);
        log.info("更改影像状态接口返回报文：" + resXml);
        return resXml;
    }

    @Override
    public String mobileImageQuery(String xml) {
        log.info("移动审批获取图片列表接口请求报文：" + xml);
        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("移动审批获取图片列表接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
            return ResultUtil.getRespXmlForMobile(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(),null,null,null);
        }
        Element rootElement = document.getRootElement();
        // 组装返回xml数据
        Map<String, Object> dataResponse = new HashMap<>();
        String businessSerialNo = rootElement.elementText("BUSI_SERIAL_NO");
        List<Element> srcBusinessSerialNoList = rootElement.elements("SRC_BUSI_SERIAL_NO");
        // 流水号下图片的集合
        List<DataImageFilesInfo> dataImageFilesInfoList = new ArrayList<>();
        String batchId = "";
        dataResponse.put("TRADETYPE", rootElement.elementText("TRADETYPE"));
        dataResponse.put("SYSTEM_CODE", rootElement.elementText("SYSTEM_CODE"));
        dataResponse.put("BRANCH_NO", rootElement.elementText("BRANCH_NO"));
        dataResponse.put("USER_NO", rootElement.elementText("USER_NO"));
        dataResponse.put("OPERATE_TIME", DateUtil.now());
        List<DataCmInfo> cmInfoList = dataCmInfoService.selectDataCmInfoByBusinessSerialNo(businessSerialNo);
        if (CollectionUtil.isNotEmpty(cmInfoList)) {
            batchId = cmInfoList.get(0).getBatchId();
            dataImageFilesInfoList = dataImageFilesInfoService.selectByBatchId(batchId);
        }
        // 将单据联查相关的流水号下的图片也塞进集合中
        if (CollectionUtil.isNotEmpty(srcBusinessSerialNoList)) {
            List<String> businessSerialNoList = srcBusinessSerialNoList.stream().map(Element::getText).collect(Collectors.toList());
            List<DataCmInfo> dataCmInfoList = dataCmInfoService.selectDataCmInfoListByBusinessSerialNoList(businessSerialNoList);
            if (ObjectUtil.isNotEmpty(dataCmInfoList)) {
                List<String> batchIdList = dataCmInfoList.stream().map(DataCmInfo::getBatchId).collect(Collectors.toList());
                List<DataImageFilesInfo> srcDataImageFilesInfoList = dataImageFilesInfoService.selectAllByBatchIdList(batchIdList);
                dataImageFilesInfoList.addAll(srcDataImageFilesInfoList);
            }
        }
        // 对图片集合进行分类处理并塞进对应map中
        Map<String, List<DataImageFilesInfo>> map = new HashMap<>();
        for (DataImageFilesInfo imageFilesInfo : dataImageFilesInfoList) {
            String documentName = imageFilesInfo.getDocumentName();
            if (StrUtil.isEmpty(documentName)) {
                documentName = "移动审批";
            }
            if (!map.containsKey(documentName)) {
                List<DataImageFilesInfo> dataImageFilesInfos = new ArrayList<>();
                dataImageFilesInfos.add(imageFilesInfo);
                map.put(documentName, dataImageFilesInfos);
            } else {
                List<DataImageFilesInfo> dataImageFilesInfos = map.get(documentName);
                dataImageFilesInfos.add(imageFilesInfo);
                map.put(documentName, dataImageFilesInfos);
            }
        }
        String resXml = ResultUtil.getRespXmlForMobile(NcCodeEnum.NC_SUCCESS_STATE.getCode(), NcCodeEnum.NC_SUCCESS_STATE.getCodeName(), dataResponse, map, batchId);
        log.info("移动审批获取图片列表接口返回报文：" + resXml);
        return resXml;
    }
}
