package org.smartlink.server.task.domain.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DocumentType {
    @JsonProperty("typeCode")
    private String typeCode;

    @JsonProperty("typeName")
    private String typeName;

    @JsonProperty("tradeType")
    private String tradeType;

    @JsonProperty("tradeTypeName")
    private String tradeTypeName;

}