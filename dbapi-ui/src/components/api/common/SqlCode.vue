<template>
  <div :class="['root', isFullScreen?'full':'']">
    <div class="left">
      <div>
        <my-select v-model="datasourceId" :options="datasources" :nullable="false" :label="$t('m.datasource')"
                   size="mini" width="176px"
                   option_label="name" option_value="id" @onchange="getTables"></my-select>
      </div>
      <div class="bottom">
        <!--        <div class="menus" :style="`top:${y}px;left:${x}px`" v-show="showMenuFlag" @onblur="showMenuFlag=false">
                  <div class="menu" @click="copy">复制</div>
                </div>-->
        <div v-for="(item,index) in tables">
          <div>
            <div class="table" @click.prevent="getColumns(item.label,index)" @contextmenu.prevent="showMenu()">
              <i class="iconfont icon-table"></i>{{ item.label }}
            </div>
            <div v-show="item.showColumns">
              <div v-for="(it) in item.columns" class="column">
                <!--              <i class="iconfont icon-ziyuan"></i>-->
                <span class="columnName">{{ it.label }}</span>
                <span class="columnType">{{ it.TypeName }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
    <div class="right">
      <div class="top">
        <div class="tool">
          <div v-show="isFullScreen">
            <div class="item" @click="run(false)"><i class="iconfont icon-play"></i><span>运行SQL</span></div>
            <div class="item" @click="run(true)"><i class="iconfont icon-play"></i><span>运行选中SQL</span></div>
            <div class="item" @click="parseSql"><i class="iconfont icon-play"></i><span>解析动态SQL</span></div>
            <div class="item" @click="formatSql"><i class="iconfont icon-play"></i><span>格式化SQL</span></div>
          </div>
        </div>
        <div class="quick">
          <div class="tag" @click="tag('foreach')">foreach</div>
          <div class="tag" @click="tag('if')">if</div>
          <div class="tag" @click="tag('where')">where</div>
          <div class="tag" @click="tag('trim')">trim</div>
          <i class="iconfont icon-full" @click="fullWindow" v-if="mode == 'mini'"></i>
          <i class="iconfont icon-mini2" @click="miniWindow" v-if="mode == 'large'"></i>

        </div>
      </div>
      <div class="code">
        <div class="multi-sql">

          <code-ui :sql="item.text" :mode="mode" v-for="(item,index) in this.sqls" :key="item.id" :ref="'codeui-'+index"
                   :tableHints="tableHints" @appendCm="appendCm" v-show="currentIndex===index"></code-ui>

          <div class="tabs">
            <div v-for="(item,index) in sqls" :class="{'tab':true,'tab-active':currentIndex === index}">
              <div @click="focusCM(index)" class="text">
                SQL-{{ item.id }}
              </div>
              <span @click="removeTab(index)" class="el-icon-circle-close close" v-if="index > 0"></span>
            </div>
            <div class="tab" @click="addTab">
              <div class="text">
                <i class="el-icon-circle-plus"></i>
                添加
              </div>
            </div>
          </div>
        </div>
        <div class="params">
          <div style="display: inline-block">参数设置：</div>
          <el-tooltip placement="top-start" effect="dark">
            <div slot="content">
              填写sql运行需要的参数值，拼接成json格式
            </div>
            <i class="el-icon-info tip" style="color:#ccc"></i>
          </el-tooltip>

          <el-input type="textarea" rows="24" v-model="params" @input="input($event)"></el-input>
          <el-button @click="formatJson" size="mini">json格式化</el-button>
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
          <el-table :data="resultList" border stripe style="width: 100%"
                    v-if="resultList != null && resultList.length > 0" size="mini">
            <el-table-column :prop="item" :label="item" v-for="item in Object.keys(resultList[0])"></el-table-column>
          </el-table>
          <div v-if="resultList != null && resultList.length == 0">查询结果为空</div>
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
      error: null, updateMsg: null,
      isFullScreen: false,
      mode: 'mini',
      params: "{}",
      datasourceId: null, datasources: [],
      tables: [], table: null,
      // columns: [], current: null,
      currentIndex: 0,// sql序号
      sqlMeta: null,
      // cmFlag: [0],
      seq: -1,

      sqls: [{text: '', id: 0}],
      cmList: [],
      tableHints:{}
    }
  },
  props: {
    //从父组件传过来的属性
    sqlText: {
      type: Array,
      default: () => ['']
    }
  },

  components: {
    codeUi
  },
  methods: {
    appendCm(cm) {
      this.cmList.push(cm)
    },
    getSql() {
      const sqlList = this.cmList.map(t => {return {sqlText: t.getValue()}}) //转换成后端需要的格式
      // debugger
      return sqlList
    },

    addTab() {
      this.sqls.push({id: ++this.seq, text: ''})
      this.currentIndex = this.sqls.length - 1
    },
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
          {sql: this.cmList[this.currentIndex].getValue(), params: (this.params)}).then((response) => {
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
      var sql = format(this.cmList[this.currentIndex].getValue())
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
      this.cmList[this.currentIndex].setValue(sql);
    },
    run(selected) {
      if (this.datasourceId == null) {
        this.$message.error("请先选择数据源")
        return
      }
      let sql
      if (selected) {
        sql = this.cmList[this.currentIndex].getSelection()
      } else {
        sql = this.cmList[this.currentIndex].getValue()
      }
      if (sql == null || sql.trim() == '') {
        this.$message.error("请先输入sql")
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
    getColumns(table, index) {
      if (this.tables[index].columns.length == 0) {
        this.axios.post("/table/getAllColumns", {
          sourceId: this.datasourceId,
          table: table
        }).then((response) => {
          this.tables[index].columns = response.data
          this.tables[index].showColumns = true
        }).catch((error) => {
          // this.$message.error("查询子节点失败")
        })
      } else {
        this.tables[index].showColumns = !(this.tables[index].showColumns)
      }

    },
    getAllSource() {
      this.axios.post("/datasource/getAll").then((response) => {
        this.datasources = response.data
      }).catch((error) => {
        this.$message.error("查询所有数据源失败")
      })
    },
    getTables(datasourceId) {
      this.axios.post("/table/getAllTables", {sourceId: datasourceId}).then((response) => {
        this.tables = response.data

        const hints = {}
        this.tables.forEach(e => {
          hints[`${e.label}`] = e.columns.map(j => j.label)
        })
        this.tableHints = hints // 表名称注入codemirror 提示
        // this.cmOptions.hintOptions.tables = hints

      }).catch((error) => {
        this.$message.error("查询所有表名称失败；但不影响使用！")
      })
    },
    removeTab(index) {
      this.sqls.splice(index, 1)
      this.cmList.splice(index, 1)
      //如果删除的是激活标签的左边标签,或激活标签本身
      if (index <= this.currentIndex) {
        this.currentIndex -= 1
      }

    },


    focusCM(index) {
      this.currentIndex = index
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

      this.cmList[this.currentIndex].setValue(this.cmList[this.currentIndex].getValue() + val)
    },
  },

  created() {
    this.getAllSource()
    this.sqls = this.sqlText.map((text) => {
      this.seq += 1
      return {id: this.seq, text: text}
    })
  },
  watch: {
    // sqlText是异步加载过来的，所以要监听
    sqlText(newV, oldV) {
      debugger
      this.sqls = newV.map((text) => {
        this.seq += 1
        return {id: this.seq, text: text}
      })
      // 为了兼容创建api页面，sqls设置了一个默认元素，这会导致cmList多一个cmInstance，
      // 编辑API页面监听到sqlText,需要清空掉这个cmInstance
      this.cmList = []
    }
  }
}
</script>
<style scoped lang="scss">
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
  z-index: 10;
  position: fixed;
  top: 10px;
  left: 10px;
  width: calc(100vw - 20px);
  height: calc(100vh - 20px);

  .left {
    width: 300px !important;
    height: calc(100vh - 20px) !important;

    .bottom {
      height: calc(100vh - 30px) !important;
    }
  }

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

  .left {
    width: 250px !important;
    height: 430px;
    border: 1px solid #999999;
    line-height: 20px;
    background-color: #fff;
    flex-shrink: 0;

    .bottom {
      height: 400px;
      overflow: auto;
      padding-top: 5px;

      .menus {
        position: fixed;
        z-index: 1000;
        background-color: #fff;
        width: 100px;
        height: 40px;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        box-shadow: 0 0 3px #333;
        line-height: 20px;

        .menu {
          cursor: pointer;

          &:hover {
            background-color: #dedede;
          }
        }

      }

      .table {
        padding-left: 5px;
        font-size: 16px;

        i {
          margin-right: 5px;
          line-height: 20px;
        }

        &:hover {
          background-color: #dedede;
        }
      }

      .column {
        padding-left: 35px;

        .columnType {
          //background-color: #cdf2f6;
          margin-left: 5px;
          padding: 0 3px;
          color: #ccc;
          font-size: 10px;
        }

        .columnName {
          background-color: #eaeaea;
          margin-left: 5px;
          padding: 0 3px;
          font-size: 16px;
        }

        i {
          margin-right: 5px;
          line-height: 20px;
        }

        &:hover {
          background-color: #dedede;
        }
      }
    }
  }

  .right {
    display: block;
    width: 100%;
    border: 1px solid #999999;
    border-left: 0px;
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
          background-color: #FF9900;
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
        width: 100%;
        background-color: rgba(199, 48, 48, 0.23);
        position: relative;


        .tabs {
          //height: 18px;
          //z-index: 1000;
          //background-color: #b96666;
          position: absolute;
          bottom: 1px;
          left: 30px;
          display: flex;

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
              position: absolute;;
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
          font-family: 'Consolas', Helvetica, Arial, sans-serif !important;
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
          white-space: pre-wrap
        }
      }
    }
  }
}

</style>
