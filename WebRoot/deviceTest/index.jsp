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
	
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="//cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

</head>
<body>	
	<div class="outerDiv">
		<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
		  <div class="container-fluid">
		    <!-- Brand and toggle get grouped for better mobile display -->
		    <div class="navbar-header">
		      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
		        <span class="sr-only">Toggle navigation</span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		      </button>
		      <a class="navbar-brand" href="#">
		        <img alt="Brand" src="/img/icons/zjh-24.png">
		      </a>
		    </div>
		
		    <!-- Collect the nav links, forms, and other content for toggling -->
		    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
		      <ul class="nav navbar-nav">
				<li class="active"><a href="chrome.jsp" target="iframe-test">浏览器环境</a></li>
				<li><a href="audio.jsp" target="iframe-test">耳机/音响测试</a></li>
				<li><a href="volumn.jsp" target="iframe-test">话筒测试</a></li>
				<li><a href="vedio.jsp" target="iframe-test">摄像头测试</a></li>
				<li><a href="speed.jsp" target="iframe-test">网络测试</a></li>
		      </ul>
		      <ul class="nav navbar-nav navbar-right">
		        <li><a href="tel:400-631-9377">客服电话：400-631-9377</a></li>
		      </ul>
		    </div><!-- /.navbar-collapse -->
		  </div><!-- /.container-fluid -->
		</nav>
		<section class="navbar-fixed-bottom" style="border:1px solid #e7e7e7;top:50px">
			<iframe id="iframe-test" name="iframe-test" src="chrome.jsp" style="width:100%;height:100%;" frameborder="no" border="0" allowscriptaccess="always" marginwidth="0" marginheight="0" allowfullscreen="true" wmode="opaque" allowTransparency="true" type="application/x-shockwave-flash"></iframe>
		</section>
	</div>
	<script>
		$(document).ready(function(){
			$('.navbar-nav:first li').click(function(){
				$(this).addClass('active').siblings().removeClass('active');
			});
		});
	</script>
</body>
</html>