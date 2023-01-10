package com.gitee.freakchicken.dbapi.basic.dto;

import java.util.List;

import lombok.Data;

@Data
public class SQLTaskDto {
    
    String datasourceId;

    Boolean transaction;

    List<ApiSqlDto> sqlList;

}
