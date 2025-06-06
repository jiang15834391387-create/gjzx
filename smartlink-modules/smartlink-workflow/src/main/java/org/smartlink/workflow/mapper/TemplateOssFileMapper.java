package org.smartlink.workflow.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.workflow.domain.TemplateOssFile;
import org.smartlink.workflow.domain.vo.TaskVo;


/**
 *
 */
public interface TemplateOssFileMapper extends BaseMapperPlus<TemplateOssFile,  TemplateOssFile> {
}
