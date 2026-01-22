package org.smartlink.business.invoice.service;

import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.entity.domain.business.domain.bo.DataOcrInfoBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataOcrInfoVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;
//查验
public interface IDataOcrInfoServices {
    /**
     * 查询增值税发票
     *
     * @param id 主键
     * @return 增值税发票
     */
    DataOcrInfoVo queryById(String id);

    /**
     * 分页查询增值税发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 增值税发票分页列表
     */
    TableDataInfo<DataOcrInfoVo> queryPageList(DataOcrInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的增值税发票列表
     *
     * @param bo 查询条件
     * @return 增值税发票列表
     */
    List<DataOcrInfoVo> queryList(DataOcrInfoBo bo);

    /**
     * 新增增值税发票
     *
     * @param bo 增值税发票
     * @return 是否新增成功
     */
    Boolean insertByBo(DataOcrInfoBo bo);

    /**
     * 修改增值税发票
     *
     * @param bo 增值税发票
     * @return 是否修改成功
     */
    Boolean updateByBo(DataOcrInfoBo bo);

    /**
     * 校验并批量删除增值税发票信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);


    // 根据fileId查询ocr基本信息和详细信息
    DataOcrInfo getByFileId(String fileId);
}
