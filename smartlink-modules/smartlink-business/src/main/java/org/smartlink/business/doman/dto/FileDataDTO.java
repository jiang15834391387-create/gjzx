package org.smartlink.business.doman.dto;

import lombok.Data;

import java.util.List;

@Data
public class FileDataDTO {
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String fileUrl;
}

