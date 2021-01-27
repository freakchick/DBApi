<template>
  <div>
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">返回</el-button>
    <h2>api详情</h2>
    <el-form label-width="100px">
      <el-form-item label="api名称">
        <el-input v-model="detail.name"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="detail.note"></el-input>
      </el-form-item>
      <el-form-item label="请求地址">
        <el-input v-model="'http://'+address+'/api/'+detail.path" class="my" placeholder="输入请求路径">

        </el-input>
      </el-form-item>

      <el-form-item label="数据源">
        <el-select v-model="detail.datasourceId" placeholder="请选择">
          <el-option v-for="item in datasources" :key="item.id" :label="item.name" :value="item.id">
            <span style="float: left">{{ item.name }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="sql">
        <el-input type="textarea" v-model="detail.sql" :autosize="{ minRows: 5, maxRows: 20 }" placeholder="请输入sql"
                  class="my"></el-input>
      </el-form-item>
      <el-form-item label="请求参数">
        <el-tag type="success" v-for="(item,index) in params" :key="index" effect="dark" class="tag">{{item.name}}</el-tag>
      </el-form-item>


    </el-form>

  </div>
</template>

<script>
export default {
  name: "detail",
  data() {
    return {
      detail: null,
      datasources: [],
      address: null,
      params: []
    }
  },
  methods: {

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
      this.axios.post("/apiConfig/detail/" + id).then((response) => {
        this.detail = response.data
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
