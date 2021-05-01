<template>
  <div class="box">
    <div class="content">
      <div class="item header">
        DBAPI
      </div>
      <div class="item">
        <el-input v-model="username" placeholder="用户名"></el-input>
      </div>
      <div class="item">
        <el-input v-model="password" placeholder="密码" type="password"></el-input>
      </div>
      <div class="item">
        <div class="buttton" @click="login">登&nbsp;录</div>
      </div>
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
      this.axios.post("/user/login", {username: this.username, password: this.password}).then((response) => {
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
html {
  background-color: #c7edcc;
}

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
  /*box-shadow: 10px 10px 5px #888888;*/
}

.item {
  width: 400px;
  height: 70px;
}

.buttton {
  width: 400px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  background-color: #06b176;
  border-radius: 10px;
  color: #ffffff;
  font-weight: 700;
  font-size: 20px;
}


.header {
  text-align: center;
  color: #06b176;
  font-weight: 700;
  font-size: 40px;
}

.buttton:hover {
  background-color: #07a863;
  font-size: 21px;
  cursor: pointer;
}
</style>