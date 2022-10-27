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
          <v-chart :option="lineData" style="height: 300px"></v-chart>
        </el-col>
        <el-col :span="12">
          <v-chart :option="pieData" style="height: 300px"></v-chart>
        </el-col>
        <el-col :span="12">
          <v-chart :option="data4" style="height: 300px"></v-chart>
        </el-col>
        <el-col :span="12">
          <v-chart :option="data4" style="height: 300px"></v-chart>
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
          text: "Referer of a Website",
          subtext: "Fake Data",
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
            name: "Access From",
            type: "pie",
            radius: "50%",
            data: [
              {value: 1048, name: "Search Engine"},
              {value: 735, name: "Direct"},
              {value: 580, name: "Email"},
              {value: 484, name: "Union Ads"},
              {value: 300, name: "Video Ads"},
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
      data4: {
        title: {
          text: "World Population",
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
          data: ["Brazil", "Indonesia", "USA", "India", "China", "World"],
        },
        series: [
          {
            name: "2011",
            type: "bar",
            data: [18203, 23489, 29034, 104970, 131744, 630230],
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
          .catch((error) => {
            this.$message.error("Failed");
          });
    },

  },
  created() {
    const now = new Date();
    this.time[1] = now;
    this.time[0] = this.$moment(now).subtract(7, "days");

    // this.countByDay();
  },
};
</script>

<style scoped>
</style>