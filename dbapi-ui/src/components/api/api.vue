<template>
  <div>
    <div>
      <ul>
        <li>
          <router-link to='/api/add'>
            <el-button type="primary" icon="el-icon-plus">创建API</el-button>
          </router-link>
        </li>
        <li>
          <el-button @click="dialogVisible = true" type="primary" >API分组管理</el-button>
        </li>
        <li>
          <el-button type="warning" plain @click="dialogVisible2=true" icon="el-icon-download" round>导出API文档</el-button>
        </li>
        <li>
          <el-button type="warning"  @click="dialogVisible3=true" icon="el-icon-download" round>导出API配置</el-button>
        </li>
        <li>
          <el-upload action="/apiConfig/import" accept=".json" :on-success="importSuccess" :headers="headers"
                     :on-error="importFail" :file-list="fileList">
            <el-button type="warning"  icon="el-icon-upload2" round>导入API配置</el-button>
          </el-upload>
        </li>
        <li>
          <el-button type="warning" @click="dialogVisible4=true" icon="el-icon-download" round>导出API分组配置</el-button>
        </li>
        <li>
          <el-upload action="/apiConfig/importGroup" accept=".json" :on-success="importGroupSuccess" :headers="headers"
                     :on-error="importFail" :file-list="groupFile">
            <el-button type="warning" icon="el-icon-upload2" round>导入API分组配置</el-button>
          </el-upload>
        </li>
      </ul>
      <div class="search">
        <my-select  v-model="groupId"  label="API分组" :options="groups" option_label="name" option_value="id"> </my-select>
<!--        <el-select v-model="groupId" class="gap">
          <el-option label="所有分组" value=""></el-option>
          <el-option :label="item.name" :value="item.id" v-for="item in groups" :key="item.id"></el-option>
        </el-select>-->
        <el-input placeholder="输入关键字" v-model="keyword" style="width:400px;" clearable
                  @keyup.enter.native="search">
          <el-select v-model="field" slot="prepend" placeholder="" style="width:80px;">
            <el-option label="名称" value="name"></el-option>
            <el-option label="描述" value="note"></el-option>
            <el-option label="路径" value="path"></el-option>
          </el-select>
        </el-input>
        <el-button type="primary" icon="el-icon-search" @click="search" plain>查询</el-button>
      </div>

      <el-table :data="tableData" border stripe max-height="700" width="100%">
        <el-table-column label="id" prop="id" width="270px"></el-table-column>
        <el-table-column label="名称" >
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
        <el-table-column prop="updateTime" label="修改时间" width="170px"></el-table-column>
        <el-table-column label="操作" width="220px">
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


      <el-dialog title="API分组" :visible.sync="dialogVisible" @close="getAllGroups">
        <group></group>
      </el-dialog>

      <el-dialog title="导出API文档" :visible.sync="dialogVisible2" @open="getApiTree">
        <el-tree :data="treeData" show-checkbox node-key="id" :props="defaultProps" ref="tree"></el-tree>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible2 = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible2 = false;exportDocs()">导出</el-button>
        </span>
      </el-dialog>

      <el-dialog title="导出API配置" :visible.sync="dialogVisible3" @open="getApiTree">
        <el-tree :data="treeData" show-checkbox node-key="id" :props="defaultProps" ref="tree2"></el-tree>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible3 = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible3 = false;exportConfig()">导出</el-button>
        </span>
      </el-dialog>

      <el-dialog title="导出API分组配置" :visible.sync="dialogVisible4" @open="getAllGroups">
        <el-checkbox-group v-model="checkList">
          <el-checkbox v-for="item in groups" :label="item.id">{{item.name}}
            <span style="color: #ccc">{{item.id}}</span>
          </el-checkbox>

        </el-checkbox-group>

        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible4 = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible4 = false;exportGroupConfig()">导出</el-button>
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
      dialogVisible3: false,
      dialogVisible4: false,
      keyword: null,
      field: null,
      tableData: [],
      groups: [],
      groupId: '',
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      headers: {
        Authorization: localStorage.getItem('token')
      },
      fileList:[],
      groupFile:[],
      checkList:[]
    }
  },
  components: {dataTag, group},
  methods: {
    importSuccess(response, file, fileList) {
      this.fileList = []
      this.$message.success("导入api成功")
      this.getAllApis()
    },
    importGroupSuccess(response, file, fileList){
      this.groupFile = []
      this.$message.success("导入api分组成功")
      this.getAllGroups()
    },
    importFail(error, file, fileList) {
      this.$message.error("import failed!  "+ error.message)
    },
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
    },
    exportConfig() {
      let a = this.$refs.tree2.getCheckedKeys().filter((t) => {
        return t != undefined
      })
      const ids = a.join(",")
      if (ids == '') {
        return
      }
      this.axios({
        method: 'post',
        params: {ids: ids},
        url: '/apiConfig/downloadConfig',
        responseType: 'blob' //这个很重要
      }).then((res) => {
        console.log(res)
        const link = document.createElement('a')
        let blob = new Blob([res.data], {type: 'application/x-msdownload'});
        link.style.display = 'none'
        link.href = URL.createObjectURL(blob);
        link.setAttribute('download', 'api_config.json')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      }).catch(error => {
        this.$message.error("导出错误")
        console.error(error)
      })
    },
    exportGroupConfig(){
      console.log(this.checkList)
      const ids = this.checkList.join(",")
      this.axios({
        method: 'post',
        params: {ids: ids},
        url: '/apiConfig/downloadGroupConfig',
        responseType: 'blob' //这个很重要
      }).then((res) => {
        console.log(res)
        const link = document.createElement('a')
        let blob = new Blob([res.data], {type: 'application/x-msdownload'});
        link.style.display = 'none'
        link.href = URL.createObjectURL(blob);
        link.setAttribute('download', 'api_group_config.json')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      }).catch(error => {
        this.$message.error("导出错误")
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

<style scoped lang="scss">
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

ul {
  margin-bottom: 10px;

  li {
    display: inline-block;
    margin-right: 10px;
  }

}

.search{
  //padding: 10px ;
  //border: 1px solid #ccc;
}
</style>
