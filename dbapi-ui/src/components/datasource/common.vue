<template>
  <div>

    <el-form label-width="140px">
      <el-form-item label="数据库">
        <el-select v-model="detail.type" placeholder="请选择" @change="selectDB">
          <el-option v-for="item in options" :key="item.label" :label="item.label" :value="item.value">
            <db-icon :type="item.value"></db-icon>
            <span>{{ item.label }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="detail.name"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="detail.note"></el-input>
      </el-form-item>
      <el-form-item label="jdbc驱动">
        <el-input v-model="detail.driver"></el-input>
      </el-form-item>
      <el-form-item label="jdbc url">
        <el-input v-model="detail.url" type="textarea" class="my"></el-input>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="detail.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="detail.password"></el-input>
      </el-form-item>
      <el-form-item label="查询所有表名称sql">
        <el-input v-model="detail.tableSql"></el-input>
        <el-alert type="warning" show-icon>
          创建或编辑API的时候，选择数据源，会执行此sql来获取该数据源下的所有表名称
        </el-alert>
      </el-form-item>
    </el-form>

    <el-button type="primary" @click="connect" plain style="margin: 10px 0;">连接测试</el-button>
  </div>
</template>

<script>
import dbIcon from "@/components/common/dbIcon";

export default {
  name: "common",
  data() {
    return {
      options: [{label: 'mysql', value: 'mysql'}, {label: 'postgresql',value: 'postgresql'}, {label: 'hive',value: 'hive'},
        {label: 'sqlserver',value: 'sqlserver'}, {label: 'clickhouse',value: 'clickhouse'}, {label: 'kylin',value: 'kylin'},
         {label: 'oracle',value: 'oracle'}, {label: '其他',value:'others'}],
      detail: {
        url: null,
        name: null,
        note: null,
        type: null,
        username: null,
        password: null,
        driver: null,
        tableSql: null
      },
      ds: {
        mysql: {
          url: 'jdbc:mysql://localhost:3306/db?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8',
          driver: 'com.mysql.cj.jdbc.Driver',
          sql: 'show tables'
        },
        postgresql: {
          url: 'jdbc:postgresql://localhost:5432/db',
          driver: 'org.postgresql.Driver',
          sql: 'SELECT table_name FROM information_schema.tables WHERE table_schema = \'public\' ORDER BY table_name'
        },
        hive: {
          url: 'jdbc:hive2://localhost:10000/db',
          driver: 'org.apache.hive.jdbc.HiveDriver',
          sql: 'show tables'
        },
        sqlserver: {
          url: 'jdbc:microsoft:sqlserver://localhost:1433;databaseName=db',
          driver: 'com.microsoft.sqlserver.jdbc.SQLServerDriver',
          sql: 'select * from sys.tables'
        },
        clickhouse: {
          url: 'jdbc:clickhouse://localhost:8123/db',
          driver: 'ru.yandex.clickhouse.ClickHouseDriver'
        },
        kylin: {
          url: 'jdbc:kylin://localhost:7070/db',
          driver: 'org.apache.kylin.jdbc.Driver'
        },
        oracle: {
          url: 'jdbc:oracle:thin:@localhost:1521:db',
          driver: 'oracle.jdbc.OracleDriver',
          sql: 'select table_name from user_tables where TABLESPACE_NAME is not null'
        }
      }
    }

  },
  props: ["id"],
  methods: {
    selectDB() {
      this.detail.url = (this.ds[this.detail.type]).url
      this.detail.driver = (this.ds[this.detail.type]).driver
      this.detail.tableSql = (this.ds[this.detail.type]).sql
    },
    getDetail(id) {
      this.axios.post("/datasource/detail/" + id).then((response) => {
        this.detail = response.data
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    connect() {
      this.axios.post("/datasource/connect", {
        "url": this.detail.url,
        "username": this.detail.username,
        "password": this.detail.password,
        "driver": this.detail.driver
      }).then((response) => {
        if (response.data.success)
          this.$message.success("成功")
        else
          this.$message.error(response.data.msg)
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
  },
  created() {
    if (this.id != undefined)
      this.getDetail(this.id)
  },
  components: {dbIcon}
}
</script>

<style scoped>

.my >>> .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
}
</style>
