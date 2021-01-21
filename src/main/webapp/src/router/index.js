import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

import donate from '../components/about/donate'
import json from '../components/tool/json'
import time from '../components/tool/time'

import datasource from '../components/datasource/datasource'

Vue.use(VueRouter)

const routes = [
    {
        path: '/', name: 'Home', component: Home,
        children: [
            {path: '/about/donate', name: 'donate', component: donate},
            {path: '/tool/json', name: 'json', component: json},
            {path: '/tool/time', name: 'time', component: time},

            {path: '/datasource', name: 'datasource', component: datasource}
        ]
    }
]

const router = new VueRouter({
    routes
})

export default router
