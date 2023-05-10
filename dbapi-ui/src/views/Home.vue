<template>
  <div class="home">
    <div>
      <home-header></home-header>
    </div>

    <div class="main">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import homeHeader from '@/components/header/homeHeader.vue'

export default {
  data() {
    return {
      msg: "我是home 组件"
    }
  },
  name: 'Home',
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    getMode() {
      this.axios.post("/system/mode").then((response) => {
        this.$store.commit('setMode', response.data)
      }).catch((error) => {

      })
    }

  },
  components: {homeHeader},
  created() {
    this.getMode()

  }
}
</script>

<style scoped lang="less">
.iconfont {
  color: #fff;
  font-size: 20px !important;
  font-weight: 700;
  padding-right: 3px;
}

.main {
  // background-color: #fff;
  //max-width: 1500px;
  //margin: 0 auto;
  //border-radius: 5px;
  //box-shadow: 1px 1px 5px #72767b;
  padding: 20px;
  //min-height: calc(100vh - 100px);
}

</style>
