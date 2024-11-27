package org.smartlink.web.ocr.service.yesfp.conversion.tmp;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.constant.NcOcrInvoiceTypeConstant;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.invoice.DataMotorVehicleSale;
import org.smartlink.web.domain.invoice.DataOcrDetails;
import org.smartlink.web.domain.invoice.DataOcrInfo;
import org.smartlink.web.ocr.service.ChangeIdentifyInfo;
import org.smartlink.web.ocr.service.bean.IdentificationData;
import org.smartlink.web.ocr.service.yesfp.utils.YesfpCoordinateUtil;
import org.smartlink.web.utils.MoneyUtil;
import org.smartlink.web.utils.NcTypeConvertUtil;
import org.springframework.core.convert.ConversionException;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Title: InvoiceConversion</p>
 * <p>
 * <p>Description:税务云增值税发票识别信息转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class InvoiceConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {

        private static final InvoiceConversion INSTANCE = new InvoiceConversion();
    }

    private InvoiceConversion() {
    }

    public static InvoiceConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject jsonObject) throws ConversionException {
        JSONObject data = jsonObject.getJSONObject("data");
        String imageId = jsonObject.getString("imageId");
        JSONObject coordinate = jsonObject.getJSONObject("coordinate");
        dataImageFilesInfo.setNcImageId(imageId);
        String token = jsonObject.getString("token");
        String invoiceType =  jsonObject.getString("billType");

        if(StrUtil.equals(invoiceType, NcOcrInvoiceTypeConstant.OTHER_INVOICE)){
            //  其他发票
            DataOcrInfo ocrInfo = new DataOcrInfo();
            ocrInfo.setFileId(dataImageFilesInfo.getFileId());
            ocrInfo.setNcImageId(imageId);
            ocrInfo.setInvoiceDate(Convert.toDate(data.getString("date")));
            ocrInfo.setInvoiceCode(data.getString("invoiceCode"));
            ocrInfo.setInvoiceNumber(data.getString("invoiceNum"));
            ocrInfo.setSumAmount(Convert.toBigDecimal(data.getString("totalAmount")));
            //ocrInfo.setImageId(imageId);
            ocrInfo.setSaveToken(token);
            if(ObjectUtil.isNotEmpty(coordinate)){
                ocrInfo.setCoordinate(YesfpCoordinateUtil.getCoordinateArr(coordinate));
                ocrInfo.setOrientation(Convert.toInt(coordinate.getString("degree")));
                ocrInfo.setCoordinateStr(YesfpCoordinateUtil.getCoordinateStr(coordinate));
            }
            return new IdentificationData<>(InvoiceConstants.INVOICE_OTHERS, ocrInfo);
        }else{
            String fileType = NcTypeConvertUtil.getSystemFileType(data.getString("fplx"));
            if(StrUtil.equals(InvoiceConstants.MOTOR_VEHICLE_SALE,fileType)){
                // 机动车发票
                DataMotorVehicleSale motorVehicleSale = new DataMotorVehicleSale();
                motorVehicleSale.setId(IdUtil.simpleUUID());
                motorVehicleSale.setNcImageId(imageId);
                motorVehicleSale.setFileId(dataImageFilesInfo.getFileId());
                motorVehicleSale.setInvoiceNumber(data.getString("fpHm"));
                motorVehicleSale.setInvoiceCode(data.getString("fpDm"));
                motorVehicleSale.setInvoiceDate(Convert.toDate(data.getString("kprq")));
                motorVehicleSale.setInvoiceTotal(Convert.toBigDecimal(data.getString("jshj")));
                motorVehicleSale.setPreTaxAmount(Convert.toBigDecimal(data.getString("hjje")));
                motorVehicleSale.setSaveToken(token);
                if(ObjectUtil.isNotEmpty(coordinate)){
                    motorVehicleSale.setCoordinate(YesfpCoordinateUtil.getCoordinateArr(coordinate));
                    motorVehicleSale.setOrientation(Convert.toInt(coordinate.getString("degree")));
                    motorVehicleSale.setCoordinateStr(YesfpCoordinateUtil.getCoordinateStr(coordinate));
                }
                return new IdentificationData<>(fileType, motorVehicleSale);
            }else{
                //  增值税发票
                DataOcrInfo ocrInfo = new DataOcrInfo();
                ocrInfo.setId(IdUtil.simpleUUID());
                ocrInfo.setNcImageId(imageId);
                ocrInfo.setFileId(dataImageFilesInfo.getFileId());
                ocrInfo.setInvoiceNumber(data.getString("fpHm"));
                ocrInfo.setInvoiceCode(data.getString("fpDm"));
                ocrInfo.setInvoiceDate(Convert.toDate(data.getString("kprq")));
                ocrInfo.setCheckCode(data.getString("jym"));
                ocrInfo.setSumAmount(Convert.toBigDecimal(data.getString("hjje")));
                ocrInfo.setSellerNo(data.getString("xsfNsrsbh"));
                ocrInfo.setSellerName(data.getString("xsfMc"));
                ocrInfo.setSellerAddress(data.getString("xsfDzdh"));
                ocrInfo.setSellerAccount(data.getString("xsfYhzh"));
                ocrInfo.setBuyerAddress(data.getString("gmfDzdh"));
                ocrInfo.setBuyerName(data.getString("gmfMc"));
                ocrInfo.setBuyerNo(data.getString("gmfNsrsbh"));
                ocrInfo.setBuyerAccount(data.getString("mfYhzh"));
                ocrInfo.setRemark(data.getString("bz"));
                ocrInfo.setTotalLowercase(Convert.toBigDecimal(data.getString("jshj")));
                ocrInfo.setSumTax(Convert.toBigDecimal(data.getString("hjse")));
                ocrInfo.setIssuer(data.getString("kpr"));
                ocrInfo.setTotalUppercase(MoneyUtil.change(data.getString("jshj")));
                //ocrInfo.setImageId(imageId);
                ocrInfo.setSaveToken(token);
                if(ObjectUtil.isNotEmpty(coordinate)){
                    ocrInfo.setCoordinate(YesfpCoordinateUtil.getCoordinateArr(coordinate));
                    ocrInfo.setOrientation(Convert.toInt(coordinate.getString("degree")));
                    ocrInfo.setCoordinateStr(YesfpCoordinateUtil.getCoordinateStr(coordinate));
                }
                // 组装ocr明细数据 eq：如果有的话
                JSONArray jsonArray = data.getJSONArray("items");
                List<DataOcrDetails> detailsList = new ArrayList<>(16);
                if(CollectionUtil.isNotEmpty(jsonArray)){
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject detailJson = jsonArray.getJSONObject(i);
                        DataOcrDetails dataOcrDetails = new DataOcrDetails();
                        dataOcrDetails.setId(IdUtil.simpleUUID());
                        dataOcrDetails.setDetailAmount(Convert.toBigDecimal(detailJson.getString("xmje")));
                        dataOcrDetails.setFileId(dataImageFilesInfo.getFileId());
                        dataOcrDetails.setName(detailJson.getString("xmmc"));
                        dataOcrDetails.setCommodityName(detailJson.getString("xmmc"));
                        dataOcrDetails.setPrice(Convert.toBigDecimal(detailJson.getString("xmdj")));
                        dataOcrDetails.setTaxRate(StrUtil.toString(detailJson.getString("sl")));
                        dataOcrDetails.setStandard(detailJson.getString("ggxh"));
                        dataOcrDetails.setTax(Convert.toBigDecimal(detailJson.getString("se")));
                        dataOcrDetails.setUnit(detailJson.getString("dw"));
                        detailsList.add(dataOcrDetails);
                    }
                }
                return new IdentificationData<>(fileType, ocrInfo);
            }
        }
    }
}
