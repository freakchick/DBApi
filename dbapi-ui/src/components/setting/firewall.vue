<template>
  <div>
    <h2>{{$t('m.firewall_settings')}}</h2>
    <el-form label-width="200px">
      <el-form-item :label="$t('m.status')">
        <el-switch
            v-model="status"
            active-color="#13ce66"
            active-value="on"
            inactive-value="off"
            :active-text="$t('m.on')"
            :inactive-text="$t('m.off')">
        </el-switch>
      </el-form-item>
      <div v-show="status=='on'">
        <el-form-item :label="$t('m.mode')">
          <el-radio-group v-model="mode" @change="modeChange">
            <el-radio label="black">{{$t('m.black_list')}}</el-radio>
            <el-radio label="white">{{$t('m.white_list')}}</el-radio>
          </el-radio-group>
          <el-alert :title="$t('m.black_tip')" type="warning" :closable="false"
                    v-show="mode == 'black'"></el-alert>
          <el-alert :title="$t('m.white_tip')" type="warning" :closable="false"
                    v-show="mode == 'white'"></el-alert>
        </el-form-item>
        <el-form-item :label="$t('m.ip_list')" v-show="mode == 'black'">
          <el-input type="textarea" :autosize="{ minRows: 8, maxRows: 20 }"
                    v-model="blackIP"
                    :placeholder="$t('m.ip_list_tip')">

          </el-input>
        </el-form-item>
        <el-form-item :label="$t('m.ip_list')" v-show="mode == 'white'">
          <el-input type="textarea"
                    :autosize="{ minRows: 8, maxRows: 20 }"
                    v-model="whiteIP" :placeholder="$t('m.ip_list_tip')">

          </el-input>
        </el-form-item>
      </div>
      <el-form-item>
        <el-button type="primary" @click="submit" plain>{{$t('m.save')}}</el-button>
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
        this.$message.success("Success")
      }).catch((error) => {
        this.$message.error("Failed")
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
      this.$message.error("Failed")
    })
  }
}
</script>

<style scoped>

</style>