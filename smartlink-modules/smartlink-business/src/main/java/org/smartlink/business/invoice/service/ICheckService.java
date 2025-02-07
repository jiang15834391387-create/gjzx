package org.smartlink.business.invoice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotBlank;
import org.smartlink.common.check.doman.InvoicePageQuery;
import org.smartlink.common.check.doman.InvoiceRequest;
import org.smartlink.common.check.doman.vo.InvoiceVo;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.entity.domain.business.response.DataResponseDTO;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

//发票夹业务接口
public interface ICheckService {
    int deleteWithValidByIds(Collection<String> ids);

    int invoiceAlter(InvoiceRequest request) throws Exception;

    Page<InvoiceVo> getInvoicePage(InvoicePageQuery pageQuery) throws ExecutionException, InterruptedException;

    R<DataResponseDTO> selectInvoiceDetail(@NotBlank String fileId);
}
