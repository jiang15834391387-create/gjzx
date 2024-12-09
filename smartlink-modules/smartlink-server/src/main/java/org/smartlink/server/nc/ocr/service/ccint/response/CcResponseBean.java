package org.smartlink.server.nc.ocr.service.ccint.response;


import lombok.Data;

import java.util.List;

/**
 * @description: 合合识别内容bean
 * @author: L
 * @create:
 **/

@Data
public class CcResponseBean {
    /**
     *
     * 多张票据 OCR 识别结果集合
     */
    private List<ObjectListBean> object_list;

    @Override
    public String toString() {
        return "CcResponseBean{" +
                "object_list=" + object_list +
                '}';
    }
}
