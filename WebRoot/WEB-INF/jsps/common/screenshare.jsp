<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html>

<head>
	<base href="/">
	<title>来自专家端的屏幕分享</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<style>
		html,
		body,
		.experiment,
		#videos-container {
			height: 100%;
			overflow: hidden;
			margin: 0;
		}

		#videos-container {
			background: url(/img/loading/31.gif) no-repeat center center;
			min-height: 200px;
		}

		#videos-container video {
			width: 100%;
			height: 100%;
			background-color: #000;
		}
	</style>
</head>

<body>
	<div class="github-stargazers"></div>
	<section class="experiment">
		<table style="width: 100%;" id="rooms-list"></table>
		<div id="videos-container"></div>
	</section>
	<script src='/app/js/app.config.seed.js'></script>
	<script src='/js/screenshare/socketio.js'></script>
	<script src='/js/screenshare/firebase.js'></script>
	<script src="/js/screenshare/getScreenId.js"></script>
	<script src="/js/screenshare/BandwidthHandler.js"></script>
	<script src="/js/screenshare/screen.js"></script>
	<script type="text/javascript">
		var videosContainer = document.getElementById("videos-container") || document.body;
		var roomsList = document.getElementById('rooms-list');

		var channel = 'room' + '${_roomKey}';
		var sender = 'user' + '${_user}';
		var socket1 = io.connect(window.SocketIOSrc);
		var screensharing = new Screen(channel);

		screensharing.userid = sender;

		screensharing.openSignalingChannel = function (callback) {
			socket1.on('message', callback);
			return socket1;
		};

		screensharing.onscreen = function (_screen) {
			console.log('onscreen1:' + _screen.userid);
			var alreadyExist = document.getElementById(_screen.userid);
			if (alreadyExist) {
				console.log('alreadyExist:' + _screen.userid);
				return;
			}

			if (typeof roomsList === 'undefined') roomsList = document.body;
			var tr = document.createElement('tr');
			tr.id = _screen.userid;
			roomsList.insertBefore(tr, roomsList.firstChild);

			console.log('onscreen2:' + _screen.userid);
			screensharing.view(_screen);

		};

		function showexpert() {
			var _screen = {
				userid: _userid,
				roomid: _room
			};
			screensharing.view(_screen);
		}

		screensharing.onaddstream = function (media) {
			console.log('onaddstream:' + media.userid);
			media.video.id = media.userid;
			var video = media.video;
			video.setAttribute('controls', true);
			//video.setAttribute('style',"");
			videosContainer.insertBefore(video, videosContainer.firstChild);
			video.play();
		};
		screensharing.onuserleft = function (userid) {
			console.log('onuserleft:' + userid);
			var video = document.getElementById(userid);
			if (video && video.parentNode) video.parentNode.removeChild(video);
		};
		screensharing.onNumberOfParticipantsChnaged = function (numberOfParticipants) {
			if (!screensharing.isModerator) return;

			console.log('onNumberOfParticipantsChnaged:' + numberOfParticipants);

			document.title = numberOfParticipants + ' users are viewing your screen!';
			var element = document.getElementById('number-of-participants');
			if (element) {
				element.innerHTML = numberOfParticipants + ' users are viewing your screen!';
			}
		};
		// check pre-shared screens
		screensharing.check();
	</script>
	<script type="text/javascript" src="https://cdn.goeasy.io/goeasy.js"></script>
	<script type="text/javascript">
		var _burl = '/';
		var goEasy = new GoEasy({
			appkey: '78028e7e-edcc-4524-b56b-45639785a53a'
		});
		goEasy.subscribe({
			channel: '${_roomKey}' + '_close',
			onMessage: function (message) {
				var content = message.content;
				if (window.self != window.top) {
					location.href = _burl + 'html/screenshare.html?state=closed';
					return false;
				}
				if (content == 'close') {
					window.close();
				}
			}
		});
	</script>
</body>

</html>