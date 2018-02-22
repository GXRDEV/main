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
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">
    <style>
    	body{text-align:center;}
    	.exlist{display:inline-block;text-align:left;margin:2em 0;}
    	.exlist li{font-size:1.3em;line-height:2em;}
    	.passed,.passed a{color:blue;}
    	.unpassed,.unpassed a{color:red;}
    	li a{opacity:.5;}
    </style>
</head>
<body>	
	<div class="exlist">
		<ul>
			<li>
				<script type="text/javascript">
					(function(){
						var userAgent = navigator.userAgent.toLowerCase(),
							uaMatch = userAgent.match(/chrome\/([\d.]+)/) || ['','0'],
							v = uaMatch[1];
						if (uaMatch != null && v > '46') {
			                document.write('<span class="passed">Chrome浏览器(V'+ v +'),测试通过。</span>');        
			            }else{
			            	document.write('<span class="unpassed">测试失败，请使用新版的<a target="_blank" href="https://www.google.com/chrome">Chrome浏览器</a>。</span>');
			            }		
					})()
				</script>
			</li>
			<li>
				<script type="text/javascript">
					(function(){
						if(document.createElement('video').canPlayType){
							document.write('<span class="passed">Video 测试通过。</span>');
						}else{
							document.write('<span class="unpassed">Video 测试失败，建议使用新版的<a target="_blank" href="https://www.google.com/chrome">Chrome浏览器</a>。</span>');
						}
					})();
				</script>
			</li>
			<li>
				<script type="text/javascript">
					(function(){
						var RTCPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;					
						if (!WebSocket) {
							document.write('<span class="unpassed">WebSocket 测试失败，建议使用新版的<a target="_blank" href="https://www.google.com/chrome">Chrome浏览器</a>。</span>');
						} else if (!RTCPeerConnection) {
							document.write('<span class="unpassed">RTCPeerConnection 测试失败，建议使用新版的<a target="_blank" href="https://www.google.com/chrome">Chrome浏览器</a>。</span>');
						} else{
							document.write('<span class="passed">WebSocket 测试通过。</span>');
						}
					})()
				</script>
			</li>
			
		</ul>
	</div>
</body>
</html>