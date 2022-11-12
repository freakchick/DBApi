<template>
  <div>
    <el-date-picker
        size="mini"
        v-model="time"
        type="datetimerange"
        :picker-options="pickerOptions"
        :range-separator="$t('m.to')"
        :start-placeholder="$t('m.startDate')"
        :end-placeholder="$t('m.endDate')"
        align="right"
    >
    </el-date-picker>
    <el-button size="mini" @click="query" type="primary">{{$t('m.query')}}</el-button>
    <div>
      <el-row :gutter="20">

        <el-col :span="12">
          <div class="box">
            <v-chart :option="lineData" style="height: 300px"></v-chart>
          </div>

        </el-col>
        <el-col :span="5">
          <div class="box">
            <v-chart :option="pieData" style="height: 300px"></v-chart>
          </div>
        </el-col>
        <el-col :span="7">
          <div class="box">
            <v-chart :option="data3" style="height: 300px"></v-chart>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="box">
            <v-chart :option="data4" style="height: 300px"></v-chart>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="box">
            <v-chart :option="data5" style="height: 300px"></v-chart>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="box">
            <v-chart :option="data6" style="height: 300px"></v-chart>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "monitor",
  data() {
    return {
      pickerOptions: {
        shortcuts: [
          {
            text: this.$t('m.lastWeek'),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t('m.lastMonth'),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t('m.last3Month'),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      time: [],

      pieData: {
        title: {
          text: this.$t('m.api_access_quantity'),
          left: "center",
        },
        tooltip: {
          trigger: "item",
        },
        legend: {
          orient: "vertical",
          left: "left",
        },
        color: ["#99CC33", "#FF6600"],
        series: [
          {
            
            type: "pie",
            radius: "50%",
            data: [
              
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
          },
        ],
      },
      lineData: {
        title: {
          text: this.$t('m.api_access_trend'),
          left: "center",
        },
        color: ["#99CC66", "#FF6666"],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            label: {
              backgroundColor: "#6a7985",
            },
          },
        },
        legend: {
          left: "left",
          data: ["Success", "Failed"],
        },

        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            data: [],
          },
        ],
        yAxis: [
          {
            type: "value",
          },
        ],
        series: [
          {
            name: "Success",
            type: "line",
            stack: "Total",
            areaStyle: {
              opacity: 0.3,
              color: "#99CC66",
            },
            emphasis: {
              focus: "series",
            },

            data: [],
          },

          {
            name: "Failed",
            type: "line",
            stack: "Total",
            areaStyle: {
              opacity: 0.3,
              color: "#FF6666",
            },
            emphasis: {
              focus: "series",
            },
            data: [],
          },
        ],
      },
      data3: {
        title: {
          text: this.$t('m.top_n_app'),
          left: "center",
        },
        color: ['#33CC99'],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
        },
        legend: {},
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: {
          type: "value",
          boundaryGap: [0, 0.01],
        },
        yAxis: {
          type: "category",
          data: [],
        },
        series: [
          {

            type: "bar",
            data: [],
          },
        ],
      },
      data4: {
        title: {
          text: this.$t('m.top_n_api'),
          left: "center",
        },
        color: ['#66CCCC'],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
        },
        legend: {},
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: {
          type: "value",
          boundaryGap: [0, 0.01],
        },
        yAxis: {
          type: "category",
          data: [],
        },
        series: [
          {

            type: "bar",
            data: [],
          },
        ],
      },
      data5: {
        title: {
          text: this.$t('m.top_n_duration'),
          left: "center",
        },
        color: ['#FF9900'],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
        },
        legend: {},
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: {
          type: "value",
          boundaryGap: [0, 0.01],
        },
        yAxis: {
          type: "category",
          data: [],
        },
        series: [
          {

            type: "bar",
            data: [],
          },
        ],
      },
      data6: {
        title: {
          text: this.$t('m.top_n_ip'),
          left: "center",
        },
        color: ['#B39CD0'],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
        },
        legend: {},
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: {
          type: "value",
          boundaryGap: [0, 0.01],
        },
        yAxis: {
          type: "category",
          data: [],
        },
        series: [
          {

            type: "bar",
            data: [],
          },
        ],
      },
    };
  },
  watch:{
    '$i18n.locale'(newValue) {
       
      this.pieData.title.text=this.$t('m.api_access_quantity')
      this.lineData.title.text=this.$t('m.api_access_trend')
      this.data3.title.text=this.$t('m.top_n_app')
      this.data4.title.text=this.$t('m.top_n_api')
      this.data5.title.text=this.$t('m.top_n_duration')
      this.data6.title.text=this.$t('m.top_n_ip')
    }
  },
  methods: {
    query() {

      this.axios
          .post("/access/countByDay", {
            start: parseInt(this.$moment(this.time[0]).valueOf() / 1000),
            end: parseInt(this.$moment(this.time[1]).valueOf() / 1000)
          })
          .then((response) => {
            this.lineData.xAxis[0].data = response.data.map(t => t.date)
            this.lineData.series[0].data = response.data.map(t => t.successNum)
            this.lineData.series[1].data = response.data.map(t => t.failNum)

            console.log(this.lineData.xAxis[0].data)
            console.log(this.lineData.series[0].data)
            console.log(this.lineData.series[1].data)
          })

      this.axios
          .post("/access/successRatio", {
            start: parseInt(this.$moment(this.time[0]).valueOf() / 1000),
            end: parseInt(this.$moment(this.time[1]).valueOf() / 1000)
          })
          .then((response) => {
            this.pieData.series[0].data = [{name: "Success", value: response.data.successNum}, {
              name: "Fail",
              value: response.data.failNum
            }]
          })
      this.axios
          .post("/access/top5api", {
            start: parseInt(this.$moment(this.time[0]).valueOf() / 1000),
            end: parseInt(this.$moment(this.time[1]).valueOf() / 1000)
          })
          .then((response) => {
            this.data4.yAxis.data = response.data.map(t => t.url).reverse()
            this.data4.series[0].data = response.data.map(t => t.num).reverse()
          });
      this.axios
          .post("/access/topNIP", {
            start: parseInt(this.$moment(this.time[0]).valueOf() / 1000),
            end: parseInt(this.$moment(this.time[1]).valueOf() / 1000)
          })
          .then((response) => {
            this.data6.yAxis.data = response.data.map(t => t.ip).reverse()
            this.data6.series[0].data = response.data.map(t => t.num).reverse()
          });
      this.axios
          .post("/access/top5app", {
            start: parseInt(this.$moment(this.time[0]).valueOf() / 1000),
            end: parseInt(this.$moment(this.time[1]).valueOf() / 1000)
          })
          .then((response) => {
            this.data3.yAxis.data = response.data.map(t => t.app_id).reverse()
            this.data3.series[0].data = response.data.map(t => t.num).reverse()
          });

      this.axios
          .post("/access/top5duration", {
            start: parseInt(this.$moment(this.time[0]).valueOf() / 1000),
            end: parseInt(this.$moment(this.time[1]).valueOf() / 1000)
          })
          .then((response) => {
            this.data5.yAxis.data = response.data.map(t => t.url).reverse()
            this.data5.series[0].data = response.data.map(t => t.duration).reverse()
          });
    },

  },
  created() {
    const now = new Date();
    this.time[1] = now;
    this.time[0] = this.$moment(now).subtract(7, "days");

    this.query();
  },
};
</script>

<style scoped lang="scss">
.box {
  //height: 330px;
  border-radius: 3px;
  box-shadow: 1px 3px 3px #cccccc;
  //padding: 5px 0;
}
</style>