package org.smartlink.server.nc.utils;

import cn.hutool.core.util.StrUtil;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.constant.NcCheckInvoiceTypeConstant;


/**
 * @description: NCC发票类型转换类
 * @author: L
 * @create:
 **/
public class NcTypeConvertUtil {

    /**
     * 根据查验接口返回的NCC的·发票类型获取影像系统内部的发票
     * 发票类型
     * 增值税电子普票 1 8 14 32
     * 增值税电子专票 2 31
     * 增值税普票 3 10
     * 增值税专票 4 11
     * 卷票 9 12
     * 机动车发票 5
     * 货物运输 6
     * @param verifyFileType 发票类型
     * @return 结果
     */
    public static String getSystemFileType(String verifyFileType){
        String fileType;
        if(StrUtil.equals(verifyFileType, NcCheckInvoiceTypeConstant.NCC_VAT_ELECTRONIC_INVOICE)
                ||StrUtil.equals(verifyFileType, NcCheckInvoiceTypeConstant.NCC_VAT_ELECTRONIC_INVOICE_REFINED_OIL)
                ||StrUtil.equals(verifyFileType, NcCheckInvoiceTypeConstant.NCC_TOLL_VAT_ELECTRONIC_INVOICE)
                || StrUtil.equals(verifyFileType,NcCheckInvoiceTypeConstant.NEW_NCC_VAT_ELECTRONIC_INVOICE)
                || StrUtil.equals(verifyFileType,NcCheckInvoiceTypeConstant.NEW_TRANSI_NCC_VAT_ELECTRONIC_INVOICE)){
            // 设置为电子普通发票
            fileType = InvoiceConstants.ELECTRONIC_INVOICE;
        }else if(StrUtil.equals(verifyFileType, NcCheckInvoiceTypeConstant.NCC_VAT_INVOICE)
                ||StrUtil.equals(verifyFileType, NcCheckInvoiceTypeConstant.NCC_VAT_INVOICE_OIL_ROLL)){
            //设置为增值税普通发票
            fileType = InvoiceConstants.TAX_INVOICE;
        }else if(StrUtil.equals(verifyFileType, NcCheckInvoiceTypeConstant.NCC_VAT_SPECIAL_INVOICE)
                ||StrUtil.equals(verifyFileType, NcCheckInvoiceTypeConstant.NCC_VAT_SPECIAL_INVOICE_OIL)){
            //设置为增值税专用发票
            fileType = InvoiceConstants.TAX_SPECIAL_INVOICE;
        }else if(StrUtil.equals(verifyFileType, NcCheckInvoiceTypeConstant.NCC_VAT__INVOICE_REFINED_OIL_ROLL)
                ||StrUtil.equals(verifyFileType, NcCheckInvoiceTypeConstant.NCC_VAT_INVOICE_ROLL)){
            //设置为增值卷票
            fileType = InvoiceConstants.ROLL_TICKET;
        }else if(StrUtil.equals(verifyFileType, NcCheckInvoiceTypeConstant.NCC_MOTOR_VEHICLE_SALES)){
            //设置为机动车发票
            fileType = InvoiceConstants.MOTOR_VEHICLE_SALE;
        }else if(StrUtil.equals(verifyFileType, NcCheckInvoiceTypeConstant.NCC_VAT_ELECTRONIC_SPECIAL_INVOICE)
                || StrUtil.equals(verifyFileType,NcCheckInvoiceTypeConstant.NEW_NCC_VAT_ELECTRONIC_SPECIAL_INVOICE)
                || StrUtil.equals(verifyFileType,NcCheckInvoiceTypeConstant.NEW_TRANSI_NCC_VAT_ELECTRONIC_SPECIAL_INVOICE)){
            //设置为电子专票
            fileType = InvoiceConstants.ELECTRONIC_OFD_INVOICE;
        }else{
            // 默认普票
            fileType = InvoiceConstants.TAX_INVOICE;
        }
        return fileType;
    }

}
