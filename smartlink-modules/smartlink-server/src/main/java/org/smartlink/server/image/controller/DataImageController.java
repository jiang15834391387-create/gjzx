package org.smartlink.server.image.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.anwen.mongo.conditions.query.LambdaQueryChainWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.core.utils.file.FileUtils;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.server.image.domain.bo.DeleteImageBo;
import org.smartlink.server.image.domain.bo.UpdateImageBo;
import org.smartlink.server.image.domain.bo.UploadImageBo;
import org.smartlink.server.image.momain.DataImage;
import org.smartlink.server.image.service.DataImageServer;
import org.smartlink.server.task.momain.DataTask;
import org.smartlink.server.task.service.DataTaskServer;
import org.smartlink.system.domain.vo.SysOssVo;
import org.smartlink.system.service.ISysOssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/server/image")
public class DataImageController extends BaseController {

    @Value("${kkfile.onlinePreviewUrl}")
    private String onlinePreviewUrl;

    private final DataImageServer dataImageServer;

    private final DataTaskServer dataTaskServer;

    private final ISysOssService iSysOssService;

    private final MongoTemplate mongoTemplate;


    @GetMapping("/getImageInfo")
    public DataTask getImageInfo(DataImage image) {
        return null;
    }

    /**
     * 上传文件，不在单据下
     *
     * @param uploadImageBo 文件bo
     * @return
     */
    @PostMapping("/uploadImage")
    public R<DataImage> uploadImage(@RequestBody UploadImageBo uploadImageBo) {
        byte[] decodedBytes = Base64.getDecoder().decode(uploadImageBo.getFileBase64());
        SysOssVo upload = iSysOssService.upload(decodedBytes, uploadImageBo.getFileName());
        DataImage dataImage = new DataImage();
        dataImage.setFileName(uploadImageBo.getFileName());
        dataImage.setFileType(upload.getFileSuffix());
        dataImage.setPreviewUrl(onlinePreviewUrl + URLEncoder.encode(Base64.getEncoder().encodeToString(upload.getUrl().getBytes())));
        dataImage.setSourceFileUrl(upload.getUrl());
        dataImage.setParentId(uploadImageBo.getParentId());
        dataImage.setOssId(upload.getOssId());
        dataImage.setCreateTime(DateUtil.now());
        if (dataImageServer.save(dataImage)) {
            return R.ok(dataImage);
        }
        return R.fail();
    }

    /**
     * 上传文件，不在单据上，用文件流
     *
     * @param file 文件
     */
    @PostMapping("/uploadImage2")
    public R<DataImage> uploadImage2(@RequestParam("file") MultipartFile file, String parentId) throws IOException {

        String fileName = file.getOriginalFilename();
        log.info("fileName:{}", fileName);
        SysOssVo upload = iSysOssService.upload(file.getBytes(), fileName);
        DataImage dataImage = new DataImage();
        dataImage.setFileName(fileName);
        dataImage.setFileType(upload.getFileSuffix());
        dataImage.setPreviewUrl(onlinePreviewUrl + URLEncoder.encode(Base64.getEncoder().encodeToString(upload.getUrl().getBytes())));
        dataImage.setSourceFileUrl(upload.getUrl());
        dataImage.setParentId(parentId);
        dataImage.setOssId(upload.getOssId());
        dataImage.setCreateTime(DateUtil.now());

        if (dataImageServer.save(dataImage)) {
            return R.ok(dataImage);
        }
        return R.fail();
    }


    /**
     * 上传文件，使用URL
     *
     * @param url              url
     * @param businessSerialNo 流水号
     * @return
     */
    @PostMapping("/uploadImageUrl")
    public R<DataImage> uploadImageUrl(@RequestParam("url") String url,
                                       @RequestParam("fileName") String fileName,
                                       @RequestParam("businessSerialNo") String businessSerialNo,
                                       @RequestParam("parentId") String parentId) {

        log.info("businessSerialNo:{}", businessSerialNo);
        // 先查询有没有
        DataTask task = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        log.info("task:{}", task);

        List<DataImage> imageList;
        if (task == null) {
            throw new ServiceException("单据不存在");
        }
        if (task.getImages() != null) {
            imageList = task.getImages();
        } else {
            imageList = new ArrayList<>(2);
        }

        HttpRequest httpRequest = HttpUtil.createGet(url);

        byte[] bytes;

        try (HttpResponse execute = httpRequest.execute()) {
            bytes = execute.bodyBytes();
        } catch (Exception e) {
            throw new ServiceException("请求航信文件失败！");
        }

        SysOssVo upload = iSysOssService.upload(bytes, fileName);
        DataImage dataImage = new DataImage();
        dataImage.setFileName(fileName);
        dataImage.setFileType(upload.getFileSuffix());
        dataImage.setPreviewUrl(onlinePreviewUrl + URLEncoder.encode(Base64.getEncoder().encodeToString(upload.getUrl().getBytes())));
        dataImage.setSourceFileUrl(upload.getUrl());
        dataImage.setParentId(parentId);
        dataImage.setOssId(upload.getOssId());
        dataImage.setCreateTime(DateUtil.now());
        dataImageServer.save(dataImage);
        Query query = new Query(Criteria.where("businessSerialNo").is(businessSerialNo));
        Update update = new Update().push("images", dataImage);
        mongoTemplate.updateFirst(query, update, DataTask.class);

        imageList.add(dataImage);
        task.setImages(imageList);

        dataTaskServer.updateById(task);
        return R.ok(dataImage);
    }

    /**
     * 单据下上传文件
     *
     * @param file             文件
     * @param businessSerialNo 流水号
     * @return
     */
    @PostMapping("/uploadImageFile")
    public R<DataImage> uploadImageFile(@RequestParam("file") MultipartFile file, String businessSerialNo, String parentId) {
        log.info("businessSerialNo:{}", businessSerialNo);
        // 先查询有没有
        DataTask task = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        log.info("task:{}", task);
        List<DataImage> imageList;
        if (task == null) {
            throw new ServiceException("单据不存在");
        }
        if (task.getImages() != null) {
            imageList = task.getImages();
        } else {
            imageList = new ArrayList<>(2);
        }

        SysOssVo upload = iSysOssService.upload(file);
        DataImage dataImage = new DataImage();
        dataImage.setFileName(file.getOriginalFilename());
        dataImage.setFileType(upload.getFileSuffix());
        dataImage.setPreviewUrl(onlinePreviewUrl + URLEncoder.encode(Base64.getEncoder().encodeToString(upload.getUrl().getBytes())));
        dataImage.setSourceFileUrl(upload.getUrl());
        dataImage.setParentId(parentId);
        dataImage.setOssId(upload.getOssId());
        dataImage.setCreateTime(DateUtil.now());
        dataImageServer.save(dataImage);
        Query query = new Query(Criteria.where("businessSerialNo").is(businessSerialNo));
        Update update = new Update().push("images", dataImage);
        mongoTemplate.updateFirst(query, update, DataTask.class);

        imageList.add(dataImage);
        task.setImages(imageList);

        dataTaskServer.updateById(task);
        return R.ok(dataImage);
    }

    /**
     * 下载文件
     *
     * @param fileId   文件ID
     * @param response 响应流
     */
    @GetMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response, @RequestParam("fileId") String fileId) throws IOException {
        final DataImage fileInfo = this.dataImageServer.getById(fileId);
        if (fileInfo == null) {
            throw new ServiceException("文件不存在");
        }
        this.iSysOssService.download(fileInfo.getOssId(), response);
    }

    /**
     * 根据单据主键打包下载多个文件
     * 测试url：http://47.97.23.199:28080/prod-api/server/image/taskPackageDownload?businessSerialNo=3
     *
     * @param businessSerialNo 单据流水号
     * @param response         响应流
     * @throws IOException IO异常
     */
    @GetMapping("/taskPackageDownload")
    public byte[] taskPackageDownload(@RequestParam("businessSerialNo") String businessSerialNo, HttpServletResponse response) throws IOException {
        DataTask task = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if (task == null) {
            throw new ServiceException("单据不存在");
        }
        final List<DataImage> images = task.getImages();
        if (CollUtil.isEmpty(images)) {
            throw new ServiceException("单据下没有文件");
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bos);
        int i = 0;
        for (DataImage image : images) {
            i++;
            byte[] bytes = this.iSysOssService.downloadByte(image.getOssId());
            zipOut.putNextEntry(new ZipEntry(i + "." + image.getFileName()));
            zipOut.write(bytes);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        FileUtils.setAttachmentResponseHeader(response, task.getBillNum() + ".zip");
        bos.flush();
        byte[] result = bos.toByteArray();
        bos.close();

        return result;

    }

    /**
     * 传递fileIds打包下载多个文件
     *
     * @param fileIds  文件ID数组
     * @param response 响应流
     * @throws IOException IO异常
     */
    @PostMapping("/multiplePackageDownload")
    public byte[] multiplePackageDownload(@RequestBody List<String> fileIds, HttpServletResponse response) throws IOException {

        if (CollUtil.isEmpty(fileIds)) {
            throw new ServiceException("文件ID数组不能为空！");
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bos);

        String fileName = "压缩文件";

        int i = 0;
        for (String fileId : fileIds) {
            if (StringUtils.isEmpty(fileId)) {
                throw new ServiceException("文件ID不能为空！");
            }
            DataImage dataImage = dataImageServer.getById(fileId);
            if (dataImage == null) {
                throw new ServiceException("文件不存在！");
            }
            i++;
            fileName = dataImage.getFileName();
            byte[] bytes = this.iSysOssService.downloadByte(dataImage.getOssId());

            zipOut.putNextEntry(new ZipEntry(i + "." + dataImage.getFileName()));
            zipOut.write(bytes);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        FileUtils.setAttachmentResponseHeader(response, fileName + "等" + fileIds.size() + "个文件.zip");
        bos.flush();
        byte[] result = bos.toByteArray();
        bos.close();
        return result;
    }


    @PostMapping("/editImage")
    public R<Void> editImage(@RequestBody DataImage image) {
        return null;
    }


    @PostMapping("/changeImageType")
    public R<Void> changeClassification(@RequestBody UpdateImageBo image) {
        //修改图片的类型
        dataImageServer.lambdaUpdate().set(DataImage::getParentId, image.getParentId()).eq(DataImage::getFileId, image.getFileId()).update();
        // 查询条件，找到具体的 dataTask 文档
        Query query = new Query(Criteria.where("businessSerialNo").is(image.getBusinessSerialNo()).and("images" + ".fileId").is(image.getFileId()));
        // 更新内容，设置 images 数组中 fileid=1 的元素的 type 属性的新值
        Update update = new Update().set("images.$.parentId", image.getParentId());
        // 执行更新
        mongoTemplate.updateFirst(query, update, "dataTask");

        return R.ok();
    }

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 成功失败
     */
    @GetMapping("/deleteImage")
    public R<Void> deleteImage(String fileId, @RequestParam(value = "businessSerialNo", required = false) String businessSerialNo) {
        if (StringUtils.isNotEmpty(businessSerialNo)){
            // 找到这个单据
            LambdaQueryChainWrapper<DataTask> queryChainWrapper = this.dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo);
            DataTask task = queryChainWrapper.one();
            if (task != null){
                List<DataImage> images = task.getImages();
                images.removeIf(image -> image.getFileId().equals(fileId));
                dataTaskServer.updateById(task);
            }
        }
        DataImage one = dataImageServer.lambdaQuery().eq(DataImage::getFileId, fileId).one();
        if (one != null) {
            iSysOssService.deleteWithValidById(one.getOssId(), false);
        }
        dataImageServer.lambdaUpdate().eq(DataImage::getFileId, fileId).remove();
        return R.ok();
    }

    /**
     * 批量删除文件(单据下)
     *
     * @param deleteImageBo 文件删除对象
     * @return 成功失败
     */
    @PostMapping("/deleteImages")
    public R<Void> deleteImages(@RequestBody DeleteImageBo deleteImageBo) {
        List<String> fileIds = Arrays.asList(deleteImageBo.getFileIds().split(","));
        for (String fileId : fileIds) {
            DataImage one = dataImageServer.lambdaQuery().eq(DataImage::getFileId, fileId).one();
            if (one != null) {
                iSysOssService.deleteWithValidById(one.getOssId(), false);
            }
            dataImageServer.lambdaUpdate().eq(DataImage::getFileId, fileId).remove();
        }
        DataTask one = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, deleteImageBo.getBusinessSerialNo()).one();
        List<DataImage> collect = one.getImages().stream().filter(e -> !fileIds.contains(e.getFileId())).collect(Collectors.toList());
        boolean update = dataTaskServer.lambdaUpdate().eq(DataTask::getBusinessSerialNo, deleteImageBo.getBusinessSerialNo()).set(DataTask::getImages, collect).update();
        if (update) {
            return R.ok();
        }
        return R.fail();
    }


}
