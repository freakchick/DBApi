import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        sqls: [{sqlText: 'test', id: 0, transformPlugin: null, transformPluginParams: null}],
        seq: 0,
        cmList: [],
        mode: null
    },
    mutations: {
        addSql(state) {
            state.seq += 1
            state.sqls.push({id: state.seq, sqlText: '', transformPlugin: null, transformPluginParams: null})
        },
        addCm(state, payload) {
            state.cmList.push(payload)
        },
        removeSql(state, index) {
            state.sqls.splice(index, 1)
            state.cmList.splice(index, 1)
        },
        emptyCmList(state, payload) {
            state.cmList = []
        },
        //编辑或新增页面初始化sql
        initSqls(state, payload) {
            debugger
            console.log('execute init sql function')
            // state.seq = 0
            state.cmList = []
            state.sqls = payload.map((item) => {
                state.seq += 1
                return {
                    id: state.seq,
                    sqlText: item.sqlText,
                    transformPlugin: item.transformPlugin,
                    transformPluginParams: item.transformPluginParams
                }
            })
            // debugger
            console.log('--------------'+ state.sqls)
        },
        setMode(state, payload){
            state.mode = payload
        }
    },
    actions: {},
    getters: {
        getSql: state => {

            const p = []
            for (let i = 0; i < state.sqls.length; i++) {
                let obj = {}
                // debugger
                obj.transformPlugin = state.sqls[i].transformPlugin
                obj.transformPluginParams = state.sqls[i].transformPluginParams
                obj.sqlText = state.cmList[i].getValue()
                p.push(obj)
            }
            return p
        },
        currentCm: state => (index) => {
            return state.cmList[index]
        }
    }

})
