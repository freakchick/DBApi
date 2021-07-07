<template>
  <div :class="['root', isFullScreen?'full':'']">
    <div class="left">
      <div>
        <my-select v-model="datasourceId" :options="datasources" :nullable="false" label="数据源" size="mini" width="176px"
                   option_label="name" option_value="id" @optionClick="getTables"></my-select>
      </div>
      <div class="bottom">

        <div v-for="(item,index) in tables">
          <div @click="getColumns(item.label,index)">
            <div class="table"><i class="iconfont icon-table"></i>{{ item.label }}</div>

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
            <div class="item"><i class="iconfont icon-play"></i><span>运行</span></div>
            <div class="item"><i class="iconfont icon-play"></i><span>运行选中</span></div>
            <div class="item"><i class="iconfont icon-play-add"></i><span>新标签运行</span></div>
            <div class="item"><i class="iconfont icon-play-add"></i><span>新标签运行选中</span></div>
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
        <codemirror ref="cmEditor"
                    class="myMirror"
                    :options="cmOptions"
                    @ready="onCmReady"
                    @focus="onCmFocus"

                    @inputRead="onCmCodeChange"
        >
        </codemirror>
        <div class="params">
          <div>动态SQL参数设置：</div>
          <el-input type="textarea" rows="20" v-model="params"></el-input>
          <div>json格式</div>
        </div>
      </div>
      <div class="result">
        123
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
        theme: 'solarized light',
        lint: true,                     // 代码出错提醒
        matchBrackets: true
      }
    }
  },

  components: {
    codemirror, dbIcon
  },
  methods: {
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
    background-color: #f1f1f1;
    flex-shrink: 0;
    overflow: hidden;

    .bottom {
      //background-color: #ec4040;
      overflow: auto;
      padding-top: 5px;
      //background-color: #f6cfcf;
      .table {
        //background-color: #ea9898;
        padding-left: 10px;
      }

      .table:hover {
        background-color: #f87070;
      }

      .table:nth-child(2n) {
        background-color: #f6cdcd;
      }

      .column {
        //background-color: #84d559;
        padding-left: 40px;
      }
    }
  }

  .right {
    display: block;
    width: 100%;
    border: 1px solid #999999;
    border-left: 0px;
    background-color: #d9d9d9;

    .top {
      height: 26px;
      line-height: 26px;
      display: flex;
      justify-content: space-between;
      border-bottom: 1px solid #82848a;

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
            //background-color: #ccc;
            color: #fff;

            span {
              color: #fff;
            }
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

      }


      .params {
        padding: 5px;
        width: 400px;
        border-left: 1px solid #82848a;
        background-color: #FFFFCC;
        display: none;
        overflow: auto;
      }
    }

    .result {
      height: 300px;
      background-color: #FFCC99;
      display: none;
      border-top: 1px solid #82848a;
    }
  }
}

</style>
