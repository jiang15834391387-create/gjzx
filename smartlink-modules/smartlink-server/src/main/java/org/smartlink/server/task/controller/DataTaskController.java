package org.smartlink.server.task.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.anwen.mongo.model.PageParam;
import com.anwen.mongo.model.PageResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.oss.service.StrategyService;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.server.image.domain.bo.ImageTreeBo;
import org.smartlink.server.image.momain.DataImage;
import org.smartlink.server.image.service.DataImageServer;
import org.smartlink.server.nodeType.domain.DataNodeType;
import org.smartlink.server.nodeType.mapper.DataNodeTypeMapper;
import org.smartlink.server.task.domain.bo.DocumentType;
import org.smartlink.server.task.domain.bo.TaskAndImages;
import org.smartlink.server.task.domain.vo.DataTaskVo;
import org.smartlink.server.task.momain.DataTask;
import org.smartlink.server.task.service.DataTaskServer;
import org.smartlink.system.domain.vo.SysOssVo;
import org.smartlink.system.service.ISysOssService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/server/task")
public class DataTaskController extends BaseController {


    private final ISysOssService iSysOssService;
    private final DataTaskServer dataTaskServer;

    private final DataImageServer dataImageServer;

    private final StrategyService strategyService;

    private final DataNodeTypeMapper dataNodeTypeMapper;


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

        for (DataImage image : images) {
            String runJianId;
            // 预览地址要去请求润健文件服务器
            if (StrUtil.isBlank(image.getRunJianId())) {
                final SysOssVo ossVo = this.iSysOssService.getById(image.getOssId());
                runJianId = ossVo.getFileId();
            } else {
                runJianId = image.getRunJianId();
            }
            String fileViewUrl = strategyService.fileViewUrl(runJianId, image.getUid());
            image.setPreviewUrl(fileViewUrl);
            image.setSourceFileUrl(fileViewUrl);

        }
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

            tree.putExtra("previewUrl", image.getPreviewUrl());
            tree.putExtra("sourceFileUrl", image.getSourceFileUrl());
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
        log.info("新增或修改单据任务,参数:{}", task.toString());
        DataTask oldTask = this.dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, task.getBusinessSerialNo()).one();

        Boolean save;
        if (oldTask == null) {
            log.info("新增单据任务,{}", task.getBusinessSerialNo());
            task.setTaskState("");
            save = this.dataTaskServer.save(task);
        } else {
            log.info("修改单据任务,{}", task.getBusinessSerialNo());

            if (StringUtils.hasText(oldTask.getBillNum())) {

                if (oldTask.getBillNum().equals(task.getBillNum())) {
                    log.info("新旧单据号相同，无需操作数据库！");
                    return R.ok();
                }
            }
            save = dataTaskServer.updateByColumn(task, DataTask::getBusinessSerialNo);
        }
        if (!save) {
            return R.fail("新增或修改单据任务错误");
        }
        return R.ok();
    }

    @GetMapping("/packageDownload")
    public void packageDownload(@RequestParam("businessSerialNo") String businessSerialNo, HttpServletResponse response) throws Exception {
        DataTask one = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if (one == null) {
            throw new Exception("单据不存在");
        }
        List<DataImage> imageList = one.getImages();
        //获取存储的ossId列表
        List<Long> ossIds = imageList.stream().map(DataImage::getOssId).toList();
        String uid = "";
        if (CollUtil.isNotEmpty(imageList)) {
            uid = imageList.get(0).getUid();
        }
        //通过ossId，去数据库查询fileId
        List<SysOssVo> sysOssVoList = iSysOssService.listByIds(ossIds);
        List<String> fileIdList = sysOssVoList.stream().map(SysOssVo::getFileId).toList();
        iSysOssService.downloadByString(fileIdList, response, uid);


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
                    iSysOssService.deleteWithValidByIds(ossIds, false);
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

    /**
     * 关联单据
     *
     * @param taskAndImages
     * @return
     */
    @PostMapping("/relevanceDocument")
    public R<Void> relevanceDocument(@RequestBody TaskAndImages taskAndImages) {

        DataTask task = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, taskAndImages.getBusinessSerialNo()).one();
        if (task == null) {
            return R.fail("单据不存在");
        }

        List<DataImage> list = dataImageServer.lambdaQuery().in(DataImage::getFileId, taskAndImages.getFileIds()).list();

        if (CollUtil.isEmpty(list)) {
            task.setImages(new ArrayList<>());
        } else {
            task.setImages(list);
        }

        boolean update = this.dataTaskServer.updateById(task);

        if (update) {
            return R.ok("更新成功！");
        }
        return R.ok("操作成功,本次没有更新");
    }

    /**
     * 关联单据，追加
     *
     * @param taskAndImages
     * @return
     */
    @PostMapping("/additionDocument")
    public R<Void> additionDocument(@RequestBody TaskAndImages taskAndImages) {
        DataTask task = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, taskAndImages.getBusinessSerialNo()).one();
        if (task == null) {
            return R.fail("单据不存在");
        }
        List<DataImage> oldImageList = task.getImages();
        // 为空初始化
        if (oldImageList == null) {
            oldImageList = new ArrayList<>(2);
        }

        List<DataImage> imageList = this.dataImageServer.lambdaQuery().in(DataImage::getFileId, taskAndImages.getFileIds()).list();
        if (CollUtil.isNotEmpty(imageList)) {
            for (DataImage dataImage : imageList) {

                if (oldImageList.contains(dataImage)) {
                    log.info("文件已经存在，请勿重复添加！");
                    continue;
                }
                oldImageList.add(dataImage);
            }
        }
        task.setImages(oldImageList);
        final Boolean aBoolean = this.dataTaskServer.updateById(task);

        if (aBoolean) {
            return R.ok("更新成功！");
        }
        return R.ok("没有改动！");
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

    @PostMapping("/synDocumentTypes")
    public R synDocumentTypes(@RequestBody List<DocumentType> documentTypeList) {
        if (CollectionUtils.isEmpty(documentTypeList)) {
            return R.fail("同步单据类型为空");
        }
        for (DocumentType documentType : documentTypeList) {
            String typeCode = documentType.getTypeCode();
//            DataImageServer
        }
        return R.ok();
    }
}
