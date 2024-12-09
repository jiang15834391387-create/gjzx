package org.smartlink.server.nc.ocr.service.yesfp.conversion.tmp;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataTollRoads;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.yesfp.utils.YesfpCoordinateUtil;
import org.springframework.core.convert.ConversionException;

/**
 * <p>Title: AircraftInvoiceConversion</p>
 * <p>
 * <p>Description:税务云过路费发票识别信息转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class TollRoadsConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {

        private static final TollRoadsConversion INSTANCE = new TollRoadsConversion();
    }

    private TollRoadsConversion() {}

    public static TollRoadsConversion getInstance() {

        return LazyHolder.INSTANCE;
    }


    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject jsonObject) throws ConversionException {
        JSONObject data = jsonObject.getJSONObject("data");
        String imageId = jsonObject.getString("imageId");
        String token = jsonObject.getString("token");
        JSONObject coordinate = jsonObject.getJSONObject("coordinate");
        dataImageFilesInfo.setNcImageId(imageId);
        DataTollRoads tollRoads=  new DataTollRoads();
        if(ObjectUtil.isNotEmpty(coordinate)){
            tollRoads.setCoordinate(YesfpCoordinateUtil.getCoordinateArr(coordinate));
            tollRoads.setOrientation(Convert.toInt(coordinate.getString("degree")));
            tollRoads.setCoordinateStr(YesfpCoordinateUtil.getCoordinateStr(coordinate));
        }
        tollRoads.setNcImageId(imageId);
        tollRoads.setId(IdUtil.simpleUUID());
        tollRoads.setFileId(dataImageFilesInfo.getFileId());
        tollRoads.setInvoiceCode(data.getString("invoiceCode"));
        tollRoads.setInvoiceNumber(data.getString("invoiceNum"));
        tollRoads.setInvoiceDate(Convert.toDate(data.getString("date")));
        tollRoads.setInvoiceTime(data.getString("time"));
        tollRoads.setEntrance(data.getString("entrance"));
        tollRoads.setTollExit(data.getString("exit"));
        tollRoads.setInvoiceTotal(Convert.toBigDecimal(data.getString("totalAmount")));
        //tollRoads.setImageId(imageId);
        tollRoads.setSaveToken(token);

        return new IdentificationData<>(InvoiceConstants.TOLL_ROADS,tollRoads);

    }
}
