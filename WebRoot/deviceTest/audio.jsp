<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>设备检测</title>
    <meta name="viewport" content="width=device-width,  user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
    <!-- 新 Bootstrap 核心 CSS 文件 -->
	<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="//cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
    <style>
    	body{text-align:center;}
    	.exlist{text-align:left;display:inline-block;padding:2em 0;}
    	.playau audio,.playau button{float:left;}
    	
	   	#text{margin-top:1.6em;list-style: disc ;}
	   	#text dt{font-size:1.2em;padding:5px 0 0.3em;}
	   	#text dd{line-height:1.8em;display: list-item;margin-left: 1.5em;}
    </style>
</head>
<body>	
	<div class="exlist">
		<div class="playau clearfix">
			<audio id="gumlocal" controls="" autoplay style="width:37em;margin-right:1em;"></audio>
			<button type="button" onclick="playvideo()" class="btn btn-primary">播放</button>		
		</div>
      	<dl id="text" class="bg-info" style="padding:1em">   
			<dt>提示：</dt>
			<dd>当您能听到这段文字时，说明耳机或者音响测试通过。您可以进行其它测试。</dd>
			<dd>如果您没有听到任何声音，请查看音量大小，或者是否静音，或者音频驱动是否正常。</dd>
			<dd>如果未解决，请联系我们的客服。</dd>
		</dl>
	</div>
	<script type="text/javascript">
		var  _burl = '/';
		function playvideo(){
			var text = $('#text dd').text();
			text && $.post(_burl + "doctor/generateAudio", {
				content : text
			}, function(data) {
				var lol = document.getElementById('gumlocal');
				lol.src = data.url;
				lol.loop = false;
			});
		}
	</script>
</body>
</html>