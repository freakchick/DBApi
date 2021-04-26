<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">返回</el-button>
    <h2>数据源详情</h2>
    <common :id="$route.query.id"></common>

  </div>
</template>

<script>
import common from '@/components/datasource/common'

export default {
  data(){
    return {}
  },
  methods:{
    getDetail(){

    }
  },
  created(){

  },
  components: {common}
}
</script>

<style scoped>

</style>
