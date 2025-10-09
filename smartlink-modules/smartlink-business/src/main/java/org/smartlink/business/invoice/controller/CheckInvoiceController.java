package org.smartlink.business.invoice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.doman.dto.FileDataDTO;
import org.smartlink.business.doman.vo.InvoiceVo;
import org.smartlink.business.invoice.service.ICheckService;
import org.smartlink.common.check.doman.InvoicePageQuery;
import org.smartlink.common.check.doman.InvoiceRequest;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.entity.domain.business.response.DataResponseDTO;
import org.smartlink.common.web.core.BaseController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/check/invoice")
public class CheckInvoiceController extends BaseController {

    private final ICheckService service;

    public CheckInvoiceController(ICheckService service) {
        this.service = service;
    }
    /**
     * 发票修改
     */
    @PostMapping("/updateInvoice")
    public R<Void> updateInvoice(@RequestBody InvoiceRequest request) throws Exception {
        return service.invoiceAlter(request);
    }
    /**
     *
     *批量删除发票
     */
    @GetMapping("/remove")
    public R<Void> remove(@RequestParam(required = false) String[] ids) {return service.deleteWithValidByIds(List.of(ids));
    }
    /**
     *发票列表查询
     *
     */
    @PostMapping("/selectPage")
    public Page<InvoiceVo> getSelectInvoices(@RequestBody InvoicePageQuery pageQuery) throws Exception {
        return service.getInvoicePage(pageQuery);
    }
    /**
     *发票列表查询查验成功
     *
     */
    @PostMapping("/selectPageCheck")
    public Page<InvoiceVo> selectPageCheck(@RequestBody InvoicePageQuery pageQuery) throws Exception {
        return service.selectPageCheck(pageQuery);
    }
    /**
     *发票详情查询
     *
     */
    @GetMapping("/invoiceDetail")
    public R<DataResponseDTO> selectInvoiceDetail(@RequestParam(value = "fileId")  @NotBlank String fileId){
        return service.selectInvoiceDetail(fileId);
    }
    /**
     *发票新增
     */
    @PostMapping("/addInvoice")
    public R<Void> addInvoice(@RequestBody InvoiceRequest request) throws Exception {
        return service.addInvoice(request);
    }
    /**
     *查询文件信息
     */
    @PostMapping("/selectFileInfo")
    public R<List<FileDataDTO>> selectFileInfo(@RequestBody List<String> fileIds){
        return service.selectFileInfo(fileIds);
    }

    /**
     *返回id和状态
     */
    @PostMapping("/selectPageStatus")
    public Page<InvoiceVo> selectPageList(@RequestBody InvoicePageQuery pageQuery) throws ExecutionException, InterruptedException {
        return service.selectPageList(pageQuery);
    }

}
