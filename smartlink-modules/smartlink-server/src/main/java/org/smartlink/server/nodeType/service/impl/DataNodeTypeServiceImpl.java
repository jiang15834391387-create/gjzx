package org.smartlink.server.nodeType.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.server.nodeType.domain.DataNodeType;
import org.smartlink.server.nodeType.domain.bo.DataNodeTypeBo;
import org.smartlink.server.nodeType.domain.vo.DataNodeTypeVo;
import org.smartlink.server.nodeType.mapper.DataNodeTypeMapper;
import org.smartlink.server.nodeType.service.IDataNodeTypeService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 树节点Service业务层处理
 *
 * @author Lion Li
 * @date 2024-08-15
 */
@RequiredArgsConstructor
@Service
public class DataNodeTypeServiceImpl implements IDataNodeTypeService {

    private final DataNodeTypeMapper baseMapper;

    /**
     * 查询树节点
     *
     * @param id 主键
     * @return 树节点
     */
    @Override
    public DataNodeTypeVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询树节点列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 树节点分页列表
     */
    @Override
    public TableDataInfo<DataNodeTypeVo> queryPageList(DataNodeTypeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataNodeType> lqw = buildQueryWrapper(bo);
        Page<DataNodeTypeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的树节点列表
     *
     * @param bo 查询条件
     * @return 树节点列表
     */
    @Override
    public List<DataNodeTypeVo> queryList(DataNodeTypeBo bo) {
        LambdaQueryWrapper<DataNodeType> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataNodeType> buildQueryWrapper(DataNodeTypeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataNodeType> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getNodeType()), DataNodeType::getNodeType, bo.getNodeType());
        lqw.like(StringUtils.isNotBlank(bo.getNodeName()), DataNodeType::getNodeName, bo.getNodeName());
        lqw.eq(StringUtils.isNotBlank(bo.getNodeCode()), DataNodeType::getNodeCode, bo.getNodeCode());
        lqw.eq(StringUtils.isNotBlank(bo.getParentId()), DataNodeType::getParentId, bo.getParentId());
        return lqw;
    }

    /**
     * 新增树节点
     *
     * @param bo 树节点
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataNodeTypeBo bo) {
        DataNodeType add = MapstructUtils.convert(bo, DataNodeType.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改树节点
     *
     * @param bo 树节点
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataNodeTypeBo bo) {
        DataNodeType update = MapstructUtils.convert(bo, DataNodeType.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataNodeType entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除树节点信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public R<Void> syncTreeNodes(List<DataNodeType> bo) {
        baseMapper.delete(new LambdaQueryWrapper<>());
        boolean b = baseMapper.insertBatch(bo);
        return R.ok();
    }
}
