<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta charset="UTF-8">
		<title>佰医汇</title>
		<jsp:include page="../icon.jsp" />
		<meta name="viewport" content="width=device-width,  user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" href="/css/mobile.css" type="text/css" />
		<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
		<link rel="stylesheet" type="text/css" href="/css/view/mainhome.css" />
		<style type="text/css">
			.nav-bar-container .button,.nav-bar-container .titlemain{color:#1ACFA2;}
			.nav-bar-container .nav-bar-block{position:relative;}
			.nav-bar-container .titlemain{position:absolute;left:0;top:0;width:100%;z-index:1;
				 font-size:18px;font-weight:600;text-align:center;line-height: 52px;}
			.nav-bar-container .bar-header{position:relative;z-index:2;}
			.nav-bar-container .bar-header .icon-kefus{font-size:22px;}
			
			.section{margin-bottom:10px;background-color:#fff;display:block;}
			.section header{margin:0 10px;color:#02CC9A;font-size:16px;font-weight:600;line-height:2.5em;}
			.section header .iconfont{font-weight:300;font-size:1.2em;margin-right:5px;}
			.section .bodyer{margin:0 10px;padding-bottom:5px;}
			.section .bodyer p{line-height:1.8em;font-size:13px;}
			
			.btngreen{display: block;font-size: 18px;text-align: center;background-color: #00CC99;margin: 10px;line-height: 44px;color: #fff;}
			.tab-nav{border-top:0;}
			.btngreen.disabled{opacity:.4;cursor:not-allowed;}
		</style>
	</head>
	<body>
		<div class="nav-bar-container g_fixed" id="mindex">
			<input type="hidden" id="cooHosId" value=""/>
			<div class="nav-bar-block">
				<div class="bar-header box">
					<div class="cols0" style="transition-duration: 0ms;">
						<a class="button button-clear" id="button_city" action="show-citys-modal"> 
							<span id="city_name" data-id="${coohos[0].id}" data-value="${coohos[0].city}">${coohos[0].city}</span> 
							<i class="iconfont ion-arrow-down">&#xe608;</i> 
						</a>
						<div class="popup" style="display:none" >
							<div class="wrapper scroll" >
								<div>
									<dl class="place">
										<dt>GPS 定位城市</dt>
										<dd class="citys" ><a href="javascript:void(0)" id="lcity"></a></dd>
									</dl>
								</div>
								<div class="cities" >
									<dl>
										<dt >已开通服务城市</dt>
										<c:forEach items="${coohos}" var="hos">
											<dd class="citys" data-id="${hos.id}" data-value="${hos.city}">
												<a href="javascript:void(0)" ><span >${hos.city}<c:if test="${!empty(hos.displayName)}"> -- ${hos.displayName}</c:if></span></a>
											</dd>
										</c:forEach>
									</dl>
								</div>
							</div>
						</div>
					</div>
					<div class="cols1"></div>
					<div class="cols0">
						<a href="tel:400-631-9377" class="button button-clear iconfont icon-kefus">&#xe604;</a>
					</div>
				</div>
				<div class="titlemain">全国名医面诊</div>
			</div>
		</div>
		<div class="index">
			<div class="header" style="height:48px"></div>
			<div class="section">
				<img src="/img/mobile/wzjh/banner.jpg" style="width:100%" alt=""/>
			</div>
			<div class="section">
				<header><i class="iconfont" style="color:#FF7B33;">&#xe617;</i><span>服务介绍</span></header>
				<div class="bodyer">
					<p>在本地合作医院挂号，向全国顶级专家面对面视频问诊。专家全部是北上广等城市知名三甲医院顶级名医。问诊中当地医生全程陪同。</p>
				</div>
			</div>
			<div class="section">
				<header><i class="iconfont" style="color:#38B0EF;">&#xe619;</i><span>特点优势</span></header>
				<div class="bodyer">
					<p>专家权威：专家全部是全国排名前十医院科室副主任以上医生。</p>
					<p>流程简单：就诊时有院内医生全程陪同，帮助患者操作，病情资料直接从医院调用。</p>
					<p>成本低廉：无需离开本地，即可获得全国顶级名医服务。</p>
				</div>
			</div>
			<div class="section">
				<header><i class="iconfont" style="color:#8FD10F;">&#xe618;</i><span>服务流程</span></header>
				<div class="bodyer">
					<p>自动匹配当地医院与科室->选择专家以及就医时间->前往当地医院就医->通过视频技术连线专家->与专家视频面对面交流->就医结束。</p>
				</div>
			</div>
			<div class="footer" style="height:64px"></div>
		</div>
		
		<div class="tab-nav tabs g_fixed">
			<a id="yuanc" href="#" class="btn btngreen">我要预约</a>
		</div>
		<script type="text/javascript" src="/js/zepto.min.js"></script>
		<script type="text/javascript">
			function setState(){
				$('#cooHosId').val() ? 
					$('#yuanc').removeClass('disabled').attr('href','/wzjh/iwantappoint?cooHosId='+ $('#cooHosId').val() +'&orderid=${orderid}&openid=${openid}').html('我要预约') :
					$('#yuanc').addClass('disabled').attr('href','javascript:void(0)').html('当前城市暂未开通');
			}
			$(function() {
				$('#button_city').click(function(){
					$('#mindex').removeClass('g_fixed');
					$('.popup').css('display','block');
				});
				$('.citys').click(function(){
					var ids = $(this).attr('data-id')||'',vls = $(this).attr('data-value');
					$('#cooHosId').val(ids);
					$('#mindex').addClass('g_fixed');
					$('.popup').css('display','none');
					$('#city_name').attr('data-id',ids).attr('data-value',vls).text(vls);
					setState();
				});
				$('.serlist').click(function(){
					var dv=$(this).attr('data-val'),url='',cooid = $('#cooHosId').val();
					if($(this).hasClass('disabled')) return false;
					switch(dv){
						case '1':
							url='/wzjh/graphicCon?openid=${openid }';
							break;
						case '2':
							break;
						case '3':
							break;
						case '4':
							url='/wzjh/speciallist?openid=${openid}&cooHosId='+ cooid;
							break;
						case '5':
							url='/wzjh/faceplus?openid=${openid}';
							break;
					}
					if(url==''){
						alert("正在开发中...");
						return ;
					}else{
						window.location.href=url;
					}
				});
				setState();
			});
		</script>
		<script src="/js/jweixin-1.0.0.js"></script>
    	<script>
	    	wx.config({
			    appId: '${appid}',
	            timestamp: +'${timestamp}',
	            nonceStr: '${nonceStr}',
	            signature: '${signature}',
	            jsApiList: [
	              'getLocation'
	            ]
			});
			wx.ready(function(){
				wx.getLocation({
				    type: 'wgs84',
				    success: function (res) {
				        var latitude = res.latitude;
				        var longitude = res.longitude;
				        gainLocation(latitude,longitude);
				    },
				    fail: function (res) {
				       //alert(JSON.stringify(res));
				    },
				    complete:function(){}
				});
			});
			wx.error(function(res){
				//alert(JSON.stringify(res));
			});
			
			function gainLocation(latitude,longitude){
				$.post("/wzjh/gainCityByLocation",{latitude:latitude,longitude:longitude},function(data){
					var hosn = data.city;
					$('#city_name').attr('data-id',data.chid).attr('data-value',data.city + hosn).text(data.city + hosn);
					$('#lcity').text(data.city + hosn)
						.closest('.citys').attr('data-id',data.chid).attr('data-value',data.city + hosn);
					if(data.chid){$('#cooHosId').val(data.chid);}
					setState();
				});
			}
	    </script>
	</body>
</html>
