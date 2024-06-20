package org.smartlink.server.image.domain.bo;

import lombok.Data;

@Data
public class UploadImageBo {

    private String fileBase64;

    private String fileName;
}
