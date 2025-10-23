package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.entity.domain.business.domain.DataUsedCarSales;
import org.smartlink.common.entity.domain.business.domain.OtherAttachments;
import org.smartlink.common.entity.domain.business.mapper.OtherAttachmentsMapper;
import org.smartlink.common.entity.domain.business.service.IOtherAttachmentsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OtherAttachmentsserviceImpl implements IOtherAttachmentsService {
    private final OtherAttachmentsMapper baseMapper;

    @Override
    public Boolean insert(OtherAttachments otherAttachments) {
        boolean flag = baseMapper.insert(otherAttachments) > 0;
        return flag;
    }

    @Override
    public List<OtherAttachments> queryList(OtherAttachments otherAttachments) {
        QueryWrapper<OtherAttachments> queryWrapper = new QueryWrapper<>();
        // queryWrapper.lambda().eq(otherAttachments.getId() != null, OtherAttachments::getId, otherAttachments.getId());
        return baseMapper.selectList(queryWrapper);
    }
}
