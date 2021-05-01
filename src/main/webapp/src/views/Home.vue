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
              <span class="menu"><i class="iconfont icon-database"></i>数据源</span>
            </template>
          </el-menu-item>

          <el-menu-item index="/api">
            <template slot="title">
              <span class="menu"><i class="iconfont icon-api"></i>API</span>
            </template>
          </el-menu-item>

          <el-menu-item index="/token">
            <template slot="title">
              <span class="menu"><i class="iconfont icon-quanxian"></i>权限</span>
            </template>
          </el-menu-item>

          <el-submenu index="">
            <template slot="title">
              <span class="menu"><i class="iconfont icon-shezhi"></i>系统设置</span>
            </template>
            <el-menu-item index="/setting/password">修改密码</el-menu-item>
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
.iconfont {
  color: #fff;
  font-size: 20px !important;
  font-weight: 700;
  padding-right: 3px;
}

.menu {
  font-size: 20px;
  font-weight: 700;
}

.home {
  width: auto;
}

</style>
