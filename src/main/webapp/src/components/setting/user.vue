<template>
  <div>
    <h2>修改管理员密码</h2>
    <div class="box">
      <div class="input">
        <el-input v-model="password" placeholder="输入密码" type="password"></el-input>
      </div>
      <div class="input">
        <el-input v-model="repassword" placeholder="请再次输入密码" type="password"></el-input>
      </div>
      <el-button @click="resetPassword()">修改</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "user",
  data() {
    return {
      password: null,
      repassword: null,
    }
  },
  methods:{
    resetPassword(){
      if (this.password !== this.repassword){
        this.$message.error("两次输入的密码不一样！")
        return
      }
      if (this.password == '' || this.password == null){
        this.$message.warning("请输入密码！")
        return
      }

      this.axios.post("/user/resetPassword", {password: this.password}).then((response) => {
        this.$message.success("修改成功")
      }).catch((error) => {
        this.$message.error(error)
      })
    }
  }
}
</script>

<style scoped>
.box{
  display: flex;
  /*display: -webkit-flex;*/
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
.input {
  margin-bottom: 10px;
  width: 300px;
}
</style>