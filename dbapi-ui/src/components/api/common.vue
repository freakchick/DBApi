<template>
  <div>
    <el-tabs tab-position="top" style="" type="border-card">
      <el-tab-pane label="基础配置">
        <el-form label-width="120px">
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

          <el-form-item label="Content-Type">
            <el-select v-model="detail.contentType" style="width:300px">
              <el-option v-for="item in types" :label="item" :value="item"></el-option>
            </el-select>
            <el-tooltip placement="top-start" effect="dark">
              <div slot="content">
                <p>对于application/x-www-form-urlencoded类型的API，用户在请求该API的时候既可以使用application/x-www-form-urlencoded，也可以使用application/json</p>
                <p>对于application/json类型的API，用户在请求该API的时候只能使用application/json</p>
              </div>
              <i class="el-icon-info tip"></i>
            </el-tooltip>

          </el-form-item>

          <el-form-item label="参数">
            <div slot="label">
              <span v-show="detail.contentType == 'application/x-www-form-urlencoded'">请求参数</span>
              <span v-show="detail.contentType == 'application/json'">请求参数示例</span>
            </div>
            <div v-show="detail.contentType == 'application/x-www-form-urlencoded'">

              <el-table :data="detail.params" border stripe max-height="700" size="mini" empty-text="暂无参数">
                <el-table-column prop="name" label="参数名称" width="220px">
                  <template slot-scope="scope">
                    <el-autocomplete v-model="scope.row.name" :fetch-suggestions="parseParams"></el-autocomplete>
                  </template>
                </el-table-column>
                <el-table-column label="参数类型" width="220px">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.type" :options="options">
                      <el-option v-for="item in options" :key="item.value" :label="item.label"
                                 :value="item.value"></el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="参数说明" width="300px">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.note"></el-input>
                  </template>
                </el-table-column>
                <!--                <el-table-column label="默认值" width="300px">
                                  <template slot-scope="scope">
                                    <el-input v-model="scope.row.defaultValue"></el-input>
                                  </template>
                                </el-table-column>-->
                <el-table-column label="操作" width="100px">
                  <template slot-scope="scope">
                    <el-button @click="deleteRow(scope.$index)" circle type="danger" icon="el-icon-delete"
                               size="mini"></el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-button @click="addRow" icon="el-icon-plus" type="primary" circle size="mini"></el-button>
            </div>
            <div v-show="detail.contentType == 'application/json'" class="textarea">
              <el-input v-model="detail.jsonParam" placeholder="填写json参数示例，用于生成接口文档" type="textarea"
                        rows="6"></el-input>
              <el-tooltip placement="top-start" effect="dark">
                <div slot="content">
                  对于application/json类型的API，这个参数示例仅用来生成接口文档，方便调用API的用户查看接口的json参数格式
                </div>
                <i class="el-icon-info tip"></i>
              </el-tooltip>
            </div>

          </el-form-item>

          <el-form-item label="权限">
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
        <el-form label-width="100px" label-position="left">
          <el-form-item label="事务">
            <el-radio-group v-model="detail.openTrans">
              <el-radio :label="1">开启</el-radio>
              <el-radio :label="0">关闭</el-radio>
            </el-radio-group>
            <el-tooltip placement="top-start" effect="dark">
              <div slot="content">
                开启事务后，如果有多条SQL，多条SQL将在同一个事务内执行
              </div>
              <i class="el-icon-info tip"></i>
            </el-tooltip>
            <el-alert type="warning" show-icon v-show="detail.openTrans == 1">

              <p>注意如果是hive等不支持事务的数据库，不要开启事务</p>
            </el-alert>
          </el-form-item>
          <el-form-item :label="$t('m.data_convert')">
            <div v-for="(item,index) in this.$store.state.sqls">
              <span>sql-{{ item.label }} : </span>
              <span class="label">插件类名</span>
              <el-select v-model="item.transformPlugin" style="width:400px" placeholder="请选择数据转换插件java类名" clearable
                         @clear="clearTransPluginParam(index)" no-data-text="暂无插件" >
                <el-option v-for="op in transformPlugins" :value="op"></el-option>
              </el-select>
              <span class="label">插件参数</span>
              <el-input placeholder="请输入数据转换插件参数" v-model="item.transformPluginParams" style="width:400px"></el-input>
            </div>


            <el-alert type="warning" show-icon>
              填写“插件类名”表示对sql执行结果开启数据转换功能，不填写表示不转换。
              如果有多条sql，每个sql对应一个数据转换插件
            </el-alert>

          </el-form-item>
          <el-form-item :label="$t('m.cache')">
            <span class="label">插件类名</span>
            <el-select v-model="detail.cachePlugin" style="width:400px" placeholder="请选择缓存插件java类名" clearable
                       @clear="clearCachePluginParam()" no-data-text="暂无插件" >
              <el-option v-for="item in cachePlugins" :value="item"></el-option>
            </el-select>
            <span class="label">插件参数</span>
            <el-input placeholder="请输入缓存插件参数" v-model="detail.cachePluginParams" style="width:400px"></el-input>
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
          <el-form-item label="失败邮件告警">
            <el-input v-model="detail.mail" placeholder="填写收件人邮箱，多个用英文分号隔开" style="width: 500px"></el-input>
            <el-tooltip placement="top-start" effect="dark">
              <div slot="content">
                不填写表示不需要失败邮件告警
              </div>
              <i class="el-icon-info tip"></i>
            </el-tooltip>

          </el-form-item>
        </el-form>
      </el-tab-pane>

    </el-tabs>


  </div>
</template>

<script>
import sqlCode from "@/components/api/common/SqlCode";
import MySelect from "../common/MySelect";

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
        jsonParam: null,
        sqlList: [{sqlText: '', transformPlugin: null, transformPluginParams: null}], //默认空字符串，当创建API的时候，默认打开一个标签
        contentType: 'application/x-www-form-urlencoded',
        openTrans: 0,
        mail: null
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
      mode: 'mini',
      types: ['application/x-www-form-urlencoded', 'application/json'],
      cachePlugins: [],
      transformPlugins: []
    }
  },
  props: ["id"],
  methods: {
    clearTransPluginParam(index) {
      this.$store.commit('clearTransformPluginParam', index)
    },
    clearCachePluginParam() {
      this.detail.cachePluginParams = null
    },

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
        this.detail.openTrans = response.data.openTrans
        this.detail.contentType = response.data.contentType
        this.detail.jsonParam = response.data.jsonParam
        this.detail.mail = response.data.mail

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
    },
    getAllPlugin() {
      this.axios.post("/plugin/all").then((response) => {
        this.cachePlugins = response.data.cache
        this.transformPlugins = response.data.transform
      }).catch((error) => {
      })
    }
  },
  mounted() {
    this.getAddress()
    //编辑页面
    if (this.id != undefined) {
      this.getDetail(this.id)
    }
    // 新增页面
    else {
      this.$store.commit('initSqls', [{
        sqlText: '-- 请输入sql，一个标签只能输入一条sql',
        transformPlugin: null,
        transformPluginParams: null
      }])
    }
    this.getAllGroups()
    this.getAllPlugin()
  },
  components: {MySelect, sqlCode}
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

.textarea {
  /deep/ .el-textarea__inner {
    font-family: 'Consolas', Helvetica, Arial, sans-serif !important;
    font-size: 16px !important;
    line-height: 20px;
  }
}
.label{
  margin: 0 5px 0 15px;

}
</style>
