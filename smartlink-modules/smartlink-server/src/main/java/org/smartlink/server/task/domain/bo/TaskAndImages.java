package org.smartlink.server.task.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class TaskAndImages {

    private String businessSerialNo;

    private List<String> fileIds;
}
