import axios from 'axios'
import qs from 'qs'
import { Message } from 'element-ui'
import store from '../../store'
import router from '../../router'

const baseURL = process.env.TARGET

const logout = function () {
  store.commit('vuex_m_logout')
  router.replace({
    path: '/',
    query: { redirect: router.currentRoute.fullPath }
  })
}

const errCode = function (code, message) {
  switch (code) {
    case '401':
      logout()
      break
    default:
      message && Message({
        message: message,
        type: 'error',
        duration: 5 * 1000
      })
  }
}

// axios 配置
axios.defaults.timeout = 5000
// axios.defaults.baseURL = process.env.TARGET

// http 发送请求 拦截器
axios.interceptors.request.use(
  config => {
    store.commit('vuex_m_loading', 'start')
    if (store.getters.token) {
      config.headers.Authorization = `${store.getters.token}`
    }
    return config
  },
  err => {
    store.commit('vuex_m_loading', 'fail')
    return Promise.reject(err)
  }
)

// http 接收请求 拦截器
axios.interceptors.response.use(
  ({ data = null }) => {
    if (data && data.status == 'success') {
      store.commit('vuex_m_loading', 'done')
      return data.data
    } else {
      store.commit('vuex_m_loading', 'fail')
      return data && errCode(data.code, data.message), Promise.reject(data)
    }
  },
  error => {
    store.commit('vuex_m_loading', 'fail')
    if (error.response) {
      errCode(error.response.status, 'Error: ' + error.response.data.error)
      console.table(error.response.data)
    }
    return Promise.reject(error)
  }
)

axios.$get = function(url, params) {
  return axios.get(baseURL + url, { params })
}

axios.$post = function(url, params) {
  return axios.post(baseURL + url, qs.stringify(params))
}

const plugin = {
  install(Vue) {
    Vue.prototype.$http = axios
    Vue.$http = axios
  },
  $http: axios
}

export default plugin
export const install = plugin.install
