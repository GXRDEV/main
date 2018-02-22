<template>
  <el-container class="h100vh">
    <el-aside :width="isCollapse ? '64px' : '180px'">
      <kx-menu :data="menus" :isCollapse="isCollapse"></kx-menu>
    </el-aside>
    <el-container>
      <el-header height="46px">
        <kx-header @toggleMenu="isCollapse = !isCollapse"></kx-header>
      </el-header>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import KxMenu from '@share/KxMenu'
  import KxHeader from '@share/KxHeader'
  export default {
    components: {
      KxMenu, KxHeader
    },
    created () {
      this.$http.get(`/static/data/menus_${this.$store.state.user.role.split('_')[0]}.json`).then(d => { this.menus = d })
    },
    data () {
      return {
        isCollapse: false,
        menus: []
      }
    }
  }
</script>

