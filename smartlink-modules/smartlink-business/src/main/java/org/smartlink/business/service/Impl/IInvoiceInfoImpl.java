package org.smartlink.business.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.service.IInvoiceInfoService;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.domain.bo.DataImageFilesInfoBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataImageFilesInfoVo;
import org.smartlink.common.entity.domain.business.domain.vo.DataOcrInfoVo;
import org.smartlink.common.entity.domain.business.response.DataResponseDTO;
import org.smartlink.common.entity.domain.business.service.*;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.ocr.glority.conversion.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class IInvoiceInfoImpl implements IInvoiceInfoService {
    private final IDataImageFilesInfoService iDataImageFilesInfoService;
    private final IDataOcrInfoService iDataOcrInfoService;
    private final IDataOcrDetailsService iDataOcrDetailsService;
    private final IDataUsedCarSalesService iDataUsedCarSalesService;
    private final IDataTollRoadsService iDataTollRoadsService;
    private final IDataTaxiTicketsService iDataTaxiTicketsService;
    private final IDataSteamerTicketService iDataSteamerTicketService;
    private final IDataReceiptService iDataReceiptService;
    private final IDataRailwayTicketService iDataRailwayTicketService;
    private final IDataQuotaInvoiceService iDataQuotaInvoiceService;
    private final IDataPassengerCarService iDataPassengerCarService;
    private final IDataNonTaxService iDataNonTaxService;
    private final IDataMotorVehicleSaleService iDataMotorVehicleSaleService;
    private final IDataMedicalTreatmentService iDataMedicalTreatmentService;
    private final IDataMedicalTreatmentDetailService iDataMedicalTreatmentDetailService;
    private final IDataFlightsItineraryDetailService iDataFlightsItineraryDetailService;
    private final IDataFlightItineraryService iDataFlightItineraryService;
    private final IDataElectronicTransportationGoodsService iDataElectronicTransportationGoodsService;
    private final IDataDutyPaidProofService iDataDutyPaidProofService;
    private final IDataDutyPaidProofDetailsService iDataDutyPaidProofDetailsService;
    private final IDataDidiItineraryService iDataDidiItineraryService;
    private final IDataDidiItineraryDetailsService iDataDidiItineraryDetailsService;
    private final IDataCustomsSpecialPaymentService iDataCustomsSpecialPaymentService;
    private final IDataCustomsExportGoodsService iDataCustomsExportGoodsService;
    private final IDataCustomsExportGoodsDetailService iDataCustomsExportGoodsDetailService;
    private final IDataCustomsImxportGoodsService iDataCustomsImxportGoodsService;
    private final IDataCustomsImportGoodsDetailService iDataCustomsImportGoodsDetailService;
    @Override
    public R queryList(DataImageFilesInfoBo bo) {
       List<DataImageFilesInfoVo> dataImageFilesInfoVos =  iDataImageFilesInfoService.queryList(bo);
        List<Map<String, Object>> result = new ArrayList<>();
       for (DataImageFilesInfoVo dataImageFilesInfoVo:dataImageFilesInfoVos){

//           switch (dataImageFilesInfoVo.getInvoice()) {
//               //增值税、机打
//               case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
//               case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
//               case InvoiceConstants.GLORITY_TAX_CODE:
//               case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
//               case InvoiceConstants.GLORITY_ELECTRONIC_QUKUAILIAN_CODE:
//               case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
//               case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
//               case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
//               case InvoiceConstants.DIGITAL_INVOICE_LIST:
//               case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
                   // 将文件信息对象转换为 Map
                   Map<String, Object> fileInfoMap = convertToMap(dataImageFilesInfoVo);

                   // 获取 OCR 信息列表
                   List<DataOcrInfo> dataOcrInfoList = iDataOcrInfoService.queryByFileId(dataImageFilesInfoVo.getFileId());
                   List<Map<String, Object>> ocrInfoList = new ArrayList<>();

                   for (DataOcrInfo dataOcrInfo : dataOcrInfoList) {
                       // 将 OCR 信息对象转换为 Map
                       Map<String, Object> ocrInfoMap = convertToMap(dataOcrInfo);

                       // 获取 OCR 明细并转换为 Map
                       List<DataOcrDetails> dataOcrDetailsList = iDataOcrDetailsService.queryByFileId(dataImageFilesInfoVo.getFileId());
                       List<Map<String, Object>> detailList = dataOcrDetailsList.stream()
                           .map(this::convertToMap)
                           .collect(Collectors.toList());

                       // 添加明细信息到 OCR 信息
                       ocrInfoMap.put("details", detailList);

                       // 添加到 OCR 信息列表
                       ocrInfoList.add(ocrInfoMap);
                   }

                   // 添加嵌套结构到文件信息
                   fileInfoMap.put("ocr_info", ocrInfoList);
                   result.add(fileInfoMap);

//                   return R.ok(result);
//            case GLORITY_QUOTA_INVOICE_CODE:
//                return QuotaInvoiceConversion.getInstance();
//            case GLORITY_AIRCRAFT_INVOICE_CODE:
//                return AircraftInvoiceConversion.getInstance();
//            case GLORITY_TAXI_TICKETS_CODE:
//                return TaxiTicketsConversion.getInstance();
//            case GLORITY_RAILWAY_TICKET_CODE:
//                return RailwayTicketConversion.getInstance();
//            case GLORITY_RECEIPT_CODE:
//                return ReceiptConversion.getInstance();
//            case GLORITY_MOTOR_VEHICLE_SALE_CODE:
//                return MotorVehicleSaleConversion.getInstance();
//            case GLORITY_PASSENGER_TICKET_CODE:
//                return PassengerTicketConversion.getInstance();
//            case GLORITY_DIDI_ITINERARY_CODE:
//                return DidiItineraryConversion.getInstance();
//            case GLORITY_USED_CAR_SALES_CODE:
//                return UsedCarSalesConversion.getInstance();
//            case GLORITY_FLIGHT_ITINERARY_CODE:
//                return FlightItineraryConversion.getInstance();
//            case GLORITY_DUTY_PAID_PROOF_CODE:
//                return DutyPaidProofConversion.getInstance();
//            case GLORITY_STEAMER_TICKET_CODE:
//                return SteamerTicketConversion.getInstance();
//            case GLORITY_TOLL_ROADS_CODE:
//                return TollRoadsConversion.getInstance();
//               default: {
//                   return null;
//               }
//           }
       }
        return R.ok(result);
    }


    private Map<String, Object> convertToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing field: " + field.getName(), e);
            }
        }
        return map;
    }

    @Override
    public R queryOneInfo(String fileId) {
        DataImageFilesInfo res = iDataImageFilesInfoService.selectOneByFileId(fileId);
        if (res != null) {
            if (!res.getFileId().isEmpty()) {
                String invoiceType = res.getInvoice();
                Object info = null;
                Object detailInfo = null;
                switch (invoiceType) {
                    //增值税、机打
                    case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
                    case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
                    case InvoiceConstants.GLORITY_TAX_CODE:
                    case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
                    case InvoiceConstants.GLORITY_ELECTRONIC_QUKUAILIAN_CODE:
                    case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
                    case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
                    case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
                    case InvoiceConstants.DIGITAL_INVOICE_LIST:
                    case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
                    case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
                    case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
                        DataOcrInfo dataOcrInfo = iDataOcrInfoService.selectOneByFileId(res.getFileId());
                        if (!dataOcrInfo.getFileId().isEmpty()) {
                            detailInfo = iDataOcrDetailsService.selectOneByFileId(dataOcrInfo.getFileId());
                        }
                        return R.ok(new DataResponseDTO(res, dataOcrInfo, detailInfo));
                    //机动车
                    case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                        DataMotorVehicleSale dataMotorVehicleSaleInfo = iDataMotorVehicleSaleService.selectOneByFileId(res.getFileId());
                        return R.ok(new DataResponseDTO(res, dataMotorVehicleSaleInfo, null));
                    //机票
                    case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                        DataFlightItinerary flightItineraryInfo = iDataFlightItineraryService.selectOneByFileId(res.getFileId());
                        if (!flightItineraryInfo.getFileId().isEmpty()) {
                            detailInfo = iDataFlightsItineraryDetailService.selectOneByFileId(flightItineraryInfo.getFileId());
                        }
                        return R.ok(new DataResponseDTO(res, flightItineraryInfo, detailInfo));
                    //二手车
                    case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                        DataUsedCarSales dataUsedCarSalesInfo = iDataUsedCarSalesService.selectOneByFileId(res.getFileId());
                        return R.ok(new DataResponseDTO(res, dataUsedCarSalesInfo, null));
                    //船票
                    case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                        DataSteamerTicket dataSteamerTicketInfo = iDataSteamerTicketService.selectOneByFileId(res.getFileId());
                        return R.ok(new DataResponseDTO(res, dataSteamerTicketInfo, null));
                    //医疗票明细票
                    case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
                        //医疗票
                    case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                        DataMedicalTreatment dataMedicalTreatmentInfo = iDataMedicalTreatmentService.selectOneByFileId(res.getFileId());
                        if (!dataMedicalTreatmentInfo.getFileId().isEmpty()) {
                            detailInfo = iDataOcrDetailsService.selectOneByFileId(dataMedicalTreatmentInfo.getFileId());
                        }
                        return R.ok(new DataResponseDTO(res, dataMedicalTreatmentInfo, detailInfo));
                    //定额发票
                    case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                        DataQuotaInvoice dataQuotaInvoice = iDataQuotaInvoiceService.selectOneByFileId(res.getFileId());
                        return R.ok(new DataResponseDTO(res, dataQuotaInvoice, null));
                    //出租车发票
                    case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                        DataTaxiTickets dataTaxiTicketsInvoice = iDataTaxiTicketsService.selectOneByFileId(res.getFileId());
                        return R.ok(new DataResponseDTO(res, dataTaxiTicketsInvoice, null));
                    //火车发票
                    case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                        DataRailwayTicket dataRailwayTicketInvoice = iDataRailwayTicketService.selectOneByFileId(res.getFileId());
                        return R.ok(new DataResponseDTO(res, dataRailwayTicketInvoice, null));
                    //客运车发票
                    case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                        DataPassengerCar dataPassengerCarInvoice = iDataPassengerCarService.selectOneByFileId(res.getFileId());
                        return R.ok(new DataResponseDTO(res, dataPassengerCarInvoice, null));
                    //过路费发票
                    case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                        DataTollRoads dataTollRoadsInvoice = iDataTollRoadsService.selectOneByFileId(res.getFileId());
                        return R.ok(new DataResponseDTO(res, dataTollRoadsInvoice, null));
                    //小票
                    case InvoiceConstants.GLORITY_RECEIPT_CODE:
                        DataReceipt dataReceiptInvoice = iDataReceiptService.selectOneByFileId(res.getFileId());
                        return R.ok(new DataResponseDTO(res, dataReceiptInvoice, null));
                    //出行发票
                    case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                        DataDidiItinerary dataDidiItineraryInfo = iDataDidiItineraryService.selectOneByFileId(res.getFileId());
                        if (!dataDidiItineraryInfo.getFileId().isEmpty()) {
                            detailInfo = iDataDidiItineraryDetailsService.selectOneByFileId(dataDidiItineraryInfo.getFileId());
                        }
                        return R.ok(new DataResponseDTO(res, dataDidiItineraryInfo, detailInfo));
                    //完税证明发票
                    case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                        DataDutyPaidProof dataDutyPaidProofInfo = iDataDutyPaidProofService.selectOneByFileId(res.getFileId());
                        if (!dataDutyPaidProofInfo.getFileId().isEmpty()) {
                            detailInfo = iDataDutyPaidProofDetailsService.selectOneByFileId(dataDutyPaidProofInfo.getFileId());
                        }
                        return R.ok(new DataResponseDTO(res, dataDutyPaidProofInfo, detailInfo));
                    //非税收入类发票
                    case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                        DataNonTax dataNonTaxInfo = iDataNonTaxService.selectOneByFileId(res.getFileId());
                        if (!dataNonTaxInfo.getFileId().isEmpty()) {
                            detailInfo = iDataOcrDetailsService.selectOneByFileId(dataNonTaxInfo.getFileId());
                        }
                        return R.ok(new DataResponseDTO(res, dataNonTaxInfo, detailInfo));
                    //海关进口货物报关单发票
                    case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                        DataCustomsImxportGoods dataCustomsImxportGoodsInfo = iDataCustomsImxportGoodsService.selectOneByFileId(res.getFileId());
                        if (!dataCustomsImxportGoodsInfo.getFileId().isEmpty()) {
                            detailInfo = iDataCustomsImportGoodsDetailService.selectOneByFileId(dataCustomsImxportGoodsInfo.getFileId());
                        }
                        return R.ok(new DataResponseDTO(res, dataCustomsImxportGoodsInfo, detailInfo));
                    //海关出口货物报关单发票
                    case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                        DataCustomsExportGoods dataCustomsExportGoodsInfo = iDataCustomsExportGoodsService.selectOneByFileId(res.getFileId());
                        if (!dataCustomsExportGoodsInfo.getFileId().isEmpty()) {
                            detailInfo = iDataCustomsExportGoodsDetailService.selectOneByFileId(dataCustomsExportGoodsInfo.getFileId());
                        }
                        return R.ok(new DataResponseDTO(res, dataCustomsExportGoodsInfo, detailInfo));
                    //海关专用缴款书发票
                    case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                        DataCustomsSpecialPayment dataCustomsSpecialPaymentInfo = iDataCustomsSpecialPaymentService.selectOneByFileId(res.getFileId());
                        if (!dataCustomsSpecialPaymentInfo.getFileId().isEmpty()) {
                            detailInfo = iDataOcrDetailsService.selectOneByFileId(dataCustomsSpecialPaymentInfo.getFileId());
                        }
                        return R.ok(new DataResponseDTO(res, dataCustomsSpecialPaymentInfo, detailInfo));
                    //货物运输电子收款凭证发票
                    case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                        DataElectronicTransportationGoods dataElectronicTransportationGoodsInfo = iDataElectronicTransportationGoodsService.selectOneByFileId(res.getFileId());
                        if (!dataElectronicTransportationGoodsInfo.getFileId().isEmpty()) {
                            detailInfo = iDataOcrDetailsService.selectOneByFileId(dataElectronicTransportationGoodsInfo.getFileId());
                        }
                        return R.ok(new DataResponseDTO(res, dataElectronicTransportationGoodsInfo, detailInfo));
                    default: {
                        return R.ok("未查询到数据!");
                    }
                }
            }
        }
              return R.ok("未查询到数据!");
        }


}
