<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>天使陪诊</title>
    <meta name="viewport" content="width=device-width,  user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
    <style>
		.index{padding:22px 15px 0;background-color:#fff;box-sizing:border-box;}
    	.center{margin:8px; padding:0 10px 12px;background-color:#fff;border-radius:5px;}
    	.buttonClasswhite{color:#444;font-weight:500;}
		.buttonClass26B5ED{box-shadow: 3px 3px 14px #bbb;height:46px;}
		a.buttonClass26B5ED{text-align:center;line-height:46px;display:inline-block;}
    </style>    
    <script src="/libs/jquery-1.11.0.min.js"></script>
    <script src="/js/jweixin-1.0.0.js"></script>
</head>
<body>
	<div id="index" class="index">
		<section>
			<dl>
				<dd style="border:0;text-align:center;line-height:200px;font-size:20px;font-weight:600;color:#26B5ED;">
					恭喜您，认证成功！
				</dd>
				<dt style="padding: 20px 0 15px;font-size:0;">
					<a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.yeevit.hsb" class="buttonClass26B5ED" >赶快去下载APP吧</a>
				</dt>
			</dl>
		</section>
	</div>
    <script>
    	wx.config({
            appId: "${appid}",
            timestamp: "${timestamp}",
            nonceStr: "${nonceStr}",
            signature: "${signature}",
            jsApiList: [
              'closeWindow'
            ]
         });
		function closeWX(){
			wx.closeWindow();
		}
		(function(){	
			function onBridgeReady(){
				WeixinJSBridge.call('hideOptionMenu');
			}
			if (typeof WeixinJSBridge == "undefined"){
			    if( document.addEventListener ){
			        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
			    }else if (document.attachEvent){
			        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
			        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			    }
			}else{
			    onBridgeReady();
			}
		})(document);
    </script>    
</body>
</html>