<template>
  <div style="display: inline-block">
    <i class="iconfont icon-my-SQL db" v-if="type == 'mysql'"></i>
    <i class="iconfont icon-postgresql db" v-if="type == 'postgresql'"></i>
    <i class="iconfont icon-hive db" v-if="type == 'hive'"></i>
    <i class="iconfont icon-SQLServer db" v-if="type == 'sqlserver'"></i>
    <i class="iconfont icon-clickhouse2 db" v-if="type == 'clickhouse'"></i>
    <i class="iconfont icon-Kylin db" v-if="type == 'kylin'"></i>
    <i class="iconfont icon-oracle db" v-if="type == 'oracle'"></i>
    <i class="iconfont icon-TDengine db" v-if="type == 'TDengine'"></i>
    <i class="iconfont icon-doris db" v-if="type == 'doris'"></i>
    <i class="iconfont icon-oceanbase db" v-if="type == 'oceanbase'"></i>
    <i class="iconfont icon-GreenPlum db" v-if="type == 'greenplum'"></i>
    <i class="iconfont icon-OTHER db" v-if="type == 'others'"></i>
  </div>
</template>

<script>
export default {
  name: "dbIcon",
  props:['type']
}
</script>

<style scoped>
.db {
  color: #0698a5;
  font-size: 18px;
  font-weight: 500;
  margin-right: 5px;
}
</style>