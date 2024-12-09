package org.smartlink.server.nc.ocr.service.ccint.response;


import lombok.Data;

import java.util.List;

@Data
public class ItemListBean {

    /**
     * 识别字段结果
     */
    private String value;
    /**
     * 识别字段 key 中文描述
     */
    private String description;
    /**
     * 识别字段类型
     */
    private String key;
    /**
     * 识别字段在原图中的坐标位置
     */
    private List<Integer> position;
    /**
     * 识别字段在原图中的坐标位置
     */
    private Float confidence;

    @Override
    public String toString() {
        return "ItemListBean{" +
                "value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", key='" + key + '\'' +
                ", position=" + position +
                '}';
    }
}
