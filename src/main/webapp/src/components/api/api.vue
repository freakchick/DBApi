<template>
  <div>
    <router-link to='/api/add'>
      <el-button>创建api</el-button>
    </router-link>

    <el-table :data="tableData">
      <el-table-column label="名称">
        <template slot-scope="scope">
          <el-tag type="success" size="mini" effect="dark" v-if="scope.row.status == 1">
            <i class="iconfont icon-on_line1"></i>
          </el-tag>
          <el-tag type="info" size="mini" effect="dark" v-else>
            <i class="iconfont icon-off_line"></i>
          </el-tag>
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <!--      <el-table-column prop="note" label="描述"></el-table-column>-->
      <el-table-column prop="path" label="路径"></el-table-column>
      <el-table-column label="参数">
        <template slot-scope="scope">
          <el-tag type="success" v-for="item in scope.row.p" size="mini" effect="dark" :title="item.type">
            {{ item.name }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button plain size="mini" type="warning" @click="handleEdit(scope.row.id)" icon="el-icon-edit"
                     circle></el-button>
          <el-button plain size="mini" type="danger" @click="handleDelete(scope.row.id)" icon="el-icon-delete"
                     circle></el-button>
          <el-button  size="mini" v-if="scope.row.status == 0" type="success" @click="online(scope.row.id)"
                     circle><i class="iconfont icon-on_line1"></i>
          </el-button>

          <el-button  size="mini"  v-if="scope.row.status == 1" type="info" @click="offline(scope.row.id)"
                     circle><i class="iconfont icon-off_line"></i>
          </el-button>

          <el-button  size="mini"  v-if="scope.row.status == 1" type="primary" @click="httpTest(scope.row.id)"
                     circle><i class="iconfont icon-HTTPRequest" style="font-size: 14px"></i>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="添加api" :visible.sync="dialogVisible">

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
         <el-button type="primary" @click="dialogVisible = false;add()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "api",
  data() {
    return {
      dialogVisible: false,
      tableData: []
    }
  },
  methods: {
    getAllApis() {
      this.axios.post("/apiConfig/getAll").then((response) => {
        const list = response.data
        list.forEach(t => {
          const obj = JSON.parse(t.params)
          t['p'] = obj
        })
        this.tableData = list
      }).catch((error) => {
        this.$message.error("添加失败")
      })
    },
    handleDelete(id) {
      this.axios.post("/apiConfig/delete/" + id).then((response) => {
        this.getAllApis()
      }).catch((error) => {
        this.$message.error("删除失败")
      })
    },
    online(id) {
      this.axios.post("/apiConfig/online/" + id).then((response) => {
        this.$message.success("已上线")
        this.getAllApis()
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    offline(id) {
      this.axios.post("/apiConfig/offline/" + id).then((response) => {
        this.$message.success("已下线")
        this.getAllApis()
      }).catch((error) => {
        this.$message.error("失败")
      })
    }
  },

  created() {
    this.getAllApis()
  }
}
</script>

<style scoped>

</style>
