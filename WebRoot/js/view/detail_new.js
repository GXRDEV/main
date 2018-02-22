var pc, main; // 视频的DIV
var pcsocket; // websocket
var localAudio;//
var localVideo; // 本地视频
var miniVideo; // 本地小窗口
var remoteVideo; // 远程视频
var localStream; // 本地视频流
var remoteStream; // 远程视频流
var localVideoUrl; // 本地视频地址
var channelCloseTime;
var channelOpenTime;
var channelReady = false; // 是否打开websocket通道
var forceclose = false;
var pcstarted = false;
var joinRoom  =false;

var onlyAudio = false;
var bSwitch = false;
///////////////////////////////////////////////////

var mediaRecorder;
var recordedBlobs;
var cansend = true;

/////////////////////////////////////////////////
var timerTask;       //定时器
var startVedioTime;  //视频开始时间
var lastUpdateTime;  //最近更新时间
var lastUpdateUITime;//最近更新UI时间
var lastOverTime;//最近一次超时的时间
var screensharing;


var RTCPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
var RTCSessionDescription = window.RTCSessionDescription || window.mozRTCSessionDescription || window.webkitRTCSessionDescription;
var RTCIceCandidate = window.mozRTCIceCandidate || window.RTCIceCandidate;
navigator.getUserMedia  = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
window.URL = window.URL || window.webkitURL || window.mozURL || window.msURL;
window.MediaStream = window.MediaStream || window.webkitMediaStream;


var server = {
	    "iceServers" : [ {
	        "url" : "stun:123.56.251.54:3478"
	    }, {
	        "url" : "turn:123.56.251.54:3478",
	        "username" : "ling",
	        "credential" : "ling1234"
	    } ]
};
var offerAnswerConstraints = {
        optional: [],
        mandatory: {
            OfferToReceiveAudio: true,
            OfferToReceiveVideo: true
        }
    };

var constraints = {
	audio : audioConstraints,
	video : true
};

var videoConstraints  = {
	    mandatory: {
	         maxWidth: 1280
	        ,maxHeight: 720
	        // maxWidth: 1920 
	        //,maxWidth: 1080
	        //,minAspectRatio: 1.333
	        //,maxAspectRatio: 1.334
	        //,minFrameRate: 20
	        //,minFrameRate: 3
	        ,minFrameRate: 30
	    },
	    optional: []
	};



var audioConstraints = {
    mandatory: {
        googEchoCancellation: true, // disabling audio processing
        googAutoGainControl: true,
        googNoiseSuppression: true,
        googHighpassFilter: true,
        googTypingNoiseDetection: true,
        //googAudioMirroring: true
    },
    optional: []
};
	
 var mediaConstraints = {
		    video: videoConstraints,
		    audio: audioConstraints
		};

var bar = document.querySelector('#stateBar'), barspan = document
		.querySelector('#stateBar span'), btnswitch = document
		.querySelector('#btn-switch'), ctrs = document
		.querySelector('#main .controls'), videotiptxt = document
		.querySelector('.videotiptxt');
var dialogBtn = document.getElementById('dialogBtn');
var footer = document.getElementById("footer");
var firsttip = document.getElementById("firsttip");
var pictxtdialog = document.getElementById("pictxtdialog");
main = document.getElementById("main");
localAudio = document.getElementById("localAudio");
localVideo = document.getElementById("localVideo");
miniVideo = document.getElementById("miniVideo");
remoteVideo = document.getElementById("remoteVideo");

var _remainTime =  (_consultationdur*60 - _vediotime)*1000;

// 初始化
function initialize() {
	forceclose = false;
	channelReady = false;
	pcstarted = false;
	onlyAudio = false;
	bSwitch = false;

	if (_utype == '1') {
		// 医生
	} else {
		// 专家
		sharetodoc();
	}

	openChannel();
	getUserMedia();

}

function openwin() {
	var src = _burl + "doctor/popscreenshare?oid=" + _roomKey,
		iframe = $('iframe#screenframe');
	iframe.size() < 1 ? 
		openNewWin() :
		iframe.attr('src',src).attr('data-src',src);
}
function openNewWin() {
	var src = _burl + "doctor/popscreenshare?oid=" + _roomKey;
	window._OpenWindow = window.open(src,"newwin","height=450,width=800,toolbar=no,menubar=no,alwaysRaised=yes,depended=yes,location=no");
}


function addRoomUser(callback) {
	$.ajax({
		async : false,
		type : "post",
		url : _burl + "doctor/addRoomUser",
		data : {
			orderid : _roomKey,
			uid  : _uid,
			utype   : _utype
		},
		dataType : "json",
		success : function(data) {
			callback(data);
			joinRoom = true;
		}
	});
}

function removeRoomUser(callback) {
	$.post(_burl + "doctor/removeRoomUser", {
		orderid : _roomKey,
		uid  : _uid,
		utype   : _utype
	}, function(data) {
		callback(data);
		joinRoom = false;
	});
}

function generateAudio(_content) {
	$.post(_burl + "doctor/generateAudio", {
		content : _content
	}, function(data) {
		localAudio.src = data.url;
		localAudio.loop=false;
	});
}

function updateVedioTime(times) {
	$.post(_burl + "doctor/insertVedioTime", {
		oid : _roomKey,
		times  : times,
	}, function(data) {
	});
}

function clearVedioTime() {
	$.post(_burl + "doctor/clearVedioTime", {
		oid : _roomKey
	}, function(data) {
		if(data.status == 'success'){
			lastUpdateUITime = new Date();
			curTime = lastUpdateUITime;
			_vediotime = 0;
			_remainTime =  (_consultationdur*60 - _vediotime)*1000;
		}
	});
}

// 获取用户的媒体
function getUserMedia() {
	console.log("获取用户媒体");
	/*navigator.mediaDevices.getUserMedia(constraints).then(onUserMediaSuccess,
			onUserMediaError);*/
	//constraints ，mediaConstraints
	navigator.getUserMedia(constraints, onUserMediaSuccess, onUserMediaError);
}

// 获取用户媒体成功
function onUserMediaSuccess(stream) {

	if (this.localVideoUrl) {
		URL.revokeObjectURL(this.localVideoUrl);
	}
	
	localVideoUrl = window.URL.createObjectURL(stream);
	localVideo.style.display = "inline-block";
	remoteVideo.style.display = "none";

	localVideo.src = localVideoUrl;
	localStream = stream;

	// 医生
    if (_utype == '1') {
		
		createOfferConnection();
	}

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
	pcsocket = new WebSocket("wss://" + location.host + "/signalmaster/"
			  + _roomKey + "/" + _uid);
	pcsocket.onopen = onChannelOpened;
	pcsocket.onmessage = onChannelMessage;
	pcsocket.onclose = onChannelClosed;
	pcsocket.onerror = onChannelError;
}

// websocket打开
function onChannelOpened() {
	console.log("websocket打开");
	channelOpenTime = new Date();
	channelReady = true;
	
	// 医生
	if (_utype == '1') {
		
		createOfferConnection();
	}
	
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
	if (!channelOpenTime) {
		channelOpenTime = new Date();
	}
	channelCloseTime = new Date();
	
	
	 //自动断开的
	if(!forceclose)
	{ 
		if(_utype == '1' ){
			onRemoteClose(true);
			setNotice("网络断开，请等待专家重新发起");
		} else if(_utype == '2' ){
			onRemoteClose(true);
			setNotice("网络断开，请重新发起");
		}
	}
	 
}

// 等待通道就绪
function waitForChannelReady() {

	if (!channelReady) {
		console.log("waitForChannelReady");
		setTimeout(waitForChannelReady,50);
	} else {
		console.log("channelReady");
	}
}
// //////////////////////////////////////////////////
// 打开连接
function createOfferConnection() {
	
	if (!pcstarted && localStream && channelReady){

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
		if(onlyAudio){
			var sendStream  = new  window.MediaStream(localStream.getAudioTracks());
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
	if(onlyAudio){
		var sendStream  = new window.MediaStream( localStream.getAudioTracks());
		pc.addStream(sendStream);
	} else {
		pc.addStream(localStream);
	}

	
	pc.setRemoteDescription(new RTCSessionDescription(sdp), onSdpSuccess, onSdpError);
	console.log("开始回答");
	pc.createAnswer(setLocalAndSendMessage, createAnswerError,offerAnswerConstraints);

}

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
	if (remoteVideo.src) {
		URL.revokeObjectURL(remoteVideo.src);
	}
	var url = window.URL.createObjectURL(event.stream);
	miniVideo.src= localVideoUrl;
	remoteVideo.src = url;
	remoteStream = event.stream;
	waitForRemoteVideo();
	
	//开始录制
	/*if(_utype=='1'){
		startRecording();
	}*/
}

// 远程视频移除
function onRemoteStreamRemoved(event) {
	console.log("远程视频移除");
	stopRecording();
}

// 等待远程视频
function waitForRemoteVideo() {
	console.log("remoteVideo.currentTime : " + remoteVideo.currentTime);
	if (remoteVideo.currentTime > 0) { // 判断远程视频长度
		localAudio && localAudio.pause();// 提示音停止播放
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

function startTimerTask(){
	startVedioTime = new Date();
	lastUpdateTime = new Date();
	lastUpdateUITime = new Date();
	lastOverTime = 0;
	timerTask = setInterval(updateUITime, 900);
}


function updateUITime(){
	var curTime = new Date();

	//更新界面UI 
	_remainTime = _remainTime - (curTime.getTime() - lastUpdateUITime.getTime());
	//如果还有超过2分钟 的时间 则正计时
	if(_remainTime > 2*60*1000 ){
	   //更新UI
		calculateUITime(_remainTime,0);
    //如果 不到 2分钟 则倒计时
	} else if(_remainTime >= 0 ){
		//更新UI
		calculateUITime(_remainTime,-1);
    //超过 了订单时间 超过时间
	} else {
		//更新UI
		calculateUITime(_remainTime,1);
		
	}
	
	lastUpdateUITime = curTime;
	//如果是医生 还需要更新时间到后台
	if(_utype == '1'){
		
		//如果大于 10 秒则更新到后台
		if(curTime.getTime() - lastUpdateTime.getTime() >= 10 * 1000){
			//调用后台函数
			updateVedioTime(Math.round((curTime.getTime() - lastUpdateTime.getTime())/1000));
			lastUpdateTime = curTime;
		}
	}	
	
}

function calculateUITime(remainTime,direction){
	var timdiv = document.querySelector('#timers');
	if(direction == 0){
		remainTime = _consultationdur*60*1000-remainTime;
	}
	
	remainTime = Math.abs(remainTime);
	
	//计算出小时数   
    var hours=Math.floor(remainTime/(3600*1000));  
    //计算相差分钟数  
    var leave2=remainTime%(3600*1000);   //计算小时数后剩余的毫秒数  
    var minutes=Math.floor(leave2/(60*1000));  
    //计算相差秒数  
    var leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数  
    var seconds=Math.floor(leave3/1000);
    
  
    var tempTime = (hours > 9 ? hours : "0" + hours) + ":" + (minutes > 9 ? minutes : "0" + minutes)  +":" + (seconds > 9 ? seconds : "0" + seconds) ;
    
	//倒计时
	if(direction == -1){
			
		//首次倒计时 提醒
		if(timdiv.innerHTML.indexOf('已通话') != -1){
			 //语音提醒  还剩几分钟，请抓紧时间提问
			//generateAudio('您本次会诊还剩' + minutes + '分钟，请抓紧时间。');
			//遮罩提醒
			 var  mesg = '您本次会诊还剩' + minutes + '分钟，请抓紧时间。';
			 showOverTime(mesg);
		}
		
		tempTime = "<span>倒计时：</span> " + tempTime;
		timdiv.style.width = '100%';
		timdiv.style.fontSize = '24px';

	//正即时
	} else if(direction == 0){
			
		tempTime = "<span>已通话：</span>" +  tempTime;
	//超时
	} else {
		
		//首次超时时 提醒
		if(timdiv.innerHTML.indexOf('倒计时') != -1){
			 //语音提醒 已到时，请抓紧时间收尾
			generateAudio('您本次会诊时间已结束，请抓紧时间收尾。');
		}
		
		tempTime = "<span>已超时：</span> " + tempTime;

	    timdiv.style.color = 'red';
	    timdiv.style.opacity = '1';
		timdiv.style.fontSize = '24px';
		timdiv.style.width = '100%';
		
		//超时每超过5分钟 界面弹出提示框,需要更改
		if(_utype == '1'){
			
			var curOverTime = Math.floor(remainTime/(5*60*1000));
			if( curOverTime != lastOverTime){
			    lastOverTime = curOverTime;
			    
			    //alert('您已超时' +curOverTime*5 + '分钟'); 休要重新修改，要影响使用
			    var mesg = '您已超时' +curOverTime*5 + '分钟！';
			    showOverTime(mesg);
			}
		}
		
		
	}
	timdiv.innerHTML = tempTime;
	timdiv.style.display = 'block';
	//console.log(tempTime);//更新界面中的时间显示，需要更改	
}

function showOverTime(msg){
	seajs.use('view/base',function(bas){
		bas.showCustomDialog({
			id:'showOverTime',
			cls:'modal2-400',
			title:'超时提醒',
			text:'<p style="padding:20px 0 0;font-size: 16px;line-height: 1.5em;">'+ msg +'</p>',
			ok:function(){				
				endwaitvideo();//关闭视频
				bas.hideDialog('showOverTime');
			}
		});	
		$('#showOverTime').on('hide.bs.modal', function (e) {
			$('body').removeClass('modal2-open')
		});
	});	
}
function stopTimerTask(){
	if(timerTask){
		clearInterval(timerTask);
		timerTask = null;
	}
	
}

// 创建 offer失败
function createOfferError(error) {
	localVideo.pause();
	miniVideo.pause();
	remoteVideo.pause();
	pc.close();
	pcstarted = false;
	console.log('Failed to createOffer message : ' + error);
}

function createAnswerError(error) {
	localVideo.pause();
	miniVideo.pause();
	remoteVideo.pause();
	pc.close();
	pcstarted = false;
	console.log('Failed to createAnswer message : ' + error);
}

function onSdpSuccess() {
}

function onSdpError(e) {
	console.error('sdp error:', e);
}

// ////////////////////////////////////////////////////////////
// 设置状态
function setNotice(msg) {
	footer.style.display = 'block';
	firsttip.style.display = 'none';
	if (msg.indexOf('已离开') != -1) {

	} else if (msg.indexOf('已断开') != -1) {

	} else if (msg.indexOf('连接成功') != -1) {
		window.setTimeout(function() {
			footer.style.display = 'none';
			_gcontroller && _gcontroller.showVideo('small').showForm('show');
		}, 600);
	} else if (msg.indexOf('连接中') != -1) {

	} else if (msg.indexOf('初始化') != -1) {

	} else if (msg.indexOf('关闭') != -1) {
		footer.style.display = 'none';
	}
	footer.innerHTML = '<label>' + msg + '</label>';
}

// 处理消息
function processSignalingMessage(message) {
	var msg = JSON.parse(message);
	if (msg.type === "offer" && !pcstarted) {
		createAnswerConnection(msg);
		// 收到专家的回答信息
	} else if (msg.type === "answer" && pcstarted) {
		pc.setRemoteDescription(new RTCSessionDescription(msg), onSdpSuccess, onSdpError);

	} else if (msg.type === "candidate" && pcstarted) {
		var candidate = new RTCIceCandidate({
			sdpMLineIndex : msg.label,
			candidate : msg.candidate
		});
		pc.addIceCandidate(candidate);
		// 断开连接
	} else if (msg.type === "bye" && pcstarted) {
		setNotice("对方已断开！");
		onRemoteClose();
	} else if (msg.type === "nowaiting") {
		onRemoteClose();
		setNotice("对方已离开！");
	} else if (msg.type === 'chart' && pcstarted) {
		chartmsg(msg.txt, 'doc');
	
	//从医生端进行音视频的切换
	} else if(msg.type === "switch" /*&& pcstarted*/){
		
		pcstarted = false;
		onlyAudio = msg.streamtype;
		
		if(pc){
			pc.close();
			pc = null;
		}
		
		if(onlyAudio){	
			setNotice("医生正切换到音频模式");
		} else {
			setNotice("医生正切换到视频模式");
		}
	
		
		console.log("switch");
	}
	
}

// 远程视频关闭
function onRemoteClose(bol) {
    stopTimerTask();//关闭计时器
     
	miniVideo.style.display = "none";
	remoteVideo.style.display = "none";
	localVideo.style.display = "inline-block";
	main.style.webkitTransform = "rotateX(360deg)";

	pcstarted = false;
	forceclose = bol;

	closevideo();
	if (pc) {
		pc.close();
		pc = null;
		console.log("force pc close");
	}
	if (pcsocket &&  pcsocket.readyState == 1 /*&& bol*/) {
		pcsocket.close();
		pcsocket = null;
		console.log("force socket close");
	}
	
	if (joinRoom) {
		// 发送结束通知，关闭分享屏幕
		if(_utype == '2'){
			closeshare();
		}

		//发送结束通知，
		console.log('removeRoomUser');
		removeRoomUser(function(d) {
		});
	}


	$('#reporttxt').addClass('center').slideDown();
	$('#reportbtn').fadeOut();

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
	_utype == '1' && !bSwitch &&  openwin();
	console.log("连接成功!");
	if(!onlyAudio){
		setNotice("连接成功！");
	} else {
		setNotice("语音通话中");
	}
	openvideo();
}

// 全屏
function fullScreen() {
	main.webkitRequestFullScreen();
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
		//videotiptxt.innerHTML = "来自医生的远程视频";
		videotiptxt.innerHTML = '<label>专家已经在其他地方发起了，可以重新发起</label>';
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
	if(_utype == '1'){
		btnswitch.innerHTML = initiatorVideo ? '接受视频问诊' : '发起视频问诊';
	} else {
		btnswitch.innerHTML =  '发起视频问诊';
	}

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

	//pictxtdialog.style.display = 'block';
	dialogBtn && (dialogBtn.style.display = 'inline-block');
	_connect = true;
	
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
	//pictxtdialog.style.display = 'none';
	dialogBtn && (dialogBtn.style.display = 'none');
	_connect = false;
	
	barspan.innerHTML = '远程会诊已完成';
	
	if (this.localVideoUrl) {
		URL.revokeObjectURL(this.localVideoUrl);
	}
	
	if (remoteVideo.src) {
		URL.revokeObjectURL(remoteVideo.src);
	}

	localVideoUrl = null;
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

	stopTimerTask();//关闭计时器
	
	sendMessage({
		type : "bye",
	});

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
	
	if (this.localVideoUrl) {
		URL.revokeObjectURL(this.localVideoUrl);
	}
	
	if (remoteVideo.src) {
		URL.revokeObjectURL(remoteVideo.src);
	}
	
	localVideoUrl = null;
	miniVideo.src = "";
	remoteVideo.src = "";
	localVideo.src = '';
	localAudio.src = _href + 'audio/wx_video_off.mp3';
	localAudio.loop = false;

	pcstarted = false;
	forceclose = true;
	if (pc) {
		pc.close();
		pc = null;
		console.log("force pc close");
	}
	if (pcsocket && pcsocket.readyState == 1) {
		pcsocket.close();
		pcsocket = null;
		console.log("force socket.close");
	}

	if (joinRoom) {
		// 发送结束通知，关闭分享屏幕
		if(_utype == '2'){
			closeshare();
		}

		//发送结束通知，
		console.log('removeRoomUser');
		removeRoomUser(function(d) {
		});
	}

	$('#reporttxt').addClass('center').slideDown();
	$('#reportbtn').fadeOut();
	_connect = false;
	
	stopRecording();

}

function closeshare() {
	$.post(_burl + "doctor/closeshare", {orderid : _roomKey}, function(data) {});
}

function onlevelpage() {
	
	//关闭定时任务
	stopTimerTask();
	
	sendMessage({ type : "bye" });
	localAudio && localAudio.pause();// 停止播放音域；
	if (pc) {
		/*localStream.getTracks().forEach(function(track) {
		    track.stop();
		});*/
		pc.close();
		pc = null;
		console.log("force pc close");
	}
	if (pcsocket && pcsocket.readyState == 1) {
		pcsocket.close();
		pcsocket = null;
		console.log("force socket.close");
	}
	window.parent['_sid'] = '';

	// 确保推出房间
	if (joinRoom) {		
		// 发送结束通知，关闭分享屏幕
		if(_utype == '2'){
			closeshare();
		}
		console.log('removeRoomUser');
		removeRoomUser(function(d){});
	}
	
	stopRecording();
	window.parent.minMenu && window.parent.minMenu(false);
}
$(document).ready(function() {
	window.onbeforeunload = onlevelpage;
	window.parent.minMenu && window.parent.minMenu(true);
});
function videoClick() {
	footer.style.display = 'none';
	firsttip.style.display = 'none';
	if (_protag == '4' && btnswitch.innerHTML.indexOf('视频问诊') != -1) {
		addRoomUser(function(d) {
			if (d.status == 'error') {
				setNotice(d.message);
			} else {
				
				//重新加载时间
				_vedioTime = d.vedioDur;
				_remainTime =  (_consultationdur*60 - _vediotime)*1000;
				
				initialize();
				waitvideo();
			}
		});// 用户进入房间
	} else if (btnswitch.innerHTML.indexOf('结束') != -1) {
		endwaitvideo();
	}
}
///////////////////////录制///////////////////////////////////////
var filename;
var position;

function startRecording() {
	  filename = "";
	  position = 0;
	  cansend = true;
	  recordedBlobs = [];
	 
	  var options = {mimeType: 'video/webm;codecs=vp9'};
	  if (!MediaRecorder.isTypeSupported(options.mimeType)) {
	    console.log(options.mimeType + ' is not Supported');
	    options = {mimeType: 'video/webm;codecs=vp8'};
	    if (!MediaRecorder.isTypeSupported(options.mimeType)) {
	      console.log(options.mimeType + ' is not Supported');
	      options = {mimeType: 'video/webm'};
	      if (!MediaRecorder.isTypeSupported(options.mimeType)) {
	        console.log(options.mimeType + ' is not Supported');
	        options = {mimeType: ''};
	      }
	    }
	  }
	  try {
	    mediaRecorder = new MediaRecorder(remoteStream, options);
	  } catch (e) {
	    console.error('Exception while creating MediaRecorder: ' + e);
	    alert('Exception while creating MediaRecorder: '
	      + e + '. mimeType: ' + options.mimeType);
	    return;
	  }
	  console.log('Created MediaRecorder', mediaRecorder, 'with options', options);
	  mediaRecorder.onstop = handleStop;
	  mediaRecorder.ondataavailable = handleDataAvailable;
	  mediaRecorder.start(1000); // collect 1 second of data
	  console.log('MediaRecorder started', mediaRecorder); 
}
 
function stopRecording() {
	
	if(!!mediaRecorder){
		
	    if (mediaRecorder.state === 'recording') {
	    	
	    	//触发ondataavailable
            //mediaRecorder.requestData();
            
        	//停止录制
	    	//触发ondataavailable
    		mediaRecorder.stop();
          
        }
		mediaRecorder =null;    
	}
}

function handleStop(event) {
    console.log('Recorder stopped: ', event);
    //不分片保存
	upblob();
	
	//分片
	//有数据
	/*if(recordedBlobs.length > 0 && cansend){                          
	    upblobslice(1);
	//没数据    
	} else {
	    upblobslice(2);  
	}
    
    filename = "";
    position = 0;*/
}
 
//疑问， 调用stop后竟然多次触发，不知道为什么
function handleDataAvailable(event) {
    if (event.data && event.data.size > 0) {   
    	recordedBlobs.push(event.data);
    	
    	/*//分片保存
    	if(recordedBlobs.length > 30 && cansend){
	        upblobslice(0);
    	} */          
    } 
}
 
//分片
function upblobslice(end) {
	
	  var blob = new Blob(recordedBlobs, {
         type: 'video/webm'
      });
	  
	  cansend = false;
	  recordedBlobs = [];

	//方式一 上传到服务器
	/*console.log('upblobslice:', 'end'+end);
	  var fd=new FormData();
	  fd.append('file',blob);
	  fd.append('oid',_roomKey);
	  fd.append('filename',filename);
	  fd.append('position',position);
	  fd.append('end',end);
	  $.ajax({  
		  url:_burl+"doctor/upblobslice", 
        type: 'POST',  
        data: fd,    
        cache: false,  
        contentType: false,  
        processData: false,  
        success: function (returndata) {  
            //alert(returndata); 
        	filename = returndata.filename;
        	position = returndata.nextPosition;
        	cansend = true;
        	console.log('upblob-success:returndata', JSON.stringify(returndata, null, '\t'));
        },  
        error: function (returndata) {  
        	cansend = true;
            console.log('upblob-error:returndata', JSON.stringify(returndata, null, '\t'));
        }  
   });*/
	  
	//方式二 保存到本地
	 if(end == 2){
		return;
	}
	var url = window.URL.createObjectURL(blob);
	var a = document.createElement('a');
	a.style.display = 'none';
	a.href = url;
	a.download = _roomKey + "_" +new Date().Format("yyyyMMddhhmmssS")  + '.webm';
	document.body.appendChild(a);
	a.click();
	setTimeout(function() {
		document.body.removeChild(a);
		window.URL.revokeObjectURL(url);
		cansend = true;
	}, 100);
	
}

//不分片
function upblob() {
	  
	  var blob = new Blob(recordedBlobs, {
       type: 'video/webm'
      });
	 
	  cansend = false;
	  recordedBlobs = [];

	 //方式一 上传到服务器
	  
	 /* var fd=new FormData();
	  fd.append('file',blob);
	  fd.append('oid',_roomKey);
	  
	  var base = seajs.require('view/base');
  	  base.showTipIngA('视频正在上传，请耐心等待,不要退出订单或关闭浏览器！');
	  $.ajax({  //使用base 中的
		  url:_burl+"doctor/upblob", 
      type: 'POST',  
      data: fd,    
      cache: false,  
      contentType: false,  
      processData: false,  
      success: function (returndata) {  
    	base.hideTip();
      	console.log('upblob-success:returndata', JSON.stringify(returndata, null, '\t'));
      },  
      error: function (returndata) {  
    	  base.hideTip();
          console.log('upblob-error:returndata', JSON.stringify(returndata, null, '\t'));
      }  
     });*/
	
	
	//方式二 保存到本地
	
	var url = window.URL.createObjectURL(blob);
	var a = document.createElement('a');
	a.style.display = 'none';
	a.href = url;
	a.download = _roomKey + "_" +new Date().Format("yyyyMMddhhmmssS")  + '.webm';
	document.body.appendChild(a);
	a.click();
	setTimeout(function() {
		document.body.removeChild(a);
		window.URL.revokeObjectURL(url);
	}, 100);
	
}
// ///////////////////////////////////////////////////////////
// 如果是专家
(function() {
	if (_utype == '1') {
		return;
	}
	var channel = 'room' + _roomKey;
	var sender = 'user' + _uid;
	console.log('create Screen:room:' + _roomKey);
	screensharing = new Screen(channel);
	screensharing.userid = sender;
	screensharing.isModerator = true;

	// custom signaling channel
	// you can use each and every signaling channel
	// any websocket, socket.io, or XHR implementation
	// any SIP server
	// anything! etc.
	
	 screensharing.openSignalingChannel = function(callback) {  var socket1 = io.connect('https://socketio.51zjh.com/'); socket1.on('message', callback); return socket1; };
	 

	/* var SIGNALING_SERVER = 'https://socketio.51zjh.com/';
	 io.connect(SIGNALING_SERVER).emit('new-channel', {
	     channel: channel,
	     sender: sender
	 });

	 var socket1 = io.connect(SIGNALING_SERVER + channel);
	 socket1.send = function (message) {
	     socket1.emit('message', {
	         sender: sender,
	         data: message
	     });
	 };

	 screensharing.openSignalingChannel = function(callback) {
		 socket1.on('message', callback);return socket1;};*/
	 

	screensharing.onscreen = function(_screen) {
		console.log('onscreen:' + _screen.userid);
	};

	screensharing.onaddstream = function(media) {
		console.log('onaddstream:' + media.userid);
	};
	screensharing.onuserleft = function(userid) {
		console.log('onuserleft:' + userid);
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
	screensharing.share();
	console.log('sharetodoc:' + screensharing.userid);
}


//从视频切换到音频 或从音频切换到视频
function switchVideoAudio(bAudio){
	// 医生
	if (_utype == '1') {
			
		if (localStream && channelReady){
			
			sendMessage({type : "switch",streamtype:bAudio});
			pcstarted = false;
			onlyAudio = bAudio;
			bSwitch = true;
			if(pc){
				pc.close();
				pc = null;
			}
			createOfferConnection();
		}
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

//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) {
 var o = {
     "M+": this.getMonth() + 1, //月份 
     "d+": this.getDate(), //日 
     "h+": this.getHours(), //小时 
     "m+": this.getMinutes(), //分 
     "s+": this.getSeconds(), //秒 
     "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
     "S": this.getMilliseconds() //毫秒 
 };
 if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
 for (var k in o)
 if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
 return fmt;
}
