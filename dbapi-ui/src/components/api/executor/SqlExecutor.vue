<template>
  <div>
    <el-form label-width="120px">

      <el-form-item label="sql">
        <div>
          <sql-code ref="sqlCode" :apiSql="sqlList"></sql-code>
        </div>
      </el-form-item>
      <el-form-item :label="$t('m.transaction')">
        <el-radio-group v-model="openTrans">
          <el-radio :label="1">{{ $t('m.on') }}</el-radio>
          <el-radio :label="0">{{ $t('m.off') }}</el-radio>
        </el-radio-group>
        <el-tooltip placement="top-start" effect="dark">
          <div slot="content">
            {{ $t('m.transaction_tip') }}
          </div>
          <i class="el-icon-info tip"></i>
        </el-tooltip>
        <el-alert type="warning" show-icon v-show="openTrans == 1">
          <p>{{ $t('m.transaction_warning') }}</p>
        </el-alert>
      </el-form-item>
      <el-form-item :label="$t('m.transform')">
        <div v-for="(item,index) in this.$store.state.sqls">
          <span>sql-{{ item.label }} : </span>
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
          <el-input v-model="item.transformPluginParams" style="width:400px"></el-input>
        </div>

        <el-alert type="warning" show-icon>
          {{ $t('m.transform_plugin_warning') }}
        </el-alert>

      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import sqlCode from "@/components/api/common/SqlCode";

export default {
  name: "SqlExecutor",
  data() {
    return {
      transformPlugins:[],
      taskType: 1,
      sqlList: [
        {sqlText: "", transformPlugin: null, transformPluginParams: null},
      ], //默认空字符串，当创建API的时候，默认打开一个标签
      openTrans: 0,

    }
  },
  components: {
    sqlCode
  },
  computed: {
    taskJson() {
      return {
        taskType: this.taskType,
        sqlList: this.sqlList,
        openTrans: this.openTrans,
        datasourceId: this.$refs.sqlCode.dataourceId
      }
    }
  }
}
</script>

<style scoped>

</style>