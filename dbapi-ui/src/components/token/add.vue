<template>
  <div class="mycontent">
    <el-button icon="el-icon-d-arrow-left" type="info" plain @click="$router.go(-1)" size="small">{{ $t('m.back') }}
    </el-button>
    <h2>{{ $t('m.create_token') }}</h2>
    <el-form label-width="160px">

      <el-form-item label="Token">
        <el-input v-model="token">
          <el-button slot="append" @click="generate">{{ $t('m.generate') }}</el-button>
        </el-input>
      </el-form-item>
      <el-form-item :label="$t('m.note')">
        <el-input v-model="note"></el-input>
      </el-form-item>
      <el-form-item :label="$t('m.expire')">
        <el-date-picker v-model="expire" type="datetime" :placeholder="$t('m.expire_tip')"
                        width="200px"></el-date-picker>
      </el-form-item>

    </el-form>

    <el-button type="primary" @click="save" plain>{{ $t('m.save') }}</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      expire: null,
      token: null,
      note: null
    }
  },
  methods: {

    save() {
      if (this.expire != null)
        this.expire = this.expire.getTime()

      this.axios.post("/token/add/", {token: this.token, note: this.note, expire: this.expire}).then((response) => {
        this.$message.success("Create Success")
        this.$router.push("/token")
      }).catch((error) => {
        this.$message.error("Create Failed")
      })
    },
    generate() {
      this.axios.post("/token/generate/").then((response) => {
        this.token = response.data
      }).catch((error) => {
      })
    }
  }
}
</script>

<style scoped>

</style>