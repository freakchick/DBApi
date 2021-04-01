<template>
  <div>
    <div>
      <router-link to='/api/add'>
        <el-button style="margin-bottom:5px;" type="primary" plain>创建api</el-button>
      </router-link>
      <el-input placeholder="输入关键字搜索api" v-model="keyword" style="width:400px;margin-left: 5px" clearable @keyup.enter.native="search">
        <el-select v-model="field" slot="prepend" placeholder="" style="width:80px;">
          <el-option label="名称" value="name"></el-option>
          <el-option label="描述" value="note"></el-option>
          <el-option label="路径" value="path"></el-option>
        </el-select>
        <el-button slot="append" type="primary" icon="el-icon-search" @click="search"></el-button>
      </el-input>
    </div>


    <el-table :data="tableData" border stripe max-height="700">
      <el-table-column label="名称">
        <template slot-scope="scope">
          <i class="iconfont icon-on_line1 circle" v-if="scope.row.status == 1" title="已上线"></i>
          <i class="iconfont icon-off_line circle red" v-else title="未上线"></i>
          <span :title="scope.row.note">{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <!--      <el-table-column prop="note" label="描述"></el-table-column>-->
      <el-table-column label="路径">
        <template slot-scope="scope">
          <span>/api/{{ scope.row.path }}</span>
        </template>
      </el-table-column>
      <el-table-column label="参数">
        <template slot-scope="scope">
          <data-tag v-for="item in scope.row.p" :name="item.name" :type="item.type"></data-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button plain size="mini" type="info" @click="detail(scope.row.id)" circle><i
              class="iconfont icon-detail"></i>
          </el-button>
          <el-button plain size="mini" type="warning" @click="handleEdit(scope.row.id)" circle>
            <i class="el-icon-edit"></i>
          </el-button>
          <el-button plain size="mini" type="danger" @click="handleDelete(scope.row.id)" circle>
            <i class="el-icon-delete"></i>
          </el-button>
          <el-button plain size="mini" v-if="scope.row.status == 0" type="warning" @click="online(scope.row.id)"
                     title="上线" circle>
            <i class="iconfont icon-on_line2"></i>
          </el-button>

          <el-button plain size="mini" v-if="scope.row.status == 1" type="info" @click="offline(scope.row.id)"
                     title="下线" circle>
            <i class="iconfont icon-off_line1"></i>
          </el-button>

          <el-button plain size="mini" v-if="scope.row.status == 1" type="primary" @click="httpTest(scope.row.id)"
                     title="请求测试" circle>
            <i class="iconfont icon-HTTPRequest"></i>
          </el-button>

        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="请求测试api" :visible.sync="dialogVisible">


      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
         <el-button type="primary" @click="dialogVisible = false;add()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import dataTag from "@/components/common/dataTag";

export default {
  name: "api",
  data() {
    return {
      dialogVisible: false,
      keyword: null,
      field: null,
      tableData: []
    }
  },
  components: {dataTag},
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
        this.$message.error("查询所有api失败")
      })
    },
    search() {
      this.axios.post("/apiConfig/search", {keyword: this.keyword, field: this.field}).then((response) => {
        const list = response.data
        list.forEach(t => {
          const obj = JSON.parse(t.params)
          t['p'] = obj
        })
        this.tableData = list
      }).catch((error) => {
        this.$message.error("查询所有api失败")
      })
    },
    handleDelete(id) {
      this.axios.post("/apiConfig/delete/" + id).then((response) => {
        this.$message.success("删除成功")
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
    },
    httpTest(id) {
      this.$router.push({path: '/api/request', query: {id: id}});
    },
    detail(id) {
      this.$router.push({path: '/api/detail', query: {id: id}});
    },
    handleEdit(id) {
      this.$router.push({path: '/api/edit', query: {id: id}});
    }
  },

  created() {
    this.getAllApis()
  }
}
</script>

<style scoped>
i {
  font-size: 14px;
}

.circle {
  border-radius: 10px;
  font-size: 18px;
  font-weight: 700;
  /*background-color: #05566b;*/
  padding: 3px;
  color: #38be2e;
}

.red {
  color: #737373;
}

.tag {
  margin-right: 5px;
}


</style>
