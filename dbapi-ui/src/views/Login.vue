<template>
  <div class="box body-bg">
    <div class="content">
      <div class="item header">
        DBAPI
      </div>
      <div class="item">
        <el-input
          v-model="username"
          :placeholder="$t('m.username')"
        ></el-input>
      </div>
      <div class="item">
        <el-input
          v-model="password"
          :placeholder="$t('m.password')"
          type="password"
        ></el-input>
      </div>
      <div class="item">
        <div
          class="buttton"
          @click="login"
        >{{$t('m.login')}}</div>
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
      password: null,
    };
  },
  methods: {
    login() {
      this.axios
        .post("/user/login", {
          username: this.username,
          password: this.password,
        })
        .then((response) => {
          if (response.data.success) {
            localStorage.setItem("token", response.data.msg);
            this.$router.push("/api");
          } else {
            this.$message.error(response.data.msg);
          }
        })
        .catch((error) => {
          this.$message.error(error);
        });
    },
  },
  created() {
    //bind login event to enter key
    document.onkeydown = (e) => {
      const key = window.event.keyCode;
      if (key == 13 || key == 100) {
        this.login();
      }
    };
  },
  destroyed() {
    //unbind login event to enter key when go to other pages
    document.onkeydown = null;
  },
};
</script>

<style scoped>
.box {
  display: flex;
  display: -webkit-flex;
  align-items: center;
  justify-content: center;
  /*margin-top: 200px;*/
}

.content {
  /*box-shadow: 10px 10px 5px #888888;*/
  /*margin-top: -100px;*/
  padding-bottom: 100px;
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

.body-bg {
  position: absolute;
  height: 100%;
  width: 100%;
  top: 0;
  left: 0;
  overflow-y: auto;
  background-color: #c7edcc;
}
</style>