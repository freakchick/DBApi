package com.gitee.freakchicken.dbapi.basic.dto;

import lombok.Data;

@Data
public class ApiSqlDto {
    
    String sqlText;
    String transformPlugin;
    String transformPluginParam;
    
}
