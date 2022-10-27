<template>
  <div>
    <el-tabs v-model="activeName">
    <el-tab-pane label="汇总" name="first">
      <chart></chart>
    </el-tab-pane>
    <el-tab-pane label="明细查询" name="second">配置管理</el-tab-pane>
 
  </el-tabs>
   
  </div>
</template>

<script>
import chart from "@/components/monitor/chart";
import Chart from './chart.vue';
export default {
  name: "monitor",
  data() {
    return {
     activeName :"first"
    };
  },
  components: { chartChart },
  methods: {
    
  },
  created() {
    
  },
};
</script>

<style scoped>
</style>