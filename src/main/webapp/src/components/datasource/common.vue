<template>
  <div>

    <el-form label-width="100px">
      <el-form-item label="数据库">
        <el-select v-model="detail.type" placeholder="请选择" @change="selectDB">
          <el-option v-for="item in options" :key="item.label" :label="item.label" :value="item.label">
            <i class="iconfont icon-my-SQL db" v-if="item.label == 'mysql'"></i>
            <i class="iconfont icon-postgre-sql db" v-if="item.label == 'postgresql'"></i>
            <i class="iconfont icon-hive db" v-if="item.label == 'hive'"></i>
            <i class="iconfont icon-SQLServer db" v-if="item.label == 'sqlserver'"></i>
            <i class="iconfont icon-clickhouse2 db" v-if="item.label == 'clickhouse'"></i>
            <i class="iconfont icon-Kylin db" v-if="item.label == 'kylin'"></i>
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
      <el-form-item label="url">
        <el-input v-model="detail.url" type="textarea" class="my"></el-input>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="detail.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="detail.password"></el-input>
      </el-form-item>
    </el-form>

    <el-button type="primary" @click="connect" plain style="margin: 10px 0;">连接测试</el-button>
  </div>
</template>

<script>
export default {
  name: "common",
  data() {
    return {
      options: [{label: 'mysql'}, {label: 'postgresql'}, {label: 'hive'},
        {label: 'sqlserver'}, {label: 'clickhouse'}, {label: 'kylin'}],
      detail: {url: null, name: null, note: null, type: null, username: null, password: null}
    }

  },
  props: ["id"],
  methods: {
    selectDB() {
      if (this.detail.type == 'mysql') {
        this.detail.url = 'jdbc:mysql://localhost:3306/db?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8'
      } else if (this.detail.type == 'postgresql') {
        this.detail.url = 'jdbc:postgresql://localhost:5432/db'
      } else if (this.detail.type == 'hive') {
        this.detail.url = 'jdbc:hive2://localhost:10000/db'
      } else if (this.detail.type == 'sqlserver') {
        this.detail.url = 'jdbc:microsoft:sqlserver://localhost:1433;databaseName=db'
      }else if (this.detail.type == 'clickhouse') {
        this.detail.url = 'jdbc:clickhouse://localhost:8123/db'
      }else if (this.detail.type == 'kylin') {
        this.detail.url = 'jdbc:kylin://localhost:7070/db'
      }


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
        "type": this.detail.type
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
  }
}
</script>

<style scoped>
.db {
  color: #0698a5;
  font-size: 24px;
  font-weight: 500;
  margin-right: 5px;
}

.my >>> .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
}
</style>
