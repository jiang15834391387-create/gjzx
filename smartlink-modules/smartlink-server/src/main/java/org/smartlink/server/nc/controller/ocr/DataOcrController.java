package org.smartlink.server.nc.controller.ocr;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.server.nc.constant.Constants;
import org.smartlink.server.nc.constant.ParamConstants;
import org.smartlink.server.nc.domain.DataCmInfo;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.dto.NcDeleteServiceDTO;
import org.smartlink.server.nc.domain.invoice.bo.ManuallyCheckInvoiceDTO;
import org.smartlink.server.nc.domain.modle.BaseEntity;
import org.smartlink.server.nc.factory.NcConfigFactory;
import org.smartlink.server.nc.mapper.DataImageFilesInfoMapper;
import org.smartlink.server.nc.properties.NcProperties;
import org.smartlink.server.nc.service.brecheck.BreCheckInvoiceService;
import org.smartlink.server.nc.service.invoice.InvoiceRelevantService;
import org.smartlink.server.nc.service.nc.*;
import org.smartlink.server.nc.utils.ExceptionUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 发票相关Controller
 *
 * @author L
 */
@Validated
@Api(value = "全票种控制器", tags = {"全票种控制器管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/ocr/dataOcr")
@Slf4j
public class DataOcrController extends BaseController {

    private final IDataOcrService iDataOcrService;
    private final IDataImageTreeService iDataImageTreeService;
    private final InvoiceRelevantService invoiceRelevantService;
//    private final BipByImgService bipByImgService;
    private final IDataCurrentTaskService dataCurrentTaskService;
    private final IDataImageFilesInfoService iDataImageFilesInfoService;
    private final IDataCmInfoService dataCmInfoService;
    private final BreCheckInvoiceService breCheckInvoiceService;
//    private final ImageFilesInfoService imageFilesInfoService;
    private final DataImageFilesInfoMapper baseMapper;
    private final IDataOcrService dataOcrService;

//    private final InvoiceRetrievalService invoiceRetrievalService;

    @ApiOperation("查询全部发票种类")
    @SaCheckPermission("ocr:dataOcr:list")
    @PostMapping("/ocrQuery")
    public TableDataInfo<BaseEntity> ocrQuery(@ApiParam("请求参数和发票类型") @RequestBody Map<String, Object> map, @RequestBody PageQuery pageQuery) throws Exception {
        return iDataOcrService.ocrQuery(map, pageQuery);
    }

    @ApiOperation("ocr信息查询")
    @SaCheckPermission("ocr:dataOcr:query")
    @GetMapping("/ocrQueryByFileId/{fileId}")
    public R<List<BaseEntity>> ocrQueryByFileId(@ApiParam("图片表主键")
                                                @NotNull(message = "主键不能为空")
                                                @PathVariable("fileId") String fileId) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        List<BaseEntity> list = iDataOcrService.multipleOcrQueryByFileId(fileId);
        return R.ok(list);

    }
    @ApiOperation("发票转图片")
    // @SaCheckPermission("ocr:dataOcr:edit")
    @GetMapping("/ocrConvertToPicture/{fileId}")
    public R<Void> invoiceConvertToPicture(@ApiParam("文件ID")
                                           @NotEmpty(message = "文件ID不能为空")
                                           @PathVariable String fileId) {
        try {
            // 与NC业务系统相关逻辑开启，则需要调用删除NC台账接口
            boolean ncEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_NC_BUSINESS));
            String nccCheck = NcProperties.getDefaultPropertiesInfo().getString("nccCheck");
            if (ncEnabled && Boolean.valueOf(nccCheck)) {
                NcDeleteServiceDTO ncDeleteServiceDTO = new NcDeleteServiceDTO();
                ncDeleteServiceDTO.setFileIdList(Arrays.asList(fileId));
                DataImageFilesInfo imageFilesInfo = iDataImageFilesInfoService.selectById(fileId);
                List<DataCmInfo> cmInfoList = dataCmInfoService.findAllByBatchId(imageFilesInfo.getBatchId());
                if (CollectionUtil.isEmpty(cmInfoList)) {
                    return R.fail("转换失败，中间表数据不存在");
                }
                ncDeleteServiceDTO.setBusinessSerialNo(cmInfoList.get(0).getBusinessSerialNo());
                NcConfigFactory.instance().deleteNcInvoiceDataBusinessService(ncDeleteServiceDTO);
            }
            R<Void> voidR = iDataOcrService.ocrConvertToPicture(fileId);//还未写实现
            return voidR;
        } catch (Exception e) {
            log.error(ExceptionUtil.getExceptionMessage(e));
            return R.fail("转换失败");
        }

    }

    @ApiOperation("增值税发票手动查验")
    @PostMapping("checkInvoice")
    public R<String> checkInvoice(@RequestBody ManuallyCheckInvoiceDTO dto) throws Exception {
        return this.invoiceRelevantService.manuallyCheckInvoice(dto);//还未写实现
    }
}
