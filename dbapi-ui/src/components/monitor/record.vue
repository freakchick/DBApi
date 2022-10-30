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

    <el-input v-model="url" size="mini" style="width: 300px">
      <template slot="prepend">Url:</template>
    </el-input>
    <el-input v-model="status" size="mini" style="width: 200px">
      <template slot="prepend">Status:</template>
    </el-input>
    <el-input v-model="appId" size="mini" style="width: 250px">
      <template slot="prepend">appId:</template>
    </el-input>

    <el-input v-model="ip" size="mini" style="width: 250px">
      <template slot="prepend">IP:</template>
    </el-input>

    <el-button size="mini" @click="query">{{$t('m.query')}}</el-button>

    <el-divider></el-divider>

    <el-table :data="tableData" border stripe max-height="700" size="mini">
      <el-table-column label="time">
        <template slot-scope="scope">
          <span>{{ scope.row.timestamp | dateFormat }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="url" label="url"></el-table-column>
      <el-table-column prop="duration" label="duration(ms)"></el-table-column>
      <el-table-column prop="status" label="Response Code"></el-table-column>
      <el-table-column prop="appId" label="appId"></el-table-column>
      <el-table-column prop="IP" label="ip"></el-table-column>
      <el-table-column prop="error" label="error"></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "record",
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
      url: null,
      status: null,
      appId: null,
      ip: null,
      tableData: [],
    };
  },

  methods: {
    query() {
      this.tableData = [];
      this.axios
        .post("/access/search", {
          start: parseInt(this.$moment(this.time[0]).valueOf() / 1000),
          end: parseInt(this.$moment(this.time[1]).valueOf() / 1000),
          url: this.url,
          status: this.status,
          appId: this.appId,
          ip: this.ip,
        })
        .then((response) => {
          this.tableData = response.data;
        });
    },
  },
  created() {
    const now = new Date();
    this.time[1] = now;
    this.time[0] = this.$moment(now).subtract(7, "days");
  },
};
</script>

<style scoped>
</style>