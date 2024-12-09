package org.smartlink.server.nc.ocr.service.ccint.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataAcceptanceBill;
import org.smartlink.server.nc.domain.invoice.DataTaxiTickets;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.ccint.ccintenum.CcintKeyValueEnum;
import org.smartlink.server.nc.ocr.service.ccint.response.ItemListBean;
import org.smartlink.server.nc.ocr.service.ccint.response.ObjectListBean;
import org.smartlink.server.nc.ocr.service.nccbip.BIPCoordinateUtil;
import org.springframework.core.convert.ConversionException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: AcceptanceBillConversion</p>
 * <p>
 * <p>Description:禹水承兑汇票识别信息转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class AcceptanceBillConversion implements ChangeIdentifyInfo<ObjectListBean> {

    //懒加载线程安全
    private static class LazyHolder {
        private static final AcceptanceBillConversion INSTANCE = new AcceptanceBillConversion();
    }
    public static AcceptanceBillConversion getInstance() {
        return LazyHolder.INSTANCE;
    }
    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, ObjectListBean objectListBean) throws ConversionException {
        List<ItemListBean> item_list = objectListBean.getItem_list();
        //置信度
        List<String> confidence=new ArrayList<String>();
        DataAcceptanceBill acceptanceBill = new DataAcceptanceBill();
        acceptanceBill.setFileId(dataImageFilesInfo.getFileId());
        acceptanceBill.setId(IdUtil.simpleUUID());
        Class<?> clazz = DataTaxiTickets.class;
        Field[] declaredFields = clazz.getDeclaredFields();
        String fieldName  = "";
        for (Field declaredField: declaredFields) {
            fieldName = declaredField.getName();
            for (ItemListBean i : item_list) {
                if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("railNumber"))) {
                    acceptanceBill.setInvoiceNumber(i.getValue());
                    if (fieldName.equals("invoiceNumber")){
                        confidence.add("\""+ fieldName+"\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("chuName"))) {
                    acceptanceBill.setDisbursementName(i.getValue());
                    if (fieldName.equals("disbursementName")){
                        confidence.add("\""+ fieldName+"\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("money"))) {
                    acceptanceBill.setInvoiceTotal(Convert.toBigDecimal(i.getValue()));
                    if (fieldName.equals("invoiceTotal")){
                        confidence.add("\""+ fieldName+"\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("shouName"))) {
                    acceptanceBill.setPayeeName(i.getValue());
                    if (fieldName.equals("payeeName")){
                        confidence.add("\""+ fieldName+"\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("timeGetOff"))) {
                    acceptanceBill.setDisbursementAccount(i.getValue());
                    if (fieldName.equals("disbursementAccount")){
                        confidence.add("\""+ fieldName+"\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("mileage"))) {
                    acceptanceBill.setDisbursementBank(i.getValue());
                    if (fieldName.equals("disbursementBank")){
                        confidence.add("\""+ fieldName+"\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("taxTotal"))) {
                    acceptanceBill.setPayeeName(i.getValue());
                    if (fieldName.equals("payeeName")){
                        confidence.add("\""+ fieldName+"\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("province"))) {
                    acceptanceBill.setPayeeAccount(i.getValue());
                    if (fieldName.equals("payeeAccount")){
                        confidence.add("\""+ fieldName+"\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("licensePlate"))) {
                    acceptanceBill.setPayeeBank(i.getValue());
                    if (fieldName.equals("payeeBank")){
                        confidence.add("\""+ fieldName+"\"" + ":" + i.getConfidence());
                    }
                }else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("existInvoiceStamp"))) {
                    acceptanceBill.setAccountLineNumber(i.getValue());
                    if (fieldName.equals("accountLineNumber")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                }else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("accountName"))) {
                    acceptanceBill.setAccountName(i.getValue());
                    if (fieldName.equals("accountName")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                }else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("invoiceTotal"))) {
                    acceptanceBill.setInvoiceTotal(Convert.toBigDecimal(i.getValue()));
                    if (fieldName.equals("invoiceTotal")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                }else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("totalUppercase"))) {
                    acceptanceBill.setTotalUppercase(i.getValue());
                    if (fieldName.equals("totalUppercase")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                }else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("invoiceType"))) {
                    acceptanceBill.setInvoiceType(Convert.toBigDecimal(i.getValue()));
                    if (fieldName.equals("invoiceType")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                }
            }
        }
        String confidenceJson ="{"+ String.join(",", confidence)+"}";
        acceptanceBill.setConfidence(confidenceJson);
        dataImageFilesInfo.setFileType(InvoiceConstants.ACCEPTANCE_BILL);
        //base64
        acceptanceBill.setCoordinate(Convert.toStrArray(objectListBean.getPosition()));
        acceptanceBill.setOption("base64");
        acceptanceBill.setBase64(objectListBean.getImage());
        acceptanceBill.setCoordinateStr(BIPCoordinateUtil.getCciCoordinateStr(Convert.toIntArray(objectListBean.getPosition())));
        return new IdentificationData<>(dataImageFilesInfo.getFileType(), acceptanceBill);
    }

}

