<template>
  <div>
    <div>
      <ul>
        <li>
          <router-link to="/datasource/add">
            <el-button type="primary" icon="el-icon-plus">{{$t('m.create_ds')}}</el-button>
          </router-link>
        </li>
        <li>
          <el-button type="warning" @click="show=true" icon="el-icon-download" round>{{$t('m.export_ds')}}</el-button>
        </li>
        <li>
          <el-upload action="/datasource/import" accept=".json" :on-success="importSuccess" :headers="headers"
                     :on-error="importFail" :file-list="fileList">
            <el-button type="warning" icon="el-icon-upload2" round>{{$t('m.import_ds')}}</el-button>
          </el-upload>
        </li>
      </ul>
    </div>

    <el-table :data="tableData" border stripe max-height="700">
      <el-table-column prop="id" label="id" width="270px"></el-table-column>
      <el-table-column :label="$t('m.name')">
        <template slot-scope="scope">
          <db-icon :type="scope.row.type"></db-icon>
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="note" :label="$t('m.note')"></el-table-column>
      <el-table-column prop="updateTime" :label="$t('m.update_time')" width="170px"></el-table-column>
      <el-table-column :label="$t('m.operation')" width="220px">
        <template slot-scope="scope">

          <el-button plain size="mini" type="info" @click="detail(scope.row.id)" circle>
            <i class="iconfont icon-detail"></i>
          </el-button>
          <el-button plain size="mini" type="warning" @click="handleEdit(scope.row.id)" circle>
            <i class="el-icon-edit"></i>
          </el-button>
          <el-button plain size="mini" type="danger" @click="handleDelete(scope.row.id)" circle>
            <i class="el-icon-delete"></i>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="$t('m.export_ds')" :visible.sync="show">
      <ul>
        <li v-for="item in tableData">
          <el-checkbox v-model="item.checked">{{item.name}}</el-checkbox>
        </li>
      </ul>
      <span slot="footer" class="dialog-footer">
        <el-button @click="show = false">{{$t('m.cancel')}}</el-button>
        <el-button type="primary" @click="show = false;exportConfig()">{{$t('m.export')}}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import dbIcon from '@/components/common/dbIcon'

export default {
  name: "datasource",
  data() {
    return {
      tableData: [],
      show: false,
      headers: {
        Authorization: localStorage.getItem('token')
      },
      fileList: []
    }
  },
  methods: {
    exportConfig(){
      const ids = this.tableData.filter(t=>t.checked).map(t=>t.id)
      console.log(ids)
      this.axios({
        method: 'post',
        params: {ids: ids.join(",")},
        url: '/datasource/export',
        responseType: 'blob' //这个很重要
      }).then((res) => {
        const link = document.createElement('a')
        let blob = new Blob([res.data], {type: 'application/x-msdownload'});
        link.style.display = 'none'
        link.href = URL.createObjectURL(blob);
        link.setAttribute('download', 'datasource.json')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      }).catch(error => {
        this.$message.error("Export Failed")
        console.error(error)
      })
    },
    importSuccess(response, file, fileList) {
      this.fileList = []
      this.$message.success("Import success")
      this.getAllSource()
    },
    importFail(error, file, fileList) {
      this.$message.error("Import failed!  "+ error.message)
    },
    detail(id) {
      this.$router.push({path: '/datasource/detail', query: {id: id}});
    },
    handleEdit(id) {
      this.$router.push({path: '/datasource/edit', query: {id: id}});
    },
    getAllSource() {
      this.axios.post("/datasource/getAll").then((response) => {
        this.tableData = response.data
      }).catch((error) => {
        // this.$message.error("Query all ")
      })
    },
    handleDelete(id) {
      this.axios.post("/datasource/delete/" + id).then((response) => {
        if (response.data.success) {
          this.$message.success("Delete Success")
        } else {
          this.$message.error(response.data.msg)
        }
        this.getAllSource()
      }).catch((error) => {
        this.$message.error("Delete Failed")
      })
    }
  },
  created() {
    this.getAllSource()
  },
  components: {dbIcon}
}
</script>

<style scoped lang="scss">
.my >>> .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
  /*font-size: 18px;*/
}
ul {
  margin-bottom: 10px;
  li {
    display: inline-block;
    margin-right: 10px;
  }
}
</style>
