<template>
  <div>
    <div class="box">
      <div class="input">
        <el-input v-model="oldPassword" :placeholder="$t('m.old_pwd')" type="password"></el-input>
      </div>
      <div class="input">
        <el-input v-model="newPassword" :placeholder="$t('m.input_pwd')" type="password"></el-input>
      </div>
      <div class="input">
        <el-input v-model="repassword" :placeholder="$t('m.input_pwd_again')" type="password"></el-input>
      </div>
      <el-button @click="resetPassword()">{{ $t('m.save') }}</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "password",
  data() {
    return {
      oldPassword: null,
      newPassword: null,
      repassword: null,
    }
  },
  methods: {
    resetPassword() {
      if (this.newPassword !== this.repassword) {
        this.$message.error("The new passwords entered twice are different！")
        return
      }
      if (this.newPassword == '' || this.newPassword == null) {
        this.$message.warning("Please input password！")
        return
      }

      this.axios.post("/user/resetPassword",
        {oldPassword: this.oldPassword, newPassword: this.newPassword, userId: localStorage.getItem("userId")})
        .then((response) => {
          if (response.data.success) {
            this.$message.success(response.data.msg)
          } else {
            this.$message.warning(response.data.msg)
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
    /*display: -webkit-flex;*/
    align-items: center;
    justify-content: center;
    flex-direction: column;
}

.input {
    margin-bottom: 10px;
    width: 400px;
}
</style>