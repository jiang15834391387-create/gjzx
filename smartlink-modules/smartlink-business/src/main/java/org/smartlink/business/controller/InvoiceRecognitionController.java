//package org.smartlink.business.controller;
//
//import jakarta.validation.constraints.NotNull;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.smartlink.business.domain.vo.DataImageFilesInfoVo;
//import org.smartlink.common.core.domain.R;
//import org.smartlink.common.log.annotation.Log;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import org.smartlink.common.log.enums.BusinessType;
//
//import java.io.File;
//
///**
// * 二手车销售统一发票
// *
// * @author Lion Li
// * @date 2025-01-08
// */
//@Slf4j
//@Validated
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/business/InvoiceRecognition")
//public class InvoiceRecognitionController {
//
//
//    //@SaCheckPermission("service:scan:upload")
//    @Log(title = "扫描图片上传", businessType = BusinessType.INSERT)
//    @PostMapping("/upload")
//    public R<DataImageFilesInfoVo> upload(@Validated FileUploadDTO dto, @NotNull(message = "上传文件不能为空") @RequestParam("file") MultipartFile file) throws Exception {
//
//        try{
//            HardWareMessage hwm = hardWareMessageService.whetherToAuthorize(dto.getMacIp());
//            if(hwm.getResult() == 1){
//                return R.fail(hwm.getRcontent());
//            }
//        }catch (Exception e){
//            log.error("授权校验出现异常:" + ExceptionUtil.getExceptionMessage(e));
//            return R.fail(e.getLocalizedMessage());
//        }
//
//
//        if (StrUtil.isEmpty(dto.getSupplementaryScan())) {
//            dto.setSupplementaryScan("N");
//        }
//        DataCurrentTaskVo task = iDataCurrentTaskService.queryById(dto.getBusinessSerialNo());
//        DataCmInfoBo cmInfoBo = new DataCmInfoBo();
//        cmInfoBo.setBusinessSerialNo(dto.getBusinessSerialNo());
//        List<DataCmInfoVo> cmInfoVos = cmInfoService.queryList(cmInfoBo);
//        if (ObjectUtil.isNull(task) || CollectionUtil.isEmpty(cmInfoVos)) {
//            InitializationResponse initialize = currentTaskService.initialize(dto.getBusinessSerialNo());
//            dto.setBatchId(initialize.getBatchId());
//        } else {
//            DataCmInfoVo dataCmInfoVo = cmInfoVos.get(0);
//            dto.setBatchId(dataCmInfoVo.getBatchId());
//            dto.setBarCode(task.getBarCode());
//            dto.setTaskStatus(task.getTaskState());
//            dto.setBillType(task.getBillType());
//            dto.setOrgCode(task.getOrgCode());
//        }
//        //图片上传
//        DataImageFilesInfoVo dataImageFilesInfoVo = null;
//        DataImageFilesInfo dataImageFilesInfo = new DataImageFilesInfo();
//        BeanCopyUtils.copy(dto, dataImageFilesInfo);
//        dataImageFilesInfo.setFileId(IdUtil.simpleUUID());
//        dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
//        dataImageFilesInfo.setFileType(InvoiceConstants.IMAGE_OTHERS);
//        dataImageFilesInfo.setFileSize(String.valueOf(file.getSize()));
//        dataImageFilesInfo.setMessage("上传成功");
//        String suffix = FileUtils.getFileSuffix(dto.getFileName());
//        try {
//            //图片
//            if (ArrayUtil.containsIgnoreCase(MimeTypeUtils.IMAGE_EXTENSION, suffix)) {
//                dataImageFilesInfoVo = scanImageService.uploadImage(dto, dataImageFilesInfo, file);
//            } else {
//                //文件
//                dataImageFilesInfoVo = scanImageService.saveDocuments(dto, dataImageFilesInfo, file);
//            }
//        } catch (Exception e) {
//            log.error("上传接口出现异常:" + ExceptionUtil.getExceptionMessage(e));
//            dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
//            dataImageFilesInfo.setMessage("上传异常:" + e.getLocalizedMessage());
//            dataImageFilesInfo.insertOrUpdate();
//            DataImageTree dataImageTree = iDataImageTreeService.selectImageTreeByFileId(dataImageFilesInfo.getFileId());
//            if(ObjectUtil.isEmpty(dataImageTree)){
//                dataImageTree=new DataImageTree();
//            }
//            //保存树
//            boolean wechatUpload = StrUtil.equals(dto.getCip(), Constants.WECHAT_SYMBOL);
//            if (!wechatUpload && StrUtil.equals(dto.getScanType(), ScanTypeConstants.BATCH_SCAN)) {
//                dataImageTree.setBatchBusinessQuote(LoginHelper.getLoginUser().getLoginId());
//            }
//            dataImageTree.setProductId(dataImageFilesInfo.getFileId());
//            dataImageTree.setParentId(dataImageFilesInfo.getFileType());
//            //附件类型加判断
//            if (!ArrayUtil.containsIgnoreCase(InvoiceConstants.INVOICE_ClASS_TYPE.keySet().toArray(new String[0]), dataImageFilesInfo.getFileType())){
//                dataImageTree.setParentId(InvoiceConstants.IMAGE_OTHERS);
//            }
//            // 多票据文件类型重新赋值
//            if(StrUtil.equals(InvoiceConstants.INVOICE_MUCH_NCC,dataImageFilesInfo.getFileType())){
//                dataImageTree.setParentId(InvoiceConstants.INVOICE_MUCH_NCC);
//            }
//            dataImageTree.setProductName(dataImageFilesInfo.getFileName());
//            dataImageTree.setProductLevel(2L);
//            dataImageTree.setProductType("0");
//            dataImageTree.setStatus("0");
//            dataImageTree.setOrderNum(0L);
//            dataImageTree.setImageId(dataImageFilesInfo.getFileId());
//            dataImageTree.setBatchId(dataImageFilesInfo.getBatchId());
//            if ("Y".equalsIgnoreCase(dto.getSupplementaryScan())){
//                dataImageTree.setParentId(InvoiceConstants.AFTER_FILE);
//                dataImageTree.setIsMove("1");
//            }
//            iDataImageTreeService.saveOrUpdate(dataImageTree);
//            return R.fail(e.getLocalizedMessage());
//        }
//        return R.ok(dataImageFilesInfoVo);
//    }
//
//}
