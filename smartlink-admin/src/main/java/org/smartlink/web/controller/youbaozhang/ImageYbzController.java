package org.smartlink.web.controller.youbaozhang;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.web.domain.ybz.request.YbzDeleteImageRequest;
import org.smartlink.web.domain.ybz.request.YbzGetImageListRequest;
import org.smartlink.web.domain.ybz.request.YbzUploadImageRequest;
import org.smartlink.web.domain.ybz.response.YbzReturnDTO;
import org.smartlink.web.service.youbaozhang.ImageYbzService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author L
 * @title 友报账处理
 * @description 友报账处理
 * @date
 */

@Validated
@Api(value = "友报账接口，影像侧提供", tags = {"友报账接口"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/appApi")
@Slf4j
public class ImageYbzController {

    private final ImageYbzService imageYbzService;

    @RequestMapping(value = {"/image"}, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public YbzReturnDTO image(HttpServletRequest request, @RequestBody String imageInfo) throws Exception {
        log.info("进入友报账处理影像接口！获得的keyClientCode是："+request.getHeader("clientcode"));

        YbzReturnDTO ybzReturnDTO = checkHeader(request);
        if (ObjectUtil.isNotEmpty(ybzReturnDTO)){
            return ybzReturnDTO;
        }
        String opType = request.getHeader("optype");
        //1-获取影像列表接口;
        String imageList = "1";
        if (imageList.equals(opType)) {
            log.info("友报账进入获取影像接口！");
            return imageYbzService.imageList(JSONObject.parseObject(imageInfo, YbzGetImageListRequest.class));
        }
        //2-影像上传;
        String imageUpload = "2";
        if (imageUpload.equals(opType)) {
            log.info("友报账进入影像上传接口！");
            return imageYbzService.imageUpload(JSONObject.parseObject(imageInfo, YbzUploadImageRequest.class));
        }
        //3-改变影像状态(包括删除);
        String updateImageState = "3";
        if (updateImageState.equals(opType)) {
            log.info("友报账进入影像删除/修改接口！参数："+JSONObject.parseObject(imageInfo));
            return imageYbzService.updateImageState(JSONObject.parseObject(imageInfo, YbzDeleteImageRequest.class));
        }
        return new YbzReturnDTO(false,"","缺少必要参数！");
    }

    public YbzReturnDTO checkHeader(HttpServletRequest request){
        String clientCode = "ybzNew";
        String serviceCode = "ImageCenter";
        if (!clientCode.equals(request.getHeader("clientCode"))
                ||!serviceCode.equals(request.getHeader("serviceCode"))
                ||StrUtil.isBlank(request.getHeader("time"))
                ||StrUtil.isBlank(request.getHeader("ticket"))
                ||StrUtil.isBlank(request.getHeader("optype"))
        ){
            return new YbzReturnDTO(false,"缺少必要参数！","");
        }
        return null;
    }
}
