package org.smartlink.common.ocr.xml.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataRailwayTicket;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * XML铁路电子客票转换类
 */
public class XmlRailwayInvoiceConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {
        private static final XmlRailwayInvoiceConversion INSTANCE = new XmlRailwayInvoiceConversion();
    }

    private XmlRailwayInvoiceConversion() {
    }

    public static XmlRailwayInvoiceConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject xmlData) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();

        // 获取xbrl节点下的所有数据
        JSONObject xbrlData = xmlData.getJSONObject("xbrli:xbrl");
        if (xbrlData == null) {
            return null;
        }

        DataRailwayTicket railwayTicket = new DataRailwayTicket();
        railwayTicket.setId(IdUtil.simpleUUID());
        railwayTicket.setFileId(dataImageFilesInfo.getFileId());

        // 基本信息映射
        railwayTicket.setInvoiceNumber(getContent(xbrlData, "inv:NumberOfInvoice"));
        railwayTicket.setDateOfIssue(getContent(xbrlData, "inv:DateOfIssue"));
        railwayTicket.setInvoiceTotal(getContent(xbrlData, "inv:TotalAmount"));
        railwayTicket.setTypeOfVoucher(getContent(xbrlData, "inv:TypeOfVoucher")); // 铁路特有字段
        railwayTicket.setName(getContent(xbrlData, "inv:PassengerName"));
        railwayTicket.setTrainNumber(getContent(xbrlData, "inv:TrainNumber"));
        railwayTicket.setSeat(getContent(xbrlData, "inv:ClassOfService"));
        railwayTicket.setStationGetOn(getContent(xbrlData, "inv:DepartureStation"));
        railwayTicket.setStationGetOff(getContent(xbrlData, "inv:ArrivalStation"));
        railwayTicket.setInvoiceTime(getContent(xbrlData, "inv:DepartureTime"));

        // 补充更多字段映射
        railwayTicket.setElectronicMark("1"); // 电子票标记
//        railwayTicket.setSeller(getContent(xbrlData, "inv:NameOfSeller"));
        railwayTicket.setBuyerTaxId(getContent(xbrlData, "inv:TaxpayerIdentificationNumberUnifiedSocialCreditCodeOfPurchaser"));
        railwayTicket.setBuyer(getContent(xbrlData, "inv:NameOfPurchaser"));
        railwayTicket.setBuyerAddrTel(getContent(xbrlData, "inv:AddressPhoneNumberOfPurchaser"));
        railwayTicket.setBuyerBankAccount(getContent(xbrlData, "inv:DepositBankAndAccountNumberOfPurchaser"));
        railwayTicket.setPhonicsOfDepartureStation(getContent(xbrlData, "inv:LocationOfDepartureStation"));
        railwayTicket.setPhonicsOfDestinationStation(getContent(xbrlData, "inv:LocationOfArrivalStation"));

        // 设置查验状态
        dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());

        // 添加到结果列表
        resultsList.add(new IdentificationData<>("railway_ticket", railwayTicket, null, "铁路电子客票识别成功"));

        return resultsList;
    }

    /**
     * 从JSON对象中获取指定key的content值
     */
    private String getContent(JSONObject data, String key) {
        if (data == null || !data.containsKey(key)) {
            return null;
        }

        Object value = data.get(key);
        if (value instanceof Map) {
            return ((Map<?, ?>) value).get("content") != null ? ((Map<?, ?>) value).get("content").toString() : null;
        }

        return value != null ? value.toString() : null;
    }
}
