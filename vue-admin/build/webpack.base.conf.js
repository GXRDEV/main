'use strict'
const fs = require('fs')
const path = require('path')
const utils = require('./utils')
const config = require('../config')
const vueLoaderConfig = require('./vue-loader.conf')

function resolve (dir) {
  return path.join(__dirname, '..', dir)
}

const createLintingRule = () => ({
  test: /\.(js|vue)$/,
  loader: 'eslint-loader',
  enforce: 'pre',
  include: [resolve('src'), resolve('test')],
  options: {
    formatter: require('eslint-friendly-formatter'),
    emitWarning: !config.dev.showEslintErrorsInOverlay
  }
})

const webpackConfig = {
  context: path.resolve(__dirname, '../'),
  entry: {
    app: './src/main.js'
  },
  output: {
    path: config.build.assetsRoot,
    filename: '[name].js',
    publicPath: process.env.NODE_ENV === 'production'
      ? config.build.assetsPublicPath
      : config.dev.assetsPublicPath
  },
  resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      'vue$': 'vue/dist/vue.esm.js',
      '@': resolve('src'),
      '@css': resolve('src/assets/css'),
      '@img': resolve('src/assets/img'),
      '@page': resolve('src/components/page'),
      '@share': resolve('src/components/share'),
    }
  },
  module: {
    rules: [
      ...(config.dev.useEslint ? [createLintingRule()] : []),
      {
        test: /\.vue$/,
        loader: 'vue-loader',
        options: vueLoaderConfig
      },
      {
        test: /\.js$/,
        loader: 'babel-loader',
        include: [resolve('src'), resolve('test')]
      },
      {
        test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: utils.assetsPath('img/[name].[hash:7].[ext]')
        }
      },
      {
        test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: utils.assetsPath('media/[name].[hash:7].[ext]')
        }
      },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: utils.assetsPath('fonts/[name].[hash:7].[ext]')
        }
      }
    ]
  },
  node: {
    // prevent webpack from injecting useless setImmediate polyfill because Vue
    // source contains it (although only uses it if it's native).
    setImmediate: false,
    // prevent webpack from injecting mocks to Node native modules
    // that does not make sense for the client
    dgram: 'empty',
    fs: 'empty',
    net: 'empty',
    tls: 'empty',
    child_process: 'empty'
  }
}

const vuxLoader = require('vux-loader')
const routePath = path.resolve(__dirname, '../src/router/router.json')

module.exports = vuxLoader.merge(webpackConfig, {
  options: {},
  plugins: [{
    name: 'js-parser',
    fn: function (source) {
      if (source.indexOf('adminRoutes') != -1) {
        this.addDependency(routePath)
        let list = fs.readFileSync(routePath, 'utf-8')
        list = JSON.parse(list)

        let routes = []
        list.forEach(function(route) {
          routes.push(JSON.stringify(makeRoute(route)))
        }, this);
        routes = `[${routes.join(',\n')}]`
        routes = routes.replace(/"<fun>/g, '')
        routes = routes.replace(/<\/fun>"/g, '')
        source = source.replace('const adminRoutes = []', 'const adminRoutes = ' + routes)
      }
      return source
    }
  }]
})

function makeRoute (route) {
  let _route = JSON.parse(JSON.stringify(route))
  let component = _route.component
  if(_route.children){
    _route.children.forEach((item, idx) => {
      _route.children[idx] = makeRoute(item)
    })
  }
  if(component){
    _route.component = `<fun>function (resolve) { require.ensure([], function () { resolve(require('${component}')) }, '${component.split('/').pop()}') }</fun>`
  }
  return _route
}
