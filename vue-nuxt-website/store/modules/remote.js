
export default {
    namespaced: true,
    state: {
        coohospitals: null,
        discodes: {
            '11': '北京市',
            '12': '天津市',
            '13': '河北省',
            '14': '山西省',
            '15': '内蒙古自治区',
            '21': '辽宁省',
            '22': '吉林省',
            '23': '黑龙江省',
            '31': '上海市',
            '32': '江苏省',
            '33': '浙江省',
            '34': '安徽省',
            '35': '福建省',
            '36': '江西省',
            '37': '山东省',
            '41': '河南省',
            '42': '湖北省',
            '43': '湖南省',
            '44': '广东省',
            '45': '广西壮族自治区',
            '46': '海南省',
            '50': '重庆市',
            '51': '四川省',
            '52': '贵州省',
            '53': '云南省',
            '54': '西藏自治区',
            '61': '陕西省',
            '62': '甘肃省',
            '63': '青海省',
            '64': '宁夏回族自治区',
            '65': '新疆维吾尔自治区'
        }
    },
    getters: {
        get_coocitys: state => {
            return Object.keys(state.coohospitals || {}).map(item => {
                return {
                    key: item,
                    name: state.discodes[item]
                }
            })
        },
        get_coohoss: state => code => {
            return code ? state.coohospitals[code] : []
        },
        get_coohos_item: state => id => {
            return id ? Object.values(state.coohospitals).reduce((prev, value) => prev.concat(value), []).find(item => item.id - id === 0) : {}
        }
    },
    mutations: {
        set_coohospitals (state, { hospitals }) {
            let _coohos = {}
            hospitals.forEach(item => {
                let key = (item.distCode + '').slice(0, 2)
                key in _coohos
                    ? _coohos[key].push(item)
                    : (_coohos[key] = [item])
            })
            state.coohospitals = _coohos
        },
        set_coodeparts (state, { code, id, departs }) {
            state.coohospitals[code] && state.coohospitals[code].forEach(item => {
                if (item.id - id === 0) {
                    item.departs = departs
                }
            })
        }
    },
    actions: {
        async get_coohospitals ({ state, getters, commit }) {
            if (!state.coohospitals) {
                console.log('get_coohospitals', new Date(), '执行了一次')
                commit(
                    'set_coohospitals',
                    await this.$axios.$get('kangxin/gaincoohospitals')
                )
            }
            return getters.get_coocitys
        },
        async get_coodeparts ({ state, getters, commit }, id) {
            let item = getters.get_coohos_item(id) || {}
            let departs = item.departs
            if (!departs) {
                departs = await this.$axios.$get('kangxin/gainhosdeparts?hosid=' + id).then(d => d.departs)
                commit('set_coodeparts', { code: String(item.distCode).slice(0, 2), id, departs })
            }
            return { departs }
        }
    }
}
