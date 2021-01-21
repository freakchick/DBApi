<template>
  <div>
    <el-input type="textarea" v-model="sql" placeholder="请输入sql"></el-input>
    <el-button>解析参数</el-button>

    <h4>请求参数：</h4>
    <el-table :data="params">
      <el-table-column prop="name" label="参数名称"></el-table-column>
      <!--      <el-table-column prop="url" label="地址"></el-table-column>-->
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

  </div>
</template>

<script>
  export default {
    name: "add",
    data() {
      return {
        sql: null,
        params: []
      }
    },
    methods: {
      parseParams() {
        this.axios.post("/apiConfig/parseParam").then((response) => {
          this.params = response.data
        }).catch((error) => {
          this.$message.error("查询所有数据源失败")
        })
      }

    }
  }
</script>

<style scoped>

</style>
