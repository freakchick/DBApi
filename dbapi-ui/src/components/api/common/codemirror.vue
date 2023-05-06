<template>
  <div class="in-coder-panel">
    <textarea :ref="textareaRef" v-model="code" autofocus="true"></textarea>
    <el-button @click="show">show</el-button>
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
      code: "select gg ",
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
    show() {
      alert(this.code);
    },
    _initialize() {
      this.coder = CodeMirror.fromTextArea(
        this.$refs[this.textareaRef],
        this.options
      );
      this.coder.setSize("90%", "400px");
      this.coder.setValue(this.value || this.code);
      this.coder.on("change", (coder, changeObj) => {
        this.code = coder.getValue();
        if (/^[a-zA-Z.]/.test(changeObj.text[0])) {
          coder.showHint();
        }
        this.$emit('setCode',this.code)
      });


    },
  },
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


