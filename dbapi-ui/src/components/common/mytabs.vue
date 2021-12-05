<template>
  <div class="tabs">


    <slot></slot>
    <ul>
      <li
          v-for="(item, index) in labels"
          @click="clickTab(item, index)"
          :key="index"
          :class="{ 'active' : item == currentName }"
      >
        {{ item }}
      </li>
      <li class="el-icon-circle-plus" @click="addTab">ADD</li>
    </ul>
  </div>
</template>
<script>
export default {
  name: "mytabs",
  data() {
    return {
      currentName: null,
      panes: [],
      labels: [],
    };
  },
  methods: {
    addTab(){
      this.$emit('addTab')
    },
    clickTab(name, index) {
      // debugger;
      //标签名显示选中
      this.currentName = name;
    },
    calcPaneInstances() {
      if (this.$slots.default) {
        const paneSlots = this.$slots.default.filter(
            (vnode) =>
                vnode.tag &&
                vnode.componentOptions &&
                vnode.componentOptions.Ctor.options.name === "mytabPane"
        );
        const panes = paneSlots.map(
            ({ componentInstance }) => componentInstance
        );
        //标签切换 currentName set也会触发updated,所以需要判断slots是不是真的变化了
        const panesChanged = !(
            panes.length === this.panes.length &&
            panes.every((pane, index) => pane === this.panes[index])
        );
        if (panesChanged) {
          this.panes = panes;
        }
      } else if (this.panes.length !== 0) {
        debugger;
        this.panes = [];
      }
    },
  },
  //收集子组件mytabPane的label,不能写在created
  mounted() {
    this.calcPaneInstances();
  },
  updated() {
    // 初次加载的时候
    //页面刚开始的时候还没有获取到用户权限信息，用户权限信息加载完后slot会变化
    //currentName set也会触发updated
    this.calcPaneInstances();
  },
  watch: {
    //页面刚开始的时候还没有获取到用户权限信息，用户权限信息加载完后slot会变化
    panes(newVal, oldVal) {
      this.labels = newVal.map((t) => t.label);
      if (this.labels.length > 0) {
        const contained = this.labels.some((t) => t === this.currentName);
        if (!contained) {
          this.currentName = this.labels[0];
        }
      } else {
        this.currentName = null;
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.tabs {
  background-color: rgba(234, 149, 149, 0.35);
  position: relative;
  ul {
    //background-color: #d22626;
    position: absolute;
    bottom: 0px;
    left: 30px;
    padding: 1px;
    display: flex;
    li {
      height: 30px;
      line-height: 30px;


      margin: 0 5px;
      padding: 0 10px;
      color: rgb(182, 182, 182);
      cursor: pointer;
      border: 1px solid #cccccc;
      border-top-left-radius: 10px;
      border-top-right-radius: 10px;
      &:hover{
        background-color: rgba(4, 103, 10, 0.11);
      }
    }
    .active {
      //border-bottom: 2px solid #931c7f;
      background-color: rgba(4, 103, 10, 0.27);
      color: rgb(10, 1, 1);
    }
  }
}
</style>
