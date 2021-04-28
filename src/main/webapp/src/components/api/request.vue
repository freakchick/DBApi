<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">返回</el-button>
    <h2>接口请求测试</h2>
    <h4>接口地址：</h4>
    <div class="path">http://{{ address }}/api/{{ path }}</div>

    <h4 v-show="previlege == 0">请求header：</h4>

    <el-form label-width="150px" style="width: 600px" size="medium" v-show="previlege == 0">
      <el-form-item label="token:">
        <el-input v-model="token"></el-input>
      </el-form-item>
    </el-form>


    <h4>接口参数：</h4>
    <el-form label-width="150px" style="width: 600px" size="medium">
      <el-form-item v-for="(item,index) in params" :key="index" style="margin-bottom: 5px">
        <template slot="label">
          <data-tag :name="item.name" :type="item.type"></data-tag>
        </template>
        <el-input v-model="item.value" v-if="!item.type.startsWith('list')">
          <!--          <template slot="append">{{ item.type }}</template>-->
        </el-input>
        <div v-show="item.type.startsWith('list')">
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
    <el-button @click="request">发送请求</el-button>

    <h4>返回结果：</h4>

    <el-table :data="tableData" v-show="showTable" size="mini" border stripe max-height="700">
      <el-table-column :prop="item" :label="item" v-for="item in keys" :key="item"></el-table-column>
    </el-table>
    <el-input type="textarea" v-model="response" :autosize="{ minRows: 5, maxRows: 20 }" class="my"
              v-show="!showTable"></el-input>

    <el-button size="small" @click="format" class="button">格式化json</el-button>
    <el-button size="small" @click="tableShow" class="button" v-if="isSelect == 1">表格展示</el-button>
    <el-button size="small" @click="tableHide" class="button" v-if="isSelect == 1">原始数据</el-button>

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
          if (t.type.startsWith("list")) {
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
        if (t.type.startsWith('list')) {
          const values = t.values.map(item => item.va)
          p[t.name] = values
        } else
          p[t.name] = t.value
      })
      console.log(p)
      let url = `http://${this.address}/api/${this.path}`

      this.axios({
        method: 'post',
        params: p,
        url: url,
        headers: {
          "Content-type": "application/x-www-form-urlencoded"
          // "Authorization": this.token == null ? '' : this.token
        }
      }).then((res) => {
        this.showTable = false
        this.response = JSON.stringify(res.data)
      }).catch(error => {
        this.$message.error(error.response.data.msg)
      })

      /*   const xhr = new XMLHttpRequest();
         xhr.open("POST", url, false);
         xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
         // xhr.setRequestHeader("Authorization", this.token == null ? '' : this.token);
         xhr.onreadystatechange = () => {
           var XMLHttpReq = xhr;
           if (XMLHttpReq.readyState == 4) {
             if (XMLHttpReq.status == 200) {
               var data = XMLHttpReq.responseText
               this.showTable = false
               this.response = data
             } else {
               var data = XMLHttpReq.responseText
               const obj = JSON.parse(data)
               this.$message.error(obj.msg)
             }
           }
         };

         //构造数组类型的请求参数
         var data = new FormData();
         this.params.forEach(t => {
           if (t.type.startsWith('list')) {
             const values = t.values.map(item => item.va)
             data.append(t.name,values)
           } else {
             data.append(t.name,t.value)
           }
         })

         xhr.send(data)*/

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
  font-size: 18px;
  border: 1px solid;
  padding: 5px;

}

.button {
  margin: 10px 10px 10px 0;
}
</style>
