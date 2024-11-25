package org.smartlink.web.ncc.deleteocr;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.web.constant.CheckConstant;
import org.smartlink.web.constant.NcConstant;
import org.smartlink.web.constant.NcOcrInvoiceTypeConstant;
import org.smartlink.web.domain.invoice.*;
import org.smartlink.web.domain.modle.BaseEntity;
import org.smartlink.web.exception.NCServiceException;
import org.smartlink.web.ncc.deleteocr.request.allinvoice.DeleteAllInvoiceData;
import org.smartlink.web.ncc.deleteocr.request.allinvoice.DeleteAllInvoiceDataItem;
import org.smartlink.web.ncc.deleteocr.request.allinvoice.DeleteAllInvoiceRequest;
import org.smartlink.web.ncc.deleteocr.request.allinvoice.DeleteElectronicInvoiceRequest;
import org.smartlink.web.ncc.deleteocr.request.taxinvoice.DeleteElectronicNonStandardRequest;
import org.smartlink.web.ncc.deleteocr.request.taxinvoice.DeleteTaxInvoiceRequest;
import org.smartlink.web.ncc.deleteocr.response.DeleteInvoiceResponse;
import org.smartlink.web.properties.NccParamProperties;
import org.smartlink.web.token.response.Token;
import org.smartlink.web.utils.DealRequest;
import org.smartlink.web.utils.SHA256Util;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author L
 * @Description NCC删除台账接口
 * @create:
 */
public class NccDeleteOcrService {

    private final static String TAX_INVOICE_DELETE_URL = "/nccloud/api/imag/invoice/tax/delete";

    private final static String ALL_INVOICE_DELETE_URL = "/nccloud/api/imag/invoice/tax/delete_allinvoice";

    private final static String ELECTRONIC_INVOICE_DELETE_URL = "/nccloud/api/imag/invoice/tax/deleteeinvoice";

    private final static String NON_STANDARD_ELECTRONIC_INVOICE_DELETE_URL = "/nccloud/api/imag/invoice/tax/einvdelete";


    /**
     * 增值税票种删除台账接口
     * @param deleteTaxInvoiceRequest 请求类
     * @param token token
     * @param paramProperties 参数
     * @return 返回
     * @throws Exception 异常
     */
    public static DeleteInvoiceResponse deleteNccTaxInvoiceData(DeleteTaxInvoiceRequest deleteTaxInvoiceRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String jsonString = JSONObject.toJSONString(deleteTaxInvoiceRequest);
        String body = DealRequest.dealRequestBody(jsonString, token.getData().getSecurity_key());
        return doDeleteMethod(paramProperties.getBaseUrl()+TAX_INVOICE_DELETE_URL,token.getData().getAccess_token(),paramProperties.getClientId(),jsonString,body);
    }

    /**
     * 全票种发票删除台账接口
     * @param deleteAllInvoiceRequest 请求类
     * @param token token
     * @param paramProperties 参数
     * @return 返回
     * @throws Exception 异常
     */
    public static DeleteInvoiceResponse deleteNccAllInvoiceData(DeleteAllInvoiceRequest deleteAllInvoiceRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String jsonString = JSONObject.toJSONString(deleteAllInvoiceRequest);
        String body = DealRequest.dealRequestBody(jsonString, token.getData().getSecurity_key());
        return doDeleteMethod(paramProperties.getBaseUrl()+ALL_INVOICE_DELETE_URL,token.getData().getAccess_token(),paramProperties.getClientId(),jsonString,body);
    }

    /**
     * 全票种删除电子发票台账接口
     * @param deleteElectronicInvoiceRequest 请求类
     * @param token token
     * @param paramProperties 参数
     * @return 返回
     * @throws Exception 异常
     */
    public static DeleteInvoiceResponse deleteElectronicInvoiceData(DeleteElectronicInvoiceRequest deleteElectronicInvoiceRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String jsonString = JSONObject.toJSONString(deleteElectronicInvoiceRequest);
        String body = DealRequest.dealRequestBody(jsonString, token.getData().getSecurity_key());
        return doDeleteMethod(paramProperties.getBaseUrl()+ELECTRONIC_INVOICE_DELETE_URL,token.getData().getAccess_token(),paramProperties.getClientId(),jsonString,body);
    }

    /**
     * 2111及以下版本客开删除电子发票台账接口
     * @param deleteElectronicInvoiceRequest 请求类
     * @param token token
     * @param paramProperties 参数
     * @return 返回
     * @throws Exception 异常
     */
    public static DeleteInvoiceResponse deleteNonStandardElectronicInvoiceData(DeleteElectronicNonStandardRequest deleteElectronicInvoiceRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String jsonString = JSONObject.toJSONString(deleteElectronicInvoiceRequest);
        String body = DealRequest.dealRequestBody(jsonString, token.getData().getSecurity_key());
        return doDeleteMethod(paramProperties.getBaseUrl()+NON_STANDARD_ELECTRONIC_INVOICE_DELETE_URL,token.getData().getAccess_token(),paramProperties.getClientId(),jsonString,body);
    }

    /**
     * 删除方法
     * @param serviceUrl 接口地址
     * @param accessToken token
     * @param clientId clientId
     * @param jsonString 请求体
     * @param body 请求体
     * @return 返回
     * @throws Exception 异常
     */
    private static DeleteInvoiceResponse doDeleteMethod(String serviceUrl,String accessToken,String clientId,String jsonString,String body) throws Exception {
        return WebClient.create()
                .post()
                .uri(serviceUrl)
                .header("access_token",accessToken)
                .header("client_id",clientId)
                .header("signature", SHA256Util.getSignatureData(clientId+jsonString))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class).flatMap(str -> Mono.just(JSONObject.parseObject(str, DeleteInvoiceResponse.class))).map(NCServiceException::deleteInvoiceResult)
                .block();
    }

    /**
     * 组装删除全票种请求类data
     * @param baseEntityList 集合
     * @return 返回
     */
    public static List<DeleteAllInvoiceData> getDeleteAllInvoiceDataByBaseEntity(List<BaseEntity> baseEntityList){
        List<DeleteAllInvoiceData> deleteAllInvoiceDataList = new ArrayList<>();
        for (BaseEntity baseEntity : baseEntityList) {
            DeleteAllInvoiceData deleteAllInvoiceData = new DeleteAllInvoiceData();
            if(baseEntity instanceof DataMotorVehicleSale){
                DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseEntity;
                if(StrUtil.equals(dataMotorVehicleSale.getPushBusinessInfoFlag(), NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    deleteAllInvoiceData.setBillType(NcOcrInvoiceTypeConstant.INVOICE);
                    deleteAllInvoiceData.setSaveToken(dataMotorVehicleSale.getSaveToken());
                    deleteAllInvoiceData.setData(DeleteAllInvoiceDataItem.builder().fpDm(dataMotorVehicleSale.getInvoiceCode()).fpHm(dataMotorVehicleSale.getInvoiceNumber()).build());
                    deleteAllInvoiceDataList.add(deleteAllInvoiceData);
                }
            }else if(baseEntity instanceof DataQuotaInvoice){
                DataQuotaInvoice dataQuotaInvoice = (DataQuotaInvoice) baseEntity;
                if(StrUtil.equals(dataQuotaInvoice.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    deleteAllInvoiceData.setBillType(NcOcrInvoiceTypeConstant.QUOTA);
                    deleteAllInvoiceData.setSaveToken(dataQuotaInvoice.getSaveToken());
                    deleteAllInvoiceData.setData(DeleteAllInvoiceDataItem.builder().fpDm(dataQuotaInvoice.getInvoiceCode()).fpHm(dataQuotaInvoice.getInvoiceNumber()).build());
                    deleteAllInvoiceDataList.add(deleteAllInvoiceData);
                }
            }else if(baseEntity instanceof DataAircraftInvoice){
                DataAircraftInvoice dataAircraftInvoice = (DataAircraftInvoice) baseEntity;
                if(StrUtil.equals(dataAircraftInvoice.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    deleteAllInvoiceData.setBillType(NcOcrInvoiceTypeConstant.MACHINE);
                    deleteAllInvoiceData.setSaveToken(dataAircraftInvoice.getSaveToken());
                    deleteAllInvoiceData.setData(DeleteAllInvoiceDataItem.builder().fpDm(dataAircraftInvoice.getInvoiceCode()).fpHm(dataAircraftInvoice.getInvoiceNumber()).build());
                    deleteAllInvoiceDataList.add(deleteAllInvoiceData);
                }
            }else if(baseEntity instanceof DataTaxiTickets){
                DataTaxiTickets dataTaxiTickets = (DataTaxiTickets) baseEntity;
                if(StrUtil.equals(dataTaxiTickets.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    deleteAllInvoiceData.setBillType(NcOcrInvoiceTypeConstant.TAXI);
                    deleteAllInvoiceData.setSaveToken(dataTaxiTickets.getSaveToken());
                    deleteAllInvoiceData.setData(DeleteAllInvoiceDataItem.builder().fpDm(dataTaxiTickets.getInvoiceCode()).fpHm(dataTaxiTickets.getInvoiceNumber()).build());
                    deleteAllInvoiceDataList.add(deleteAllInvoiceData);
                }
            }else if(baseEntity instanceof DataRailwayTicket){
                DataRailwayTicket dataRailwayTicket = (DataRailwayTicket) baseEntity;
                if(StrUtil.equals(dataRailwayTicket.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    deleteAllInvoiceData.setBillType(NcOcrInvoiceTypeConstant.TRAIN);
                    deleteAllInvoiceData.setSaveToken(dataRailwayTicket.getSaveToken());
                    deleteAllInvoiceData.setData(DeleteAllInvoiceDataItem.builder().fpDm("").fpHm(dataRailwayTicket.getInvoiceNumber()).build());
                    deleteAllInvoiceDataList.add(deleteAllInvoiceData);
                }
            }else if(baseEntity instanceof DataPassengerTicket){
                DataPassengerTicket dataPassengerTicket = (DataPassengerTicket) baseEntity;
                if(StrUtil.equals(dataPassengerTicket.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    deleteAllInvoiceData.setBillType(NcOcrInvoiceTypeConstant.PASSENGER);
                    deleteAllInvoiceData.setSaveToken(dataPassengerTicket.getSaveToken());
                    deleteAllInvoiceData.setData(DeleteAllInvoiceDataItem.builder().fpDm(dataPassengerTicket.getInvoiceCode()).fpHm(dataPassengerTicket.getInvoiceNumber()).build());
                    deleteAllInvoiceDataList.add(deleteAllInvoiceData);
                }
            }else if(baseEntity instanceof DataFlightItinerary){
                DataFlightItinerary dataFlightItinerary = (DataFlightItinerary) baseEntity;
                if(StrUtil.equals(dataFlightItinerary.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    deleteAllInvoiceData.setBillType(NcOcrInvoiceTypeConstant.AIR);
                    deleteAllInvoiceData.setSaveToken(dataFlightItinerary.getSaveToken());
                    deleteAllInvoiceData.setData(DeleteAllInvoiceDataItem.builder().fpDm("").fpHm(dataFlightItinerary.getInvoiceNumber()).build());
                    deleteAllInvoiceDataList.add(deleteAllInvoiceData);
                }
            }else if(baseEntity instanceof DataTollRoads){
                DataTollRoads dataTollRoads = (DataTollRoads) baseEntity;
                if(StrUtil.equals(dataTollRoads.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    deleteAllInvoiceData.setBillType(NcOcrInvoiceTypeConstant.TOLLS);
                    deleteAllInvoiceData.setSaveToken(dataTollRoads.getSaveToken());
                    deleteAllInvoiceData.setData(DeleteAllInvoiceDataItem.builder().fpDm(dataTollRoads.getInvoiceCode()).fpHm(dataTollRoads.getInvoiceNumber()).build());
                    deleteAllInvoiceDataList.add(deleteAllInvoiceData);
                }
            }else{
                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
                if(StrUtil.equals(dataOcrInfo.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    // checkInvoice有值的话 为增值税发票类型，否则为其他发票类型
                    if(StrUtil.equals(dataOcrInfo.getCheckInvoice(), CheckConstant.SUCCESS_CHECK)){
                        deleteAllInvoiceData.setBillType(NcOcrInvoiceTypeConstant.INVOICE);
                    }else{
                        deleteAllInvoiceData.setBillType(NcOcrInvoiceTypeConstant.OTHER_INVOICE);
                    }
                    deleteAllInvoiceData.setSaveToken(dataOcrInfo.getSaveToken());
                    deleteAllInvoiceData.setData(DeleteAllInvoiceDataItem.builder().fpDm(dataOcrInfo.getInvoiceCode()).fpHm(dataOcrInfo.getInvoiceNumber()).build());
                    deleteAllInvoiceDataList.add(deleteAllInvoiceData);
                }
            }
        }
        return deleteAllInvoiceDataList;
    }


}
