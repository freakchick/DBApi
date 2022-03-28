package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName(value = "api_alarm")
public class Alarm {

    @TableField(value = "api_id")
    String apiId;
    
    @TableField
    String mail;
}