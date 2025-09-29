package org.smartlink.workflow.service;


import jakarta.validation.constraints.NotNull;
import org.smartlink.business.doman.dto.StructureDataDTO;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.workflow.domain.TestFormManage;
import org.smartlink.workflow.domain.bo.TestExpenseReimbursementBo;
import org.smartlink.workflow.domain.vo.TestExpenseReimbursementVo;

import java.util.Collection;
import java.util.List;

/**
 * 费用报销申请Service接口
 *
 * @author Lion Li
 * @date 2025-01-07
 */
public interface ITestExpenseReimbursementService {

    /**
     * 查询费用报销申请
     *
     * @param id 主键
     * @return 费用报销申请
     */
    TestExpenseReimbursementVo queryById(Long id);

    /**
     * 分页查询费用报销申请列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 费用报销申请分页列表
     */
    TableDataInfo<TestExpenseReimbursementVo> queryPageList(TestExpenseReimbursementBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的费用报销申请列表
     *
     * @param bo 查询条件
     * @return 费用报销申请列表
     */
    List<TestExpenseReimbursementVo> queryList(TestExpenseReimbursementBo bo);

    /**
     * 新增费用报销申请
     *
     * @param bo 费用报销申请
     * @return 是否新增成功
     */
    TestExpenseReimbursementVo insertByBo(TestExpenseReimbursementBo bo);

    /**
     * 修改费用报销申请
     *
     * @param bo 费用报销申请
     * @return 是否修改成功
     */
    TestExpenseReimbursementVo updateByBo(TestExpenseReimbursementBo bo);

    /**
     * 校验并批量删除费用报销申请信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<TestFormManage> byFromId(@NotNull(message = "表单id为空") String id);


    R<Void> setReceiptUrl(TestExpenseReimbursementBo bo);

    R<List<StructureDataDTO>> selectStructureData(List<String> workIds);
}
