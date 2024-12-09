package org.smartlink.server.nc.ocr.service.yesfp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.constant.NcOcrInvoiceTypeConstant;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.yesfp.conversion.tmp.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 税务云发票类型转换类
 * @author: L
 * @create:
 **/
public class YesfpConverter {

    /**
     * 将JSON数组转换为影像系统发票对象方法
     * @param dataList 税务云发票数组
     * @return 返回
     */
    public static List<IdentificationData> convertInvoiceInfo(DataImageFilesInfo dataImageFilesInfo, JSONArray dataList){
        List<IdentificationData> invoiceList = new ArrayList<>();
        // OCR信息处理
        for (int i = 0; i < dataList.size(); i++) {
            IdentificationData identificationData;
            JSONObject result = dataList.getJSONObject(i);
            //  发票类型
            String  invoiceType = result.getString("billType");
            switch (invoiceType){
                case NcOcrInvoiceTypeConstant.MACHINE:
                    identificationData = AircraftInvoiceConversion.getInstance().changeInfo(dataImageFilesInfo,result);
                    break;
                case NcOcrInvoiceTypeConstant.TRAIN:
                    identificationData = RailwayTicketConversion.getInstance().changeInfo(dataImageFilesInfo,result);
                    break;
                case NcOcrInvoiceTypeConstant.AIR:
                    identificationData = FlightItineraryConversion.getInstance().changeInfo(dataImageFilesInfo,result);
                    break;
                case NcOcrInvoiceTypeConstant.PASSENGER:
                    identificationData = PassengerTicketConversion.getInstance().changeInfo(dataImageFilesInfo,result);
                    break;
                case NcOcrInvoiceTypeConstant.TAXI:
                    identificationData = TaxiTicketsConversion.getInstance().changeInfo(dataImageFilesInfo,result);
                    break;
                case NcOcrInvoiceTypeConstant.TOLLS:
                    identificationData = TollRoadsConversion.getInstance().changeInfo(dataImageFilesInfo,result);
                    break;
                case NcOcrInvoiceTypeConstant.QUOTA:
                    identificationData = QuotaInvoiceConversion.getInstance().changeInfo(dataImageFilesInfo,result);
                    break;
                case NcOcrInvoiceTypeConstant.INVOICE:
                case NcOcrInvoiceTypeConstant.OTHER_INVOICE:
                    identificationData = InvoiceConversion.getInstance().changeInfo(dataImageFilesInfo,result);
                    break;
                default:
                    identificationData = OthersConversion.getInstance().changeInfo(dataImageFilesInfo,result);
                    break;
            }
            invoiceList.add(identificationData);
        }
        return invoiceList;
    }

}
