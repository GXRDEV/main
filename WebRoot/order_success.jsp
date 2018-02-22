<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>我的订单</title>
    <meta name="viewport" content="width=device-width,  user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/libs/jquery.mobile-1.4.5.min.css">
    <style>
    	*{padding:0;margin:0;}
    	#index{width:600px;margin:0 auto;}
    	.top{background-color:#E6E6E6;line-height:60px;font-size:20px;font-family:"SimHei";text-align:center;font-weight: 700;}
    	.info dt{color:#2E9AB7;font-size:18px;padding:1em 0 4px;font-weight: 700;}
    	.info dd{padding-left:10px;font-size:14px;line-height:2em;}
    	.center{margin:20px 10px 6px;background-color:#fff;border-radius:5px;line-height:100px;text-align:center;color:#20B101;font-size:24px;}
    	.ui-page-theme-a .footer .ui-btn {
			border: 0;
			background: #20B101;
			font-size:18px;
			color: #fff;
			text-shadow: none;
			margin:4px 0;
		}
		.ui-page-theme-a .footer .ui-btn:hover {
			box-shadow: none;
			text-shadow: none;
			color: #fff;
			background: #20B101;
		}
    	@media (max-width: 600px) {	
			#index{width:100%;margin:0 auto;}
		}
    </style>    
    <script src="/libs/jquery-1.11.0.min.js"></script>
    <script src="/libs/jquery.mobile-1.4.5.min.js"></script>
    <script src="/js/jweixin-1.0.0.js"></script>
</head>
<body>
	<div id="index">
		<div class="center">
			<img style="margin-bottom:-7px;" src="/images/ok32.png" />支付成功
		</div>
		<div class="footer" style="margin:0 4px;">
			<input type="button" value="关闭并返回聊天界面" class="ui-btn" data-corners="false" data-shadow="false" name="submit" id="submit"/>
		</div>
	</div>
    <script>
		wx.config({
            appId: 'wx92dac83f47209d85',
            timestamp: 1422586106,
            nonceStr: '38ncic762ocquc7u9csfoaqtog21dm2k',
            signature: '89C2BDB122739FE212F86F0C5CFC63D0',
            jsApiList: [
              'closeWindow'
            ]
          });
          wx.ready(function () {            
            $(document).ready(function(){
	            $('#submit').click(function(){
	            	wx.closeWindow();
	            });
			});
          });
    </script>
    
</body>
</html>