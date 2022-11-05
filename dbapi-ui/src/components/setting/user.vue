<template>
  <div>
    <h2>{{$t('m.change_pass')}}</h2>
    <div class="box">
      <div class="input">
        <el-input v-model="password" :placeholder="$t('m.input_pwd')" type="password"></el-input>
      </div>
      <div class="input">
        <el-input v-model="repassword" :placeholder="$t('m.input_pwd_again')" type="password"></el-input>
      </div>
      <el-button @click="resetPassword()">{{$t('m.save')}}</el-button>
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
        this.$message.error("The passwords entered twice are different！")
        return
      }
      if (this.password == '' || this.password == null){
        this.$message.warning("Please input password！")
        return
      }

      this.axios.post("/user/resetPassword", {password: this.password}).then((response) => {
        this.$message.success("Success")
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