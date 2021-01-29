<template>
  <div>
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">返回</el-button>
    <h2>api详情</h2>

    <common :id="$route.query.id"></common>
  </div>
</template>

<script>
import common from '@/components/api/common'

export default {
  data() {
    return {

    }
  },
  components: {common},
  methods: {

  },
  created() {

  }
}
</script>

<style scoped>


</style>
