var pc, main; // 视频的DIV
var errorNotice; // 错误提示DIV
var socket; // websocket
var localAudio;//
var localVideo; // 本地视频
var miniVideo; // 本地小窗口
var remoteVideo; // 远程视频
var localStream; // 本地视频流
var remoteStream; // 远程视频流
var localVideoUrl; // 本地视频地址
var initiator = false; // 是否已经有人在等待---默认false--如果医生点击等待连接的话专家进入就需要设置为true
var started = false; // 是否开始
var channelReady = false; // 是否打开websocket通道
var bClose = false;
var channelOpenTime;
var channelCloseTime;

var mediaRecorder;
var recordedBlobs;

var screensharing;


window.URL = window.URL || window.webkitURL || window.mozURL || window.msURL;
var offerOptions = {
	offerToReceiveAudio : 1,
	offerToReceiveVideo : 1
};
var constraints = {
	audio : true,
	video : true
};
var bar = document.querySelector('#stateBar'), 
	barspan = document.querySelector('#stateBar span'), 
	btnswitch = document.querySelector('#btn-switch'), 
	ctrs = document.querySelector('#main .controls'), 
	videotiptxt = document.querySelector('.videotiptxt');
var footer = document.getElementById("footer");
var firsttip = document.getElementById("firsttip");
var pictxtdialog = document.getElementById("pictxtdialog");
main = document.getElementById("main");
errorNotice = document.getElementById("errorNotice");
localAudio = document.getElementById("localAudio");
localVideo = document.getElementById("localVideo");
miniVideo = document.getElementById("miniVideo");
remoteVideo = document.getElementById("remoteVideo");

var _uid; // user

// 初始化
function initialize() {
	console.log("_utype:" + _utype + "initiator:" + initiator);
	channelReady = false;
	bClose = false;
	console.log("初始化");
	noticeMsg();
	if (_utype == '1') {
	} else {
		sharetodoc();
	}
	openChannel();
	getUserMedia();
}

function openwin() {
	window._OpenWindow = window.open(_burl + "doctor/popscreenshare?oid=" + _roomKey,
			"newwin", "height=450,width=800,toolbar=no,menubar=no,alwaysRaised=yes,depended=yes,location=no");
}
function addRoomUser(callback) {
	$.ajax({
		async : false,
		type : "post",
		url : _burl + "doctor/addRoomUser",
		data : {
			orderid : _roomKey,
			utype : _utype
		},
		dataType : "json",
		success : function(data) {
			initiator = data._initiator == 1 ? true : false;
			_uid = data._uid;
			callback(data);
		}
	});
}

function upblob(blob) {
	 var fd=new FormData();
	  fd.append('file',blob);
	  fd.append('oid',_roomKey);
	  $.ajax({  
		  url:_burl+"doctor/upblob", 
         type: 'POST',  
         data: fd,    
         cache: false,  
         contentType: false,  
         processData: false,  
         success: function (returndata) {  
             //alert(returndata);  
         },  
         error: function (returndata) {  
             console.log('upblob-error:returndata', JSON.stringify(returndata, null, '\t'));
         }  
    }); 


}
function removeRoomUser(callback) {
	$.post(_burl + "doctor/removeRoomUser", {
		orderid : _roomKey,
		utype : _utype
	}, function(data) {
		_uid = null;
		callback(data);
	});
}
// 获取用户的媒体
function getUserMedia() {
	console.log("获取用户媒体");
	// navigator.mediaDevices.getUserMedia(constraints, onUserMediaSuccess,
	// onUserMediaError);
	navigator.mediaDevices.getUserMedia(constraints).then(onUserMediaSuccess,
			onUserMediaError);
}
// 获取用户媒体成功
function onUserMediaSuccess(stream) {
	
   if (this.localVideoUrl) {
      URL.revokeObjectURL(this.localVideoUrl);
    }
	var url = window.URL.createObjectURL(stream);
	localVideo.style.display = "inline-block";
	remoteVideo.style.display = "none";

	localVideo.src = url;
	localVideoUrl = url;
	localStream = stream;
	// localVideo.muted= true;
	// localVideo.volume = 0;

	if (initiator)
		maybeStart();
}
// 开始连接
function maybeStart() {
	if (!started && localStream && channelReady) {
		setNotice("连接中...");
		console.log("连接中...");
		createPeerConnection();
		pc.addStream(localStream);
		started = true;

		if (initiator)
			doCall();
	}
}

// 开始通话
function doCall() {
	console.log("开始呼叫");
	pc.createOffer(setLocalAndSendMessage, createOfferError, offerOptions);
}

function createOfferError(error) {
	localVideo.pause();
	miniVideo.pause();
	remoteVideo.pause();
	pc.close();
	pc = null;
	console.log('Failed to createOffer message : ' + error);
}

function setLocalAndSendMessage(sessionDescription) {
	pc.setLocalDescription(sessionDescription);
	waitForChannelReady();
	sendMessage(sessionDescription);
}

// 发送信息
function sendMessage(message) {
	var msgJson = JSON.stringify(message);
	if (socket && socket.readyState == 1) {
		socket.send(msgJson);
		console.log("发送信息 : " + msgJson);
	}
}

// 打开websocket
function openChannel() {
	console.log("====uid==" + _uid);
	console.log("打开websocket");
	socket = new WebSocket("wss://" + location.host
			+ "/acgist.videoTest/" + _roomKey + "/" + _uid);
	socket.onopen = onChannelOpened;
	socket.onmessage = onChannelMessage;
	socket.onclose = onChannelClosed;
	socket.onerror = onChannelError;
}

// 设置状态
function noticeMsg() {
	if (!initiator) {
	} else {
		setNotice("初始化...");
		console.log("初始化...");
	}
}

// 打开连接
function createPeerConnection() {
	var server = {
		"iceServers" : [ {
			"url" : "stun:123.56.251.54:3478"
		}, {
			"url" : "turn:123.56.251.54:3478",
			"username" : "ling",
			"credential" : "ling1234"
		} ]
	};
	pc = new RTCPeerConnection(server);

	pc.onicecandidate = onIceCandidate;
	pc.onconnecting = onSessionConnecting;
	pc.onopen = onSessionOpened;
	pc.onaddstream = onRemoteStreamAdded;
	pc.onremovestream = onRemoteStreamRemoved;
}

// 设置状态
function setNotice(msg) {
	footer.style.display = 'block';
	firsttip.style.display = 'none';
	if (msg.indexOf('已离开') != -1) {

	} else if (msg.indexOf('已断开') != -1) {

	} else if (msg.indexOf('连接成功') != -1) {
		window.setTimeout(function() {
			footer.style.display = 'none';
		}, 600);
	} else if (msg.indexOf('连接中') != -1) {

	} else if (msg.indexOf('初始化') != -1) {

	} else if (msg.indexOf('关闭') != -1) {
		footer.style.display = 'none';
	}
	footer.innerHTML = '<label>' + msg + '</label>';
}
// 响应
function doAnswer() {
	pc.createAnswer(setLocalAndSendMessage, createAnswerError);
}

function createAnswerError(error) {

	localVideo.pause();
	miniVideo.pause();
	remoteVideo.pause();
	pc.close();
	pc=null;
	console.log('Failed to createAnswer message : ' + error);
}

// websocket打开
function onChannelOpened() {
	console.log("websocket打开");

	channelOpenTime = new Date();
	channelReady = true;
	if (initiator)
		maybeStart();
}
// websocket收到消息
function onChannelMessage(message) {
	console.log("收到信息 : " + message.data);
	processSignalingMessage(message.data);// 建立视频连接
}

// 处理消息
function processSignalingMessage(message) {
	var msg = JSON.parse(message);
	if (msg.type === "offer") {
		if (!initiator && !started)
			maybeStart();
		pc.setRemoteDescription(new RTCSessionDescription(msg));
		doAnswer();
	} else if (msg.type === "answer" && started) {
		pc.setRemoteDescription(new RTCSessionDescription(msg));
	} else if (msg.type === "candidate" && started) {
		var candidate = new RTCIceCandidate({
			sdpMLineIndex : msg.label,
			candidate : msg.candidate
		});
		if(pc){
			pc.addIceCandidate(candidate);
		}
	} else if (msg.type === 'chart' && started) {
		chartmsg(msg.txt, 'doc');
	} else if (msg.type === "bye" && started) {
		onRemoteClose();
	} else if (msg.type === "nowaiting") {
		onRemoteClose();
		setNotice("对方已离开！");
	}
}

// websocket异常
function onChannelError(event) {
	console.log("websocket异常 ： " + event);
}

// websocket关闭 
function onChannelClosed() {
	console.log("websocket关闭");
	if (!channelOpenTime) {
		channelOpenTime = new Date();
	}
	channelCloseTime = new Date();
	if (!bClose) {
		openChannel();
	}//关闭后自动重新打开
}

// 获取用户流失败
function onUserMediaError(error) {
	console.log("获取用户流失败！");
	alert("获取用户流失败！");
}

// 邀请聊天：这个不是很清楚，应该是对方进入聊天室响应函数
function onIceCandidate(event) {
	if (event.candidate) {
		sendMessage({
			type : "candidate",
			label : event.candidate.sdpMLineIndex,
			id : event.candidate.sdpMid,
			candidate : event.candidate.candidate
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

	var url = window.URL.createObjectURL(event.stream);

	miniVideo.src = localVideo.src;
	remoteVideo.src = url;
	remoteStream = event.stream;
	waitForRemoteVideo();
	
	
	//开始录制
	if(_utype=='1'){
		//window.stream = remoteStream;//
		//startRecording();
	}
}

// 远程视频移除
function onRemoteStreamRemoved(event) {
	console.log("远程视频移除");
	
	//停止录制 并保存
	if(mediaRecorder){
		stopRecording();
		mediaRecorder =null;
		download();
	}

}

// 远程视频关闭
function onRemoteClose() {
	initiator = false;
	miniVideo.style.display = "none";
	remoteVideo.style.display = "none";
	localVideo.style.display = "inline-block";
	main.style.webkitTransform = "rotateX(360deg)";
	miniVideo.src = "";
	remoteVideo.src = "";
	localVideo.src = localVideoUrl;
	
	setNotice("对方已断开！");
	bClose = true;
	channelReady = false;
	started = false;
	closevideo();
	if (pc) {
		pc.close();
		pc = null;
		console.log("force pc close");
	}
	if (socket &&  socket.readyState == 1) {
		socket.close();
		socket = null;
		console.log("force socket close");
	}

	$('#reporttxt').addClass('center').slideDown();
	$('#reportbtn').fadeOut();
	
	
	//停止录制 并保存
	if(mediaRecorder){
		stopRecording();
		mediaRecorder =null;
		download();
	}
	
}

// 等待远程视频
function waitForRemoteVideo() {
	console.log("remoteVideo.currentTime : " + remoteVideo.currentTime);
	if (remoteVideo.currentTime > 0) { // 判断远程视频长度
		localAudio && localAudio.pause();// 停止播放
		transitionToActive();
	} else {
		setTimeout(waitForRemoteVideo, 100);
	}
}

// 等待远程视频
function waitForChannelReady() {

	if (channelReady == false) {
		setTimeout(waitForChannelReady, 100);
	}

}
// 接通远程视频
function transitionToActive() {
	remoteVideo.style.display = "inline-block";
	localVideo.style.display = "none";
	main.style.webkitTransform = "rotateX(360deg)";
	setTimeout(function() {
		localVideo.src = "";
	}, 500);
	setTimeout(function() {
		miniVideo.style.display = "inline-block";
		// miniVideo.style.display = "none";
	}, 1000);
	_utype == '1' && openwin();
	console.log("连接成功!");
	setNotice("连接成功！");
	openvideo();
}

// 全屏
function fullScreen() {
	main.webkitRequestFullScreen();
}

// 设置浏览器支持提示信息
function errorNotice(msg) {
	main.style.display = "none";
	errorNotice.style.display = "block";
	errorNotice.innerHTML = msg;
}

if (!WebSocket) {
	alert("你的浏览器不支持WebSocket！建议使用谷歌浏览器");
} else if (!RTCPeerConnection) {
	alert("你的浏览器不支持RTCPeerConnection！");
}

function initialfirstipxt(bol) {
	var initiatorVideo = _hasuser >= '1';
	if (_utype == '2') {
		if (bol) {
			firsttip.innerHTML = '<label>对方医生已就绪，可以发起视频问诊</label>';
		} else {
			firsttip.innerHTML = '<label>等待医生准备就绪后，再发起连接</label>';
		}
	} else {
		if (bol) {
			firsttip.innerHTML = '<label>等待专家发起连接</label>';
		} else {
			firsttip.innerHTML = '<label>标记状态为‘确认等待’后，等待专家发起连接</label>';
		}
	}
	firsttip.style.display = !initiatorVideo ? 'block' : 'none';
}
function initiallizeVideo(bol) {
	var initiatorVideo = _hasuser >= '1';
	if (_utype == '2') {
		btnswitch.style.display = 'block';
		videotiptxt.innerHTML = "来自医生的远程视频";
	} else {
		btnswitch.style.display = initiatorVideo ? 'block' : 'none';
		videotiptxt.innerHTML = "来自专家的远程视频";
	}
	if (initiatorVideo && bol) {
		localAudio.src = _href + 'audio/wx_video_on.mp3';
		localAudio.loop = true;
	}

	ctrs.style.display = (bol ? 'block' : 'none');
	ctrs.className = 'controls' + (initiatorVideo ? ' tipforuser' : '');
	btnswitch.innerHTML = initiatorVideo ? '接受视频问诊' : '发起视频问诊';

	barspan.innerHTML = '远程会诊' + (bol ? '已准备就绪' : '');
	footer.style.display = 'none';
	// 标记为等待界面
	otherset(3);
	nextable(3);
	initialfirstipxt(bol);
}
function openvideo() {
	barspan.innerHTML = bar.getAttribute('data-state');
	btnswitch.innerHTML = '结束';
	btnswitch.className = 'videoswitch btn-switch cancelbtn';
	btnswitch.style.display = 'block';
	firsttip.style.display = 'none';

	ctrs.style.display = 'block';
	ctrs.className = 'controls hascancelbtn';

	pictxtdialog.style.display = 'block';

	localAudio.src = '';
	localAudio.loop = false;
	// 标记为可以结束
	otherset(4);
	nextable(4);
}
function closevideo() {
	btnswitch.innerHTML = '发起视频问诊';
	btnswitch.className = 'videoswitch btn-switch animate1';
	if (_utype == '2') {
		btnswitch.style.display = 'block';
	} else {
		btnswitch.style.display = 'none';
	}
	ctrs.style.display = 'block';
	ctrs.className = 'controls';

	firsttip.style.display = 'none';
	pictxtdialog.style.display = 'none';

	barspan.innerHTML = '远程会诊已完成';

	miniVideo.src = "";
	remoteVideo.src = "";
	localVideo.src = '';
	localAudio.src = _href + 'audio/wx_video_off.mp3';
	localAudio.loop = false;
}
function waitvideo() {
	if (_utype == '2') {
		videotiptxt.innerHTML = "等待对方医生接收";
	} else {
		videotiptxt.innerHTML = "等待对方专家接收";
	}
	ctrs.style.display = 'block';
	ctrs.className = 'controls tipforuser hascancelbtn';
	firsttip.style.display = 'none';

	btnswitch.className = 'videoswitch btn-switch cancelbtn';
	btnswitch.style.display = 'block';
	btnswitch.innerHTML = '结束';
	barspan.innerHTML = '远程会诊' + ('已准备就绪');
	localAudio.src = _href + 'audio/wx_video_on.mp3';
	localAudio.loop = true;
}

function endwaitvideo() {

	var video = document.getElementById("user" + _roomKey);
	if (video && video.parentNode)
		video.parentNode.removeChild(video);
	if (_utype == '2') {
		btnswitch.style.display = 'block';
	} else {
		btnswitch.style.display = 'none';
	}
	btnswitch.innerHTML = '发起视频问诊';
	btnswitch.className = 'videoswitch btn-switch animate1';
	ctrs.style.display = 'block';
	ctrs.className = 'controls';
	firsttip.style.display = 'block';

	barspan.innerHTML = '远程会诊';

	miniVideo.src = "";
	remoteVideo.src = "";
	localVideo.src = '';
	localAudio.src = _href + 'audio/wx_video_off.mp3';
	localAudio.loop = false;

	sendMessage({
		type : "bye"
	});
	started = false;
	bClose = true;
	channelReady = false;
	if (pc) {
		pc.close();
		pc = null;
		console.log("force pc close");
	}
	if (socket && socket.readyState == 1) {
		socket.close();
		socket = null;
		console.log("force socket close");
	}
	$('#reporttxt').addClass('center').slideDown();
	$('#reportbtn').fadeOut();
	
	//发送结束通知，关闭分享屏幕
	closeshare();
	
	//停止录制 并保存
	if(mediaRecorder){
		stopRecording();
		mediaRecorder =null;
		download();
	}
}

function closeshare(){
	
	if(screensharing){
		//screensharing.leave();
	}
	
	$.post(_burl + "doctor/closeshare", {
		orderid : _roomKey
	}, function(data) {
	});
	
	
}

function onlevelpage() {
	sendMessage({
		type : "bye"
	});
	localAudio && localAudio.pause();// 停止播放音域；
	if (pc) {
		pc.close();
		pc = null;
		console.log("force pc close");
	}
	if (socket && socket.readyState == 1) {
		socket.close();
		socket = null;
		console.log("force socket.close");
	}
	window.parent['_sid'] = '';
	
	//发送结束通知，关闭分享屏幕
	closeshare();
  
	 //确保推出房间
	if(_uid){
		console.log('removeRoomUser');
		removeRoomUser(function(d) {});
	}
	
	//停止录制 并保存
	if(mediaRecorder){
		stopRecording();
		mediaRecorder =null;
		download();
	}

	//没有实际作用，留下时依旧视频断开了
	/*if(!!remoteVideo.src || !!localVideo.src)
		return '确定要离开视频聊天么？';*/
}
$(document).ready(function() {
	window.onbeforeunload = onlevelpage;
});
function videoClick() {
	footer.style.display = 'none';
	firsttip.style.display = 'none';
	if (_protag == '4' && btnswitch.innerHTML.indexOf('视频问诊') != -1) {
		addRoomUser(function(d) {
			if (d.status == 'error') {
				setNotice(d.message);
			} else {
				initialize();
				!initiator && waitvideo();
			}
		});// 用户进入房间
	} else if (btnswitch.innerHTML.indexOf('结束') != -1) {
		endwaitvideo();
		if (videotiptxt.innerHTML.indexOf('等待对方') != -1) {
			console.log('removeRoomUser');
			removeRoomUser(function(d) {});
		}
	}
}


//////////////////////////////////////////////////////////////
function startRecording() {
    var options = {
        mimeType: 'video/webm'
    };
    recordedBlobs = [];
    try {
        mediaRecorder = new MediaRecorder(window.stream, options);
    } catch (e0) {
        console.log('Unable to create MediaRecorder with options Object: ', e0);
        try {
            options = {
                mimeType: 'video/webm,codecs=vp9'
            };
            mediaRecorder = new MediaRecorder(window.stream, options);
        } catch (e1) {
            console.log('Unable to create MediaRecorder with options Object: ', e1);
            try {
                options = 'video/vp8'; // Chrome 47
                mediaRecorder = new MediaRecorder(window.stream, options);
            } catch (e2) {
                alert('MediaRecorder is not supported by this browser.\n\n' +
                    'Try Firefox 29 or later, or Chrome 47 or later, with Enable experimental Web Platform features enabled from chrome://flags.');
                console.error('Exception while creating MediaRecorder:', e2);
                return;
            }
        }
    }
    console.log('Created MediaRecorder', mediaRecorder, 'with options', options);
    //recordButton.textContent = 'Stop Recording';
    //playButton.disabled = true;
    //downloadButton.disabled = true;
    mediaRecorder.onstop = handleStop;
    mediaRecorder.ondataavailable = handleDataAvailable;
    mediaRecorder.start(10); // collect 10ms of data
    console.log('MediaRecorder started', mediaRecorder);
}
 
function stopRecording() {
    mediaRecorder.stop();
    //console.log('Recorded Blobs: ', recordedBlobs);
}
 
function download() {
    var blob = new Blob(recordedBlobs, {
        type: 'video/webm'
    });
    upblob(blob);
 
    /*var url = window.URL.createObjectURL(blob);
	var a = document.createElement('a');
	a.style.display = 'none';
	a.href = url;
	a.download = _roomKey + "-" +getNowFormatDate()  + '.webm';
	document.body.appendChild(a);
	a.click();
	setTimeout(function() {
		document.body.removeChild(a);
		window.URL.revokeObjectURL(url);
	}, 100);*/
 
}
 
//获取当前的日期时间 格式“yyyyMMddHHMMSS”
 
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "";
    var seperator2 = "";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate + "" + date.getHours() +
        seperator2 + date.getMinutes() + seperator2 + date.getSeconds();
    return currentdate;
}
 
function handleStop(event) {
    console.log('Recorder stopped: ', event);
}
 
function handleDataAvailable(event) {
    if (event.data && event.data.size > 0) {
        recordedBlobs.push(event.data);
    }
}
/////////////////////////////////////////////////////////////

//如果是专家
(function () {
   if(_utype=='1'){
	   return;
   }
   
   console.log('create Screen:room:' +_roomKey);
   screensharing = new Screen("room" + _roomKey);

   //custom signaling channel
   //you can use each and every signaling channel
   //any websocket, socket.io, or XHR implementation
   //any SIP server
   //anything! etc.
   /*screensharing.openSignalingChannel = function(callback) {
    return io.connect("https://123.56.251.54:8888").on('message', callback);
   };
   */

   screensharing.onscreen = function(_screen) {
       	console.log('onscreen:' +_screen.userid);
   };

   screensharing.onaddstream = function(media) {
   	console.log('onaddstream:' +media.userid);
   };
   screensharing.onuserleft = function(userid) {
   	console.log('onuserleft:' +userid);
   };

   screensharing.onNumberOfParticipantsChnaged = function(numberOfParticipants) {
   	if (!screensharing.isModerator)
   		return;

   	console.log('onNumberOfParticipantsChnaged:' + numberOfParticipants);
   	
   };
   // check pre-shared screens
   console.log('check Screen:');
   screensharing.check();
})();




function sharetodoc() {
	
	screensharing.userid = "user" + _uid;
	screensharing.isModerator = true;
	screensharing.share();
	
	console.log('sharetodoc:' + screensharing.userid);
}
