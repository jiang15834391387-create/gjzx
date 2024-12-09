package org.smartlink.server.nc.service.brecheck.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.executor.RedissonLockExecutor;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.Constants;
import org.smartlink.server.nc.constant.FileStatusConstants;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.constant.ParamConstants;
import org.smartlink.server.nc.domain.DataCmInfo;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.SysDept;
import org.smartlink.server.nc.domain.invoice.DataMotorVehicleSale;
import org.smartlink.server.nc.domain.invoice.DataOcrInfo;
import org.smartlink.server.nc.domain.invoice.DataUsedCarSales;
import org.smartlink.server.nc.domain.invoice.bo.DataOcrDetailsBo;
import org.smartlink.server.nc.domain.invoice.vo.DataOcrDetailsVo;
import org.smartlink.server.nc.domain.modle.BaseEntity;
import org.smartlink.server.nc.domain.precheck.DataInvoicePrecheckBo;
import org.smartlink.server.nc.domain.precheck.DataInvoicePrecheckVo;
import org.smartlink.server.nc.enumd.CheckConstants;
import org.smartlink.server.nc.mapper.DataCmInfoMapper;
import org.smartlink.server.nc.mapper.DataCurrentTaskMapper;
import org.smartlink.server.nc.service.brecheck.BreCheckInvoiceService;
import org.smartlink.server.nc.service.nc.IDataImageFilesInfoService;
import org.smartlink.server.nc.service.nc.IDataOcrDetailsService;
import org.smartlink.server.nc.service.nc.ISysDeptService;
import org.smartlink.server.nc.service.precheck.IDataInvoicePrecheckService;
import org.smartlink.server.nc.utils.ExceptionUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author shidunkai
 * @title
 * @description
 * @date 2022-11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BreCheckInvoiceServiceImpl implements BreCheckInvoiceService {

    private final LockTemplate lockTemplate;
    private final IDataImageFilesInfoService imageFilesInfoService;
    private final IDataOcrDetailsService dataOcrDetailsService;
    private final DataCurrentTaskMapper currentTaskService;
    private final DataCmInfoMapper cmInfoService;
    private final ISysDeptService sysDeptService;
    private final IDataInvoicePrecheckService dataInvoicePrecheckService;
    private final String[] errorFileType={
            FileStatusConstants.INVOICE_INCONSISTENT,
            FileStatusConstants.INVOICE_SPECIAL_COMMODITY,
            FileStatusConstants.INVOICE_SENSITIVE,
            FileStatusConstants.INVOICE_HOLIDAY,
            FileStatusConstants.INVOICE_SERIAL_NUMBERS,
            FileStatusConstants.INVOICE_ALREADY_EXISTS,
            FileStatusConstants.INVOICE_SUPPLER
    };

    @Override
    public DataImageFilesInfo breCheckInvoice(DataImageFilesInfo dataImageFilesInfo, String orgCode) throws Exception {
        DataInvoicePrecheckBo dataInvoicePrecheckBo = new DataInvoicePrecheckBo();
        dataInvoicePrecheckBo.setCheckSwitch("1");
        boolean bipEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_BIP_BUSINESS));
        List<DataInvoicePrecheckVo> dataInvoicePrecheckVos = dataInvoicePrecheckService.queryList(dataInvoicePrecheckBo);
        if((FileStatusConstants.SAVED_SUCCESSFULLY.equalsIgnoreCase(dataImageFilesInfo.getFileStatus())
        ||FileStatusConstants.INVOICE_CHECK_SUCCESS.equalsIgnoreCase(dataImageFilesInfo.getFileStatus())
        ||FileStatusConstants.INVOICE_UPDATE.equalsIgnoreCase(dataImageFilesInfo.getFileStatus())) && CollectionUtil.isNotEmpty(dataInvoicePrecheckVos)){
            //0节假日1发票连号2敏感词发票3特殊商品发票4抬头校验5发票重复6打印一致性校验
            String fileId = dataImageFilesInfo.getFileId();
            String fileType = dataImageFilesInfo.getFileType();
            List<String> imageList = imageFilesInfoService.selectByBatchId(dataImageFilesInfo.getBatchId()).stream().map(DataImageFilesInfo::getFileId).collect(Collectors.toList());
            String[] type = {fileType};
            if (fileType.equals(InvoiceConstants.INVOICE_MUCH_NCC)) {
                type = dataImageFilesInfo.getIncludeTypeArr().split(",");
            }
            for (String s : type) {
                String className = InvoiceConstants.INVOICE_ClASS_TYPE.get(s);
                //获取校验内容
                if (StrUtil.isNotEmpty(className)) {
                    BaseEntity queryBaseEntity = (BaseEntity) Class.forName(className).newInstance();
                    QueryWrapper<BaseEntity> baseEntityLaqwFileId = new QueryWrapper<>();
                    baseEntityLaqwFileId.eq("file_id", fileId);
                    List<BaseEntity> baseEntityList = queryBaseEntity.selectList(baseEntityLaqwFileId);
                    for (BaseEntity baseEntityInvoice : baseEntityList) {
                        for (DataInvoicePrecheckVo dataInvoicePrecheckVo : dataInvoicePrecheckVos) {
                            //走校验
                            if ("0".equalsIgnoreCase(dataInvoicePrecheckVo.getCheckType())){
                                //节假日
                                breCheckHolidays(dataImageFilesInfo, baseEntityInvoice, dataInvoicePrecheckVo.getCheckContent1());
                                //跳出
                                if (ArrayUtil.containsIgnoreCase(errorFileType,dataImageFilesInfo.getFileStatus())){
                                    continue;
                                }
                            }
                            if ("1".equalsIgnoreCase(dataInvoicePrecheckVo.getCheckType())){
                                //连号
                                breCheckNumbers(dataImageFilesInfo, baseEntityInvoice);
                                //跳出
                                if (ArrayUtil.containsIgnoreCase(errorFileType,dataImageFilesInfo.getFileStatus())){
                                    continue;
                                }
                            }
                            if ("2".equalsIgnoreCase(dataInvoicePrecheckVo.getCheckType())){
                                //敏感词
                                breCheckSensitive(dataImageFilesInfo, baseEntityInvoice, dataInvoicePrecheckVo.getCheckContent1());
                                //跳出
                                if (ArrayUtil.containsIgnoreCase(errorFileType,dataImageFilesInfo.getFileStatus())){
                                    continue;
                                }
                            }
                            if ("3".equalsIgnoreCase(dataInvoicePrecheckVo.getCheckType())){
                                //特殊商品
                                breCheckCommodity(dataImageFilesInfo, baseEntityInvoice, dataInvoicePrecheckVo.getCheckContent1());
                                //跳出
                                if (ArrayUtil.containsIgnoreCase(errorFileType,dataImageFilesInfo.getFileStatus())){
                                    continue;
                                }
                            }
                            if ("4".equalsIgnoreCase(dataInvoicePrecheckVo.getCheckType())){
                                //抬头
                                breCheckLookUp(dataImageFilesInfo, baseEntityInvoice, orgCode,dataInvoicePrecheckVo.getCheckContent1());
                                //跳出
                                if (ArrayUtil.containsIgnoreCase(errorFileType,dataImageFilesInfo.getFileStatus())){
                                    continue;
                                }
                            }if ("5".equalsIgnoreCase(dataInvoicePrecheckVo.getCheckType()) && !bipEnabled){
                                //重复
                                breCheckRepeat(dataImageFilesInfo, imageList,baseEntityInvoice,s);
                                //跳出
                                if (ArrayUtil.containsIgnoreCase(errorFileType,dataImageFilesInfo.getFileStatus())){
                                    continue;
                                }
                            }if ("6".equalsIgnoreCase(dataInvoicePrecheckVo.getCheckType())){
                                //一致
                                breCheckPrint(dataImageFilesInfo, baseEntityInvoice);
                                //跳出
                            }if ("7".equalsIgnoreCase(dataInvoicePrecheckVo.getCheckType())){
                                //发票联次
                                breFromType(dataImageFilesInfo, baseEntityInvoice);
                                //跳出
                            }
                        }
                    }
                }
            }
        }

        return dataImageFilesInfo;
    }

    /**
     * 节假日
     *
     * @param dataImageFilesInfo 节假日
     */
    private void breCheckHolidays(DataImageFilesInfo dataImageFilesInfo, BaseEntity invoiceMsg, String rule) throws Exception {
        Date invoiceDate = null;
        if (invoiceMsg instanceof DataOcrInfo) {
            DataOcrInfo dataOcrInfo = (DataOcrInfo) invoiceMsg;
            invoiceDate = dataOcrInfo.getInvoiceDate();
        } else if (invoiceMsg instanceof DataUsedCarSales) {
            DataUsedCarSales dataUsedCarSales = (DataUsedCarSales) invoiceMsg;
            invoiceDate = dataUsedCarSales.getInvoiceDate();
        } else if (invoiceMsg instanceof DataMotorVehicleSale) {
            DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) invoiceMsg;
            invoiceDate = dataMotorVehicleSale.getInvoiceDate();
        }
        if (ArrayUtil.containsIgnoreCase(rule.split(","), DateUtil.format(invoiceDate, "MM-dd"))) {
            dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_HOLIDAY);
            dataImageFilesInfo.setMessage("违反规则：节假日发票：" + DateUtil.format(invoiceDate, "MM-dd"));
        }
    }

    /**
     * 发票连号
     *
     * @param dataImageFilesInfo 发票连号
     */
    private void breCheckNumbers(DataImageFilesInfo dataImageFilesInfo, BaseEntity invoiceMsg) {
        String invoiceNumber = "";
        if (invoiceMsg instanceof DataOcrInfo) {
            DataOcrInfo dataOcrInfo = (DataOcrInfo) invoiceMsg;
            invoiceNumber = dataOcrInfo.getInvoiceNumber();
        } else if (invoiceMsg instanceof DataMotorVehicleSale) {
            DataMotorVehicleSale dataOcrInfo = (DataMotorVehicleSale) invoiceMsg;
            invoiceNumber = dataOcrInfo.getInvoiceNumber();
        } else if (invoiceMsg instanceof DataUsedCarSales) {
            DataUsedCarSales dataOcrInfo = (DataUsedCarSales) invoiceMsg;
            invoiceNumber = dataOcrInfo.getInvoiceNumber();
        }
        if (StrUtil.isBlank(invoiceNumber)) {
            // 发票号码为空
            return;
        }
        Integer cacheInvoiceNumber = Convert.toInt(RedisUtils.getCacheObject(invoiceNumber), 0);
        // 发票号码取出来为空
        if (cacheInvoiceNumber == 0) {
            // 放进Redis
            RedisUtils.setCacheObject(invoiceNumber, invoiceNumber, 2, TimeUnit.MINUTES);
            return;
        }
        LockInfo lockInfo = lockTemplate.lock(invoiceNumber, 300L, 500L, RedissonLockExecutor.class);
        if (null == lockInfo) {
            throw new RuntimeException("业务处理中,请稍后再试");
        }
        try {
            if (Convert.toStr(cacheInvoiceNumber + 1).equals(invoiceNumber) || Convert.toStr(cacheInvoiceNumber - 1).equals(invoiceNumber)) {
                dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_SERIAL_NUMBERS);
                dataImageFilesInfo.setMessage("违反规则：发票连号");
            }
        } finally {
            lockTemplate.releaseLock(lockInfo);
        }
    }

    /**
     * 敏感词
     *
     * @param dataImageFilesInfo 敏感词
     */
    private void breCheckSensitive(DataImageFilesInfo dataImageFilesInfo, BaseEntity invoiceMsg, String rule) {
        String sellerName = null;
        if (invoiceMsg instanceof DataOcrInfo) {
            DataOcrInfo dataOcrInfo = (DataOcrInfo) invoiceMsg;
            sellerName = dataOcrInfo.getSellerName();
        }
        String[] split = rule.split(",");
        for (String s : split) {
            if (StrUtil.isNotBlank(sellerName) && sellerName.contains(s)){
                dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_SENSITIVE);
                dataImageFilesInfo.setMessage("违反规则：销售方包含敏感词");
            }
        }
    }

    /**
     * 特殊商品
     *
     * @param dataImageFilesInfo 特殊商品
     */
    private void breCheckCommodity(DataImageFilesInfo dataImageFilesInfo, BaseEntity invoiceMsg, String rule) {
        if (invoiceMsg instanceof DataOcrInfo) {
            DataOcrDetailsBo dataOcrDetails = new DataOcrDetailsBo();
            dataOcrDetails.setFileId(((DataOcrInfo) invoiceMsg).getFileId());
            List<DataOcrDetailsVo> dataOcrDetailsVos = dataOcrDetailsService.queryList(dataOcrDetails);
            for (DataOcrDetailsVo dataOcrDetailsVo : dataOcrDetailsVos) {
                String[] split = rule.split(",");
                for (String s : split) {
                    if (StrUtil.isNotBlank(dataOcrDetailsVo.getName()) && dataOcrDetailsVo.getName().contains(s)){
                        dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_SPECIAL_COMMODITY);
                        dataImageFilesInfo.setMessage("违反规则：明细包含特殊商品");
                    }
                }
            }
        }
    }

    /**
     * 抬头校验
     *
     * @param dataImageFilesInfo 抬头校验
     */
    private void breCheckLookUp(DataImageFilesInfo dataImageFilesInfo, BaseEntity invoiceMsg, String orgCode, String rule) {
        String taxCode="";
        String taxCodeName="";
        if (invoiceMsg instanceof DataOcrInfo) {
            DataOcrInfo dataOcrInfo = (DataOcrInfo) invoiceMsg;
            taxCode=dataOcrInfo.getBuyerNo();
            taxCodeName=dataOcrInfo.getBuyerName();
        } else if (invoiceMsg instanceof DataUsedCarSales) {
            DataUsedCarSales dataUsedCarSales = (DataUsedCarSales) invoiceMsg;
            taxCode=dataUsedCarSales.getBuyerId();
            taxCodeName=dataUsedCarSales.getBuyerName();
        } else if (invoiceMsg instanceof DataMotorVehicleSale) {
            DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) invoiceMsg;
            taxCode=dataMotorVehicleSale.getBuyerId();
            taxCodeName=dataMotorVehicleSale.getBuyerName();
        }else{
            return;
        }
        String message = dataImageFilesInfo.getMessage();
        String fileStatus = dataImageFilesInfo.getFileStatus();
        log.info("开始抬头校验，传入的税号为" + taxCode + "========传入的名称为" + taxCodeName);
        boolean b = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.INVOICE_CHECK_LOOKUP));
        if (b){
            log.info("进入NCC抬头校验");
            SysDept sysDept = sysDeptService.selectDeptById(orgCode);
            String taxNo=sysDept.getDeptTax();
            String taxName=sysDept.getDeptName();
            if ((StrUtil.isNotBlank(taxNo) && !taxNo.equalsIgnoreCase(taxCode))
                &&((StrUtil.isNotBlank(taxName) && !taxName.equalsIgnoreCase(taxCodeName))
            )){
                dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_SUPPLER);
                dataImageFilesInfo.setMessage("违反规则：抬头信息有误");
            }
        }else{
            log.info("进入影像抬头校验");
            JSONArray objects = JSONUtil.parseArray(rule);
            dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_SUPPLER);
            dataImageFilesInfo.setMessage("违反规则：抬头信息有误");
            for (Object object : objects) {
                String s = object.toString();
                JSONObject jsonObject = JSONObject.parseObject(s);
                String taxNo=jsonObject.getString("key");
                String taxName=jsonObject.getString("value");
                if (((StrUtil.isNotBlank(taxNo) && taxNo.equalsIgnoreCase(taxCode)))
                        &&(((StrUtil.isNotBlank(taxName) && taxName.equalsIgnoreCase(taxCodeName)))
                )){
                    dataImageFilesInfo.setFileStatus(fileStatus);
                    dataImageFilesInfo.setMessage(message);
                    log.info("抬头校验成功");
                    return;
                }
            }
        }
    }

    /**
     * 重复校验
     *
     * @param dataImageFilesInfo 重复校验
     */
    private void breCheckRepeat(DataImageFilesInfo dataImageFilesInfo, List<String> imageList, BaseEntity invoiceMsg, String fileType) throws Exception {
        String className = InvoiceConstants.INVOICE_ClASS_TYPE.get(fileType);
        Class<?> invoiceClass = Class.forName(className);
        String invoiceCode = null;
        String invoiceNumber = null;
        String checkInvoice = null;
        try {
            Field invoiceCodeField = invoiceClass.getDeclaredField("invoiceCode");
            invoiceCodeField.setAccessible(true);
            invoiceCode = (String) invoiceCodeField.get(invoiceMsg);
        } catch (Exception e) {
            log.error("invoiceCode: {}",e.getMessage());
        }
        try {
            Field invoiceNumberField = invoiceClass.getDeclaredField("invoiceNumber");
            invoiceNumberField.setAccessible(true);
            invoiceNumber = (String) invoiceNumberField.get(invoiceMsg);
        } catch (Exception e) {
            log.error("invoiceNumber: {}", e.getMessage());
        }
        try {
            Field invoiceNumberField = invoiceClass.getDeclaredField("checkInvoice");
            invoiceNumberField.setAccessible(true);
            checkInvoice = (String) invoiceNumberField.get(invoiceMsg);
        } catch (Exception e) {
            log.error("checkInvoice: {}", e.getMessage());
        }
        QueryWrapper<BaseEntity> baseEntityLaqw = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(invoiceCode)) {
            baseEntityLaqw.eq("invoice_code", invoiceCode);
        }
        if (StrUtil.isNotEmpty(invoiceNumber)) {
            baseEntityLaqw.eq("invoice_number", invoiceNumber);
        }
        if (StrUtil.isNotEmpty(checkInvoice)) {
            baseEntityLaqw.eq("check_invoice", CheckConstants.INVOKE_CHECKED_CODE);
        }
        boolean invoiceIsStaging = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.INVOICE_IS_STAGING));
        if (invoiceIsStaging) {
            baseEntityLaqw.eq("is_staging", Constants.CONFIG_OFF_STATUS);
        }
        if (!Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.INVOICE_CHECK_REPEAT))){
            baseEntityLaqw.in("file_id", imageList);
        }
        if (StrUtil.isBlank(invoiceCode) && StrUtil.isBlank(invoiceNumber)) {
            return;
        }
        List<BaseEntity> list = invoiceMsg.selectList(baseEntityLaqw);
        if (CollUtil.isNotEmpty(list) && list.size() > 1 || (invoiceIsStaging && StrUtil.equals(Constants.IS_STAGING,dataImageFilesInfo.getIsStaging()) && CollUtil.isNotEmpty(list))) {
            StringBuilder msg = new StringBuilder("发票已存在:");
            for (BaseEntity repeatBaseEntity : list) {
                Field baseEntityFileId = null;
                try {
                    baseEntityFileId = invoiceClass.getDeclaredField("fileId");
                    baseEntityFileId.setAccessible(true);
                    String o = (String) baseEntityFileId.get(repeatBaseEntity);
                    String repeatBill = getRepeatBill(o);
                    if (StrUtil.isNotBlank(repeatBill) && !dataImageFilesInfo.getFileId().equals(o)) {
                        msg.append(repeatBill);
                    }
                } catch (NoSuchFieldException e) {
                    log.error("重复校验异常:{}", ExceptionUtil.getExceptionMessage(e));
                }
            }
            dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_ALREADY_EXISTS);
            dataImageFilesInfo.setMessage(msg.toString());
        }
    }

    /**
     * 打印号码
     *
     * @param dataImageFilesInfo 打印号码一致校验
     * @return
     */
    private DataImageFilesInfo breCheckPrint(DataImageFilesInfo dataImageFilesInfo, BaseEntity invoiceMsg) {
        if(invoiceMsg instanceof DataOcrInfo){
            DataOcrInfo dataOcrInfo= (DataOcrInfo) invoiceMsg;
            if ((StrUtil.isNotBlank(dataOcrInfo.getRightInvoiceNumber()) && !StrUtil.equalsAnyIgnoreCase(dataOcrInfo.getRightInvoiceNumber(),dataOcrInfo.getInvoiceNumber()))){
                dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_INCONSISTENT);
                dataImageFilesInfo.setMessage("违反规则：右侧打印信息不一致");
            }
        }
        return dataImageFilesInfo;
    }

    /**
     * 联次校验
     *
     * @param dataImageFilesInfo 联次校验
     * @return
     */
    private DataImageFilesInfo breFromType(DataImageFilesInfo dataImageFilesInfo, BaseEntity invoiceMsg) {
        if(invoiceMsg instanceof DataOcrInfo){
            DataOcrInfo dataOcrInfo= (DataOcrInfo) invoiceMsg;
            if ((StrUtil.isNotBlank(dataOcrInfo.getPageNumber()) && !"发票联".equalsIgnoreCase(dataOcrInfo.getPageNumber()))){
                dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_FROMNUMBERERROR);
                dataImageFilesInfo.setMessage("违反规则：非发票联次");
            }
        }
        return dataImageFilesInfo;
    }

    public String getRepeatBill(String fileId) {
        DataImageFilesInfo byFileId = this.imageFilesInfoService.selectById(fileId);
        if (ObjectUtil.isEmpty(byFileId)) {
            return null;
        }
        if ("1".equals(byFileId.getParentFileId())) {
            return null;
        }
        String batchId = byFileId.getBatchId();
        DataCmInfo dataCmInfo = new DataCmInfo();
        dataCmInfo.setBatchId(batchId);
        List<DataCmInfo> byBatchId = cmInfoService.selectList(new QueryWrapper<DataCmInfo>().setEntity(dataCmInfo));
        if (CollUtil.isEmpty(byBatchId)) {
            return null;
        }
        DataCurrentTask dataCurrentTask = new DataCurrentTask();
        dataCurrentTask.setBusinessSerialNo(byBatchId.get(0).getBusinessSerialNo());
        List<DataCurrentTask> currentTaskList = currentTaskService.selectList(new QueryWrapper<DataCurrentTask>().setEntity(dataCurrentTask));
        if (CollUtil.isEmpty(currentTaskList)) {
            return null;
        }
        return currentTaskList.get(0).getBillNum() + "影像任务ID:" + currentTaskList.get(0).getBusinessSerialNo() ;
    }

}
