<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import Home from '@/views/Home.vue'

export default {
  name: 'app',
  components: {
    Home
  }
}
</script>

<style>
/*每个页面公共css */
* {
  margin: 0;
  padding: 0;
  /* css3盒子模型 */
  box-sizing: border-box;
}

/* 去掉li 的小圆点 */
li {
  list-style: none
}

a {
  color: #666;
  text-decoration: none
}

img {
  /* border 0 照顾低版本浏览器 如果 图片外面包含了链接会有边框的问题 */
  border: 0;
  /* 取消图片底侧有空白缝隙的问题 */
  vertical-align: middle
}

#app {
  font-family: 'Consolas', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*display: flex;*/
  /*justify-content: center;*/
  width: auto;
  margin: 0 auto;

}

i {
  font-size: 14px;
}

h2 {
  margin-bottom: 25px;
  text-align: center;
}

h4 {
  margin: 10px 0;
}

.el-button--primary.is-active,
.el-button--primary:active {
  background: #20B2AA;
  border-color: #20B2AA;
  color: #fff;
}

.mycontent{
  margin: 0 auto;
  max-width: 1300px;
}

.gap{
  margin: 0 5px 5px 0;
}

</style>
