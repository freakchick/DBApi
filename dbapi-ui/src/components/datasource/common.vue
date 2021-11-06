<template>
  <div>

    <el-form label-width="200px">
      <el-form-item :label="$t('m.database')">
        <el-select v-model="detail.type" placeholder="请选择" @change="selectDB">
          <el-option v-for="item in options" :key="item.label" :label="item.label" :value="item.value">
            <db-icon :type="item.value"></db-icon>
            <span>{{ item.label }}</span>
          </el-option>
        </el-select>

        <el-alert type="warning" show-icon v-show="detail.type == 'others'" :title="$t('m.ds_driver_tip')" style="margin-top: 10px;">

        </el-alert>
      </el-form-item>
      <el-form-item :label="$t('m.name')">
        <el-input v-model="detail.name"></el-input>
      </el-form-item>
      <el-form-item :label="$t('m.note')">
        <el-input v-model="detail.note"></el-input>
      </el-form-item>
      <el-form-item :label="$t('m.jdbc_driver_class')">
        <el-input v-model="detail.driver"></el-input>
      </el-form-item>
      <el-form-item label="Jdbc Url">
        <el-input v-model="detail.url" type="textarea" class="my"></el-input>
      </el-form-item>
      <el-form-item :label="$t('m.username')">
        <el-input v-model="detail.username"></el-input>
      </el-form-item>
      <el-form-item :label="$t('m.password')">
        <el-input v-model="detail.password" type="password"></el-input>
      </el-form-item>
      <el-form-item :label="$t('m.sql_query_all_table_name')">
        <el-input v-model="detail.tableSql"></el-input>
        <el-alert type="warning" :title="$t('m.ds_sql_tip')" show-icon style="margin-top: 10px;"></el-alert>
      </el-form-item>
    </el-form>

    <el-button type="primary" @click="connect" plain style="margin: 10px 0;">{{$t('m.test_connection')}}</el-button>
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
          url: 'jdbc:mysql://localhost:3306/<db>?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8',
          driver: 'com.mysql.cj.jdbc.Driver',
          sql: 'show tables'
        },
        postgresql: {
          url: 'jdbc:postgresql://localhost:5432/<db>',
          driver: 'org.postgresql.Driver',
          sql: 'SELECT table_name FROM information_schema.tables WHERE table_schema = \'public\' ORDER BY table_name'
        },
        hive: {
          url: 'jdbc:hive2://localhost:10000/<db>',
          driver: 'org.apache.hive.jdbc.HiveDriver',
          sql: 'show tables'
        },
        sqlserver: {
          url: 'jdbc:microsoft:sqlserver://localhost:1433;databaseName=<db>',
          driver: 'com.microsoft.sqlserver.jdbc.SQLServerDriver',
          sql: 'select * from sys.tables'
        },
        clickhouse: {
          url: 'jdbc:clickhouse://localhost:8123/<db>',
          driver: 'ru.yandex.clickhouse.ClickHouseDriver'
        },
        kylin: {
          url: 'jdbc:kylin://localhost:7070/<db>',
          driver: 'org.apache.kylin.jdbc.Driver'
        },
        oracle: {
          url: 'jdbc:oracle:thin:@localhost:1521:<db>',
          driver: 'oracle.jdbc.OracleDriver',
          sql: "SELECT OWNER || '.' || TABLE_NAME FROM ALL_TABLES"
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
        this.$message.error("failed")
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
          this.$message.success("Success")
        else
          this.$message.error(response.data.msg)
      }).catch((error) => {
        this.$message.error("Failed")
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
