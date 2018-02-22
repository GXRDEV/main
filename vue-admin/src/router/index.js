import Vue from 'vue'
import Router from 'vue-router'
import ElementUI from 'element-ui'

import store from '../store'

import '@css/reset.css'
import 'element-ui/lib/theme-chalk/index.css'
import 'element-ui/lib/theme-chalk/display.css'
import '@/assets/font/iconfont.css'
import '@css/public.less'

import http from '@/plugins/http'
import url from '@/plugins/url'

Vue.use(http)
Vue.use(url)
Vue.use(Router)
Vue.use(ElementUI, { size: 'small' })

const adminRoutes = []

const router = new Router({
  routes: adminRoutes
})

router.beforeEach((to, from, next) => {
  store.commit('vuex_m_loading', 'start')
  if (/\:\/\//.test(to.path)) {
      let urlPath = to.fullPath.split('://')[1]
      window.location.href = '//' + urlPath
  } else if (to.matched.some(record => record.meta.skipAuth) || store.state.user.token) {
    next()
  } else {
    next('/')
  }
})

router.afterEach(route => {
  store.commit('vuex_m_loading', 'done')
})

export default router
