<template>
  <div>
    <code-view
      ref="shell"
      lang-display-name="shell"
      :value="shellCode"
      mode="application/x-sh"
      :is-first="true"
    />
    <code-view
      ref="javascript"
      lang-display-name="javascript"
      :value="javaScriptCode"
      mode="text/javascript"
    />
    <code-view
      ref="python"
      lang-display-name="python"
      :value="pythonCode"
      mode="text/x-python"
    />
    <code-view
      ref="go"
      lang-display-name="go"
      :value="goCode"
      mode="text/x-go"
    />
    <code-view
      ref="java"
      lang-display-name="java"
      :value="javaCode"
      mode="text/x-java"
    />
  </div>
</template>

<script>
import codeView from "@/components/api/common/codeView";
import { generateCallExampleCode } from "@/utils";
import { LANGUAGE } from "@/constant";
export default {
  name: "CallExample",
  components: {
    codeView,
  },
  props: {
    // 请求地址 "127.0.0.1:8520/api"
    address: {
      type: String,
      default: "",
    },
    /**
      {
        "path": "test",
        "params": [
          {
            "name": "id",
            "type": "string",
            "values": [
              {
                "va":"a"
              },
              {
                "va":"b"
              }
            ]
          }
        ],
        "previlege": 1,
        "jsonParam": null,
        "sqlList": [
          {
            "sqlText": "-- 请输入sql，一个标签只能输入一条sql\\nselect * from user\\n\\n<where>\\nid = #{id}\\n</where>",
            "transformPlugin": null,
            "transformPluginParams": null
          }
        ],
        "contentType": "application/x-www-form-urlencoded",
        "token": null,
      }
     */
    detail: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      shellCode: ``,
      javaScriptCode: "",
      pythonCode: "",
      goCode: "",
      javaCode: "",
    };
  },
  methods: {
    initCallExampleCode() {
      this.shellCode = generateCallExampleCode(
        this.generateOtherParam(LANGUAGE.SHELL)
      );
      this.javaScriptCode = generateCallExampleCode(
        this.generateOtherParam(LANGUAGE.JAVASCRIPT)
      );
      this.pythonCode = generateCallExampleCode(
        this.generateOtherParam(LANGUAGE.PYTHON)
      );
      this.goCode = generateCallExampleCode(
        this.generateOtherParam(LANGUAGE.GO)
      );
      this.javaCode = generateCallExampleCode(
        this.generateOtherParam(LANGUAGE.JAVA)
      );
    },
    generateOtherParam(lang) {
      return {
        address: this.address,
        detail: this.detail,
        lang,
      };
    },
    refresh() {
      this.initCallExampleCode();
      Object.keys(this.$refs).forEach((refName) => {
        if (this.$refs[refName]?.refresh) {
          this.$refs[refName].refresh();
        }
      });
    },
  },
};
</script>

<style scoped>
</style>