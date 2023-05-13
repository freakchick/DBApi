<template>
  <div class="tree_root">
    <el-button type="primary" icon="el-icon-plus" @click="createDialog = true" size="mini" plain>{{ $t('m.create_group') }}</el-button>

    <el-tree :data="data" node-key="id" default-expand-all :expand-on-click-node="false" :props="defaultProp">
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span class="el-icon-folder-opened" v-if="data.type=='group'">{{ node.label }}</span>
          <span class="iconfont icon-api" :class="{disable:data.status === 0}" v-if="data.type=='api'">
<!--             <i class="iconfont icon-on_line1 circle" v-if="data.status == 1"></i>-->
            <!--              <i class="iconfont icon-off_line circle offline" v-else></i>-->
            <!--            <i class="el-icon-lock circle lock" v-if="data.access == PRIVILEGE.PRIVATE"></i>-->
            <!--            <i class="el-icon-unlock circle " v-else></i>-->
            {{ node.label }}
          </span>

           <span class="align:right" v-if="data.type=='api'">
            <el-dropdown size="small">
                <i class="el-icon-arrow-down el-icon--right"></i>
              <el-dropdown-menu slot="dropdown">
                 <el-dropdown-item>
                  <el-tooltip class="item" effect="light" content="Edit API" placement="left">
                    <i class="el-icon-edit" @click="$router.push({path: '/api/edit', query: {id: data.id}});"></i>
                  </el-tooltip>
                </el-dropdown-item>

                <el-dropdown-item v-if="data.status == 0">
                  <el-tooltip class="item" effect="light" content="Online API" placement="left">
                    <i class="el-icon-top" @click="online(data.id)"></i>
                  </el-tooltip>
                </el-dropdown-item>
                <el-dropdown-item v-if="data.status == 1">
                  <el-tooltip class="item" effect="light" content="Offline API" placement="left">
                    <i class="el-icon-bottom" @click="offline(data.id)"></i>
                  </el-tooltip>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-tooltip class="item" effect="light" content="Request" placement="left">
                    <i class="iconfont icon-HTTPRequest" @click="$router.push({path: '/api/request', query: {id: data.id}});"></i>
                  </el-tooltip>
                </el-dropdown-item>

                <el-dropdown-item>
                  <el-tooltip class="item" effect="light" content="Delete API" placement="left">
                    <i class="el-icon-delete" @click="deleteAPI(data.id)"></i>
                  </el-tooltip>
                </el-dropdown-item>

              </el-dropdown-menu>
            </el-dropdown>
          </span>
          <span class="align:right" v-if="data.type=='group'">
            <el-tooltip class="item" effect="light" content="Delete Group" placement="top" v-if="data.children.length == 0">
              <i class="el-icon-delete" @click="deleteGroup(data.id)"></i>
            </el-tooltip>
            <el-tooltip class="item" effect="light" :content="$t('m.create_api')" placement="top">
              <i class="el-icon-circle-plus" @click="$router.push({path: '/api/add', query: {groupId: data.id}});"></i>
            </el-tooltip>


            <!--          <el-button type="text" size="mini" @click="() => append(data)">Append</el-button>-->
            <!--          <el-button type="text" size="mini" @click="() => remove(data)">Delete</el-button>-->
          </span>
        </span>
    </el-tree>

    <el-dialog :title="$t('m.create_group')" :visible.sync="createDialog">
      <el-input v-model="groupName"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="createDialog = false">{{ $t('m.cancel') }}</el-button>
        <el-button type="primary" @click="createDialog = false;createGroup()">{{ $t('m.save') }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ApiTree",
  data() {
    return {
      data: [],
      groupName: null,
      defaultProp: {children: 'children', label: 'name'},
      createDialog: false
    }
  },
  methods: {

    deleteAPI(id) {
      this.axios
        .post("/apiConfig/delete/" + id)
        .then((response) => {
          this.$message.success("Delete Success");
          this.getAllApiTree();
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
          this.getAllApiTree();
        })
        .catch((error) => {
          this.$message.error("Publish Failed");
        });
    },
    offline(id) {
      this.axios
        .post("/apiConfig/offline/" + id)
        .then((response) => {
          this.$message.success("Already Offline");
          this.getAllApiTree();
        })
        .catch((error) => {
          this.$message.error("Offline Failed");
        });
    },
    createGroup() {
      this.axios.post("/group/create/", {name: this.groupName}).then((response) => {
        this.getAllApiTree()
      }).catch((error) => {
        this.$message.error(error.response.data.message)
      })
    },
    deleteGroup(id) {
      this.axios.post("/group/delete/" + id).then((response) => {
        if (response.data.success) {
          this.getAllApiTree()
        } else {
          this.$message.error(response.data.msg)
        }
      }).catch((error) => {
      })
    },
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
  },
  created() {
    this.getAllApiTree();
  }
}
</script>

<style scoped lang="less">
.tree_root {
  i {
    padding: 0 5px;

    &:hover {
      font-weight: bold;
    }
  }

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;

    .disable {
      text-decoration: line-through;
      color: #cccccc;
    }
  }
}
</style>