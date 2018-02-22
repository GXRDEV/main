<template>
  <el-menu-item-group v-if="'group' in menu" :title="menu.group.name">
    <kx-menu-item v-for="(item, idx) in menu.group.children" :key="idx" :menu="item" :index="item.path"></kx-menu-item>
  </el-menu-item-group>
  <el-submenu v-else-if="'children' in menu" :index="index+''">
    <template slot="title">
      <i v-if="menu.icon" :class="menu.icon"></i>
      <span slot="title">{{menu.name}}</span>
    </template>
    <kx-menu-item v-for="(item, idx) in menu.children" :key="idx" :menu="item" :index="item.path"></kx-menu-item>
  </el-submenu>
  <el-menu-item v-else :index="index+''">
    <i v-if="menu.icon" :class="menu.icon"></i>
    <span slot="title">{{menu.name}}</span>
  </el-menu-item>
</template>

<script>
  import KxMenuItem from './KxMenuItem'
  export default {
    name: 'KxMenuItem',
    props: {
      menu: Object,
      index: [String, Number]
    },
    components: {
      KxMenuItem
    }
  }
</script>
