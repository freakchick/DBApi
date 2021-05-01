import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import axios from 'axios'
import VueAxios from 'vue-axios'
import qs from 'qs'


import './theme/index.css'

// import store from './store'

Vue.config.productionTip = false

//使用vue-axios，这样才可以全局使用this.axios调用
Vue.use(VueAxios, axios);


// axios.defaults.baseURL = '/api'

axios.defaults.headers = {'Content-Type': 'application/x-www-form-urlencoded'}
//全局拦截post请求的参数，用qs序列化
axios.interceptors.request.use(config => {
  //form表单提交multipart/form-data的时候，不需要序列化参数
  if (config.method === 'post' && config.headers['Content-Type'] === 'application/x-www-form-urlencoded') {
    config.data = qs.stringify(config.data, {indices: false});
  }
  //拦截所有请求，添加登陆用户的token
  if (localStorage.getItem('token')) {
    const x = config.headers.Authorization
    if (x === null || x === undefined) {
      config.headers.Authorization = localStorage.getItem('token');
    }
  }
  return config
})

//后台用户登陆信息校验不成功就跳转到登录页面
axios.interceptors.response.use(response => {
  if (response.status == '401') {
    console.log('已过期重新登陆');
    router.push("/login");
  } else {
    return response;
  }
}, error => {
  if (error.response.status == '401') {
    console.log('--已过期重新登陆');
    router.push("/login");
  } else {
    return Promise.reject(error)
  }
})

//前端页面路由跳转时校验用户登陆信息不成功就跳转到登录页
// router.beforeEach((to, from, next) => {
//     //非登录页拦截
//     if (to.name != "Login") {
//
//       var user = store.state.user;
//       //没有登陆就拦截
//       if (user == null) {
//         next({path: '/', query: {redirect: to.path}}); //跳转到登录页，并且携带重定向页面参数
//       } else {
//         next()
//       }
//     } else {
//       next();//登录页不拦截
//     }
//   }
// );

//过滤器
Vue.filter('dateFormat', function (originVal) {
  const dt = new Date(originVal)

  const y = dt.getFullYear()
  const m = (dt.getMonth() + 1 + '').padStart(2, '0')
  const d = (dt.getDate() + '').padStart(2, '0')

  const hh = (dt.getHours() + '').padStart(2, '0')
  const mm = (dt.getMinutes() + '').padStart(2, '0')
  const ss = (dt.getSeconds() + '').padStart(2, '0')

  return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
})


new Vue({
  router,
  // store,
  render: h => h(App)
}).$mount('#app')
