<template>
  <div>
    <el-form label-width="100px">
      <el-form-item label="api名称">
        <el-input v-model="detail.name"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="detail.note"></el-input>
      </el-form-item>
      <el-form-item label="请求路径">
        <el-input v-model="detail.path" class="my" placeholder="输入请求路径">
          <template slot="prepend">
            <span style="margin: 0 -14px">http://{{ address }}/api/</span>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item label="数据源">
        <el-select v-model="detail.datasourceId" placeholder="请选择">
          <el-option v-for="item in datasources" :key="item.id" :label="item.name" :value="item.id">
            <i class="iconfont icon-my-SQL" v-if="item.type == 'mysql'"></i>
            <i class="iconfont icon-postgre-sql" v-if="item.type == 'postgresql'"></i>
            <i class="iconfont icon-hive" v-if="item.type == 'hive'"></i>
            <i class="iconfont icon-SQLServer" v-if="item.type == 'sqlserver'"></i>
            <span>{{ item.name }}</span>
            <!--            <span style="float: left">{{ item.name }}</span>-->
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="sql类型">
        <el-select v-model="detail.isSelect">
          <el-option label="查询类" value=1></el-option>
          <el-option label="非查询类" value=0></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="sql">
        <el-input type="textarea" v-model="detail.sql" :autosize="{ minRows: 5, maxRows: 20 }" placeholder="请输入sql" class="my"></el-input>
        <!--        <el-button type="primary" plain @click="parseParams" style="margin :10px 0">解析参数</el-button>-->
      </el-form-item>
      <el-form-item label="请求参数">
        <div v-for="(item,index) in detail.params" style="margin-bottom:5px">
          <el-autocomplete v-model="item.name" :fetch-suggestions="parseParams" style="width:200px;margin-right:5px" placeholder="请输入参数名"></el-autocomplete>
          <!--          <el-select v-model="item.type" placeholder="请选择数据类型">-->
          <!--            <el-option label="string" value="string"></el-option>-->
          <!--            <el-option label="double" value="double"></el-option>-->
          <!--            <el-option label="bigint" value="bigint"></el-option>-->
          <!--            <el-option label="date" value="date"></el-option>-->
          <!--          </el-select>-->

          <el-cascader  v-model="item.type" separator=" > " :options="options"></el-cascader>

          <el-button @click="deleteRow" circle type="danger" icon="el-icon-delete" size="mini"></el-button>
        </div>
        <el-button @click="addRow" icon="el-icon-plus" type="primary" circle size="mini"></el-button>

      </el-form-item>


    </el-form>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        datasources: [],
        address: null,
        detail: {
          datasourceId: null,
          name: null,
          note: null,
          path: null,
          isSelect: "1",
          sql: 'select name,age from user where id > #{minId} and id < #{maxId}',
          params: []
        },
        options: [
          {label: 'string', value: 'string'},
          {label: 'bigint', value: 'bigint'},
          {label: 'decimal', value: 'decimal'},
          {label: 'date', value: 'date'},
          {
            label: '数组', children: [
              {label: 'string', value: 'list<string>'},
              {label: 'bigint', value: 'list<bigint>'},
              {label: 'decimal', value: 'list<decimal>'},
              {label: 'date', value: 'list<date>'}]
          }

        ]
      }
    },
    props: ["id"],
    methods: {
      addRow() {
        this.detail.params.push({name: null, type: null})
      },
      deleteRow() {
        // this.detail.params.
      },
      parseParams() {

      },
      getAllSource() {
        this.axios.post("/datasource/getAll").then((response) => {
          this.datasources = response.data
        }).catch((error) => {
          this.$message.error("查询所有数据源失败")
        })
      },
      parseParams() {

        // this.axios.post("/apiConfig/parseParam", {sql: this.detail.sql, datasourceId: this.detail.datasourceId}).then((response) => {
        //   if (response.data.success) {
        //     this.detail.params = response.data.data
        //   } else {
        //     this.$message.error(response.data.msg)
        //   }
        // }).catch((error) => {
        //   this.$message.error("失败")
        // })
      },
      getAddress() {
        this.axios.post("/apiConfig/getIPPort").then((response) => {
          this.address = response.data
        }).catch((error) => {
          this.$message.error("失败")
        })
      },
      getDetail(id) {
        this.id = id
        this.axios.post("/apiConfig/detail/" + id).then((response) => {
          this.detail.name = response.data.name
          this.detail.note = response.data.note
          this.detail.path = response.data.path
          this.detail.sql = response.data.sql
          this.detail.isSelect = response.data.isSelect.toString()
          this.detail.datasourceId = response.data.datasourceId
          this.detail.params = JSON.parse(response.data.params)
        }).catch((error) => {
          this.$message.error("失败")
        })
      }
    },
    created() {
      this.getAllSource()
      this.getAddress()
      if (this.id != undefined)
        this.getDetail(this.id)
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
    font-size: 18px;
    font-weight: 700;
    margin-right: 5px;
  }
</style>
