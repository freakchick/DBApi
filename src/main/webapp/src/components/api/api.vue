<template>
  <div>
    <el-button @click="dialogVisible = true">创建api</el-button>

    <!--    <el-table :data="tableData">-->
    <!--      <el-table-column prop="name" label="名称"></el-table-column>-->
    <!--      <el-table-column prop="type" label="数据库"></el-table-column>-->
    <!--      <el-table-column prop="note" label="描述"></el-table-column>-->
    <!--      <el-table-column label="操作">-->
    <!--        <template slot-scope="scope">-->
    <!--          <el-button size="mini" @click="handleEdit( scope.row)">编辑</el-button>-->
    <!--          <el-button size="mini" type="danger" @click="handleDelete( scope.row)">删除</el-button>-->
    <!--        </template>-->
    <!--      </el-table-column>-->
    <!--    </el-table>-->

    <el-dialog title="添加api" :visible.sync="dialogVisible">


      <el-input type="textarea" v-model="sql" placeholder="请输入sql"></el-input>
      <el-button>解析参数</el-button>


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
      sql:null
    }
  }
}
</script>

<style scoped>

</style>
