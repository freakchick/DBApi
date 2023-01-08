package com.gitee.freakchicken.dbapi.basic.dto;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.freakchicken.dbapi.basic.domain.ApiAlarm;
import com.gitee.freakchicken.dbapi.basic.domain.ApiCache;
import com.gitee.freakchicken.dbapi.basic.domain.ApiSql;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import lombok.Data;

import java.util.List;

@Data
public class ApiConfigDto {

    ApiConfig apiConfig;

    List<ApiSql> sqlList;

    ApiCache apiCache;

    ApiAlarm alarm;

}
