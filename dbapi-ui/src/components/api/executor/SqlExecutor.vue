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
            <el-tab-pane :key="item.name" v-for="(item, index) in editableTabs" :label="item.title" :name="item.name">
<!--              <sql-code ref="sqlCode" :sqlText="item.sqlText" :index="index+''"></sql-code>-->
              <codemirror :textareaRef="'cms'+index" :value="item.sqlText" @setCode="setCode" mode="mini"></codemirror>
              <span>sql-{{ item.name }} : </span>
              <span class="label">{{ $t('m.plugin_name') }}</span>
              <el-select v-model="item.transformPlugin" style="width:400px" clearable @clear="clearTransPluginParam(index)" :no-data-text="$t('m.no_plugin')">

                <el-option v-for="op in transformPlugins" :value="op.className" :label="op.className">
                  <div>
                    <el-tooltip placement="top-start" effect="dark">
                      <div slot="content">
                        <div>{{ $t('m.plugin_desc') }}：{{ op.description }}</div>
                        <div>{{ $t('m.plugin_param_desc') }}：{{ op.paramDescription }}</div>
                      </div>
                      <div>
                        <span>{{ op.className }}</span>
                        <span style="color: #cccccc;margin-left:10px;">{{ op.name }} </span>
                      </div>
                    </el-tooltip>
                  </div>
                </el-option>
              </el-select>
              <span class="label">{{ $t('m.plugin_param') }}</span>
              <el-input v-model="item.transformPluginParam" style="width:400px"></el-input>

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
// import sqlCode from "@/components/api/common/SqlCode";
import codemirror from "@/components/api/common/codemirror.vue";
import {EXECUTOR_TYPE} from "@/constant";

export default {
  name: "SqlExecutor",
  data() {
    return {
      transformPlugins: [],
      // sqlList: [{sqlText: "", transformPlugin: null, transformPluginParams: null}], //默认空字符串，当创建API的时候，默认打开一个标签
      transaction: 0,
      currentActiveTabName: '1', //当前选中的tab的name
      currentActiveTabIndex: 0, // 当前选中tab的索引值
      editableTabs: [{title: 'SQL 1', name: '1', sqlText: "--one", transformPlugin: null, transformPluginParam: null}],
      tabIndex: 1, //tab 总数
      datasourceId: null,
      datasources: []
    }
  },
  methods: {
    setCode(code){
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
    editableTabs(newV, oldV) {
      let i = 0;
      this.editableTabs.forEach((tab, index) => {
        if (tab.name === this.currentActiveTabName) {
          this.currentActiveTabIndex = index;
        }
      });
      // console.log(i)
      // this.$store.commit('setCurrentActiveCmIndex', i)
    },
    currentActiveTabName(newV, oldV) {
      let i = 0;
      this.editableTabs.forEach((tab, index) => {
        if (tab.name === this.currentActiveTabName) {
          this.currentActiveTabIndex = index;
        }
      });
      // console.log(i)
      // this.$store.commit('setCurrentActiveCmIndex', i)
    }
  },
  computed: {},
  created() {
    this.getAllSource();
  }
}
</script>

<style scoped>

</style>