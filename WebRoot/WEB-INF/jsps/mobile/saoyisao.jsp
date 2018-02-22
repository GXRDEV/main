<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="/">
    <title>扫码登录</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/reset.css" />
	<style>
		body{
			background:url('/img/login/bg.jpg') center center / cover no-repeat;
			font-family: "Helvetica Neue",Helvetica,"Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
		}
		.login {height: 100%; min-width: 860px;min-height: 650px; overflow: auto; position: relative;}		
		.login_box {
		    position: absolute;
		    top: 50%; left: 50%;
		    margin-left: -175px;
		    margin-top: -220px;
		    border-radius: 4px;
		    -moz-border-radius: 4px;
		    -webkit-border-radius: 4px;
		    background-color: #fff;
		    width: 350px; height: 440px;
		    box-shadow: #bbb 0 2px 10px;
		    -moz-box-shadow: #bbb 0 2px 10px;
		    -webkit-box-shadow: #bbb 0 2px 10px;
		}
		.login_box .qrcode { text-align: center;}
		.login_box .qrcode .img { display: block; width: 200px; height: 200px; margin: 64px auto 50px;}
		.login_box .qrcode .sub_title {text-align: center; font-size: 16px; color: #353535;}
		.login_box .avatar { display: none;  text-align: center; margin-top: 100px;}
		.login_box .avatar .img {
		    width: 100px; height: 100px;
		    border-radius: 4px;
		    -moz-border-radius: 4px;
		    -webkit-border-radius: 4px;
		    margin-bottom: 80px;
		    border: 1px solid rgba(0,0,0,.1);
		}
		.login_box .avatar .sub_title { font-size: 24px; color: #353535; margin-bottom: 14px; font-weight: 400;}
		.login_box .qrcode .action,
		.login_box .avatar .action {
		    position: absolute;
		    bottom: 40px;
		    display: block;
		    width: 100%;
		    text-align: center;
		    text-decoration: none;
		    color: #459ae9;
		    font-size: 14px;
		}
		.login .telme{position:absolute;bottom:1.5em;width:100%;text-align:center;font-weight:400;font-size:21px;color:#333;}
		.logo { position: absolute; top: 20px; left: 26px;}
		.web_wechat_login_logo {
		    background: url(/img/login/logo.png) no-repeat center center;
		    width: 82px; height: 30px;
		    vertical-align: middle;
		    display: inline-block;
		}
		.web_wechat_login_sublogo {
		    background: url(/img/login/sublogo.png) no-repeat center center;
		    width: 207px; height: 16px;
		    vertical-align: middle;
		    display: inline-block;
		    margin:5px 0 0 10px;
		}
		.download{position: absolute;top: 20px;right: 26px; color:#767676;font-size:12px; line-height: 1.5em;}
		.download span{line-height:2em;}
		.download a{display:inline-block;padding:2px 6px;border:1px solid #0085D0;border-radius:2px; color:#0085D0;text-decoration: none;}
	</style>
  </head>  
  <body>
  	<div class="login">
  		<div class="logo">
  			<i class="web_wechat_login_logo"></i>
  			<i class="web_wechat_login_sublogo"></i>
  		</div>
  		<div class="download">
  			<span>APP下载：</span>
  			<a href="https://itunes.apple.com/us/app/zhuan-jia-hao/id985715000?l=zh&ls=1&mt=8#" target="_blank">iPhone 下载</a>
  			<a href="http://openbox.mobilem.360.cn/index/d/sid/2947131" target="_blank">Android 下载</a>
  		</div>
  		<div class="login_box">
  			<div class="qrcode">
    			<img alt="" class="img" src="//hybrid/showQR?keyid=${keyid}">
    			<p class="sub_title">通过“佰医汇-医生版”App扫描本页二维码</p>
                <a href="https://develop.ebaiyihui.com/" class="action">手动登录后台</a>
  			</div>
  			<div class="avatar">
                <img class="img" alt="" src="/img/defdoc.jpg">
                <h4 class="sub_title">扫描成功</h4>
                <p class="tips"><!-- 请在手机上点击确认以 -->正在登录，请稍等</p>
                <a href="javascript:;" class="action">返回二维码登录</a>
            </div>
  		</div>  
        <h2 class="telme">佰医汇客服热线：400-8905111</h2>  			
  	</div>  	
  	<form method="post" id="redirect" style="display:none;" action="/hybrid/sucjump">
		<input type="hidden" name="keyid" value="${keyid}">
	</form>
	<script src="/js/jquery.min.js"></script>
	<script type="text/javascript">		
		$(document).ready(function(){
			$('body').delegate('.avatar .action','click',function(){
				$('.avatar').hide().siblings('.qrcode').show();
			});
			window.setTimeout(function(){
				console.log('二维码失效');
				window.location = window.location;
			}, 300000);
		});
	</script>
	<script type="text/javascript" src="https://cdn.goeasy.io/goeasy.js"></script>
	<script type="text/javascript">    
        var _bse = '/',outsideGroup = 'zjh';
     	var goEasy = new GoEasy({
           	appkey: '78028e7e-edcc-4524-b56b-45639785a53a'
        });
      	goEasy.subscribe({
            channel: outsideGroup,
            onMessage: function(message){
            	console.log('message',message);
            	if(!message) return window.location.reload(),false;
            	if('${keyid}' != message.content) return console.log('返回不一至'),false;
          		$('.avatar').show().siblings('.qrcode').hide();
          		window.setTimeout(function(){
					listener(message.content);
				},300);
            }
        });
      	function listener(kid){
			$('#redirect').submit();
		}
    </script>
  </body>
</html>
