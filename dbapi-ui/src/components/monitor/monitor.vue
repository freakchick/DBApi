<template>
  <div style="padding: 20px">
    <el-tabs v-model="activeName">
    <el-tab-pane :label="$t('m.summary')" name="first">
      <chart></chart>
    </el-tab-pane>
    <el-tab-pane :label="$t('m.detail')" name="second">
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