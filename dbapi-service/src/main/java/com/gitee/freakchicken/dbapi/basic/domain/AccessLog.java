package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("access_log")
public class AccessLog {

    @TableId(value = "id")
    private String id;

    @TableField
    private String url;

    @TableField
    private int status;

    @TableField
    private long duration;

    @TableField
    private long timestamp;

    @TableField
    private String ip;

    @TableField("client_id")
    private String clientId;

    @TableField("api_id")
    private String apiId;

    @TableField
    private String error;


}