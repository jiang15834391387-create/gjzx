package org.smartlink.common.ocr.glority.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 票小蜜返回数据
 * @author lqm
 */
@Getter
@Setter
public  class GlorityDate {

    private String version;

    private String result;

    private String timestamp;

    /**
     * 信息说明
     */
    private String message;

    /**
     * 识别结果标识id,用于结果反馈
     */
    private String id;

    /**
     * 识别图片唯一标识
     */
    private String sha1;

    /**
     * 识别花费的时长，单位毫秒
     */
    private String time_cost;

    private List<IdentifyResults> identify_results;


}
