<template>
  <div class="head">
    <div style="padding: 5px 10px">
      <img src="@/img/logo.png" alt="" class="logo2"/>
    </div>
    <!--    <div class="logo">DBApi</div>-->
    <span class="version">{{ version }}</span>
    <div class="menus">
      <div class="menu iconfont icon-database " :class="{'activeMenu':$route.path == '/datasource'}" @click="clickMenu('/datasource')">{{ $t("m.datasource") }}</div>
      <div class="menu iconfont icon-api" :class="{'activeMenu':$route.path.startsWith('/api')}" @click="clickMenu('/api')">API</div>
      <div class="menu iconfont icon-kehu" :class="{'activeMenu':$route.path == '/client'}" @click="clickMenu('/client')">{{ $t("m.client") }}</div>
      <div class="menu iconfont icon-shezhi" :class="{'activeMenu':$route.path.startsWith('/security')}">
        {{ $t("m.settings") }}
        <div class="submenus">
          <div class="submenu" :class="{'activeMenu':$route.path == '/security/firewall'}" @click="clickMenu('/security/firewall')">{{ $t("m.firewall") }}</div>
        </div>
      </div>
      <div class="menu iconfont icon-jiankong" :class="{'activeMenu':$route.path == '/monitor'}" @click="clickMenu('/monitor')">{{ $t("m.monitor") }}</div>
    </div>
    <div class="right">
      <!--      <span class="mode">{{ this.$store.state.mode }}</span>-->
      <div style="line-height: 60px;margin: 0 5px">
        <a href="https://github.com/freakchick/DBApi" target="_blank"><i class="iconfont icon-github" style="font-size: 26px"></i></a>
      </div>

      <el-dropdown @command="changeLanguage" style="margin-right: 15px">
        <span class="el-dropdown-link" style="color: #bfcbd9">
          {{ languageName }}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item :command="item" :key="index" v-for="(item,index) in langs">{{ item.name }}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>


      <el-dropdown @command="handleCommand">
        <span class="el-dropdown-link" style="color: #bfcbd9">
          <i class="el-icon-user"></i>{{ username }}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="logout">{{ $t('m.logout') }}</el-dropdown-item>
          <el-dropdown-item command="changePassword">{{ $t('m.change_password') }}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

      <el-dialog :title="$t('m.change_password')" :visible.sync="dialogVisible">
        <password></password>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import password from "@/components/user/password.vue"

export default {
  name: "homeHeader",
  components: {password},
  data() {
    return {
      dialogVisible: false,
      langs: [
        {name: "English", value: "en"},
        {name: "中文", value: "cn"},
      ],
      currentLang: this.$i18n.locale,
      version: null,
      username: localStorage.getItem("username")
    };
  },
  methods: {

    handleCommand(command) {
      if (command == 'logout') {
        localStorage.removeItem("token")
        localStorage.removeItem("username")
        localStorage.removeItem("userId")
        this.$router.push("/login");
      } else if (command == 'changePassword') {
        this.dialogVisible = true
        console.log(this.$route.path)
      }
    },

    clickMenu(data) {
      this.$router.push(data);
    },
    changeLanguage(data) {
      this.$i18n.locale = data.value;
      localStorage.setItem("locale", data.value);
      this.currentLang = data.value;
    },
    getVersion() {
      this.axios
        .post("/system/version")
        .then((response) => {
          this.version = response.data;
        })
        .catch((error) => {
        });
    },
  },
  created() {
    this.getVersion();
  },
  computed: {
    languageName() {
      const p = this.langs.filter((item) => item.value === this.currentLang)[0]
        .name;
      return p;
    },
  },
};
</script>

<style scoped lang="less">
.head {
  display: flex;
  //background-color: #304156;
  background-image: linear-gradient(15deg, #486180, #324256, #486180);
  color: #bfcbd9;
  width: 100%;
  line-height: 60px;

  .logo2 {
    flex-shrink: 0;
    display: block;
    height: 50px;
  }

  .version {
    padding: 30px 20px 0px 0px;
    font-size: 14px;
    line-height: 20px;
  }

  .menus {
    flex-shrink: 0;
    flex-grow: 1;
    display: flex;


    .activeMenu {
      //background-image: linear-gradient(90deg, #495f7a, #2f3d50, #495f7a);
      //background-image: radial-gradient( #486180, #283546);
      //opacity: 0.3;
      color: #f9fbfd;
      font-size: 22px;
    }

    .menu {
      margin: 0 5px;
      padding: 0 10px;
      font-size: 20px;
      font-weight: 700;
      cursor: pointer;
      position: relative;

      .submenus {
        padding: 5px 0;
        display: none;
        z-index: 1000;
        position: absolute;
        top: 60px;
        left: 0px;
        background-color: #304156;
        //padding: 0 10px;
        width: 200px;

        border-radius: 5px;
        //border: 1px solid #00ff00;
        .submenu {
          font-size: 16px;
          line-height: 40px;
          padding: 0 10px;
          font-weight: 500;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;

          &:hover {
            background-color: #222d3b;
          }
        }
      }

      &:hover {
        background-color: #222d3b;

        .submenus {
          display: block;
        }
      }
    }
  }

  .right {
    margin: 0 20px;
    flex-shrink: 0;
    display: flex;

    .mode {
      font-family: Helvetica;
      font-weight: 900;
      font-size: 15px;
      margin-right: 10px;

    }

    .langs {
      position: relative;

      span {
        cursor: pointer;

        font-size: 18px;
      }

      .options {
        z-index: 1000;
        position: absolute;
        right: 0;
        // display: none;
        background-color: #304156;

        line-height: 30px;

        .option {
          cursor: pointer;
          padding: 0 10px;

          &:hover {
            background-color: #222d3b;
          }
        }
      }
    }
  }
}
</style>