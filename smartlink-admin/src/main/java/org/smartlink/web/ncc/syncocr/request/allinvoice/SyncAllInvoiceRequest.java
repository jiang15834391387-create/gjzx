package org.smartlink.web.ncc.syncocr.request.allinvoice;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @description: NCC同步全票种发票信息接口
 * @author: L
 * @create:
 **/
@Data
public class SyncAllInvoiceRequest {

    /**
     * 制单人ID
     */
    private String userid;

    /**
     * 组织主键
     */
    private String orgCode;

    /**
     * 数据源
     */
    private String datasource;

    /**
     * 厂商编码
     */
    private String factorycode;

    /**
     * 单据类型
     */
    private String billtype;

    /**
     * 交易类型
     */
    private String transitype;

    /**
     * 单据pk
     */
    private String billid;

    /**
     * 发票信息
     */
    private List<JSONObject> data;

}
