package org.smartlink.server.nc.ncc.nccstand.response.allinvoice;

import lombok.Data;

/**
 * @description: 响应data
 * @author: L
 * @create:
 **/
@Data
public class StandAllInvoiceResponseData {

    private String code;

    private String msg;

    private String fpDm;

    private String fpHm;

    private String billId;

    private String saveToken;

}
