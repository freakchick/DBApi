<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">{{ $t('m.back') }}
    </el-button>
    <h2>{{ $t('m.create_api') }}</h2>

    <common ref="apiAdd"></common>

    <el-button @click="save" type="primary" plain style="margin:10px 0">{{ $t('m.save') }}</el-button>

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
      const sqlList = this.$store.getters.getSql
      let p = {
        name: detail.name,
        path: detail.path,
        note: detail.note,
        groupId: detail.groupId,
        previlege: detail.previlege,
        cachePlugin: detail.cachePlugin,
        cachePluginParams: detail.cachePluginParams,
        // transformPlugin: detail.transformPlugin,
        // transformPluginParams: detail.transformPluginParams,
        datasourceId: this.$refs.apiAdd.$refs.sqlCode.datasourceId,
        sqlList: sqlList,
        params: JSON.stringify(detail.params),
        contentType: detail.contentType,
        jsonParam: detail.jsonParam,
        openTrans: detail.openTrans,
        alarmPlugin: detail.alarmPlugin,
        alarmPluginParam: detail.alarmPluginParam
      }

      console.log(p)

      if (p.sql == "" || p.datasourceId == null || p.name == null
          || p.path == null || p.groupId == null) {
        this.$message.error("Something Required!")
        return
      }

      this.axios.post("/apiConfig/add", p,
          {headers: {'Content-Type': 'application/json'}}
      ).then((response) => {
        if (response.data.success) {
          this.$message.success(response.data.msg)
          this.$router.push("/api")
        } else {
          this.$message.error(response.data.msg)
        }

      }).catch((error) => {
        this.$message.error("Failed")
      })
    }
  },
  created() {

  }
}
</script>

<style scoped>


</style>
