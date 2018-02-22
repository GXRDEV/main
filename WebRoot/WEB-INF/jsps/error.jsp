<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>404/500</title>
    <meta name="viewport" content="width=device-width,  user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
    <style>
    	body{background-color: rgb(246, 246, 246);font-size:0.14rem;}
		a {text-decoration: none;}
		a:link {color: #666;}
		a:visited {color: #666;}
    	.cGray, .cGray a:link, .cGray a:visited { color: #666;}

    	.index{position:relative; width:620px;margin:10% auto;}
    	.index img{max-width:100%;}
    	.index .editor{position:absolute;top: 24%;left: 15%; width: 40%;}
    	.tips404 .h2{line-height:1.5em;color:#888;}
    	.tips404 .h3{text-align:right;padding-right:0.6em;}
    	.tips404 h3 .back{display:inline-block; background: url(http://y2.ifengimg.com/8cbe73a7378dafdb/2014/0715/icon.gif) no-repeat 0 -26px;padding-left: 18px;}
    	@media (max-width: 620px) {	
    		body{font-size:0.12rem;}
    		.index{position:absolute;width:100%;margin:0 auto; top:50%;left:0;transform:translateY(-50%);-webkit-transform:translateY(-50%);}
    		.index .editor{top: 15%;}
    		.index p{margin:0.5em 0;}
    	}
    </style>
    <script type="text/javascript">
  		!function(n){var e=n.document,t=e.documentElement,i=414,d=i/100,o="orientationchange"in n?"orientationchange":"resize",a=function(){var n=t.clientWidth||320;n>414&&(n=414),t.style.fontSize=n/d+"px"};e.addEventListener&&(n.addEventListener(o,a,!1),e.addEventListener("DOMContentLoaded",a,!1))}(window);
  	</script>
</head>
<body>	
	<div class="index tips404">
		<img src="/img/500.png" alt="异常"/>
		<div class="editor">
			<h1>异常</h1>
			<p class="h2">页面加载出面异常，请重试或者联系管理员</p>
			<p class="h3 cGray">
				<a class="back" href="javascript:history.go(-1);">返回</a>
			</p>
		</div>
	</div>
</body>

</html>