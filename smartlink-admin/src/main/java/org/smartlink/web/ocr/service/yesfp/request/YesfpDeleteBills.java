package org.smartlink.web.ocr.service.yesfp.request;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title: YesfpDeleteBills </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
public class YesfpDeleteBills {

    private String billType;

    private String invoiceCode;

    private String invoiceNum;
}
