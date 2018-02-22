<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="/">
    
    <title>测试视频录像</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <link rel="stylesheet" href="style.css">
    <style>
    audio {
        vertical-align: bottom;
        width: 10em;
    }
    video {
        max-width: 100%;
        vertical-align: top;
    }
    
    input {
        border: 1px solid #d9d9d9;
        border-radius: 1px;
        font-size: 2em;
        margin: .2em;
        width: 30%;
    }
    
    p,
    .inner {
        padding: 1em;
    }
    
    li {
        border-bottom: 1px solid rgb(189, 189, 189);
        border-left: 1px solid rgb(189, 189, 189);
        padding: .5em;
    }
    
    label {
        display: inline-block;
        width: 8em;
    }
    </style>

  </head>
  
  <body>
    <article>
        <div class="github-stargazers"></div>
        <section class="experiment">
            <button id="start">Start</button>
            <button id="stop" disabled>Stop</button>
            <button id="save" disabled>Save</button>
            <div id="videos-container"></div>
            <audio id="audio"></audio>
        </section>   
    </article>
    <script src="/js/adapter.js"></script>
    <script src="js/RecordRTC.js"></script>

        <script>
        var constraints = { audio: true,video: true};
        //navigator.getUserMedia  = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
        function getUserMedia() {
			//navigator.getUserMedia(constraints, onUserMediaSuccess, onUserMediaError);
			
			navigator.mediaDevices.getUserMedia(
			  constraints
			).then(
				onUserMediaSuccess,
				onUserMediaError
			);
		}
        function onUserMediaError(error){}
		function onUserMediaSuccess(stream) {
           var video = document.createElement('video');
           video.src = URL.createObjectURL(stream);
           video.play();
          mRecordRTC.addStream(stream);
          mRecordRTC.startRecording();     
		}
        var mRecordRTC = new MRecordRTC();
        mRecordRTC.mediaType = {
            audio: true, 
            video: true
        };
        document.querySelector('#start').onclick = function() {
            this.disabled = true;
            document.querySelector('#stop').disabled=false;
            getUserMedia();
        };
        document.querySelector('#stop').onclick = function() {
            this.disabled = true;
            mRecordRTC.stopRecording(function(url, type) {
                mRecordRTC.writeToDisk();
                save.disabled = false;
            });
        };
        document.getElementById('save').onclick = function() {
            this.disabled = true;
             document.querySelector('#start').disabled=false;
            mRecordRTC.save();
        };
        </script>
  </body>
</html>
