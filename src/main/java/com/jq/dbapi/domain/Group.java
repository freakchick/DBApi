package com.jq.dbapi.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "api_group")
public class Group {

    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    @TableField
    String name;
}
