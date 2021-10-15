<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">返回</el-button>
    <h2>添加数据源</h2>
    <common ref="detail"></common>

    <el-button type="primary" @click="save" plain>
      保存
    </el-button>
  </div>
</template>

<script>
import common from '@/components/datasource/common'

export default {
  data() {
    return {}
  },
  methods: {
    save() {
      const data = this.$refs.detail.detail
      this.axios.post("/datasource/add", {
        "name": data.name,
        "note": data.note,
        "url": data.url,
        "username": data.username,
        "password": data.password,
        "type": data.type,
        "driver": data.driver,
        "tableSql": data.tableSql
      }).then((response) => {
        this.$message.success("添加成功")
        this.$router.push("/datasource")
      }).catch((error) => {
        this.$message.error("添加失败")
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
