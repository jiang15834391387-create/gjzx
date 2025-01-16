package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataMedicalTreatment;
import org.smartlink.common.entity.domain.business.domain.DataMotorVehicleSale;
import org.smartlink.common.entity.domain.business.domain.bo.DataMedicalTreatmentBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataMedicalTreatmentVo;
import org.smartlink.common.entity.domain.business.mapper.DataMedicalTreatmentMapper;
import org.smartlink.common.entity.domain.business.service.IDataMedicalTreatmentService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 非税收入类票据Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DataMedicalTreatmentServiceImpl implements IDataMedicalTreatmentService {

    private final DataMedicalTreatmentMapper baseMapper;

    /**
     * 查询非税收入类票据
     *
     * @param id 主键
     * @return 非税收入类票据
     */
    @Override
    public DataMedicalTreatmentVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询非税收入类票据列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 非税收入类票据分页列表
     */
    @Override
    public TableDataInfo<DataMedicalTreatmentVo> queryPageList(DataMedicalTreatmentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataMedicalTreatment> lqw = buildQueryWrapper(bo);
        Page<DataMedicalTreatmentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public Boolean insert(DataMedicalTreatment dataOcrInfo) {
        boolean flag = baseMapper.insert(dataOcrInfo) > 0;
        return flag;
    }

    /**
     * 查询符合条件的非税收入类票据列表
     *
     * @param bo 查询条件
     * @return 非税收入类票据列表
     */
    @Override
    public List<DataMedicalTreatmentVo> queryList(DataMedicalTreatmentBo bo) {
        LambdaQueryWrapper<DataMedicalTreatment> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public DataMedicalTreatment selectOneByFileId(String fileId) {
        DataMedicalTreatment result = buildQueryWrapperByFileId(fileId);
        return result;
    }

    private DataMedicalTreatment buildQueryWrapperByFileId(String fileId) {
        LambdaQueryWrapper<DataMedicalTreatment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DataMedicalTreatment::getFileId, fileId); // 使用 Lambda 表达式
        return baseMapper.selectOne(lambdaQueryWrapper); // 返回查询结果
    }

    private LambdaQueryWrapper<DataMedicalTreatment> buildQueryWrapper(DataMedicalTreatmentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataMedicalTreatment> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataMedicalTreatment::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataMedicalTreatment::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataMedicalTreatment::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataMedicalTreatment::getInvoiceCode, bo.getInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckCode()), DataMedicalTreatment::getCheckCode, bo.getCheckCode());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicMark()), DataMedicalTreatment::getElectronicMark, bo.getElectronicMark());
        lqw.eq(StringUtils.isNotBlank(bo.getKind()), DataMedicalTreatment::getKind, bo.getKind());
        lqw.eq(StringUtils.isNotBlank(bo.getOtherInfo()), DataMedicalTreatment::getOtherInfo, bo.getOtherInfo());
        lqw.eq(StringUtils.isNotBlank(bo.getPayee()), DataMedicalTreatment::getPayee, bo.getPayee());
        lqw.eq(StringUtils.isNotBlank(bo.getPayer()), DataMedicalTreatment::getPayer, bo.getPayer());
        lqw.eq(StringUtils.isNotBlank(bo.getSocialCreditCode()), DataMedicalTreatment::getSocialCreditCode, bo.getSocialCreditCode());
        lqw.eq(StringUtils.isNotBlank(bo.getHospital()), DataMedicalTreatment::getHospital, bo.getHospital());
        lqw.eq(StringUtils.isNotBlank(bo.getOverallAmount()), DataMedicalTreatment::getOverallAmount, bo.getOverallAmount());
        lqw.eq(StringUtils.isNotBlank(bo.getMedicalRecordNumber()), DataMedicalTreatment::getMedicalRecordNumber, bo.getMedicalRecordNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getInpatientNumber()), DataMedicalTreatment::getInpatientNumber, bo.getInpatientNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getOutpatientNumber()), DataMedicalTreatment::getOutpatientNumber, bo.getOutpatientNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getMedicalInsuranceNumber()), DataMedicalTreatment::getMedicalInsuranceNumber, bo.getMedicalInsuranceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getVisitDate()), DataMedicalTreatment::getVisitDate, bo.getVisitDate());
        lqw.eq(StringUtils.isNotBlank(bo.getMedicalInstitutionType()), DataMedicalTreatment::getMedicalInstitutionType, bo.getMedicalInstitutionType());
        lqw.eq(StringUtils.isNotBlank(bo.getMedicalInsuranceType()), DataMedicalTreatment::getMedicalInsuranceType, bo.getMedicalInsuranceType());
        lqw.eq(StringUtils.isNotBlank(bo.getGende()), DataMedicalTreatment::getGende, bo.getGende());
        lqw.eq(StringUtils.isNotBlank(bo.getOtherPayments()), DataMedicalTreatment::getOtherPayments, bo.getOtherPayments());
        lqw.eq(StringUtils.isNotBlank(bo.getPersonalAccountPayment()), DataMedicalTreatment::getPersonalAccountPayment, bo.getPersonalAccountPayment());
        lqw.eq(StringUtils.isNotBlank(bo.getCashPayment()), DataMedicalTreatment::getCashPayment, bo.getCashPayment());
        lqw.eq(StringUtils.isNotBlank(bo.getPersonalExpense()), DataMedicalTreatment::getPersonalExpense, bo.getPersonalExpense());
        lqw.eq(StringUtils.isNotBlank(bo.getPersonalPayment()), DataMedicalTreatment::getPersonalPayment, bo.getPersonalPayment());
        lqw.eq(StringUtils.isNotBlank(bo.getInpatientDepartment()), DataMedicalTreatment::getInpatientDepartment, bo.getInpatientDepartment());
        lqw.eq(StringUtils.isNotBlank(bo.getAdmissionDate()), DataMedicalTreatment::getAdmissionDate, bo.getAdmissionDate());
        lqw.eq(StringUtils.isNotBlank(bo.getDischargeDate()), DataMedicalTreatment::getDischargeDate, bo.getDischargeDate());
        lqw.eq(StringUtils.isNotBlank(bo.getAnnualHealthInsuranceCoverage()), DataMedicalTreatment::getAnnualHealthInsuranceCoverage, bo.getAnnualHealthInsuranceCoverage());
        lqw.eq(StringUtils.isNotBlank(bo.getAnnualOutpatientCatastrophicPayment()), DataMedicalTreatment::getAnnualOutpatientCatastrophicPayment, bo.getAnnualOutpatientCatastrophicPayment());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataMedicalTreatment::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataMedicalTreatment::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getTotalWords()), DataMedicalTreatment::getTotalWords, bo.getTotalWords());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataMedicalTreatment::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataMedicalTreatment::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataMedicalTreatment::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增非税收入类票据
     *
     * @param bo 非税收入类票据
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataMedicalTreatmentBo bo) {
        DataMedicalTreatment add = MapstructUtils.convert(bo, DataMedicalTreatment.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改非税收入类票据
     *
     * @param bo 非税收入类票据
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataMedicalTreatmentBo bo) {
        DataMedicalTreatment update = MapstructUtils.convert(bo, DataMedicalTreatment.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataMedicalTreatment entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除非税收入类票据信息
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
    /*
       * 根据文件fileId查询非税收入类票据信息
     */
    @Override
    public DataMedicalTreatment getByFileId(String fileId) {
        if (StringUtils.isBlank(fileId)){
            log.error("非税收入类票据fileId为空", fileId);
            return null;
        }
        return baseMapper.selectOne(new LambdaQueryWrapper<DataMedicalTreatment>().eq(DataMedicalTreatment::getFileId, fileId));
    }
}
