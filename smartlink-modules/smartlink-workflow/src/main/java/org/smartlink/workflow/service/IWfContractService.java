package org.smartlink.workflow.service;

import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.workflow.domain.bo.WfContractBo;
import org.smartlink.workflow.domain.vo.WfContractTreeVo;
import org.smartlink.workflow.domain.vo.WfContractVo;

import java.util.Collection;
import java.util.List;

/**
 * 合同Service接口
 */
public interface IWfContractService {

    WfContractVo queryById(Long id);

    TableDataInfo<WfContractVo> queryPageList(WfContractBo bo, PageQuery pageQuery);

    List<WfContractVo> queryList(WfContractBo bo);

    List<WfContractTreeVo> treeList(WfContractBo bo);

    Boolean insertByBo(WfContractBo bo);

    Boolean updateByBo(WfContractBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
