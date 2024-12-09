package org.smartlink.server.nc.service.youbaozhang.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.*;
import org.smartlink.server.nc.domain.DataCmInfo;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.imagefilesinfo.DataImageTree;
import org.smartlink.server.nc.domain.invoice.DataOcrInfo;
import org.smartlink.server.nc.domain.ybz.request.YbzDeleteImageRequest;
import org.smartlink.server.nc.domain.ybz.request.YbzGetImageListRequest;
import org.smartlink.server.nc.domain.ybz.request.YbzUploadImageRequest;
import org.smartlink.server.nc.domain.ybz.response.YbzQueryFileInfoDTO;
import org.smartlink.server.nc.domain.ybz.response.YbzQueryItemsDTO;
import org.smartlink.server.nc.domain.ybz.response.YbzQueryOcrInfoDTO;
import org.smartlink.server.nc.domain.ybz.response.YbzReturnDTO;
import org.smartlink.server.nc.oss.factory.OssFactory;
import org.smartlink.server.nc.service.document.DocumentFactory;
import org.smartlink.server.nc.service.nc.*;
import org.smartlink.server.nc.service.youbaozhang.ImageYbzService;
import org.smartlink.server.nc.utils.BatchIdUtils;
import org.smartlink.server.nc.utils.file.FileUtils;
import org.smartlink.server.nc.utils.file.ImageWatermarkUtil;
import org.smartlink.server.nc.utils.file.MimeTypeUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author L
 * @title
 * @description
 * @date
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageYbzServiceImpl implements ImageYbzService {

    private final IDataCmInfoService dataCmInfoService;
    private final IDataImageFilesInfoService dataImageFilesInfoService;
    private final IDataOcrInfoService ocrInfoService;
    private final IDataImageTreeService imageTreeService;
    private final IDataCurrentTaskService dataCurrentTaskService;

    @Override
    public YbzReturnDTO imageList(YbzGetImageListRequest imageInfo) {
        log.info("进入获取影像列表接口,友报账请求参数：" + JSON.toJSONString(imageInfo));
        //返回信息
        List<YbzQueryItemsDTO> responseItemList = new ArrayList<>();
        for (String barCode : imageInfo.getBarCodes()) {
            List<YbzQueryFileInfoDTO> ybzQueryFileInfoDTOS = new ArrayList<>(16);
            YbzQueryItemsDTO itemsDTO = new YbzQueryItemsDTO();
            String barCodes = barCode.replaceAll(" +", "");
            //查询中间表
            List<DataCmInfo> dataCmInfos = dataCmInfoService.selectDataCmInfoByBusinessSerialNo(barCodes);
            if (CollUtil.isEmpty(dataCmInfos)) {
                //查不出来可能是 费用管理 模块的查询
                dataCmInfos = dataCmInfoService.selectDataCmInfoByBusinessSerialNo(barCodes.substring(barCodes.length() - 20));
            }

            //查询图片表
            List<DataImageFilesInfo> dataImageFilesInfos;
            if (CollUtil.isNotEmpty(dataCmInfos)) {
                String batchId = dataCmInfos.get(0).getBatchId();
                dataImageFilesInfos = dataImageFilesInfoService.selectByBatchId(batchId);
            } else {
                dataImageFilesInfos = dataImageFilesInfoService.fuzzySelectAllByBarCode(barCode);
            }
            for (DataImageFilesInfo dataImageFilesInfo : dataImageFilesInfos) {
                //图片信息
                YbzQueryFileInfoDTO imagess = new YbzQueryFileInfoDTO();
                imagess.setFilesize(dataImageFilesInfo.getFileSize());
                imagess.setImgkey(dataImageFilesInfo.getFileId());
                imagess.setName(dataImageFilesInfo.getFileName());
                //如果是本地存储
                if (dataImageFilesInfo.getIurl().contains(Constants.RESOURCE_PREFIX)) {
                    imagess.setPointimage(FileUtils.getFileUrlResource(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_IP) + ":" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_PORT) + dataImageFilesInfo.getIurl(), dataImageFilesInfo.getFileId()));
                    imagess.setSmallimage(FileUtils.getFileUrlResource(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_IP) + ":" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_PORT) + dataImageFilesInfo.getIurl(), dataImageFilesInfo.getFileId()));
                } else {
                    imagess.setPointimage(dataImageFilesInfo.getIurl());
                    imagess.setSmallimage(dataImageFilesInfo.getSurl());
                }
                DataOcrInfo byFileId = ocrInfoService.getByFileId(dataImageFilesInfo.getFileId());
                //ocr信息
                if (ObjectUtil.isNotEmpty(byFileId)) {
                    YbzQueryOcrInfoDTO ocrInfo = new YbzQueryOcrInfoDTO();
                    if ("0".equals(byFileId.getCheckInvoice())) {
                        //是否已经发票验真,1-验证通过;2-验证不通过或未验证;
                        ocrInfo.setIschecked("2");
                    } else {
                        //是否已经发票验真,1-验证通过;2-验证不通过或未验证;
                        ocrInfo.setIschecked("1");
                    }
                    //购方税号
                    ocrInfo.setGfsh(byFileId.getBuyerNo());
                    //销方税号
                    ocrInfo.setXfsh(byFileId.getSellerNo());
                    //发票代码
                    ocrInfo.setFpdm(byFileId.getInvoiceCode());
                    //发票号码
                    ocrInfo.setFphm(byFileId.getInvoiceNumber());
                    //金额
                    ocrInfo.setJe(String.valueOf(byFileId.getSumAmount()));
                    //税额
                    ocrInfo.setSe(String.valueOf(byFileId.getSumTax()));
                    //开票日期
                    ocrInfo.setKprq(DateUtil.format(byFileId.getInvoiceDate(), "yyyy-MM-dd"));
                    //发票校验码
                    ocrInfo.setFpjym(byFileId.getCheckCode());
                    //开票金额
                    ocrInfo.setTotalamount(String.valueOf(byFileId.getSumAmount()));
                    //发票类型
                    ocrInfo.setInvtype("01");
                    imagess.setInfo(ocrInfo);
                }
                ybzQueryFileInfoDTOS.add(imagess);
            }
            itemsDTO.setImages(ybzQueryFileInfoDTOS);
            itemsDTO.setBarcode(barCode);
            responseItemList.add(itemsDTO);
        }
        log.info("返回给移动端数据报文：" + JSONObject.toJSONString(responseItemList));
        return new YbzReturnDTO(true, "影像数据获取成功", responseItemList);
    }

    @Override
    public YbzReturnDTO imageUpload(YbzUploadImageRequest imageInfo) throws Exception {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("content");
        log.info("友报账上传报文" + JSONObject.toJSONString(imageInfo, filter));
        //log.info("友报账上传报文"+JSONObject.toJSONString(imageInfo));
        //拼接图片信息
        DataImageFilesInfo imageFilesInfo = new DataImageFilesInfo();
        //获取batchid
        List<DataImageFilesInfo> dataImageFilesInfos = dataImageFilesInfoService.fuzzySelectAllByBarCode(imageInfo.getBarcode());
        String batchId = "";
        if (CollUtil.isNotEmpty(dataImageFilesInfos)) {
            batchId = dataImageFilesInfos.get(0).getBatchId();
        } else {
            batchId = dataCmInfoService.saveCminfo(imageInfo.getBarcode()).getBatchId();
        }
        //文件内容
        byte[] decodeByte = Base64.decode(imageInfo.getContent());
        imageFilesInfo.setFileName(imageInfo.getFilename());
        imageFilesInfo.setCip(Constants.YBZ);
        imageFilesInfo.setFileId(imageInfo.getImgkey());
        imageFilesInfo.setBarCode(imageInfo.getBarcode());
        imageFilesInfo.setBatchId(batchId);
        imageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
        imageFilesInfo.setSortValue(0);
        imageFilesInfo.setFileSize(String.valueOf(decodeByte.length));
        //树信息
        String fileSuffix = FileUtils.getFileSuffix(imageFilesInfo.getFileName());
        if (ArrayUtil.containsIgnoreCase(MimeTypeUtils.IMAGE_EXTENSION, fileSuffix)) {
            UploadResult uploadDocument = OssFactory.instance().upload(decodeByte, BatchIdUtils.getBatchIdPath(batchId, imageFilesInfo.getFileId()), fileSuffix);
            imageFilesInfo.setUrl(uploadDocument.getUrl());
            imageFilesInfo.setIurl(uploadDocument.getUrl());
            imageFilesInfo.setSurl(uploadDocument.getUrl());
            imageFilesInfo.setFileType(InvoiceConstants.IMAGE_OTHERS);
        } else {
            UploadResult uploadDocument = OssFactory.instance().upload(decodeByte, BatchIdUtils.getBatchIdPath(batchId, imageFilesInfo.getFileId()), fileSuffix);
            imageFilesInfo.setUrl(uploadDocument.getUrl());
            imageFilesInfo.setIurl(uploadDocument.getUrl());
            byte[] bytes = DocumentFactory.instance(fileSuffix).documentToPdf(decodeByte);
            UploadResult upload = OssFactory.instance().upload(bytes, BatchIdUtils.getBatchIdPath(batchId, imageFilesInfo.getFileId()), fileSuffix);
            imageFilesInfo.setPurl(upload.getUrl());
            DocumentFactory.instance(fileSuffix).setFileType(imageFilesInfo);
        }
        //存储发票信息
        YbzUploadImageRequest.InfoDTO info = imageInfo.getInfo();
        if (ObjectUtil.isNotNull(info) && StrUtil.isNotBlank(info.getFplx()) && info.getFplx().equalsIgnoreCase("invoice")) {
            DataOcrInfo ocrInfo = new DataOcrInfo();
            if (ArrayUtil.containsIgnoreCase(MimeTypeUtils.IMAGE_EXTENSION, fileSuffix)) {
                imageFilesInfo.setFileType(InvoiceConstants.TAX_INVOICE);
                //设置文件状态与水印
                if ("1".equalsIgnoreCase(info.getCanAddWatermark())) {
                    //查验成功
                    imageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
                    ByteArrayOutputStream outputStream = ImageWatermarkUtil.ImageByText("查验成功", decodeByte, 45);
                    UploadResult uploadDocument = OssFactory.instance().upload(outputStream.toByteArray(), BatchIdUtils.getBatchIdPath(batchId, imageFilesInfo.getFileId()), fileSuffix);
                    imageFilesInfo.setIurl(uploadDocument.getUrl());
                } else {
                    //失败水印
                    ByteArrayOutputStream outputStream = ImageWatermarkUtil.ImageByText("查验失败", decodeByte, 45);
                    UploadResult uploadDocument = OssFactory.instance().upload(outputStream.toByteArray(), BatchIdUtils.getBatchIdPath(batchId, imageFilesInfo.getFileId()), fileSuffix);
                    imageFilesInfo.setIurl(uploadDocument.getUrl());
                }
            } else {
                byte[] bytes = DocumentFactory.instance(fileSuffix).documentToImgOne(decodeByte, 90);
                UploadResult upload = OssFactory.instance().upload(bytes, BatchIdUtils.getBatchIdPath(batchId, "simg_" + imageFilesInfo.getFileId()), fileSuffix);
                imageFilesInfo.setSurl(upload.getUrl());
                UploadResult uploadLimg = OssFactory.instance().upload(bytes, BatchIdUtils.getBatchIdPath(batchId, "img_" + imageFilesInfo.getFileId()), fileSuffix);
                imageFilesInfo.setIurl(uploadLimg.getUrl());
                imageFilesInfo.setFileType(InvoiceConstants.ELECTRONIC_INVOICE);
            }
            //发票代码
            ocrInfo.setInvoiceCode(info.getFpdm());
            //发票号码
            ocrInfo.setInvoiceNumber(info.getFphm());
            //金额
            ocrInfo.setSumAmount(Convert.toBigDecimal(info.getJe()));
            //税额
            ocrInfo.setSumTax(Convert.toBigDecimal(info.getSe()));
            //开票日期
            ocrInfo.setInvoiceDate(DateUtil.parseDate(info.getKprq()));

            ocrInfo.setTotalLowercase(Convert.toBigDecimal(info.getTotalamount()));
            //发票校验码
            ocrInfo.setCheckCode(info.getFpjym());
            //购方税号
            ocrInfo.setBuyerNo(info.getGfsh());
            //销方税号
            ocrInfo.setSellerNo(info.getXfsh());
            //购方地址
            ocrInfo.setBuyerAddress(info.getGfdz());
            //购方银行账号
            ocrInfo.setBuyerAccount(info.getGfyhzh());
            //购方名称
            ocrInfo.setBuyerName(info.getGfmc());
            //销货方纳税账户
            ocrInfo.setSellerAccount(info.getXfyhzh());
            //销货方纳税地址
            ocrInfo.setSellerAddress(info.getXfdz());
            //销货方纳税名称
            ocrInfo.setSellerName(info.getXfmc());
            //查验成功
            if ("1".equals(info.getIschecked())) {
                ocrInfo.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
            } else {
                ocrInfo.setCheckInvoice(CheckConstant.ERROE_CHECK);
            }
            //设置文件id （文件和ocr信息的关联关系）
            ocrInfo.setFileId(imageInfo.getImgkey());
            ocrInfoService.insert(ocrInfo);
        }
        dataImageFilesInfoService.insert(imageFilesInfo);
        // 判断该条数据是否成功绑定到了单据下边，如果绑定了则需要生成任务表数据
        List<DataCmInfo> cmInfoList = dataCmInfoService.findAllByBatchId(imageFilesInfo.getBatchId());
        if(CollUtil.isNotEmpty(cmInfoList)){
            DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(cmInfoList.get(0).getBusinessSerialNo());
            if(ObjectUtil.isNotEmpty(dataCurrentTask)){
                //存树表
                DataImageTree dataImageTree = imageTreeService.selectImageTreeByFileId(imageFilesInfo.getFileId());
                if (ObjectUtil.isNotNull(dataImageTree)) {
                    dataImageTree.setProductName(imageFilesInfo.getFileName());
                    dataImageTree.setProductLevel(2L);
                    dataImageTree.setProductType("0");
                    dataImageTree.setStatus("0");
                    dataImageTree.setOrderNum(0L);
                    dataImageTree.setImageId(imageFilesInfo.getFileId());
                    dataImageTree.setBatchId(imageFilesInfo.getBatchId());
                    dataImageTree.setCreateTime(new Date());
                    dataImageTree.setUpdateTime(new Date());
                    dataImageTree.updateById();
                } else {
                    dataImageTree = new DataImageTree();
                    if (ArrayUtil.containsIgnoreCase(MimeTypeUtils.IMAGE_EXTENSION, FileUtils.getFileSuffix(imageFilesInfo.getFileName()))) {
                        dataImageTree.setParentId(InvoiceConstants.YOUBAOZHANG_IMG);
                    } else {
                        dataImageTree.setParentId(InvoiceConstants.YOUBAOZHANG_DOC);
                    }
                    dataImageTree.setProductId(imageFilesInfo.getFileId());
                    dataImageTree.setProductName(imageFilesInfo.getFileName());
                    dataImageTree.setProductLevel(2L);
                    dataImageTree.setProductType("0");
                    dataImageTree.setStatus("0");
                    dataImageTree.setOrderNum(0L);
                    dataImageTree.setImageId(imageFilesInfo.getFileId());
                    dataImageTree.setBatchId(imageFilesInfo.getBatchId());
                    dataImageTree.setCreateTime(new Date());
                    dataImageTree.setUpdateTime(new Date());
                    imageTreeService.saveOrUpdate(dataImageTree);
                }
            }
        }
        return new YbzReturnDTO(true, "保存成功", imageFilesInfo);
    }

    @Override
    public YbzReturnDTO updateImageState(YbzDeleteImageRequest imageInfo) {
        String imgkey = imageInfo.getImageKey();
        if (StrUtil.isBlank(imgkey)) {
            log.info("友费控删除的barcode" + imageInfo.getBarcode());
            //删除单据下的所有文件
            List<DataImageFilesInfo> dataImageFilesInfos = dataImageFilesInfoService.fuzzySelectAllByBarCode(imageInfo.getBarcode());
            log.info("友费控删除的barcode查询出来的数据" + JSONObject.toJSONString(dataImageFilesInfos));
            for (DataImageFilesInfo dataImageFilesInfo : dataImageFilesInfos) {
                log.info("进入了删除的数据" + JSONObject.toJSONString(dataImageFilesInfo));
                dataImageFilesInfoService.deleteById(dataImageFilesInfo.getFileId());
                imageTreeService.deleteImageTreeByFileId(dataImageFilesInfo.getFileId());
            }
        } else {
            //删除指定文件
            log.info("删除指定文件" + imgkey);
            dataImageFilesInfoService.deleteById(imgkey);
            imageTreeService.deleteImageTreeByFileId(imgkey);
        }
        return new YbzReturnDTO(true, "删除成功", null);
    }
}
