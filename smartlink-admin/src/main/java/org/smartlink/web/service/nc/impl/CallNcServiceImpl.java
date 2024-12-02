package org.smartlink.web.service.nc.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.smartlink.common.core.utils.DateUtils;
import org.smartlink.web.constant.Constants;
import org.smartlink.web.constant.TaskStateConstants;
import org.smartlink.web.constant.UserConstants;
import org.smartlink.web.domain.DataBillType;
import org.smartlink.web.domain.DataCurrentTask;
import org.smartlink.web.domain.SysDept;
import org.smartlink.web.domain.SysUser;
import org.smartlink.web.service.nc.CallNcService;
import org.smartlink.web.utils.WebClientUtil;
import org.smartlink.web.utils.WebServiceUtil;
import org.smartlink.web.utils.XMLProcess;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 调用NCC服务接口
 *
 * @author 刘壮壮
 */
@Slf4j
@Service
public class CallNcServiceImpl implements CallNcService {

    /**
     * 接口返回节点
     */
    private static final String NC_WS_RESULT = "ncwsresult";

    /**
     * 返回结果状态
     */
    private static final String NC_WS_SUCCESS_RESULT = "true";

    /**
     * 接口命名空间
     */
    private static final String TARGET_NAMESPACE = "imageInterfaceService";

    /**
     * 更改影像状态接口
     */
    private static final String UPDATE_IMAGE_STATE_NAMESPACE = "imageStateChange";

    /**
     * NCC事后补扫场景下同步状态接口
     */
    private static final String AFTER_IMAGE_RESCAN = "afterImageRescan";
    /**
     * 同步用户接口名称
     */
    private static final String USER_INTERFACE = "getUsers";

    /**
     * 同步组织机构接口名称
     */
    private static final String DEPART_INTERFACE = "getOrgs";

    /**
     * 同步单据类型接口名称
     */
    private static final String BILL_INTERFACE = "getBillTypes";

    /**
     * 拉取影像任务接口名称
     */
    private static final String PULL_TASK_INTERFACE = "fetchImageTaskInfo";

    /**
     * xml头部
     */
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";


    /**
     * 添加XML头
     *
     * @param xmlParam xml参数
     * @return 返回
     */
    private String handleParameters(String xmlParam) {
        xmlParam = XML_HEADER + xmlParam;
        return xmlParam;
    }
    @Override
    public List<SysUser> synchronizeUser(String factoryCode, String dataSource, String webUrl) throws Exception {
        String requestParam = this.getSynchronizeRequestParam(USER_INTERFACE, factoryCode, dataSource);
        log.info("同步基础用户信息接口请求报文：" + requestParam);
        Element ncWsResult = this.getNcWsResult(webUrl, requestParam);
        final String ncResult = ncWsResult.elementText(NC_WS_RESULT);
        if (!NC_WS_SUCCESS_RESULT.equals(ncResult)) {
            log.error("同步基础用户信息失败：" + ncResult);
            throw new Exception(ncResult);
        }
        Iterator<Element> userinfoIter = ncWsResult.element("userinfo").elementIterator();
        List<SysUser> userList = new ArrayList<>();
        String password = BCrypt.hashpw(Constants.SYSTEM_CODE);
        userinfoIter.forEachRemaining(obj -> {
            String cUserId = Convert.toStr(obj.elementText("CUSERID"), "");
            SysUser user = new SysUser();
            user.setUserType(UserConstants.SYNCHRONIZE_USER);
            user.setStatus(UserConstants.NORMAL);
            user.setDelFlag("0");
            user.setNcUserId(cUserId);
            user.setUserName(Optional.ofNullable(obj.element("USER_CODE").getTextTrim()).orElse(""));
            user.setNickName(Optional.ofNullable(obj.element("USER_NAME").getTextTrim()).orElse(""));
            user.setDeptId(Optional.ofNullable(obj.element("PK_ORG").getTextTrim()).orElse(""));
            user.setGroupId(Optional.ofNullable(obj.element("PK_GROUP").getTextTrim()).orElse(""));
            user.setNcUserRelevanceCode(Optional.ofNullable(obj.element("PK_PSNDOC").getTextTrim()).orElse(""));
            user.setNcUserRelevanceCode(Optional.ofNullable(obj.element("PK_BASE_DOC").getTextTrim()).orElse(""));
            user.setCreateTime(DateUtils.getNowDate());
            user.setCreateBy("NC");
            user.setSex("0");
            user.setPassword(password);
            userList.add(user);
        });
        return userList;
    }

    /**
     * 组装同步接口所需参数
     *
     * @param serverName  服务名称
     * @param factoryCode 厂商编码
     * @param dataSource  数据源
     * @return
     */
    private String getSynchronizeRequestParam(String serverName, String factoryCode, String dataSource) {
        String xml = "<params>" + "<factorycode>" + factoryCode + "</factorycode>" + "<servername>" + serverName + "</servername>" + "<datasource>" + dataSource + "</datasource>" + "</params>";
        return this.handleParameters(xml);
    }

    /**
     * 发起接口请求方法
     *
     * @param webUrl       接口地址
     * @param requestParam 请求参数
     * @return 结果
     * @throws Exception 异常
     */
    private Element getNcWsResult(String webUrl, String requestParam) throws Exception {
        String result;
        if (StrUtil.contains(webUrl, "?wsdl")) {
            // webService请求
            result = WebServiceUtil.sendWebService(webUrl, requestParam, TARGET_NAMESPACE);
        } else {
            // HTTP请求
            result = WebClientUtil.sendHttpWebService(webUrl, this.getSoapRequestParamForHttp(requestParam));
            result = XMLProcess.getXMLResult(result, "return", false);
        }
//        result = Java2XmlUtil.javaBeanToXml(result);
        final Document document = DocumentHelper.parseText(result);
        return document.getRootElement();
    }

    private String getSoapRequestParamForHttp(String xml) {
        String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:iim=\"http://ws.itf.imag.nc/IImageInterfaceService\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <iim:imageInterfaceService>\n" +
            "         <iim:string><![CDATA[ " + xml +
            " ]]></iim:string>\n" +
            "      </iim:imageInterfaceService>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";
        return soap;
    }

    /**
     * 同步组织
     * @param
     * @throws
     */
    @Override
    public List<SysDept> synchronizeDepartment(String factoryCode, String dataSource, String webUrl) throws Exception {
        String requestParam = this.getSynchronizeRequestParam(DEPART_INTERFACE, factoryCode, dataSource);
        log.info("同步组织机构接口请求报文：" + requestParam);
        Element ncWsResult = this.getNcWsResult(webUrl, requestParam);
        final String ncResult = ncWsResult.elementText(NC_WS_RESULT);
        if (!NC_WS_SUCCESS_RESULT.equals(ncResult)) {
            log.error("同步基础组织机构数据失败：" + ncResult);
            throw new Exception(ncResult);
        }
        List<SysDept> deptList = new ArrayList<>(16);
        Iterator<Element> orgList = ncWsResult.element("orginfo").elementIterator("org");
        orgList.forEachRemaining(obj -> {
            SysDept depart = new SysDept();
            depart.setDeptId(obj.elementText("pk_org"));
            String parentId = StrUtil.isNotEmpty(obj.elementText("pk_fatherorg")) ? obj.elementText("pk_fatherorg") : "0";
            depart.setParentId(parentId);
            depart.setDeptName(obj.elementTextTrim("name"));
            depart.setDeptCode(obj.elementText("code"));
            depart.setDeptTax(obj.elementText("taxpayercode"));
            depart.setDeptGroupid(obj.elementText("pk_group"));
            depart.setDeptVid(obj.elementText("pk_vid"));
            depart.setLeader(obj.elementText("creator"));
            depart.setStatus("0");
            depart.setDelFlag("0");
            depart.setCreateBy(obj.elementText("ts"));
            depart.setCreateTime(DateUtils.getNowDate());
            depart.setUpdateBy(obj.elementText("modifier"));
            depart.setUpdateTime(DateUtils.getNowDate());
            depart.setRemark("NCC");
            deptList.add(depart);
        });
        return deptList;
    }

    @Override
    public List<DataBillType> synchronizeBillType(String factoryCode, String dataSource, String webUrl) throws Exception {
        String requestParam = this.getSynchronizeRequestParam(BILL_INTERFACE, factoryCode, dataSource);
        log.info("同步单据类型接口请求报文：" + requestParam);
        Element ncWsResult = this.getNcWsResult(webUrl, requestParam);
        final String ncResult = ncWsResult.elementText(NC_WS_RESULT);
        if (!NC_WS_SUCCESS_RESULT.equals(ncResult)) {
            log.error("同步基础单据类型失败:" + ncResult);
            throw new Exception(ncResult);
        }
        List<DataBillType> billTypeList = new ArrayList<>(16);
        Iterator<Element> billTypeInfoList = ncWsResult.element("billtypeinfo").elementIterator("item");
        billTypeInfoList.forEachRemaining(obj -> {
            DataBillType bill = new DataBillType();
            bill.setId(IdUtil.simpleUUID());
            bill.setTypeCode(obj.elementTextTrim("billtypecode"));
            bill.setTypeName(obj.elementText("billtypename"));
            bill.setParentTypeId(obj.elementText("parentbilltype"));
            bill.setSystemCode(obj.elementText("systemcode"));
            bill.setParentSystem(obj.elementText("parentsystem"));
            bill.setOcrEnable("0");
            bill.setSystemName(obj.elementText("systemname"));
            bill.setGroupId(obj.elementText("pk_org"));
            billTypeList.add(bill);
        });
        return billTypeList;
    }

    @Override
    public DataCurrentTask synchronizeTaskFromNc(String factoryCode, String dataSource, String groupId, String barCode, String userId, String webUrl) throws Exception {
        String synchronizeTaskRequestParam = this.getSynchronizeTaskRequestParam(PULL_TASK_INTERFACE, factoryCode, dataSource, groupId, barCode, userId);
        log.info("拉取影像任务接口请求报文：" + synchronizeTaskRequestParam);
        Element ncWsResult = this.getNcWsResult(webUrl, synchronizeTaskRequestParam);
        final String ncResult = ncWsResult.elementText(NC_WS_RESULT);
        if (!NC_WS_SUCCESS_RESULT.equals(ncResult)) {
            log.error("主动拉取影像任务失败：" + ncResult);
            throw new Exception(ncResult);
        }
        Element billBody = ncWsResult.element("BillBody");
        Element bill = billBody.element("Bill");
        String id = bill.elementText("userid");
        if (StrUtil.isEmpty(id)) {
            log.error("主动拉取影像任务失败：节点userId为空");
            throw new Exception("主动拉取影像任务失败：节点userId为空");
        }
        String billNum = bill.elementText("BillCode");
        String businessSerialNo = bill.elementText("Busi_Serial_No");
        String billType = bill.elementText("BillType");
        String pkBillType = bill.elementText("pk_billtype");
        String billDate = bill.elementText("BillDate");
        String detailInfo = bill.elementText("DetailInfo");
        String userNo = bill.elementText("UserNo");
        String cash = bill.elementText("Cash");
        String userName = bill.elementText("UserName");
        String orgNo = bill.elementText("OrgNo");
        String orgName = bill.elementText("OrgName");
        String ocrType = bill.elementText("ocrType");
        String scanType = bill.elementText("ScanType");
        scanType = String.valueOf(Math.abs(Integer.parseInt(scanType)));

        DataCurrentTask dataCurrentTask = new DataCurrentTask();
        dataCurrentTask.setBillNum(billNum);
        dataCurrentTask.setBusinessSerialNo(businessSerialNo);
        dataCurrentTask.setScanType(scanType);
        dataCurrentTask.setPkBillType(pkBillType);
        dataCurrentTask.setBillType(billType);
        dataCurrentTask.setBillDate(Convert.toDate(billDate));
        dataCurrentTask.setCash(cash);
        dataCurrentTask.setUserId(id);
        dataCurrentTask.setGroupId(detailInfo);
        dataCurrentTask.setOrgCode(orgNo);
        dataCurrentTask.setOrgName(orgName);
        dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_SCAN);
        dataCurrentTask.setOcrType(ocrType);
        dataCurrentTask.insert();
        return dataCurrentTask;
    }

    private String getSynchronizeTaskRequestParam(String serverName, String factoryCode, String dataSource, String groupId, String barCode, String userId) {
        StringBuffer xml = new StringBuffer();
        xml.append("<params><factorycode>" + factoryCode + "</factorycode><servername>" + serverName + "</servername><datasource>" + dataSource + "</datasource>");
        xml.append("<billinfo>");
        xml.append("<datasource>" + dataSource + "</datasource>");
        xml.append("<groupid>").append(groupId).append("</groupid>");
        xml.append("<barcode>").append(barCode).append("</barcode>");
        xml.append("<opuserdatetime>").append(DateUtil.formatDateTime(new Date())).append("</opuserdatetime>");
        xml.append("<opusername></opusername>");
        xml.append("<opuserpk>").append(userId).append("</opuserpk>");
        xml.append("<opuseraccount></opuseraccount>");
        xml.append("</billinfo></params>");
        String xmlString = xml.toString();
        return this.handleParameters(xmlString);
    }
}
