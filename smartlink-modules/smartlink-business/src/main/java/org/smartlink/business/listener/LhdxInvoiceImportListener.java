package org.smartlink.business.listener;


import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.doman.vo.LhdxInvoiceVo;
import org.smartlink.common.excel.core.ExcelListener;
import org.smartlink.common.excel.core.ExcelResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户自定义导入
 *
 * @author Lion Li
 */
@Slf4j
public class LhdxInvoiceImportListener extends AnalysisEventListener<LhdxInvoiceVo> implements ExcelListener<LhdxInvoiceVo> {

    private List<LhdxInvoiceVo> lhdxInvoiceVoList = new ArrayList<>();

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
        lhdxInvoiceVo.setInvoiceNumberCode(lhdxInvoiceVo.getInvoiceNumber() + lhdxInvoiceVo.getInvoiceCode());
        lhdxInvoiceVoList.add(lhdxInvoiceVo);
    }

    @Override
    public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext analysisContext) {

    }
}
