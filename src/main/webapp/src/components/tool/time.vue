<template>
  <div>
    <h3>日期<i class="el-icon-right"></i>时间戳</h3>
    <div style="margin: 10px 0">
      <el-date-picker v-model="value" type="datetime" style="margin-right: 5px" placeholder="yyyy-MM-dd hh:mm:ss"></el-date-picker>
      <el-button type="primary" @click="transform" style="margin-right: 5px" icon="el-icon-d-arrow-right"></el-button>

      <el-input class="text" v-model="timestamp">

      </el-input>


      <el-select v-model="type" style="width:70px">
        <el-option label="ms" value="ms"></el-option>
        <el-option label="s" value="s"></el-option>
      </el-select>
    </div>
    <h3>时间戳<i class="el-icon-right"></i>日期</h3>
    <div style="margin: 10px 0">
      <el-input class="text" v-model="timestamp2" placeholder="输入时间戳">

      </el-input>
      <el-select v-model="type2" style="width:70px">
        <el-option label="ms" value="ms"></el-option>
        <el-option label="s" value="s"></el-option>
      </el-select>

      <el-button @click="toDate" type="primary"  style="margin:0 5px" icon="el-icon-d-arrow-right"></el-button>
      <el-input class="text" v-model="dateString"></el-input>
    </div>
  </div>
</template>

<script>
export default {
  name: "time",
  data() {
    return {
      value: null,
      timestamp: null,
      timestamp2: null,
      dateString: null,
      type: "ms",
      type2: "ms"
    }
  },
  methods: {
    transform() {
      this.timestamp = this.value.getTime()
      if (this.type === 'ms') {
        this.timestamp = this.value.getTime()
      } else if (this.type === 's') {
        this.timestamp = this.value.getTime() / 1000
      }
    },
    toDate() {
      if (this.type2 === 'ms') {

        this.dateString = this.getTime(parseInt(this.timestamp2));
      } else if (this.type2 === 's') {
        this.dateString = this.getTime(parseInt(this.timestamp2) * 1000);
      }
    },
    getTime(nS) {
      var date = new Date(nS);
      var year = date.getFullYear();
      var mon = date.getMonth() + 1;
      var day = date.getDate();
      var hours = date.getHours();
      var minu = date.getMinutes();
      var sec = date.getSeconds();

      return year + '-' + mon + '-' + day + ' ' + hours + ':' + minu + ':' + sec;
    }
  }
}
</script>

<style scoped>
.text {
  width: 200px;
}

.f {
  /*display: flex;*/
}
</style>