package org.smartlink.business.service.Impl;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.business.domain.bo.DataImageFilesInfoBo;
import org.smartlink.business.domain.vo.DataImageFilesInfoVo;
import org.smartlink.business.domain.DataImageFilesInfo;
import org.smartlink.business.mapper.DataImageFilesInfoMapper;
import org.smartlink.business.service.IDataImageFilesInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 图片文件Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataImageFilesInfoServiceImpl implements IDataImageFilesInfoService {

    private final DataImageFilesInfoMapper baseMapper;

    /**
     * 查询图片文件
     *
     * @param fileId 主键
     * @return 图片文件
     */
    @Override
    public DataImageFilesInfoVo queryById(String fileId){
        return baseMapper.selectVoById(fileId);
    }

    /**
     * 分页查询图片文件列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 图片文件分页列表
     */
    @Override
    public TableDataInfo<DataImageFilesInfoVo> queryPageList(DataImageFilesInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataImageFilesInfo> lqw = buildQueryWrapper(bo);
        Page<DataImageFilesInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的图片文件列表
     *
     * @param bo 查询条件
     * @return 图片文件列表
     */
    @Override
    public List<DataImageFilesInfoVo> queryList(DataImageFilesInfoBo bo) {
        LambdaQueryWrapper<DataImageFilesInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataImageFilesInfo> buildQueryWrapper(DataImageFilesInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataImageFilesInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getParentFileId()), DataImageFilesInfo::getParentFileId, bo.getParentFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getGlorityInvestigationNo()), DataImageFilesInfo::getGlorityInvestigationNo, bo.getGlorityInvestigationNo());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoice()), DataImageFilesInfo::getInvoice, bo.getInvoice());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), DataImageFilesInfo::getBarCode, bo.getBarCode());
        lqw.eq(StringUtils.isNotBlank(bo.getBatchId()), DataImageFilesInfo::getBatchId, bo.getBatchId());
        lqw.eq(StringUtils.isNotBlank(bo.getGlorityImageId()), DataImageFilesInfo::getGlorityImageId, bo.getGlorityImageId());
        lqw.eq(StringUtils.isNotBlank(bo.getCip()), DataImageFilesInfo::getCip, bo.getCip());
        lqw.eq(StringUtils.isNotBlank(bo.getFileMd5()), DataImageFilesInfo::getFileMd5, bo.getFileMd5());
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), DataImageFilesInfo::getFileName, bo.getFileName());
        lqw.eq(StringUtils.isNotBlank(bo.getFileSize()), DataImageFilesInfo::getFileSize, bo.getFileSize());
        lqw.eq(StringUtils.isNotBlank(bo.getFileFlowStatus()), DataImageFilesInfo::getFileFlowStatus, bo.getFileFlowStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getFileType()), DataImageFilesInfo::getFileType, bo.getFileType());
        lqw.eq(StringUtils.isNotBlank(bo.getFolderId()), DataImageFilesInfo::getFolderId, bo.getFolderId());
        lqw.eq(StringUtils.isNotBlank(bo.getImageSecret()), DataImageFilesInfo::getImageSecret, bo.getImageSecret());
        lqw.eq(StringUtils.isNotBlank(bo.getSurl()), DataImageFilesInfo::getSurl, bo.getSurl());
        lqw.eq(StringUtils.isNotBlank(bo.getIurl()), DataImageFilesInfo::getIurl, bo.getIurl());
        lqw.eq(StringUtils.isNotBlank(bo.getPurl()), DataImageFilesInfo::getPurl, bo.getPurl());
        lqw.eq(StringUtils.isNotBlank(bo.getLurl()), DataImageFilesInfo::getLurl, bo.getLurl());
        lqw.eq(StringUtils.isNotBlank(bo.getMessage()), DataImageFilesInfo::getMessage, bo.getMessage());
        lqw.like(StringUtils.isNotBlank(bo.getDocumentName()), DataImageFilesInfo::getDocumentName, bo.getDocumentName());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckStatus()), DataImageFilesInfo::getCheckStatus, bo.getCheckStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getFileStatus()), DataImageFilesInfo::getFileStatus, bo.getFileStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getSortValue()), DataImageFilesInfo::getSortValue, bo.getSortValue());
        lqw.eq(StringUtils.isNotBlank(bo.getRotateAngle()), DataImageFilesInfo::getRotateAngle, bo.getRotateAngle());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataImageFilesInfo::getDeleteFlag, bo.getDeleteFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getPageTime()), DataImageFilesInfo::getPageTime, bo.getPageTime());
        lqw.eq(StringUtils.isNotBlank(bo.getIncludeTypeArr()), DataImageFilesInfo::getIncludeTypeArr, bo.getIncludeTypeArr());
        return lqw;
    }

    /**
     * 新增图片文件
     *
     * @param bo 图片文件
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataImageFilesInfoBo bo) {
        DataImageFilesInfo add = MapstructUtils.convert(bo, DataImageFilesInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setFileId(add.getFileId());
        }
        return flag;
    }

    /**
     * 修改图片文件
     *
     * @param bo 图片文件
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataImageFilesInfoBo bo) {
        DataImageFilesInfo update = MapstructUtils.convert(bo, DataImageFilesInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataImageFilesInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除图片文件信息
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
        return baseMapper.deleteByIds(ids) > 0;
    }
}
