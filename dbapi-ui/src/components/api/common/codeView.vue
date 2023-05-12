<template>
  <div class="code-view-container">
    <h1 ref="displayName" :class="{'display-name-unfirst':!isFirst,'display-name-first':isFirst}">{{langDisplayName}}</h1>
    <el-card @mouseover.native="onMouseOverCard" @mouseleave.native="onMouseLeaveCard">
      <pre>{{code}}</pre>
      <!-- <codemirror class="code-block" ref="codemirror" :value="code" :options="cmOptions" @ready="onCmReady" @mouseenter.native="onMouseEnterCodemirror">
      </codemirror> -->
    </el-card>
    <el-button ref="btn" v-show="btnVisiable" class="code-btn" plain size="mini" :icon="iconName" @click="copyCode" @mouseover.native="onMouseOverBtn" />
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
      btnVisiable: false,
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
    this.cmOptions.mode = this.mode;
  },
  methods: {
    onMouseEnterCodemirror() {
      this.showBtn();
      this.$nextTick(() => {
        const { width: containerWidthV } = this.$el.getBoundingClientRect();
        const { height: displayNameheightV } =
          this.$refs.displayName.getBoundingClientRect();
        this.$refs.btn.$el.style.top = `${displayNameheightV + 15}px`;
        this.$refs.btn.$el.style.left = `${containerWidthV - 30}px`;
      });
    },
    onMouseOverBtn() {
      this.showBtn();
    },
    onMouseOverCard() {
      this.showBtn();
    },
    onMouseLeaveCard() {
      this.hideBtn();
    },
    showBtn() {
      this.btnVisiable = true;
    },
    hideBtn() {
      this.btnVisiable = false;
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
  watch: {
    value: {
      immediate: true,
      handler(val) {
        this.code = val;
        this.refresh();
      },
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