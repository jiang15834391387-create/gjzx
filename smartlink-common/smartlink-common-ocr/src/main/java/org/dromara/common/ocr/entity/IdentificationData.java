package org.dromara.common.ocr.entity;


import org.smartlink.business.domain.BaseEntitys;

/**
 * <p>Title: IdentificationData</p>
 * <p>提供了一个泛型元组对象</p>
 * <p>Description: 存储OCR识别信息</p>
 * @author datafly
 **/
public class IdentificationData<K extends String, T extends BaseEntitys> {


    public final K k;

    public final T t;


    public IdentificationData(K k, T t) {
        this.k = k;

        this.t = t;
    }
}
