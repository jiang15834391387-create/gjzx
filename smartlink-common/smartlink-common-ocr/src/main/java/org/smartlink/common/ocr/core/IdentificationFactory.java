package org.smartlink.common.ocr.core;


import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

/**
 * <p>Title: IdentificationFactory</p>
 * <p>识别内容转换实体工厂</p>
 * <p>Description: 识别内容转换实体</p>
 * @author lqm
 **/
public interface IdentificationFactory<T> {

    /**
     * 如果识别信息{@code T}不存在则返回{@code null}
     *
     * @param t 需要转换的对象
     * @return ChangeIdentifyInfo 如果需要转换的对象不存在或识别错误时则返回{@code null}
     */
    ChangeIdentifyInfo conversionInfo(DataImageFilesInfo dataImageFilesInfo, T t) throws OcrException;


}
