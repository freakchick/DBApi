<template>
  <div>
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">{{ $t('m.back') }}
    </el-button>
    <h2>{{ $t('m.request_test') }}</h2>
    <el-tabs tab-position="top" type="border-card" @tab-click="handleTabClick">
      <el-tab-pane label="接口测试">
        <div class="mycontent">
          <div v-show="previlege == PREVILEGE.PRIVATE">
            <h2>申请token</h2>
            <el-input v-model="tokenUrl">
              <el-button slot="append" icon="el-icon-caret-right" @click="getToken"></el-button>
            </el-input>
            <el-alert type="warning" show-icon>请使用您的应用id（appid）和密钥（secret）来申请token，访问私有接口需要使用token</el-alert>
          </div>

          <h2>访问接口</h2>

          <h4>{{ $t('m.url') }}：</h4>
          <el-input v-model="url"></el-input>

          <el-alert type="warning" show-icon v-show="this.$store.state.mode == 'cluster'" title="如果是外网访问请将网关地址设置为外网IP端口"
                    style="margin-top: 10px;">
          </el-alert>
          <h4>Header：</h4>

          <el-form label-width="150px" style="width: 600px" size="medium">
            <el-form-item label="Content-Type">
              <el-input v-model="contentType" disabled></el-input>
            </el-form-item>
            <el-form-item label="Authorization" v-show="previlege == PREVILEGE.PRIVATE">
              <el-input v-model="token" placeholder="请填入token"></el-input>
            </el-form-item>
          </el-form>

          <h4>{{ $t('m.parameters') }}：</h4>
          <div class="textarea">
            <el-input v-model="jsonParam" placeholder="填写json参数" type="textarea" rows="10"
                      v-show="contentType === CONTENT_TYPE.JSON"></el-input>
          </div>
          <el-form
              label-width="150px"
              style="width: 600px"
              size="medium"
              v-show="contentType === CONTENT_TYPE.FORM_URLENCODED"
          >
            <el-form-item
                v-for="(item,index) in params"
                :key="item.id"
                style="margin-bottom: 5px"
            >
              <template slot="label">
                <data-tag
                    :name="item.name"
                    :type="item.type"
                ></data-tag>
              </template>
              <el-input
                  v-model="item.value"
                  v-if="!item.type.startsWith('Array')"
              >
                <!--          <template slot="append">{{ item.type }}</template>-->
              </el-input>
              <div v-show="item.type.startsWith('Array')">
                <div
                    v-for="(childItem,childIndex) in item.values"
                    :key="childIndex"
                >
                  <el-input
                      v-model="childItem.va"
                      style="width: 400px"
                  >
                  </el-input>
                  <el-button
                      slot="append"
                      icon="el-icon-delete"
                      type="danger"
                      circle
                      size="mini"
                      @click="deleteRow(index,childIndex)"
                      style="margin-left: 4px;"
                  ></el-button>
                </div>

                <el-button
                    icon="el-icon-plus"
                    type="primary"
                    circle
                    size="mini"
                    @click="addRow(index)"
                ></el-button>
              </div>
            </el-form-item>

          </el-form>
          <el-button @click="request">{{ $t('m.send') }}</el-button>

          <h4>{{ $t('m.result') }}：</h4>

          <el-table
              :data="tableData"
              v-show="showTable"
              size="mini"
              border
              stripe
              max-height="700"
          >
            <el-table-column
                :prop="item"
                :label="item"
                v-for="item in keys"
                :key="item"
            ></el-table-column>
          </el-table>
          <el-input
              type="textarea"
              v-model="response"
              :autosize="{ minRows: 5, maxRows: 20 }"
              class="my"
              v-show="!showTable"
          ></el-input>

          <el-button
              size="small"
              @click="format"
              class="button"
          >{{ $t('m.json_format') }}
          </el-button>

        </div>
      </el-tab-pane>
      <el-tab-pane :label="CALL_EXAMPLE_TAB_NAME">
        <call-example
            ref="callExample"
            :address="url"
            :detail="{path,params,previlege,jsonParam,contentType,token}"
        />
      </el-tab-pane>
    </el-tabs>

  </div>

</template>

<script>
import {CONTENT_TYPE, PREVILEGE} from "@/constant";
import callExample from "@/components/api/common/callExample";

export default {
  name: "request",
  components: {callExample},
  data() {
    return {
      CONTENT_TYPE: Object.freeze(CONTENT_TYPE),
      PREVILEGE: Object.freeze(PREVILEGE),
      api: {},
      params: [],
      path: null,
      previlege: PREVILEGE.PRIVATE,
      address: null,
      response: null,
      isSelect: null,
      keys: [],
      tableData: [],
      showTable: false,
      token: null,
      url: "",
      contentType: null,
      jsonParam: null,
      CALL_EXAMPLE_TAB_NAME: Object.freeze("调用示例"),
      tokenUrl: `http://${this.address}/token`
    };
  },
  methods: {
    getToken() {
      this.axios
          .get(this.tokenUrl)
          .then((response) => {
            console.log(response.data)
            this.$message.success(response.data.token)
            this.token = response.data.token
          })
          .catch((error) => {
            this.$message.error("get token failed");
          });
    },
    async getDetail(id) {
      await this.axios
          .post("/apiConfig/detail/" + id)
          .then((response) => {
            this.path = response.data.path;
            this.previlege = response.data.previlege;
            let params = JSON.parse(response.data.params);
            params.forEach((t) => {
              if (t.type.startsWith("Array")) {
                t.values = [{va: ""}];
              }
            });
            this.params = params;
            this.isSelect = response.data.isSelect;

            this.url = `http://${this.address}/${this.path}`;
            this.contentType = response.data.contentType;
            this.jsonParam = response.data.jsonParam;
          })
          .catch((error) => {
            this.$message.error("get detail failed");
          });
    },
    async getAddress() {
      await this.axios
          .post("/apiConfig/getIPPort")
          .then((response) => {
            this.address = response.data;
            this.url = `http://${this.address}/${this.path}`;
            this.tokenUrl = `http://${this.address}/token/generate?appid=xxx&secret=xxx`;
          })
          .catch((error) => {
            this.$message.error("get address failed");
          });
    },
    async getIP() {
      await this.axios
          .post("/apiConfig/getIP")
          .then((response) => {
            this.tokenUrl = `http://${response.data}/token/generate?appid=xxx&secret=xxx`;
          })
          .catch((error) => {
            this.$message.error("get ip failed");
          });
    },
    request() {
      this.response = null;
      let p = {};
      if (this.contentType === CONTENT_TYPE.FORM_URLENCODED) {
        this.params.forEach((t) => {
          //构造数组类型的请求参数
          if (t.type.startsWith("Array")) {
            const values = t.values.map((item) => item.va);
            p[t.name] = values;
          } else p[t.name] = t.value;
        });
      } else if (this.contentType === CONTENT_TYPE.JSON) {
        p = this.jsonParam;
      }
      // let url = `http://${this.address}/api/${this.path}`
      this.axios
          .post(this.url, p, {
            headers: {
              "Content-Type": this.contentType,
              Authorization: this.token == null ? "" : this.token,
            },
          })
          .then((response) => {
            if (response.status == 200) {
              this.showTable = false;
              this.response = JSON.stringify(response.data);
            }
          })
          .catch((error) => {
            // 只要状态码不是200都会进入catch逻辑
            this.$message.error(error.response.data.msg);
          });
    },
    format() {
      const o = JSON.parse(this.response);
      this.response = JSON.stringify(o, null, 4);
    },
    tableShow() {
      if (this.response == null) return;
      let obj = JSON.parse(this.response);
      if (obj.success) {
        this.tableData = obj.data;
        if (obj.data.length > 0) {
          this.keys = Object.keys(obj.data[0]);
        } else {
          return;
        }
      } else {
        return;
      }
      this.showTable = true;
    },
    tableHide() {
      this.showTable = false;
    },
    addRow(index) {
      this.params[index].values.push({va: null});
    },
    deleteRow(index, childIndex) {
      this.params[index].values.splice(childIndex, 1);
    },
    handleTabClick(tab) {
      const label = tab.label;
      if (label === this.CALL_EXAMPLE_TAB_NAME) {
        this.$nextTick(() => {
          this.$refs.callExample.refresh();
        });
      }
    },
  },
  created() {
    this.getDetail(this.$route.query.id);
    this.getAddress();
    this.getIP()
  },
  computed: {
    // url(){
    //   return `http://${ this.address }/api/${ this.path }`
    // }
  },
};
</script>

<style scoped lang="scss">
.my > .el-textarea__inner {
  font-family: "Consolas", Helvetica, Arial, sans-serif;
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

.textarea {
  /deep/ .el-textarea__inner {
    font-family: "Consolas", Helvetica, Arial, sans-serif !important;
    font-size: 16px !important;
    line-height: 20px;
  }
}
</style>
