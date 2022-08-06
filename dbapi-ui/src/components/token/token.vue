<template>
  <div>
    <div class="gap">
      <!--      <router-link to="/token/add">-->
      <el-button type="primary" plain @click="dialogCreateApp = true">创建应用</el-button>
      <!--      </router-link>-->
    </div>

    <el-table :data="tableData" border stripe max-height="700" class="gap">
      <el-table-column prop="id" label="appid"></el-table-column>
      <el-table-column prop="name" label="应用名称"></el-table-column>
      <el-table-column prop="note" label="应用描述"></el-table-column>
      <el-table-column prop="secret" label="secret"></el-table-column>
      <el-table-column prop="expireDesc" label="token失效时间"></el-table-column>


      <el-table-column :label="$t('m.operation')" width="100px">
        <template slot-scope="scope">
          <el-button plain size="mini" type="warning" @click="handleAuth(scope.row.id)" circle>
            <i class="el-icon-lock"></i>
          </el-button>
          <el-button plain size="mini" type="danger" @click="handleDelete(scope.row.id)" circle>
            <i class="el-icon-delete"></i>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="授权该应用访问以下分组的API" :visible.sync="dialogVisible" @open="getAllGroups">
      <el-checkbox-group v-model="checkList">
        <el-checkbox :label="item.id" v-for="item in groups">{{ item.name }}</el-checkbox>
      </el-checkbox-group>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">{{ $t('m.cancel') }}</el-button>
        <el-button type="primary" @click="dialogVisible = false;auth()">{{ $t('m.save') }}</el-button>
      </span>
    </el-dialog>


    <el-alert type="warning" show-icon>
      <div class="tip"></div>
      <div>
        请求私有接口时，需要把token值放入header的Authorization字段中携带，才可以访问成功。（如果是开放接口，不需要设置header）
        <br/>
        <br/>以python为例，访问api的代码示例如下：
        <br/>
        import requests <br/>
        headers = {"Authorization": "5ad0dcb4eb03d3b0b7e4b82ae0ba433f"} <br/>
        re = requests.post("http://127.0.0.1:8520/api/userById", {"idList": [1, 2]}, headers=headers) <br/>
        print(re.text) <br/>
      </div>
      <br/>
      <div>
        <h3>*token如何获取？</h3>
        使用appid和secret访问以下接口来获取token<br/>
        http://{{ip}}/token/generate?appid=xxx&secret=xxx<br/>
      </div>

    </el-alert>

    <el-dialog title="创建应用" :visible.sync="dialogCreateApp" width="60%">
      <el-form label-width="120px">
        <el-form-item label="应用名称">
          <el-input v-model="app.name"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="app.note"></el-input>
        </el-form-item>
        <el-form-item label="token过期时间">
          <el-radio-group v-model="app.expireDesc">
            <el-radio-button label="5min">5min</el-radio-button>
            <el-radio-button label="1hour">1hour</el-radio-button>
            <el-radio-button label="1day">1day</el-radio-button>
            <el-radio-button label="30day">30day</el-radio-button>
            <el-radio-button label="单次有效">单次有效</el-radio-button>
            <el-radio-button label="永久有效">永久有效</el-radio-button>
            <!--            <el-radio-button label="300">5min</el-radio-button>-->
            <!--            <el-radio-button label="3600">1hour</el-radio-button>-->
            <!--            <el-radio-button label="86400">1day</el-radio-button>-->
            <!--            <el-radio-button label="2592000">30day</el-radio-button>-->
            <!--            <el-radio-button label="0">单次有效</el-radio-button>-->
            <!--            <el-radio-button label="-1">永久有效</el-radio-button>-->
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogCreateApp = false">取 消</el-button>
        <el-button type="primary" @click="dialogCreateApp = false;createApp()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "token",
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      dialogCreateApp: false,
      groups: [],
      checkList: [],
      tokenId: null,
      app: {
        name: null,
        note: null,
        expireDesc: null
      },
      ip:null
    }
  },
  methods: {
    createApp() {
      this.axios.post("/app/create", this.app).then((response) => {
        const msg = `创建应用成功！请妥善保存:<br/><br/>appid = ${response.data.id}<br/>secret = ${response.data.secret}`
        this.$message.success({

          dangerouslyUseHTMLString: true,
          message: msg,
          duration: 10000
        })
      }).catch((error) => {
      })
    },
    getAll() {
      this.axios.post("/app/getAll").then((response) => {
        this.tableData = response.data
      }).catch((error) => {
      })
    },
    getAllGroups() {
      this.axios.post("/group/getAll/").then((response) => {
        this.groups = response.data
      }).catch((error) => {
      })
    },
    auth() {
      console.log(this.checkList)
      this.axios.post("/app/auth/", {appId: this.tokenId, groupIds: this.checkList.join(",")}).then((response) => {
        this.$message.success("Authorization Success")
      }).catch((error) => {
        this.$message.error("Authorization Failed")
      })
    },
    handleDelete(id) {
      this.axios.post("/app/delete/" + id).then((response) => {
        this.$message.success("Delete Success")
        this.getAll()
      }).catch((error) => {
        this.$message.error("Delete Failed")
      })
    },
    handleAuth(id) {
      this.dialogVisible = true
      this.tokenId = id
      this.axios.post("/app/getAuthGroups/" + id).then((response) => {
        this.checkList = response.data
      }).catch((error) => {
      })
    },
    async getIP() {
      await this.axios
          .post("/apiConfig/getIP")
          .then((response) => {
            this.ip = response.data
          })
          .catch((error) => {
            this.$message.error("get ip failed");
          });
    },

  },
  created() {
    this.getAll()
    this.getIP()
  }
}
</script>

<style scoped>
.tip {
  white-space: pre;
}

</style>