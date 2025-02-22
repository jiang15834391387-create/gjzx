package org.smartlink.workflow.mapper;



import org.apache.ibatis.annotations.Mapper;
import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.workflow.domain.BpmFormDO;
import org.smartlink.workflow.domain.vo.TestFormManageVo;
import org.smartlink.workflow.domain.vo.form.BpmFormRespVO;

/**
 * 动态表单 Mapper
 *
 * @author 风里雾里
 */
@Mapper
public interface BpmFormMapper extends BaseMapperPlus<BpmFormDO, BpmFormRespVO> {

}
