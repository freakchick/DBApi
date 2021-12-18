<template>
  <div>
    <codemirror class="myMirror" :options="cmOptions" @ready="onCmReady" @focus="onCmFocus"
                @inputRead="onCmCodeChange">
    </codemirror>
  </div>
</template>

<script>
import {codemirror} from 'vue-codemirror'

import 'codemirror/theme/solarized.css'
import 'codemirror/theme/idea.css'
import 'codemirror/theme/darcula.css'
import "codemirror/addon/hint/show-hint.css";
import 'codemirror/mode/sql/sql.js'

require("codemirror/lib/codemirror");
require("codemirror/mode/sql/sql");
require("codemirror/addon/hint/show-hint");
require("codemirror/addon/hint/sql-hint");

export default {
  name: "codeUI",
  components: {
    codemirror
  },
  data() {
    return {
      cmInstance: null, // 当前codemirror实例
      // cmList: [],
      // sequence: 0,
      cmOptions: {
        value: '',
        styleActiveLine: true,
        lineNumbers: true,
        mode: 'text/x-mysql',
        theme: 'idea',
        lint: true,                     // 代码出错提醒
        matchBrackets: true,
        extraKeys: {"Tab": "autocomplete"},  //Tab可以弹出选择项
        hintOptions: { // 自定义提示选项
          completeSingle: false, // 当匹配只有一项的时候是否自动补全
          tables: {}
        }
      },


    }
  },
  props: {
    sql: {
      type: String,
      default: ''
    },
    mode: {
      type: String,
      default: 'mini'
    },
    tableHints:{
      type: Object,

    }
  },
  methods: {
    onCmReady(cm) {
      cm.setValue(this.sql)
      this.cmInstance = cm
      this.$emit('appendCm',cm)
      // debugger
      // this.cmList.push(cm)
      if (this.mode === "mini")
        cm.setSize('100%', '400px')
      else {
        cm.setSize('100%', 'calc(100vh - 350px)')
      }
      // console.log('the editor is readied!', cm)
    },
    onCmFocus(cm) {
    },
    onCmCodeChange(cm, changeObj) {

      //如果输入的是字母才提示，空格不提示
      if (/^[a-zA-Z.]/.test(changeObj.text[0])) {
        cm.showHint()
      }

    },
    getSql(){
      // return this.cmInstance.getValue()
    }
  },
  watch: {
    mode(val, oldVal) {
      if (val === "mini")
        this.cmInstance.setSize('100%', '400px')
      else {
        this.cmInstance.setSize('100%', 'calc(100vh - 350px)')
      }
    },
    tableHints(val,OldVal){
      debugger
      this.cmOptions.hintOptions.tables = val
    }
  },
  created() {
    this.cmOptions.hintOptions.tables = this.tableHints
  }
}
</script>

<style scoped lang="scss">
.myMirror {
  width: 100%;
  //max-width: 100%;
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
</style>