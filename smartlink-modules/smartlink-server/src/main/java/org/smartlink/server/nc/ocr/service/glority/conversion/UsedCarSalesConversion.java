package org.smartlink.server.nc.ocr.service.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataUsedCarSales;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.glority.response.IdentifyResults;

import java.util.Date;

/**
 * 二手车销售
 *
 * @author L
 **/
public class UsedCarSalesConversion implements ChangeIdentifyInfo<IdentifyResults> {

    private static class LazyHolder {

        private static final UsedCarSalesConversion INSTANCE = new UsedCarSalesConversion();
    }

    private UsedCarSalesConversion() {
    }

    public static UsedCarSalesConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, IdentifyResults identifyResults) throws OcrException {
        JSONObject jsonObject = identifyResults.getDetails();
        DataUsedCarSales dataUsedCarSales = JSONObject.parseObject(jsonObject.toJSONString(), DataUsedCarSales.class);
        if (null == dataUsedCarSales) {
            return null;
        }
        dataUsedCarSales.setId(IdUtil.simpleUUID());
        dataUsedCarSales.setFileId(dataImageFilesInfo.getFileId());
        dataImageFilesInfo.setFileType(InvoiceConstants.USED_CAR_SALES);
        dataUsedCarSales.setInvoiceNumber(jsonObject.getString("number"));
        dataUsedCarSales.setInvoiceCode(jsonObject.getString("code"));
        dataUsedCarSales.setInvoiceDate(Convert.toDate(jsonObject.getString("date"), new Date()));
        dataUsedCarSales.setInvoiceTotal(jsonObject.getBigDecimal("total"));
        dataUsedCarSales.setSellerName(jsonObject.getString("seller"));
        dataUsedCarSales.setRegisTrationNumber(jsonObject.getString("seller"));
        dataUsedCarSales.setSellerName(jsonObject.getString("seller"));
        dataUsedCarSales.setSellerName(jsonObject.getString("registration_number"));
        dataUsedCarSales.setVehicleType(jsonObject.getString("car_type"));
        dataUsedCarSales.setBuyerId(jsonObject.getString("buyer_id"));
        dataUsedCarSales.setSellerId(jsonObject.getString("seller_id"));
        dataUsedCarSales.setCarModel(jsonObject.getString("car_model"));
        dataUsedCarSales.setBuyerAddress(jsonObject.getString("buyer_address"));
        dataUsedCarSales.setSellerAddress(jsonObject.getString("seller_address"));
        dataUsedCarSales.setBuyerName(jsonObject.getString("buyer"));
        dataUsedCarSales.setSellerPhone(jsonObject.getString("seller_tel"));
        dataUsedCarSales.setLicensePlate(jsonObject.getString("license_plate"));
        dataUsedCarSales.setBuyerPhone(jsonObject.getString("buyer_tel"));
        dataUsedCarSales.setCompanyName(jsonObject.getString("company_name"));
        dataUsedCarSales.setCarCode(jsonObject.getString("car_code"));
        dataUsedCarSales.setCarModel(jsonObject.getString("car_model"));
        dataUsedCarSales.setCompanySeal(jsonObject.getString("company_seal"));
        dataUsedCarSales.setMachineCode(jsonObject.getString("machine_id"));
        dataUsedCarSales.setInvoiceSheet(jsonObject.getString("form_type"));
        dataUsedCarSales.setPageNumber(jsonObject.getString("form_name"));
        dataUsedCarSales.setCoordinate(identifyResults.getRegion());
        dataUsedCarSales.setOrientation(Integer.parseInt("-"+identifyResults.getOrientation()));
        return new IdentificationData<>(dataImageFilesInfo.getFileType(), dataUsedCarSales);
    }
}
