<template>
  <div>
    <el-form label-width="100px">
      <el-form-item label="api名称">
        <el-input v-model="detail.name"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="detail.note"></el-input>
      </el-form-item>
      <el-form-item label="请求路径">
        <el-input v-model="detail.path" class="my" placeholder="输入请求路径">
          <template slot="prepend">
            <span style="margin: 0 -14px">http://{{ address }}/api/</span>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item label="数据源">
        <el-select v-model="detail.datasourceId" placeholder="请选择">
          <el-option v-for="item in datasources" :key="item.id" :label="item.name" :value="item.id">
            <i class="iconfont icon-my-SQL" v-if="item.type == 'mysql'"></i>
            <i class="iconfont icon-postgre-sql" v-if="item.type == 'postgresql'"></i>
            <i class="iconfont icon-hive" v-if="item.type == 'hive'"></i>
            <i class="iconfont icon-SQLServer" v-if="item.type == 'sqlserver'"></i>
            <span>{{ item.name }}</span>
            <!--            <span style="float: left">{{ item.name }}</span>-->
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="sql">
        <el-input type="textarea" v-model="detail.sql" :autosize="{ minRows: 5, maxRows: 20 }" placeholder="请输入sql" class="my"></el-input>
        <el-button type="primary" plain @click="parseParams" style="margin :10px 0">解析参数</el-button>
      </el-form-item>
      <el-form-item label="请求参数">

        <el-table :data="detail.params" border stripe max-height="700" size="mini">
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column prop="type" label="类型">
            <template slot-scope="scope">
              <el-select v-model="scope.row.type" placeholder="请选择">
                <el-option label="string" value="string"></el-option>
                <el-option label="double" value="double"></el-option>
                <el-option label="bigint" value="bigint"></el-option>
                <el-option label="date" value="date"></el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>

      </el-form-item>


    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      datasources: [],
      address: null,
      detail: {
        datasourceId: null,
        name: null,
        note: null,
        path: null,
        sql: 'select name,age from user where id > $minId and id < $maxId',
        params: []
      }
    }
  },
  props: ["id"],
  methods: {
    getAllSource() {
      this.axios.post("/datasource/getAll").then((response) => {
        this.datasources = response.data
      }).catch((error) => {
        this.$message.error("查询所有数据源失败")
      })
    },
    parseParams() {
      if (this.detail.datasourceId == null) {
        this.$message.warning("请先选择数据源，解析sql参数需要获取数据库类型")
        return
      }
      this.axios.post("/apiConfig/parseParam", {sql: this.detail.sql, datasourceId: this.detail.datasourceId}).then((response) => {
        if (response.data.success) {
          this.detail.params = response.data.data
        } else {
          this.$message.error(response.data.msg)
        }
      }).catch((error) => {
        this.$message.error("失败")
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
        this.detail.name = response.data.name
        this.detail.note = response.data.note
        this.detail.path = response.data.path
        this.detail.sql = response.data.sql
        this.detail.datasourceId = response.data.datasourceId
        this.detail.params = JSON.parse(response.data.params)
      }).catch((error) => {
        this.$message.error("失败")
      })
    }
  },
  created() {
    this.getAllSource()
    this.getAddress()
    if (this.id != undefined)
      this.getDetail(this.id)
  }
}
</script>

<style scoped>
.my >>> .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
  /*font-size: 18px;*/
}

i {
  color: #0698a5;
  font-size: 18px;
  font-weight: 700;
  margin-right: 5px;
}
</style>
