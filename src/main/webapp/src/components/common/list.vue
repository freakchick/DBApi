<template>
  <ul>
    <li v-for="(item,index) in dataList" :key="index">{{ item }}</li>
  </ul>
</template>

<script>
export default {
  name: "list",
  data() {
    return {

    }
  },
  props: ["dataList"],
}
</script>

<style scoped>
li {
  border-bottom: 1px solid #8c939d;
}

</style>