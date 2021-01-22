<template>
  <div>
    <el-form label-width="100px">
      <el-form-item label="api名称">
        <el-input v-model="name"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="note"></el-input>
      </el-form-item>
      <el-form-item label="请求路径">
        <el-input v-model="path" class="my">
          <template slot="prepend">http://ip:port/api/</template>
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
        <el-input type="textarea" v-model="sql" placeholder="请输入sql"></el-input>
        <el-button @click="parseParams">解析参数</el-button>
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
      datasources: []
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
    }

  },
  created() {
    this.getAllSource()
  }
}
</script>

<style scoped>

</style>
