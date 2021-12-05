<template>
  <div v-show="active">
    <slot></slot>
  </div>
</template>
<script>
export default {
  name: "mytabPane",
  data() {
    return {
      flag: false,
    };
  },
  props: {
    label: {
      type: String,
      default: null,
    },
    value: {
      type: String,
      default: null,
    },
  },
  computed: {
    active() {
      const active = this.$parent.currentName === this.label;
      return active;
    },
  },
};
</script>
<style scoped>
</style>

