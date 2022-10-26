<template>
  <div>monitor</div>
</template>

<script>
export default {
  name: "monitor",
  data() {
    return {

    }
  },
  methods:{
    countByDay(){
      this.axios.post("/access/countByDay", {
        "start": "2022-10-20",
        "end": "2022-10-25"
      }).then((response) => {
        this.$message.success("Success")

      }).catch((error) => {
        this.$message.error("Failed")
      })
    }
  },
  created(){
    this.countByDay()
  }
}
</script>

<style scoped>

</style>