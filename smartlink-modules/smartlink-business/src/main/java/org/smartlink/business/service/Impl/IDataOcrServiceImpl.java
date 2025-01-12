package org.smartlink.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.smartlink.business.service.IDataOcrService;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.mapper.DataDidiItineraryDetailsMapper;
import org.smartlink.common.entity.domain.business.mapper.DataImageFilesInfoMapper;
import org.smartlink.common.entity.domain.business.mapper.DataOcrDetailsMapper;
import org.smartlink.common.entity.domain.business.service.IDataImageFilesInfoService;
import org.smartlink.common.entity.domain.business.service.IDataOcrDetailsService;
import org.smartlink.common.entity.domain.business.service.IDataOcrInfoService;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Zhang Shuai
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class IDataOcrServiceImpl implements IDataOcrService {


    private final DataOcrDetailsMapper dataOcrDetailsMapper;
    private final DataImageFilesInfoMapper dataImageFilesInfoMapper;
    private final DataDidiItineraryDetailsMapper dataDidiItineraryDetailsMapper;

    private final IDataImageFilesInfoService dataImageFilesInfoService;
    private final IDataOcrInfoService iDataOcrInfoService;
    private final IDataOcrDetailsService iDataOcrDetailsService;

    /**
     * ocr信息插入或保存  （插入会根据FileID修改文件表的type为传入type，修改根据ID修改）
     *
     * @param key        发票类型
     * @param obj 发票实体
     * @return 0 失败 1 成功
     */
    @Override
    public R ocrInsertOrUpdateByBaseEntity(String key, Object obj) throws Exception {
        if (key.isEmpty()){
            return R.warn("发票类型识别为空！");
        }
        switch (key) {
            //增值税、机打
            case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_TAX_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_QUKUAILIAN_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
            case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_LIST:
            case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
                DataOcrInfo dataOcrInfo = new DataOcrInfo();
                BeanUtils.copyProperties(obj,dataOcrInfo);
//                DataOcrInfo dataOcrInfo = MapstructUtils.convert(obj, DataOcrInfo.class);
                Boolean info = iDataOcrInfoService.insert(dataOcrInfo);
                log.info("增值税信息入库:{}", info);
                List<DataOcrDetails> list = dataOcrInfo.getDetails();
                Boolean detail = iDataOcrDetailsService.insertBatch(list);
                log.info("增值税明细信息入库:{}", detail);
                break;
//            // 如果有其他的case分支，可以继续添加在这里，例如：
//            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
//                System.out.println("匹配到其他代码分支");
//                break;
//            // 如果有其他的case分支，可以继续添加在这里，例如：
//            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
//                System.out.println("匹配到其他代码分支");
//                break;
//            // 如果有其他的case分支，可以继续添加在这里，例如：
//            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
//                System.out.println("匹配到其他代码分支");
//                break;
//            // 如果有其他的case分支，可以继续添加在这里，例如：
//            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
//                System.out.println("匹配到其他代码分支");
//                break;
//            // 如果有其他的case分支，可以继续添加在这里，例如：
//            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
//                System.out.println("匹配到其他代码分支");
//                break;
//            // 如果有其他的case分支，可以继续添加在这里，例如：
//            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
//                System.out.println("匹配到其他代码分支");
//                break;
//            // 如果有其他的case分支，可以继续添加在这里，例如：
//            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
//                System.out.println("匹配到其他代码分支");
//                break;
//            // 如果有其他的case分支，可以继续添加在这里，例如：
//            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
//                System.out.println("匹配到其他代码分支");
//                break;
//            // 如果有其他的case分支，可以继续添加在这里，例如：
//            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
//                System.out.println("匹配到其他代码分支");
//                break;
//            // 如果有其他的case分支，可以继续添加在这里，例如：
//            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
//                System.out.println("匹配到其他代码分支");
//                break;
            default:
                // 当没有匹配到任何case分支时执行的默认逻辑
                return R.ok("没有匹配到发票处理流程！");
        }


//        Map<String, Object> map = BeanUtil.beanToMap(baseEntity);
//        map.put("type", key);
//        R<Void> voidR = ocrInsertOrUpdate(map);
//        int code = voidR.getCode();
        return R.ok();
    }





}
