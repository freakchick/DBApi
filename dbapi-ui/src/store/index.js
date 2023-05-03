import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        // sqls: [{ sqlText: 'test', id: 0, transformPlugin: null, transformPluginParams: null }],
        // seq: 0, // 用来做for循环key
        // labelSeq: 0, // 用来显示
        cmList: [],
        currentActiveCmIndex: 0,
        mode: null
    },
    mutations: {
        // clearTransformPluginParam(state, index) {
        //     state.sqls[index].transformPluginParams = null
        // },
        // addSql(state) {
        //     state.seq += 1
        //     state.labelSeq += 1
        //     state.sqls.push({
        //         id: state.seq,
        //         sqlText: '-- only one sql in one tab',
        //         transformPlugin: null,
        //         transformPluginParams: null,
        //         label: state.labelSeq
        //     })
        // },
        setCurrentActiveCmIndex(state, payload) {
            state.currentActiveCmIndex = (payload)
        },
        addCm(state, payload) {
            state.cmList.push(payload)
        },
        removeCm(state,payload){
            state.cmList.splice(payload,1)
        },

        // 编辑或新增页面初始化sql
        // initSqls(state, payload) {
        //     state.cmList = []
        //     state.labelSeq = 0
        //     state.sqls = payload.map((item) => {
        //         state.seq += 1
        //         state.labelSeq += 1
        //
        //         return {
        //             id: state.seq,
        //             sqlText: item.sqlText,
        //             transformPlugin: item.transformPlugin,
        //             transformPluginParams: item.transformPluginParams,
        //             label: state.labelSeq
        //         }
        //     })
        // },
        setMode(state, payload) {
            state.mode = payload
        }
    },
    actions: {},
    getters: {
        getSql: state => {
            const p = []
            for (let i = 0; i < state.cmList.length; i++) {
                let sqlText = state.cmList[i].getValue()
                p.push(sqlText)
            }
            return p
        },
        currentCm: state => {
            return state.cmList[state.currentActiveCmIndex]
        }
    }

})
