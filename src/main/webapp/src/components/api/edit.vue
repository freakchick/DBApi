<template>
  <div>
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">返回</el-button>
    <h2>修改api</h2>
    <el-form label-width="100px">
      <el-form-item label="api名称">
        <el-input v-model="name"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="note"></el-input>
      </el-form-item>
      <el-form-item label="请求路径">
        <el-input v-model="path" class="my" placeholder="输入请求路径">
          <template slot="prepend">
            <span style="margin: 0 -14px">http://{{ address }}/api/</span>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item label="数据源">
        <el-select v-model="datasourceId" placeholder="请选择">
          <el-option v-for="item in datasources" :key="item.id" :label="item.name" :value="item.id">
            <span style="float: left">{{ item.name }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="sql">
        <el-input type="textarea" v-model="sql" :autosize="{ minRows: 5, maxRows: 20 }" placeholder="请输入sql"
                  class="my"></el-input>
        <el-button @click="parseParams" style="margin :10px 0">解析参数</el-button>
      </el-form-item>
      <el-form-item label="请求参数">
        <el-tag type="success" v-for="(item,index) in params" :key="index" effect="dark" class="tag">{{item.name}}</el-tag>
      </el-form-item>


    </el-form>
    <el-button @click="save">保存</el-button>

  </div>
</template>

<script>
export default {
  name: "edit",
  data() {
    return {
      id: null,
      datasourceId: null,
      name: null,
      note: null,
      path: null,
      sql: 'select name,age from user where id > $minId and id < $maxId',
      params: [],
      datasources: [],
      address: null
    }
  },
  methods: {
    parseParams() {
      if (this.datasourceId == null) {
        this.$message.warning("请先选择数据源，解析sql参数需要获取数据库类型")
        return
      }
      this.axios.post("/apiConfig/parseParam", {sql: this.sql, datasourceId: this.datasourceId}).then((response) => {
        if (response.data.success) {
          this.params = response.data.data
        } else {
          this.$message.error(response.data.msg)
        }
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    save() {
      this.axios.post("/apiConfig/update", {
        id: this.id,
        name: this.name,
        note: this.note,
        path: this.path,
        datasourceId: this.datasourceId,
        sql: this.sql,
        params: JSON.stringify(this.params)
      }).then((response) => {
        if (response.data.success) {
          this.$message.success(response.data.msg)
        } else {
          this.$message.error(response.data.msg)
        }
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    getAllSource() {
      this.axios.post("/datasource/getAll").then((response) => {
        this.datasources = response.data
      }).catch((error) => {
        this.$message.error("查询所有数据源失败")
      })
    },
    getAddress() {
      this.axios.post("/apiConfig/getIPPort").then((response) => {
        this.address = response.data
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    getDetail(id) {
      this.id = id
      this.axios.post("/apiConfig/detail/" + id).then((response) => {
        this.name = response.data.name
        this.note = response.data.note
        this.path = response.data.path
        this.sql = response.data.sql
        this.datasourceId = response.data.datasourceId
        this.params = JSON.parse(response.data.params)
        console.log(this.params)
      }).catch((error) => {
        this.$message.error("失败")
      })
    }

  },
  created() {
    this.getAllSource()
    this.getAddress()
    this.getDetail(this.$route.query.id)
  }
}
</script>

<style scoped>
.my >>> .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
  /*font-size: 18px;*/
}

h2 {
  margin-bottom: 25px;
  text-align: center;
}

h4 {
  margin: 10px 0;
}

.tag{
  margin-right: 10px;
  font-width: 700;
}
</style>
