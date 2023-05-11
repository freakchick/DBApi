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
      debugger
      if(!this.$refs.apiAdd.checkValue()){
        return;
      }
      const detail = this.$refs.apiAdd.detail

      const executors = this.$refs.apiAdd.$refs.executor
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
        globalTransformPlugin: detail.globalTransformPlugin

      }
      console.log(p)
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
        this.$message.error("Create API Failed")
      })
    }
  },
  created() {

  }
}
</script>

<style scoped>


</style>
