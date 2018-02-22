module.exports = {
    /*
   ** Headers of the page
   */
    head: {
        title: '佰医汇',
        meta: [
            {
                charset: 'utf-8'
            },
            {
                name: 'viewport',
                content: 'width=device-width, initial-scale=1'
            },
            {
                hid: 'keywords',
                name: 'keywords',
                content:
                    '佰医汇，佰医汇在线，在线问诊，好大夫，在线医疗，挂号，挂佰医汇，名医，专家会诊，病例讨论，专家随诊，慢病管理，医疗专家，专家在线交流与讨论，医患交流平台，医患咨询平台，门诊预约系统，就医经验分享'
            },
            {
                hid: 'description',
                name: 'description',
                content:
                    '佰医汇是汇聚国内排名前十医院科室专家提供在线精问诊的移动医疗平台。患者可以通过佰医汇与中国顶级名医直接交流、问诊。佰医汇提供高端、专业、完善的医疗咨询，其中包括：在线问诊专家，专家门诊预约，医院/专家信息查询中心，医患咨询交流平台，专家就医经验分享系统，专家康复计划等服务。'
            }
        ],
        link: [
            {
                rel: 'icon',
                type: 'image/x-icon',
                href: '/favicon.ico'
            }
        ]
    },
    css: [
        '~assets/css/reset.css',
        'element-ui/lib/theme-chalk/index.css',
        '~assets/font/iconfont.css',
        '~assets/css/main.less'
    ],
    /*
   ** 全局加载进度条
   */
    loading: {
        color: '#0086D1'
    },
    modules: ['@nuxtjs/axios'],
    axios: {
        proxyHeaders: false,
        // debug: true,
        baseURL: 'https://www.ebaiyihui.com/',
        redirectError: {
            401: '/login'
        }
    },
    /*
   ** 第三方组件库
   */
    plugins: ['~plugins/element-ui'],
    /*
   ** Build configuration
   */
    build: {
        vendor: ['axios', 'element-ui'],
        /*
     ** Run ESLint on save
     */
        extend (config, ctx) {
            // if (ctx.dev && ctx.isClient) {
            //   config.module.rules.push({
            //     enforce: 'pre',
            //     test: /\.(js|vue)$/,
            //     loader: 'eslint-loader',
            //     exclude: /(node_modules)/
            //   })
            // }
        }
    }
}
