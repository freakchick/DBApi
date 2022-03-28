import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        sqls: [{sqlText: 'test', id: 0, transformPlugin: null, transformPluginParams: null}],
        seq: 0, //用来做for循环key
        labelSeq: 0, //用来显示
        cmList: [],
        mode: null
    },
    mutations: {
        clearTransformPluginParam(state,index){
            state.sqls[index].transformPluginParams = null
        },
        addSql(state) {
            state.seq += 1
            state.labelSeq += 1
            state.sqls.push({
                id: state.seq,
                sqlText: '-- 请输入sql，一个标签只能输入一条sql',
                transformPlugin: null,
                transformPluginParams: null,
                label: state.labelSeq
            })
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
            state.cmList = []
            state.labelSeq = 0
            state.sqls = payload.map((item) => {
                state.seq += 1
                state.labelSeq += 1

                return {
                    id: state.seq,
                    sqlText: item.sqlText,
                    transformPlugin: item.transformPlugin,
                    transformPluginParams: item.transformPluginParams,
                    label: state.labelSeq
                }
            })
            // debugger
            console.log('--------------' + state.sqls)
        },
        setMode(state, payload) {
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
