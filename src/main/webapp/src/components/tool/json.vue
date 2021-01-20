<template>
  <div>
    <el-button type="primary" plain @click="format" size="small"><i class="iconfont icon-format" style="font-size: 10px"/>json格式化</el-button>
    <el-button type="primary" plain size="small" v-clipboard:copy="value" v-clipboard:success="onCopy"
               v-clipboard:error="onError"><i class="el-icon-document-copy"/>复制
    </el-button>
    <el-input class="text" type="textarea" v-model="value" ref="jsonStr" placeholder="输入json字符串"
              :autosize="{ minRows: 10, maxRows: 30 }"></el-input>
  </div>
</template>

<script>
export default {
  name: "json",
  data() {
    return {
      value: ""
    }
  },
  methods: {
    format() {
      const o = JSON.parse(this.value)
      this.value = JSON.stringify(o, null, 4)
    },
    onCopy: function (e) {
      this.$refs.jsonStr.select()
      this.$message.success("已复制到剪贴板")
    },
    onError: function (e) {
      this.$message.error("复制失败")
    }
  }
}
</script>

<style scoped>
.text {
  margin-top: 10px;
}
</style>