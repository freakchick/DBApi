<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import Home from '@/views/Home.vue'

export default {
  name: 'app',
  components: {
    HelloWorld,Home
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
  width: 1000px;
  margin: 0 auto;

}
</style>
