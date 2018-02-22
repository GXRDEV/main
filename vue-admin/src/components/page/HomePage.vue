<template>
  <div class="firstpage h100vh w100vw ohidden">
    <el-form :model="loginForm" :rules="rules" ref="loginForm" class="loginForm">
      <h3 class="absolute top left w100 tcenter gtitle">医药管理系统</h3>
      <el-form-item prop="loginName">
        <el-input size="medium" placeholder="请输入用户名" prefix-icon="iconfont icon-username" v-model="loginForm.loginName"></el-input>
      </el-form-item>
      <el-form-item prop="loginPass">
        <el-input size="medium" placeholder="请输入密码" prefix-icon="iconfont icon-mima" type="password" v-model="loginForm.loginPass" @keyup.enter.native="submitForm"></el-input>
      </el-form-item>
      <el-form-item class="tcenter">
        <el-button size="medium" type="primary" @click="submitForm">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        loginForm: {
          loginName: '',
          loginPass: ''
        },
        rules: {
          loginName: [{ required: true, message: '请输入有效的用户名', trigger: 'blur' }],
          loginPass: [{ required: true, message: '请输入有效的密码', trigger: 'blur' }]
        }
      }
    },
    beforeCreate() {
      this.$store.commit('vuex_m_logout')
    },
    methods: {
      submitForm() {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.$http.$post(this.$urls.DRUG_LOGIN_POSTLOGIN, this.loginForm).then(data => {
              this.$message({
                message: '登录成功',
                type: 'success',
                duration: 3 * 1000
              })
              this.$store.commit('vuex_m_login', {
                token: data.token + '|' + data.shopName,
                role: 'drugs_' + data.isSupper
              })
              this.$router.replace(this.$route.query.redirect || '/drugs')
            })
          } else {
            return false
          }
        })
      }
    }
  }
</script>

<style lang="less">
  .firstpage {
    background: url(../../assets/img/bg.jpg) no-repeat center;
    background-size: cover;
    .gtitle {
      margin-top: -2em;
      font-size: 2em;
      letter-spacing: 2px;
    }
    .loginForm {
      width: 320px;
      height: 240px;
      background: #fff;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -60%);
      border: 1px solid #ccc;
      border-radius: 6px;
      box-shadow: 0 0 15px 2px #ccc;
      padding: 2em;
    }
    .el-form-item {
      margin-bottom: 25px;
    }
    .el-input__prefix {
      font-size: 1.2em;
      width: 26px;
    }
    .el-input__inner {
      height: 40px;
      font-size: 1.2em;
    }
    .el-button {
      width: 180px;
      font-size: 1.2em;
    }
  }

  @media (max-width: 414px) {
    .firstpage {
      .loginForm {
        width: 90%;
      }
    }
  }
</style>
