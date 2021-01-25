<template>
  <div>
    <h2>创建api</h2>
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
        <el-input type="textarea" v-model="sql" :autosize="{ minRows: 5, maxRows: 20 }" placeholder="请输入sql" class="my"></el-input>
        <el-button @click="parseParams" style="margin :10px 0">解析参数</el-button>
      </el-form-item>
      <el-form-item label="请求参数">
        <el-table :data="params" size="mini" border stripe>
          <el-table-column prop="name" label="参数名称"></el-table-column>
          <el-table-column label="数据类型">
            <template slot-scope="scope">
              <el-select v-model="scope.row.type">
                <el-option label="string" value="string"></el-option>
                <el-option label="number" value="number"></el-option>
                <el-option label="date" value="date"></el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>


    </el-form>
    <el-button @click="save">保存</el-button>

  </div>
</template>

<script>
export default {
  name: "add",
  data() {
    return {
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
      this.axios.post("/apiConfig/parseParam", {sql: this.sql}).then((response) => {
        this.params = response.data
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    save() {
      this.axios.post("/apiConfig/add", {
        name: this.name,
        note: this.note,
        path: this.path,
        datasourceId: this.datasourceId,
        sql: this.sql,
        params: JSON.stringify(this.params)
      }).then((response) => {
        this.$message.success("保存成功")
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
    }

  },
  created() {
    this.getAllSource()
    this.getAddress()
  }
}
</script>

<style scoped>
.my >>> .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
  /*font-size: 18px;*/
}

h2{
  margin-bottom : 25px;
}

h4{
  margin: 10px 0;
}
</style>
