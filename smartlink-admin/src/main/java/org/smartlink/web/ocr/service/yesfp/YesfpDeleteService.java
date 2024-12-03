package org.smartlink.web.ocr.service.yesfp;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.web.constant.Constants;
import org.smartlink.web.constant.NcConstant;
import org.smartlink.web.constant.OcrConstant;
import org.smartlink.web.constant.YesfpInvoiceTypeConstants;
import org.smartlink.web.domain.invoice.*;
import org.smartlink.web.domain.modle.BaseEntity;
import org.smartlink.web.ocr.exception.OcrException;
import org.smartlink.web.ocr.service.yesfp.config.YesfpProperties;
import org.smartlink.web.ocr.service.yesfp.reponse.OpenApiAccessTokenData;
import org.smartlink.web.ocr.service.yesfp.reponse.YesfpDeleteResult;
import org.smartlink.web.ocr.service.yesfp.request.YesfpDeleteBills;
import org.smartlink.web.ocr.service.yesfp.request.YesfpDeleteData;
import org.smartlink.web.ocr.service.yesfp.utils.SignUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: YesfpDelete </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author
 * @version 1.0.0
 * @date L
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class YesfpDeleteService {

    public static YesfpDeleteResult deleteStandData(YesfpDeleteData yesfpDeleteData, YesfpProperties yesfpProperties) throws Exception {
        String request = JSONObject.toJSONString(yesfpDeleteData);
        String sign;
        String deleteUrl;
        if(StrUtil.equals(yesfpProperties.getInterFaceType(), Constants.BIP_YESFP_INTERFACE_TYPE)){
            OpenApiAccessTokenData openApiAccessToken;
            try {
                openApiAccessToken = TenantAuthProvider.getOpenApiAccessToken(yesfpProperties.getAppKey(), yesfpProperties.getAppSecret(), RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_TOEKEN_KEY));
                sign = openApiAccessToken.getAccess_token();
                deleteUrl = RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_GATEWAY_KEY) + yesfpProperties.getDeleteUrl() + "?access_token="+sign;
            } catch (Exception e) {
                throw new Exception(e);
            }
        }else{
            try {
                sign = SignUtils.sign(request,yesfpProperties.getPrivateKeyType(), yesfpProperties.getP12Path(), yesfpProperties.getPassword());
                deleteUrl = yesfpProperties.getBaseUrl()+yesfpProperties.getDeleteUrl() + "?appid=" + yesfpProperties.getAppId();
            } catch (Exception e) {
                throw new OcrException("签名验证失败！");
            }
        }
        return WebClient.create().post()
            .uri(deleteUrl)
            .contentType(MediaType.APPLICATION_STREAM_JSON)
            .header("sign", sign)
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(YesfpDeleteResult.class)
            .map(ConversionException::yesfpOcrDelete)
            .block();
    }

    public static List<YesfpDeleteBills> getDeleteBills(List<BaseEntity> baseEntityList) {
        List<YesfpDeleteBills> yesfpDeleteBillsList = new ArrayList<>();
        for (BaseEntity baseEntity : baseEntityList) {
            if (baseEntity instanceof DataMotorVehicleSale) {
                DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseEntity;
                if(StrUtil.equals(dataMotorVehicleSale.getPushBusinessInfoFlag(), NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    YesfpDeleteBills yesfpDeleteBills = new YesfpDeleteBills();
                    yesfpDeleteBills.setBillType(YesfpInvoiceTypeConstants.INVOICE);
                    yesfpDeleteBills.setInvoiceCode(dataMotorVehicleSale.getInvoiceCode());
                    yesfpDeleteBills.setInvoiceNum(dataMotorVehicleSale.getInvoiceNumber());
                    yesfpDeleteBillsList.add(yesfpDeleteBills);
                }
            } else if (baseEntity instanceof DataQuotaInvoice) {
                DataQuotaInvoice dataQuotaInvoice = (DataQuotaInvoice) baseEntity;
                if(StrUtil.equals(dataQuotaInvoice.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    YesfpDeleteBills yesfpDeleteBills = new YesfpDeleteBills();
                    yesfpDeleteBills.setBillType(YesfpInvoiceTypeConstants.QUOTA);
                    yesfpDeleteBills.setInvoiceCode(dataQuotaInvoice.getInvoiceCode());
                    yesfpDeleteBills.setInvoiceNum(dataQuotaInvoice.getInvoiceNumber());
                    yesfpDeleteBillsList.add(yesfpDeleteBills);
                }
            } else if (baseEntity instanceof DataAircraftInvoice) {
                DataAircraftInvoice dataAircraftInvoice = (DataAircraftInvoice) baseEntity;
                if(StrUtil.equals(dataAircraftInvoice.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    YesfpDeleteBills yesfpDeleteBills = new YesfpDeleteBills();
                    yesfpDeleteBills.setBillType(YesfpInvoiceTypeConstants.MACHINE);
                    yesfpDeleteBills.setInvoiceCode(dataAircraftInvoice.getInvoiceCode());
                    yesfpDeleteBills.setInvoiceNum(dataAircraftInvoice.getInvoiceNumber());
                    yesfpDeleteBillsList.add(yesfpDeleteBills);
                }
            } else if (baseEntity instanceof DataTaxiTickets) {
                DataTaxiTickets dataTaxiTickets = (DataTaxiTickets) baseEntity;
                if(StrUtil.equals(dataTaxiTickets.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    YesfpDeleteBills yesfpDeleteBills = new YesfpDeleteBills();
                    yesfpDeleteBills.setBillType(YesfpInvoiceTypeConstants.TAXI);
                    yesfpDeleteBills.setInvoiceCode(dataTaxiTickets.getInvoiceCode());
                    yesfpDeleteBills.setInvoiceNum(dataTaxiTickets.getInvoiceNumber());
                    yesfpDeleteBillsList.add(yesfpDeleteBills);
                }
            } else if (baseEntity instanceof DataRailwayTicket) {
                DataRailwayTicket dataRailwayTicket = (DataRailwayTicket) baseEntity;
                if(StrUtil.equals(dataRailwayTicket.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    YesfpDeleteBills yesfpDeleteBills = new YesfpDeleteBills();
                    yesfpDeleteBills.setBillType(YesfpInvoiceTypeConstants.TRAIN);
                    yesfpDeleteBills.setInvoiceCode("");
                    yesfpDeleteBills.setInvoiceNum(dataRailwayTicket.getInvoiceNumber());
                    yesfpDeleteBillsList.add(yesfpDeleteBills);
                }
            } else if (baseEntity instanceof DataPassengerTicket) {
                DataPassengerTicket dataPassengerTicket = (DataPassengerTicket) baseEntity;
                if(StrUtil.equals(dataPassengerTicket.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    YesfpDeleteBills yesfpDeleteBills = new YesfpDeleteBills();
                    yesfpDeleteBills.setBillType(YesfpInvoiceTypeConstants.PASSENGER);
                    yesfpDeleteBills.setInvoiceCode(dataPassengerTicket.getInvoiceCode());
                    yesfpDeleteBills.setInvoiceNum(dataPassengerTicket.getInvoiceNumber());
                    yesfpDeleteBillsList.add(yesfpDeleteBills);
                }
            } else if (baseEntity instanceof DataFlightItinerary) {
                DataFlightItinerary dataFlightItinerary = (DataFlightItinerary) baseEntity;
                if(StrUtil.equals(dataFlightItinerary.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    YesfpDeleteBills yesfpDeleteBills = new YesfpDeleteBills();
                    yesfpDeleteBills.setBillType(YesfpInvoiceTypeConstants.AIR);
                    yesfpDeleteBills.setInvoiceCode("");
                    yesfpDeleteBills.setInvoiceNum(dataFlightItinerary.getInvoiceNumber());
                    yesfpDeleteBillsList.add(yesfpDeleteBills);
                }
            } else if (baseEntity instanceof DataTollRoads) {
                DataTollRoads dataTollRoads = (DataTollRoads) baseEntity;
                if(StrUtil.equals(dataTollRoads.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    YesfpDeleteBills yesfpDeleteBills = new YesfpDeleteBills();
                    yesfpDeleteBills.setBillType(YesfpInvoiceTypeConstants.TOLLS);
                    yesfpDeleteBills.setInvoiceCode(dataTollRoads.getInvoiceCode());
                    yesfpDeleteBills.setInvoiceNum(dataTollRoads.getInvoiceNumber());
                    yesfpDeleteBillsList.add(yesfpDeleteBills);
                }
            } else {
                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
//                if (!CheckConstant.SUCCESS_CHECK.equals(dataOcrInfo.getCheckInvoice())) {
//                    return null;
//                }
                if(StrUtil.equals(dataOcrInfo.getPushBusinessInfoFlag(),NcConstant.PUSH_BUSINESS_INFO_SUCCESS)){
                    YesfpDeleteBills yesfpDeleteBills = new YesfpDeleteBills();
                    yesfpDeleteBills.setBillType(YesfpInvoiceTypeConstants.INVOICE);
                    yesfpDeleteBills.setInvoiceCode(dataOcrInfo.getInvoiceCode());
                    yesfpDeleteBills.setInvoiceNum(dataOcrInfo.getInvoiceNumber());
                    yesfpDeleteBillsList.add(yesfpDeleteBills);
                }
            }
        }
        return yesfpDeleteBillsList;
    }

}
