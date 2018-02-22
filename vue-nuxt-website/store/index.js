import Vue from 'vue'
import Vuex from 'vuex'
import remote from './modules/remote'

Vue.use(Vuex)

const store = () =>
    new Vuex.Store({
        state: {
            token: ''
        },
        modules: {
            remote
        }
    })

export default store
