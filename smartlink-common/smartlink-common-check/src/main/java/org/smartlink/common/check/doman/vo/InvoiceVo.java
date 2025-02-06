package org.smartlink.common.check.doman.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 发票夹列表对象
 */
@Data
public class InvoiceVo {
    private String id;//发票唯一标识
    private String fileId;//文件唯一标识
    private String sellerName; // 销售方
    private String buyerName; //购买方
    private String invoiceDate;//发票日期
    private String invoiceType; // 发票种类
    private String message; // 文件信息，上传成功、修改成功
    private String checkStatus; // 查验结果，如成功2、失败3
    private String money; //金额
    private String status;//文件状态 ,待报销、已报销、已删除
    private String grossAmount;//所有发票总计金额

    @JsonIgnore
    public BigDecimal getMoneyAsBigDecimal() {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(money);
    }
}
