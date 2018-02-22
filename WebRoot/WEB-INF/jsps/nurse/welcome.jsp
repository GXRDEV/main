<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>看病找谁</title>
    <meta name="viewport" content="width=device-width,  user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
    <style>
    	html,body,#index{height:100%;box-sizing:border-box;}
    	#index{background:#E4E3E2 url(/img/mobile/kbzs/v1.0/bg_welcome.png) no-repeat 0 0;background-size:100% auto;position:relative;min-height:7em;}
    	a{font-size:0.65rem;color:#57C1CF;display:inline-block;padding:5px 15px;border:1px solid #57C1CF;
    		border-radius:5px;position:absolute;right:20px;top: 50%;margin-top: -0.8em;font-weight:500}
    	.g_fixed{font-size:0.8rem;padding:0.8em 6em 1em 2.8em;
    		background:url(/img/mobile/kbzs/v1.0/icon_logo@2x.png) no-repeat 0.6em center;background-size: 1.8em auto;}
    </style>
</head>
<body>
	<div id="index">
		<div style="position:absolute;top:50%;margin-top:-3rem;left:0.7rem;">
			<p style="color:#57C1CF;font-size:0.94rem;font-weight:600;line-height:1.8em;">做一名美丽的护士，<br/>助人悦已。</p>
			<p style="font-size:0.53rem;color:#B4AEAE;line-height:1.6em;padding:16px 0;">为自己多一些收入，为人生多一条精彩的路。</p>
			<a style="position:static;margin-top:0.4em;font-weight:300" href="javascript:alert('开发中...');">下载护士宝</a>
		</div>
		<div class="g_fixed">
			<p style="font-weight:600;color:#57C1CF;padding:6px 0;">护士宝</p>
			<p style="font-size:0.4rem;color:#B4AEAE;">专业医疗陪诊-护士专用</p>
			<a href="/wtspz/tonurseregister.do">立即注册</a>
		</div>
	</div>
    <script>
		(function(){
		    function o(){document.documentElement.style.fontSize=(document.documentElement.clientWidth>414?414:document.documentElement.clientWidth)/12+"px"}
		    var e=null;
		    window.addEventListener("resize",function(){clearTimeout(e),e=setTimeout(o,300)},!1),o()
		})(window);
    </script>    
</body>
</html>