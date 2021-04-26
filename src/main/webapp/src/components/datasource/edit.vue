<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">返回</el-button>
    <h2>修改数据源</h2>
    <common :id="$route.query.id" ref="detail"></common>

    <el-button type="primary" @click="save" plain>
      保存
    </el-button>
  </div>
</template>

<script>
import common from '@/components/datasource/common'

export default {
  name: "edit",
  data() {
    return {}
  },
  methods: {
    save() {
      const data = this.$refs.detail.detail
      this.axios.post("/datasource/update", {
        "name": data.name,
        "note": data.note,
        "url": data.url,
        "username": data.username,
        "password": data.password,
        "type": data.type,
        "id": data.id
      }).then((response) => {
        this.$message.success("修改成功")
      }).catch((error) => {
        this.$message.error("修改失败")
      })
    }
  },
  created() {

  },
  components: {common}
}
</script>

<style scoped>

</style>
