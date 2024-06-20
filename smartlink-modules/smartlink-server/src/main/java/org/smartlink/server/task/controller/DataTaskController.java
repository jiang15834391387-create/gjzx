package org.smartlink.server.task.controller;

import cn.hutool.core.util.StrUtil;
import com.anwen.mongo.model.PageParam;
import com.anwen.mongo.model.PageResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.server.image.momain.DataImage;
import org.smartlink.server.image.service.DataImageServer;
import org.smartlink.server.task.domain.bo.TaskAndImages;
import org.smartlink.server.task.momain.DataTask;
import org.smartlink.server.task.service.DataTaskServer;
import org.smartlink.system.service.ISysOssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/server/task")
public class DataTaskController extends BaseController {

    @Value("${frontEnd.url}")
    private String frontEndUrl;
    private final ISysOssService iSysOssService;
    private final DataTaskServer dataTaskServer;

    private final DataImageServer dataImageServer;


    @PostMapping("/getTaskList")
    public PageResult<DataTask> getTaskList(@RequestBody DataTask dataTask, @RequestBody PageParam pageParam) {
        return dataTaskServer.lambdaQuery().projectNone(DataTask::getImages
        ).like(StrUtil.isNotEmpty(dataTask.getBusinessSerialNo()),
            DataTask::getBusinessSerialNo, dataTask.getBusinessSerialNo()).page(pageParam);
    }

    @GetMapping("/getTaskInfo")
    public DataTask getTaskInfo(String businessSerialNo) {
        return dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo,businessSerialNo).one();
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
        //删除oss文件
        List<Long> ossIds = dataTask.getImages().stream().map(DataImage::getOssId).toList();
        if(ossIds.size()>0){
            iSysOssService.deleteWithValidByIds(ossIds,false);
        }
        //删除文件数据
        List<String> list = dataTask.getImages().stream().map(DataImage::getFileId).toList();
        dataImageServer.lambdaUpdate().in(DataImage::getFileId, list).remove();

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
            return R.ok();
        }
        return R.fail();
    }

    @GetMapping("/getTaskUrl")
    public R<String> getTaskUrl(String businessSerialNo) {
        String s = frontEndUrl+ "/documentInfo/" + businessSerialNo;
        return R.ok(s);

    }


}
