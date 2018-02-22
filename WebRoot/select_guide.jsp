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
		<link href="/css/tstyle/h.css" rel="stylesheet">
		<link href="//css/styles/idangerous.swiper.css"
			rel="stylesheet">
		<script src="http://g.alicdn.com/??kissy/k/1.4.14/seed-min.js"
			data-config="{combine:true}" type="text/javascript"></script>

		<script src="/js/tjs/UFO.js" type="text/javascript"></script>
		<script src="/js/tjs/config.js" type="text/javascript"></script>
		<style type="text/css">
.new-index-header-service{position:absolute;top:190px;left:75px;display: inline-block;color:#fff;z-index: 10;padding: 2px;border:1px solid #3fced2;border-radius: 3px;}
.new-index-header-service span{display: block;padding:8px 10px;;background: #42d2d9;border-radius: 3px;font-size:1.5rem;}
.new-index-content-left{float: left;width: 100%;background:#2acbac;border-radius: 4px;height:240px;text-align: center;}
.new-index-content-right{float: right;width:49%;}
.new-index-content-right-top{background: #fc6368;overflow:hidden;  padding: 32px 0;border-radius: 5px;display:block;}
.new-index-content-right-bottom{background: #918eff;margin-top:10px; overflow:hidden; padding: 32px 0;border-radius: 5px;display:block;}
.new-index-content-left img{width:60px;margin-top: 55px;}
.new-index-content-left-name{margin-top:10px;}
.new-index-content-left-span{  font-size: 14px;color: #fff; padding-bottom: 5px;display: inline-block;position: relative;}
.new-index-content-left-tip{font-size: 12px;color: #fff; margin-top: 5px;opacity:0.6;}
.new-index-content-right img{float: left;width: 45px;  margin: 0 10px;}
.new-orderadd-tag{display: inline-block;border:1px solid #d6d6d6;border-radius: 5px;padding:1px 5px;color:#999;font-size:6px;}
@media screen and (max-device-width:374px) {
    .new-index-content-right img{width:40px;}
    .new-index-content-left-tip{font-size:1.2rem;}
    .new-index-content-left-span{font-size:1.8rem;}
    .new-index-content-right-top{  padding: 25px 0;}
    .new-index-content-right-bottom{  padding: 25px 0;}
    .new-index-content-left{height:200px;}
    .new-index-content-left img{margin-top:44px;}
    .new-index-header-service{bottom: 58px;left: 43px;}
}
</style>
	</head>
	<body>
		<div class="bar-positive nav-bar-container">
			<div class="nav-bar-block">
				<div class="bar-love bar bar-header disable-user-behavior">
					<div class="buttons buttons-left" style="transition-duration: 0ms;">
						<a class="button button-clear" id="button_city"
							action="show-citys-modal"> <i id="city_name">北京市</i> <i
							class="iconfont ion-arrow-down"
							style="color: #fff; font-size: 16px;"></i> </a>
					</div>
					<div class="title title-center header-item"
						style="transform: translate3d(0px, 0px, 0px);">
						天使陪诊
					</div>
					<div class="buttons buttons-right">
						<a href="tel:010-68547797" style="font-size: 23px;"
							class="button button-clear iconfont icon-phone"></a>
					</div>
				</div>
			</div>
		</div>
		<div class="view-container">
			<div class="tabs-icon-top pane">
				<div class="view-container tab-content">
					<div class="pane has-header has-tabs" style="overflow: auto;">
						<div class="scroll-content" style="min-height: 375px;">
							<section class="index-banner" style="text-align:center;"
								style="position:relative;">
							<div class="swiper-container" style="height:245px !important">
								<div class="swiper-wrapper">
									<img src="#" data-src="//images/beau.jpg" alt=""
										class="swiper-slide">
								</div>
							</div>
							<div class="pagination"></div>
							<a href="javascript:void(0);" class='new-index-header-service'>
								<span style="font-size:12px;">辅助就医我选&nbsp;<b style="font-size: 0.9rem;">天使陪诊</b>
							</span> </a>
							</section>
							<div id="card_container" class="card-container clear-float">

								<ul class="column">

									<li class="cell rowspan-2">
										<a class="new-index-content-left" href="/wtspz/intoservice/2/${openid}">
									        <img src="//images/new-index-nav-icon_06.png"/>
									        <p class="new-index-content-left-name">
									            <span class='new-index-content-left-span'>陪医导诊
									            <span class="new-banner-bottom-border"></span>
									            </span> 
									        </p>
									        <p class="new-index-content-left-tip">院内陪诊&nbsp;取送报告</p>
									    </a>
									</li>
								</ul>
								<ul class="column">

									<li class="cell rowspan-1">
										<a
											href="/wtspz/intoservice/1/${openid}"
											class="card-item clear-float"
											style="background-color: #fc6368;">
											<div class="fl flex" style="width: 50%; height: 100%;">
												<img
													src="//images/new-index-nav-icon_03.png"
													style="margin: auto;">
											</div>
											<div class="fl flex" style="width: 50%; height: 100%;">
												<div style="margin: auto 0;">
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
											</div> </a>
									</li>
									<li class="cell rowspan-1">
										<a
											href="javascript:alert('暂未开通!敬请期待!');"
											class="card-item clear-float"
											style="background-color:#dddddd;">
											<label class="label-have-not-open"></label>
											<div class="fl flex" style="width: 50%; height: 100%;">
												<img
													src="//images/new-index-nav-icon_10.png"
													style="margin: auto;">
											</div>
											<div class="fl flex" style="width: 50%; height: 100%;">
												<div style="margin: auto 0;">
													<h5 class="has-line">
														陪护
													</h5>
													<h6>
														护理
													</h6>
													<h6>
														照顾
													</h6>
												</div>
											</div> </a>
									</li>
								</ul>

							</div>
						</div>
					</div>
				</div>
				<div class="tab-nav tabs">
					<a class="tab-item"> <i class="icon iconfont icon-home"></i> <span
						class="tab-title">首页</span> </a>
					<a href="javascript:alert('开发中..');"
						class="tab-item"> <i class="icon iconfont icon-order"></i> <span
						class="tab-title">我的订单</span> </a>
				</div>
			</div>
		</div>


		<script type="text/javascript" src="//js/zepto.min.js"></script>
		<script type="text/javascript"
			src="//js/idangerous.swiper.js"></script>
		<script type="text/javascript">
	$(function() {
		$container = $('.swiper-container');
		$container.find('img').eq(0).on('load', function() {
			// 将轮播图区域设置成跟图片等高
				var containerH = $container.find('img').height();
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
