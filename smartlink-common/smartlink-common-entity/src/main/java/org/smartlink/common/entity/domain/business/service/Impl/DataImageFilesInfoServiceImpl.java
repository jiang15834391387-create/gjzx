package org.smartlink.common.entity.domain.business.service.Impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.enums.FileStatusEnumd;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.constant.InvoiceTypeConstants;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.domain.bo.DataImageFilesInfoBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataImageFilesInfoVo;
import org.smartlink.common.entity.domain.business.mapper.*;
import org.smartlink.common.entity.domain.business.service.IDataImageFilesInfoService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 图片文件Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataImageFilesInfoServiceImpl implements IDataImageFilesInfoService {

    private final DataImageFilesInfoMapper baseMapper;
    private final DataNonTaxMapper dataNonTaxMapper;
    private final DataMotorVehicleSaleMapper motorVehicleSaleMapper;
    private final DataUsedCarSalesMapper usedCarSalesMapper;
    private final DataFlightItineraryMapper flightItineraryMapper;
    private final DataFlightsItineraryDetailMapper flightsItineraryDetailMapper;
    private final DataSteamerTicketMapper steamerTicketMapper;
    private final DataMedicalTreatmentMapper dataMedicalTreatmentMapper;
    private final DataMedicalTreatmentDetailMapper dataMedicalTreatmentDetailMapper;
    private final DataQuotaInvoiceMapper quotaInvoiceMapper;
    private final DataTaxiTicketsMapper taxiTicketsMapper;
    private final DataRailwayTicketMapper railwayTicketMapper;
    private final DataPassengerCarMapper passengerCarMapper;
    private final DataTollRoadsMapper tollRoadsMapper;
    private final DataReceiptMapper dataReceiptMapper;
    private final DataDidiItineraryMapper didiItineraryMapper;
    private final DataDidiItineraryDetailsMapper didiItineraryDetailsMapper;
    private final DataDutyPaidProofMapper paidProofMapper;
    private final DataDutyPaidProofDetailsMapper paidProofDetailsMapper;
    private final DataCustomsImportGoodsDetailMapper customsImportGoodsDetailMapper;
    private final DataCustomsImxportGoodsMapper customsImportGoodsMapper;
    private final DataCustomsExportGoodsMapper customsExportGoodsMapper;
    private final DataCustomsExportGoodsDetailMapper customsExportGoodsDetailMapper;
    private final DataCustomsSpecialPaymentMapper customsSpecialPaymentMapper;
    private final DataElectronicTransportationGoodsMapper paymentMapper;
    private final DataOcrInfoMapper ocrInfoMapper;
    private final DataOcrDetailsMapper ocrDetailsMapper;
    /**
     * 查询图片文件
     *
     * @param fileId 主键
     * @return 图片文件
     */
    @Override
    public DataImageFilesInfoVo queryById(String fileId){
        return baseMapper.selectVoById(fileId);
    }

    /**
     * 分页查询图片文件列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 图片文件分页列表
     */
    @Override
    public TableDataInfo<DataImageFilesInfoVo> queryPageList(DataImageFilesInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataImageFilesInfo> lqw = buildQueryWrapper(bo);
        Page<DataImageFilesInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的图片文件列表
     *
     * @param bo 查询条件
     * @return 图片文件列表
     */
    @Override
    public List<DataImageFilesInfoVo> queryList(DataImageFilesInfoBo bo) {
        LambdaQueryWrapper<DataImageFilesInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public DataImageFilesInfo selectOneByFileId(String fileId) {
        DataImageFilesInfo result = buildQueryWrapperByFileId(fileId);
        return result;
    }

    private DataImageFilesInfo buildQueryWrapperByFileId(String fileId) {
        LambdaQueryWrapper<DataImageFilesInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DataImageFilesInfo::getFileId, fileId); // 使用 Lambda 表达式
        return baseMapper.selectOne(lambdaQueryWrapper); // 返回查询结果
    }

    private LambdaQueryWrapper<DataImageFilesInfo> buildQueryWrapper(DataImageFilesInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataImageFilesInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getParentFileId()), DataImageFilesInfo::getParentFileId, bo.getParentFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getGlorityInvestigationNo()), DataImageFilesInfo::getGlorityInvestigationNo, bo.getGlorityInvestigationNo());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoice()), DataImageFilesInfo::getInvoice, bo.getInvoice());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), DataImageFilesInfo::getBarCode, bo.getBarCode());
        lqw.eq(StringUtils.isNotBlank(bo.getBatchId()), DataImageFilesInfo::getBatchId, bo.getBatchId());
        lqw.eq(StringUtils.isNotBlank(bo.getGlorityImageId()), DataImageFilesInfo::getGlorityImageId, bo.getGlorityImageId());
        lqw.eq(StringUtils.isNotBlank(bo.getCip()), DataImageFilesInfo::getCip, bo.getCip());
        lqw.eq(StringUtils.isNotBlank(bo.getFileMd5()), DataImageFilesInfo::getFileMd5, bo.getFileMd5());
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), DataImageFilesInfo::getFileName, bo.getFileName());
        lqw.eq(StringUtils.isNotBlank(bo.getFileSize()), DataImageFilesInfo::getFileSize, bo.getFileSize());
        lqw.eq(StringUtils.isNotBlank(bo.getFileFlowStatus()), DataImageFilesInfo::getFileFlowStatus, bo.getFileFlowStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getFileType()), DataImageFilesInfo::getFileType, bo.getFileType());
        lqw.eq(StringUtils.isNotBlank(bo.getFolderId()), DataImageFilesInfo::getFolderId, bo.getFolderId());
        lqw.eq(StringUtils.isNotBlank(bo.getImageSecret()), DataImageFilesInfo::getImageSecret, bo.getImageSecret());
        lqw.eq(StringUtils.isNotBlank(bo.getSurl()), DataImageFilesInfo::getSurl, bo.getSurl());
        lqw.eq(StringUtils.isNotBlank(bo.getIurl()), DataImageFilesInfo::getIurl, bo.getIurl());
        lqw.eq(StringUtils.isNotBlank(bo.getPurl()), DataImageFilesInfo::getPurl, bo.getPurl());
        lqw.eq(StringUtils.isNotBlank(bo.getLurl()), DataImageFilesInfo::getLurl, bo.getLurl());
        lqw.eq(StringUtils.isNotBlank(bo.getMessage()), DataImageFilesInfo::getMessage, bo.getMessage());
        lqw.like(StringUtils.isNotBlank(bo.getDocumentName()), DataImageFilesInfo::getDocumentName, bo.getDocumentName());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckStatus()), DataImageFilesInfo::getCheckStatus, bo.getCheckStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getFileStatus()), DataImageFilesInfo::getFileStatus, bo.getFileStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getSortValue()), DataImageFilesInfo::getSortValue, bo.getSortValue());
        lqw.eq(StringUtils.isNotBlank(bo.getRotateAngle()), DataImageFilesInfo::getRotateAngle, bo.getRotateAngle());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataImageFilesInfo::getDeleteFlag, bo.getDeleteFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getPageTime()), DataImageFilesInfo::getPageTime, bo.getPageTime());
        lqw.eq(StringUtils.isNotBlank(bo.getIncludeTypeArr()), DataImageFilesInfo::getIncludeTypeArr, bo.getIncludeTypeArr());
        return lqw;
    }

    /**
     * 新增图片文件
     *
     * @param bo 图片文件
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataImageFilesInfoBo bo) {
        DataImageFilesInfo add = MapstructUtils.convert(bo, DataImageFilesInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setFileId(add.getFileId());
        }
        return flag;
    }

    @Override
    public Boolean insert(DataImageFilesInfo dataImageFilesInfo) {
        boolean flag = baseMapper.insert(dataImageFilesInfo) > 0;
        return flag;
    }

    /**
     * 修改图片文件
     *
     * @param bo 图片文件
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataImageFilesInfoBo bo) {
        DataImageFilesInfo update = MapstructUtils.convert(bo, DataImageFilesInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataImageFilesInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除图片文件信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public List<DataImageFilesInfo> listlqw(LambdaQueryWrapper<DataImageFilesInfo> queryWrapper) {
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public R<Void> bindAndRelieve(String workflowId, List<Map<String, String>> fileIds, Boolean isBinding) {
        if (isBinding){
            if (StringUtils.isBlank(workflowId)) {
                return R.fail();
            }
            if(!CollectionUtils.isEmpty(fileIds)){
                for (Map<String, String> file : fileIds) {
                    String id = file.get("id");
                    String type = file.get("type");
                    if(!StringUtils.isEmpty(id)&&!StringUtils.isEmpty(type)){
                        //根据id查询对应的发票fileId
                        String fileId = selectInvoice(type,id);
                        //根据发票fileId处理业务
                        LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(DataImageFilesInfo::getFileId, fileId);
                        DataImageFilesInfo filesInfo = baseMapper.selectOne(queryWrapper);
                        if(filesInfo!=null){
                            filesInfo.setWorkflowId(workflowId);
                            filesInfo.setFileFlowStatus(FileStatusEnumd.REIMBURSED.getCode());
                            baseMapper.updateById(filesInfo);
                        }
                    }
                }
            }

          }else {
            return relieveWorkflow(workflowId);
        }
        return R.ok();
    }


    private R<Void> relieveWorkflow(String workflowId) {
        LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataImageFilesInfo::getWorkflowId, workflowId);
        List<DataImageFilesInfo> filesInfoList = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(filesInfoList)){
            return R.fail();
        }
        for (DataImageFilesInfo info : filesInfoList) {
            info.setWorkflowId(null);
            info.setFileFlowStatus(FileStatusEnumd.TO_BE_REIMBURSED.getCode());
            baseMapper.updateById(info);
        }
        return R.ok();
    }

    @Override
    public R<List<DataImageFilesInfo>> getAllFiles(String workflowId, List<String> fileIds) {
        if(StringUtils.isEmpty(workflowId)&& CollectionUtils.isEmpty(fileIds)){
            return R.fail();
        }
        LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
         if (ObjectUtil.isNotEmpty(workflowId)){
            queryWrapper.eq(DataImageFilesInfo::getWorkflowId, workflowId);
        }else if (ObjectUtil.isNotEmpty(fileIds)){
            queryWrapper.in(DataImageFilesInfo::getFileId, fileIds);
        }
        List<DataImageFilesInfo> dataImageFilesInfos = baseMapper.selectList(queryWrapper);

        return R.ok(dataImageFilesInfos);
    }

    @Override
    public Long checkFile(List<Map<String, String>> fileIds) {
//        LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.in(DataImageFilesInfo::getFileId, fileIds);
//        queryWrapper.isNotNull(DataImageFilesInfo::getWorkflowId);
//        return baseMapper.selectCount(queryWrapper);
        List<String> list = new ArrayList<>();
        LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
        if(!CollectionUtils.isEmpty(fileIds)){
            for (Map<String, String> file : fileIds) {
                String id = file.get("id");
                String type = file.get("type");
                if(!StringUtils.isEmpty(id)&&!StringUtils.isEmpty(type)){
                    //根据id查询对应的发票fileId
                    String fileId = selectInvoice(type,id);
                    if (StringUtils.isNotBlank(fileId)){
                        list.add(fileId);
                    }
                }
            }
        }
        queryWrapper.in(DataImageFilesInfo::getFileId, list);
        queryWrapper.isNotNull(DataImageFilesInfo::getWorkflowId);
        return baseMapper.selectCount(queryWrapper);
    }
    private String selectInvoice(String invoiceType, String invoiceId) {
        switch (invoiceType) {
            //增值税、机打
            case InvoiceTypeConstants.GLORITY_TAX_SPECIAL_CODE:
            case InvoiceTypeConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
            case InvoiceTypeConstants.GLORITY_TAX_CODE:
            case InvoiceTypeConstants.GLORITY_ELECTRONIC_CODE:
            case InvoiceTypeConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
            case InvoiceTypeConstants.GLORITY_ROLL_TICKET_CODE:
            case InvoiceTypeConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
            case InvoiceTypeConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
            case InvoiceTypeConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
            case InvoiceTypeConstants.DIGITAL_INVOICE_LIST:
                DataOcrInfo ocrInfo = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getId, invoiceId));
                return ocrInfo.getFileId();
            //机动车
            case InvoiceTypeConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                DataMotorVehicleSale dataMotorVehicleSale =motorVehicleSaleMapper.selectOne(new LambdaQueryWrapper<DataMotorVehicleSale>().eq(DataMotorVehicleSale::getId, invoiceId));
                return dataMotorVehicleSale.getFileId();

            //航空运输电子客票行程单
            case InvoiceTypeConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                DataFlightItinerary dataFlightItinerary =flightItineraryMapper.selectOne(new LambdaQueryWrapper<DataFlightItinerary>().eq(DataFlightItinerary::getId, invoiceId));
                return dataFlightItinerary.getFileId();
            //二手车
            case InvoiceTypeConstants.GLORITY_USED_CAR_SALES_CODE:
                DataUsedCarSales usedCarSales =usedCarSalesMapper.selectOne(new LambdaQueryWrapper<DataUsedCarSales>().eq(DataUsedCarSales::getId, invoiceId));
                return usedCarSales.getFileId();
            //船票
            case InvoiceTypeConstants.GLORITY_STEAMER_TICKET_CODE:
                DataSteamerTicket dataSteamerTicket =steamerTicketMapper.selectOne(new LambdaQueryWrapper<DataSteamerTicket>().eq(DataSteamerTicket::getId, invoiceId));
                return dataSteamerTicket.getFileId();
            //医疗票明细票
            case InvoiceTypeConstants.MEDICAL_TICKET_DETAILS_CODE:
            case InvoiceTypeConstants.MEDICAL_RECEIPTS_CODE:
                DataMedicalTreatment medicalTreatment=dataMedicalTreatmentMapper.selectOne(new LambdaQueryWrapper<DataMedicalTreatment>().eq(DataMedicalTreatment::getId, invoiceId));
                return medicalTreatment.getFileId();
            //非税收入类发票
            case InvoiceTypeConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                DataNonTax dataNonTax =dataNonTaxMapper.selectOne(new LambdaQueryWrapper<DataNonTax>().eq(DataNonTax::getId, invoiceId));
                return dataNonTax.getFileId();
            //定额发票
            case InvoiceTypeConstants.GLORITY_QUOTA_INVOICE_CODE:
                DataQuotaInvoice dataQuotaInvoice =quotaInvoiceMapper.selectOne(new LambdaQueryWrapper<DataQuotaInvoice>().eq(DataQuotaInvoice::getId, invoiceId));
                return dataQuotaInvoice.getFileId();
            //出租车发票
            case InvoiceTypeConstants.GLORITY_TAXI_TICKETS_CODE:
                DataTaxiTickets dataTaxiTickets =taxiTicketsMapper.selectOne(new LambdaQueryWrapper<DataTaxiTickets>().eq(DataTaxiTickets::getId, invoiceId));
                return dataTaxiTickets.getFileId();
            //火车发票
            case InvoiceTypeConstants.GLORITY_RAILWAY_TICKET_CODE:
                DataRailwayTicket dataRailwayTicket =railwayTicketMapper.selectOne(new LambdaQueryWrapper<DataRailwayTicket>().eq(DataRailwayTicket::getId, invoiceId));
                return dataRailwayTicket.getFileId();
            //客运车发票
            case InvoiceTypeConstants.GLORITY_PASSENGER_TICKET_CODE:
                DataPassengerCar dataPassengerCar =passengerCarMapper.selectOne(new LambdaQueryWrapper<DataPassengerCar>().eq(DataPassengerCar::getId, invoiceId));
                return dataPassengerCar.getFileId();

            //过路费发票
            case InvoiceTypeConstants.GLORITY_TOLL_ROADS_CODE:
                DataTollRoads dataTollRoads =tollRoadsMapper.selectOne(new LambdaQueryWrapper<DataTollRoads>().eq(DataTollRoads::getId, invoiceId));
                return dataTollRoads.getFileId();

            //小票/可报销其他发票
            case InvoiceTypeConstants.GLORITY_RECEIPT_CODE:
            case InvoiceTypeConstants.REIMBURSABLE_OTHER_CODE:
                DataReceipt dataReceipt=dataReceiptMapper.selectOne(new LambdaQueryWrapper<DataReceipt>().eq(DataReceipt::getId, invoiceId));
                return dataReceipt.getFileId();
            //出行发票/滴滴
            case InvoiceTypeConstants.GLORITY_DIDI_ITINERARY_CODE:
                DataDidiItinerary didiItinerary =didiItineraryMapper.selectOne(new LambdaQueryWrapper<DataDidiItinerary>().eq(DataDidiItinerary::getId, invoiceId));
                return didiItinerary.getFileId();
            //完税证明发票
            case InvoiceTypeConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                DataDutyPaidProof dutyPaidProof =paidProofMapper.selectOne(new LambdaQueryWrapper<DataDutyPaidProof>().eq(DataDutyPaidProof::getId, invoiceId));
                return dutyPaidProof.getFileId();
            //海关进口货物报关单发票
            case InvoiceTypeConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                DataCustomsImxportGoods dataCustomsImxportGoods =customsImportGoodsMapper.selectOne(new LambdaQueryWrapper<DataCustomsImxportGoods>().eq(DataCustomsImxportGoods::getId, invoiceId));
                return dataCustomsImxportGoods.getFileId();
            //海关出口货物报关单发票
            case InvoiceTypeConstants.CUSTOMS_EXPORT_GOODS_CODE:
                DataCustomsExportGoods dataCustomsExportGoods =customsExportGoodsMapper.selectOne(new LambdaQueryWrapper<DataCustomsExportGoods>().eq(DataCustomsExportGoods::getId, invoiceId));
                return dataCustomsExportGoods.getFileId();

            //海关专用缴款书发票
            case InvoiceTypeConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                DataCustomsSpecialPayment dataCustomsSpecialPayment =customsSpecialPaymentMapper.selectOne(new LambdaQueryWrapper<DataCustomsSpecialPayment>().eq(DataCustomsSpecialPayment::getId, invoiceId));
                return dataCustomsSpecialPayment.getFileId();

            //货物运输电子收款凭证发票
            case InvoiceTypeConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                DataElectronicTransportationGoods transportationGoods =paymentMapper.selectOne(new LambdaQueryWrapper<DataElectronicTransportationGoods>().eq(DataElectronicTransportationGoods::getId, invoiceId));
                return transportationGoods.getFileId();
            default: {
                return "";
            }
        }
    }


}
