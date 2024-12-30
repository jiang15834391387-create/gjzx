package org.smartlink.server.nc.controller.scan;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.log.enums.BusinessType;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.server.nc.annotation.Log;
import org.smartlink.server.nc.constant.Constants;
import org.smartlink.server.nc.constant.FileStatusConstants;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.constant.ScanTypeConstants;
import org.smartlink.server.nc.domain.DataCmInfoBo;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.hardwaremessage.HardWareMessage;
import org.smartlink.server.nc.domain.imagefilesinfo.DataImageFilesInfoVo;
import org.smartlink.server.nc.domain.imagefilesinfo.DataImageTree;
import org.smartlink.server.nc.domain.scan.dto.FileUploadDTO;
import org.smartlink.server.nc.domain.scan.response.InitializationResponse;
import org.smartlink.server.nc.domain.vo.DataCmInfoVo;
import org.smartlink.server.nc.domain.vo.DataCurrentTaskVo;
import org.smartlink.server.nc.helper.LoginHelper;
import org.smartlink.server.nc.service.hardwaremessage.HardWareMessageService;
import org.smartlink.server.nc.service.nc.*;
import org.smartlink.server.nc.service.scan.ScanImageService;
import org.smartlink.server.nc.utils.BeanCopyUtils;
import org.smartlink.server.nc.utils.ExceptionUtil;
import org.smartlink.server.nc.utils.file.FileUtils;
import org.smartlink.server.nc.utils.file.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author L
 */

@Validated
@Api(value = "扫描页面相关", tags = {"扫描页面相关管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/service/scanImage")
@Slf4j
public class ScanImageController extends BaseController {

    private final ScanImageService scanImageService;
    private final IDataCurrentTaskService dataCurrentTaskService;
    private final CurrentTaskService currentTaskService;
    private final IDataCmInfoService cmInfoService;

    private final IDataImageFilesInfoService imageFilesInfoService;
    private final IDataImageTreeService dataImageTreeService;
    private final HardWareMessageService hardWareMessageService;

    /**
     * 扫描图片上传
     */
    @ApiOperation("扫描图片上传")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件", paramType = "query", dataTypeClass = File.class, required = true)})
    //@SaCheckPermission("service:scan:upload")
    @Log(title = "扫描图片上传", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public R<DataImageFilesInfoVo> upload(@Validated FileUploadDTO dto, @NotNull(message = "上传文件不能为空") @RequestParam("file") MultipartFile file) throws Exception {

        try{
            HardWareMessage hwm = hardWareMessageService.whetherToAuthorize(dto.getMacIp());
            if(hwm.getResult() == 1){
                return R.fail(hwm.getRcontent());
            }
        }catch (Exception e){
            log.error("授权校验出现异常:" + ExceptionUtil.getExceptionMessage(e));
            return R.fail(e.getLocalizedMessage());
        }

        if (StrUtil.isEmpty(dto.getSupplementaryScan())) {
            dto.setSupplementaryScan("N");
        }
        DataCurrentTaskVo task = dataCurrentTaskService.queryById(dto.getBusinessSerialNo());
        DataCmInfoBo cmInfoBo = new DataCmInfoBo();
        cmInfoBo.setBusinessSerialNo(dto.getBusinessSerialNo());
        List<DataCmInfoVo> cmInfoVos = cmInfoService.queryList(cmInfoBo);
        if (ObjectUtil.isNull(task) || CollectionUtil.isEmpty(cmInfoVos)) {
            InitializationResponse initialize = currentTaskService.initialize(dto.getBusinessSerialNo());
            dto.setBatchId(initialize.getBatchId());
        } else {
            DataCmInfoVo dataCmInfoVo = cmInfoVos.get(0);
            dto.setBatchId(dataCmInfoVo.getBatchId());
            dto.setBarCode(task.getBarCode());
            dto.setTaskStatus(task.getTaskState());
            dto.setBillType(task.getBillType());
            dto.setOrgCode(task.getOrgCode());
        }
        //图片上传
        DataImageFilesInfoVo dataImageFilesInfoVo = null;
        DataImageFilesInfo dataImageFilesInfo = new DataImageFilesInfo();
        BeanCopyUtils.copy(dto, dataImageFilesInfo);
        dataImageFilesInfo.setIsStaging(ObjectUtil.isNotEmpty(task) && StrUtil.equals("0",task.getBillSaved())?"0":"1");
        dataImageFilesInfo.setFileId(IdUtil.simpleUUID());
        dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
        dataImageFilesInfo.setFileType(InvoiceConstants.IMAGE_OTHERS);
        dataImageFilesInfo.setFileSize(String.valueOf(file.getSize()));
        dataImageFilesInfo.setMessage("上传成功");
        String suffix = FileUtils.getFileSuffix(dto.getFileName());
        if ("xml".equalsIgnoreCase(suffix)) {
            suffix = "jpg";
        }
        try {
            //图片
            if (ArrayUtil.containsIgnoreCase(MimeTypeUtils.IMAGE_EXTENSION, suffix)) {
                dataImageFilesInfoVo = scanImageService.uploadImage(dto, dataImageFilesInfo, file);
            } else {
                //文件
                dataImageFilesInfoVo = scanImageService.saveDocuments(dto, dataImageFilesInfo, file);
            }
        } catch (Exception e) {
            log.error("上传接口出现异常:" + ExceptionUtil.getExceptionMessage(e));
            dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
            dataImageFilesInfo.setMessage("上传异常:" + e.getLocalizedMessage());
            dataImageFilesInfo.insertOrUpdate();
            DataImageTree dataImageTree = dataImageTreeService.selectImageTreeByFileId(dataImageFilesInfo.getFileId());
            if(ObjectUtil.isEmpty(dataImageTree)){
                dataImageTree=new DataImageTree();
            }
            //保存树
            boolean wechatUpload = StrUtil.equals(dto.getCip(), Constants.WECHAT_SYMBOL);
            if (!wechatUpload && StrUtil.equals(dto.getScanType(), ScanTypeConstants.BATCH_SCAN)) {
                dataImageTree.setBatchBusinessQuote(LoginHelper.getLoginUser().getLoginId());
            }
            dataImageTree.setProductId(dataImageFilesInfo.getFileId());
            dataImageTree.setParentId(dataImageFilesInfo.getFileType());
            //附件类型加判断
            if (!ArrayUtil.containsIgnoreCase(InvoiceConstants.INVOICE_ClASS_TYPE.keySet().toArray(new String[0]), dataImageFilesInfo.getFileType())){
                dataImageTree.setParentId(InvoiceConstants.IMAGE_OTHERS);
            }
            // 多票据文件类型重新赋值
            if(StrUtil.equals(InvoiceConstants.INVOICE_MUCH_NCC,dataImageFilesInfo.getFileType())){
                dataImageTree.setParentId(InvoiceConstants.INVOICE_MUCH_NCC);
            }
            dataImageTree.setProductName(dataImageFilesInfo.getFileName());
            dataImageTree.setProductLevel(2L);
            dataImageTree.setProductType("0");
            dataImageTree.setStatus("0");
            dataImageTree.setOrderNum(0L);
            dataImageTree.setImageId(dataImageFilesInfo.getFileId());
            dataImageTree.setBatchId(dataImageFilesInfo.getBatchId());
            if ("Y".equalsIgnoreCase(dto.getSupplementaryScan())){
                dataImageTree.setParentId(InvoiceConstants.AFTER_FILE);
                dataImageTree.setIsMove("1");
            }
            dataImageTreeService.saveOrUpdate(dataImageTree);
            return R.fail(e.getLocalizedMessage());
        }finally {
            scanImageService.pushFileDataToBusinessSystem(dataImageFilesInfo, dto.getBusinessSerialNo());
        }
        return R.ok(dataImageFilesInfoVo);
    }
}
