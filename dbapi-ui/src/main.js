import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
import axios from 'axios'
import VueAxios from 'vue-axios'
import qs from 'qs'
import i18n from './i18n/i18n'

import './theme/index.css'
import './icon/iconfont.css'

// import VueCodeMirror from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'

import install from '@/components/common/index.js'
import VueClipboard from 'vue-clipboard2'


import moment from 'moment';

import { CONTENT_TYPE } from "@/constant";

import 'echarts';
import ECharts from 'vue-echarts';
Vue.component('v-chart', ECharts);

Vue.use(VueClipboard)
// Vue.use(VueCodeMirror)
Vue.use(install) // 导入模块
moment.locale('zh-cn'); // 设置语言 或 moment.lang('zh-cn');
Vue.prototype.$moment = moment;// 赋值使用

Vue.config.productionTip = false

// 使用vue-axios，这样才可以全局使用this.axios调用
Vue.use(VueAxios, axios);

// axios.defaults.baseURL = '/api'

axios.defaults.headers = { 'Content-Type': CONTENT_TYPE.FORM_URLENCODED }
// 全局拦截post请求的参数，用qs序列化
axios.interceptors.request.use(config => {
    // form表单提交multipart/form-data的时候，不需要序列化参数
    if (config.method === 'post' && config.headers['Content-Type'] === CONTENT_TYPE.FORM_URLENCODED) {
        config.data = qs.stringify(config.data, { indices: false });
    }
    // 拦截所有请求，添加登陆用户的token
    if (localStorage.getItem('token')) {
        const x = config.headers.Authorization
        if (x === null || x === undefined) {
            config.headers.Authorization = localStorage.getItem('token');
        }
    }
    return config
})


// 后台用户登陆信息校验不成功就跳转到登录页面
axios.interceptors.response.use(response => {
    return response
}, error => {
    if (error.response.status == '401') {
        // 不是api请求测试的请求，就跳转登录页
        if (!error.response.config.url.startsWith("http://")) {
            router.push("/login");
        } else {
            return Promise.reject(error)
        }
    } else {
        return Promise.reject(error)
    }
})

// //过滤器
Vue.filter('dateFormat', function (originVal) {
    const dt = new Date(originVal*1000)
    return moment(dt).format('YYYY-MM-DD HH:mm:ss')
})


new Vue({
    router,
    i18n,
    store,
    render: h => h(App)
}).$mount('#app')
