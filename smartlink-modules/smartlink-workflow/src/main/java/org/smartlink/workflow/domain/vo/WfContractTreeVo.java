package org.smartlink.workflow.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 合同树节点
 */
@Data
public class WfContractTreeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String parentId;

    private String nodeType;

    private String label;

    private String contractNo;

    private Long contractId;

    private Integer total;

    private List<WfContractTreeVo> children = new ArrayList<>();
}
