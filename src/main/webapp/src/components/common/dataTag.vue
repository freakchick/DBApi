<template>


  <div class="mytag">
    <span class="left">{{ name }}</span>
    <span class="right">{{ type }}</span>
  </div>

</template>

<script>
export default {
  name: "dataTag",
  props: ["name", "type"]
}
</script>

<style scoped>
.mytag {
  margin: 0 4px 2px 0;
  display: inline-block;
  /*overflow-wrap: normal;*/
}

.mytag span {

  /*display: inline-block;*/
  font-size: 12px;
  line-height: 12px;
  color: cornsilk;
}

.left {
  padding: 4px 4px 4px 6px;
  background-color: #40485b;
  border-radius: 4px 0 0 4px;

}

.right {
  padding: 4px 6px 4px 4px;
  background-color: #409eff;
  border-radius: 0 4px 4px 0;
}
</style>
