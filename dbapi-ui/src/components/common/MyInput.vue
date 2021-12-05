<template>
  <div class="form">
    <span class="label">{{ (nullable ? '' : '*') + label }}</span>
    <div class="preffix" v-if="preffix != ''">{{ preffix }}</div>
    <input class="input" :value="value" @input="handleInput" :style="'width: ' + width" :placeholder="placeholder"/>
  </div>
</template>
<script>
export default {
  name: "myInput",
  props: {
    value: {
      type: String | Number,
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
    placeholder: {
      type: String,
      default: ''
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
input::-webkit-input-placeholder {
  /* WebKit browsers */
  color: #ccc;
}

input:-moz-placeholder {
  /* Mozilla Firefox 4 to 18 */
  color: #ccc;
}

input::-moz-placeholder {
  /* Mozilla Firefox 19+ */
  color: #ccc;
}

input:-ms-input-placeholder {
  /* Internet Explorer 10+ */
  color: #ccc;
}

.form {
  display: inline-block;
  height: 40px;
  line-height: 40px;
  vertical-align: top;
  margin-right: 10px;
  margin-bottom: 5px;
  font-family: Consolas, Arial, monospace !important;
  white-space: nowrap;
  //padding: 10px;
  //background-color: #009933;

  .label {
    display: inline-block;
    background-color: #06B176;
    //height: 40px;
    padding: 0 10px;
    color: #fff;
    border: 0px solid #a7a7a7;
    border-radius: 5px 0 0 5px;
    //vertical-align: middle;
  }

  .preffix {
    display: inline-block;
    height: 40px;
    background-color: #f5f7fa;
    border: 1px solid #ccc;
    vertical-align: top;
    padding: 0 3px;
    border-right-width: 0px;

  }

  .input {
    display: inline-block;
    height: 40px;
    padding: 0 10px;
    vertical-align: top;
    border: 1px solid #ccc;
    border-left: 0px;
    outline: 0px solid #afecab;
    border-radius: 0 5px 5px 0;
    font-family: Consolas, Arial, monospace !important;
    font-size: 16px;

    &:focus {
      border: 1px solid #009933;
      border-left: 0px;
    }
  }
}
</style>