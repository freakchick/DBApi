<template>
  <div>
    <div>http://{{ address }}/api/{{ path }}</div>

    <el-form label-width="100px" width="200" size="mini">
      <el-form-item :label="item.name" v-for="item in params">
        <el-input v-model="item.value"></el-input>
      </el-form-item>

    </el-form>
    <el-button @click="request">请求</el-button>

    <el-input type="textarea" v-model="response" :autosize="{ minRows: 5, maxRows: 20 }" class="my"></el-input>
    <el-button @click="format">格式化json</el-button>
    <h4>请求示例：</h4>
    <el-collapse accordion>
      <el-collapse-item title="shell" name="1">
        <div>#re = requests.post("http://localhost:12345/dolphinscheduler//realtime/summary/publish", {"key": 7,"hashkey": 7})


          print(re.text)</div>
      </el-collapse-item>
      <el-collapse-item title="python" name="2">
        <div>控制反馈：通过界面样式和交互动效让用户可以清晰的感知自己的操作；</div>
        <div>页面反馈：操作后，通过页面元素的变化清晰地展现当前状态。</div>
      </el-collapse-item>
      <el-collapse-item title="java" name="3">
        <div>简化流程：设计简洁直观的操作流程；</div>
        <div>清晰明确：语言表达清晰且表意明确，让用户快速理解进而作出决策；</div>
        <div>帮助用户识别：界面简单直白，让用户快速识别而非回忆，减少用户记忆负担。</div>
      </el-collapse-item>
      <el-collapse-item title="javascript" name="4">
        <div>用户决策：根据场景可给予用户操作建议或安全提示，但不能代替用户进行决策；</div>
        <div>结果可控：用户可以自由的进行操作，包括撤销、回退和终止当前操作等。</div>
      </el-collapse-item>
    </el-collapse>
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
    },
    format() {
      const o = JSON.parse(this.response)
      this.response = JSON.stringify(o, null, 4)
    },
  },
  created() {
    this.getDetail(this.$route.query.id)
    this.getAddress()
  }

}
</script>

<style scoped>
.my >>> .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
  /*font-size: 18px;*/
}
</style>
