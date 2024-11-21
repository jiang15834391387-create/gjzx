package org.smartlink.server.task.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.anwen.mongo.model.PageParam;
import com.anwen.mongo.model.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.server.image.domain.bo.ImageTreeBo;
import org.smartlink.server.image.momain.DataImage;
import org.smartlink.server.image.service.DataImageServer;
import org.smartlink.server.image.service.impl.DataImageService;
import org.smartlink.server.nodeType.domain.DataNodeType;
import org.smartlink.server.nodeType.mapper.DataNodeTypeMapper;
import org.smartlink.server.task.domain.bo.TaskAndImages;
import org.smartlink.server.task.domain.vo.DataTaskVo;
import org.smartlink.server.task.momain.DataTask;
import org.smartlink.server.task.service.DataTaskServer;
import org.smartlink.server.task.util.*;
import org.smartlink.system.service.ISysOssService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/server/task")
public class DataTaskController extends BaseController {


    private final ISysOssService iSysOssService;
    private final DataTaskServer dataTaskServer;

    private final DataImageServer dataImageServer;

    private final MongoTemplate mongoTemplate;

    private final DataNodeTypeMapper dataNodeTypeMapper;

    private final DataImageService dataImageService;


    @PostMapping("/getTaskList")
    public PageResult<DataTask> getTaskList(@RequestBody DataTask dataTask, @RequestBody PageParam pageParam) {
        return dataTaskServer.lambdaQuery().projectNone(DataTask::getImages)
            .like(StrUtil.isNotEmpty(dataTask.getBusinessSerialNo()), DataTask::getBusinessSerialNo, dataTask.getBusinessSerialNo())
            .like(StrUtil.isNotEmpty(dataTask.getBillNum()), DataTask::getBillNum, dataTask.getBillNum())
            .page(pageParam);
    }

    @GetMapping("/getTaskInfo")
    public R<DataTaskVo> getTaskInfo(String businessSerialNo) {
        DataTask one = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if (one == null) {
            return R.fail(businessSerialNo + "单据不存在");
        }
        //树节点对象
        List<DataNodeType> dataNodeTypes = dataNodeTypeMapper.selectList();
        //树节点集合
        List<ImageTreeBo> imageTreeList = new ArrayList<>();
        //单据下影像集合
        List<DataImage> images = one.getImages() == null ? new ArrayList<>() : one.getImages();
        //节点ID
        List<String> typeIds = dataNodeTypes.stream().map(DataNodeType::getId).toList();
        //其他节点ID
        Optional<DataNodeType> other = dataNodeTypes.stream().filter(e -> StrUtil.equals(e.getNodeName(), "其他")).findFirst();
        //文件信息转换为树对象
        for (DataImage image : images) {
            //将其他ID都当成其他
            if (!typeIds.contains(image.getParentId())) {
                image.setParentId(other.get().getId());
            }
            ImageTreeBo nodeTypeImage = new ImageTreeBo();
            BeanUtil.copyProperties(image, nodeTypeImage);
            nodeTypeImage.setType("image");
            imageTreeList.add(nodeTypeImage);
        }
        //节点信息转换为树对象
        for (DataNodeType dataNodeType : dataNodeTypes) {
            ImageTreeBo nodeTypeImage = new ImageTreeBo();
            nodeTypeImage.setFileId(dataNodeType.getId());
            nodeTypeImage.setParentId(dataNodeType.getParentId());
            nodeTypeImage.setFileName(dataNodeType.getNodeName());
            if (StrUtil.equals(dataNodeType.getId(), "0")) {
                //根节点数量
                nodeTypeImage.setTotal((long) images.size());
            } else {
                //当前节点数量
                long count = images.stream().filter(e -> StrUtil.equals(e.getParentId(), dataNodeType.getId())).count();
                nodeTypeImage.setTotal(count);
            }
            nodeTypeImage.setType("node");
            if (nodeTypeImage.getTotal() == 0) {
                continue;
            }
            imageTreeList.add(nodeTypeImage);
        }
        List<Tree<String>> build = TreeUtil.build(imageTreeList, "-1", (image, tree) -> {
            tree.setId(image.getFileId());
            tree.setParentId(image.getParentId());
            tree.setName(image.getFileName());
            tree.setWeight(image.getSort());
            if (StringUtils.isNotBlank(image.getCreateTime())) {
                String createTime = image.getCreateTime();
                image.setCreateTime(createTime.substring(0, createTime.length() - 2));
            }
            tree.putExtra("createTime", image.getCreateTime());
            tree.putExtra("sourceFileUrl", image.getSourceFileUrl());
            tree.putExtra("previewUrl", image.getPreviewUrl());
            tree.putExtra("fileName", image.getFileName());
            tree.putExtra("ossId", image.getOssId());
            tree.putExtra("total", image.getTotal());
            tree.putExtra("type", image.getType());
            tree.putExtra("name", image.getFileName());
        });

        DataTaskVo dataTaskVo = new DataTaskVo();
        BeanUtil.copyProperties(one, dataTaskVo);
        dataTaskVo.setImageTree(build);
        return R.ok(dataTaskVo);

    }


    @PostMapping("/addTask")
    public R<Void> addTask(@RequestBody DataTask task) {
        DataTask one = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, task.getBusinessSerialNo()).one();
        Boolean save;
        if (one == null) {
            save = dataTaskServer.save(task);
        } else {
            save = dataTaskServer.updateByColumn(task, DataTask::getBusinessSerialNo);
        }
        return R.ok();
    }


    @PostMapping("/editTask")
    public R<Void> editTask(@RequestBody DataTask task) {
        return null;
    }


    @GetMapping("/deleteTask")
    public R<Void> deleteTask(String businessSerialNo, String isDeleteFile) {
        DataTask dataTask = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if (dataTask == null) {
            return R.ok("已经删除过了！");
        }
        //是否删除文件
        if (StrUtil.isNotEmpty(isDeleteFile) && isDeleteFile.equals("1")) {
            //如果文件不为null
            if (dataTask.getImages() != null) {
                //删除oss文件
                List<Long> ossIds = dataTask.getImages().stream().map(DataImage::getOssId).toList();
                if (!ossIds.isEmpty()) {
                    iSysOssService.deleteWithValidByIds(ossIds, Boolean.FALSE);
                }
                //删除文件数据
                List<String> list = dataTask.getImages().stream().map(DataImage::getFileId).toList();
                dataImageServer.lambdaUpdate().in(DataImage::getFileId, list).remove();
            }
        }
        //删除单据
        boolean remove = dataTaskServer.lambdaUpdate().eq(DataTask::getBusinessSerialNo, businessSerialNo).remove();
        if (remove) {
            return R.ok();
        }
        return R.fail();

    }

    @PostMapping("/relevanceDocument")
    public R<Void> relevanceDocument(@RequestBody TaskAndImages taskAndImages) {
        final String businessSerialNo = taskAndImages.getBusinessSerialNo();
        if (StringUtils.isBlank(businessSerialNo)) {
            throw new ServiceException("业务流水号不能为空!");
        }
        DataTask task = this.dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if (task == null) {
            throw new ServiceException("业务单据不存在!");
        }

        List<DataImage> list = dataImageServer.lambdaQuery().in(DataImage::getFileId, taskAndImages.getFileIds()).list();
        task.setImages(list);
//         boolean update = dataTaskServer.lambdaUpdate().eq(DataTask::getBusinessSerialNo, taskAndImages.getBusinessSerialNo()).set(DataTask::getImages, list).update();
        final Boolean update = this.dataTaskServer.updateById(task);
        if (update) {
            return R.ok("更新成功！");
        }
        return R.ok("操作成功,本次没有更新");
    }


    @PostMapping("/additionDocument")
    public R<Void> additionDocument(@RequestBody TaskAndImages taskAndImages) {
        final String businessSerialNo = taskAndImages.getBusinessSerialNo();
        if (StringUtils.isBlank(businessSerialNo)) {
            throw new ServiceException("业务流水号不能为空!");
        }
        DataTask task = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if (task == null) {
            throw new ServiceException("业务单据不存在!");
        }
        List<DataImage> imageList = task.getImages();
        if (imageList == null) {
            // 如果为空就初始化一下
            imageList = new ArrayList<>(2);
        }
        List<DataImage> list = dataImageServer.lambdaQuery().in(DataImage::getFileId, taskAndImages.getFileIds()).list();

        log.info("list长度:{}", list.size() + "====");

        // 这里需要改一下修改mongo的方式
        //            Query query = new Query(Criteria.where("businessSerialNo").is(taskAndImages.getBusinessSerialNo()));
        //            Update update = new Update().push("images", dataImage);
        //            mongoTemplate.updateFirst(query, update, DataTask.class);
        imageList.addAll(list);
        task.setImages(imageList);
        final Boolean aBoolean = this.dataTaskServer.updateById(task);
        log.info("更新结果:{}", aBoolean);

        return R.ok("更新成功！");
    }

    @GetMapping("/getTaskInfoImages")
    public R<List<DataImage>> getTaskInfoImages(String businessSerialNo) {

        DataTask one = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if (one == null) {
            return R.fail(businessSerialNo + "单据不存在");
        }
        List<DataImage> images = one.getImages() == null ? new ArrayList<>() : one.getImages();
        return R.ok(images);

    }

    @PostMapping(value = "/getPDF")
    public R<String> getPDF(@RequestBody Map<String, Object> requestBody) {
        Object object = requestBody.get("fieldIds");
        if (!(object instanceof List)) {
            return R.fail("参数错误");
        }
        List<String> fieldIds = (List<String>) requestBody.get("fieldIds");
        if (CollectionUtils.isEmpty(fieldIds)) {
            return R.fail("文件id为空");
        }
        log.info("文件转换pdf, fieldIds:{}", JSON.toJSONString(fieldIds));
        List<DataImage> imageList = dataImageService.lambdaQuery().in(DataImage::getFileId, fieldIds).list();
        log.info("获取文件详情， imageList：{}", JSON.toJSONString(imageList));

        if (CollectionUtils.isEmpty(imageList)) {
            return R.fail("文件不存在");
        }

        List<String> imageUrls = imageList.stream()
            .map(DataImage::getSourceFileUrl)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(imageUrls)) {
            return R.fail("文件不存在");
        }

        List<byte[]> byteList = new ArrayList<>();

        try {
            // 检测文件类型
            Tika tika = new Tika();
            List<String> base64EncodedPages = new ArrayList<>();

            for (String url : imageUrls) {
                // 根据url地址，获取文件的byte数组
                byte[] fileBytesFromUrl = ImageUtil.getFileBytesFromUrl(url);
                // 获取文件类型
                String mimeType = tika.detect(new URL(url));
                String extension = ImageUtil.getExtensionFromMimeType(mimeType);

                byte[] bytes = null;
                switch (extension.toLowerCase()) {
                    case "xls":
                    case "xlsx":
                        bytes = ExcelToPdf.excelToPdf(fileBytesFromUrl);
                        break;
                    case "doc":
                    case "docx":
                        bytes = WordToPdf.wordToPdf(fileBytesFromUrl);
                        break;
                    case "ppt":
                    case "pptx":
                        bytes = PptToPdf.pptToPdf(fileBytesFromUrl);
                        break;
                    case "jpg":
                    case "jpeg":
                    case "png":
                    case "gif":
                        bytes = ImageUtil.convertImageToPdf(fileBytesFromUrl, extension);
                        break;
                    case "ofd":
                        bytes = OfdUtils.ofdToPdf(fileBytesFromUrl);
                        break;
                    case "txt":
                        bytes = TxtToPdf.txtToPdf(fileBytesFromUrl);
                        break;
                    case "pdf":
                        bytes = fileBytesFromUrl;
                        break;
                    default:
                        throw new IllegalArgumentException("不支持的文件类型: " + extension);
                }
                byteList.add(bytes);
            }
            byte[] bytes = ImageUtil.mergePdfDocuments(byteList);
            String base64Pdf = Base64.getEncoder().encodeToString(bytes);
            return R.ok("", base64Pdf);
        } catch (Exception e) {
            log.error("文件转换pdf异常, imageUrls:{}", JSON.toJSONString(imageUrls), e);
            return R.fail("发生未知异常");
        }
    }

    @SaIgnore
    @GetMapping("/getTaskInfoForThirdParties")
    public R<List<DataImage>> getTaskInfoForThirdParties(String businessSerialNo) {
        if (StrUtil.isEmpty(businessSerialNo)) {
            return R.ok("入参为null，请重新传参");
        }
        DataTask dataTask = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if (dataTask == null) {
            return R.fail(businessSerialNo + "单据不存在");
        }
        //单据下影像集合
        List<DataImage> images = dataTask.getImages() == null ? new ArrayList<>() : dataTask.getImages();
        return R.ok(images);

    }


}
