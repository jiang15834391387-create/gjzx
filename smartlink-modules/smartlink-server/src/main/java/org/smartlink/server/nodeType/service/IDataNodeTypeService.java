package org.smartlink.server.nodeType.service;

import org.smartlink.common.core.domain.R;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.server.nodeType.domain.DataNodeType;
import org.smartlink.server.nodeType.domain.bo.DataNodeTypeBo;
import org.smartlink.server.nodeType.domain.vo.DataNodeTypeVo;

import java.util.Collection;
import java.util.List;

/**
 * 树节点Service接口
 *
 * @author Lion Li
 * @date 2024-08-15
 */
public interface IDataNodeTypeService {

    /**
     * 查询树节点
     *
     * @param id 主键
     * @return 树节点
     */
    DataNodeTypeVo queryById(String id);

    /**
     * 分页查询树节点列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 树节点分页列表
     */
    TableDataInfo<DataNodeTypeVo> queryPageList(DataNodeTypeBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的树节点列表
     *
     * @param bo 查询条件
     * @return 树节点列表
     */
    List<DataNodeTypeVo> queryList(DataNodeTypeBo bo);

    /**
     * 新增树节点
     *
     * @param bo 树节点
     * @return 是否新增成功
     */
    Boolean insertByBo(DataNodeTypeBo bo);

    /**
     * 修改树节点
     *
     * @param bo 树节点
     * @return 是否修改成功
     */
    Boolean updateByBo(DataNodeTypeBo bo);

    /**
     * 校验并批量删除树节点信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);



    /**
     * 同步树节点
     */
    R<Void> syncTreeNodes(List<DataNodeType> bo);
}
