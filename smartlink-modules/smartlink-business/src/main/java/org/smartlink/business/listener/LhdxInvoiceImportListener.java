package org.smartlink.business.listener;


import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.doman.vo.LhdxInvoiceVo;
import org.smartlink.common.core.utils.SpringUtils;
import org.smartlink.common.entity.domain.business.domain.bo.DataOcrInfoBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataOcrInfoVo;
import org.smartlink.common.entity.domain.business.service.IDataOcrInfoService;
import org.smartlink.common.excel.core.ExcelListener;
import org.smartlink.common.excel.core.ExcelResult;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户自定义导入
 *
 * @author Lion Li
 */
@Slf4j
public class LhdxInvoiceImportListener extends AnalysisEventListener<LhdxInvoiceVo> implements ExcelListener<LhdxInvoiceVo> {

    private final List<LhdxInvoiceVo> lhdxInvoiceVoList = new ArrayList<>();
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
                return null;
            }
        };
    }

    @Override
    public void invoke(LhdxInvoiceVo lhdxInvoiceVo, com.alibaba.excel.context.AnalysisContext analysisContext) {

        DataOcrInfoBo dataOcrInfo = new DataOcrInfoBo();

        String invoiceCode = lhdxInvoiceVo.getInvoiceCode().replace("'", "");
        String invoiceNumber = lhdxInvoiceVo.getInvoiceNumber().replace("'", "");

        dataOcrInfo.setInvoiceCode(invoiceCode);
        dataOcrInfo.setInvoiceNumber(invoiceNumber);
        lhdxInvoiceVo.setInvoiceNumberCode(invoiceCode + invoiceNumber);
        List<DataOcrInfoVo> dataOcrInfoVos = iDataOcrInfoService.queryList(dataOcrInfo);
        if (CollectionUtils.isEmpty(dataOcrInfoVos)) lhdxInvoiceVoList.add(lhdxInvoiceVo);
    }

    @Override
    public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext analysisContext) {

    }
}
