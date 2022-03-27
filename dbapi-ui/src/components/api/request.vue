<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">{{ $t('m.back') }}
    </el-button>
    <h2>{{ $t('m.request_test') }}</h2>
    <h4>{{ $t('m.url') }}：</h4>
    <el-input v-model="url"></el-input>

    <el-alert type="warning" show-icon v-show="this.$store.state.mode == 'cluster'"
              title="如果是外网访问请将网关地址设置为外网IP端口" style="margin-top: 10px;">
    </el-alert>
    <h4 >Header：</h4>

    <el-form label-width="150px" style="width: 600px" size="medium">
      <el-form-item label="Content-Type">
        <el-input v-model="contentType" disabled></el-input>
      </el-form-item>
      <el-form-item label="token"  v-show="previlege == 0">
        <el-input v-model="token"></el-input>
      </el-form-item>
    </el-form>


    <h4>{{ $t('m.parameters') }}：</h4>
    <el-input v-model="jsonParam" placeholder="填写json参数示例，用于生成接口文档" type="textarea" rows="10" v-show="contentType ==='application/json' "></el-input>

    <el-form label-width="150px" style="width: 600px" size="medium" v-show="contentType ==='application/x-www-form-urlencoded' ">
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
    <el-button @click="request">{{ $t('m.send') }}</el-button>

    <h4>{{ $t('m.result') }}：</h4>

    <el-table :data="tableData" v-show="showTable" size="mini" border stripe max-height="700">
      <el-table-column :prop="item" :label="item" v-for="item in keys" :key="item"></el-table-column>
    </el-table>
    <el-input type="textarea" v-model="response" :autosize="{ minRows: 5, maxRows: 20 }" class="my"
              v-show="!showTable"></el-input>

    <el-button size="small" @click="format" class="button">{{ $t('m.json_format') }}</el-button>

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
      previlege: 0,
      address: null,
      response: null,
      isSelect: null,
      keys: [],
      tableData: [],
      showTable: false,
      token: null,
      url: '',
      contentType:null,
      jsonParam:null
    }
  },
  methods: {
    async getDetail(id) {
      await this.axios.post("/apiConfig/detail/" + id).then((response) => {
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

        this.url = `http://${this.address}/${this.path}`
        this.contentType = response.data.contentType
        this.jsonParam = response.data.jsonParam
      }).catch((error) => {
        this.$message.error("get detail failed")
      })
    },
    async getAddress() {
      await this.axios.post("/apiConfig/getIPPort").then((response) => {
        this.address = response.data
        this.url = `http://${this.address}/${this.path}`
      }).catch((error) => {
        this.$message.error("get address failed")
      })
    },
    request() {
      this.response = null
      let p = {}
      if (this.contentType === 'application/x-www-form-urlencoded'){
        this.params.forEach(t => {
          //构造数组类型的请求参数
          if (t.type.startsWith('Array')) {
            const values = t.values.map(item => item.va)
            p[t.name] = values
          } else
            p[t.name] = t.value
        })
      }else if(this.contentType === 'application/json'){
        p = this.jsonParam
      }

      // let url = `http://${this.address}/api/${this.path}`

      this.axios.post(this.url, p, {
        headers: {
          "Content-Type": this.contentType,
          "Authorization": this.token == null ? '' : this.token
        }
      }).then((response) => {
        if (response.status == 200) {
          this.showTable = false
          this.response = JSON.stringify(response.data)
        }

      }).catch((error) => {
        // 只要状态码不是200都会进入catch逻辑
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
  // components: {dataTag},
  created() {
    this.getDetail(this.$route.query.id)
    this.getAddress()
  },
  computed: {
    // url(){
    //   return `http://${ this.address }/api/${ this.path }`
    // }
  }

}
</script>

<style scoped lang="scss">
.my > > > .el-textarea__inner {
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

.url {
  padding: 5px;
  border: 1px solid #ccc;
  .input {
    background-color: rgba(1, 87, 36, 0.06);
    font-size: 16px;
    padding: 5px;
    border-width: 0px;
    outline: none;
  }

}


.button {
  margin: 10px 10px 10px 0;
}
</style>
