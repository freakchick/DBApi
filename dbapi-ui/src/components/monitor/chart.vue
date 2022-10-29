<template>
  <div>
    <el-date-picker
        size="mini"
        v-model="time"
        type="datetimerange"
        :picker-options="pickerOptions"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        align="right"
    >
    </el-date-picker>
    <el-button size="mini" @click="query">查询</el-button>
    <div>
      <el-row :gutter="20">

        <el-col :span="12">
          <div class="box">
            <v-chart :option="lineData" style="height: 300px"></v-chart>
          </div>

        </el-col>
        <el-col :span="12">
          <div class="box">
            <v-chart :option="pieData" style="height: 300px"></v-chart>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="box">
            <v-chart :option="data3" style="height: 300px"></v-chart>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="box">
            <v-chart :option="data4" style="height: 300px"></v-chart>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="box">
            <v-chart :option="data5" style="height: 300px"></v-chart>
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
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
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
          text: "API访问总量",
          left: "center",
        },
        tooltip: {
          trigger: "item",
        },
        legend: {
          orient: "vertical",
          left: "left",
        },
        series: [
          {
            name: "API访问总次数",
            type: "pie",
            radius: "50%",
            data: [
              {value: 1048, name: "Success"},
              {value: 735, name: "Fail"}
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
          text: "API访问量走势",
          left: "center",
        },
        color: ["#11D238", "#FF101F"],
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
              opacity: 0.8,
              color: "rgb(5,148,99)",
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
              opacity: 0.8,
              color: "rgb(255, 0, 135)",
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
          text: "访问量最大的客户端",
          left: "center",
        },
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
          text: "访问量最大的API",
          left: "center",
        },
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
          text: "平均访问时长最大的API",
          left: "center",
        },
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
          })

      this.axios
          .post("/access/successRatio", {
            start: parseInt(this.$moment(this.time[0]).valueOf() / 1000),
            end: parseInt(this.$moment(this.time[1]).valueOf() / 1000)
          })
          .then((response) => {
            this.lineData.series[0].data = [{name: "Success", value: response.data.successNum}, {
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
          .post("/access/top5app", {
            start: parseInt(this.$moment(this.time[0]).valueOf() / 1000),
            end: parseInt(this.$moment(this.time[1]).valueOf() / 1000)
          })
          .then((response) => {
            this.data3.yAxis.data = response.data.map(t => t.app_id).reverse()
            this.data3.series[0].data = response.data.map(t => t.num).reverse()
          });
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
  span: 3px;
  box-shadow: 1px 3px 3px #cccccc;
  margin: 3px;
}
</style>