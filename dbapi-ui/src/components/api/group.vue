<template>
  <div>
    <el-tag :key="tag.id" v-for="tag in groups" closable :disable-transitions="false" @close="handleClose(tag.id)" effect="dark">
      {{ tag.name }}
    </el-tag>
    <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue"
              ref="saveTagInput" size="small" @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
    </el-input>

    <el-button v-else class="button-new-tag" size="small" @click="showInput">+ 添加新的分组
    </el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      groups: [],
      inputVisible: false,
      inputValue: ''
    };
  },
  methods: {
    getAllGroups() {
      this.axios.post("/group/getAll/").then((response) => {
        this.groups = response.data
      }).catch((error) => {
      })
    },
    createGroup() {
      this.axios.post("/group/create/", {name: this.inputValue}).then((response) => {
        this.getAllGroups()
      }).catch((error) => {
        console.log(error.response)
        this.$message.error(error.response.data.message)
      })
    },
    handleClose(id) {
      this.axios.post("/group/delete/" + id).then((response) => {
        if (response.data.success) {
          this.getAllGroups()
        } else {
          this.$message.error(response.data.msg)
        }
      }).catch((error) => {
      })
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.createGroup()
      }
      this.inputVisible = false;
      this.inputValue = '';
    }
  },
  created() {
    this.getAllGroups()
  }
}
</script>

<style scoped>
.el-tag {
  margin: 0px 5px 5px 0;
}

.button-new-tag {

  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.input-new-tag {
  width: 120px;
  /*margin: -5px 0 0 5px;*/

  /*vertical-align: bottom;*/
}
</style>