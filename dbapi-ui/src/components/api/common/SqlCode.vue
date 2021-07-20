<template>
  <div :class="['root', isFullScreen?'full':'']">
    <div class="left">
      <div>
        <my-select v-model="datasourceId" :options="datasources" :nullable="false" label="数据源" size="mini" width="176px"
                   option_label="name" option_value="id" @optionClick="getTables"></my-select>
      </div>
      <div class="bottom">
        <div class="menus" :style="`top:${y}px;left:${x}px`" v-show="showMenuFlag">
          <div class="menu" @click="copy">复制</div>
        </div>
        <div v-for="(item,index) in tables">
          <div @click="getColumns(item.label,index)">
            <div class="table" @contextmenu.prevent="showMenu()"><i class="iconfont icon-table"></i>{{ item.label }}
            </div>

            <div v-for="(it) in columns" v-if="current == index" class="column">
              <i class="iconfont icon-ziyuan"></i>
              <span>{{ it.label }}</span>

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
        <codemirror ref="cmEditor" class="myMirror" :options="cmOptions" @ready="onCmReady" @focus="onCmFocus"
                    @inputRead="onCmCodeChange">
        </codemirror>
        <div class="params">
          <div style="display: inline-block">动态SQL参数设置：</div>
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
        <el-table :data="resultList" border style="width: 100%" v-if="resultList.length > 0" size="mini">
          <el-table-column :prop="item" :label="item" v-for="item in Object.keys(resultList[0])"></el-table-column>
        </el-table>
      </div>
    </div>

  </div>
</template>

<script>
import dbIcon from "@/components/common/dbIcon";

import {codemirror} from 'vue-codemirror'

import 'codemirror/theme/solarized.css'
import 'codemirror/theme/idea.css'
import "codemirror/addon/hint/show-hint.css";


import 'codemirror/mode/sql/sql.js'

require("codemirror/lib/codemirror");
require("codemirror/mode/sql/sql");
require("codemirror/addon/hint/show-hint");
require("codemirror/addon/hint/sql-hint");


export default {
  data() {
    return {
      x: 0, //菜单坐标
      y: 0,//菜单坐标
      showMenuFlag: false,
      resultList: [],
      error: null, updateMsg: null,
      isFullScreen: false,
      mode: 'mini',
      params: null,
      datasourceId: null, datasources: [],
      tables: [], table: null, columns: [], current: null,
      cmOptions: {
        value: '',
        styleActiveLine: true,
        lineNumbers: true,
        mode: 'text/x-mysql',
        theme: 'idea',
        lint: true,                     // 代码出错提醒
        matchBrackets: true
      }
    }
  },

  components: {
    codemirror, dbIcon
  },
  methods: {
    copy(){
      this.showMenuFlag = false
    },
    showMenu() {
      event.preventDefault()
      this.x = event.clientX
      this.y = event.clientY + 10
      this.showMenuFlag = true
    },
    formatJson() {
      const o = JSON.parse(this.params)
      this.params = JSON.stringify(o, null, 4)
    },
    input(event) {
      console.log(event)
    },
    parseSql() {
      this.axios.post("/apiConfig/parseDynamicSql",
          {sql: this.codemirror.getValue(), params: (this.params)}).then((response) => {
        if (response.data.success) {


        } else {
          this.error = response.data.msg
        }
      }).catch((error) => {
        // this.$message.error("查询所有数据源失败")
      })
    },
    run(selected) {
      if (this.datasourceId == null) {
        this.$message.error("请先选择数据源")
        return
      }
      let sql
      if (selected) {
        sql = this.codemirror.getSelection()
      } else {
        sql = this.codemirror.getValue()
      }
      if (sql == null || sql.trim() == '') {
        this.$message.error("请先输入sql")
        return
      }
      this.resultList = []
      this.updateMsg = null
      this.error = null

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
        // this.$message.error("查询所有数据源失败")
      })
    },
    fullWindow() {
      this.mode = "large"
      this.isFullScreen = true
      this.codemirror.setSize('100%', 'calc(100vh - 350px)')
    },
    miniWindow() {
      this.mode = "mini"
      this.isFullScreen = false
      this.codemirror.setSize('100%', '400px')
    },
    getColumns(table, index) {
      this.current = index
      this.axios.post("/table/getAllColumns", {
        sourceId: this.datasourceId,
        table: table
      }).then((response) => {
        this.columns = (response.data);
        // console.log(this.tables)
      }).catch((error) => {
        this.$message.error("查询子节点失败")
      })
    },
    getAllSource() {
      this.axios.post("/datasource/getAll").then((response) => {
        this.datasources = response.data
      }).catch((error) => {
        this.$message.error("查询所有数据源失败")
      })
    },
    getTables() {
      this.axios.post("/table/getAllTables", {sourceId: this.datasourceId}).then((response) => {
        this.tables = response.data
      }).catch((error) => {
        this.$message.error("查询所有表失败")
      })
    },


    onCmReady(cm) {
      cm.setSize('100%', '400px')
      // console.log('the editor is readied!', cm)
    },
    onCmFocus(cm) {
      // console.log('the editor is focused!', cm)
    },
    onCmCodeChange(instance, changeObj) {
      //如果输入的是字母才提示，空格不提示
      if (/^[a-zA-Z]/.test(changeObj.text[0])) {
        this.codemirror.showHint({
          completeSingle: false
        })
      }

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

      this.codemirror.setValue(this.codemirror.getValue() + val)
    },
  },
  computed: {
    codemirror() {
      return this.$refs.cmEditor.codemirror
    }
  },
  created() {
    this.getAllSource()

  }

}
</script>
<style scoped lang="scss">


.full {
  z-index: 10;
  position: fixed;
  top: 10px;
  left: 10px;
  width: calc(100vw - 20px);
  height: calc(100vh - 20px);

  .left {
    width: 300px !important;
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
    border: 1px solid #999999;
    line-height: 20px;
    background-color: #fff;
    flex-shrink: 0;
    overflow: hidden;

    .bottom {
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
        .menu{
          cursor: pointer;
          &:hover{
            background-color: #dedede;
          }
        }

      }

      .table {
        padding-left: 10px;

        i {
          margin-right: 5px;
          line-height: 20px;
        }

        &:hover {
          background-color: #dedede;
        }
      }

      .column {
        padding-left: 40px;

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
      width: 100%;
      height: 400px;
      //background-color: #a8eee3;
      padding: 0px;
      overflow: auto;
      overscroll-y: scroll !important;

      .myMirror {
        width: 100%;

        /deep/ .CodeMirror-line {
          font-family: 'Consolas', Helvetica, Arial, sans-serif !important;
          font-size: 18px !important;
          line-height: 20px;

          .cm-comment {
            font-family: 'Consolas', Helvetica, Arial, sans-serif !important;
            font-size: 18px !important;
            line-height: 20px;
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

      .error {
        color: #f60303;

      }
    }
  }
}

</style>
