<template>

  <div class="api_root">
    <div class="group">
      <api-tree></api-tree>
    </div>
    <div class="api">
      <ul>
<!--        <li>-->
<!--          &lt;!&ndash;          <router-link to='/api/add'>&ndash;&gt;-->
<!--          <el-button type="primary" icon="el-icon-plus" @click="handleAdd" size="mini" plain>{{ $t('m.create_api') }}</el-button>-->
<!--          &lt;!&ndash;          </router-link>&ndash;&gt;-->
<!--        </li>-->
        <li>
          <el-button type="primary" @click="dialogVisible2=true" icon="iconfont icon-document" round size="mini" plain>{{ $t('m.export_api_doc') }}</el-button>
        </li>
        <li>
          <el-button type="primary" @click="dialogVisible3=true" icon="el-icon-download" round size="mini" plain>{{ $t('m.export_api') }}</el-button>
        </li>
        <li>
          <el-upload action="/apiConfig/import" accept=".json" :on-success="importSuccess" :headers="headers" :on-error="importFail" :file-list="fileList">
            <el-button type="warning" icon="el-icon-upload2" round size="mini" plain>{{ $t('m.import_api') }}</el-button>
          </el-upload>
        </li>
        <li>
          <el-button type="primary" @click="dialogVisible4=true" icon="iconfont icon-group" round size="mini" plain>{{ $t('m.export_api_groups') }}</el-button>
        </li>
        <li>
          <el-upload action="/apiConfig/importGroup" accept=".json" :on-success="importGroupSuccess" :headers="headers" :on-error="importFail" :file-list="groupFile">
            <el-button type="warning" icon="iconfont icon-group" round size="mini" plain>{{ $t('m.import_api_groups') }}</el-button>
          </el-upload>
        </li>
      </ul>
      <div class="search">
        <div>
          <span class="label">{{ $t('m.api_group') }}</span>
          <el-select v-model="groupId" placeholder="" style="width:180px;" size="mini" clearable>
            <el-option v-for="(item,index) in groups" :label="item.name" :value="item.id" :key="index"></el-option>
          </el-select>
          <span class="label">{{ $t('m.name') }}</span>
          <el-input v-model="keyword.name" style="width:200px;" clearable size="mini"></el-input>
          <span class="label">{{ $t('m.note') }}</span>
          <el-input v-model="keyword.note" style="width:200px;" clearable size="mini"></el-input>
          <span class="label">{{ $t('m.path') }}</span>
          <el-input v-model="keyword.path" style="width:200px;" clearable size="mini">
            <template slot="prepend">/{{ context }}/</template>
          </el-input>
          <el-button type="primary" icon="el-icon-search" @click="search" plain size="mini">{{ $t('m.search') }}</el-button>
        </div>

      </div>

      <el-table :data="tableData" border stripe max-height="700" width="100%">
        <el-table-column label="id" prop="id" width="100px"></el-table-column>
        <el-table-column :label="$t('m.name')">
          <template slot-scope="scope">
            <i class="iconfont icon-on_line1 circle" v-if="scope.row.status == 1"></i>
            <i class="iconfont icon-off_line circle offline" v-else></i>
            <i class="el-icon-lock circle lock" v-if="scope.row.access == PRIVILEGE.PRIVATE"></i>
            <i class="el-icon-unlock circle " v-else></i>
            <span :title="scope.row.note">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('m.path')">
          <template slot-scope="scope">
            <span>/{{ context }}/{{ scope.row.path }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('m.note')" prop="note">
        </el-table-column>
        <el-table-column label="Content-Type" prop="contentType" sortable></el-table-column>
        <el-table-column :label="$t('m.parameters')">
          <template slot-scope="scope">
            <div v-show="scope.row.contentType === CONTENT_TYPE.FORM_URLENCODED ">
              <data-tag v-for="item in scope.row.p" :name="item.name" :type="item.type"></data-tag>
            </div>
            <div v-show="scope.row.contentType === CONTENT_TYPE.JSON ">{{ scope.row.jsonParam }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" :label="$t('m.update_time')" sortable></el-table-column>
        <el-table-column :label="$t('m.operation')" width="220px">
          <template slot-scope="scope">
            <el-button plain size="mini" type="info" @click="detail(scope.row.id)" circle><i class="iconfont icon-detail"></i></el-button>
            <el-button plain size="mini" type="warning" @click="handleEdit(scope.row.id)" circle><i class="el-icon-edit"></i></el-button>

            <el-button plain size="mini" v-if="scope.row.status == 0" type="warning" @click="online(scope.row.id)" circle>
              <i class="iconfont icon-on_line2"></i>
            </el-button>

            <el-button plain size="mini" v-if="scope.row.status == 1" type="info" @click="offline(scope.row.id)" circle>
              <i class="iconfont icon-off_line1"></i>
            </el-button>

            <el-button plain size="mini" v-if="scope.row.status == 1" type="primary" @click="httpTest(scope.row.id)" :title="$t('m.request_test')" circle>
              <i class="iconfont icon-HTTPRequest"></i>
            </el-button>
            <el-button plain size="mini" type="danger" @click="handleDelete(scope.row.id)" circle>
              <i class="el-icon-delete"></i>
            </el-button>
          </template>
        </el-table-column>
      </el-table>

<!--      <el-dialog :title="$t('m.api_group')" :visible.sync="dialogVisible" @close="getAllGroups">-->
<!--        <group></group>-->
<!--      </el-dialog>-->

      <el-dialog :title="$t('m.export_api_doc')" :visible.sync="dialogVisible2" @open="getApiTree">
        <el-tree :data="treeData" show-checkbox node-key="id" :props="defaultProps" ref="tree"></el-tree>
        <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible2 = false">{{ $t('m.cancel') }}</el-button>
        <el-button type="primary" @click="dialogVisible2 = false;exportDocs()">{{ $t('m.export') }}</el-button>
      </span>
      </el-dialog>

      <el-dialog :title="$t('m.export_api')" :visible.sync="dialogVisible3" @open="getApiTree">
        <el-tree :data="treeData" show-checkbox node-key="id" :props="defaultProps" ref="tree2"></el-tree>
        <span slot="footer" class="dialog-footer"><el-button @click="dialogVisible3 = false">{{ $t('m.cancel') }}</el-button>
        <el-button type="primary" @click="dialogVisible3 = false;exportConfig()">{{ $t('m.export') }}</el-button>
      </span>
      </el-dialog>

      <el-dialog :title="$t('m.export_api_groups')" :visible.sync="dialogVisible4" @open="getAllGroups">
        <el-checkbox-group v-model="checkList">
          <el-checkbox v-for="item in groups" :label="item.id">{{ item.name }}
            <span style="color: #ccc">{{ item.id }}</span>
          </el-checkbox>

        </el-checkbox-group>

        <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible4 = false">{{ $t('m.cancel') }}</el-button>
        <el-button type="primary" @click="dialogVisible4 = false;exportGroupConfig()">{{ $t('m.export') }}</el-button>
      </span>
      </el-dialog>
    </div>
  </div>


</template>

<script>
import group from "@/components/api/group";
import ApiTree from "@/components/api/ApiTree.vue";
import {CONTENT_TYPE, PRIVILEGE} from "@/constant";

export default {
  name: "api",
  data() {
    return {


      CONTENT_TYPE: Object.freeze(CONTENT_TYPE),
      PRIVILEGE: Object.freeze(PRIVILEGE),
      dialogVisible: false,
      dialogVisible2: false,
      dialogVisible3: false,
      dialogVisible4: false,
      createDialog: false,
      keyword: {
        name: null,
        note: null,
        path: null,
      },
      tableData: [],
      groups: [],
      groupId: "",
      treeData: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
      headers: {
        Authorization: localStorage.getItem("token"),
      },
      fileList: [],
      groupFile: [],
      checkList: [],
      context: null,
    };
  },
  components: {group,ApiTree},
  methods: {

    getAllApiTree() {
      this.axios
        .post("/apiConfig/getApiTree")
        .then((response) => {
          this.data = response.data;
        })
        .catch((error) => {
          // this.$message.error("查询所有api失败")
        });
    },
    importSuccess(response, file, fileList) {
      this.fileList = [];
      this.$message.success("Import Success");
      // this.getAllApis();
    },
    importGroupSuccess(response, file, fileList) {
      this.groupFile = [];
      this.$message.success("Import Success");
      this.getAllGroups();
    },
    importFail(error, file, fileList) {
      this.$message.error("Import failed!  " + error.message);
    },
    // getAllApis() {
    //   this.axios
    //     .post("/apiConfig/getAll")
    //     .then((response) => {
    //       const list = response.data;
    //       list.forEach((t) => {
    //         const obj = JSON.parse(t.params);
    //         t["p"] = obj;
    //       });
    //       this.tableData = list;
    //     })
    //     .catch((error) => {
    //       // this.$message.error("查询所有api失败")
    //     });
    // },
    search() {
      this.axios
        .post("/apiConfig/search", {
          name: this.keyword.name,
          note: this.keyword.note,
          path: this.keyword.path,
          groupId: this.groupId,
        })
        .then((response) => {
          const list = response.data;
          list.forEach((t) => {
            const obj = JSON.parse(t.params);
            t["p"] = obj;
          });
          this.tableData = list;
        })
        .catch((error) => {
          this.$message.error("Search Failed");
        });
    },
    getContext() {
      this.axios
        .post("/apiConfig/context")
        .then((response) => {
          this.context = response.data;
        })
        .catch((error) => {
          this.$message.error("Failed");
        });
    },
    handleDelete(id) {
      this.axios
        .post("/apiConfig/delete/" + id)
        .then((response) => {
          this.$message.success("Delete Success");
          this.search();
        })
        .catch((error) => {
          this.$message.error("Delete Failed");
        });
    },
    online(id) {
      debugger
      this.axios
        .post("/apiConfig/online/" + id)
        .then((response) => {
          this.$message.success("Publish Success");
          this.search();
        })
        .catch((error) => {
          this.$message.error("Publish Failed");
        });
    },
    offline(id) {
      debugger
      this.axios
        .post("/apiConfig/offline/" + id)
        .then((response) => {
          this.$message.success("Already Offline");
          this.search();
        })
        .catch((error) => {
          this.$message.error("Failed");
        });
    },
    httpTest(id) {
      this.$router.push({path: "/api/request", query: {id: id}});
    },
    detail(id) {
      this.$router.push({path: "/api/detail", query: {id: id}});
    },
    handleEdit(id) {
      this.$router.push({path: "/api/edit", query: {id: id}});
    },
    handleAdd() {
      this.$router.push({path: "/api/add"});
    },
    getAllGroups() {
      this.axios
        .post("/group/getAll/")
        .then((response) => {
          this.groups = response.data;
        })
        .catch((error) => {
        });
    },
    getApiTree() {
      this.axios
        .post("/apiConfig/getApiTree/")
        .then((response) => {
          this.treeData = response.data;
        })
        .catch((error) => {
        });
    },
    exportDocs() {
      let a = this.$refs.tree.getCheckedKeys().filter((t) => {
        return t != undefined;
      });
      const ids = a.join(",");
      if (ids == "") {
        return;
      }
      this.axios({
        method: "post",
        params: {ids: ids},
        url: "/apiConfig/apiDocs",
        responseType: "blob", //这个很重要
      })
        .then((res) => {
          console.log(res);
          const link = document.createElement("a");
          let blob = new Blob([res.data], {type: "application/x-msdownload"});
          link.style.display = "none";
          link.href = URL.createObjectURL(blob);
          link.setAttribute("download", "API Doc.md");
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        })
        .catch((error) => {
          this.$message.error("Export Failed");
          console.error(error);
        });
    },
    exportConfig() {
      let a = this.$refs.tree2.getCheckedKeys(true).filter((t) => {
        return t != undefined;
      });
      const ids = a.join(",");
      if (ids == "") {
        return;
      }
      this.axios({
        method: "post",
        params: {ids: ids},
        url: "/apiConfig/downloadConfig",
        responseType: "blob", //这个很重要
      })
        .then((res) => {
          console.log(res);
          const link = document.createElement("a");
          let blob = new Blob([res.data], {type: "application/x-msdownload"});
          link.style.display = "none";
          link.href = URL.createObjectURL(blob);
          link.setAttribute("download", "api_config.json");
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        })
        .catch((error) => {
          this.$message.error("Export Failed");
          console.error(error);
        });
    },
    exportGroupConfig() {
      console.log(this.checkList);
      const ids = this.checkList.join(",");
      this.axios({
        method: "post",
        params: {ids: ids},
        url: "/apiConfig/downloadGroupConfig",
        responseType: "blob", //这个很重要
      })
        .then((res) => {
          console.log(res);
          const link = document.createElement("a");
          let blob = new Blob([res.data], {type: "application/x-msdownload"});
          link.style.display = "none";
          link.href = URL.createObjectURL(blob);
          link.setAttribute("download", "api_group_config.json");
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        })
        .catch((error) => {
          this.$message.error("Export Failed");
          console.error(error);
        });
    },
  },

  created() {
    // this.getAllApis();
    this.getAllGroups();
    this.getContext();
    // this.getAllApiTree();
  },
};
</script>

<style scoped lang="less">
.api_root {
  display: flex;

  height: calc(100vh - 65px);

  .group {
    width: 350px;
    padding: 20px 10px;

    //flex-grow: 0;
    //flex-shrink: 0;
    //background-color: #324256;
  }

  .api {
    padding: 20px;
    width: calc(100vw - 330px);

    .search {
      margin: 0 0 5px 0;

      .label {
        font-weight: 700;
        font-size: 14px;
        margin: 0 5px 0 10px;
      }
    }
  }
}


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
</style>
