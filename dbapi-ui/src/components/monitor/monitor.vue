<template>
  <div>
    <el-tabs v-model="activeName">
    <el-tab-pane label="汇总" name="first">
      <chart></chart>
    </el-tab-pane>
    <el-tab-pane label="明细查询" name="second">
      <record></record>
    </el-tab-pane>
 
  </el-tabs>
   
  </div>
</template>

<script>
import chart from "@/components/monitor/chart";
import record from "@/components/monitor/record";

export default {
  name: "monitor",
  data() {
    return {
     activeName :"first"
    };
  },
  components: { chart,record},
  methods: {
    
  },
  created() {
    
  },
};
</script>

<style scoped>
</style>