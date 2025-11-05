package org.smartlink.business.listener;


import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.doman.vo.LhdxInvoiceVo;
import org.smartlink.common.core.utils.SpringUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.bo.DataOcrInfoBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataOcrInfoVo;
import org.smartlink.common.entity.domain.business.service.IDataOcrInfoService;
import org.smartlink.common.excel.core.ExcelListener;
import org.smartlink.common.excel.core.ExcelResult;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 系统用户自定义导入
 *
 * @author Lion Li
 */
@Slf4j
public class LhdxInvoiceImportListener extends AnalysisEventListener<LhdxInvoiceVo> implements ExcelListener<LhdxInvoiceVo> {

    private final List<LhdxInvoiceVo> lhdxInvoiceVoList = new ArrayList<>();
    private final List<String> lhdxErrorList = new ArrayList<>();
    private final IDataOcrInfoService iDataOcrInfoService;

    public LhdxInvoiceImportListener() {
        this.iDataOcrInfoService = SpringUtils.getBean(IDataOcrInfoService.class);
    }


    @Override
    public ExcelResult<LhdxInvoiceVo> getExcelResult() {
        return new ExcelResult<>() {

            @Override
            public String getAnalysis() {
                return null;
            }

            @Override
            public List<LhdxInvoiceVo> getList() {
                return lhdxInvoiceVoList;
            }

            @Override
            public List<String> getErrorList() {
                return lhdxErrorList;
            }
        };
    }

    @Override
    public void invoke(LhdxInvoiceVo lhdxInvoiceVo, com.alibaba.excel.context.AnalysisContext analysisContext) {
        DataOcrInfoBo dataOcrInfo = new DataOcrInfoBo();
        String invoiceCode = StringUtils.isNotBlank(lhdxInvoiceVo.getInvoiceCode()) ? lhdxInvoiceVo.getInvoiceCode().replace("'", "") : "";
        String invoiceNumber = StringUtils.isNotBlank(lhdxInvoiceVo.getInvoiceNumber()) ?  lhdxInvoiceVo.getInvoiceNumber().replace("'", "") : "";
        dataOcrInfo.setInvoiceCode(invoiceCode);
        dataOcrInfo.setInvoiceNumber(invoiceNumber);
        lhdxInvoiceVo.setInvoiceNumberCode(invoiceCode + invoiceNumber);
        List<DataOcrInfoVo> dataOcrInfoVos = iDataOcrInfoService.queryList(dataOcrInfo);

        if (CollectionUtils.isEmpty(dataOcrInfoVos)) {
            String invoiceDate = lhdxInvoiceVo.getInvoiceDate();
            if (!isValidDate(invoiceDate)){
                lhdxErrorList.add("序号：【"+lhdxInvoiceVo.getSellerName() + "】行数据，日期格式错误，已经跳过，请调整格式后重新导入！");
                return;
            }

            String invoiceType = lhdxInvoiceVo.getInvoiceType();
            if (invoiceType == null || (!invoiceType.equals("电子发票（增值税专用发票）") && !invoiceType.equals("电子发票（普通发票）"))){
                lhdxErrorList.add("序号：【"+lhdxInvoiceVo.getSellerName() + "】行数据，发票类型错误，已经跳过，发票类型为（电子发票（增值税专用发票） 或者 电子发票（普通发票））请调整格式后重新导入！");
                return;
            }
            lhdxInvoiceVoList.add(lhdxInvoiceVo);
        }
    }

    @Override
    public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext analysisContext) {

    }

    private static final DateTimeFormatter[] formatters = {
        DateTimeFormatter.ISO_LOCAL_DATE,              // yyyy-MM-dd
        DateTimeFormatter.ofPattern("yyyy/MM/dd"),     // yyyy/MM/dd
        DateTimeFormatter.ofPattern("yyyy/M/d"),       // yyyy/M/d
        DateTimeFormatter.ofPattern("yyyy/MM/d"),      // yyyy/MM/d
        DateTimeFormatter.ofPattern("yyyy/M/dd"),      // yyyy/M/dd
        DateTimeFormatter.ofPattern("yyyy年MM月dd日"),    // yyyy年MM月dd日
        DateTimeFormatter.ofPattern("yyyy年M月d日"),      // yyyy年M月d日
        DateTimeFormatter.ofPattern("yyyy年MM月d日"),     // yyyy年MM月d日
        DateTimeFormatter.ofPattern("yyyy年M月dd日")      // yyyy年M月dd日
    };

    /**
     * 检查单元格是否为有效日期
     */
    public static boolean isValidDate(String dateStr) {
        if (dateStr == null) return false;

        try {
            return isValidDateString(dateStr);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查字符串是否为有效日期
     */
    public static boolean isValidDateString(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }

        // 尝试多种日期格式解析
        for (DateTimeFormatter formatter : formatters) {
            try {
                LocalDate invoiceDate = LocalDate.parse(dateStr, formatter);
                Date date = Date.from(invoiceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                // 检查日期范围
                if (isValidDateRange(date)) {
                    return true;
                }
            } catch (Exception e) {
                // 尝试下一种格式
            }
        }

        return false;
    }

    /**
     * 检查日期范围是否合理
     */
    private static boolean isValidDateRange(Date date) {
        if (date == null) return false;

        // 设置合理的日期范围（例如：1900-2100年）
        Calendar cal = Calendar.getInstance();
        cal.set(1900, Calendar.JANUARY, 1);
        Date minDate = cal.getTime();

        cal.set(2100, Calendar.DECEMBER, 31);
        Date maxDate = cal.getTime();

        return !date.before(minDate) && !date.after(maxDate);
    }
}
