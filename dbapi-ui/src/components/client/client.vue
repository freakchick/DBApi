<template>
  <div>
    <div class="gap">
      <!--      <router-link to="/token/add">-->
      <el-button type="primary" plain @click="dialogCreateApp = true">{{$t('m.create_client')}}</el-button>
      <!--      </router-link>-->
    </div>

    <el-table :data="tableData" border stripe max-height="700" class="gap">
      <el-table-column prop="id" label="clientId"></el-table-column>
      <el-table-column prop="name" :label="$t('m.name')"></el-table-column>
      <el-table-column prop="note" :label="$t('m.description')"></el-table-column>
      <el-table-column prop="secret" label="Secret"></el-table-column>
      <el-table-column prop="expireDesc" :label="$t('m.token_expire_time')"></el-table-column>


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

    <el-dialog :title="$t('m.grant')" :visible.sync="dialogVisible" @open="getAllGroups">
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
        <pre>{{$t('m.token_tip')}}</pre>
        <pre>
import requests
headers = {"Authorization": "5ad0dcb4eb03d3b0b7e4b82ae0ba433f"}
re = requests.post("http://127.0.0.1:8520/api/userById", {"idList": [1, 2]}, headers=headers) 
print(re.text)
        </pre>
      </div>
      <el-divider></el-divider>
      <div>
        <pre>{{$t('m.token_tip2')}}</pre>
        http://{{ip}}/token/generate?clientId=xxx&secret=xxx
      </div>

    </el-alert>

    <el-dialog :title="$t('m.create_client')" :visible.sync="dialogCreateApp" width="60%">
      <el-form label-width="150px">
        <el-form-item :label="$t('m.name')">
          <el-input v-model="client.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('m.description')">
          <el-input type="textarea" v-model="client.note"></el-input>
        </el-form-item>
        <el-form-item :label="$t('m.token_expire_time')">
          <el-radio-group v-model="client.expireDesc">
            <el-radio-button label="5min">5min</el-radio-button>
            <el-radio-button label="1hour">1hour</el-radio-button>
            <el-radio-button label="1day">1day</el-radio-button>
            <el-radio-button label="30day">30day</el-radio-button>
            <el-radio-button label="once">{{$t('m.once')}}</el-radio-button>
            <el-radio-button label="forever">{{$t('m.forever')}}</el-radio-button>
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
        <el-button @click="dialogCreateApp = false">{{$t('m.cancel')}}</el-button>
        <el-button type="primary" @click="dialogCreateApp = false;create()">{{$t('m.ok')}}</el-button>
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
      clientId: null,
      client: {
        name: null,
        note: null,
        expireDesc: null
      },
      ip:null
    }
  },
  methods: {
    create() {
      this.axios.post("/client/create", this.client).then((response) => {
        const msg = `Created！Please save:<br/><br/>clientId = ${response.data.id}<br/>secret = ${response.data.secret}`
        this.$message.success({

          dangerouslyUseHTMLString: true,
          message: msg,
          duration: 10000
        })
        this.getAll()
      }).catch((error) => {
      })
    },
    getAll() {
      this.axios.post("/client/getAll").then((response) => {
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
      this.axios.post("/client/auth/", {clientId: this.clientId, groupIds: this.checkList.join(",")}).then((response) => {
        this.$message.success("Authorization Success")
      }).catch((error) => {
        this.$message.error("Authorization Failed")
      })
    },
    handleDelete(id) {
      this.axios.post("/client/delete/" + id).then((response) => {
        this.$message.success("Delete Success")
        this.getAll()
      }).catch((error) => {
        this.$message.error("Delete Failed")
      })
    },
    handleAuth(id) {
      this.dialogVisible = true
      this.clientId = id
      this.axios.post("/client/getAuthGroups/" + id).then((response) => {
        this.checkList = response.data
      }).catch((error) => {
      })
    },
    async getIP() {
      await this.axios
          .post("/system/getIP")
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