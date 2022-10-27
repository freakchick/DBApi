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
    <el-button size="mini">查询</el-button>
    <div>
      <el-row :gutter="20">
        <el-col :span="12">
          <v-chart :option="option_column" style="height: 300px"></v-chart>
        </el-col>
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
      option_column: {
        title: { text: "Column Chart" },
        tooltip: {},
        xAxis: {
          data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"],
        },
        yAxis: {},
        series: [
          {
            name: "销量",
            type: "line",
            data: [5, 20, 36, 10, 10, 20],
          },
        ],
      },
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
              { value: 1048, name: "Search Engine" },
              { value: 735, name: "Direct" },
              { value: 580, name: "Email" },
              { value: 484, name: "Union Ads" },
              { value: 300, name: "Video Ads" },
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
          text: "成功失败走势t",
        },
        color: ["#5470c6", "#91ca75"],
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
        toolbox: {
          feature: {
            saveAsImage: {},
          },
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
            data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
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

            emphasis: {
              focus: "series",
            },

            data: [220, 182, 191, 234, 290, 330, 310],
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
            data: [320, 332, 301, 334, 390, 330, 320],
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
    countByDay() {
      this.axios
        .post("/access/countByDay", {
          start: "2022-10-20",
          end: "2022-10-25",
        })
        .then((response) => {
          this.$message.success("Success");
        })
        .catch((error) => {
          this.$message.error("Failed");
        });
    },
  },
  created() {
    const now = new Date();
    this.time[0] = now;
    this.time[1] = this.$moment(now).subtract(7, "days");

    // this.countByDay();
  },
};
</script>

<style scoped>
</style>