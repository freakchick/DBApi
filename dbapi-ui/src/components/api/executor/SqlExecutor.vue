<template>
  <div>
    <el-form label-width="120px">
      <el-form-item label="datasource">
        <el-select v-model="datasourceId">
          <el-option :value="item.id" :label="item.name" v-for="item in datasources">{{ item.name }}</el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <div slot="label">
          sql
          <el-tooltip placement="top-start" effect="dark">
            <div slot="content">可以编写多条sql，多条sql执行结果会拼接后返回</div>
            <i class="el-icon-info tip"></i>
          </el-tooltip>
        </div>
        <div>
          <el-tabs v-model="currentActiveTabName" type="card" editable @edit="handleTabsEdit" tab-position="top">
            <el-tab-pane :key="item.name" v-for="(item, index) in editableTabs" :label="'SQL-'+item.name" :name="item.name">
              <codemirror :textareaRef="'cms'+index" :value="item.sqlText" @setCode="setCode" mode="mini"></codemirror>
              <div style="margin-top: 10px">
                <span>SQL-{{ item.name }} : </span>
                <span class="label">{{ $t('m.plugin_name') }}</span>
                <el-select v-model="item.transformPlugin" style="width:400px" clearable @clear="clearTransPluginParam(index)" :no-data-text="$t('m.no_plugin')">

                  <el-option v-for="op in transformPlugins" :value="op.className" :label="op.name">
                    <div>
                      <el-tooltip placement="top-start" effect="dark">
                        <div slot="content">
                          <div>{{ $t('m.plugin_desc') }}：{{ op.description }}</div>
                          <div>{{ $t('m.plugin_param_desc') }}：{{ op.paramDescription }}</div>
                        </div>
                        <div>
                          <span>{{ op.name }}</span>
                          <span style="color: #cccccc;margin-left:10px;">{{ op.className }} </span>
                        </div>
                      </el-tooltip>
                    </div>
                  </el-option>
                </el-select>
                <span class="label">{{ $t('m.plugin_param') }}</span>
                <el-input v-model="item.transformPluginParam" style="width:400px"></el-input>
              </div>

            </el-tab-pane>
          </el-tabs>

        </div>
      </el-form-item>
      <el-form-item>
        <div slot="label">
          {{ $t('m.transaction') }}
          <el-tooltip placement="top-start" effect="dark">
            <div slot="content">{{ $t('m.transaction_tip') }}</div>
            <i class="el-icon-info tip"></i>
          </el-tooltip>
        </div>

        <el-radio-group v-model="transaction">
          <el-radio :label="1">{{ $t('m.on') }}</el-radio>
          <el-radio :label="0">{{ $t('m.off') }}</el-radio>
        </el-radio-group>

      </el-form-item>

    </el-form>
  </div>
</template>

<script>
import codemirror from "@/components/api/common/codemirror.vue";
import {EXECUTOR_TYPE} from "@/constant";

export default {
  name: "SqlExecutor",
  data() {
    return {
      transformPlugins: [],
      transaction: 0,
      currentActiveTabName: '1', //当前选中的tab的name
      currentActiveTabIndex: 0, // 当前选中tab的索引值
      editableTabs: [{name: '1', sqlText: "-- only one sql in one tab", transformPlugin: null, transformPluginParam: null}],
      tabIndex: 1, //tab 总数
      datasourceId: null,
      datasources: []
    }
  },
  props: {
    detail: {
      type: Object
    }
  },
  methods: {
    getAllPlugins() {
      this.axios
        .post("/plugin/all")
        .then((response) => {
          this.transformPlugins = response.data.transform;
        })
        .catch((error) => {
        });
    },
    setCode(code) {
      this.editableTabs[this.currentActiveTabIndex].sqlText = code
      // console.log(this.editableTabs)
    },
    getTaskJson() {

      let p = this.editableTabs.map((item, index) => {
        return {sqlText: item.sqlText, transformPlugin: item.transformPlugin, transformPluginParam: item.transformPluginParam}
      })
      // console.log(p)
      return {
        taskType: EXECUTOR_TYPE.SQL_EXECUTOR,
        sqlList: p,
        transaction: this.transaction,
        datasourceId: this.datasourceId
      }
    },
    getAllSource() {
      this.axios.post("/datasource/getAll").then((response) => {
        this.datasources = response.data
      }).catch((error) => {
        this.$message.error("Get all datasources Failed")
      })
    },
    handleTabsEdit(targetName, action) {
      if (action === 'add') {
        let newTabName = ++this.tabIndex + '';
        this.editableTabs.push({
          title: 'SQL ' + newTabName,
          name: newTabName,
          sqlText: "--add", transformPlugin: null, transformPluginParam: null
        });
        this.currentActiveTabName = newTabName;
      }
      if (action === 'remove') {
        if (this.editableTabs.length === 1) {
          this.$message.warning("At least one tab!")
          return;
        }
        let tabs = this.editableTabs;
        let activeName = this.currentActiveTabName;
        let i = 0;
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              i = index;
              let nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                activeName = nextTab.name;
              }
            }
          });
        }

        this.currentActiveTabName = activeName;
        this.editableTabs = tabs.filter(tab => tab.name !== targetName);
        this.$store.commit('removeCm', i) // 删除 vuex中 的cmInstance
      }
    }
  },
  components: {
    codemirror
  },
  watch: {
    detail: function (newVal, oldVal) {
      debugger
      this.transaction = newVal.transaction
      this.datasourceId = newVal.datasourceId

      for (let j = 0; j < newVal.sqlList.length; j++) {
        const b = newVal.sqlList[j]
        b.name = (j + 1) + '';
      }
      this.editableTabs = newVal.sqlList;
      this.tabIndex = newVal.sqlList.length;
    },
    editableTabs(newV, oldV) {
      this.editableTabs.forEach((tab, index) => {
        if (tab.name === this.currentActiveTabName) {
          this.currentActiveTabIndex = index;
        }
      });
    },
    currentActiveTabName(newV, oldV) {
      this.editableTabs.forEach((tab, index) => {
        if (tab.name === this.currentActiveTabName) {
          this.currentActiveTabIndex = index;
        }
      });
    }
  },
  computed: {},
  created() {
    this.getAllSource();
    this.getAllPlugins()
  }
}
</script>

<style scoped>
.label {
    font-weight: 700;
    margin: 0 5px 0 20px;
}
</style>