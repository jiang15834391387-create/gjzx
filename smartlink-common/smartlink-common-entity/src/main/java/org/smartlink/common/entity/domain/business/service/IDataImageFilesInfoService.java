package org.smartlink.common.entity.domain.business.service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.bo.DataImageFilesInfoBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataImageFilesInfoVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 图片文件Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataImageFilesInfoService {

    /**
     * 查询图片文件
     *
     * @param fileId 主键
     * @return 图片文件
     */
    DataImageFilesInfoVo queryById(String fileId);

    /**
     * 分页查询图片文件列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 图片文件分页列表
     */
    TableDataInfo<DataImageFilesInfoVo> queryPageList(DataImageFilesInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的图片文件列表
     *
     * @param bo 查询条件
     * @return 图片文件列表
     */
    List<DataImageFilesInfoVo> queryList(DataImageFilesInfoBo bo);

    /**
     * 新增图片文件
     *
     * @param bo 图片文件
     * @return 是否新增成功
     */
    Boolean insertByBo(DataImageFilesInfoBo bo);

    /**
     * 修改图片文件
     *
     * @param bo 图片文件
     * @return 是否修改成功
     */
    Boolean updateByBo(DataImageFilesInfoBo bo);

    /**
     * 校验并批量删除图片文件信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

    List<DataImageFilesInfo> listlqw(LambdaQueryWrapper<DataImageFilesInfo> queryWrapper);
}
