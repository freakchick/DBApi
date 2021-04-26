<template>
  <div class="home">
    <el-container>
      <el-header style="background-color: #06b176">
        <!--        <span>DBAPI</span>-->

        <el-menu
            default-active="1"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="#06B176"
            text-color="#fff"
            active-text-color="#01293b"
            router>

          <el-menu-item index="/datasource">
            <template slot="title">
              <span class="menu">数据源</span>
            </template>
          </el-menu-item>

          <el-menu-item index="/api">
            <template slot="title">
              <span class="menu">API</span>
            </template>
          </el-menu-item>

          <el-menu-item index="/token">
            <template slot="title">
              <span class="menu">权限</span>
            </template>
          </el-menu-item>
        </el-menu>

      </el-header>
      <el-main>
          <router-view></router-view>
      </el-main>
      <!--      <el-footer>Footer</el-footer>-->
    </el-container>
  </div>
</template>

<script>
// @ is an alias to /src

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

  },
  created() {


  }
}
</script>

<style scoped>
.icon {
  color: #ffffff;
  font-size: 23px !important;
  font-weight: normal;
}

.menu {
  font-size: 20px;
  font-weight: 700;
}

.home {
  width: auto;
}

</style>
