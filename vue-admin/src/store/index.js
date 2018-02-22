import Vuex from 'vuex'
import Vue from 'vue'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        user: {
          token: sessionStorage.getItem('_kx_admin_token_') || '',
          role: sessionStorage.getItem('_kx_admin_role_') || ''
        },
        status: {
          'drugs_1_1': '待分配',
          'drugs_1_2': '已分配',
          'drugs_0_10': '待发货',
          'drugs_0_20': '配送中',
          'drugs_0_30': '已拒绝',
          'drugs_0_40': '已完成',
          'drugs_0_50': '已取消'
        },
        loading: ''
    },
    mutations: {
      vuex_m_login(state, { role, token }) {
        state.user.token = token
        state.user.role = role
        sessionStorage.setItem('_kx_admin_token_', token)
        sessionStorage.setItem('_kx_admin_role_', role)
      },
      vuex_m_logout(state) {
        state.user.token = ''
        state.user.role = ''
        sessionStorage.removeItem('_kx_admin_token_')
        sessionStorage.removeItem('_kx_admin_role_')
      },
      vuex_m_loading(state, key) {
        state.loading = key || 'done'
      }
    },
    getters: {
      username(state) {
        return state.user.token.split('|')[1] || '匿名用户'
      },
      token(state) {
        return state.user.token.split('|')[0]
      },
      table_tags: (state) => {
        let arr = Object.keys(state.status).filter(i => {
          return i.indexOf(state.user.role) != -1
        }).map(i => {
          return {
            name: state.status[i],
            value: i.split('_').pop()
          }
        })
        arr.unshift({
          name: '全部',
          value: ''
        })
        return arr
      },
      getter_table_tag: (state) => (key) => {
        return state.status[(key + '').indexOf(state.user.role) != -1 ? key : ('drugs_0_' + key)]
      }
    }
})
