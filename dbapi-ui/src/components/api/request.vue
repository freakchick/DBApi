<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">{{$t('m.back')}}</el-button>
    <h2>{{$t('m.request_test')}}</h2>
    <h4>{{$t('m.url')}}：</h4>
    <div class="path">http://{{ address }}/api/{{ path }}</div>

    <h4 v-show="previlege == 0">{{$t('m.header')}}：</h4>

    <el-form label-width="150px" style="width: 600px" size="medium" v-show="previlege == 0">
      <el-form-item label="token:">
        <el-input v-model="token"></el-input>
      </el-form-item>
    </el-form>


    <h4>{{$t('m.parameters')}}：</h4>
    <el-form label-width="150px" style="width: 600px" size="medium">
      <el-form-item v-for="(item,index) in params" :key="index" style="margin-bottom: 5px">
        <template slot="label">
          <data-tag :name="item.name" :type="item.type"></data-tag>
        </template>
        <el-input v-model="item.value" v-if="!item.type.startsWith('Array')">
          <!--          <template slot="append">{{ item.type }}</template>-->
        </el-input>
        <div v-show="item.type.startsWith('Array')">
          <div v-for="(childItem,childIndex) in item.values" :key="childIndex">
            <el-input v-model="childItem.va" style="width: 400px">
            </el-input>
            <el-button slot="append" icon="el-icon-delete" type="danger" circle size="mini"
                       @click="deleteRow(index,childIndex)" style="margin-left: 4px;"></el-button>
          </div>

          <el-button icon="el-icon-plus" type="primary" circle size="mini" @click="addRow(index)"></el-button>
        </div>
      </el-form-item>

    </el-form>
    <el-button @click="request">{{$t('m.send')}}</el-button>

    <h4>{{ $t('m.result') }}：</h4>

    <el-table :data="tableData" v-show="showTable" size="mini" border stripe max-height="700">
      <el-table-column :prop="item" :label="item" v-for="item in keys" :key="item"></el-table-column>
    </el-table>
    <el-input type="textarea" v-model="response" :autosize="{ minRows: 5, maxRows: 20 }" class="my"
              v-show="!showTable"></el-input>

    <el-button size="small" @click="format" class="button">{{$t('m.json_format')}}</el-button>
    <el-button size="small" @click="tableShow" class="button">{{$t('m.view_in_table')}}</el-button>
    <el-button size="small" @click="tableHide" class="button">{{$t('m.raw_data')}}</el-button>

  </div>
</template>

<script>
import dataTag from "@/components/common/dataTag";

export default {
  name: "request",
  data() {
    return {
      api: {},
      params: [],
      path: null,
      previlege: 0,
      address: null,
      response: null,
      isSelect: null,
      keys: [],
      tableData: [],
      showTable: false,
      token: null
    }
  },
  methods: {
    getDetail(id) {
      this.axios.post("/apiConfig/detail/" + id).then((response) => {
        this.path = response.data.path
        this.previlege = response.data.previlege
        let params = JSON.parse(response.data.params)
        params.forEach(t => {
          if (t.type.startsWith("Array")) {
            t.values = [{va: ''}]
          }
        })
        this.params = params
        this.isSelect = response.data.isSelect
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
        //构造数组类型的请求参数
        if (t.type.startsWith('Array')) {
          const values = t.values.map(item => item.va)
          p[t.name] = values
        } else
          p[t.name] = t.value
      })
      let url = `http://${this.address}/api/${this.path}`

      this.axios.post(url, p, {
        headers: {
          "Content-type": "application/x-www-form-urlencoded",
          "Authorization": this.token == null ? '' : this.token
        }
      }).then((response) => {
        if (response.status == 200) {
          this.showTable = false
          this.response = JSON.stringify(response.data)
        } else {
          this.$message.error(response.response.data.msg)
        }

      }).catch((error) => {
        this.$message.error(error.response.data.msg)
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
      this.params[index].values.push({"va": null})
    },
    deleteRow(index, childIndex) {
      this.params[index].values.splice(childIndex, 1)
    }
  },
  components: {dataTag},
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
  font-size: 16px;
  border: 1px #ccc solid;
  padding: 10px 10px;


}

.button {
  margin: 10px 10px 10px 0;
}
</style>
