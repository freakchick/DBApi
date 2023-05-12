package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-20 09:52
 **/
@Data
@TableName(value = "datasource")
public class DataSource implements Serializable {

    @TableId(value = "id")
    String id;

    @TableField
    String name;

    @TableField
    String note;

    @TableField
    String url;

    @TableField
    String username;

    @TableField
    String password;

    /**
     * true 修改密码 false不修改
     */
    @TableField(exist = false)
    boolean edit_password;

    @TableField
    String type;

    @TableField
    String driver;

    @TableField(value = "table_sql")
    String tableSql;

    @TableField("create_user_id")
    Integer createUserId;

    @TableField(value = "create_time")
    String createTime;

    @TableField(value = "update_time")
    String updateTime;

}
