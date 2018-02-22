<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//通过request["code"]得到code，通过code得到用户的基本信息
%>

<!DOCTYPE html>
<html>
<head lang="en">
    <title>告诉我们</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0,user-scalable=0">	
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
    <style>
    	html,body{background-color:#CACACA;font-family:"Microsoft YaHei","SimHei","圆体";}
    	#main{width:600px;margin:0 auto;font-size:14px;}
    	@media (max-width: 600px) {	
			#main{width:100%;margin:0 auto;}
		}
    </style>
    <script src="/js/jquery-1.11.0.min.js"></script>
    <script src="/js/jweixin-1.0.0.js"></script>
    <script>
		wx.config({
            appId: 'wx895e0940966d46ce',
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
	            	var txtcontent=$("#txtcontent").val(),openid=$("#openid").val();
	            	if(txtcontent==""){
	            		alert("请输入您的意见...");
	            		return  ;
	            	}else{	            		
	            		$.post(
	            			"/wzjh/saveFeedback",
	            			{
	            				content:txtcontent,
	            				openid:openid
	            			},
	            			function(data){
	            				$('.page1').hide();
	            				$('.page2').show();
	            				window.setTimeout(function(){
	            					wx.closeWindow();	            				
	            				},3000);
	            			}
	            		);
	            	}
	            });
	            
			});
          });
    </script>
</head>

<body>
		<div id="main">
			<dl>
				<dd style="margin:12px 12px 2px;">
					<textarea id="txtcontent" class="page1" style="width:100%;height:200px;border:1px solid #25B6ED;border-radius:8px; padding:10px;box-sizing:border-box;line-height:1.8em;" placeholder="爱用户就像爱生命，期待您提交宝贵的反馈与建议，我们会不断努力完善产品！^_^"></textarea>
					<div class="page2" style="background-color:#fff;border-radius:10px;padding:20px;box-sizing:border-box;line-height:1.8em;color:#555;display:none;">
						感谢您提的宝贵意见，我们会不断努力完善产品！^_^
					</div>
				</dd>
				<dd>
					<img alt="" src="/img/mobile/kbzs/ask_bg.png" style="width:57%;"/>
				</dd>
				<dd class="fixed page1" style="padding:6px;">
					<button class="buttonClass74BDCE" style="background-color:#25B6ED;" id="submit">提&ensp;交</button>
				</dd>
			</dl>
		</div>
		<form>
			<input type="hidden" value="${openid}" id="openid"/>
		</form>
</body>
</html>