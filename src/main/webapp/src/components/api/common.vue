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
      <el-form-item label="sql类型">
        <el-select v-model="detail.isSelect">
          <el-option label="查询类" value=1></el-option>
          <el-option label="非查询类" value=0></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="sql">
        <div v-show="$route.path != '/api/detail'" class="tag">
          <el-tag size="mini" @click="tag('foreach')" effect="plain">foreach</el-tag>
          <el-tag size="mini" @click="tag('if')" effect="plain">if</el-tag>
        </div>
        <el-input type="textarea" v-model="detail.sql" :autosize="{ minRows: 5, maxRows: 20 }" placeholder="请输入sql" class="my"></el-input>
        <!--        <el-button type="primary" plain @click="parseParams" style="margin :10px 0">解析参数</el-button>-->
      </el-form-item>
      <el-form-item label="请求参数">
        <div v-for="(item,index) in detail.params" style="margin-bottom:5px">
          <el-autocomplete v-model="item.name" :fetch-suggestions="parseParams" style="width:200px;margin-right:5px" placeholder="请输入参数名"></el-autocomplete>
          <el-select v-model="item.type" :options="options" placeholder="请选择数据类型">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>

          <!--          <el-cascader  v-model="item.type" separator=" > " :options="options"></el-cascader>-->

          <el-button @click="deleteRow(index)" circle type="danger" icon="el-icon-delete" size="mini" v-if="$route.path != '/api/detail'"></el-button>
        </div>
        <el-button @click="addRow" icon="el-icon-plus" type="primary" circle size="mini" v-if="$route.path != '/api/detail'"></el-button>

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
        isSelect: "1",
        sql: '',
        params: []
      },
      options: [
        {label: 'string', value: 'string'},
        {label: 'bigint', value: 'bigint'},
        {label: 'decimal', value: 'decimal'},
        {label: 'date', value: 'date'},

        {label: 'string 数组', value: 'list<string>'},
        {label: 'bigint 数组', value: 'list<bigint>'},
        {label: 'decimal 数组', value: 'list<decimal>'},
        {label: 'date 数组', value: 'list<date>'}

      ]
    }
  },
  props: ["id"],
  methods: {
    addRow() {
      this.detail.params.push({name: null, type: null})
    },
    deleteRow(index) {
      this.detail.params.splice(index, 1)
    },
    getAllSource() {
      this.axios.post("/datasource/getAll").then((response) => {
        this.datasources = response.data
      }).catch((error) => {
        this.$message.error("查询所有数据源失败")
      })
    },
    parseParams(queryString, cb) {
      this.axios.post("/apiConfig/parseParam", {sql: this.detail.sql}).then((response) => {
        if (response.data.success) {
          console.log(response.data.data[0])
          cb(response.data.data)
        } else {
          this.$message.error(response.data.msg)
          cb([])
        }
      }).catch((error) => {
        // this.$message.error("失败")
        cb([])
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
        this.detail.isSelect = response.data.isSelect.toString()
        this.detail.datasourceId = response.data.datasourceId
        this.detail.params = JSON.parse(response.data.params)
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    tag(item) {
      if (item == 'foreach') {
        this.detail.sql += "\n<foreach open=\"(\" close=\")\" collection=\"\" separator=\",\" item=\"item\" index=\"index\">#{item}</foreach>"
      }
      if (item == 'if') {
        this.detail.sql += "\n<if test=\"\" ></if>"
      }
    }
  },
  created() {
    this.getAllSource()
    this.getAddress()
    if (this.id != undefined)
      this.getDetail(this.id)
    console.log(this.$route.path)
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
.el-tag {
  float: right;
  margin-right: 5px;
  margin-bottom: 2px;
}
</style>
