package org.smartlink.common.check.task;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.util.StringUtils;
import org.smartlink.common.check.constant.FileStatusConstants;
import org.smartlink.common.check.constant.InvoiceConstants;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.check.enumd.InvoiceGlorityEnumd;
import org.smartlink.common.check.exception.CheckException;
import org.smartlink.common.check.factory.CheckFactory;
import org.smartlink.common.check.invoice.DataImageFilesInfo;
import org.smartlink.common.check.invoice.DataOcrDetails;
import org.smartlink.common.check.invoice.DataOcrInfo;
import org.smartlink.common.check.mapper.DataOcrDetailsMapper;
import org.smartlink.common.check.mapper.DataOcrInfoMapper;
import org.smartlink.common.check.service.IDataImageFilesInfoService;
import org.smartlink.common.check.service.IDataOcrDetailsService;
import org.smartlink.common.check.service.IDataOcrInfoService;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class CheckInvoiceTask {
    private final IDataOcrInfoService dataOcrInfoService;
    private final IDataImageFilesInfoService imageFilesInfoService;
    private final DataOcrDetailsMapper detailsMapper;
    private final DataOcrInfoMapper ocrInfoMapper;
    public CheckInvoiceTask(IDataOcrInfoService dataOcrInfoService, IDataImageFilesInfoService imageFilesInfoService, IDataOcrDetailsService ocrDetailsService, DataOcrDetailsMapper detailsMapper, DataOcrInfoMapper ocrInfoMapper) {
        this.dataOcrInfoService = dataOcrInfoService;
        this.imageFilesInfoService = imageFilesInfoService;
        this.detailsMapper = detailsMapper;
        this.ocrInfoMapper = ocrInfoMapper;
    }
    @Scheduled(fixedRate = 120000)
//    @Scheduled(fixedRate = 5000)
    public void check() throws InvocationTargetException, IllegalAccessException {
        System.out.println("发票查验定时器任务启动了");
        final List<String> vatInvoiceList = InvoiceGlorityEnumd.getVatInvoiceCodes();
        final LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(i -> i.eq(DataImageFilesInfo::getFileStatus, FileStatusConstants.INVOICE_CHECK_FAIL).in(DataImageFilesInfo::getInvoice, vatInvoiceList));
        final List<DataImageFilesInfo> list = this.imageFilesInfoService.listlqw(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            log.info("没有查询到需要查验的文件图片！");
            return;
        }

        log.info("查询到{}条需要查验的文件图片", list.size());
        int i = 1;
        for (DataImageFilesInfo filesInfo : list) {
            log.info("正在查验第{}张发票", i);
            InvoiceCheckParamDTO invoiceCheckParamDTO=null;
            switch (filesInfo.getInvoice()){
                case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                    log.info("查验二手车第"+i+"张发票");
                    break;
                case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                    log.info("查验机动车第"+i+"张发票");
                    break;
                case InvoiceConstants.GLORITY_TAX_SPECIAL_INVOICE:
                    log.info("查验增值税专票第"+i+"张发票");
                    final DataOcrInfo ocrInfo = this.dataOcrInfoService.getByFileId(filesInfo.getFileId());
                    invoiceCheckParamDTO = new InvoiceCheckParamDTO();
                    invoiceCheckParamDTO.setCode(ocrInfo.getInvoiceCode());
                    invoiceCheckParamDTO.setNumber(ocrInfo.getInvoiceNumber());
                    invoiceCheckParamDTO.setDate(ocrInfo.getInvoiceDate());
                    invoiceCheckParamDTO.setType(filesInfo.getInvoice());
                    invoiceCheckParamDTO.setElectron_mark(Integer.valueOf(ocrInfo.getElectronicMark()));
                    invoiceCheckParamDTO.setPretax_amount(ocrInfo.getPretaxAmount());
                    break;
                case InvoiceConstants.GLORITY_TAX_INVOICE:
                case InvoiceConstants.GLORITY_ELECTRONIC_INVOICE:
                case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
                    log.info("查验ocr第"+i+"张发票");
                    final DataOcrInfo ocrInfos = this.dataOcrInfoService.getByFileId(filesInfo.getFileId());
                    invoiceCheckParamDTO = new InvoiceCheckParamDTO();
                    invoiceCheckParamDTO.setCode(ocrInfos.getInvoiceCode());
                    invoiceCheckParamDTO.setNumber(ocrInfos.getInvoiceNumber());
                    invoiceCheckParamDTO.setDate(ocrInfos.getInvoiceDate());
                    invoiceCheckParamDTO.setType(filesInfo.getInvoice());
                    String checkCodes = ocrInfos.getCheckCode();
                    if (StringUtils.hasText(checkCodes) && checkCodes.length() > 5) {
                        checkCodes = checkCodes.substring(checkCodes.length() - 6);
                    }
                    invoiceCheckParamDTO.setCheck_code(checkCodes);
                    break;
                default:
                    throw new CheckException("发票类型有误!");
            }
            this.checkInvoice(filesInfo,invoiceCheckParamDTO);
            i++;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void checkInvoice(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO invoiceCheckParamDTO) throws InvocationTargetException, IllegalAccessException {
        BaseEntity baseEntity= CheckFactory.instance().checkInvoke(filesInfo, invoiceCheckParamDTO);
        if (!filesInfo.getFileStatus().equals(FileStatusConstants.INVOICE_CHECK_FAIL)) {
            DataOcrInfo dataOcrInfo = new DataOcrInfo();
            BeanUtils.copyProperties(baseEntity, dataOcrInfo);
            List<DataOcrDetails> arrayList = new ArrayList<>();
            if (dataOcrInfo.getDetails() != null) {
                List<DataOcrDetails> details = dataOcrInfo.getDetails();
                DataOcrDetails ocrDetail = new DataOcrDetails();
                for (DataOcrDetails ocrDetails : details) {
                    BeanUtils.copyProperties(ocrDetails, ocrDetail);
                    ocrDetail.setFileId(filesInfo.getFileId());
                    arrayList.add(ocrDetail);
                }
            }
            //根据file_id修改ocr基本信息
            ocrInfoMapper.update(dataOcrInfo, new LambdaUpdateWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, filesInfo.getFileId()));
            for (DataOcrDetails detail : arrayList) {
                detailsMapper.update(detail, new LambdaUpdateWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, filesInfo.getFileId()));
            }
        }
    }
}
