<template>
  <div :class="['root', isFullScreen?'full':'']">
    <div class="right">
      <div class="top">
        <div class="tool">
          <div v-show="isFullScreen">
            <div
              class="item"
              @click="run(false)"
            ><i class="iconfont icon-play"></i><span>{{$t('m.run_sql')}}</span></div>
            <div
              class="item"
              @click="run(true)"
            ><i class="iconfont icon-play"></i><span>{{$t('m.run_selected_sql')}}</span></div>
            <div
              class="item"
              @click="parseSql"
            ><i class="iconfont icon-play"></i><span>{{$t('m.parse_sql')}}</span></div>
            <div
              class="item"
              @click="formatSql"
            ><i class="iconfont icon-play"></i><span>{{$t('m.format')}}</span></div>
          </div>
        </div>
        <div class="quick">
          <div
            class="tag"
            @click="tag('foreach')"
          >foreach</div>
          <div
            class="tag"
            @click="tag('if')"
          >if</div>
          <div
            class="tag"
            @click="tag('where')"
          >where</div>
          <div
            class="tag"
            @click="tag('trim')"
          >trim</div>
          <i
            class="iconfont icon-full"
            @click="fullWindow"
            v-if="mode == 'mini'"
          ></i>
          <i
            class="iconfont icon-mini2"
            @click="miniWindow"
            v-if="mode == 'large'"
          ></i>
        </div>
      </div>
      <div class="code">
        <div class="multi-sql" :style="{'width':  !isFullScreen ? '100%' : ''}">
          <code-ui :sqlText="sqlText" :mode="mode" :tableHints="tableHints"></code-ui>
        </div>
        <div class="params">
          <div style="display: inline-block">{{$t('m.sql_param')}}：</div>
          <el-tooltip placement="top-start" effect="dark">
            <div slot="content">{{$t('m.sql_param_tip')}}</div>
            <i class="el-icon-info tip" style="color:#ccc"></i>
          </el-tooltip>

          <el-input type="textarea" rows="24" v-model="params" @input="input($event)"></el-input>
          <el-button @click="formatJson" size="mini">{{$t('m.format')}}</el-button>
        </div>
      </div>
      <div class="result">
        <div v-if="error != null" class="error">
          <i class="el-icon-error"></i>
          {{ error }}
        </div>
        <div v-if="updateMsg != null" class="updateMsg">
          <i class="el-icon-success"></i>
          {{ updateMsg }}
        </div>
        <div v-if="sqlMeta != null" class="sqlMeta">
          <div class="sql">{{ sqlMeta.sql }}</div>
          <div class="sql">{{ sqlMeta.jdbcParamValues }}</div>
        </div>
        <div class="table">
          <el-table :data="resultList" border stripe style="width: 100%" v-if="resultList != null && resultList.length > 0" size="mini">
            <el-table-column :prop="item" :label="item" v-for="item in Object.keys(resultList[0])"></el-table-column>
          </el-table>
          <div v-if="resultList != null && resultList.length == 0">No Result</div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import codeUi from "@/components/api/common/codeUI";
import {format} from 'sql-formatter';
export default {
  data() {
    return {
      resultList: null,
      error: null,
      updateMsg: null,
      isFullScreen: false,
      mode: 'mini',
      params: "{}",

      currentIndex: 0,// sql序号
      sqlMeta: null,

      tableHints: {}
    }
  },
  props: {
    //从父组件传过来的属性
    sqlText: {
      type: String
    }
  },

  components: {
    codeUi
  },
  methods: {

    // addTab() {
    //   this.$store.commit('addSql')
    //   this.currentIndex = this.$store.state.sqls.length - 1
    // },
    formatJson() {
      const o = JSON.parse(this.params)
      this.params = JSON.stringify(o, null, 4)
    },
    input(event) {
      console.log(event)
    },
    parseSql() {
      this.resultList = null
      this.updateMsg = null
      this.error = null
      this.sqlMeta = null
      this.axios.post("/apiConfig/parseDynamicSql",
          {sql: this.$store.getters.currentCm.getValue(), params: (this.params)}).then((response) => {
        if (response.data.success) {
          this.sqlMeta = response.data.data

        } else {
          this.error = response.data.msg
        }
      }).catch((error) => {
        // this.$message.error("查询所有数据源失败")
      })
    },
    formatSql() {
    debugger
      let p = this.$store.getters.currentCm
      const sql = format(p.getValue())
        .replace(/# /g, "#")
        .replace(/{ /g, "{")
        .replace(/ }/g, "}")
        .replace(/< foreach/g, "\n<foreach\n")
        .replace(/< \/ foreach >/g, "\n</foreach>\n")
        .replace(/< if/g, "\n<if")
        .replace(/< \/ if >/g, "\n</if>\n")
        .replace(/<\nwhere\n  >/g, "\n<where>\n")
        .replace(/< \/\nwhere\n  >/g, "\n</where>\n")
        .replace(/< trim/g, "\n<trim")
        .replace(/< \/ trim >/g, "\n</trim>\n");
      this.$store.getters.currentCm.setValue(sql);
    },
    run(selected) {
      if (this.datasourceId == null) {
        this.$message.error("Please select datasource")
        return
      }
      let sql
      if (selected) {
        sql = this.$store.getters.currentCm.getSelection()
      } else {
        sql = this.$store.getters.currentCm.getValue()
      }
      if (sql == null || sql.trim() == '') {
        this.$message.error("Please Input sql")
        return
      }
      this.resultList = null
      this.updateMsg = null
      this.error = null
      this.sqlMeta = null

      this.axios.post("/apiConfig/sql/execute",
          {sql: sql, datasourceId: this.datasourceId, params: this.params}).then((response) => {
        if (response.data.success) {
          if (Array.isArray(response.data.data)) {
            this.resultList = response.data.data

          } else {
            this.updateMsg = response.data.data
          }

        } else {
          this.error = response.data.msg
        }
      }).catch((error) => {
        this.$message.error(error)
      })
    },
    fullWindow() {
      this.mode = "large"
      this.isFullScreen = true
    },
    miniWindow() {
      this.mode = "mini"
      this.isFullScreen = false
    },


    focusCM(index, sql) {
      this.currentIndex = index
      const _this = this
      this.$nextTick(() => {
        _this.$refs['codeui-'+index][0].$refs['codemirror'].codemirror.refresh()
      })
    },
    tag(item) {
      let val = ''
      if (item == 'foreach') {
        val = "\n<foreach open=\"(\" close=\")\" collection=\"\" separator=\",\" item=\"item\" index=\"index\">#{item}</foreach>"
      } else if (item == 'if') {
        val = "\n<if test=\"\" ></if>"
      } else if (item == 'where') {
        val = "\n<where></where>"
      } else if (item == 'trim') {
        val = "\n<trim prefix=\"\" suffix=\"\" suffixesToOverride=\"\" prefixesToOverride=\"\"></trim>"
      }

      this.$store.getters.currentCm.setValue(this.$store.getters.currentCm.getValue() + val)
    },
  },

  created() {

  },
  watch: {
    // props:apiSql是异步加载过来的，所以要监听
    // apiSql(newV, oldV) {
    //   this.$store.commit('initSqls',this.apiSql)
    // }
  }
}
</script>
<style scoped lang="less">
::-webkit-scrollbar {
  /*滚动条整体样式*/
  width: 6px; /*高宽分别对应横竖滚动条的尺寸*/
  height: 1px;
}

::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 6px;
  //box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #a0a0a0;
}

::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  //box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 16px;
  //background: #ededed;
}

.full {
  background-color: rgba(22, 118, 11, 0.53);
  z-index: 10;
  position: fixed;
  top: 10px;
  left: 10px;
  width: calc(100vw - 20px);
  height: calc(100vh - 20px);

  .right {
    .code {
      height: calc(100vh - 350px) !important;

      .params {
        display: block !important;
      }
    }

    .result {
      display: block !important;
    }
  }
}

.root {
  display: flex;
  //height: 430px;
  .right {
    display: block;
    // width: 100%;
    width: calc(100vw) !important;
    overflow: auto;
    border: 1px solid #999999;
    background-color: #fff;
    //flex-shrink: 0;
    //flex-grow: 3;

    .top {
      height: 26px;
      line-height: 26px;
      display: flex;
      justify-content: space-between;
      border-bottom: 1px solid #82848a;
      background-color: #d2d2d2;

      .tool {
        .item {
          display: inline-block;
          font-size: 20px;
          color: #009966;
          margin: 0 20px 0 0;
          line-height: 26px;
          cursor: pointer;

          span {
            color: #000;
            font-size: 16px;
          }

          &:hover {
            background-color: #d77f00;
          }
        }
      }

      .quick {
        //background-color: #ea5959;
        display: flex;

        .tag {
          //display: inline-block;
          //height: 30px;
          //border: 1px solid #FF9933;
          background-color: #ff9900;
          color: #fff;
          border-radius: 3px;
          margin: 2px;
          line-height: 22px;
          padding: 0 5px;
          cursor: pointer;

          &:hover {
            font-weight: 700;
            background-color: #d77f00;
          }
        }

        .iconfont {
          font-size: 20px;
          margin: 0 5px;
          padding: 0 5px;

          &:hover {
            background-color: #d77f00;
            color: #fff;
          }
        }
      }
    }

    .code {
      display: flex;
      //width: 100%;

      height: 400px;
      padding: 0px;
      overflow: auto;
      //background-color: #f13838;
      .multi-sql {
        width: calc(100% - 300px);
        background-color: rgba(199, 48, 48, 0.23);
        position: relative;

        .tabs {
          //height: 18px;
          //z-index: 1000;
          background-color: #fff;
          position: absolute;
          bottom: 1px;
          left: 30px;
          display: flex;
          z-index:999;

          .tab-active {
            font-weight: 700;
            background-color: rgba(4, 103, 10, 0.18);
          }

          .tab {
            z-index: 1000;
            position: relative;
            cursor: pointer;
            border-top-left-radius: 5px;
            border-top-right-radius: 5px;
            border: 1px solid #ccc;
            border-bottom-width: 0;
            //color: #fff;
            //background-color: #03830a;
            margin: 0 3px;
            width: 80px;
            height: 22px;
            line-height: 22px;
            //padding: 0 3px;
            .text {
              padding: 0 5px;
              text-align: center;
              //width: 50px;
            }

            .close {
              position: absolute;
              right: -10px;
              top: -10px;
              width: 20px;
              display: none;
              background-color: rgba(4, 103, 10, 0.27);
              padding: 3px;
              border-radius: 50%;

              &:hover {
                font-weight: 900;
              }
            }

            &:hover {
              background-color: rgba(4, 103, 10, 0.11);

              .close {
                background-color: rgba(23, 19, 19, 0.17);
                display: block;
              }
            }
          }
        }
      }

      .params {
        padding: 5px;
        width: 600px;
        border-left: 1px solid #82848a;
        //background-color: #ccc;
        display: none;
        overflow: auto;

        /deep/ .el-textarea__inner {
          font-family: "Consolas", Helvetica, Arial, sans-serif !important;
          font-size: 18px !important;
          line-height: 20px;
        }
      }
    }

    .result {
      height: 300px;
      //background-color: #eee;
      display: none;
      border-top: 1px solid #82848a;
      padding: 5px;
      overflow: auto;
      width: 100%;

      .table {
        margin: 5px;
        overflow: auto;
        width: 98%;
      }

      .error {
        color: #f60303;
      }

      .sqlMeta {
        .sql {
          line-height: 20px;
          background-color: #f3f3f3;
          padding: 5px;
          margin: 3px;
          white-space: pre-wrap;
        }
      }
    }
  }
}
</style>
