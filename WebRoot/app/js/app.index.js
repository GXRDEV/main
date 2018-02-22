(function(utype){
    $('#vue-menu').load('ajax/menu.html');
    $('#user-profile').load('ajax/user-profile.html');
    BASE.refreshNotify();
    //加载goeasy推送
    loadScript('js/app.goeasy.js', function goeasy() {
        if ('undefined' == typeof GO_SUB) return window.setTimeout(goeasy, 50), 0;
        GO_SUB((utype == '2' ? 'expert_' : 'doctor_') + BASE.uid, function (message) {
            var content = message.content.replace(/&quot;/g, "\"");
            var jmessage = JSON.parse(content);
            var _type = jmessage.type,
                _from = jmessage.from,
                _result = jmessage.result || '',
                _progress =  jmessage.progress || '',
                _orderId = jmessage.orderId,
                _sponsor = jmessage.sponsor || '',
                _hosName = jmessage.hosName || '未知医院',
                _depName = jmessage.depName || '未知科室';
            var msg = '';            
            debugState && console.log('全局GoEasy', JSON.stringify(message, null, '\t'));
            //更新通知数量
            BASE.refreshNotify();
            //就绪通知
            if (_type == 'launchNotify') {
                msg = _hosName + _depName + _sponsor + '专家正发起视频，点击进入。'
                sendToPC(msg);
                //退出通知
            } else if (_type == 'onlyOneWeb' && BASE.loginuuid && BASE.loginuuid != jmessage.loginUuid) {
                BASE.loginuuid = '';
                BASE.clearSession();
                BASE.showModel({
                    remote: 'ajax/modal-content/modal-alert.html',
                    cls: 'modal-center'
                });
                //清除通知
            } else if (_type == 'clearNotify') {
                
                //报告通知
            } else if (_type == 'reportNotify') {
                msg = '收到来自' + _hosName + _depName + _sponsor + '专家的就诊报告，请及时查看。'
                sendToPC(msg);
                //医生进入或推出准备就绪状态后的通知
            } else if(_type == 'progressNotify'){
                
            } 
            function sendToPC(msg){
                var oid = getParam('oid');
                var normal = {
                    color: $.color.noraml,
                    icon: "fa fa-bell swing animated",
                    timeout: 16000
                };
                if(oid == _orderId) return 0;
                $.bigBox($.extend({ title : "订单通知", content : msg }, normal));
                // window.setTimeout(function(){
                //     notification({
                //         title: '订单通知',
                //         txt: msg
                //     });
                // },5000);
            }
            function notification(opt){
                if (window.Notification) {
                    var popNotice = function() {
                        if (Notification.permission == "granted") {
                            var notification = new Notification(opt.title, {
                                body: opt.txt,
                                icon: BASE.href + 'img/defdoc.jpg',
                                tag: 'notification',
                                renotify: false, //新通知出现的时候是否替换之前的。如果设为true，则表示替换
                                requireInteraction:true, //等待用户与之互动
                                silent: true, //通知出现的时候，是否要有声音
                                sound: BASE.href + 'audio/global.wav',
                                sticky: true //是否通知具有粘性
                            });
                            notification.onclick = notification.close;
                        }    
                    };
                    if (Notification.permission == "granted") {
                        popNotice();
                    } else if (Notification.permission != "denied") {
                        Notification.requestPermission(function (permission) {
                            popNotice();
                        });
                    }
                } else {
                    console.log('浏览器不支持Notification');    
                }
            }
        });
    });
})(SStorage.get('_token_utype'));