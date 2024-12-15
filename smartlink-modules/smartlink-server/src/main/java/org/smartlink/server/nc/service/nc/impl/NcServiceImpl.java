package org.smartlink.server.nc.service.nc.impl;

import cn.hutool.core.collection.CollectionUtil;
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
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.*;
import org.smartlink.server.nc.domain.*;
import org.smartlink.server.nc.domain.dto.NcUpdateTaskStateDTO;
import org.smartlink.server.nc.domain.token.LoginVo;
import org.smartlink.server.nc.enums.NCCTaskStateEnum;
import org.smartlink.server.nc.enums.NcCodeEnum;
import org.smartlink.server.nc.ncc.testapi.TestApiService;
import org.smartlink.server.nc.ncc.testapi.response.TestOpenApiResponse;
import org.smartlink.server.nc.ncc.testapi.resqust.TestOpenApiRequest;
import org.smartlink.server.nc.ncc.testapi.resqust.TestOpenApiRequestData;
import org.smartlink.server.nc.properties.NcProperties;
import org.smartlink.server.nc.properties.NccParamProperties;
import org.smartlink.server.nc.service.nc.*;
import org.smartlink.server.nc.token.NccToken;
import org.smartlink.server.nc.token.request.NccTokenRequest;
import org.smartlink.server.nc.token.response.Token;
import org.smartlink.server.nc.utils.ExceptionUtil;
import org.smartlink.server.nc.utils.ResultUtil;
import org.smartlink.server.nc.utils.UrlAddressTypeConstant;
import org.smartlink.server.nc.utils.XmlUtil;
import org.smartlink.server.task.momain.DataTask;
import org.smartlink.server.task.service.DataTaskServer;
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
    private final DataTaskServer dataTaskServer;

    private List invoiceList = Arrays.asList(InvoiceConstants.TAX_SPECIAL_INVOICE, InvoiceConstants.TAX_INVOICE, InvoiceConstants.ELECTRONIC_INVOICE, InvoiceConstants.ROLL_TICKET, InvoiceConstants.ELECTRONIC_OFD_INVOICE, InvoiceConstants.ELECTRONIC_INVOICE_ITINERARY, InvoiceConstants.MOTOR_VEHICLE_SALE, InvoiceConstants.USED_CAR_SALES, InvoiceConstants.QUOTA_INVOICE, InvoiceConstants.AIRCRAFT_INVOICE, InvoiceConstants.TAXI_TICKETS, InvoiceConstants.RAILWAY_TICKET, InvoiceConstants.PASSENGER_TICKET, InvoiceConstants.FLIGHT_ITINERARY, InvoiceConstants.STEAMER_TICKET, InvoiceConstants.TOLL_ROADS, InvoiceConstants.RECEIPT, InvoiceConstants.ELECTRONIC_INVOICE_QUKUAILIAN, InvoiceConstants.INVOICE_MUCH_NCC, InvoiceConstants.INVOICE_OTHERS);

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

            DataTask task = this.dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
            List<DataBillType> dataBillTypeList = billTypeService.listDataBillTypeByTypeCode(billType);
            boolean taskIsExists = ObjectUtil.isNotEmpty(task);
            task = !taskIsExists ? new DataTask() : task;
            String taskState = taskIsExists ? task.getTaskState() : TaskStateConstants.TASK_STATE_SCAN;
            task.setScanTypeName(StrUtil.equals(scanType, ScanTypeConstants.SINGLE_SCAN) ? "单笔扫描" : "批量扫描");
            task.setScanType(scanType);
            task.setBusinessSerialNo(businessSerialNo);
            task.setBillNum(billCode);
            task.setBillType(pkBillType);
            task.setPkBillType(billType);
            String tradeTypeName = CollectionUtil.isNotEmpty(dataBillTypeList) && dataBillTypeList.size() > 0 ? dataBillTypeList.get(0).getTypeName() : "未知业务单据";
            task.setBillTypeName(tradeTypeName);
            task.setTradeTypeName(tradeTypeName);
            task.setBillDate(bill.elementText("BillDate"));
            task.setCash(bill.elementText("Cash"));
            task.setUserId(bill.elementText("userid"));
            task.setGroupId(bill.elementText("DetailInfo"));
            task.setOrgCode(bill.elementText("OrgNo"));
            task.setOrgName(bill.elementText("OrgName"));
            task.setOcrType(bill.elementText("ocrType") != null ? bill.elementText("ocrType") : "0");
            task.setTaskState(taskState);
            task.setSystemCode(clientCode);
//            dataCurrentTaskService.insertOrUpdateDataCurrentTaskByBusinessSerialNo(task);
            this.dataTaskServer.saveOrUpdate(task);
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
            DataTask task = this.dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
      // 废弃任务需删除：1.任务表数据 2.中间表数据 3.图片表数据 4.发票信息表数据 5.源文件数据
            if (ObjectUtil.isNotEmpty(task)) {
                try {
                     this.dataTaskServer.removeById(task.getId());
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
//        String systemIp = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_IP);
//        String systemPort = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_PORT);
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
                DataTask dataTask = this.dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();

                // 如果单据任务不存在 则手动添加至数据库中
                if (ObjectUtil.isEmpty(dataTask)) {
                    DataTask task = new DataTask();
                    task.setScanType(scanType);
                    task.setBusinessSerialNo(businessSerialNo);
                    task.setBillNum(billNum);
                    task.setCash(cash);
                    task.setBillType(billType);
                    task.setPkBillType(pkBillType);
                    List<DataBillType> dataBillTypes = billTypeService.listDataBillTypeByTypeCode(pkBillType);
                    String billTypeName = CollectionUtil.isNotEmpty(dataBillTypes) && dataBillTypes.size() > 0 ? dataBillTypes.get(0).getTypeName() : "未知业务单据";
                    task.setBillTypeName(billTypeName);
                    task.setTradeTypeName(billTypeName);
                    task.setUserId(userId);
                    task.setGroupId(billBody.elementText("DetailInfo"));
                    task.setOrgCode(billBody.elementText("OrgNoV"));
                    task.setOrgName(billBody.elementText("OrgName"));
                    task.setOcrType("1");
                    task.setTaskState(TaskStateConstants.TASK_STATE_SCAN);
                    task.setSystemCode("NCC");
                  this.dataTaskServer.save(task);
                }
            }
            JSONObject urlResponse = new JSONObject();
            urlResponse.put("ip", "http://10.124.9.147");
            urlResponse.put("port", "8086");
            Map<String, Object> urlMap = new HashMap<>();
            String token = externalTokenService.getNccToken(String.valueOf(sysUserList.get(0).getUserId()), userNo);
            String url = "";
            //  流水号字段为空为专岗扫描场景，反之为单扫场景
            if (StrUtil.isNotEmpty(businessSerialNo)) {
                url = "http://10.124.9.147:8086" + UrlAddressTypeConstant.SCAN_URL_ADDRESS + "?token=" + token;
                DataTask task=this.dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
                // 如果影像任务不存在，则向业务系统主动拉取影像任务
                if (ObjectUtil.isEmpty(task)) {
                    String webUrl = NcProperties.getDefaultPropertiesInfo().getString("baseUrl") + NcProperties.getDefaultPropertiesInfo().getString("wsUrl");
                    try {
                        log.info("拉影像任务：待实现");
                      //  task = callNcService.synchronizeTaskFromNc(NcProperties.getDefaultPropertiesInfo().getString("factoryCode"), NcProperties.getDefaultPropertiesInfo().getString("dataSource"), groupId, barCode, userId, webUrl);
                    } catch (Exception e) {
                        log.error("singleLogin接口出现异常：" + ExceptionUtil.getExceptionMessage(e));
                        return ResultUtil.getRespXML(NcCodeEnum.NC_FAIL_STATE.getCode(), e.getLocalizedMessage(), null);
                    }
                }
                urlMap.put("businessSerialNo", businessSerialNo);
                urlMap.put("userId", userId);
                urlMap.put("userNo", userNo);
                urlMap.put("billNum", task.getBillNum());
                urlMap.put("supplementaryScan", isSupplementaryScanning);
            } else {
                url = "http://10.124.9.147:8086" + UrlAddressTypeConstant.SCAN_URL_ADDRESS + "?token=" + token;
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
//        String systemIp = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_IP);
//        String systemPort = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_PORT);
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
//            List<SysUser> sysUserList = sysUserService.selectListByNcUserId(userId);
//            if (CollectionUtil.isEmpty(sysUserList) || sysUserList.size() < 1) {
//                code = NcCodeEnum.NC_NOT_SYNC_DATA.getCode();
//                message = NcCodeEnum.NC_NOT_SYNC_DATA.getCodeName();
//                String respXML = ResultUtil.getRespXML(code, message, dataResponse);
//                log.info("影像系统接口-获取查看链接接口返回成功报文：" + respXML);
//                return respXML;
//            }
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

            R<LoginVo> token = externalTokenService.getToken();
            String url = "http://10.124.9.147:8086" + UrlAddressTypeConstant.SHOW_URL_ADDRESS + "?token=" + token.getData().getAccessToken();
//            String url = "http://10.124.9.147:8086" + UrlAddressTypeConstant.SHOW_URL_ADDRESS + "?token=" + StpUtil.getTokenValueByLoginId(loginUser.getLoginId());

//            try {
            // 定义URL
//                String baseUrl = "http://10.124.9.147:8086" + UrlAddressTypeConstant.SHOW_URL_ADDRESS;
//                // 定义参数Map
//                Map<String, String> params = new HashMap<>();
////                params.put("businessSerialNo", businessSerialNo);
//                params.put("token", token);
//
//                // 拼接参数
//                StringBuilder urlBuilder = new StringBuilder(baseUrl);
//                urlBuilder.append("?");
//
//                for (Map.Entry<String, String> entry : params.entrySet()) {
//                    urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
//                        .append("=")
//                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
//                        .append("&");
//                }
//
//                // 移除最后一个多余的&
//                if (urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
//                    urlBuilder.deleteCharAt(urlBuilder.length() - 1);
//                }
//                // 输出最终的URL
//                String url = urlBuilder.toString();

            Map<String, Object> urlMap = new HashMap<>();
            urlMap.put("isEdit", isEdit);
            urlMap.put("userId", userId);
            urlMap.put("businessSerialNo", StrUtil.join(Constants.CONNECT_SYMBOL, businessSerialNoList));
            url = ResultUtil.getUrl(url, urlMap);
            dataResponse.put("ImagCount", imageCount);
            dataResponse.put("RspUrl", url);
            code = NcCodeEnum.NC_SUCCESS_STATE.getCode();
            message = NcCodeEnum.NC_SUCCESS_STATE.getCodeName();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            String url = "http://10.124.9.147:8086" + UrlAddressTypeConstant.SHOW_URL_ADDRESS + "?businessSerialNo=" + businessSerialNo + "token=" + token;
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
                if (nccVersion.compareTo(2411) >= 0) {
                    for (int i = 0; i < stringList.size(); i++) {
                        String businessSerialNo = stringList.get(i);
                        List<DataCmInfo> dataCmInfoList = dataCmInfoService.selectDataCmInfoByBusinessSerialNo(businessSerialNo);
                        if (CollectionUtil.isNotEmpty(dataCmInfoList)) {
                            String urlStr = "";
                            DataCmInfo dataCmInfo = dataCmInfoList.get(0);
                            List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectByBatchIdAndCip(dataCmInfo.getBatchId(), Constants.YBZ);
                            if (CollectionUtil.isNotEmpty(dataImageFilesInfoList)) {
                                String items = "<ITEMS>";
                                for (int j = 0; j < dataImageFilesInfoList.size(); j++) {
                                    DataImageFilesInfo dataImageFilesInfo = dataImageFilesInfoList.get(j);
                                    String fileName = dataImageFilesInfo.getFileName();
                                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                                    String url = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_IP) + ":" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_PORT) + dataImageFilesInfo.getIurl() + "?ext=" + suffix;
                                    urlStr += "<URL>" + url + "</URL>";
                                    boolean isInvoice = false;
                                    if (invoiceList.contains(dataImageFilesInfo.getFileType())) {
                                        isInvoice = true;
                                    }
                                    String item = "<ITEM><URL>" + url + "</URL><FILE_NAME>" + dataImageFilesInfo.getFileName() + "</FILE_NAME><FILE_TYPE>" + suffix + "</FILE_TYPE><IS_INVOICE>" + isInvoice + "</IS_INVOICE></ITEM>";
                                    items += item;
                                }
                                items += "</ITEMS>";
                                String businessS = "<BUSI_SERIAL_NO>" + businessSerialNo + "</BUSI_SERIAL_NO>";
                                resultStr += "<BILL>" + businessS + urlStr + items + "</BILL>";
                            } else {
                                resultStr = buildResponse(businessSerialNo, resultStr);
                            }
                        } else {
                            resultStr = buildResponse(businessSerialNo, resultStr);
                        }
                    }
                } else {
                    for (int i = 0; i < stringList.size(); i++) {
                        String businessSerialNo = stringList.get(i);
                        List<DataCmInfo> dataCmInfoList = dataCmInfoService.selectDataCmInfoByBusinessSerialNo(businessSerialNo);
                        if (CollectionUtil.isNotEmpty(dataCmInfoList)) {
                            String urlStr = "";
                            DataCmInfo dataCmInfo = dataCmInfoList.get(0);
                            List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectByBatchIdAndCip(dataCmInfo.getBatchId(), Constants.YBZ);
                            if (CollectionUtil.isNotEmpty(dataImageFilesInfoList)) {
                                for (int j = 0; j < dataImageFilesInfoList.size(); j++) {
                                    DataImageFilesInfo dataImageFilesInfo = dataImageFilesInfoList.get(j);
                                    String fileName = dataImageFilesInfo.getFileName();
                                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                                    urlStr += "<URL>" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_IP) + ":" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_PORT) + dataImageFilesInfo.getIurl() + "?ext=" + suffix + "</URL>";
                                }
                                String businessS = "<BUSI_SERIAL_NO>" + businessSerialNo + "</BUSI_SERIAL_NO>";
                                resultStr += "<BILL>" + businessS + urlStr + "</BILL>";
                            } else {
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
            String token = externalTokenService.getNccToken(null, null);
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
            String token = externalTokenService.getNccToken(null, null);
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
//        String systemIp = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_IP);
//        String systemPort = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_PORT);
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
//            String businessSerialNo = body.elementText("Business_Serial_No");
            if (StrUtil.isEmpty(orgNoV)) {
                orgNoV = IdUtil.fastSimpleUUID();
            }
            SysUser sysUser = sysUserService.selectSysUserByUserNo(userNo);
            // 判断系统内是否同步了基础用户数据
            if (ObjectUtil.isEmpty(sysUser)) {
                return ResultUtil.getRespXML(NcCodeEnum.NC_NOT_SYNC_DATA.getCode(), NcCodeEnum.NC_NOT_SYNC_DATA.getCodeName(), null);
            }
            String token = externalTokenService.getNccToken(null, null);
            String url = "http://10.124.9.147:8086" + UrlAddressTypeConstant.SCAN_URL_ADDRESS + "?token=" + token;
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
            urlResponse.put("ip", "http://10.124.9.147");
            urlResponse.put("port", "8086");
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
            return ResultUtil.getRespXmlForMobile(NcCodeEnum.NC_XML_ERROR.getCode(), NcCodeEnum.NC_XML_ERROR.getCodeName(), null, null, null);
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

    @Override
    public DataCurrentTask submitTaskStateForNc(DataCurrentTask dataCurrentTask, String userId, String supplementaryScan) throws Exception {
        String businessSerialNo = dataCurrentTask.getBusinessSerialNo();
        String state = NCCTaskStateEnum.TASK_STATE_COMPLETE.getState();
        List<DataCmInfo> dataCmInfoList = dataCmInfoService.selectDataCmInfoByBusinessSerialNo(businessSerialNo);
        List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectByBatchId(dataCmInfoList.get(0).getBatchId());
        List<DataImageFilesInfo> filterDataImageFilesInfoList = dataImageFilesInfoList.stream().filter((obj) -> !StrUtil.equals(obj.getFileStatus(), FileStatusConstants.INVOICE_CHECK_SUCCESS) && !StrUtil.equals(obj.getFileStatus(), FileStatusConstants.SAVED_SUCCESSFULLY) && !StrUtil.equals(obj.getFileStatus(), FileStatusConstants.INVOICE_UPDATE)).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(filterDataImageFilesInfoList) && filterDataImageFilesInfoList.size() > 0) {
            throw new Exception("提交影像状态失败，该单据下包含异常状态发票");
        }
        List<SysUser> sysUserList = sysUserService.selectListByNcUserId(userId);
        if (CollectionUtil.isEmpty(sysUserList)) {
            throw new Exception("提交影像状态失败，影像系统不存在该用户：" + userId);
        }
        NcUpdateTaskStateDTO updateTaskRequestParam;
        // 如果是事后补扫场景 走相应的补扫同步状态接口
        if (StrUtil.equals("Y", supplementaryScan)) {
            updateTaskRequestParam = this.getUpdateTaskRequestParam(businessSerialNo, sysUserList.get(0), "", new ArrayList<>());
            updateTaskRequestParam.setDataCurrentTask(dataCurrentTask);
            try {
                callNcService.updateNcImageStateForReScan(updateTaskRequestParam);
            } catch (Exception e) {
                log.error("事后补扫同步状态失败：" + ExceptionUtil.getExceptionMessage(e));
                throw new Exception("事后补扫同步状态失败：" + e.getLocalizedMessage());
            }
        } else {
            // 若当前影像已经为驳回状态，则设置传给NC的state为补扫完成：5
            if (StrUtil.equals(dataCurrentTask.getTaskState(), TaskStateConstants.TASK_STATE_BH_BS)) {
                // 补扫提交
                state = NCCTaskStateEnum.TASK_STATE_BS_COMPLETE.getState();
                dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_BS_WC);
            } else if (StrUtil.equals(dataCurrentTask.getTaskState(), TaskStateConstants.TASK_STATE_BH_CS)) {
                // 重扫提交
                state = NCCTaskStateEnum.TASK_STATE_BS_COMPLETE.getState();
                dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_CS_WC);
            } else {
                // 默认扫描成功状态
                dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_COMPLETE);
            }
            dataCurrentTaskService.updateDataCurrentTask(dataCurrentTask);
            updateTaskRequestParam = this.getUpdateTaskRequestParam(businessSerialNo, sysUserList.get(0), state, dataImageFilesInfoList);
            updateTaskRequestParam.setDataCurrentTask(dataCurrentTask);
            try {
                callNcService.updateNcImageState(updateTaskRequestParam);
            } catch (Exception e) {
                log.error("提交影像状态失败：" + ExceptionUtil.getExceptionMessage(e));
                throw new Exception("提交影像状态失败：" + e.getLocalizedMessage());
            }
        }
        return dataCurrentTask;
    }

    /**
     * 获取NC更新影像状态接口所需参数
     *
     * @param businessSerialNo 流水号
     * @param sysUser          用户
     * @param state            影像状态
     * @return 结果
     */
    private NcUpdateTaskStateDTO getUpdateTaskRequestParam(String businessSerialNo, SysUser sysUser, String state, List<DataImageFilesInfo> dataImageFilesInfoList) {
        String webUrl = NcProperties.getDefaultPropertiesInfo().getString("baseUrl") + NcProperties.getDefaultPropertiesInfo().getString("wsUrl");
        NcUpdateTaskStateDTO ncUpdateTaskStateDTO = new NcUpdateTaskStateDTO();
        // 需影像状态变更时，需通知给NC影像数量
        int imageCount = 0;
        int invoiceCount = 0;
        if (ObjectUtil.isNotEmpty(dataImageFilesInfoList)) {
            imageCount = dataImageFilesInfoList.size();
        }
        for (DataImageFilesInfo dataImageFilesInfo : dataImageFilesInfoList) {
            if (NcConstant.ALL_INVOICE_LIST.contains(dataImageFilesInfo.getFileType())) {
                invoiceCount += 1;
            }
        }
        ncUpdateTaskStateDTO.setImageCount(imageCount);
        ncUpdateTaskStateDTO.setInvoiceCount(invoiceCount);
        ncUpdateTaskStateDTO.setSysUser(sysUser);
        ncUpdateTaskStateDTO.setState(state);
        ncUpdateTaskStateDTO.setFactoryCode(NcProperties.getDefaultPropertiesInfo().getString("factoryCode"));
        ncUpdateTaskStateDTO.setDataSource(NcProperties.getDefaultPropertiesInfo().getString("dataSource"));
        ncUpdateTaskStateDTO.setWebUrl(webUrl);
        return ncUpdateTaskStateDTO;
    }

    @Override
    public DataCurrentTask rejectTaskStateForNc(DataCurrentTask dataCurrentTask, String userId, String taskState) throws Exception {
        String businessSerialNo = dataCurrentTask.getBusinessSerialNo();
        List<DataCmInfo> dataCmInfoList = dataCmInfoService.selectDataCmInfoByBusinessSerialNo(businessSerialNo);
        List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectByBatchId(dataCmInfoList.get(0).getBatchId());
        List<SysUser> sysUserList = sysUserService.selectListByNcUserId(userId);
        if (ObjectUtil.isEmpty(dataCurrentTask)) {
            throw new Exception("驳回影像状态失败，对应影像任务不存在，流水号：" + businessSerialNo);
        }
        if (ObjectUtil.isEmpty(sysUserList)) {
            throw new Exception("驳回影像状态失败，影像系统不存在该用户：" + userId);
        }
        if (taskState.equalsIgnoreCase(TaskStateConstants.TASK_STATE_BH_CS)) {
            //驳回重扫
            dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_BH_CS);
        }
        if (taskState.equalsIgnoreCase(TaskStateConstants.TASK_STATE_BH_BS)) {
            //驳回补扫
            dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_BH_BS);
        }
        taskState = NCCTaskStateEnum.TASK_STATE_BS.getState();
        dataCurrentTaskService.updateDataCurrentTask(dataCurrentTask);
        NcUpdateTaskStateDTO updateTaskRequestParam = this.getUpdateTaskRequestParam(businessSerialNo, sysUserList.get(0), taskState, dataImageFilesInfoList);
        updateTaskRequestParam.setDataCurrentTask(dataCurrentTask);
        try {
            callNcService.updateNcImageState(updateTaskRequestParam);
        } catch (Exception e) {
            log.error("驳回影像状态失败：" + ExceptionUtil.getExceptionMessage(e));
            throw new Exception("驳回影像状态失败：" + e.getLocalizedMessage());
        }
        return dataCurrentTask;
    }

    @Override
    public R<DataTask> imageSubmission(String businessSerialNo) throws Exception {

        DataTask dataTask = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if (dataTask == null) {
            throw new Exception("影像提交失败，对应影像任务不存在，流水号：" + businessSerialNo);
        }
        dataTask.setTaskState(TaskStateConstants.TASK_STATE_COMPLETE);
        dataTaskServer.updateById(dataTask);
        NcUpdateTaskStateDTO updateTaskRequestParam;
        updateTaskRequestParam = this.getUpdateBipTaskRequestParam(businessSerialNo,NCCTaskStateEnum.TASK_STATE_COMPLETE.getState());
        updateTaskRequestParam.setDataTask(dataTask);
        try {
            callNcService.updateBipImageState(updateTaskRequestParam);
            log.info("影像状态提交成功+6666666666666666666666666666666666666666");
        } catch (Exception e) {
            log.error("提交影像状态失败：" + ExceptionUtil.getExceptionMessage(e));
            throw new Exception("提交影像状态失败：" + e.getLocalizedMessage());
        }
        return R.ok(dataTask);
    }
    private NcUpdateTaskStateDTO getUpdateBipTaskRequestParam(String businessSerialNo, String state) {
        String webUrl = nccTokenRequest.getBaseUrl() + nccTokenRequest.getWsUrl();
        DataTask task = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();

//        int imageCount = 0;
//        int invoiceCount = 0;
        NcUpdateTaskStateDTO ncUpdateTaskStateDTO = new NcUpdateTaskStateDTO();
//        ncUpdateTaskStateDTO.setImageCount(imageCount);
//        ncUpdateTaskStateDTO.setInvoiceCount(invoiceCount);
        ncUpdateTaskStateDTO.setFactoryCode("shy");
        ncUpdateTaskStateDTO.setDataSource("YONBIP");
        ncUpdateTaskStateDTO.setBillCode(task.getBusinessSerialNo());
        ncUpdateTaskStateDTO.setState(state);
        ncUpdateTaskStateDTO.setBillType(task.getBillType());
        ncUpdateTaskStateDTO.setPk_billtype(task.getPkBillType());
        ncUpdateTaskStateDTO.setImagenum(task.getImages()==null?"0":task.getImages().size()+"");
        ncUpdateTaskStateDTO.setOrgNo(task.getOrgCode());
        ncUpdateTaskStateDTO.setGroupid(task.getGroupId());
        ncUpdateTaskStateDTO.setOpuserdatetime(DateUtil.today());
        ncUpdateTaskStateDTO.setOpuserpk(task.getUserId());
        ncUpdateTaskStateDTO.setOpusername(task.getUserName());
        ncUpdateTaskStateDTO.setOpuseraccount(task.getUserNum());
        ncUpdateTaskStateDTO.setScanType("1");
        ncUpdateTaskStateDTO.setWebUrl(webUrl);
        ncUpdateTaskStateDTO.setBusinessSerialNo(businessSerialNo);
        return ncUpdateTaskStateDTO;
    }
}
