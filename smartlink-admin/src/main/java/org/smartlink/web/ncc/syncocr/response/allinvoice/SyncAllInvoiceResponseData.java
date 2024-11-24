package org.smartlink.web.ncc.syncocr.response.allinvoice;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @description: 返回data
 * @author: L
 * @create:
 **/
@Data
public class SyncAllInvoiceResponseData {

    private String code;

    private String msg;

    private List<JSONObject> datas;

}
