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
      debugger
      if(!this.$refs.apiEditCommon.checkValue()){
        return;
      }
      const detail = this.$refs.apiEditCommon.detail
      const executors = this.$refs.apiEditCommon.$refs.executor
      const taskJson = executors.map(node => node.getTaskJson())
      let p = {
        name: detail.name,
        path: detail.path,
        groupId: detail.groupId,
        note: detail.note,
        contentType: detail.contentType,
        jsonParam: detail.jsonParam,
        paramsJson: detail.paramsJson,
        access: detail.access,
        taskJson: taskJson,
        cachePlugin: detail.cachePlugin,
        alarmPlugins: detail.alarmPlugins,
        globalTransformPlugin: detail.globalTransformPlugin,
        id: this.$route.query.id
      }
      console.log(p)

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
        this.$message.error("Failed")
      })
    }
  },
  created() {

  }
}
</script>

<style scoped>
.mycontent{
    padding: 20px;
}

</style>
