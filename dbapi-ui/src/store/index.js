import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {

        mode: null
    },
    mutations: {

        setMode(state, payload) {
            state.mode = payload
        }
    },
    actions: {},
    getters: {

    }

})
