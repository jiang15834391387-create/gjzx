package org.smartlink.server.task.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.anwen.mongo.model.PageParam;
import com.anwen.mongo.model.PageResult;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.server.image.momain.DataImage;
import org.smartlink.server.image.service.DataImageServer;
import org.smartlink.server.task.domain.bo.TaskAndImages;
import org.smartlink.server.task.domain.vo.DataTaskVo;
import org.smartlink.server.task.momain.DataTask;
import org.smartlink.server.task.service.DataTaskServer;
import org.smartlink.system.service.ISysOssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/server/task")
public class DataTaskController extends BaseController {


    private final ISysOssService iSysOssService;
    private final DataTaskServer dataTaskServer;

    private final DataImageServer dataImageServer;


    @PostMapping("/getTaskList")
    public PageResult<DataTask> getTaskList(@RequestBody DataTask dataTask, @RequestBody PageParam pageParam) {
        return dataTaskServer.lambdaQuery().projectNone(DataTask::getImages).like(StrUtil.isNotEmpty(dataTask.getBusinessSerialNo()),
            DataTask::getBusinessSerialNo, dataTask.getBusinessSerialNo()).page(pageParam);
    }

    @GetMapping("/getTaskInfo")
    public R<DataTaskVo> getTaskInfo(String businessSerialNo) {
        DataTask one = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if(one==null){
            return R.fail(businessSerialNo+"单据不存在");
        }
        List<DataImage> images = one.getImages() == null ? new ArrayList<>(): one.getImages();
        //TODO 临时加的类型，后面需要手动添加表
        DataImage fj = new DataImage();
        fj.setParentId("0");
        fj.setFileId("fj");
        fj.setFileName("附件");
        images.add(fj);
        List<Tree<String>> build = TreeUtil.build(images,"0",  (image, tree) -> {
            tree.setId(image.getFileId());
            tree.setParentId(image.getParentId());
            tree.setName(image.getFileName());
            tree.setWeight(image.getSort());
            tree.putExtra("sourceFileUrl", image.getSourceFileUrl());
            tree.putExtra("previewUrl", image.getPreviewUrl());
            tree.putExtra("fileName", image.getFileName());
            tree.putExtra("ossId", image.getOssId());
        });

       DataTaskVo dataTaskVo = new DataTaskVo();
       BeanUtil.copyProperties(one,dataTaskVo);
       dataTaskVo.setImageTree(build);
       return R.ok(dataTaskVo);

    }

    @PostMapping("/addTask")
    public R<Void> addTask(@RequestBody DataTask task) {
        Boolean save = dataTaskServer.save(task);
        if (save) {
            return R.ok();
        }
        return R.fail();
    }


    @PostMapping("/editTask")
    public R<Void> editTask(@RequestBody DataTask task) {
        return null;
    }


    @GetMapping("/deleteTask")
    public R<Void> deleteTask(String businessSerialNo) {
        DataTask dataTask = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if(dataTask==null){
            return R.ok("已经删除过了！");
        }
        //如果文件不为null
        if(dataTask.getImages()!=null){
            //删除oss文件
            List<Long> ossIds = dataTask.getImages().stream().map(DataImage::getOssId).toList();
            if(ossIds.size()>0){
                iSysOssService.deleteWithValidByIds(ossIds,false);
            }
            //删除文件数据
            List<String> list = dataTask.getImages().stream().map(DataImage::getFileId).toList();
            dataImageServer.lambdaUpdate().in(DataImage::getFileId, list).remove();
        }
        //删除单据
        boolean remove = dataTaskServer.lambdaUpdate().eq(DataTask::getBusinessSerialNo, businessSerialNo).remove();
        if(remove){
            return R.ok();
        }
        return R.fail();

    }
    @PostMapping("/relevanceDocument")
    public R<Void> relevanceDocument(@RequestBody TaskAndImages taskAndImages) {
        List<DataImage> list = dataImageServer.lambdaQuery().in(DataImage::getFileId, taskAndImages.getFileIds()).list();
        boolean update = dataTaskServer.lambdaUpdate().eq(DataTask::getBusinessSerialNo, taskAndImages.getBusinessSerialNo()).set(DataTask::getImages, list).update();
        if(update){
            return R.ok("更新成功！");
        }
        return R.ok("操作成功,本次没有更新");
    }


}
