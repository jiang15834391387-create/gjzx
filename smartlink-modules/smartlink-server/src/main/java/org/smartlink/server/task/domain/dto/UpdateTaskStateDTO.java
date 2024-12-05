package org.smartlink.server.task.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateTaskStateDTO implements Serializable {

    /**
     * 单据流水号
     */
    private String businessSerialNo;
    /**
     * 单据状态 000未归档，001归档成功
     */
    private String state;
    /**
     * 用户名称
     */
    private String userName;
}
