<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">返回</el-button>
    <h2>添加token</h2>
    <el-form label-width="100px">

      <el-form-item label="token">
        <el-input v-model="token">
          <el-button slot="append" icon="el-icon-search" @click="generate">生成token</el-button>
        </el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="note"></el-input>
      </el-form-item>
      <el-form-item label="有效期">
        <el-date-picker v-model="expire" type="datetime" placeholder="不选择表示永久有效"></el-date-picker>
      </el-form-item>

    </el-form>

    <el-button type="primary" @click="save" plain>保存</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      expire: null,
      token: null,
      note: null
    }
  },
  methods: {

    save() {
      if (this.expire != null)
        this.expire = this.expire.getTime()

      this.axios.post("/token/add/", {token: this.token, note: this.note, expire: this.expire}).then((response) => {
        this.$message.success("创建token成功")
      }).catch((error) => {
      })
    },
    generate() {
      this.axios.post("/token/generate/").then((response) => {
        this.token = response.data
      }).catch((error) => {
      })
    }
  }
}
</script>

<style scoped>

</style>