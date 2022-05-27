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
        "name": "dbapi",
        "note": null,
        "path": "test",
        "params": [
          {
            "name": "id",
            "type": "string"
          }
        ],
        "groupId": "djwUzW2E",
        "previlege": 1,
        "cachePlugin": null,
        "cachePluginParams": null,
        "alarmPlugin": null,
        "alarmPluginParam": null,
        "jsonParam": null,
        "sqlList": [
          {
            "sqlText": "-- 请输入sql，一个标签只能输入一条sql\\nselect * from user\\n\\n<where>\\nid = #{id}\\n</where>",
            "transformPlugin": null,
            "transformPluginParams": null
          }
        ],
        "contentType": "application/x-www-form-urlencoded",
        "openTrans": 0
      }
     */
    detail: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      shellCode: `curl -H "Content-Type: application/json" -H "Authorization: 5ad0dcb4eb03d3b0b7e4b82ae0ba433f" -X POST  --data '{"key":"value"}' http://127.0.0.1:8520/api/xxx`,
      javaScriptCode: `refresh() {
      if (this.$refs.codeView?.refresh) {
        this.$refs.codeView.refresh();
      }
      if (this.$refs.codeView2?.refresh) {
        this.$refs.codeView2.refresh();
      }
    },`,
      pythonCode: `# Some Example code
import os
from package import ParentClass
`,
      goCode: `// Prime Sieve in Go.
// Taken from the Go specification.
// Copyright © The Go Authors.

package main

import "fmt"

// Send the sequence 2, 3, 4, ... to channel 'ch'.
func generate(ch chan<- int) {
	for i := 2; ; i++ {
		ch <- i  // Send 'i' to channel 'ch'
	}
}`,
      javaCode: `import com.demo.util.MyType;
import com.demo.util.MyInterface;

public enum Enum {
  VAL1, VAL2, VAL3
}

public class Class<T, V> implements MyInterface {
  public static final MyType<T, V> member;
  
  private class InnerClass {
    public int zero() {
      return 0;
    }
  }

  @Override
  public MyType method() {
    return member;
  }

  public void method2(MyType<T, V> value) {
    method();
    value.method3();
    member = value;
  }
}
`,
    };
  },
  methods: {
    refresh() {
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