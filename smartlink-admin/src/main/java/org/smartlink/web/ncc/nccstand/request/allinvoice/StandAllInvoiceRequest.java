package org.smartlink.web.ncc.nccstand.request.allinvoice;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @description: 发票推送台账请求类
 * @author: L
 * @create:
 **/
@Data
public class StandAllInvoiceRequest {

    private String userid;

    private String orgCode;

    private String datasource;

    private String factorycode;

    private String billtype;

    private String transitype;

    private String billid;

    private List<JSONObject> data;

}
