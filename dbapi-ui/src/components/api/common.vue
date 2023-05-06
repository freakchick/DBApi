<template>
  <div>
    <el-tabs tab-position="left">
      <el-tab-pane label="基本信息">
        <el-form label-width="120px">
          <el-form-item :label="$t('m.name')">
            <el-input v-model="detail.name"></el-input>
          </el-form-item>
          <el-form-item :label="$t('m.path')">
            <el-input v-model="detail.path">
              <template slot="prepend">http://{{ address }}/</template>
            </el-input>
          </el-form-item>
          <el-form-item :label="$t('m.api_group')">
            <el-select v-model="detail.groupId">
              <el-option :value="item.id" v-for="item in groups" :label="item.name">{{ item.name }}</el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('m.note')">
            <el-input v-model="detail.note"></el-input>
          </el-form-item>
          <el-form-item label="Content Type">
            <el-select v-model="detail.contentType" style="width:300px">
              <el-option v-for="item in types" :label="item" :value="item"></el-option>
            </el-select>
            <el-tooltip placement="top-start" effect="dark">
              <div slot="content"><p>{{ $t('m.content_type_info') }}</p></div>
              <i class="el-icon-info tip"></i>
            </el-tooltip>
          </el-form-item>
          <el-form-item :label="$t('m.parameters')">
            <div slot="label">
              <span v-show="detail.contentType == CONTENT_TYPE.FORM_URLENCODED">{{ $t('m.request_params') }}</span>
              <span v-show="detail.contentType == CONTENT_TYPE.JSON">{{ $t('m.request_param_demo') }}</span>
            </div>
            <div v-show="detail.contentType == CONTENT_TYPE.FORM_URLENCODED">

              <el-table :data="detail.paramsJson" border stripe max-height="700" size="mini" :empty-text="$t('m.no_param')">
                <el-table-column prop="name" :label="$t('m.name')" width="220px">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.name"></el-input>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('m.type')" width="220px">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.type" :options="options">
                      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('m.description')" width="300px">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.note"></el-input>
                  </template>
                </el-table-column>

                <el-table-column :label="$t('m.operation')" width="100px">
                  <template slot-scope="scope">
                    <el-button @click="deleteRow(scope.$index)" circle type="danger" icon="el-icon-delete" size="mini"></el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-button @click="addRow" icon="el-icon-plus" type="primary" circle size="mini"></el-button>
            </div>
            <div v-show="detail.contentType == 'application/json'" class="textarea">
              <el-input v-model="detail.jsonParam" :placeholder="$t('m.param_demo_placeholder')" type="textarea" rows="6"></el-input>
              <el-tooltip placement="top-start" effect="dark">
                <div slot="content">{{ $t('m.app_json_tip') }}</div>
                <i class="el-icon-info tip"></i>
              </el-tooltip>
            </div>
          </el-form-item>

          <el-form-item :label="$t('m.authority')">
            <el-radio-group v-model="detail.access">
              <el-radio :label="PRIVILEGE.PRIVATE">{{ $t('m.private') }}</el-radio>
              <el-radio :label="PRIVILEGE.PUBLIC">{{ $t('m.public') }}</el-radio>
            </el-radio-group>

            <el-tooltip placement="top-start" effect="dark">
              <div slot="content">{{ $t('m.access_tip') }}</div>
              <i class="el-icon-info tip"></i>
            </el-tooltip>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="执行器">
        <div v-for="(item,index) in detail.taskJson">
          <el-form label-width="120px">
            <el-form-item label="Executor Type">
              <el-radio-group v-model="detail.taskType">
                <el-radio :label="1">SQL Executor</el-radio>
                <el-radio :label="2">ElasticSearch DSL Executor</el-radio>
                <el-radio :label="3">HTTP API Executor</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>

          <sql-executor v-if="detail.taskType == 1" ref="sqlExecutor"></sql-executor>
        </div>
      </el-tab-pane>
      <el-tab-pane label="全局插件">
        <div>
          <el-form label-width="120px">
            <el-form-item :label="$t('m.cache')">
              <span class="label">{{ $t('m.plugin_name') }}</span>
              <el-select v-model="detail.cachePlugin.pluginName" style="width:400px" clearable @clear="clearCachePluginParam()" :no-data-text="$t('m.no_plugin')">
                <el-option v-for="item in cachePlugins" :value="item.className">
                  <div>
                    <el-tooltip placement="top-start" effect="dark">
                      <div slot="content">
                        <div>{{ $t('m.plugin_desc') }}：{{ item.description }}</div>
                        <div>{{ $t('m.plugin_param_desc') }}：{{ item.paramDescription }}</div>
                      </div>
                      <div>
                        <span>{{ item.className }}</span>
                        <span style="color: #cccccc;margin-left:10px;">{{ item.name }} </span>
                      </div>
                    </el-tooltip>
                  </div>

                </el-option>
              </el-select>
              <span class="label">{{ $t('m.plugin_param') }}</span>
              <el-input v-model="detail.cachePlugin.pluginParam" style="width:400px"></el-input>
              <el-alert type="warning" show-icon>
                {{ $t('m.cache_plugin_warning') }}
              </el-alert>
            </el-form-item>

            <el-button v-for="(item,index) in alarmPlugins" @click="click(index)"></el-button>

            <el-form-item :label="$t('m.alarm')">
              <div v-for="(item,index) in detail.alarmPlugins">
                <span class="label">{{ $t('m.plugin_name') }}</span>
                <el-select v-model="item.pluginName" style="width:400px" clearable @clear="clearAlarmPluginParam()" :no-data-text="$t('m.no_plugin')">
                  <el-option v-for="item in alarmPlugins" :value="item.className" :label="item.className">
                    <div>
                      <el-tooltip placement="top-start" effect="dark">
                        <div slot="content">
                          <div>{{ $t('m.plugin_desc') }}：{{ item.description }}</div>
                          <div>{{ $t('m.plugin_param_desc') }}：{{ item.paramDescription }}</div>
                        </div>
                        <div>
                          <span>{{ item.className }}</span>
                          <span style="color: #cccccc;margin-left:10px;">{{ item.name }} </span>
                        </div>
                      </el-tooltip>
                    </div>
                  </el-option>
                </el-select>
                <span class="label">{{ $t('m.plugin_param') }}</span>

                <el-input v-model="item.pluginParam" style="width:400px"></el-input>

              </div>
              <el-alert type="warning" show-icon>
                {{ $t('m.alarm_plugin_warning') }}
              </el-alert>
            </el-form-item>
          </el-form>
          <div>
            <a class="el-icon-question" target="_blank" href="http://51dbapi.com/zh/plugin">{{ $t('m.what_is_plugin') }}</a>
            <a class="el-icon-question" target="_blank" href="http://51dbapi.com/zh/plugin/#%E5%B1%80%E9%83%A8%E5%8F%82%E6%95%B0">{{ $t('m.what_is_plugin_param') }}</a>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import sqlCode from "@/components/api/common/SqlCode";
import SqlExecutor from "@/components/api/executor/SqlExecutor";
import MySelect from "../common/MySelect";
import {DATA_TYPE, CONTENT_TYPE, PRIVILEGE, PLUGIN_TYPE} from "@/constant";

export default {
  components: {
    MySelect,
    sqlCode,
    SqlExecutor
  },
  data() {
    return {
      CONTENT_TYPE: Object.freeze(CONTENT_TYPE),
      PRIVILEGE: Object.freeze(PRIVILEGE),
      datasources: [],
      address: null,
      show: false,
      groups: [],
      dialogVisible: false,
      detail: {
        name: null,
        note: null,
        path: null,
        paramsJson: [],
        groupId: null,
        access: PRIVILEGE.PRIVATE, //访问权限

        cachePlugin: {pluginName: null, pluginParam: null, pluginType: PLUGIN_TYPE.CACHE_PLUGIN, apiId: null},
        alarmPlugins: [{pluginName: null, pluginParam: null, pluginType: PLUGIN_TYPE.ALARM_PLUGIN, apiId: null}],
        taskJson: [{"taskType":1,"sqlList":[{"sqlText":"--one","transformPlugin":null,"transformPluginParam":null}],"transaction":0,"datasourceId":"2biv61Q4"}],
        jsonParam: null,
        taskType: 1,
        // sqlList: [
        //   {sqlText: "", transformPlugin: null, transformPluginParams: null},
        // ], //默认空字符串，当创建API的时候，默认打开一个标签
        contentType: CONTENT_TYPE.FORM_URLENCODED,
        openTrans: 0,
      },
      options: [
        {label: "string", value: DATA_TYPE.STRING},
        {label: "bigint", value: DATA_TYPE.BIGINT},
        {label: "double", value: DATA_TYPE.DOUBLE},
        {label: "date", value: DATA_TYPE.DATE},
        {label: "Array<string>", value: DATA_TYPE.ARRAY_STRING},
        {label: "Array<bigint>", value: DATA_TYPE.ARRAY_BIGINT},
        {label: "Array<double>", value: DATA_TYPE.ARRAY_DOUBLE},
        {label: "Array<date>", value: DATA_TYPE.ARRAY_DATE},
      ],
      table: null,
      tables: [],
      columns: [],
      column: null,
      // isFullScreen: false,
      // mode: "mini",
      types: [CONTENT_TYPE.FORM_URLENCODED, CONTENT_TYPE.JSON],
      cachePlugins: [],
      transformPlugins: [],
      alarmPlugins: [],


    };
  },
  props: ["id"],
  methods: {
    // clearTransPluginParam(index) {
    //   this.$store.commit("clearTransformPluginParam", index);
    // },
    clearCachePluginParam() {
      this.detail.cachePluginParam = null;
    },
    clearAlarmPluginParam() {
      this.detail.alarmPluginParam = null;
    },
    addRow() {
      this.detail.paramsJson.push({name: null, type: null});
    },
    deleteRow(index) {
      this.detail.paramsJson.splice(index, 1);
    },

    getAddress() {
      this.axios
        .post("/system/getIPPort")
        .then((response) => {
          this.address = response.data;
        })
        .catch((error) => {
          // this.$message.error("失败")
        });
    },
    getDetail(id) {
      this.id = id;
      this.axios.post("/apiConfig/detail/" + id).then((response) => {
        this.detail = response.data
        console.log(this.detail)
      });
    },

    getAllGroups() {
      this.axios
        .post("/group/getAll/")
        .then((response) => {
          this.groups = response.data;
        })
        .catch((error) => {
        });
    },
    getAllPlugin() {
      this.axios
        .post("/plugin/all")
        .then((response) => {
          this.cachePlugins = response.data.cache;
          this.transformPlugins = response.data.transform;
          this.alarmPlugins = response.data.alarm;
        })
        .catch((error) => {
        });
    },
  },
  mounted() {
    this.getAddress();
    //编辑页面
    if (this.id != undefined) {
      this.getDetail(this.id);
    }
    // 新增页面
    else {
      // this.$store.commit("initSqls", [
      //   {
      //     sqlText: "-- only one sql in one tab",
      //     transformPlugin: null,
      //     transformPluginParams: null,
      //   },
      // ]);
    }
    this.getAllGroups();
    this.getAllPlugin();
  },
};
</script>

<style scoped lang="less">
.my > .el-textarea__inner {
  font-family: "Consolas", Helvetica, Arial, sans-serif;
  /*font-size: 18px;*/
}

.mydialog > .el-dialog {
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
    font-family: "Consolas", Helvetica, Arial, sans-serif !important;
    font-size: 16px !important;
    line-height: 20px;
  }
}

.label {
  margin: 0 5px 0 15px;
}
</style>
