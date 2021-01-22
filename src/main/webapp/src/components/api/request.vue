<template>
  <div>
    <div>http://{{ address }}/api/{{ path }}</div>

    <el-form label-width="100px" width="200">
      <el-form-item :label="item.name" v-for="item in params">
        <el-input v-model="item.value"></el-input>
      </el-form-item>

    </el-form>
    <el-button @click="request">请求</el-button>

    <el-input type="textarea" v-model="response"></el-input>

    <h4>请求示例：</h4>

  </div>
</template>

<script>
export default {
  name: "request",
  data() {
    return {
      api: {},
      params: [],
      path: null,
      address: null,
      response: null
    }
  },
  methods: {
    getDetail(id) {
      this.axios.post("/apiConfig/detail/" + id).then((response) => {
        this.path = response.data.path
        this.params = JSON.parse(response.data.params)
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    getAddress() {
      this.axios.post("/apiConfig/getIPPort").then((response) => {
        this.address = response.data
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    request() {
      let p = {}
      this.params.forEach(t => {
        p[t.name] = t.value
      })
      let url = `http://${this.address}/api/${this.path}`
      this.axios.post("/apiConfig/request",
          {url: url, "params": JSON.stringify(p)}
      ).then((response) => {
        console.log(response.data)
        if (response.data.success){
          this.response = response.data.data
        }
      }).catch((error) => {
        this.$message.error("失败")
      })
    }
  },
  created() {
    this.getDetail(this.$route.query.id)
    this.getAddress()
  }

}
</script>

<style scoped>

</style>
