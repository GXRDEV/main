<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE HTML>
<html>
  <head lang="en">
    <base href="/">
    <meta charset="utf-8">
	<meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
	<meta content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
	<meta content="email=no" name="format-detection">
	<title>佰医汇合作医生平台</title>
	<link rel="stylesheet" type="text/css" href="/css/mobile.css?v=1.0" />
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
	<link rel="stylesheet" type="text/css" href="/libs/swiper/css/swiper.min.css" />
	<link rel="stylesheet" type="text/css" href="/css/view/pingllist.css" />
	<style>
		body{font-family:PingFang-SC-Regular, Helvetica, sans-serif;background:#f5f5f5;}
		figure{margin:0;}
		header{margin-bottom:10px;}
		.swiper-container{height:200px;background:#B9B9B9;overflow:hidden;}
		.swiper-slide{position:relative;}
		.swiper-slide img{position:absolute;left:50%;top:50%;transform:translate(-50%,-50%);-webkit-transform:translate(-50%,-50%);height:100%;}
		.main .block{background:#fff;border-bottom:1px solid #E2E2E2;border-top:1px solid #DEDEDE;padding:0 14px;box-sizing:border-box;}
		.main .block + .block{margin-top:10px;}
		.docinfo{margin-top:2em;font-size:1rem;color:#323232;}
		.docinfo b{font-size:1.3rem;margin-right:0.5em;}
		.docinfo p:last-child{color:#8D8D8D;line-height:3em;}
		
		.sdegree{font-size:1rem;margin-top:0.5em;color:#323232;}
		.sdegree b{font-size:1.6rem;color:#D84C3D;}
		.signlabel{margin:1em 0 1.5em;font-size:0.8rem;}
		.signlabel span{display:inline-block;padding:2px;color:#8095A6;background-color:#F2F3F5;margin-right:1em;border-radius:2px;}
		
		.hosinfo .flex{padding:1em 0;font-size:1rem;color:#8C8C8C;line-height:1.5em;}
		.hosinfo .flex + .flex{border-top:1px solid #EDEDED;}
		.hosinfo .flex .f1{width:55%;}
		.hosinfo .flex .f1 + .f1{width:45%;}
		.hosinfo .flex span{color:#242424;}
		
		.textinfo h2{font-size:1.3rem;color:#2a2a2a;border-bottom:1px solid #E6E6E6;padding:0.8em 0;font-weight:700;}
		.textinfo p{padding:1em 0;line-height:1.6em;font-size:1rem;color:#2b2b2b;}
		
		.numlist b,.numlist span{display:block;text-align:center;}
		.numlist b{font-size:3rem;padding:0.8em 0 0.3em;}
		.numlist span{font-size:0.9rem; padding:0.3em 0 2em;}
		.btngoto{height:46px;color:#fff;background:#06C494;text-align:center;line-height:46px;font-size:16px;}
		@media (min-width: 1025px) {
			html{font-size:16px!important;}
			.main{width:800px;margin:0 auto;}
		}
		@media (min-width: 768px) and (max-width: 970px) {
			html{font-size:16px!important;}
			.swiper-container{height:360px;}
		}
		@media (min-width: 481px) and (max-width: 767px) {
			html{font-size:16px!important;}
			.swiper-container{height:300px;}
		}
		@media (max-width: 480px) {
			html{font-size:13px!important;}
			.swiper-container{height:200px;}
		}
	</style>
	<!-- <script type="text/javascript">
		;(function(f,i){var b=i.documentElement;var e=f.navigator.appVersion;var g=e.match(/android/gi);var c=e.match(/iphone/gi);var k=f.devicePixelRatio;var j=1,d=1;if(c){if(k>=3){j=3}else{if(k>=2){j=2}else{j=1}}}else{j=1}d=1/j;var h=i.querySelector('meta[name="viewport"]');if(!h){h=i.createElement("meta");h.setAttribute("name","viewport");if(b.firstElementChild){b.firstElementChild.appendChild(h)}else{var a=i.createElement("div");a.appendChild(h);i.write(a.innerHTML)}}if(j){h.setAttribute("content","initial-scale="+d+", maximum-scale="+d+", minimum-scale="+d+", user-scalable=no,uc-fitscreen=yes");b.style.fontSize=16*j+"px"}window.DPR=j})(window,document);
	</script> -->
  </head>  
  <body>
    <div class="main">
		<header>
			<section class="swiper-container">
				<figure class="swiper-wrapper">
					<c:forEach items="${images }" var="img">
						<p class="swiper-slide">
							<img src="${img.fileUrl }" />
						</p>
					</c:forEach>					
				</figure>
				<div class="swiper-pagination"></div>
			</section>			
			<section class="block">
				<div class="docinfo flex">
					<div class="f1">
						<p><b>${special.specialName }</b><span>${special.duty }&ensp;${special.profession }</span></p>
						<p>${special.hosName }&emsp;${special.depName }</p>
					</div>
					<!-- <div class="follow">
						<i class="iconfont"></i>关注
					</div> -->
				</div>
				<!-- <p class="sdegree">
					患者满意度：<b>100%</b>
				</p> -->
				<p class="signlabel">
					<c:choose>
						<c:when test="${ltype == '1' }"><span>图文问诊</span><label>费用：<b style="color:red;">${special.askAmount }元</b></label></c:when>
						<c:otherwise><span>电话问诊</span><label>费用：<b style="color:red;">${special.telAmount }元</b></label></c:otherwise>
					</c:choose>
				</p>
			</section>
		</header>
		<section class="block hosinfo">
			<div class="flex">
				<div class="f1 whitespace">医院等级：<span>${special.hosLevel}</span></div>
				<div class="f1 whitespace">教学职称：<span>${special.profession }</span></div>
			</div>
			<div class="flex">
				<div class="f1 whitespace">所属医院：<span>${special.hosName }</span></div>
				<div class="f1 whitespace">职&emsp;&emsp;称：<span>${special.duty }</span></div>
			</div>
		</section>
		<section class="block textinfo">
			<h2>擅长领域</h2>
			<p>${special.specialty }</p>
		</section>
		<section class="block textinfo">
			<h2>简介</h2>
			<p>${special.profile }</p>
		</section>
		<section id="pinglistdiv" class="block textinfo">
			<h2>评价列表</h2>
			<p>
				<span id="signslist" class="signlists"></span>
				<span id="pingllist"></span>
			</p>
		</section>
		<!-- <section class="block textinfo">
			<h2>平台数据</h2>
			<div class="flex numlist">
				<div class="f1">
					<b>32</b>
					<span>专家会诊次数</span>
				</div>
				<div class="f1">
					<b>10</b>
					<span>电话问诊次数</span>
				</div>
				<div class="f1">
					<b>56</b>
					<span>图文问诊次数</span>
				</div>
			</div>
		</section> -->
		<div style="height:52px;">	
			<a class="g_fixed btngoto" href="/wzjh/intocasesub?docid=${special.specialId}&ltype=${ltype }&openid=${openid}"><span>立即下单</span></a>
		</div>
    </div>
	<c:if test="${ fn:indexOf(special.listSpecialPicture,'://') != -1}">
		<input type="hidden" id="imgUrl" value="${fn:replace(special.listSpecialPicture,'http://','https://')}"/>
	</c:if>
	<c:if test="${ fn:indexOf(special.listSpecialPicture,'://') == -1}">
		<input type="hidden" id="imgUrl" value="http://wx.15120.cn/SysApi2/Files/${special.listSpecialPicture }"/>
	</c:if>
	<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
	<script src="/libs/swiper/js/swiper.jquery.min.js"></script>
	<script>
		new Swiper('.swiper-container', {
	         pagination: '.swiper-pagination',
        	paginationClickable: true
		});
	</script>
    <script src="/js/base.js"></script>
	<script src="/js/infinite.scroll/jquery.endless-scroll-1.3.js"></script>
	<script type="text/javascript">
		var docid = '${special.specialId}';
	</script>
	<script src="/js/view/moredocpingl.js"></script>
	<script src="/js/jweixin-1.0.0.js"></script>
	<script>		
		var imgUrl = document.querySelector('#imgUrl').value;
		if(imgUrl.indexOf('://') != -1){
			imgUrl = imgUrl.replace('http://','https://');
		}else {
			imgUrl="http://wx.15120.cn/SysApi2/Files/"+imgUrl;
		}
        var lineLink = location.href;
        var descContent = '${special.specialty }';
        var shareTitle = '${special.specialName} ${special.duty} ${special.profession} ${special.hosName}';
        var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx4ee3ae2857ad1e18&redirect_uri=';
		url += encodeURIComponent(lineLink);
		url += '&response_type=code&scope=snsapi_base&state=1234#wechat_redirect';
		wx.config({
	        appId: '${appid}',
	        timestamp: +'${timestamp}' || 0,
	        nonceStr: '${nonceStr}',
	        signature: '${signature}',
	        jsApiList: [
	            'onMenuShareAppMessage','onMenuShareTimeline','showAllNonBaseMenuItem'//,'showMenuItems'
	        ]
        });
        var ops={
        	title: shareTitle,
		    desc: descContent,
		    link: lineLink,
		    imgUrl: imgUrl.replace(/\\/g, "/")
        };
		wx.ready(function(){
        	wx.onMenuShareAppMessage(ops);
			wx.onMenuShareTimeline(ops);
			wx.showAllNonBaseMenuItem();
			//wx.hideAllNonBaseMenuItem();
			//wx.showMenuItems({
			//    menuList: ['menuItem:share:appMessage','menuItem:share:timeline']
			//});
		});
	</script>
  </body>
</html>
