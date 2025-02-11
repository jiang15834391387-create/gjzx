package org.smartlink.common.ocr.entity;


import org.smartlink.common.mybatis.core.domain.BaseEntity;

/**
 * <p>Title: IdentificationData</p>
 * <p>提供了一个泛型元组对象</p>
 * <p>Description: 存储OCR识别信息</p>
 * @author datafly
 **/
public class IdentificationData<K extends String, T, C> {
    public final K k;
    public final T t;
    public final C c;

    public IdentificationData(K k, T t, C c) {
        this.k = k;
        this.t = t;
        this.c = c;
    }
}
