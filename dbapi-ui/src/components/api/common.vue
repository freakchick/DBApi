<template>
  <div>
    <el-tabs tab-position="top" style="" type="border-card">
      <el-tab-pane label="基础配置">
        <el-form label-width="100px">
          <el-form-item :label="$t('m.basic_info')">
            <my-input :label="$t('m.name')" :nullable="false" v-model="detail.name"></my-input>
            <my-input :label="$t('m.path')" v-model="detail.path" :preffix="`http://${ address }/`"
                      :nullable="false"
                      width="400px"></my-input>
            <my-select v-model="detail.groupId" :options="groups" :label="$t('m.api_group')" option_label="name"
                       option_value="id" :nullable="false"></my-select>
            <my-input :label="$t('m.note')" v-model="detail.note" width="500px"></my-input>
          </el-form-item>

          <el-form-item label="sql">
            <div>
              <sql-code ref="sqlCode" :apiSql="detail.sqlList"></sql-code>
            </div>
          </el-form-item>
          <el-form-item :label="$t('m.parameters')">
            <div v-for="(item,index) in detail.params" style="margin-bottom:5px;display: flex;align-items:center">
              <el-autocomplete v-model="item.name" :fetch-suggestions="parseParams" style="width:200px;margin-right:5px"
                               placeholder="*参数名称"></el-autocomplete>
              <el-select v-model="item.type" :options="options" placeholder="*数据类型" style="margin-right:5px">
                <el-option v-for="item in options" :key="item.value" :label="item.label"
                           :value="item.value"></el-option>
              </el-select>
              <el-input v-model="item.note" placeholder="参数说明" style="width:400px;margin-right:5px"></el-input>
              <!--          <el-cascader  v-model="item.type" separator=" > " :options="options"></el-cascader>-->

              <el-button @click="deleteRow(index)" circle type="danger" icon="el-icon-delete" size="mini"
                         v-if="$route.path != '/api/detail'"></el-button>
            </div>
            <el-button @click="addRow" icon="el-icon-plus" type="primary" circle size="mini"
                       v-if="$route.path != '/api/detail'"></el-button>

          </el-form-item>

          <el-form-item :label="$t('m.access')">
            <el-radio-group v-model="detail.previlege">
              <el-radio :label="0">{{ $t('m.private') }}</el-radio>
              <el-radio :label="1">{{ $t('m.public') }}</el-radio>
            </el-radio-group>

            <el-tooltip placement="top-start" effect="dark">
              <div slot="content">
                {{ $t('m.access_tip') }}
              </div>
              <i class="el-icon-info tip"></i>
            </el-tooltip>
          </el-form-item>


        </el-form>
      </el-tab-pane>
      <el-tab-pane label="高级配置">
        <el-form label-width="100px">
          <el-form-item :label="$t('m.data_convert')">
            <div v-for="(item,index) in this.$store.state.sqls">
              <span>sql-{{ item.id }} : </span>
              <my-input :label="$t('m.plugin_class')" v-model="item.transformPlugin" placeholder="填写数据转换插件java类名"
                        width="400px"></my-input>
              <my-input :label="$t('m.plugin_parameter')" v-model="item.transformPluginParams" width="300px"></my-input>
            </div>


            <el-alert type="warning" show-icon>
              填写“插件类名”表示对sql执行结果开启数据转换功能，不填写表示不转换。
              如果有多条sql，每个sql对应一个数据转换插件
            </el-alert>

          </el-form-item>
          <el-form-item :label="$t('m.cache')">
            <el-tooltip placement="top-start" effect="dark">
              <!--              <div slot="content"></div>
                            <i class="el-icon-info tip"></i>-->
            </el-tooltip>
            <my-input :label="$t('m.plugin_class')" v-model="detail.cachePlugin" placeholder="填写缓存插件java类名"
                      width="400px"></my-input>
            <my-input :label="$t('m.plugin_parameter')" v-model="detail.cachePluginParams" width="300px"></my-input>
            <el-alert type="warning" show-icon>
              填写“插件类名”表示对结果数据开启缓存，不填写表示不开启缓存
            </el-alert>
            <div>
              <a class="el-icon-question" target="_blank"
                 href="https://gitee.com/freakchicken/db-api/blob/master/dbapi-assembly/docs/instruction.md#%E6%8F%92%E4%BB%B6">{{
                  $t('m.what_is_plugin')
                }}</a>
              <a class="el-icon-question" target="_blank"
                 href="https://gitee.com/freakchicken/db-api/blob/master/dbapi-assembly/docs/plugin%20development.md#252-%E5%B1%80%E9%83%A8%E5%8F%82%E6%95%B0">{{
                  $t('m.what_is_plugin_param')
                }}</a>
            </div>
          </el-form-item>
        </el-form>
      </el-tab-pane>

    </el-tabs>


  </div>
</template>

<script>
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
        cachePlugin: null,
        cachePluginParams: null,
        sqlList: [{sqlText: '', transformPlugin: null, transformPluginParams: null}] //默认空字符串，当创建API的时候，默认打开一个标签
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
        // this.$message.error("失败")
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
        this.detail.cachePluginParams = response.data.cachePluginParams


        this.$refs.sqlCode.datasourceId = response.data.datasourceId

        this.detail.sqlList = response.data.sqlList.map(t => {
          return {
            sqlText: t.sqlText,
            transformPlugin: t.transformPlugin,
            transformPluginParams: t.transformPluginParams
          }
        })
      })
    },

    getAllGroups() {
      this.axios.post("/group/getAll/").then((response) => {
        this.groups = response.data
      }).catch((error) => {
      })
    }
  },
  mounted() {
    console.log('mount----+++')
    this.getAddress()
    if (this.id != undefined) {
      console.log('edit api page')
      this.getDetail(this.id)
    }
    else {
      console.log('add api page')
      this.$store.commit('initSqls', [{sqlText: '-- 请输入sql', transformPlugin: null, transformPluginParams: null}])
    }
    this.getAllGroups()
  },
  // mounted() {
    // console.log('mount----+++')
  // },
  components: {sqlCode}
}
</script>

<style scoped lang="scss">

.my > > > .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
  /*font-size: 18px;*/
}

.mydialog > > > .el-dialog {
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

a {
  font-size: 16px;
  color: #afafaf;
  margin: 0 5px;

  &:hover {
    color: #000000;
  }
}

</style>
