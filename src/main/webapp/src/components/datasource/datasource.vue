<template>
  <div>
    <el-button @click="dialogVisible = true">创建数据源</el-button>

    <el-table :data="tableData">
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="type" label="数据库"></el-table-column>
      <el-table-column prop="note" label="描述"></el-table-column>
      <!--      <el-table-column prop="url" label="地址"></el-table-column>-->
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit( scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete( scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="添加数据源" :visible.sync="dialogVisible">

      <el-form label-width="100px">
        <el-form-item label="数据库">
          <el-select v-model="type" placeholder="请选择" @change="selectDB">
            <el-option v-for="item in options" :key="item.label" :label="item.label" :value="item.label">
              <i class="iconfont icon-my-SQL" v-if="item.label == 'mysql'"></i>
              <i class="iconfont icon-postgre-sql" v-if="item.label == 'postgreSql'"></i>
              <i class="iconfont icon-hive" v-if="item.label == 'hive'"></i>
              <i class="iconfont icon-SQLServer" v-if="item.label == 'sqlServer'"></i>
              <span>{{ item.label }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="name"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="note"></el-input>
        </el-form-item>
        <el-form-item label="url">
          <el-input v-model="url" type="textarea" class="my"></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="username"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="password"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="connect">连接测试</el-button>
         <el-button type="primary" @click="dialogVisible = false;add()">确 定</el-button>
      </span>
    </el-dialog>


  </div>
</template>

<script>
export default {
  name: "datasource",
  data() {
    return {
      tableData: [],
      type: null,
      dialogVisible: false,
      options: [{label: 'mysql'}, {label: 'postgreSql'}, {label: 'hive'}, {label: 'sqlServer'}],
      url: null,
      username: null,
      password: null,
      name: null,
      note: null
    }
  },
  methods: {
    clickAdd() {

    },
    selectDB() {
      console.log(this.type)
      if (this.type == 'mysql') {
        this.url = 'jdbc:mysql://localhost:3306/db?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8'
      } else if (this.type == 'postgreSql') {
        this.url = 'jdbc:postgresql://localhost:5432/db'
      } else if (this.type == 'hive') {
        this.url = 'jdbc:hive2://localhost:10000/db'
      } else if (this.type == 'sqlServer') {
        this.url = 'jdbc:microsoft:sqlserver://localhost:1433;databaseName=db'
      }
    },
    add() {
      this.axios.post("/datasource/add", {
        "name": this.name,
        "note": this.note,
        "url": this.url,
        "username": this.username,
        "password": this.password,
        "type": this.type
      }).then((response) => {
        this.$message.success("添加成功")
        this.getAllSource()
      }).catch((error) => {
        this.$message.error("添加失败")
      })
    },
    connect() {
      this.axios.post("/datasource/connect", {
        "url": this.url,
        "username": this.username,
        "password": this.password,
        "type": this.type
      }).then((response) => {
        if (response.data.success)
          this.$message.success("成功")
        else
          this.$message.error(response.data.msg)
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    getAllSource() {
      this.axios.post("/datasource/getAll").then((response) => {
        this.tableData = response.data
      }).catch((error) => {
        this.$message.error("查询所有数据源失败")
      })
    },
    handleDelete(row) {
      this.axios.post("/datasource/delete/" + row.id).then((response) => {
        this.$message.success("删除成功")
        this.getAllSource()
      }).catch((error) => {
        this.$message.error("删除失败")
      })
    }
  },
  created() {
    this.getAllSource()
  }
}
</script>

<style scoped>
.my >>> .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
  /*font-size: 18px;*/
}
i {
  color: #0698a5;
  /*background-color: #0698a5;*/
  font-size: 18px;
  font-weight: 700;
  margin-right: 5px;
  /*padding: 2px;*/
  /*border-radius: 10px;*/
}

</style>
