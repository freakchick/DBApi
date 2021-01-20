import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    kafkaAuth: {},
    zkAuth: {},
    redisAuth: {}
  },
  mutations: {
    setKafkaAuth(state, payload) {
      state.kafkaAuth = payload
    },
    setZKAuth(state, payload) {
      state.zkAuth = payload
    },
    setRedisAuth(state, payload) {
      state.redisAuth = payload
    }
  },
  actions: {},
  getters: {
    getKafkaAuth: (state) => (sourceId) => {
      let a = state.kafkaAuth[sourceId]
      console.log("getters...", a)
      return a
    },
    getZKAuth: (state) => (sourceId) => {
      let a = state.zkAuth[sourceId]
      return a
    },
    getRedisAuth: (state) => (sourceId) => {
      return state.redisAuth[sourceId]
    }
  }

})
