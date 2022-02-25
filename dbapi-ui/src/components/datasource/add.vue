<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">{{$t('m.back')}}</el-button>
    <h2>{{$t('m.create_ds')}}</h2>
    <common ref="detail"></common>

    <el-button type="primary" @click="save" plain>{{$t('m.save')}}</el-button>
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
        "edit_password": data.edit_password,
        "type": data.type,
        "driver": data.driver,
        "tableSql": data.tableSql
      }).then((response) => {
        this.$message.success("Success")
        this.$router.push("/datasource")
      }).catch((error) => {
        this.$message.error("Failed")
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
