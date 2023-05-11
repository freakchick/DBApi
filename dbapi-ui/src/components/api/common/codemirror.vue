<template>
  <div class="in-coder-panel">
    <textarea :ref="textareaRef" v-model="code" autofocus="true"></textarea>
    <!--    <el-button @click="show">show</el-button>-->
  </div>
</template>

<script>
import CodeMirror from "codemirror";

import "codemirror/lib/codemirror.css";

import "codemirror/theme/solarized.css";
import "codemirror/theme/idea.css";
import "codemirror/theme/darcula.css";

import "codemirror/addon/hint/show-hint.css";
import "codemirror/addon/hint/show-hint.js";
import "codemirror/addon/hint/sql-hint.js";

import "codemirror/mode/sql/sql.js";

export default {
  name: "codemirror",
  props: {
    value: String,

    textareaRef: {
      type: String,
      default: "",
    },
    mode: {
      type: String,
      default: 'mini'
    }
  },
  data() {
    return {
      CodeMirror: null,
      code: "",//不能null，否则报错
      coder: null,
      options: {
        tabSize: 2,
        lineNumbers: true,
        line: true,
        mode: "text/x-mysql",
        theme: "darcula",
        readOnly: false,
        lineWrapping: false,
        autofocus: true,
        autoRefresh: true, //很重要，否则编辑API页面初始化加载不显示
        styleActiveLine: true,
        lint: true, // 代码出错提醒
        matchBrackets: true,
        extraKeys: {Tab: "autocomplete"}, //Tab可以弹出选择项
        hintOptions: {
          completeSingle: false,
          alignWithWord: false,
          hint: CodeMirror.hint.sql,
        },
      },
    };
  },
  mounted() {
    this._initialize();
  },
  methods: {
    // show() {
    //   alert(this.code);
    // },
    _initialize() {
      // debugger
      this.coder = CodeMirror.fromTextArea(
        this.$refs[this.textareaRef],
        this.options
      );
      this.coder.setSize("90%", "400px");
      // alert(this.value)
      this.coder.setValue(this.value || this.code);

      this.coder.on("change", (coder, changeObj) => {
        this.code = coder.getValue();
        if (/^[a-zA-Z.]/.test(changeObj.text[0])) {
          this.coder.showHint();
        }
      });

    },
  },
  watch: {
    //编辑api页面初次渲染的时候还获取不到props值，是先生成本组件再从父组件注入props值，所以要监听
    value: function (newVal, oldVal) {
      // console.log("监听到value :" + newVal)
      // debugger
      this.code = newVal
      this.coder.setValue(newVal);
      //编辑页面初次渲染注入value值会触发codemirror change事件，导致页面显示代码提示框，应该关闭
      this.coder.closeHint();

    }
  }
};
</script>

<style>
.in-coder-panel {
    flex-grow: 1;
    display: flex;
    position: relative;
}

.CodeMirror {
    flex-grow: 1;
    z-index: 1;
    height: auto;
}

.CodeMirror-code {
    line-height: 22px;
    font-size: 17px;
}

.code-mode-select {
    position: absolute;
    z-index: 2;
    right: 10px;
    top: 10px;
    max-width: 130px;
}
</style>


