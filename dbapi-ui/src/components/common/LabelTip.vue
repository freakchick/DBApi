<template>

  <div class="labeltip">
    {{label}}
    <el-tooltip placement="top-start" effect="dark">
      <div slot="content">{{tip}}</div>
      <i class="el-icon-info tip"></i>
    </el-tooltip>
  </div>

</template>

<script>
export default {
  name: "LabelTip",
  props: ["label", "tip"],
};
</script>

<style scoped lang="less">
.labeltip {
//   font-weight: 700;
  .tip {
    color: #505050;
    font-size: 14px;
    font-weight: 100;
  }
}
</style>
