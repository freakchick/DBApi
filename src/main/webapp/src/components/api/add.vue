<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">返回</el-button>
    <h2>创建api</h2>

    <common ref="apiAdd"></common>

    <el-button @click="save" type="primary" plain>保存</el-button>

  </div>
</template>

<script>
import common from '@/components/api/common'

export default {
  data() {
    return {}
  },
  components: {common},
  methods: {

    save() {
      const detail = this.$refs.apiAdd.detail
      detail.datasourceId = this.$refs.apiAdd.$refs.sqlCode.datasourceId
      detail.sql = this.$refs.apiAdd.$refs.sqlCode.codemirror.getValue()
      detail.params = JSON.stringify(detail.params)

      this.axios.post("/apiConfig/add", detail).then((response) => {
        if (response.data.success) {
          this.$message.success(response.data.msg)
        } else {
          this.$message.error(response.data.msg)
        }

      }).catch((error) => {
        this.$message.error("失败")
      })
    }
  },
  created() {

  }
}
</script>

<style scoped>


</style>
