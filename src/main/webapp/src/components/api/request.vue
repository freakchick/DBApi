<template>
  <div>
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">返回</el-button>
    <h2>接口请求测试</h2>
    <h4>接口地址：</h4>
    <div class="path">http://{{ address }}/api/{{ path }}</div>

    <h4>接口参数：</h4>
    <el-form label-width="100px" style="width: 400px" size="medium">
      <el-form-item :label="item.name + '：'" v-for="(item,index) in params">
        <el-input v-model="item.value" v-if="!item.type.startsWith('list')">
          <template slot="append">{{ item.type }}</template>
        </el-input>
        <div v-if="item.type.startsWith('list')">
          <div v-for="tt in item.value">
            <el-input  v-model="tt.v" style="width: 150px" >
            </el-input>
            <el-button slot="append" icon="el-icon-delete" type="danger" circle size="mini" @click=""></el-button>
          </div>

          <el-button icon="el-icon-plus" type="primary" circle size="mini" @click="addRow(index)"></el-button>
        </div>
      </el-form-item>

    </el-form>
    <el-button @click="request">发送请求</el-button>

    <h4>返回结果：</h4>

    <el-table :data="tableData" v-show="showTable" size="mini" border stripe max-height="700">
      <el-table-column :prop="item" :label="item" v-for="item in keys" :key="item"></el-table-column>
    </el-table>
    <el-input type="textarea" v-model="response" :autosize="{ minRows: 5, maxRows: 20 }" class="my" v-show="!showTable"></el-input>

    <el-button size="small" @click="format" class="button">格式化json</el-button>
    <el-button size="small" @click="tableShow" class="button" v-if="isSelect == 1">表格展示</el-button>
    <el-button size="small" @click="tableHide" class="button" v-if="isSelect == 1">原始数据</el-button>

  </div>
</template>

<script>

export default {
  name: "request",
  data() {
    return {
      api: {},
      params: [],
      path: null,
      address: null,
      response: null,
      isSelect: null,
      keys: [],
      tableData: [],
      showTable: false
    }
  },
  methods: {
    getDetail(id) {
      this.axios.post("/apiConfig/detail/" + id).then((response) => {
        this.path = response.data.path
        this.params = JSON.parse(response.data.params)
        this.isSelect = response.data.isSelect

        this.params.forEach(t => {
          if (t.type.startsWith("list"))
            t.value = ['aa','bb']
        })
        console.log(this.params)
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    getAddress() {
      this.axios.post("/apiConfig/getIPPort").then((response) => {
        this.address = response.data
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    request() {
      let p = {}
      this.params.forEach(t => {
        p[t.name] = t.value
      })
      let url = `http://${this.address}/api/${this.path}`
      this.axios.post("/apiConfig/request",
          {url: url, "params": JSON.stringify(p)}
      ).then((response) => {
        this.showTable = false
        if (response.data.success) {
          this.response = response.data.data

        }
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    format() {
      const o = JSON.parse(this.response)
      this.response = JSON.stringify(o, null, 4)
    },
    tableShow() {
      if (this.response == null)
        return
      let obj = JSON.parse(this.response);
      if (obj.success) {
        this.tableData = obj.data
        if (obj.data.length > 0) {
          this.keys = (Object.keys(obj.data[0]))
        } else {
          return
        }
      } else {
        return
      }
      this.showTable = true
    },
    tableHide() {
      this.showTable = false
    },
    addRow(index) {
      console.log( this.params[index])
      this.params[index].value.push('')
    }
  },
  components: {},
  created() {
    this.getDetail(this.$route.query.id)
    this.getAddress()
  }

}
</script>

<style scoped>
.my >>> .el-textarea__inner {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
  /*font-size: 18px;*/
}

h2 {
  margin-bottom: 25px;
  text-align: center;
}

h4 {
  margin: 10px 0;
}

.path {
  font-size: 18px;
  border: 1px solid;
  padding: 5px;

}

.button {
  margin: 10px 10px 10px 0;
}
</style>
