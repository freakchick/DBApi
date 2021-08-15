<template>
  <div class="home">
    <el-container>

      <el-header style="background-color:#06B176;padding: 0">
        <!--        <span>DBAPI</span>-->
        <div class="head">
          <div class="logo">DBApi</div>
          <el-menu
              default-active="1"
              class="menu"
              mode="horizontal"
              @select="handleSelect"
              background-color="#06B176"
              text-color="#fff"
              active-text-color="#01293b"
              router>

            <el-menu-item index="/datasource">
              <template slot="title">
                <span class="title"><i class="iconfont icon-database"></i>数据源</span>
              </template>
            </el-menu-item>

            <el-menu-item index="/api">
              <template slot="title">
                <span class="title"><i class="iconfont icon-api"></i>API</span>
              </template>
            </el-menu-item>

            <el-menu-item index="/token">
              <template slot="title">
                <span class="title"><i class="iconfont icon-quanxian"></i>权限</span>
              </template>
            </el-menu-item>

            <el-submenu index="">
              <template slot="title">
                <span class="title"><i class="iconfont icon-shezhi"></i>系统设置</span>
              </template>
              <el-menu-item index="/setting/password">修改密码</el-menu-item>
              <el-menu-item index="/setting/firewall">IP防火墙</el-menu-item>
            </el-submenu>

          </el-menu>
        </div>
      </el-header>
      <el-main>
        <div class="main">
          <router-view></router-view>
        </div>
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

<style scoped lang="scss">
.iconfont {
  color: #fff;
  font-size: 20px !important;
  font-weight: 700;
  padding-right: 3px;
}



.head {
  display: flex;
  background-color: #06B176;
  box-shadow: 1px 1px 5px #72767b;

  .logo {
    font-family:Helvetica ;
    font-size: 34px;
    font-weight: 900;
    line-height: 60px;
    text-align: center;
    color: #045008;
    //background-color: #fff;
    display: block;
    min-width: 150px;
    width: 200px;
    text-shadow: 2px 2px 1px #a5d4b7;

    //width: auto;
  }
  .menu {
    //width: 100%;
    min-width: 500px;
    border: 0 ;
    .title{
      font-size: 16px;
      font-weight: 700;
    }
  }
}


.main {
  background-color: #fff;
  //max-width: 1500px;
  //margin: 0 auto;
  border-radius: 5px;
  box-shadow: 1px 1px 5px #72767b;
  padding: 20px;
  min-height: calc(100vh - 100px);
}

</style>
