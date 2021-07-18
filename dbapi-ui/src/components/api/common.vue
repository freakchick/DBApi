<template>
  <div>
    <el-form label-width="100px">
      <el-form-item label="API基本信息">
        <my-input label="名称" :nullable="false" v-model="detail.name"></my-input>
        <my-input label="请求路径" v-model="detail.path" :preffix="`http://${ address }/api/`" :nullable="false"
                  width="400px"></my-input>
        <my-select v-model="detail.groupId" :options="groups" label="API分组" option_label="name"
                   option_value="id" :nullable="false"></my-select>
        <my-input label="描述" v-model="detail.note" width="500px"></my-input>
      </el-form-item>

      <el-form-item label="sql">
        <div>
          <sql-code ref="sqlCode"></sql-code>
        </div>
      </el-form-item>
      <el-form-item label="请求参数">
        <div v-for="(item,index) in detail.params" style="margin-bottom:5px;display: flex;align-items:center">
          <el-autocomplete v-model="item.name" :fetch-suggestions="parseParams" style="width:200px;margin-right:5px"
                           placeholder="*参数名称"></el-autocomplete>
          <el-select v-model="item.type" :options="options" placeholder="*数据类型" style="margin-right:5px">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
          <el-input v-model="item.note" placeholder="参数说明" style="width:400px;margin-right:5px"></el-input>
          <!--          <el-cascader  v-model="item.type" separator=" > " :options="options"></el-cascader>-->

          <el-button @click="deleteRow(index)" circle type="danger" icon="el-icon-delete" size="mini"
                     v-if="$route.path != '/api/detail'"></el-button>
        </div>
        <el-button @click="addRow" icon="el-icon-plus" type="primary" circle size="mini"
                   v-if="$route.path != '/api/detail'"></el-button>

      </el-form-item>

      <el-form-item label="访问权限">
        <el-radio-group v-model="detail.previlege">
          <el-radio :label="0">私有接口</el-radio>
          <el-radio :label="1">开放接口</el-radio>
        </el-radio-group>

        <el-tooltip placement="top-start" effect="light">
          <div slot="content">开放接口可以直接访问<br/>

            私有接口在访问时必须在请求头中携带token，且该token值对此接口有访问权限，具体请到权限菜单查看
          </div>
          <i class="el-icon-info tip"></i>
        </el-tooltip>
      </el-form-item>

      <el-form-item label="数据转换">
        <div style="width: 400px;display: inline-block">
          <el-input v-model="detail.transformPlugin" placeholder="填写数据转换插件java类名" clearable></el-input>
        </div>
        <el-tooltip placement="top-start" effect="light">
          <div slot="content">填写表示开启数据转换功能，不填写表示不转换。转换逻辑是自定义插件中编写的逻辑</div>
          <i class="el-icon-info tip"></i>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="缓存">
        <div style="width: 400px;display: inline-block">
          <el-input v-model="detail.cachePlugin" placeholder="填写缓存插件java类名" clearable></el-input>
        </div>
        <el-tooltip placement="top-start" effect="light">
          <div slot="content">填写表示对结果数据开启缓存，不填写表示不开启缓存</div>
          <i class="el-icon-info tip"></i>
        </el-tooltip>
      </el-form-item>


    </el-form>

  </div>
</template>

<script>
import dbIcon from "@/components/common/dbIcon";
import sqlCode from "@/components/api/common/SqlCode";

export default {
  data() {
    return {
      datasources: [],
      address: null,
      show: false,
      groups: [{label: '姓名', value: 'name'}],
      dialogVisible: false,
      detail: {
        name: null,
        note: null,
        path: null,
        params: [],
        groupId: null,
        previlege: 0,//访问权限
        transformPlugin: null,
        cachePlugin: null
      },
      options: [
        {label: 'string', value: 'string'},
        {label: 'bigint', value: 'bigint'},
        {label: 'double', value: 'double'},
        {label: 'date', value: 'date'},

        {label: 'string 数组', value: 'Array<string>'},
        {label: 'bigint 数组', value: 'Array<bigint>'},
        {label: 'double 数组', value: 'Array<double>'},
        {label: 'date 数组', value: 'Array<date>'}

      ],
      table: null, tables: [], columns: [], column: null,
      isFullScreen: false,
      mode: 'mini'
    }
  },
  props: ["id"],
  methods: {

    addRow() {
      this.detail.params.push({name: null, type: null})
    },
    deleteRow(index) {
      debugger
      this.detail.params.splice(index, 1)
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
        this.detail.previlege = response.data.previlege
        this.detail.groupId = response.data.groupId
        this.detail.params = JSON.parse(response.data.params)
        this.detail.cachePlugin = response.data.cachePlugin
        this.detail.transformPlugin = response.data.transformPlugin


        console.log(this.$refs.sqlCode.datasourceId, response.data.datasourceId)
        this.$refs.sqlCode.datasourceId = response.data.datasourceId
        this.$refs.sqlCode.codemirror.setValue(response.data.sql)
      })

      /*   .catch((error) => {
       this.$message.error("")
     })*/
    },

    getAllGroups() {
      this.axios.post("/group/getAll/").then((response) => {
        this.groups = response.data
      }).catch((error) => {
      })
    }
  },
  created() {
    this.getAddress()
    if (this.id != undefined)
      this.getDetail(this.id)

    this.getAllGroups()
  },
  components: {dbIcon, sqlCode}
}
</script>

<style scoped>

.my >>> .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
  /*font-size: 18px;*/
}

.mydialog >>> .el-dialog {
  margin-top: 20px !important;
  margin-bottom: 0px !important;

}

i {
  color: #0698a5;
  font-size: 18px;
  font-weight: 700;
  margin-right: 5px;
}

.tip {
  /*display: inline-block !important;*/
  margin-left: 10px;
  /*background-color: #fdf6ec;*/
  /*padding: 15px;*/
  color: #afafaf;
  font-size: 20px;
  font-weight: 100;

}


</style>
