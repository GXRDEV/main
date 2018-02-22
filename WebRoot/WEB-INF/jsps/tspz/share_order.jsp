<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE HTML>
<html>
	<head>
		<base href="/">
	   	<title>天使陪诊</title>
	   	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=EDGE"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
		<style>
			body,html{background-color:#C6E3E9;font-size:16px;}
			#popweixin {
			    width:100%;height:100%;
			    overflow:hidden;
			    position:fixed;
			    z-index:1000; top:0;left:0;			    
			    display:none;background-color:#000;
			    opacity:0.85
			}
			#popweixin .tip { width:100%;z-index:1001;}
			.top2bottom {
			    -webkit-animation:top2bottom 0.6s ease;
			    -moz-animation:top2bottom 0.6s ease;
			    -o-animation:top2bottom 0.6s ease;
			    animation:top2bottom 0.6s ease;
			    -webkit-animation-fill-mode:backwards;
			    -moz-animation-fill-mode:backwards;
			    -o-animation-fill-mode:backwards;
			    animation-fill-mode:backwards
			}
			.animate-delay-1 {
			    -webkit-animation-delay:0.4s;
			    -moz-animation-delay:0.4s;
			    -o-animation-delay:0.4s;
			    animation-delay:0.4s
			}
			@-webkit-keyframes top2bottom {
			    0% {
				    -webkit-transform:translateY(-300px);
				    opacity:.6
				}
				100% {
				    -webkit-transform:translateY(0px);
				    opacity:1
				}
			}
			@keyframes top2bottom {
			    0% {
				    transform:translateY(-300px);
				    opacity:.6
				}
				100% {
				    transform:translateY(0px);
				    opacity:1
				}
			}
			.main{border:1px solid #ccc;padding:6px;border-radius:4px;background-color:#fff;margin:0 10px;}
			.main p{line-height:1.6em;color:#333;text-indent:2em;}
			.buttonClassgreen{height:40px;border:0;background-color:#04BE02;border-radius:3px;width:100%;color:#fff;font-size:18px;font-weight:600;}
		</style>
	</head>
	<body id="index">
		<div id='popweixin' onclick="hideInfo()">
		    <div class='tip top2bottom animate-delay-1'>
		        <img src='/img/share/tip@2x.png' style="width:100%;"/>
		    </div>
		</div>			
		<div style="margin:20px 10px;">
			<button class="buttonClassgreen" onclick="showInfo()">感觉不错，分享给朋友吧</button>
		</div>
		<div class="main">
			<p>
				我通过‘看病找谁’微信公众号非常幸运的预约了${dinfo.hospitalName}的${dinfo.realName}${dinfo.dutyName}就诊，整个看病过程非常简单， 不需要我考虑任何东西，所有注意事项，专业的陪诊人员都替我考虑好了，我只需要跟着走即可，非常省心，并且还不贵哦。
			</p>
			<p>据说接下来还有专车接送服务哦。</p>
			<p>有需要的可以关注以下公众号即可使用服务。</p>
			<p style="text-align:center;text-indent:0em;padding-top:1em;"></p>
		</div>
		<script src="/js/jweixin-1.0.0.js"></script>
		<script type="text/javascript">
			var baseUrl = '/', imgUrl = baseUrl + 'img/mobile/kbzs/3.png';
	        var lineLink = baseUrl +'wtspz/shared/${orderid}';
	        //var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx4ee3ae2857ad1e18&redirect_uri=';
			//url += encodeURIComponent(lineLink);
			//url += '&response_type=code&scope=snsapi_userinfo&state=1234#wechat_redirect';

	        var descContent = '我通过‘看病找谁’微信公众号非常幸运的预约了……';
	        var shareTitle = '“看病找谁”代金券，看病咨询、预约专家、就医陪诊，为您的健康买单！';
	        var ops={
	        	title: shareTitle,
			    desc: descContent,
			    link: lineLink,
			    imgUrl: imgUrl
	        };
			wx.config({
	          appId: '${appid}',
	          timestamp: ${timestamp},
	          nonceStr: '${nonceStr}',
	          signature: '${signature}',
	          jsApiList: [
		            'onMenuShareAppMessage','onMenuShareTimeline','hideAllNonBaseMenuItem','showMenuItems'
	          ]
	        });
			wx.ready(function(){
				wx.onMenuShareAppMessage(ops);
				wx.onMenuShareTimeline(ops);
				//wx.onMenuShareQQ(ops);
				//wx.onMenuShareWeibo(ops);
				wx.hideAllNonBaseMenuItem();
				wx.showMenuItems({
				    menuList: ["menuItem:share:appMessage","menuItem:share:timeline"]
				});
			});
		</script>
		<script>
			function showInfo(){
				var d = document.getElementById('popweixin');
				d.style.display = 'block';
			}
			function hideInfo(){
				var d = document.getElementById('popweixin');
				d.style.display = 'none';
			}
		</script>
	</body>
</html>
