package org.smartlink.server.nc.service.nc.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.server.nc.domain.imagefilesinfo.DataImageTree;
import org.smartlink.server.nc.mapper.DataImageTreeMapper;
import org.smartlink.server.nc.service.nc.IDataImageTreeService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
/**
 * 图片树节点Service业务层处理
 *
 * @author L
 * @date
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class DataImageTreeServiceImpl implements IDataImageTreeService {

    private final DataImageTreeMapper baseMapper;


    @Override
    public boolean verifyImageTree(List<String> productNames, String batchId) {
        boolean result = true;//默认自定义节点下没有发票为true
        LambdaQueryWrapper<DataImageTree> dataImageTreeLqw = new LambdaQueryWrapper<>();
        dataImageTreeLqw.in(DataImageTree::getProductName, productNames).eq(DataImageTree::getBatchId, batchId);
        //获取自定义树节点
        final List<DataImageTree> dataImageTrees = baseMapper.selectList(dataImageTreeLqw);
        log.info("dataImageTree=" + dataImageTrees);
        if (CollectionUtil.isNotEmpty(dataImageTrees) && dataImageTrees.size() == productNames.size()) {
            final List<String> collect = dataImageTrees.stream().map(DataImageTree::getProductId).collect(Collectors.toList());
            log.info("collect=" + collect);
            LambdaQueryWrapper<DataImageTree> dataImageTreeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dataImageTreeLambdaQueryWrapper.in(DataImageTree::getParentId, collect);
            //根据自定义树节点productId作为parentId查询树表
            final List<DataImageTree> dataImageTrees1 = baseMapper.selectList(dataImageTreeLambdaQueryWrapper);
            ArrayList<DataImageTree> dataImageTreeList = dataImageTrees1.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(
                Comparator.comparing(DataImageTree::getParentId)
            )), ArrayList::new));
            log.info("dataImageTrees1=" + dataImageTrees1);
            log.info("dataImageTreeList=" + dataImageTreeList);
            if (CollectionUtil.isNotEmpty(dataImageTrees1) && (dataImageTreeList.size() == collect.size())) {//每个自定义树节点下都要有发票
                result = false;//自定义节点下有发票为false
            }
        }
        return result;
    }

    @Override
    public boolean saveOrUpdate(DataImageTree dataImageTree) {
        return baseMapper.insertOrUpdate(dataImageTree);
    }

    @Override
    public List<DataImageTree> selectDataImageTreeByFileIdList(List<String> fileIdList) {
        if (CollUtil.isEmpty(fileIdList)) {
            return Collections.emptyList();
        }
        final LambdaQueryWrapper<DataImageTree> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DataImageTree::getImageId, fileIdList);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public DataImageTree selectImageTreeByFileId(String fileId) {
        final LambdaQueryWrapper<DataImageTree> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataImageTree::getImageId, fileId);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean deleteImageTreeByFileId(String fileId) {
        final LambdaQueryWrapper<DataImageTree> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataImageTree::getImageId, fileId);
        return this.baseMapper.delete(queryWrapper) > 0;
    }

    @Override
    public List<DataImageTree> selectList(LambdaQueryWrapper<DataImageTree> wrapper) {
        return baseMapper.selectList(wrapper);
    }
}
