<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">{{$t('m.back')}}</el-button>
    <h2>{{$t('m.api_detail')}}</h2>

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

<style scoped lang="less">

.mycontent{
    padding: 20px;
}
</style>
