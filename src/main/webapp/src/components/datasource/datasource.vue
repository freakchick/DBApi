<template>
  <div>
    <div class="gap">
    <router-link to="/datasource/add" >

      <el-button  type="primary" plain>创建数据源</el-button>
    </router-link>
    </div>
    <el-table :data="tableData" border stripe max-height="700">
      <el-table-column label="名称">
        <template slot-scope="scope">
          <db-icon :type="scope.row.type"></db-icon>
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
<!--      <el-table-column prop="type" label="数据库"></el-table-column>-->
      <el-table-column prop="note" label="描述"></el-table-column>
      <!--      <el-table-column prop="url" label="地址"></el-table-column>-->
      <el-table-column label="操作">
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


  </div>
</template>

<script>
import dbIcon from '@/components/common/dbIcon'

export default {
  name: "datasource",
  data() {
    return {
      tableData: []
    }
  },
  methods: {
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
        this.$message.error("查询所有数据源失败")
      })
    },
    handleDelete(id) {
      this.axios.post("/datasource/delete/" + id).then((response) => {
        if (response.data.success) {
          this.$message.success("删除成功")
        } else {
          this.$message.error(response.data.msg)
        }
        this.getAllSource()
      }).catch((error) => {
        this.$message.error("删除失败")
      })
    }
  },
  created() {
    this.getAllSource()
  },
  components: {dbIcon}
}
</script>

<style scoped>
.my >>> .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
  /*font-size: 18px;*/
}

</style>
