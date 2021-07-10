<template>
  <div class="form">
    <span class="label">{{ (nullable ? '' : '*') + label }}</span>
    <div class="preffix" v-if="preffix != ''">{{ preffix }}</div>
    <input class="input" :value="value" @input="handleInput" :style="'width: ' + width"/>
  </div>
</template>
<script>
export default {
  props: {
    value: {
      type: String,
      default: "",
    },
    label: {
      type: String,
      default: "",
    },
    nullable: {
      type: Boolean,
      default: true
    },
    preffix: {
      type: String,
      default: ''
    },
    width: {
      type: String,
      default: '200px'
    },
  },
  methods: {
    handleInput(e) {
      this.$emit("input", e.target.value);
    },
  },
};
</script>

<style lang="scss" scoped>
.form {
  display: inline-block;
  height: 40px;
  line-height: 40px;
  vertical-align: middle;
  margin-right: 10px;
  margin-bottom: 5px;
  font-family: Consolas, Arial, monospace !important;
  white-space: nowrap;
  //padding: 10px;
  //background-color: #009933;

  .label {
    display: inline-block;
    background-color: #009933;
    //height: 40px;
    padding: 0 10px;
    color: #fff;
    border: 0px solid #a7a7a7;
    border-radius: 5px 0 0 5px;
    vertical-align: middle;
  }

  .preffix {
    display: inline-block;
    height: 40px;
    background-color: #f5f7fa;
    border: 1px solid #ccc;
    vertical-align: middle;
    padding: 0 3px;
    border-right-width: 0px;

  }

  .input {
    display: inline-block;
    height: 40px;
    padding: 0 10px;
    vertical-align: middle;
    border: 1px solid #ccc;
    border-left: 0px;
    outline: 0px solid #afecab;
    border-radius: 0 5px 5px 0;
    font-family: Consolas, Arial, monospace !important;

    &:focus {
      border: 1px solid #009933;
      border-left: 0px;
    }
  }
}
</style>