<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">返回</el-button>
    <h2>修改api</h2>

    <common :id="$route.query.id" ref="apiEditCommon"></common>

    <el-button @click="save">保存</el-button>
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
      const detail = this.$refs.apiEditCommon.detail
      let p = {
        name: detail.name,
        path: detail.path,
        note: detail.note,
        groupId: detail.groupId,
        previlege: detail.previlege,
        cachePlugin: detail.cachePlugin,
        transformPlugin: detail.transformPlugin,
        datasourceId: this.$refs.apiEditCommon.$refs.sqlCode.datasourceId,
        sql: this.$refs.apiEditCommon.$refs.sqlCode.codemirror.getValue().trim(),
        params: JSON.stringify(detail.params),
        id: this.$route.query.id
      }

      // console.log(detail)
      if (p.sql == null || p.datasourceId == null || p.name == null
          || p.path == null || p.groupId == null) {
        this.$message.error("必填项未填")
        return
      }

      this.axios.post("/apiConfig/update", p).then((response) => {
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
