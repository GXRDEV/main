<template>
  <section id="app">
    <vue-topprogress ref="topProgress"></vue-topprogress>
    <router-view/>
  </section>
</template>

<script>
  import { vueTopprogress } from 'vue-top-progress'
  export default {
    name: 'app',
    components: {
      vueTopprogress
    },
    computed: {
      loading() {
        return this.$store.state.loading
      }
    },
    watch: {
      loading(key) {
        this.$refs.topProgress[key]()
      }
    }
  }
</script>

<style lang="less">
  html,
  body {
    background: #fff;
  }
  ::-webkit-scrollbar-track {
    background: transparent;
  }
  ::-webkit-scrollbar-thumb {
    background: #d9d9d9;
    cursor: pointer;
  }
  ::-webkit-scrollbar {
    width: 1px;
    height: 6px;
  }
  #app {
    .el-aside {
      background: #2a313b;
      color: #fff;
      overflow: visible;
    }
    .el-header {
      border-bottom: 1px solid #ccc;
    }
  }
</style>
