<template>
  <div>
    <div>
      <div>
        <el-select v-model="groupId" class="gap">
          <el-option label="所有分组" value=""></el-option>
          <el-option :label="item.name" :value="item.id" v-for="item in groups" :key="item.id"></el-option>
        </el-select>
        <el-input placeholder="输入关键字" v-model="keyword" style="width:400px;" clearable class="gap"
                  @keyup.enter.native="search">
          <el-select v-model="field" slot="prepend" placeholder="" style="width:80px;">
            <el-option label="名称" value="name"></el-option>
            <el-option label="描述" value="note"></el-option>
            <el-option label="路径" value="path"></el-option>
          </el-select>
        </el-input>
        <el-button type="primary" icon="el-icon-search" @click="search" plain>查询</el-button>
      </div>

      <el-table :data="tableData" border stripe max-height="700" class="gap">
        <el-table-column label="名称">
          <template slot-scope="scope">
            <el-tooltip effect="light" content="已上线" placement="top-start" v-if="scope.row.status == 1">
              <i class="iconfont icon-on_line1 circle"></i>
            </el-tooltip>

            <el-tooltip effect="light" content="未上线" placement="top-start" v-else>
              <i class="iconfont icon-off_line circle offline"></i>
            </el-tooltip>

            <el-tooltip effect="light" content="私有接口" placement="top-start" v-if="scope.row.previlege == 0">
              <i class="el-icon-lock circle lock"></i>
            </el-tooltip>
            <el-tooltip effect="light" content="开放接口" placement="top-start" v-else>
              <i class="el-icon-unlock circle "></i>
            </el-tooltip>


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

      <router-link to='/api/add' class="gap">
        <el-button type="primary" plain>创建api</el-button>
      </router-link>

      <el-button @click="dialogVisible = true" type="primary" plain class="gap">api分组管理</el-button>
      <el-button class="gap" type="primary" plain @click="dialogVisible2=true">导出api文档</el-button>
      <el-dialog title="API分组" :visible.sync="dialogVisible" @close="getAllGroups">
        <group></group>
      </el-dialog>

      <el-dialog title="导出api文档" :visible.sync="dialogVisible2" @open="getApiTree">
        <el-tree :data="treeData" show-checkbox node-key="id" :props="defaultProps" ref="tree"></el-tree>
        <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible2 = false;exportDocs()">导出</el-button>
      </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import dataTag from "@/components/common/dataTag";
import group from "@/components/api/group";

export default {
  name: "api",
  data() {
    return {
      dialogVisible: false,
      dialogVisible2: false,
      keyword: null,
      field: null,
      tableData: [],
      groups: [],
      groupId: '',
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  components: {dataTag, group},
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
      this.axios.post("/apiConfig/search", {
        keyword: this.keyword,
        field: this.field,
        groupId: this.groupId
      }).then((response) => {
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
    },
    getAllGroups() {
      this.axios.post("/group/getAll/").then((response) => {
        this.groups = response.data
      }).catch((error) => {
      })
    },
    getApiTree() {
      this.axios.post("/apiConfig/getApiTree/").then((response) => {
        this.treeData = response.data
      }).catch((error) => {
      })
    },
    exportDocs() {
      let a = this.$refs.tree.getCheckedKeys().filter((t) => {
        return t != undefined
      })
      const ids = a.join(",")
      if (ids == '') {
        return
      }
      this.axios({
        method: 'post',
        params: {ids: ids},
        url: '/apiConfig/apiDocs',
        responseType: 'blob' //这个很重要
      }).then((res) => {
        console.log(res)
        const link = document.createElement('a')
        let blob = new Blob([res.data], {type: 'application/x-msdownload'});
        link.style.display = 'none'
        link.href = URL.createObjectURL(blob);
        link.setAttribute('download', '接口文档.md')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      }).catch(error => {
        this.$message.error("导出接口文档错误")
        console.error(error)
      })
    }
  },

  created() {
    this.getAllApis()
    this.getAllGroups()
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

.offline {
  color: #737373;
}

.lock {
  color: #c10b0b;
}

.tag {
  margin-right: 5px;
}


</style>
