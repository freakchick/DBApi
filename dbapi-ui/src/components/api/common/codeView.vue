<template>
  <div class="code-view-container">
    <h1 :class="{'display-name-unfirst':!isFirst,'display-name-first':isFirst}">{{langDisplayName}}</h1>
    <codemirror
      class="code-block"
      ref="codemirror"
      :options="cmOptions"
      @ready="onCmReady"
      @mouseenter.native="onMouseEnterCodemirror"
      @mouseleave.native="onMouseLeaveCodemirror"
    >
    </codemirror>
    <el-button
      ref="btn"
      v-show="showBtn"
      class="code-btn"
      plain
      size="mini"
      :icon="iconName"
      @click="copyCode"
      @mouseover.native="onMouseOverBtn"
      @mouseleave.native="onMouseLeaveBtn"
    />
  </div>
</template>

<script>
// import "codemirror/theme/idea.css";
import "codemirror/mode/shell/shell";
import "codemirror/mode/javascript/javascript";
import "codemirror/mode/go/go";
import "codemirror/mode/python/python";
import "codemirror/mode/clike/clike";
import "codemirror/addon/display/autorefresh";

const ICON = {
  COPY: "el-icon-document-copy",
  CHECK: "el-icon-check",
};
export default {
  name: "codeView",
  data() {
    return {
      code: "",
      cmOptions: {
        matchBrackets: true,
        autorefresh: true,
        lineNumbers: true,
        mode: "",
        theme: "idea",
        readOnly: true,
      },
      // el-icon-check el-icon-document-copy
      iconName: ICON.COPY,
      cmInstance: null,
      showBtn: false,
    };
  },
  props: {
    langDisplayName: {
      type: String,
      default: "",
    },
    width: {
      type: String,
      default: "100%",
    },
    height: {
      type: String,
      default: "100%",
    },
    value: {
      type: String,
      default: "",
    },
    mode: {
      type: String,
      default: "text/javascript",
    },
    isFirst: {
      type: Boolean,
      defautl: false,
    },
  },
  created() {
    this.code = this.value;
    this.cmOptions.mode = this.mode;
  },
  methods: {
    onMouseEnterCodemirror() {
      this.showBtn = true;
      this.$nextTick(() => {
        const { width: widthV } = this.$el.getBoundingClientRect();
        this.$refs.btn.$el.style.top = `${25}px`;
        this.$refs.btn.$el.style.left = `${widthV - 25}px`;
      });
    },
    onMouseOverBtn() {
      this.showBtn = true;
    },
    onMouseLeaveBtn() {
      this.showBtn = false;
    },
    onMouseLeaveCodemirror() {
      this.showBtn = false;
    },
    onCmReady(cm) {
      this.cmInstance = cm;
      cm.setValue(this.code);
      cm.setSize(this.width, this.height);
    },
    refresh() {
      if (this.$refs.codemirror?.refresh) {
        this.$refs.codemirror.refresh();
      }
    },
    copyCode() {
      this.$copyText(this.code);
      this.iconName = ICON.CHECK;
      setTimeout(() => {
        this.iconName = ICON.COPY;
      }, 300);
    },
  },
};
</script>
<style scoped>
.code-block {
  font-family: Arial, monospace;
  font-size: 16px;
}
.el-button--mini {
  padding: 3px 5px !important;
}
.code-btn {
  position: absolute;
  height: 25px;
  width: 25px;
}
.code-view-container {
  position: relative;
}
.display-name-first {
  margin-bottom: 10px;
  text-align: left;
}
.display-name-unfirst {
  margin-bottom: 10px;
  margin-top: 10px;
  text-align: left;
}
</style>