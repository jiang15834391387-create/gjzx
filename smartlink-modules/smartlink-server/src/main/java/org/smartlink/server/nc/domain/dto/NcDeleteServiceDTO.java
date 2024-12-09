package org.smartlink.server.nc.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author L
 * @Description NC业务删除台账DTO
 * @create:
 */
@Data
public class NcDeleteServiceDTO {

    /**
     * 要删除的文件ID集合
     */
    List<String> fileIdList;

    /**
     * 流水号
     */
    String businessSerialNo;

}
