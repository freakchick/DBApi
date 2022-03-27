<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">{{ $t('m.back') }}
    </el-button>
    <h2>{{ $t('m.update_api') }}</h2>

    <common :id="$route.query.id" ref="apiEditCommon"></common>

    <el-button @click="save" style="margin: 10px 0">{{ $t('m.save') }}</el-button>
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
      const sqlList = this.$store.getters.getSql
      let p = {
        name: detail.name,
        path: detail.path,
        note: detail.note,
        groupId: detail.groupId,
        previlege: detail.previlege,
        cachePlugin: detail.cachePlugin,
        transformPlugin: detail.transformPlugin,
        cachePluginParams: detail.cachePluginParams,
        transformPluginParams: detail.transformPluginParams,
        datasourceId: this.$refs.apiEditCommon.$refs.sqlCode.datasourceId,
        sqlList: sqlList,
        params: JSON.stringify(detail.params),
        contentType: detail.contentType,
        jsonParam: detail.jsonParam,
        openTrans: detail.openTrans,
        mail: detail.mail,
        id: this.$route.query.id
      }

      // console.log(detail)
      if (p.sql == "" || p.datasourceId == null || p.name == null
          || p.path == null || p.groupId == null) {
        this.$message.error("必填项未填")
        return
      }

      this.axios.post("/apiConfig/update", p,
          {headers: {'Content-Type': 'application/json'}}
      ).then((response) => {
        if (response.data.success) {
          this.$message.success(response.data.msg)
          this.$router.push("/api")
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
