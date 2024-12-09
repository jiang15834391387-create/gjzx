package org.smartlink.server.nc.ocr.service.yesfp.request;


import lombok.*;
import org.smartlink.server.nc.ocr.service.yesfp.reponse.Invoices;

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
public class YesfpTaxInvoiceRequest {

    private String nsrsbh;

    private String orgcode;

    private String submitter;

    private List<Invoices> invoices;
}
