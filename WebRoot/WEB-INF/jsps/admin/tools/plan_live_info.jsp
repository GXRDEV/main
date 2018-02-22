<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>视频详情</title>
        <link rel="stylesheet" href="/css/matrix-style2.css" />
        <link rel="stylesheet" href="/css/bootstrap.min.css" />
        <link rel="stylesheet" href="/font-awesome/css/font-awesome.css" />
        <link rel="stylesheet" href="/fonticon/base/iconfont.css"></link>
        <link rel="stylesheet" href="/css/flex.css"></link>
        <link rel="stylesheet" href="/sea-modules/imgviewer/viewer.css">
        <style>
            #vedio-detail .err{ color: red}
            #vedio-detail .section .header a{ color: #5f5f5f; text-decoration: none; line-height: 30px; background: #dcdcdc; padding-left: 1em; margin-top: 20px}
            #vedio-detail .section .header a i{ font-size: .8em; margin-right: .5em; line-height: 30px}
            #vedio-detail .section .bodyer{ border: 1px solid #dcdcdc; box-sizing: border-box; background: #fff; min-height: 0!important}
            #vedio-detail .form-horizontal{ padding-top: 5px; margin-left: 0 !important;}
            #vedio-detail .form-horizontal:last-child{ margin-bottom: 1em !important}
            #vedio-detail .form-horizontal .controls { padding-top: 5px; padding-right: 20px; margin-left: 10px}
            #vedio-detail .form-horizontal .control-label{ width: 140px !important;}
            #vedio-detail .msg-head{ border-radius: 50%; width: 30px; height: 30px}
            #vedio-detail .msg-body,.msg-right{ border: 1px solid #00CC99; border-radius: .5em; padding: .5em; margin: 0 1.5em; position: relative}
            #vedio-detail .msg-body:after{ content: ""; position: absolute; left: -11px; top: 8px; width: 0px; height: 0px; border-top: 4px solid transparent; border-right: 10px solid #00CC99; border-bottom: 4px solid transparent;}
            #vedio-detail .msg-body:before{ content: ""; position: absolute; left: -9px; top: 8px; width: 0px; height: 0px; z-index: 1; border-top: 4px solid transparent; border-right: 10px solid #fff; border-bottom: 4px solid transparent;}
            #vedio-detail .msg-right:after{ content: ""; position: absolute; right: -11px; top: 8px; width: 0px; height: 0px; border-top: 4px solid transparent; border-left: 10px solid #00CC99; border-bottom: 4px solid transparent;}
            #vedio-detail .msg-right:before{ content: ""; position: absolute; right: -9px; top: 8px; width: 0px; height: 0px; z-index: 1; border-top: 4px solid transparent; border-left: 10px solid #fff; border-bottom: 4px solid transparent;}
            .ohidden{ overflow: hidden; text-overflow:ellipsis; white-space: nowrap;}
            .viewer-toolbar li{ line-height: 0}
            .noresult{
                text-align: center;
                padding: 2em 0;
            }
        </style>
    </head>
    <body>
        <div id="vedio-detail" style="padding: 0 1em 3em">
            <div style="float: right; padding: 1em 1em 0;" v-if="playing=='未开始'">
                <button type="button" class="btn btn-content" style="background:red;border:0; color: #fff; margin-right: .5em" @click="delOrder">删除订单</button>
            </div>		
            <div class="container-fluid" style="clear: both">
                <div class="row-fluid">
                    <div class="section">
                        <!--视频信息-->
                        <div class="pay">
                            <div class="header"> 
                                <a data-toggle="collapse" href="#collapsePay" class="flex">
                                    <i class="iconfont icon-xialajiantou"></i>视频信息 ({{ playing }})
                                </a>
                            </div>
                            <div id="collapsePay" class="panel-collapse collapse in bodyer">
                                <div class="control-group form-horizontal flex">
                                    <label class="control-label ">标题：</label>
                                    <div class="controls f10">${order.title}</div>
                                </div>
                                <div class="control-group form-horizontal flex">
                                    <label class="control-label">视频开始播放时间：</label>
                                    <div class="controls f10">${order.beginTime}</div>
                                </div>
                                <div class="control-group form-horizontal flex">
                                    <label class="control-label">聊天室开始时间：</label>
                                    <div class="controls f10">${order.chatRoomStartTime}</div>
                                </div>
                                <div class="control-group form-horizontal flex">
                                    <label class="control-label">聊天室关闭时间：</label>
                                    <div class="controls f10">${order.chatRoomCloseTime}</div>
                                </div>
                                <!-- <div class="control-group form-horizontal flex">
                                    <label class="control-label ohidden">视频地址：</label>
                                    <div class="controls f10"></div>
                                </div> -->
                            </div>
                        </div>
                        <!--聊天-->
                        <div class="bottom-content" style="overflow: hidden">
                            <div class="chat">
                                <div class="header clearfix"> 
                                    <a data-toggle="collapse" href="#collapseChat" class="flex">
                                        <i class="iconfont icon-xialajiantou"></i>聊天消息 ({{ opening }})
                                    </a>
                                </div>
                                <div id="collapseChat" class="panel-collapse collapse in bodyer">
                                    <div v-for="(msg, ind) in messages" :key="ind" style="padding: 1rem">
                                        <p style="text-align: center">{{msg.msgTime}}</p>
                                        <div class="flex">
                                            <div style="position: relative">
                                                <img src="https://develop.ebaiyihui.com:443/img/defdoc.jpg" alt="" class="msg-head" style="float: left">
                                                <p style="position: absolute; top: 30px; width: 80px; left: -5px">{{msg.content.extra.split(',')[2]}}</p>
                                            </div>
                                            <div class="msg-body">
                                                <span class="arrow"></span>&ensp;
                                                <span v-if="msg.objectName == 'RC:VcMsg'">
                                                    <button class="voiceInfo" @click="showMsg(msg.content.content)">语音播放</button>
                                                </span>
                                                <span v-else-if="msg.objectName == 'RC:ImgMsg'" class="hasimgview">
                                                    <img :src="msg.content.imageUri" style="width: 100px;"/>
                                                </span>
                                                <span v-else-if="msg.objectName == 'RC:TxtMsg'">
                                                    {{RongIMEmoji.symbolToEmoji(msg.content.content)}}
                                                </span>
                                                <span v-else>{{msg.content.content }}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="noresult" v-if="!messages.length"><img src="/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>
                                </div>
                            </div>
                        </div>
                    </div>		
                </div>
            </div>
        </div>
        <script src="/app/js/libs/vue.min.js"></script>
        <script src="/js/jquery.min.js"></script>
        <script src="/js/bootstrap.min.js"></script>
        <script src="/sea-modules/rong/RongIMLib-2.2.8.min.js"></script>
        <script src="/sea-modules/rong/Libamr-2.2.5.min.js"></script>
        <script src="/sea-modules/rong/RongIMVoice-2.2.5.js"></script>
        <script src="/sea-modules/rong/RongEmoji-2.2.6.min.js"></script>
        <script src="/sea-modules/imgviewer/viewer.js"></script>
        <script>
            var vm = new Vue({
                el: '#vedio-detail',
                data: {
                    messages: [],
                    beginTime: '${order.beginTime}',
                    chatRoomStartTime: '${order.chatRoomStartTime}',
                    chatRoomCloseTime: '${order.chatRoomCloseTime}',
                    status: ['未开始', '进行中', '已结束'],
                    RongIMVoice: '',
                    RongIMEmoji: ''
                },
                computed: {
                    playing: function() {
                        console.log(new Date())
                        var bt = new Date(this.beginTime);
                        if (bt > new Date()) {
                            return this.status[0]
                        } else if (bt <= new Date() && bt <= new Date(this.chatRoomCloseTime)) {
                            return this.status[1]
                        } else {
                            return this.status[2]
                        }
                         
                    },
                    opening: function() {
                        if (new Date() < new Date(this.chatRoomStartTime)) {
                            return this.status[0]
                        } else if (new Date() > new Date(this.chatRoomCloseTime)) {
                            return this.status[2]
                        } else {
                            return this.status[1]
                        }
                    }
                },
                watch: {
                    opening: function (val) {
                        val == '进行中' && this.joinRoom()
                    }
                },
                mounted: function () {
                    var that = this;
                    var config = {
                        size: 24, // 大小, 默认 24, 建议15 - 55
                        url: '//f2e.cn.ronghub.com/sdk/emoji-48.png', // 所有 emoji 的背景图片
                        lang: 'zh', // 选择的语言, 默认 zh
                        // 扩展表情
                        extension: {
                            dataSource: {
                                "u1F914":{  
                                    "en":"thinking face", // 英文名称
                                    "zh":"思考", // 中文名称
                                    "tag":"🤔", // 原生emoji
                                    "position":"0px 0px" // 所在背景图位置坐标
                                }
                            },
                            // 新增 emoji 的背景图 url
                            url: 'https://emojipedia-us.s3.amazonaws.com/thumbs/160/apple/96/thinking-face_1f914.png'
                        }
                    };
                    // RongIMLib.RongIMClient.init("kj7swf8o7wvd2");
                    RongIMLib.RongIMClient.init("${key}");
                    // 设置连接监听状态 （ status 标识当前连接状态 ）
                    // 连接状态监听器
                    RongIMClient.setConnectionStatusListener({
                        onChanged: function (status) {
                            switch (status) {
                                case RongIMLib.ConnectionStatus.CONNECTED:
                                    console.log('链接成功');
                                    break;
                                case RongIMLib.ConnectionStatus.CONNECTING:
                                    console.log('正在链接');
                                    break;
                                case RongIMLib.ConnectionStatus.DISCONNECTED:
                                    console.log('断开连接');
                                    break;
                                case RongIMLib.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT:
                                    console.log('其他设备登录');
                                    break;
                                case RongIMLib.ConnectionStatus.DOMAIN_INCORRECT:
                                    console.log('域名不正确');
                                    break;
                                case RongIMLib.ConnectionStatus.NETWORK_UNAVAILABLE:
                                console.log('网络不可用');
                                break;
                                }
                        }});

                    // 消息监听器
                    RongIMClient.setOnReceiveMessageListener({
                        // 接收到的消息
                        onReceived: function (message) {
                            console.log('message', message);
                            // 判断消息类型
                            switch(message.messageType){
                                case RongIMClient.MessageType.TextMessage:
                                    // message.content.content => 消息内容
                                    that.messages.push(message)
                                    break;
                                case RongIMClient.MessageType.VoiceMessage:
                                    // 对声音进行预加载                
                                    // message.content.content 格式为 AMR 格式的 base64 码
                                    that.messages.push(message)
                                    break;
                                case RongIMClient.MessageType.ImageMessage:
                                    // message.content.content => 图片缩略图 base64。
                                    // message.content.imageUri => 原图 URL。
                                    that.messages.push(message)
                                break;
                                case RongIMClient.MessageType.DiscussionNotificationMessage:
                                // message.content.extension => 讨论组中的人员。
                                break;
                                case RongIMClient.MessageType.LocationMessage:
                                // message.content.latiude => 纬度。
                                // message.content.longitude => 经度。
                                // message.content.content => 位置图片 base64。
                                break;
                                case RongIMClient.MessageType.RichContentMessage:
                                // message.content.content => 文本消息内容。
                                // message.content.imageUri => 图片 base64。
                                // message.content.url => 原图 URL。
                                break;
                                case RongIMClient.MessageType.InformationNotificationMessage:
                                    // do something...
                                break;
                                case RongIMClient.MessageType.ContactNotificationMessage:
                                    // do something...
                                break;
                                case RongIMClient.MessageType.ProfileNotificationMessage:
                                    // do something...
                                break;
                                case RongIMClient.MessageType.CommandNotificationMessage:
                                    // do something...
                                break;
                                case RongIMClient.MessageType.CommandMessage:
                                    // do something...
                                break;
                                case RongIMClient.MessageType.UnknownMessage:
                                    // do something...
                                break;
                                default:
                                    // do something...
                            }
                        }
                    });
                    // 连接服务器
                    RongIMClient.connect("${token}", {
                        onSuccess: function(userId) {
                            console.log("Connect successfully." + userId);
                            that.opening == '进行中' && that.joinRoom()
                        },
                        onTokenIncorrect: function() {
                            console.log('token无效');
                        },
                        onError:function(errorCode){
                            var info = '';
                            switch (errorCode) {
                                case RongIMLib.ErrorCode.TIMEOUT:
                                info = '超时';
                                break;
                                case RongIMLib.ErrorCode.UNKNOWN_ERROR:
                                info = '未知错误';
                                break;
                                case RongIMLib.ErrorCode.UNACCEPTABLE_PaROTOCOL_VERSION:
                                info = '不可接受的协议版本';
                                break;
                                case RongIMLib.ErrorCode.IDENTIFIER_REJECTED:
                                info = 'appkey不正确';
                                break;
                                case RongIMLib.ErrorCode.SERVER_UNAVAILABLE:
                                info = '服务器不可用';
                                break;
                            }
                            console.log(errorCode);
                        }
                    });
                    this.RongIMVoice = RongIMLib.RongIMVoice;
                    this.RongIMEmoji = RongIMLib.RongIMEmoji;
                    this.RongIMVoice.init();
                    this.RongIMEmoji.init(config)
                    this.initViewer()
                },
                methods: {
                    delOrder: function() {
                        if(confirm('确认要删除？'))
                            $.post('/system/delplanliveinfo', { liveId: '${order.id}' }).done(function(){
                                window.location.replace('/system/vediolist')
                            })
                    },
                    showMsg: function (msgContent){
                        var that = this
                        if(msgContent){
                            var duration = msgContent.length/1024;    // 音频持续大概时间(秒)
                            if(!!window.ActiveXObject || "ActiveXObject" in window){
                                //如果是 IE 浏览器
                                that.RongIMVoice.preLoaded(msgContent);
                                that.RongIMVoice.play(msgContent,duration);
                            }else{
                                that.RongIMVoice.preLoaded(msgContent, function(){
                                    that.RongIMVoice.play(msgContent,duration);
                                });
                            }
                        }else{
                            console.error('请传入 amr 格式的 base64 音频文件');
                        }
                    },
                    initViewer: function () {
                                $('.hasimgview').viewer()
                    },
                    joinRoom: function() {
                        var IM = RongIMClient.getInstance();
                        IM.joinChatRoom('6666', 50, {
                            onSuccess: function() {
                                console.log("加入聊天室成功");
                            },
                            onError: function(error) {
                                console.log("加入聊天室失败");
                            }
                        });
                    },
                    sendMsg: function() {
                        // 发送消息相关参数说明
                        var conversationtype = RongIMLib.ConversationType.CHATROOM;
                        /*
                        PRIVATE 为单聊、
                        DISCUSSION 为讨论组、
                        GROUP 为群组、
                        CHATROOM 为聊天室、
                        CUSTOMER_SERVICE 为客服、
                        SYSTEM 为系统消息、
                        APP_PUBLIC_SERVICE 为应用公众账号（应用内私有）、
                        PUBLIC_SERVICE 为公众账号 (公有)
                        */
                        var targetId = "xxx"; // 目标 Id，根据不同的 ConversationType，可能是用户 Id、讨论组 Id、群组 Id。
                        var msg = new RongIMLib.TextMessage({content:"hello RongCloud!",extra:"附加信息"});
                        RongIMClient.getInstance().sendMessage(conversationtype, targetId, msg, {
                            onSuccess: function (message) {
                                //message 为发送的消息对象并且包含服务器返回的消息唯一Id和发送消息时间戳
                                console.log("Send successfully");
                            },
                            onError: function (errorCode,message) {
                                var info = '';
                                switch (errorCode) {
                                    case RongIMLib.ErrorCode.TIMEOUT:
                                        info = '超时';
                                        break;
                                    case RongIMLib.ErrorCode.UNKNOWN_ERROR:
                                        info = '未知错误';
                                        break;
                                    case RongIMLib.ErrorCode.REJECTED_BY_BLACKLIST:
                                        info = '在黑名单中，无法向对方发送消息';
                                        break;
                                    case RongIMLib.ErrorCode.NOT_IN_DISCUSSION:
                                        info = '不在讨论组中';
                                        break;
                                    case RongIMLib.ErrorCode.NOT_IN_GROUP:
                                        info = '不在群组中';
                                        break;
                                    case RongIMLib.ErrorCode.NOT_IN_CHATROOM:
                                        info = '不在聊天室中';
                                        break;
                                    default :
                                        info = x;
                                        break;
                                }
                                console.log('发送失败:' + info);
                            }
                        });
                    }
                }
            });
        </script>
    </body>
</html>