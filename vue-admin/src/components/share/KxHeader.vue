<template>
  <div class="flex acenter h100 kx-header">
    <div class="flex_0">
      <el-button @click="$emit('toggleMenu')" icon="el-icon-menu" size="mini"></el-button>
    </div>
    <div class="flex_1 seconddiv">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>{{$route.meta.remark}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="flex_1 tright">
      <el-dropdown @command="handleHeader">
        <span class="el-dropdown-link">
          <i class="iconfont icon-touxiang1"></i>
          欢迎，{{$store.getters.username}}
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item disabled command="info">
            <i class="iconfont icon-username big16"></i> 个人资料</el-dropdown-item>
          <el-dropdown-item disabled command="modify">
            <i class="iconfont icon-shezhi big16"></i> 修改密码</el-dropdown-item>
          <el-dropdown-item command="exit">
            <i class="iconfont icon-tuichu big16"></i> 退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!-- 个人信息 -->
    <el-dialog title="个人信息" :visible.sync="dialogInfo" width="30%">
      <span>这是一段信息</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogInfo = false">取 消</el-button>
        <el-button type="primary" @click="dialogInfo = false">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 修改密码 -->
    <el-dialog title="修改密码" :visible="dialogModify">
      <el-form :model="modifyForm">
        <el-form-item label="原始密码">
          <el-input type="password" v-model="modifyForm.psd_old" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input type="password" v-model="modifyForm.psd_new" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码">
          <el-input type="password" v-model="modifyForm.psd_renew" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogModify = false">取 消</el-button>
        <el-button type="primary" @click="modifySubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        dialogModify: false,
        dialogInfo: false,
        modifyForm: {
          psd_old: '',
          psd_new: '',
          psd_renew: ''
        }
      }
    },
    computed: {
      selected() {
        return this.$route.fullPath
      }
    },
    methods: {
      handleHeader(command) {
        switch (command) {
          case 'info':
            this.dialogInfo = true
            break
          case 'modify':
            this.dialogModify = true
            break
          case 'exit':
            this.$router.replace('/')
            break
        }
      },
      modifySubmit() {
        this.dialogModify = false
        this.$message({
          message: '恭喜你，密码修改成功。',
          type: 'success'
        })
      }
    }
  }
</script>


<style lang="less">
  .kx-header {
    .seconddiv {
      margin: 0 1em;
    }
    .el-button--mini {
      padding: 7px;
      .el-icon-menu {
        font-size: 1.4em;
      }
    }
    .icon-touxiang1 {
      font-size: 1.4em;
      margin-right: 0.3em;
    }
  }
  .el-dropdown {
    cursor: pointer;
  }
</style>
