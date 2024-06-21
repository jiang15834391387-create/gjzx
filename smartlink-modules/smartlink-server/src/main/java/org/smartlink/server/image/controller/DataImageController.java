package org.smartlink.server.image.controller;


import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.web.core.BaseController;
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

import java.net.URLEncoder;
import java.util.Base64;

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

    @PostMapping("/uploadImage")
    public R<DataImage> uploadImage(@RequestBody UploadImageBo uploadImageBo) {
        byte[] decodedBytes = Base64.getDecoder().decode(uploadImageBo.getFileBase64());
        SysOssVo upload = iSysOssService.upload(decodedBytes,uploadImageBo.getFileName());
        DataImage dataImage = new DataImage();
        dataImage.setFileName(uploadImageBo.getFileName());
        dataImage.setFileType(upload.getFileSuffix());
        dataImage.setPreviewUrl(onlinePreviewUrl+URLEncoder.encode( Base64.getEncoder().encodeToString(upload.getUrl().getBytes())));
        dataImage.setSourceFileUrl(upload.getUrl());
        dataImage.setParentId("fj");
        dataImage.setOssId(upload.getOssId());
        if (dataImageServer.save(dataImage)) {
            return R.ok(dataImage);
        }
        return R.fail();
    }


    @PostMapping("/editImage")
    public R<Void> editImage(@RequestBody DataImage image) {
        return null;
    }


    @GetMapping("/deleteImage")
    public R<Void> deleteImage(String fileId) {
        DataImage one = dataImageServer.lambdaQuery().eq(DataImage::getFileId, fileId).one();
        if(one!=null){
            iSysOssService.deleteWithValidById(one.getOssId(),false);
        }
        dataImageServer.lambdaUpdate().eq(DataImage::getFileId, fileId).remove();
        Query query = new Query(Criteria.where("images").elemMatch(Criteria.where("fileId").is(fileId)));
        Update update = new Update().pull("images", Query.query(Criteria.where("fileId").is(fileId)));
        mongoTemplate.updateMulti(query, update, "dataTask");
        return R.ok();
    }



}
