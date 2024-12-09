package org.smartlink.server.nc.ncc.nccstand.request.taxinvoice;

import lombok.Data;

/**
 * @description: StandTaxInvoiceRequestData
 * @author: L
 * @create:
 **/
@Data
public class StandTaxInvoiceRequestData {

    private String fpDm;

    private String fpHm;

    private String saveToken;

    private String billid;

}
