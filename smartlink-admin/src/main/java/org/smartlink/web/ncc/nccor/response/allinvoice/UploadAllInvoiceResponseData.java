package org.smartlink.web.ncc.nccor.response.allinvoice;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

/**
 * @description: ocr结果返回data
 * @author: L
 * @create:
 **/
@Data
public class UploadAllInvoiceResponseData {

    /**
     * 状态code
     */
    private String code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 发票信息JSON集合
     */
    private JSONArray datas;

}
