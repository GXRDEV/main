<script>
    export default {
        methods: {
            alert () {
                const h = this.$createElement;
                this.$notify({
                    title: '标题名称',
                    message: h('p', { style: 'color: red' }, '这是提示文案这是提示文案这是提示文案这是提示文案这是提示文案这是提示文案这是提示文案这是提示文案')
                });
            }
        }
    }
</script>

<template>
    <section class="e-auth">
        <el-row class="e-auth-inner w1366">
            <el-col :span="12">
                <nuxt-link to="/">首页</nuxt-link>
                <nuxt-link target="_blank" to="/app">手机应用
                    <i class="iconfont" style="vertical-align: 0;">&#xe60d;</i>
                </nuxt-link>
                <a href="javascript:void(0)" data-js="addfavorite">加入收藏</a>
            </el-col>
            <el-col :span="12" class="tright">
                <span class="usercenter">
                    <nuxt-link to="/user/login">登录</nuxt-link>
                    <nuxt-link to="/user/regist">注册</nuxt-link>
                </span>
                <nuxt-link to="/user/myask">我的提问
                    <i class="iconfont">&#xe60b;</i>
                </nuxt-link>
            </el-col>
        </el-row>
    </section>
</template>

<style lang="less">
    .e-auth {
        background: #eee;
        background: -webkit-linear-gradient(#fbfbfb, #efefef);
        background: -o-linear-gradient(#fbfbfb, #efefef);
        background: -moz-linear-gradient(#fbfbfb, #efefef);
        background: linear-gradient(#fbfbfb, #efefef);
        border-bottom: 1px solid #e5e5e5;
        .e-auth-inner {
            line-height: 30px;
            a {
                display: inline-block;
                border-right: 1px solid #ccc;
                padding: 0 20px;
                line-height: 1em;
                margin-top: 9px;
                color: #666;
                font-size: 12px;
                i {
                    margin-left: 5px;
                    vertical-align: -2px;
                }
            }
            .usercenter i {
                font-style: normal;
                font-size: 12px;
                line-height: 2.5em;
            }
        }
    }
</style>
