<template>
  <div class="head">
    <div style="padding: 5px 10px">
      <img src="@/img/logo.png" alt="" class="logo2"/>
    </div>
    <!--    <div class="logo">DBApi</div>-->
    <span class="version">{{ version }}</span>
    <div class="menus">
      <div class="menu iconfont icon-database" @click="clickMenu('/datasource')">
        {{ $t("m.datasource") }}
      </div>
      <div class="menu iconfont icon-api" @click="clickMenu('/api')">API</div>
      <div class="menu iconfont icon-quanxian" @click="clickMenu('/client')">
        {{ $t("m.client") }}
      </div>
      <div class="menu iconfont icon-shezhi">
        {{ $t("m.settings") }}
        <div class="submenus">
          <div class="submenu" @click="clickMenu('/setting/password')">
            {{ $t("m.change_pass") }}
          </div>
          <div class="submenu" @click="clickMenu('/setting/firewall')">
            {{ $t("m.firewall") }}
          </div>
        </div>
      </div>
      <div class="menu iconfont icon-jiankong" @click="clickMenu('/monitor')">
        {{ $t("m.monitor") }}
      </div>
    </div>
    <div class="right">
      <!--      <span class="mode">{{ this.$store.state.mode }}</span>-->
      <div style="line-height: 60px;margin: 0 5px">
        <a href="https://github.com/freakchick/DBApi" target="_blank"><i class="iconfont icon-github" style="font-size: 26px"></i></a>
      </div>

      <el-dropdown @command="changeLanguage" style="margin-right: 15px">
        <span class="el-dropdown-link" style="line-height: 60px;color: #bfcbd9">
          {{ languageName }}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item :command="item" :key="index" v-for="(item,index) in langs">{{ item.name }}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>


      <el-dropdown @command="handleCommand">
        <span class="el-dropdown-link" style="line-height: 60px;color: #bfcbd9">
          {{ username}}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="logout">{{$t('m.logout')}}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

    </div>
  </div>
</template>

<script>
export default {
  name: "homeHeader",
  data() {
    return {
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
        this.$router.push("login");
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
  background-color: #304156;
  color: #bfcbd9;
  width: 100%;

  .logo2 {
    flex-shrink: 0;
    display: block;
    height: 50px;
  }

  .version {
    padding: 30px 20px 0px 0px;
    font-size: 14px;
  }

  .menus {
    flex-shrink: 0;
    flex-grow: 1;
    display: flex;
    line-height: 60px;

    .menu {
      margin: 0 5px;
      padding: 0 10px;
      font-size: 22px;
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
      line-height: 60px;
    }

    .langs {
      position: relative;

      span {
        cursor: pointer;
        line-height: 60px;
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