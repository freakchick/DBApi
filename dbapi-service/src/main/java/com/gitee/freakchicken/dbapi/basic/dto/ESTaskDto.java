package com.gitee.freakchicken.dbapi.basic.dto;

import lombok.Data;

@Data
public class ESTaskDto {
    
    String method;
    String endpoint;
    String body;

    String datasourceId;
}
