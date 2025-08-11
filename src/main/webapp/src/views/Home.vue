<template>
  <div class="home">
    <el-container>
      <el-header>

        <el-menu
            default-active="1"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="#00a0e9"
            text-color="#fff"
            active-text-color="#01293b"
            router>

          <el-menu-item index="/datasource">
            <template slot="title">
              <span class="">数据源</span>
            </template>
          </el-menu-item>

          <el-menu-item index="/api">
            <template slot="title">
              <span class="">api</span>
            </template>
          </el-menu-item>

          <el-submenu index="9">
            <template slot="title">
              <span class="">关于</span>
            </template>
            <el-menu-item index="/about/donate">捐赠</el-menu-item>

          </el-submenu>
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
  font-size: 17px;
  font-weight: 700;
}

</style>
