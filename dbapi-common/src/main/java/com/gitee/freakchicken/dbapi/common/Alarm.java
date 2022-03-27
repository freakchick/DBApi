package com.gitee.freakchicken.dbapi.common;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName(value = "api_config")
public class ApiConfig {

    @TableField(value = "api_id")
    String apiId;
    
    @TableField
    String mail;
}