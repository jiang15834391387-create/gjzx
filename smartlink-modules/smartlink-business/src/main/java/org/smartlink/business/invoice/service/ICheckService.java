package org.smartlink.business.invoice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotBlank;
import org.smartlink.business.doman.vo.InvoiceVo;
import org.smartlink.business.doman.vo.InvoiceWriteBackVo;
import org.smartlink.common.check.doman.InvoicePageQuery;
import org.smartlink.common.check.doman.InvoiceRequest;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.entity.domain.business.response.DataResponseDTO;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

//发票夹业务接口
public interface ICheckService {
    R<Void> deleteWithValidByIds(Collection<String> ids);

    R<Void> invoiceAlter(InvoiceRequest request) throws Exception;

    Page<InvoiceVo> getInvoicePage(InvoicePageQuery pageQuery) throws ExecutionException, InterruptedException;
    Page<InvoiceVo> selectPageCheck(InvoicePageQuery pageQuery) throws ExecutionException, InterruptedException;
    Page<InvoiceVo> selectPageCommon(InvoicePageQuery pageQuery, List<InvoiceVo> invoiceVosList) throws ExecutionException, InterruptedException;

    R<DataResponseDTO> selectInvoiceDetail(@NotBlank String fileId);

    R<Void> addInvoice(InvoiceRequest request) throws Exception;

    List<InvoiceWriteBackVo> invoiceWriteSelect(List<HashMap<String,Object>> list);
}
