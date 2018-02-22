function VIDEO_JS(o) {
    var pc; // 视频的DIV
    var pcsocket; // websocket
    var localAudio; //
    var localVideo; // 本地视频
    var remoteVideo; // 远程视频

    var localStream; // 本地视频流
    var remoteStream; // 远程视频流
    var localVideoUrl; // 本地视频地址
    var channelCloseTime;
    var channelOpenTime;
    var channelReady = false; // 是否打开websocket通道
    var forceclose = false;
    var pcstarted = false;
    var bShare = false;
    var bMediaReady = false;

    var onlyAudio = false;
    var bSwitch = false;

    /////////////////////////////////////////////////
    var timerTask; //定时器
    var startVedioTime; //视频开始时间
    var lastUpdateTime; //最近更新时间
    var lastUpdateUITime; //最近更新UI时间
    var lastOverTime; //最近一次超时的时间
    var screensharing;

    var RTCPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
    var RTCSessionDescription = window.RTCSessionDescription || window.mozRTCSessionDescription || window.webkitRTCSessionDescription;
    var RTCIceCandidate = window.mozRTCIceCandidate || window.RTCIceCandidate;

    navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
    window.URL = window.URL || window.webkitURL || window.mozURL || window.msURL;
    window.MediaStream = window.MediaStream || window.webkitMediaStream;

    var _consultationdur = o.consultationDur,
        _vediotime = o.vedioDur;
    var _utype = o.roletype == 'receive' ? '2' : '1';
    var _roomKey = o.id;
    var _uid = o.uid;
    var _remainTime = (_consultationdur * 60 - _vediotime) * 1000;
    var _connect = false;

    var server = window.IceServers || {};
    var offerAnswerConstraints = {
        optional: [],
        mandatory: {
            OfferToReceiveAudio: true,
            OfferToReceiveVideo: true
        }
    };

    var constraints = {
        audio: true,
        video: true
    };

    var fail = {
        color: $.color.error,
        icon: "fa fa-warning shake animated",
        timeout: 5000
    };
    var normal = {
        color: $.color.noraml,
        icon: "fa fa-bell swing animated",
        timeout: 5000
    };
    var warning = {
        color: $.color.waring,
        icon: "fa fa-shield fadeInLeft animated",
        timeout: 5000
    };
    var success = {
        color: $.color.success,
        icon: "fa fa-check",
        timeout: 5000
    };
    
    //==========计时器开始=========
    function startTimerTask() {
        startVedioTime = new Date();
        lastUpdateTime = new Date();
        lastUpdateUITime = new Date();
        lastOverTime = 0;
        timerTask = setInterval(updateUITime, 900);
    }

    function stopTimerTask() {
        if (timerTask) {
            clearInterval(timerTask);
            timerTask = null;
        }
    }

    function updateUITime() {
        var curTime = new Date();

        //更新界面UI 
        _remainTime = _remainTime - (curTime.getTime() - lastUpdateUITime.getTime());
        //如果还有超过2分钟 的时间 则正计时
        if (_remainTime > 2 * 60 * 1000) {
            //更新UI
            calculateUITime(_remainTime, 0);
            //如果 不到 2分钟 则倒计时
        } else if (_remainTime >= 0) {
            //更新UI
            calculateUITime(_remainTime, -1);
            //超过 了订单时间 超过时间
        } else {
            //更新UI
            calculateUITime(_remainTime, 1);

        }

        lastUpdateUITime = curTime;
        //如果是医生 还需要更新时间到后台
        if (_utype == '1') {
            //如果大于 10 秒则更新到后台
            if (curTime.getTime() - lastUpdateTime.getTime() >= 10 * 1000) {
                //调用后台函数
                updateVedioTime(Math.round((curTime.getTime() - lastUpdateTime.getTime()) / 1000));
                lastUpdateTime = curTime;
            }
        }

    }
    //更新计时器
    function calculateUITime(remainTime, direction) {
        var timdiv = document.querySelector('#timers');
        if (direction == 0) {
            remainTime = _consultationdur * 60 * 1000 - remainTime;
        }
        remainTime = Math.abs(remainTime);
        //计算出小时数   
        var hours = Math.floor(remainTime / (3600 * 1000));
        //计算相差分钟数  
        var leave2 = remainTime % (3600 * 1000); //计算小时数后剩余的毫秒数  
        var minutes = Math.floor(leave2 / (60 * 1000));
        //计算相差秒数  
        var leave3 = leave2 % (60 * 1000); //计算分钟数后剩余的毫秒数  
        var seconds = Math.floor(leave3 / 1000);
        var tempTime = (hours > 9 ? hours : "0" + hours) + ":" + (minutes > 9 ? minutes : "0" + minutes) + ":" + (seconds > 9 ? seconds : "0" + seconds);

        //倒计时
        if (direction == -1) {
            //首次倒计时 提醒
            if (timdiv.innerHTML.indexOf('已通话') != -1) {
                //语音提醒  还剩几分钟，请抓紧时间提问
                //generateAudio('您本次会诊还剩' + minutes + '分钟，请抓紧时间。');
                //遮罩提醒
                var mesg = '您本次会诊还剩' + minutes + '分钟，请抓紧时间。';
                showOverTime(mesg);
            }
            tempTime = "<span>倒计时：</span> " + tempTime;
            timdiv.style.width = '100%';
            timdiv.style.fontSize = '24px';
            //正即时
        } else if (direction == 0) {
            tempTime = "<span>已通话：</span>" + tempTime;
            //超时
        } else {
            //首次超时时 提醒
            if (timdiv.innerHTML.indexOf('倒计时') != -1) {
                //语音提醒 已到时，请抓紧时间收尾
                generateAudio('您本次会诊时间已结束，请抓紧时间收尾。');
            }
            tempTime = "<span>已超时：</span> " + tempTime;
            timdiv.style.color = 'red';
            timdiv.style.opacity = '1';
            timdiv.style.fontSize = '24px';
            timdiv.style.width = '100%';
            //超时每超过三分钟 界面弹出提示框,需要更改
            if (_utype == '1') {
                var curOverTime = Math.floor(remainTime / (3 * 60 * 1000));
                if (curOverTime != lastOverTime) {
                    lastOverTime = curOverTime;
                    //alert('您已超时' +curOverTime*3 + '分钟'); 休要重新修改，要影响使用
                    var mesg = '您已超时' + curOverTime * 3 + '分钟！';
                    showOverTime(mesg);
                }
            }
        }
        timdiv.innerHTML = tempTime;
        timdiv.style.display = 'block';
        //console.log(tempTime);//更新界面中的时间显示，需要更改	
    }
    //==========计时器结束=========

    //文字转语音播放
    function generateAudio(txt) {
        $.post("/doctor/generateAudio", {
            content: txt
        }, function (data) {
            localAudio.src = data.url;
            localAudio.loop = false;
        });
    }

    function updateVedioTime(times) {
        $.post("/doctor/insertVedioTime", {
            oid: _roomKey,
            times: times,
        }, function (data) {});
    }

    //清空时间
    function clearVedioTime() {
        $.post("/doctor/clearVedioTime", {
            oid: _roomKey
        }, function (data) {
            if (data.status == 'success') {
                lastUpdateUITime = new Date();
                curTime = lastUpdateUITime;
                _vediotime = 0;
                _remainTime = (_consultationdur * 60 - _vediotime) * 1000;
            }
        });
    }

    //获取用户的媒体
    function getUserMedia() {
        console.log("获取用户媒体");
        clearcamera();
        navigator.getUserMedia(constraints, onUserMediaSuccess, onUserMediaError);
    }

    //获取用户媒体成功
    function onUserMediaSuccess(stream) {
        if (localVideoUrl) {
            URL.revokeObjectURL(localVideoUrl);
        }
        localAudio = document.getElementById("localAudio");
        localVideo = document.getElementById("localVideo");
        remoteVideo = document.getElementById("remoteVideo");

        localVideoUrl = window.URL.createObjectURL(stream);
        localVideo.src = localVideoUrl;
        localStream = stream;
        bMediaReady = true;

        // 专家 开始会诊按钮
        _utype == '2' && sendMessage({
            type: "ready",
            roomId: _roomKey,
            userId: _uid,
            userType: _utype
        });
    }

    // 获取用户流失败
    function onUserMediaError(error) {
        console.log("获取用户流失败！");
        alert("获取用户流失败！");
    }

    // 发送信息
    function sendMessage(message) {
        var msgJson = JSON.stringify(message);
        if (pcsocket && pcsocket.readyState == 1) {
            pcsocket.send(msgJson);
        } else {
            console.log("发送信息 失败,socket 没有打开 ");
        }
    }

    // //////////////////////////////////////////
    // 打开websocket
    function openChannel() {
        pcsocket = new WebSocket(window.WebSocketSrc || '');
        pcsocket.onopen = onChannelOpened;
        pcsocket.onmessage = onChannelMessage;
        pcsocket.onclose = onChannelClosed;
        pcsocket.onerror = onChannelError;
    }

    // websocket打开
    function onChannelOpened() {
        //专家尝试重新连接
        if(_utype == '2' && Page.status == '1') {
            // setNotice('尝试重新连接...');
            start();
        }
        console.log("websocket打开");
        channelOpenTime = new Date();
        channelReady = true;

        //进入房间 
        sendMessage({
            type: "join",
            roomId: _roomKey,
            userId: _uid,
            userType: _utype
        });
        window.onbeforeunload = destroy;
    }
    // websocket收到消息
    function onChannelMessage(message) {
        //console.log("收到信息 : " + message.data);
        processSignalingMessage(message.data);
    }

    // websocket异常
    function onChannelError(event) {
        channelReady = false;
        console.log("websocket异常 ： " + event);
    }

    // websocket关闭
    function onChannelClosed() {
        console.log("websocket关闭");
        channelReady = false;

        if (!forceclose) {
            //关闭后重新打开
            openChannel();
        }
        forceclose = false;

    }

    function createOfferConnection_new() {
        bMediaReady = false;
        getUserMedia();
        waitForMedia_offer();
    }

    function waitForMedia_offer() {
        if (bMediaReady) {
            bMediaReady = false;
            createOfferConnection();
        } else {
            setTimeout(waitForMedia_offer, 100);
        }
    }

    // 打开连接
    function createOfferConnection() {
        if (!pcstarted && localStream && channelReady) {
            setNotice("连接中...");
            console.log("连接中...");
            pc = new RTCPeerConnection(server);
            pc.onicecandidate = onIceCandidate;
            pc.onconnecting = onSessionConnecting;
            pc.onopen = onSessionOpened;
            pc.onaddstream = onRemoteStreamAdded;
            pc.onremovestream = onRemoteStreamRemoved;
            pcstarted = true;
            //pc.addStream(localStream);
            if (onlyAudio) {
                var sendStream = new window.MediaStream(localStream.getAudioTracks());
                pc.addStream(sendStream);
            } else {
                pc.addStream(localStream);
            }
            console.log("开始呼叫");
            pc.createOffer(setLocalAndSendMessage, createOfferError, offerAnswerConstraints);
        }
    }

    function createAnswerConnection(sdp) {
        setNotice("连接中...");
        console.log("连接中...");
        pc = new RTCPeerConnection(server);
        pc.onicecandidate = onIceCandidate;
        pc.onconnecting = onSessionConnecting;
        pc.onopen = onSessionOpened;
        pc.onaddstream = onRemoteStreamAdded;
        pc.onremovestream = onRemoteStreamRemoved;
        pcstarted = true;
        //pc.addStream(localStream);

        if (!localStream) {
            setNotice("视频启动失败，请刷新重试");
        }

        if (onlyAudio) {
            var sendStream = new window.MediaStream(localStream.getAudioTracks());
            pc.addStream(sendStream);
        } else {
            pc.addStream(localStream);
        }

        pc.setRemoteDescription(new RTCSessionDescription(sdp), onSdpSuccess, onSdpError);
        
        console.log("开始回答");
        pc.createAnswer(setLocalAndSendMessage, createAnswerError, offerAnswerConstraints);

    }

    function onIceCandidate(event) {
        if (event.candidate) {
            sendMessage({
                type: "candidate",
                label: event.candidate.sdpMLineIndex,
                id: event.candidate.sdpMid,
                candidate: event.candidate.candidate
            });
        } else {
            console.log("End of candidates.");
        }
    }

    // 开始连接
    function onSessionConnecting(message) {
        console.log("开始连接");
    }

    // 连接打开
    function onSessionOpened(message) {
        console.log("连接打开");
    }

    // 远程视频添加
    function onRemoteStreamAdded(event) {
        console.log("远程视频添加");
        if (remoteVideo.src) {
            URL.revokeObjectURL(remoteVideo.src);
        }
        var url = window.URL.createObjectURL(event.stream);
        localVideo.src = localVideoUrl;
        remoteVideo.src = url;
        remoteStream = event.stream;
        waitForRemoteVideo();
    }

    // 远程视频移除
    function onRemoteStreamRemoved(event) {
        console.log("远程视频移除");
    }

    // 等待远程视频
    function waitForRemoteVideo() {
        // console.log("remoteVideo.currentTime : " + remoteVideo.currentTime);
        if (remoteVideo.currentTime > 0) { // 判断远程视频长度
            //localAudio && localAudio.pause();// 提示音停止播放
            transitionToActive();
            //定时器更新页面时间
            startTimerTask();
        } else {
            setTimeout(waitForRemoteVideo, 100);
        }
    }

    function setLocalAndSendMessage(sessionDescription) {
        //sessionDescription.sdp = setBandwidth(sessionDescription.sdp);//
        pc.setLocalDescription(sessionDescription);
        sendMessage(sessionDescription);
    }

    function showOverTime(msg) {
        console.log('=======showovertime===========', msg);
        setNotice(msg, fail);
    }

    // 创建 offer失败
    function createOfferError(error) {
        localVideo.pause();
        remoteVideo.pause();
        pc.close();
        pcstarted = false;
        console.log('Failed to createOffer message : ' + error);
    }

    function createAnswerError(error) {
        localVideo.pause();
        remoteVideo.pause();
        pc.close();
        pcstarted = false;
        console.log('Failed to createAnswer message : ' + error);
    }

    function onSdpSuccess() {}

    function onSdpError(e) {
        console.error('sdp error:', e);
    }

    // ////////////////////////////////////////////////////////////
    // 设置状态
    function setNotice(msg, state) {
        var curr = {};
        if (!msg) return 0;
        if (msg.indexOf('已离开') != -1) {
            curr = state || fail;
        } else if (msg.indexOf('已断开') != -1) {
            curr = state || fail;
        } else if (msg.indexOf('连接成功') != -1) {
            curr = state || success;
        } else if (msg.indexOf('连接中') != -1) {
            curr = state || normal;
        } else if (msg.indexOf('初始化') != -1) {
            curr = state || normal;
        } else if (msg.indexOf('关闭') != -1 || msg.indexOf('失败') != -1) {
            curr = state || fail;
        } else {
            curr = state || normal;
        }
        PostMsg({
            name: 'bigBox',
            options: $.extend({
                title: "订单提示",
                content: msg
            }, curr)
        });
        console.log('setNotice', msg);
    }

    // 处理消息
    function processSignalingMessage(message) {
        var msg = JSON.parse(message);
        var u = _utype == '2' ? '医生' : '专家';

        if (msg.type === "ready") {
            createOfferConnection_new();
            PostMsg('showfooter');
        } else if (msg.type === "offer" && !pcstarted) {
            createAnswerConnection(msg);
            // 收到专家的回答信息
        } else if (msg.type === "answer" && pcstarted) {
            pc.setRemoteDescription(new RTCSessionDescription(msg), onSdpSuccess, onSdpError);
        } else if (msg.type === "candidate" && pcstarted) {
            var candidate = new RTCIceCandidate({
                sdpMLineIndex: msg.label,
                candidate: msg.candidate
            });
            pc.addIceCandidate(candidate);
            // 断开连接
        } else if (msg.type === "switch" /*&& pcstarted*/ ) {
            pcstarted = false;
            onlyAudio = msg.streamtype;
            if (pc) {
                pc.close();
                pc = null;
            }
            setNotice(onlyAudio ? "医生正切换到音频模式" : "医生正切换到视频模式");
            //离开房间
        } else if (msg.type === "leave") {
            pcstarted && onRemoteClose();
            setNotice(u + "已离开！");
            PostMsg('leaveroom');
            $room.expInfo.state = false;
            $room.audio = false;
            //进入房间
        } else if (msg.type === "join") {
            setNotice(u + "已进入房间！");
            $room.expInfo.state = true;
        } else if (msg.type === "otherjoin") {
            setNotice(msg.remark);
            pcstarted && onRemoteClose();
        }
    }

    // 远程视频关闭
    function onRemoteClose() {
        stopTimerTask(); //关闭计时器
        onlyAudio = bSwitch = pcstarted = false;
        clearvideo(); //关闭远程视频
        clearpc(); //关闭peer
        clearcamera();
    }

    // 接通远程视频
    function transitionToActive() {
        _utype != '2' && !bSwitch && PostMsg('sharescreen');
        console.log("连接成功!");
        if (!onlyAudio) {
            setNotice("连接成功！");
            $room.audio = false;
        } else {
            setNotice("语音通话中");
            $room.audio = true;
        }
        _connect = true;
    }

    function clearvideo() {
        _connect = false;

        //本地视频关闭
        if (localVideo && localVideo.src) {
            URL.revokeObjectURL(localVideo.src);
            localVideo.src = '';
        }
        localVideoUrl = null; //供隐藏的min使用的

        //远程视频关闭
        if (remoteVideo && remoteVideo.src) {
            URL.revokeObjectURL(remoteVideo.src);
            remoteVideo.src = "";
        }
        //关闭提示音
        if (localAudio) {
            localAudio.src = BASE.href + 'audio/wx_video_off.mp3';
            localAudio.loop = false;
        }
    }

    function clearpc() {
        closepc();
        // 发送结束通知，关闭分享屏幕
        if (bShare && _utype == '2') {
            closeshare();
            bShare = false;
        }

    }

    function clearsocket() {
        forceclose = true;
        if (pcsocket && pcsocket.readyState == 1 /*&& bol*/ ) {
            pcsocket.close();
            pcsocket = null;
            console.log("force socket close");
        }
    }

    function clearcamera() {
        //关闭摄像头
        if (localStream) {
            localStream.getTracks().forEach(function (track) {
                track.stop();
            });
            localStream = null;
        }
    }
    function closepc() {
        if (pc) {
            pc.close();
            pc = null;
            console.log("force pc close");
        }
    }
    function destroy() {
        pcstarted = false;
        localAudio && localAudio.pause(); // 停止播放音域；

        stopTimerTask(); //关闭定时任务
        clearvideo();
        clearpc();
        clearcamera();
        clearsocket();
        window.onbeforeunload = null;
    }
    function start() {
        _vedioTime = o.vedioDur;
        _remainTime = (_consultationdur * 60 - _vediotime) * 1000;
        //开始视频，开始有专家引导，医生端没有此功能
        pcstarted = false;
        onlyAudio = false;
        bSwitch = false;
        // 专家
        if (_utype == '2') {
            getUserMedia();
            //专家分享
            sharetodoc();
            bShare = true;
        }
    }
    // 如果是专家,实例化分享屏幕
    function initShareScreen() {
        var channel = 'room' + _roomKey;
        var sender = 'user' + _uid;
        var socket1 = io.connect(window.SocketIOSrc);

        console.log('create Screen:room:' + _roomKey);

        screensharing = new Screen(channel);
        screensharing.userid = sender;
        screensharing.isModerator = true;

        screensharing.openSignalingChannel = function (callback) {
            socket1.on('message', callback);
            return socket1;
        };

        screensharing.onscreen = function (_screen) {
            console.log('onscreen:' + _screen.userid);
        };

        screensharing.onaddstream = function (media) {
            console.log('onaddstream:' + media.userid);
        };
        screensharing.onuserleft = function (userid) {
            console.log('onuserleft:' + userid);
        };

        screensharing.onNumberOfParticipantsChnaged = function (numberOfParticipants) {
            if (!screensharing.isModerator)
                return;
            console.log('onNumberOfParticipantsChnaged:' + numberOfParticipants);
        };
        // check pre-shared screens
        console.log('check Screen:');
        screensharing.check();
    }

    function sharetodoc() {
        screensharing && screensharing.share();
        screensharing && console.log('sharetodoc:' + screensharing.userid);
    }
    function closeshare() {
        screensharing && screensharing.leave();
        $.post("/doctor/closeshare", {
            orderid: _roomKey
        });
    }
    this.sharetodoc = sharetodoc;

    //从视频切换到音频 或从音频切换到视频
    function switchVideoAudio(bAudio) {
        // 医生
        if (localStream && channelReady) {
            sendMessage({
                type: "switch",
                streamtype: bAudio
            });
            pcstarted = false;
            onlyAudio = bAudio;
            bSwitch = true;
            closepc();
            createOfferConnection();
        }
    }

    function setBandwidth(sdp) {
        if (typeof BandwidthHandler !== 'undefined') {
            var bandwidth = {
                audio: 64, // kbits (both min-max)    6-510 kbit/s  kbps是kilobits per seconds 
                video: 256 // kbits (both min-max)    100 - 2000+  kbit/s
            };
            sdp = BandwidthHandler.setApplicationSpecificBandwidth(sdp, bandwidth, false);
            sdp = BandwidthHandler.setVideoBitrates(sdp, {
                min: bandwidth.video,
                max: bandwidth.video
            });
            sdp = BandwidthHandler.setOpusAttributes(sdp, {
                maxaveragebitrate: bandwidth.audio * 8 * 1024,
                maxplaybackrate: bandwidth.audio * 8 * 1024,
                stereo: 1,
                maxptime: 3
            });
            return sdp;
        }
        // removing existing bandwidth lines
        sdp = sdp.replace(/b=AS([^\r\n]+\r\n)/g, '');
        sdp = sdp.replace(/a=mid:audio\r\n/g, 'a=mid:audio\r\nb=AS:64\r\n');
        sdp = sdp.replace(/a=mid:video\r\n/g, 'a=mid:video\r\nb=AS:256\r\n');
        return sdp;
    }
    this.tellExp = function () {
        if (!pcstarted) return PostMsg({
            name: 'smallBox',
            options: {
                title: "提示",
                content: '专家还没有发起连接呢',
                color: $.color.error,
                iconSmall: "fa fa-times",
                timeout: 10000
            }
        }), 0;
        $.post("/doctor/callforshare", {
            orderid: _roomKey
        }, function () {
            PostMsg({
                name: 'smallBox',
                options: {
                    title: "提示",
                    content: '已经通知专家了',
                    color: $.color.success,
                    iconSmall: "fa fa-times",
                    timeout: 10000
                }
            });
        });
    };

    this.switch = switchVideoAudio;
    this.destroy = destroy;
    this.start = start;
    this.init = function () {
        if (!WebSocket) {
            return alert("你的浏览器不支持WebSocket！建议使用谷歌浏览器"), false;
        } else if (!RTCPeerConnection) {
            return alert("你的浏览器不支持RTCPeerConnection！"), false;
        }
        openChannel();
        //getUserMedia();

        if (_utype == '2') {
            initShareScreen();
        }

        return this;
    }
}