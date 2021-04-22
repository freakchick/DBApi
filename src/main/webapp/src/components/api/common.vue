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
        <div style="display:flex">
          <el-select v-model="detail.datasourceId" placeholder="请选择" @change="getTables">
            <el-option v-for="item in datasources" :key="item.id" :label="item.name" :value="item.id">
              <db-icon :type="item.type"></db-icon>
              <span>{{ item.name }}</span>
              <!--            <span style="float: left">{{ item.name }}</span>-->
            </el-option>
          </el-select>
          <i class="el-icon-s-opportunity tip" @click="show=!show" v-if="$route.path != '/api/detail'"></i>
          <div v-show="show">
            <el-select placeholder="查看所有表" v-model="table" @change="getColumns" clearable>
              <el-option :value="item" v-for="item in tables"><i class="iconfont icon-table"></i>{{ item }}</el-option>
            </el-select>
            <el-select placeholder="查看所有字段" v-model="column" style="margin-left: 10px" clearable>
              <el-option :value="item.fieldName" v-for="item in columns">
                <i class="iconfont icon-ziyuan"></i>{{ item.fieldName }}
              </el-option>
            </el-select>
          </div>
        </div>
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
          <el-tag size="mini" @click="tag('where')" effect="plain">where</el-tag>
          <el-tag size="mini" @click="tag('trim')" effect="plain">trim</el-tag>
        </div>
        <el-input type="textarea" v-model="detail.sql" :autosize="{ minRows: 5, maxRows: 20 }" placeholder="请输入sql"
                  class="my"></el-input>
        <!--        <el-button type="primary" plain @click="parseParams" style="margin :10px 0">解析参数</el-button>-->
      </el-form-item>
      <el-form-item label="请求参数">
        <div v-for="(item,index) in detail.params" style="margin-bottom:5px;display: flex;align-items:center">
          <el-autocomplete v-model="item.name" :fetch-suggestions="parseParams" style="width:200px;margin-right:5px"
                           placeholder="*参数名"></el-autocomplete>
          <el-select v-model="item.type" :options="options" placeholder="*数据类型" style="margin-right:5px">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
          <el-input v-model="item.note" placeholder="参数说明" style="width:200px;margin-right:5px"></el-input>
          <!--          <el-cascader  v-model="item.type" separator=" > " :options="options"></el-cascader>-->

          <el-button @click="deleteRow(index)" circle type="danger" icon="el-icon-delete" size="mini"
                     v-if="$route.path != '/api/detail'"></el-button>
        </div>
        <el-button @click="addRow" icon="el-icon-plus" type="primary" circle size="mini"
                   v-if="$route.path != '/api/detail'"></el-button>

      </el-form-item>

      <el-form-item label="API分组">
        <el-select v-model="detail.group">
          <el-option :label="item.name" :value="item.id" v-for="item in groups" :key="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="访问权限">
        <el-radio-group v-model="detail.previlege">
          <el-radio :label="0">私有接口</el-radio>
          <el-radio :label="1">开放接口</el-radio>
        </el-radio-group>
      </el-form-item>


    </el-form>
  </div>
</template>

<script>
import dbIcon from "@/components/common/dbIcon";

export default {
  data() {
    return {
      datasources: [],
      address: null,
      show: false,
      groups:[],
      detail: {
        datasourceId: null,
        name: null,
        note: null,
        path: null,
        isSelect: "1",
        sql: '',
        params: [],
        group:null,
        previlege:0
      },
      options: [
        {label: 'string', value: 'string'},
        {label: 'bigint', value: 'bigint'},
        {label: 'double', value: 'double'},
        {label: 'date', value: 'date'},

        {label: 'string 数组', value: 'list<string>'},
        {label: 'bigint 数组', value: 'list<bigint>'},
        {label: 'double 数组', value: 'list<double>'},
        {label: 'date 数组', value: 'list<date>'}

      ],
      table: null, tables: [], columns: [], column: null
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
        this.detail.previlege = response.data.previlege
        this.detail.group = response.data.group
        this.detail.params = JSON.parse(response.data.params)
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    tag(item) {
      if (item == 'foreach') {
        this.detail.sql += "\n<foreach open=\"(\" close=\")\" collection=\"\" separator=\",\" item=\"item\" index=\"index\">#{item}</foreach>"
      } else if (item == 'if') {
        this.detail.sql += "\n<if test=\"\" ></if>"
      } else if (item == 'where') {
        this.detail.sql += "\n<where></where>"
      } else if (item == 'trim') {
        this.detail.sql += "\n<trim prefix=\"\" suffix=\"\" suffixesToOverride=\"\" prefixesToOverride=\"\"></trim>"
      }
    },
    getTables() {
      this.axios.post("/table/getAllTables", {sourceId: this.detail.datasourceId}).then((response) => {
        this.tables = response.data
      }).catch((error) => {
        this.$message.error("查询所有表失败")
      })
    },
    getColumns() {
      this.axios.post("/table/getAllColumns", {
        sourceId: this.detail.datasourceId,
        table: this.table
      }).then((response) => {
        console.log(response.data)
        this.columns = response.data
      }).catch((error) => {
        this.$message.error("查询所有表失败")
      })
    },
    getAllGroups() {
      this.axios.post("/group/getAll/").then((response) => {
        this.groups = response.data
      }).catch((error) => {
      })
    }
  },
  created() {
    this.getAllSource()
    this.getAddress()
    if (this.id != undefined)
      this.getDetail(this.id)

    this.getAllGroups()
    console.log(this.$route.path)
  },
  components: {dbIcon}
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
  margin-left: 4px;
  margin-bottom: 2px;
}

.el-tag:hover {
  font-weight: 700;
  background-color: #ace2f8;
}

.tip {
  font-weight: 300;
  color: #8c939d;
  line-height: 38px;
  margin: 0 5px;
}

.tip:hover {
  font-weight: 700;
}

</style>
