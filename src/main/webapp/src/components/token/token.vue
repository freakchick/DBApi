<template>
  <div>
    <div class="gap">
      <router-link to="/token/add">
        <el-button type="primary" plain>创建token</el-button>
      </router-link>

    </div>


    <el-table :data="tableData" border stripe max-height="700" class="gap">
      <el-table-column prop="token" label="token">
        <template slot-scope="scope">
          <span>{{ scope.row.token }}</span>
          <el-tag v-if="scope.row.expire != null && scope.row.expire < new Date().getTime()" type="danger" effect="dark"
                  size="mini">已过期
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="note" label="描述"></el-table-column>
      <el-table-column prop="expire" label="有效期">
        <template slot-scope="scope">
          <span v-if="scope.row.expire == null">永久</span>
          <span v-else>{{ scope.row.expire | dateFormat }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | dateFormat }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">

          <!--          <el-button plain size="mini" type="info" @click="detail(scope.row.id)" circle>
                      <i class="iconfont icon-detail"></i>
                    </el-button>
                    <el-button plain size="mini" type="warning" @click="handleEdit(scope.row.id)" circle>
                      <i class="el-icon-edit"></i>
                    </el-button>-->
          <el-button plain size="mini" type="warning" @click="handleAuth(scope.row.id)" circle>
            <i class="el-icon-lock"></i>
          </el-button>
          <el-button plain size="mini" type="danger" @click="handleDelete(scope.row.id)" circle>
            <i class="el-icon-delete"></i>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="授权该token访问以下分组的API" :visible.sync="dialogVisible" @open="getAllGroups">
      <el-checkbox-group v-model="checkList">
        <el-checkbox :label="item.id" v-for="item in groups">{{ item.name }}</el-checkbox>
      </el-checkbox-group>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false;auth()">保存</el-button>
      </span>
    </el-dialog>

    <el-alert title="token使用说明" type="warning" show-icon>
      <div>请求私有接口时，需要把token值放入header的Authorization字段中携带，才可以访问成功。（如果是开放接口，不需要设置header）</div>
      <div>以python为例，访问api的代码示例如下：</div>
      <div> <br/>
        import requests  <br/>
        headers = {"Authorization": "5ad0dcb4eb03d3b0b7e4b82ae0ba433f"} <br/>
        re = requests.post("http://127.0.0.1:8520/api/userById", {"idList": [1, 2]}, headers=headers) <br/>
        print(re.text) <br/>
      </div>

    </el-alert>
  </div>
</template>

<script>
export default {
  name: "token",
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      groups: [],
      checkList: [],
      tokenId: null
    }
  },
  methods: {
    getAll() {
      this.axios.post("/token/getAll").then((response) => {
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
      this.axios.post("/token/auth/", {tokenId: this.tokenId, groupIds: this.checkList.join(",")}).then((response) => {
        this.$message.success("授权成功")
      }).catch((error) => {
        this.$message.error("授权失败")
      })
    },
    handleDelete(id) {
      this.axios.post("/token/delete/" + id).then((response) => {
        this.getAll()
      }).catch((error) => {
      })
    },
    handleAuth(id) {
      this.dialogVisible = true
      this.tokenId = id
      this.axios.post("/token/getAuthGroups/" + id).then((response) => {
        this.checkList = response.data
      }).catch((error) => {
      })
    }

  },
  created() {
    this.getAll()
  }
}
</script>

<style scoped>

</style>