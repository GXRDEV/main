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
			
		</style>
	</head>
	<body>
		<div class="nav-bar-container g_fixed" id="mindex">
			<input type="hidden" id="cooHosId"/>
			<div class="nav-bar-block">
				<div class="bar-header box">
					<div class="cols0" style="transition-duration: 0ms;">
						<a class="button button-clear" id="button_city" action="show-citys-modal"> 
							<span id="city_name" data-id="${coohos[0].id}" data-value="${coohos[0].city}">${coohos[0].city}</span> 
							<i class="iconfont ion-arrow-down">&#xe608;</i> 
						</a>
						<div class="popup" style="display:none" >
							<div class="wrapper" >
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
												<a href="javascript:void(0)" ><span >${hos.city}</span></a>
											</dd>
										</c:forEach>
									</dl>
								</div>
							</div>
						</div>
					</div>
					<div class="cols1">
						<form action="/wzjh/speciallist" method="get" class="queryform">
							<input type="search" name="scontent" placeholder="疾病.医生.医院"/>
							<button type="submit"><i class="iconfont icon-query">&#xe603;</i> </button>
						</form>
					</div>
					<div class="cols0">
						<a href="tel:13810949001" class="button button-clear iconfont icon-kefus">&#xe604;</a>
					</div>
				</div>
			</div>
		</div>
		<div class="index main">
			<div class="header"></div>
			<div class="serlst serverlists">
				<div class="titles"><span>问诊</span></div>
				<div class="box">
					<div class="cols0 blckleft">
						<dl class="serlist nyuanc" data-val="1">
							<dd class="ddList" >
								<div class="cmiddle">
									<div class="iconfont icon-persons" style="color:#4AAD9B">&#xe601;<i>&#xe60b;</i></div> 
									<div class="title whitespace">
										图文问诊
									</div>
									<div class="remark whitespace">图文问诊全国名医</div>
								</div>
							</dd>
						</dl>
					</div>
					<div class="cols1 blckright">
						<dl class="serlist" style="border-bottom:1px solid #E9E9E9;" data-val="2">
							<dd class="ddList nosign" >
								<div class="box">
									<div class="cols1">
										<div class="title whitespace">电话问诊</div>
										<div class="remark whitespace">电话直通全国名医</div>
									</div>
									<div class="cols0">
										<i class="iconfont icon-telp" style="color:#1B94A7">&#xe607;</i> 
									</div>
								</div>
							</dd>
						</dl>
						<dl class="serlist"  data-val="3">
							<dd class="ddList nosign" >
								<div class="box">
									<div class="cols1">
										<div class="title whitespace">视频问诊<span>(暂未开通)</span></div>
										<div class="remark whitespace">与名医视频面对面交流</div>
									</div>
									<div class="cols0">
										<i class="iconfont icon-telp" style="color:#6894C3">&#xe602;</i> 
									</div>
								</div>
							</dd>
						</dl>
					</div>
				</div>
			</div>
			<div class="serlst serverlists">
				<div class="titles"><span>就诊</span></div>
				<div class="box">
					<div class="cols1 blckleft">
						<dl class="serlist nyuanc" data-val="4"  >
							<dd class="ddList disabled" id="yuanc">
								<div class="cmiddle">
									<div class="iconfont icon-persons" style="color:#6894C3">&#xe606;<span class="hoticon">HOT</span></div> 
									<div class="title whitespace">
										全国名医会诊
									</div>
									<div class="remark">在当地医院看病时，直接对接知名专家视频会诊</div>
								</div>								
							</dd>
						</dl>
					</div>
					<div class="cols1 blckleft">
						<dl class="serlist nyuanc" data-val="5" >
							<dd class="ddList" >
								<div class="cmiddle">
									<div class="iconfont icon-persons" style="color:#1A95A7;font-size:42px;margin-top:6px">&#xe609;</div> 
									<div class="title whitespace">
										加号
									</div>
									<div class="remark">助你预约北上广以及当地名称专家</div>
								</div>
							</dd>
						</dl>
					</div>
				</div>
			</div>
			
			<div class="serlst hosplists">
				<a class="box hosplst" href="http://localhost:8080/wzjh/graphicCon?openid=${openid }">
					<span class="cols1 whitespace">六安市全民就医服务</span>
					<span class="cols0"><i class="iconfont icon-persons">&#xe60a;</i></span>
				</a>
			</div>
			<div class="footer"></div>
		</div>
		
		<div class="tab-nav tabs g_fixed">
			<a class="tab-item" href="javascript:location=location;"> 
				<i class="iconfont icon-home" style="color:#33BBB3">&#xe600;</i> 
				<span class="tab-title" style="color:#33BBB3">首页</span> 
			</a>
			<a href="javascript:location.href='/wzjh/myorders?openid=${openid}';" class="tab-item">
				<i class="iconfont icon-order" style="color:#666">&#xe605;</i> 
				<span class="tab-title" style="color:#666">我的订单</span> 
			</a>
		</div>
		<script type="text/javascript" src="//js/zepto.min.js"></script>
		<script type="text/javascript">
			function setState(){
				$('#cooHosId').val() ? $('#yuanc').removeClass('disabled') : $('#yuanc').addClass('disabled');
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
					var dv=$(this).attr('data-val'),url='';
					if($(this).find('.ddList.disabled').size())return false;
					switch(dv){
						case '1':
							url='/wzjh/graphicCon?openid=${openid }';
							break;
						case '2':
							break;
						case '3':
							break;
						case '4':
							url='/wzjh/speciallist?openid=${openid}&cooHosId='+$('#cooHosId').val();
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
				$('#city_name').attr('data-id',data.chid).attr('data-value',data.city).text(data.city);
				$('#lcity').text(data.city)
					.closest('.citys').attr('data-id',data.chid).attr('data-value',data.city);
				if(data.chid){$('#cooHosId').val(data.chid);}
				setState();
			});
		}
    </script>
	</body>
</html>
