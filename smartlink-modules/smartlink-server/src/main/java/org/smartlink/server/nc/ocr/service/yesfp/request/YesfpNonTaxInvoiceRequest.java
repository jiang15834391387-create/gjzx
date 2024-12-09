package org.smartlink.server.nc.ocr.service.yesfp.request;

import lombok.*;

import java.util.List;

/**
 * <p>Title: YesfpSaveOcrInfo </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YesfpNonTaxInvoiceRequest {

    private String nsrsbh;

    private String orgcode;

    private List<Bills> bills;

    private String srcBillCode;

    private String srcBillType;
}
