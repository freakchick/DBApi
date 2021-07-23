<template>
  <div :class="['form', size == 'mini' ? 'mini':'']">
    <span class="label">{{ (nullable ? '' : '*') + label }}</span>
    <div class="inputPP" :style="'width: ' + width">
      <input class="input" :value="getLabel()" @input="handleInput" readonly="readonly" @focus.stop="showOptions = true"

             @blur.stop="showOptions = false"/>
      <div class="options" v-show="showOptions">
        <div class="option" @mousedown="optionClick(item)" v-for="(item, index) in options">
          {{ item[option_label] }}
        </div>
        <div class="bottom">
          <slot></slot>
        </div>
        <div v-if="options.length == 0" class="none">暂无数据</div>
      </div>


      <div class="el-icon-arrow-down icon"></div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      showOptions: false,

    };
  },
  props: {
    value: {
      type: String | Number,
      default: "",
    },
    label: {
      type: String,
      default: "",
    },
    size: {
      type: String,
      default: "",
    },
    options: {
      type: Array,
      default: [],
    },
    option_label: {
      type: String,
      default: 'label'
    },
    option_value: {
      type: String,
      default: 'value'
    },
    width: {
      type: String,
      default: '200px'
    },
    nullable: {
      type: Boolean,
      default: true
    },
  },
  methods: {
    getLabel() {
      let p = null;
      // console.log(this.options);
      this.options.forEach((elem, index) => {
        if (elem[this.option_value] == this.value) {
          p = elem[this.option_label];
        }
      });
      return p;
    },
    optionClick(v) {
      this.$emit("input", (v[this.option_value]).toString());
      this.showOptions = false;
      this.$emit("optionClick", v);
    },
    handleInput(e) {
      // this.$emit("input", e.target.value);
    }
  },
  watch: {
   /* a(val, oldVal) {//普通的watch监听
      console.log("a: " + val, oldVal);
    }*/
  }
};
</script>

<style lang="scss" scoped>


.form {
  display: inline-block;
  line-height: 40px;
  vertical-align: middle;
  margin-right: 10px;
  margin-bottom: 5px;
  position: relative;
  font-family: Consolas, Arial, monospace;
  white-space: nowrap;


  .label {
    display: inline-block;
    background-color: #06B176;
    height: 40px;
    padding: 0 10px;
    color: #fff;
    border: 1px solid #a7a7a7;
    vertical-align: middle;
    border-radius: 5px 0 0 5px;
  }

  .inputPP {
    width: 200px;
    display: inline-block;
    position: relative;
    vertical-align: middle;

    .icon {
      position: absolute;
      right: 0px;
      top: 0px;
      line-height: 40px;
      padding-right: 5px;
    }

    .input {
      display: block;
      padding: 0 10px;
      width: 100%;
      height: 40px;
      vertical-align: middle;
      border: 1px solid #a7a7a7;
      border-left: 0px;
      outline: 0px solid #afecab;
      border-radius: 0 5px 5px 0;
      font-family: Consolas, Arial, monospace;

      &:focus {
        border: 1px solid #009933;
        border-left: 0px;
      }
    }

    .options {
      //border: 1px solid #009933;
      border-radius: 5px;
      width: 100%;
      position: absolute;
      right: 0;
      top: 42px;
      background-color: #fff;
      box-shadow: 0 0 3px #82848a;
      z-index: 1000;

      .option {
        margin: 2px 0;

        padding: 0px 10px;
        line-height: 30px;
        cursor: pointer;

        &:hover {
          //color: rgb(61, 192, 21);
          background-color: #f6f6f6;
        }
      }

      .none {
        text-align: center;
        color: #ccc;
      }
    }

    .bottom {
      //border-top: 1px solid #a7a7a7;
    }
  }


}

.mini {
  line-height: 26px;
  height: 26px;
  margin: 0;
  border-radius: 0;
  //border-width: 0;

  .label {
    height: 26px;
    border-radius: 0;
  }

  .inputPP {


    .options {
      top: 26px;
    }

    .input {
      height: 26px;
      border-radius: 0;
    }

    .icon {
      line-height: 26px;
    }
  }
}
</style>
