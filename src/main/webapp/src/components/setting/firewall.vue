<template>
  <div>
    <h2>IP防火墙设置</h2>
    <el-form label-width="120px">
      <el-form-item label="API防火墙状态">
        <el-switch
            v-model="status"
            active-color="#13ce66"
            active-value="on"
            inactive-value="off"
            active-text="开启"
            inactive-text="关闭">
        </el-switch>
      </el-form-item>
      <div v-show="status=='on'">
        <el-form-item label="模式">
          <el-radio-group v-model="mode" @change="modeChange">
            <el-radio label="black">黑名单</el-radio>
            <el-radio label="white">白名单</el-radio>
          </el-radio-group>
          <el-alert title="除了黑名单列表中的IP禁止访问API，其他IP一律允许访问" type="warning" :closable="false"
                    v-show="mode == 'black'"></el-alert>
          <el-alert title="只有白名单列表中的IP才允许访问API，其他IP一律禁止访问" type="warning" :closable="false"
                    v-show="mode == 'white'"></el-alert>
        </el-form-item>
        <el-form-item label="IP黑名单列表" v-show="mode == 'black'">
          <el-input type="textarea" :autosize="{ minRows: 8, maxRows: 20 }"
                    v-model="blackIP"
                    placeholder="每行填写一个ip，多个ip用换行符隔开">

          </el-input>
        </el-form-item>
        <el-form-item label="IP白名单列表" v-show="mode == 'white'">
          <el-input type="textarea"
                    :autosize="{ minRows: 8, maxRows: 20 }"
                    v-model="whiteIP" placeholder="每行填写一个ip，多个ip用换行符隔开">

          </el-input>
        </el-form-item>
      </div>
      <el-form-item>
        <el-button type="primary" @click="submit" plain>提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      status: "on",
      mode: "black",
      blackIP: "",
      whiteIP: ""
    }
  },
  methods: {
    modeChange(p) {
      console.log(p)
    },
    submit() {
      this.axios.post("/firewall/save",
          {status: this.status, mode: this.mode, whiteIP: this.whiteIP, blackIP: this.blackIP}
      ).then((response) => {
        this.$message.success("修改成功")
      }).catch((error) => {
        this.$message.error(error)
      })
    }
  },
  created() {
    this.axios.post("/firewall/detail"
    ).then((response) => {
      console.log(response.data)
      this.status = response.data.status
      this.mode = response.data.mode
      this.blackIP = response.data.blackIP
      this.whiteIP = response.data.whiteIP
    }).catch((error) => {
      this.$message.error("error")
    })
  }
}
</script>

<style scoped>

</style>