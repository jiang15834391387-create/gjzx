package org.smartlink.server.nc.ocr.service.yesfp.conversion.tmp;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataQuotaInvoice;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.yesfp.utils.YesfpCoordinateUtil;
import org.springframework.core.convert.ConversionException;

/**
 * <p>Title: QuotaInvoiceConversion</p>
 * <p>
 * <p>Description:税务云定额发票识别信息转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class QuotaInvoiceConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {
        private static final QuotaInvoiceConversion INSTANCE = new QuotaInvoiceConversion();
    }

    private QuotaInvoiceConversion() {}

    public static QuotaInvoiceConversion getInstance() {
        return LazyHolder.INSTANCE;
    }


    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject jsonObject) throws ConversionException {
        JSONObject data = jsonObject.getJSONObject("data");
        String imageId = jsonObject.getString("imageId");
        JSONObject coordinate = jsonObject.getJSONObject("coordinate");
        String token = jsonObject.getString("token");
        dataImageFilesInfo.setNcImageId(imageId);
        DataQuotaInvoice quotaInvoice=  new DataQuotaInvoice();
        if(ObjectUtil.isNotEmpty(coordinate)){
            quotaInvoice.setCoordinate(YesfpCoordinateUtil.getCoordinateArr(coordinate));
            quotaInvoice.setOrientation(Convert.toInt(coordinate.getString("degree")));
            quotaInvoice.setCoordinateStr(YesfpCoordinateUtil.getCoordinateStr(coordinate));
        }
        quotaInvoice.setNcImageId(imageId);
        quotaInvoice.setId(IdUtil.simpleUUID());
        quotaInvoice.setFileId(dataImageFilesInfo.getFileId());
        quotaInvoice.setId(IdUtil.simpleUUID());
        quotaInvoice.setInvoiceCode(data.getString("invoiceCode"));
        quotaInvoice.setInvoiceNumber(data.getString("invoiceNum"));
        quotaInvoice.setInvoiceTotal(Convert.toBigDecimal(data.getString("totalAmount")));
        //quotaInvoice.setImageId(imageId);
        quotaInvoice.setSaveToken(token);

        return new IdentificationData<>(InvoiceConstants.QUOTA_INVOICE,quotaInvoice);
    }
}
