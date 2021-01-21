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
<!--          <el-submenu index="8">-->
<!--            <template slot="title">-->
<!--              <span class="">工具</span>-->
<!--            </template>-->
<!--            <el-menu-item index="/tool/json">JSON</el-menu-item>-->
<!--            <el-menu-item index="/tool/time">时间戳</el-menu-item>-->
<!--          </el-submenu>-->
          <el-submenu index="9">
            <template slot="title">
              <span class="">关于</span>
            </template>
            <el-menu-item index="/about/donate">捐赠</el-menu-item>
            <el-menu-item index="/about/authority">权限设置</el-menu-item>
            <!--            <el-menu-item index="/about/opensource"></el-menu-item>-->
            <!--            <el-menu-item index="/about/contact">联系作者</el-menu-item>-->
          </el-submenu>
        </el-menu>

      </el-header>
      <el-main>
        <!--        <el-breadcrumb separator-class="el-icon-arrow-right">-->
        <!--          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>-->
        <!--          <el-breadcrumb-item>活动管理</el-breadcrumb-item>-->
        <!--          <el-breadcrumb-item>活动列表</el-breadcrumb-item>-->
        <!--          <el-breadcrumb-item>活动详情</el-breadcrumb-item>-->
        <!--        </el-breadcrumb>-->
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
