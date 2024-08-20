package org.smartlink.server.task.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.anwen.mongo.model.PageParam;
import com.anwen.mongo.model.PageResult;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Literal;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.server.image.domain.bo.ImageTreeBo;
import org.smartlink.server.image.momain.DataImage;
import org.smartlink.server.image.service.DataImageServer;
import org.smartlink.server.nodeType.domain.DataNodeType;
import org.smartlink.server.nodeType.mapper.DataNodeTypeMapper;
import org.smartlink.server.nodeType.service.IDataNodeTypeService;
import org.smartlink.server.task.domain.bo.TaskAndImages;
import org.smartlink.server.task.domain.vo.DataTaskVo;
import org.smartlink.server.task.momain.DataTask;
import org.smartlink.server.task.service.DataTaskServer;
import org.smartlink.system.service.ISysOssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
        if(one==null){
            return R.fail(businessSerialNo+"单据不存在");
        }
        //树节点对象
        List<DataNodeType> dataNodeTypes = dataNodeTypeMapper.selectList();
        //树节点集合
        List<ImageTreeBo> imageTreeList = new ArrayList<>();
        //单据下影像集合
        List<DataImage> images = one.getImages() == null ? new ArrayList<>(): one.getImages();
        //节点ID
        List<String> typeIds = dataNodeTypes.stream().map(DataNodeType::getId).toList();
        //其他节点ID
        Optional<DataNodeType> other = dataNodeTypes.stream().filter(e -> StrUtil.equals(e.getNodeName(), "其他")).findFirst();
        //文件信息转换为树对象
        for (DataImage image : images) {
            //将其他ID都当成其他
            if(!typeIds.contains(image.getParentId())){
                image.setParentId(other.get().getId());
            }
            ImageTreeBo nodeTypeImage = new ImageTreeBo();
            BeanUtil.copyProperties(image,nodeTypeImage);
            nodeTypeImage.setType("image");
            imageTreeList.add(nodeTypeImage);
        }
        //节点信息转换为树对象
        for (DataNodeType dataNodeType : dataNodeTypes) {
            ImageTreeBo nodeTypeImage = new ImageTreeBo();
            nodeTypeImage.setFileId(dataNodeType.getId());
            nodeTypeImage.setParentId(dataNodeType.getParentId());
            nodeTypeImage.setFileName(dataNodeType.getNodeName());
            if(StrUtil.equals(dataNodeType.getId(),"0")){
                //根节点数量
                nodeTypeImage.setTotal((long) images.size());
            }else {
                //当前节点数量
                long count = images.stream().filter(e -> StrUtil.equals(e.getParentId(), dataNodeType.getId())).count();
                nodeTypeImage.setTotal(count);
            }
            nodeTypeImage.setType("node");
//            if(nodeTypeImage.getTotal()==0){
//                continue;
//            }
            imageTreeList.add(nodeTypeImage);
        }
        List<Tree<String>> build = TreeUtil.build(imageTreeList,"-1",  (image, tree) -> {
            tree.setId(image.getFileId());
            tree.setParentId(image.getParentId());
            tree.setName(image.getFileName());
            tree.setWeight(image.getSort());
            tree.putExtra("sourceFileUrl", image.getSourceFileUrl());
            tree.putExtra("previewUrl", image.getPreviewUrl());
            tree.putExtra("fileName", image.getFileName());
            tree.putExtra("ossId", image.getOssId());
            tree.putExtra("total", image.getTotal());
            tree.putExtra("type", image.getType());
        });

       DataTaskVo dataTaskVo = new DataTaskVo();
       BeanUtil.copyProperties(one,dataTaskVo);
       dataTaskVo.setImageTree(build);
       return R.ok(dataTaskVo);

    }

    @PostMapping("/addTask")
    public R<Void> addTask(@RequestBody DataTask task) {
        DataTask one = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, task.getBusinessSerialNo()).one();
        Boolean save ;
        if(one==null){
             save = dataTaskServer.save(task);
        }else {
            save = dataTaskServer.updateByColumn(task, DataTask::getBusinessSerialNo);
        }
        return R.ok();
    }


    @PostMapping("/editTask")
    public R<Void> editTask(@RequestBody DataTask task) {
        return null;
    }


    @GetMapping("/deleteTask")
    public R<Void> deleteTask(String businessSerialNo,String isDeleteFile) {
        DataTask dataTask = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if(dataTask==null){
            return R.ok("已经删除过了！");
        }
        //是否删除文件
        if(StrUtil.isNotEmpty(isDeleteFile)&&isDeleteFile.equals("1")){
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


    @PostMapping("/additionDocument")
    public R<Void> additionDocument(@RequestBody TaskAndImages taskAndImages) {

        List<DataImage> list  = dataImageServer.lambdaQuery().in(DataImage::getFileId, taskAndImages.getFileIds()).list();
        for (DataImage  dataImage: list) {
            Query query = new Query(Criteria.where("businessSerialNo").is(taskAndImages.getBusinessSerialNo()));
            Update update = new Update().push("images", dataImage);
            mongoTemplate.updateFirst(query, update, DataTask.class);
        }

        return R.ok("更新成功！");
    }

    @GetMapping("/getTaskInfoImages")
    public R<List<DataImage>> getTaskInfoImages(String businessSerialNo) {

        DataTask one = dataTaskServer.lambdaQuery().eq(DataTask::getBusinessSerialNo, businessSerialNo).one();
        if(one==null){
            return R.fail(businessSerialNo+"单据不存在");
        }
        List<DataImage> images = one.getImages() == null ? new ArrayList<>(): one.getImages();
        return R.ok(images);

    }


}
