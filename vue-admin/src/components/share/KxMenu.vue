<template>
  <section class="kx-menu">
    <div class="logo" :class="isCollapse ? 'logo-l' : 'logo-b'"></div>
    <el-menu :default-active="selected" :collapse="isCollapse" :unique-opened="true" :router="true" background-color="#2a313b" text-color="#fff">
      <kx-menu-item v-for="(item, idx) in data" :key="idx" :menu="item" :index="item.path"></kx-menu-item>
    </el-menu>
  </section>
</template>

<script>
  import KxMenuItem from './KxMenuItem'
  export default {
    name: 'KxMenu',
    props: {
      isCollapse: Boolean,
      data: Array
    },
    components: {
      KxMenuItem
    },
    computed: {
      selected () {
        return this.$route.fullPath
      }
    }
  }
  /** 菜单数据示例，可以动态加载 */
  // const _menus = [
  //   {
  //     path: '/',
  //     icon: 'el-icon-location',
  //     name: '首页'
  //   },
  //   {
  //     name: '导航二',
  //     icon: 'el-icon-menu',
  //     children: [
  //       {
  //         path: '/list',
  //         name: '列表'
  //       },
  //       {
  //         path: '/',
  //         name: '选项2',
  //         children: [
  //           {
  //             path: '',
  //             name: '选项2-1'
  //           }
  //         ]
  //       }
  //     ]
  //   },
  //   {
  //     group: {
  //       name: '分组一',
  //       children: [
  //         {
  //           path: '/',
  //           icon: 'el-icon-menu',
  //           name: '选项3-1'
  //         },
  //         {
  //           path: '/',
  //           icon: 'el-icon-menu',
  //           name: '选项3-2'
  //         }
  //       ]
  //     }
  //   },
  //   {
  //     group: {
  //       name: '分组二',
  //       children: [
  //         {
  //           path: '',
  //           icon: 'el-icon-menu',
  //           name: '选项3-2-1',
  //           children: [
  //             {
  //               path: '/',
  //               name: '选项3-2-1-1'
  //             },
  //             {
  //               group: {
  //                 name: '分组三',
  //                 children: [
  //                   {
  //                     path: '/',
  //                     name: '选项3-2-1-2'
  //                   }
  //                 ]
  //               }
  //             }
  //           ]
  //         }
  //       ]
  //     }
  //   }
  // ]
</script>

<style lang="less">
  @import '../../assets/css/var';
  .kx-menu {
    .logo {
      height: 46px;
      background: @main-color url() no-repeat center center;
      background-size: auto 80%;
    }
    .logo-b {
      background-image: url(../../assets/img/logo.png);
    }
    .logo-l {
      background-image: url(../../assets/img/logo-l.png);
    }
    .el-menu {
      border-right: 0;
      .el-submenu .el-menu-item {
        min-width: auto;
      }
    }
  }
</style>
