(function () {
    var login = new Vue({
        el: '#loginForm',
        data: {
            login: {
                stype: 3,
                username: LStorage.get('_pname_') || '',
                password: LStorage.get('_pid_') || ''
            },
            lost:{
                telphone: '',
                newpass: '',
                re_confirmpassword: '',
                code: ''
            },
            message: {
                txt: '',
                cls: ''
            },
            codeurl:'',
            ltype: getParam('ltype') || 1,
            appid: '',
            isrember: !!LStorage.get('_pname_'),
            islogin: false,
            islost: false,
            issend: false,
            timer: 60,
            headimg:'img/avatars/male.png',
            scanMsg: '正在登录，请稍等...'
        },
        computed:{
            telphone: function(){
                return !BASE.valideTel(this.lost.telphone)
            },
            code: function(){
                return !(this.lost.code && (this.lost.code+'').length ==4);
            },
            samePSD: function(){
                return !(this.lost.re_confirmpassword && this.lost.newpass == this.lost.re_confirmpassword);
            },
            validatePSD: function(){
                if(!this.lost.newpass) return false;
                return !(/[0-9 | A-Z | a-z]{6,16}/.test(this.lost.newpass)) 
            },
            passed: function(){
                return this.telphone || this.code || this.samePSD || this.validatePSD;
            }
        },
        methods: {
            inputchange: function(s){
                var cls = 'animated shake';
                var t = '';
                switch(s){
                    case 'tel':
                        t = this.telphone ? { txt: '<span class="error-color">请输入有效的手机号</span>', cls: cls } : {};
                        break;
                    case 'code':
                        t = this.code ? { txt: '<span class="error-color">请输入有效的验证码</span>', cls: cls } : {};
                        break;
                    case 'npsd':
                        t = this.validatePSD ? { txt: '<span class="error-color">请输入有效的新密码</span>', cls: cls } : {};
                        break;
                    case 'rpsd':
                        t = this.samePSD ? { txt: '<span class="error-color">两次密码输入不一致</span>', cls: cls } : {};
                        break;
                }
                this.message = t;
            },
            submit: function () {
                var that = this;
                var cls = 'animated shake';
                var formData = this.login;
                if (!this.login.username) {
                    return this.message = { txt: '<span class="error-color">请输入用户名</span>', cls: cls }, false;
                }
                if (!this.login.password) {
                    return this.message = { txt: '<span class="error-color">请输入有效的密码</span>', cls: cls }, false;
                }
                this.islogin = true;
                BASE.loginIn(formData, function () {
                    that.message = { txt: '<span class="success-color">登录成功</span>', cls: '' };
                }, function () {
                    that.message = { txt: '<span class="error-color">账号或密码错误，请重新输入！</span>', cls: cls };
                }, function () {
                    that.message = { txt: '<span class="error-color">网络出错</span>', cls: cls };
                }, function(){
                    that.islogin = false;
                    that.isrember ? (LStorage.set('_pname_', that.login.username),LStorage.set('_pid_', that.login.password)) : LStorage.clear();
                });
            },
            sendcode: function(){
                var that = this;
                if(this.telphone || this.issend){ return false; }
                this.issend = true;
                $.post('/doctor/gainVeryCode', {
                    telphone: this.lost.telphone
                }).done(function(data){
                    if(data.status=="success"){
                        $.smallBox({
                            title: "找回密码",
                            content: '验证码发送成功',
                            color: $.color.noraml,
                            timeout: 3000
                        });
                        that.timeleave();
                    }else if(data.status=="error"){
                        $.smallBox({
                            title: "找回密码",
                            content: data.messages || '查无此手机号，请确认手机号是否正确。',
                            color: $.color.error,
                            timeout: 3000
                        });
                        that.issend = false;
                    }
                })
                .fail(function(){
                    that.issend = false;
                });
            },
            resetpsd: function(){
                var that = this;
                if(this.passed) return 0;
                this.islogin = true;
                $.post('/doctor/newpassset',this.lost)
                .done(function(data){
                    if (data.status == 'success') {
                        $.smallBox({
                            title: "找回密码",
                            content: '找回密码成功，请登录',
                            color: $.color.noraml,
                            timeout: 3000
                        });
                        that.islost = false;
                    }else if(data.status == 'error'){
                        $.smallBox({
                            title: "找回密码",
                            content: data.messages || '你好，密码重置失败，请稍后重试！',
                            color: $.color.error,
                            timeout: 3000
                        });
                    }else{
                        $.smallBox({
                            title: "找回密码",
                            content: data.messages || '你好，参数异常，请重新填写表单！',
                            color: $.color.error,
                            timeout: 3000
                        });
                    }
                })
                .fail(function(){
                    that.islogin = false;
                });
            },
            timeleave: function(){
                (this.timer -= 1) ? window.setTimeout(this.timeleave,1000) : (this.issend = false);
            }
        },
        watch:{
            issend: function(n,o){
                this.timer = 60;
            },
            islost: function(){
                this.message = {}
            }
        },
        created: function () {
            var that = this;
            $.get('/doctor/indexsaoma')
            .done(function(d){
                if(d.keyid){
                    that.appid = d.keyid;
                    that.codeurl = '/hybrid/showQR?keyid=' + d.keyid;
                }
            });
            loadScript('js/app.goeasy.js', function goeasy() {
                if ('undefined' == typeof GO_SUB) return window.setTimeout(goeasy, 50), 0;
                GO_SUB('zjh', function (message) {
                    console.log('message',message);
                    if(!message) return window.location.reload(),false;
                    if(that.appid != message.content) return console.log('返回不一至'),false;
                    $('.avatar').show().siblings('.qrcode').hide();
                    $.get('/hybrid/sucjump',{keyid: that.appid})
                    .done(function(d){
                    	if(d.headimage){
                    		that.headimg = d.headimage;
                    	}
                    	if(d.status != 'success'){ 
                    		that.scanMsg = '登录失败，用户不存在或者认证未通过';
                    		return $.smallBox({
                                title: "扫码登录",
                                content: that.scanMsg,
                                color: $.color.error,
                                timeout: 6000
                            }),false;
                    	}
                        if(d.redirectUrl){
                    		that.scanMsg = '登录成功，正在跳转';
                            BASE.sstorageFun(d);
                            window.location.replace(d.redirectUrl);
                        }else{
                    		that.scanMsg = '跳转失败，数据异常';
                            $.smallBox({
                                title: "扫码登录",
                                content: that.scanMsg,
                                color: $.color.error,
                                timeout: 3000
                            });
                        }
                    });
                });
            });
        }
    });
})(BASE.clearSession());
