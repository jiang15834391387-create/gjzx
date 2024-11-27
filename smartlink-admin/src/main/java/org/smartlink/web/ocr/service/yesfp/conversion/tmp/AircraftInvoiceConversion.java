package org.smartlink.web.ocr.service.yesfp.conversion.tmp;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.invoice.DataAircraftInvoice;
import org.smartlink.web.domain.invoice.DataOcrDetails;
import org.smartlink.web.ocr.service.ChangeIdentifyInfo;
import org.smartlink.web.ocr.service.bean.IdentificationData;
import org.smartlink.web.ocr.service.yesfp.utils.YesfpCoordinateUtil;
import org.springframework.core.convert.ConversionException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: AircraftInvoiceConversion</p>
 * <p>
 * <p>Description:税务云机打发票识别信息转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class AircraftInvoiceConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {
        private static final AircraftInvoiceConversion INSTANCE = new AircraftInvoiceConversion();
    }

    private AircraftInvoiceConversion (){}
    public static AircraftInvoiceConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject jsonObject) throws ConversionException {
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject coordinate = jsonObject.getJSONObject("coordinate");
        String imageId = jsonObject.getString("imageId");
        //dataImageFilesInfo.setNcImageId(imageId);
        String token = jsonObject.getString("token");
        JSONArray itemList = data.getJSONArray("itemList");
        List<DataOcrDetails> ocrDetailsList = new ArrayList<>();
        DataAircraftInvoice aircraftInvoice = new DataAircraftInvoice();
        if(ObjectUtil.isNotEmpty(coordinate)){
            aircraftInvoice.setCoordinate(YesfpCoordinateUtil.getCoordinateArr(coordinate));
            aircraftInvoice.setOrientation(Convert.toInt(coordinate.getString("degree")));
            aircraftInvoice.setCoordinateStr(YesfpCoordinateUtil.getCoordinateStr(coordinate));
        }
        aircraftInvoice.setNcImageId(imageId);
        aircraftInvoice.setId(IdUtil.simpleUUID());
        aircraftInvoice.setFileId(dataImageFilesInfo.getFileId());
        aircraftInvoice.setBuyerName(data.getString("buyerName"));
        aircraftInvoice.setBuyerTaxid(data.getString("buyerTaxId"));
        aircraftInvoice.setCheckCode(data.getString("checkCode"));
        aircraftInvoice.setCity(data.getString("city"));
        aircraftInvoice.setCompanySeal(data.getString("companySeal"));
        aircraftInvoice.setInvoiceDate(Convert.toDate(data.getString("date")));
        aircraftInvoice.setCategory(data.getString("category"));
        aircraftInvoice.setInvoiceCode(data.getString("invoiceCode"));
        aircraftInvoice.setInvoiceNumber(data.getString("invoiceNum"));
        aircraftInvoice.setProvince(data.getString("province"));
        aircraftInvoice.setSellerName(data.getString("sellerName"));
        aircraftInvoice.setSellerTaxid(data.getString("sellerTaxId"));
        aircraftInvoice.setInvoiceTotal(Convert.toBigDecimal(data.getString("totalAmount")));
        //aircraftInvoice.setImageId(imageId);
        aircraftInvoice.setSaveToken(token);
        for (int i = 0; i < itemList.size(); i++) {
            DataOcrDetails dataOcrDetails = new DataOcrDetails();
            JSONObject item = itemList.getJSONObject(i);
            dataOcrDetails.setId(IdUtil.simpleUUID());
            dataOcrDetails.setFileId(dataImageFilesInfo.getFileId());
            dataOcrDetails.setTax(Convert.toBigDecimal(item.getString("taxRate")));
            dataOcrDetails.setDetailAmount(Convert.toBigDecimal(item.getString("amount")));
            dataOcrDetails.setName(item.getString("item"));
            dataOcrDetails.setPrice(Convert.toBigDecimal(item.getString("price")));
            dataOcrDetails.setDetailsCount(Convert.toBigDecimal(item.getString("num")));
            ocrDetailsList.add(dataOcrDetails);
        }
        aircraftInvoice.setDetails(ocrDetailsList);
        return new IdentificationData<>(InvoiceConstants.AIRCRAFT_INVOICE,aircraftInvoice);

    }

}
