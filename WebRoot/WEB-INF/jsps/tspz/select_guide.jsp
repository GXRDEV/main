<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta charset="UTF-8">
		<title>天使陪诊•辅助就医第一平台</title>
		<meta name="viewport" content="width=device-width,  user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" href="/css/mobile.css" type="text/css"></link>
		<link rel="stylesheet" href="/css/icon.css" type="text/css"></link>
		<link href="//css/styles/idangerous.swiper.css" rel="stylesheet">
		<style type="text/css">
			html,body,.main{height:100%;box-sizing:border-box;}
			.main{padding:48px 0 54px;font-size:0;width:100%; max-width:600px;margin:0 auto;}
			.imgs{height:45%;position:relative;overflow:hidden;}
			.imgs img{height:100%;}
			.nav-bar-container{bottom:auto!important;top:0;height:48px;background-color:#F683A4;color:#fff;}
			.nav-bar-container a{color:#fff;}
			.new-index-header-service{position:absolute;left:50%;bottom:20px;margin-left:-75px;
				display:inline-block;padding:8px;color:#fff;text-align:center;
				background-color:#42D2D9;border-radius:4px;z-index:100;}
			.buttons-left{position:absolute;left:16px;top:15px;font-size:14px;}
			.button i{font-style:normal;color:#fff;}
			.title-center{text-align:center;font-size:18px;line-height:48px;font-weight:600}
			.buttons-right{position:absolute;right:16px;top:10px;}
			.links{height:55%;min-height:200px; padding:5px;box-sizing:border-box;}
			.spical1{display:block;background-color:#2ACBAC;width:40%;border-radius:3px;
				float:left;clear:both;height:100%;font-size:14px;text-align:center;}
			.spical1 div{position:relative;display:inline-block;top:50%;left:50%;
				-webkit-transform: translate(-50%, -50%);   
    			transform: translate(-50%, -50%);width:100%}
			.spical2{display:block;height:50%;margin-left:41%; font-size:14px;border-bottom:3px solid #fff;box-sizing:border-box;}
			.spical2 + .spical2{border-bottom-width:0;border-top:3px solid #fff;}
			.spical2{position:relative; background:#B3DF7D;border-radius:3px;}
			.spical3{background:#E8D8D1;}
			.spical2 div{position:relative;display:inline-block;top:50%;left:50%;
				-webkit-transform: translate(-50%, -50%);   
    			transform: translate(-50%, -50%); min-width:150px;min-height:80px;
    			padding-left:80px;box-sizing:border-box;
    			background:url(/images/new-index-nav-icon_03.png) no-repeat 0 center;background-size:60px auto;}
    			
			.spical3 div{background:url(/images/new-index-nav-icon_10.png) no-repeat 0 center;background-size:60px auto;}
    			
			.spical2 h5.has-line { display: inline-block; border-bottom: 1px solid #fff; padding-right: 10px;}
			.spical2 h5, .spical2 h6 { padding-top: 5px; color: #fff; line-height: 1;font-size:12px;}
			.spical2 h5 {padding-bottom: 5px;font-size:14px;}
			.new-index-content-left-name { margin-top: 10px;}
			.new-index-content-left-span { font-size: 14px; color: #fff; padding-bottom: 5px; display: inline-block; position: relative;}
			.new-index-content-left-tip { font-size: 12px; color: #fff; margin-top: 5px; opacity: 0.6;}
			
			.tab-nav{font-size:0;background-color:#5ECFD3;}
			.tab-item{display:inline-block; width:50%;text-align:center;padding:5px 0;font-size:13px;color:#fff;}
			.tab-item .icon{display:block;padding-bottom:3px;color:#fff;}
			
			.nono{-webkit-transform:rotate(-45deg);transform:rotate(-45deg);position:absolute;right:-18px;bottom:7px;color:#fff;}
		</style>
	</head>
	<body>
		<div class="nav-bar-container g_fixed">
			<div class="nav-bar-block">
				<div class="bar-love bar bar-header disable-user-behavior">
					<div class="buttons buttons-left" style="transition-duration: 0ms;">
						<a class="button button-clear" id="button_city" action="show-citys-modal"> 
							<i id="city_name">北京市</i> 
							<i class="iconfont ion-arrow-down" style="color: #fff; font-size: 16px;"></i> 
						</a>
					</div>
					<div class="title title-center">
						天使陪诊
					</div>
					<div class="buttons buttons-right">
						<a href="tel:13810949001" style="font-size: 23px;" class="button button-clear iconfont icon-phone"></a>
					</div>
				</div>
			</div>
		</div>
		<div class="main">
			<div class="imgs">
				<div class="swiper-container">
					<div class="swiper-wrapper">
						<img src="#" data-src="//images/beau.jpg" alt="" class="swiper-slide" />
					</div>
				</div>
				<div class="pagination"></div>
				<a href="javascript:void(0);" class='new-index-header-service'>
					<span style="font-size:12px;">辅助就医我选&nbsp;
						<b style="font-size: 14px;">天使陪诊</b>
					</span> 
				</a>
			</div>
			<div class="links clearfix">
				<a class="spical1" href="/wtspz/intoservice/2/${openid}">
					<div>
						<img src="//images/new-index-nav-icon_06.png" style="width:55px;"/>
						<p class="new-index-content-left-name">
				            <span class='new-index-content-left-span'>陪医导诊</span> 
				        </p>
				        <p class="new-index-content-left-tip">院内陪诊&nbsp;取送报告</p>
			        </div>
				</a>
				<a class="spical2" href="/wtspz/intoservice/1/${openid}">
					<div>
						<h5 class="has-line">
							挂号 加号
						</h5>
						<h6>
							普通号
						</h6>
						<h6>
							佰医汇
						</h6>
						<h6>
							指定专家
						</h6>
					</div>
				</a>
				<a class="spical2 spical3">
					<div>
						<h5 class="has-line">
							专业陪护
						</h5>
						<h6>
							护理
						</h6>
						<h6>
							照顾
						</h6>
						<span class="nono">暂未开通</span>
					</div>
				</a>				
			</div>
			
		</div>
		<div class="tab-nav tabs g_fixed">
			<a class="tab-item" href="javascript:location=location;"> 
				<i class="icon iconfont icon-home"></i> 
				<span class="tab-title">首页</span> 
			</a>
			<a href="/wtspz/myorders.do?openid=${openid}" class="tab-item">
				<i class="icon iconfont icon-order"></i> 
				<span class="tab-title">我的订单</span> 
			</a>
		</div>
		<!--	
		oLAmZt5uvYKF9OTZVPC4pXQhUxHc
		-->
		<script type="text/javascript" src="//js/zepto.min.js"></script>
		<script type="text/javascript" src="//js/idangerous.swiper.js"></script>
		<script type="text/javascript">
			$(function() {
				$container = $('.swiper-container');
				$container.find('img').eq(0).on('load', function() {
					// 将轮播图区域设置成跟图片等高
					var containerH = $('.imgs').height();
					$container.css( {
						height : containerH,
						overflow : 'hidden'
					});
					// 图片轮播
					var mySwiper = $container.swiper( {
						mode : 'horizontal',
						pagination : '.pagination',
						loop : true
					});
				});
				$container.find('img').each(function(index, item) {
					var that = $(this);
					that.attr('src', that.attr('data-src'));
				});
			});
		</script>
	</body>
</html>
