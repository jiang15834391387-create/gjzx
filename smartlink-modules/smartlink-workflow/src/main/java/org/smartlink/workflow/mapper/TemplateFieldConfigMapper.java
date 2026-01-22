package org.smartlink.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.smartlink.workflow.domain.TemplateFieldConfig;

import java.util.List;

@Mapper
public interface TemplateFieldConfigMapper extends BaseMapper<TemplateFieldConfig> {

}
