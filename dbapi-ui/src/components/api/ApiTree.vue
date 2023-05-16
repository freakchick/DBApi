<template>
  <div class="tree_root">
    <el-button type="primary" icon="el-icon-plus" @click="createDialog = true" size="mini" plain>{{ $t('m.create_group') }}</el-button>

    <el-button type="primary" icon="el-icon-refresh" circle @click="getAllApiTree" size="mini" plain></el-button>

    <el-tree :data="data" node-key="id" default-expand-all :expand-on-click-node="true" :props="defaultProp">
        <span class="custom-tree-node" slot-scope="{ node, data }">
<!--          <div class="left">-->
            <span class="left" >

              <span class="el-icon-folder-opened title" v-if="data.type=='group'" :title="node.label"> {{ node.label }}</span>
              <span class="iconfont icon-api title" :class="{disable:data.status === 0}" v-if="data.type=='api'" :title="node.label"> {{ node.label }}</span>
            </span>
<!--            <span class="iconfont icon-api"  v-if="data.type=='api'">{{ node.label }}</span>-->
<!--          </div>-->

<!--          <div class="right">-->
            <span class="align:right" v-if="data.type=='api'">
              <el-dropdown size="medium">
                  <i class="el-icon-arrow-down el-icon--right"></i>
                <el-dropdown-menu slot="dropdown">
                   <el-dropdown-item>
                    <el-tooltip class="item" effect="light" :content="$t('m.edit')" placement="left">
                      <i class="el-icon-edit" @click="$router.push({path: '/api/edit', query: {id: data.id}});"></i>
                    </el-tooltip>
                  </el-dropdown-item>

                  <el-dropdown-item v-if="data.status == 0">
                    <el-tooltip class="item" effect="light" :content="$t('m.online')" placement="left">
                      <i class="el-icon-top" @click="online(data.id)"></i>
                    </el-tooltip>
                  </el-dropdown-item>
                  <el-dropdown-item v-if="data.status == 1">
                    <el-tooltip class="item" effect="light" :content="$t('m.offline')" placement="left">
                      <i class="el-icon-bottom" @click="offline(data.id)"></i>
                    </el-tooltip>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-tooltip class="item" effect="light" :content="$t('m.request_test')" placement="left">
                      <i class="iconfont icon-HTTPRequest" @click="$router.push({path: '/api/request', query: {id: data.id}});"></i>
                    </el-tooltip>
                  </el-dropdown-item>

                  <el-dropdown-item>
                    <el-tooltip class="item" effect="light" :content="$t('m.delete')" placement="left">
                      <i class="el-icon-delete" style="color: #c50303" @click="deleteAPI(data.id)"></i>
                    </el-tooltip>
                  </el-dropdown-item>

                </el-dropdown-menu>
              </el-dropdown>
            </span>
            <span class="align:right" v-if="data.type=='group'">
              <el-tooltip :open-delay="500" class="item" effect="light" content="Delete Group" placement="top" v-if="data.children.length == 0">
                <i class="el-icon-delete" @click="deleteGroup(data.id)" style="color: #c50303"></i>
              </el-tooltip>
              <el-tooltip :open-delay="500" class="item" effect="light" :content="$t('m.create_api')" placement="top">
                <i class="el-icon-circle-plus" @click="$router.push({path: '/api/add', query: {groupId: data.id}});"></i>
              </el-tooltip>
            </span>
<!--          </div>-->
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
  box-shadow: 0px 0px 3px 2px rgba(196, 194, 194, 0.34);
  height: calc(100vh - 100px);
  font-size: 18px;
  padding: 5px ;
  overflow: auto;
  //border-radius: 5px;

  i {
    padding: 0 5px;
    font-size: 20px;

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
    //padding-right: 8px;

    .left{

        width: 220px;
        //background-color: #ec8282;
        overflow: hidden; // 文字超长隐藏
        text-overflow: ellipsis; // 显示...
        white-space: nowrap; // 单行显示

    }
    .right{
      width: 50px;
      background-color: #ec5d5d;
      //text-align: right;
    }

    .disable {
      text-decoration: line-through;
      color: #cccccc;
    }
  }
}
</style>