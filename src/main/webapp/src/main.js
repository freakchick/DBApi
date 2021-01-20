import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import axios from 'axios'
import VueAxios from 'vue-axios'
import qs from 'qs'

import VueClipboard from 'vue-clipboard2'
import store from './store'

Vue.config.productionTip = false

//使用vue-axios，这样才可以全局使用this.axios调用
Vue.use(VueAxios, axios);
Vue.use(VueClipboard)

// axios.defaults.baseURL = '/api'

axios.defaults.headers = {'Content-Type': 'application/x-www-form-urlencoded'}
//全局拦截post请求的参数，用qs序列化
axios.interceptors.request.use(config => {
    //form表单提交multipart/form-data的时候，不需要序列化参数
    if (config.method === 'post' && config.headers['Content-Type'] === 'application/x-www-form-urlencoded') {
        config.data = qs.stringify(config.data, {indices: false});
    }
    return config
})

// axios.interceptors.response.use(response => {
//     // console.log(response.data.success)
//     // if (!response.data.success) {
//     //     this.$message.error(response.data.message)
//     // }
// }, error => {
//     // console.log(error)
// })

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
