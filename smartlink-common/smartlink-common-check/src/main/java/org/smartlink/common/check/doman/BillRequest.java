package org.smartlink.common.check.doman;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BillRequest {
    //主表id
    private String hostId;
    //发票类型
    private String type;
    //发票详情
    private List<Map<String, Object>> details;
}
