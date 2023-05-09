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

    <el-input v-model="url" size="mini" style="width: 300px">
      <template slot="prepend">Url:</template>
    </el-input>
    <el-input v-model="status" size="mini" style="width: 200px">
      <template slot="prepend">Status:</template>
    </el-input>
    <el-input v-model="clientId" size="mini" style="width: 250px">
      <template slot="prepend">clientId:</template>
    </el-input>

    <el-input v-model="ip" size="mini" style="width: 250px">
      <template slot="prepend">IP:</template>
    </el-input>

    <el-button size="mini" @click="query">{{ $t("m.query") }}</el-button>

    <el-divider></el-divider>

    <el-table :data="tableData" border stripe max-height="700" size="mini">
      <el-table-column label="Time">
        <template slot-scope="scope">
          <span>{{ scope.row.timestamp | dateFormat }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="url" label="URL"></el-table-column>
      <el-table-column prop="duration" label="Duration(ms)"></el-table-column>
      <el-table-column prop="status" label="Response Code"></el-table-column>
      <el-table-column prop="clientId" label="clientId"></el-table-column>
      <el-table-column prop="ip" label="IP"></el-table-column>
      <el-table-column prop="error" label="Error"></el-table-column>
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
      url: null,
      status: null,
      clientId: null,
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
          clientId: this.clientId,
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