<template>
  <div class="box">
    <div class="content">
      <el-form label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="username"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="password"></el-input>
        </el-form-item>


        <el-form-item>
          <el-button type="primary" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      username: null,
      password: null

    }
  },
  methods: {
    login() {
      this.axios.post("/user/login/", {username: this.username, password: this.password}).then((response) => {
        console.log(response.data)
        if (response.data.success) {
          localStorage.setItem("token", response.data.msg)

          this.$router.push("/api")
        } else {
          this.$message.error(response.data.msg)
        }
      }).catch((error) => {
        this.$message.error(error)
      })
    }
  }
}
</script>

<style scoped>
.box {
  display: flex;
  display: -webkit-flex;
  /*border: 1px solid #0000FF;*/
  /*height: 200px;*/
  /*width: 400px;*/
  align-items: center;
  justify-content: center;
  margin-top: 200px;

}

.content {
  box-shadow: 10px 10px 5px #888888;
}
</style>