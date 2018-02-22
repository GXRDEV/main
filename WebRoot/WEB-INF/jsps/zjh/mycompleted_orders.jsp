<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<!DOCTYPE html>
<html>
  <head>
    <base href="/">
    <title>已完成的订单</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
  	<link rel="stylesheet" href="/css/view/myorders.css" type="text/css"/>
  	<script type="text/javascript">
  		!function(n){var e=n.document,t=e.documentElement,i=720,d=i/100,o="orientationchange"in n?"orientationchange":"resize",a=function(){var n=t.clientWidth||320;n>720&&(n=720),t.style.fontSize=n/d+"px"};e.addEventListener&&(n.addEventListener(o,a,!1),e.addEventListener("DOMContentLoaded",a,!1))}(window);
  	</script>
  </head>  
  <body>
    <div class="index">
    	<header>
    		<p class="tabs">
    			<a href="/wzjh/myorders?openid=${openid}">进行中</a>
				<a href="/wzjh/mycompletedorders?openid=${openid}"  class="current">已完成</a>
				<a href="/wzjh/mycancelorders?openid=${openid}">已取消</a>
    		</p>
    	</header>
    	<ul id="orderList" class="newlist">
    		<c:forEach items="${orders}" var="order">
    			<li class="orderList">
	    			<div class="headTop box">
	    				<label>订单ID：${order.id}</label>
	    				<div class="cols1">
	    					<c:choose>
	    						<c:when test="${order.status==4}">
	    							第二次下单完成
	    						</c:when>
	    						<c:when test="${order.status==5}">
	    							第三次下单完成
	    						</c:when>
	    						<c:otherwise>已完成</c:otherwise> 
	    					</c:choose>
		    			</div>
	    			</div>
	    			<div class="bodyDiv">
	    				<dl>
	    					<dd><label>患者姓名：</label><span>${order.patientName }</span></dd>
			    			<dd><label>完成时间</label><span><fmt:formatDate value="${order.refreshTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span></dd>
	    					<dd><label>就诊时间：</label><span>${order.conDate }</span></dd>
	    					<dd class="hosp"><label>就诊医院：</label><span>
	    						<b>${order.localHosName }</b>
	    						<a href="javascript:void(0)" onclick="tomap('${order.localHosName }','${order.hosAddress }',${order.lat },${order.lng })"><i class="iconfont">&#xe61d;</i><b>导航</b></a>
	    						<a href="javascript:void(0)" data-address="${order.hosAddress}"><b>详细地址</b></a>
	    					</span></dd>
	    					<dd><label>就诊科室：</label><span>${order.localDepName }</span></dd>
	    					<dt class="split"></dt>
	    					<dd class="docs"><label>专家信息：</label><span>${order.expertName }
	    						<b>${order.duty}</b>
	    						<b>${order.profession}</b>
	    					</span></dd>
	    					<dd><label>所在医院：</label><span>${order.hosName } &emsp;${order.depName }</span></dd>	    					
	    				</dl>
	    			</div>
	    		</li>
    		</c:forEach>
    		<c:if test="${fn:length(orders)<=0}">
    			<li class="noresult">
					<img src="/img/mobile/kbzs/icon_noresult@2x.png" style="width:58px" alt=""/>
					<div>您还没有订单信息，到别处去看看。</div>
				</li>
    		</c:if>	
		</ul>
		<c:if test="${fn:length(orders)>4}">
			<div id="moreLi">
				<a class="morePin moreBtn" id="moreA" onclick="moreInfo('orderList','moreLi')" href="javascript:void(0)">
	   				<span>展开更多订单</span>
	   			</a>
	  		</div>			
		</c:if>	
    </div>
	<script src="/js/jquery-1.11.0.min.js"></script>
    <script src="/js/base.js"></script>
	<script>
    	var _b = {
    		href: '/',
    		oid:'${openid}',
    		flg:'${flag}',
    		dia: new _dialog(),
    		limg:'<img src="/img/mobile/loading2.gif" alt="" style="width:15px;"/>',
    		loadimg:function(){
    			return '<a class="loading" style="display:block;font-size:12px;line-height:26px;text-align:center;padding:10px 0;background-color:#fff;"><img src="'+ this.href +'img/mobile/loading2.gif" alt="" style="width:24px;vertical-align: top;"/>正在加载</a>';
    		},
    		_post:function(url,ops,fun){
    			return _$(this.href + url,ops,fun);
    		}};	
    	$(function(){
    		$('body').delegate('[data-address]','click',function(){
    			var ads = this.getAttribute('data-address');
    			ads && _b.dia.alert.show({
    				modelEvent:true,
    				text:ads
    			});
    		});
    	});	
		function moreInfo(id,more,p){
			var out_d = $('#' + id),more_d = $('#' + more),
				pageno = out_d[0]['_pageNo'] || 1,nextP = parseInt(pageno) + 1;
			more_d.find('.moreBtn').hide().after(_b.loadimg());
			p && p == 1 && (nextP = p);
			_b._post('wzjh/moreorders',{openid:_b.oid,flag:_b.flg,pageNo: nextP},function(d){
				var ops = '';
				d.orders.length ? ($.each(d.orders,function(i,o){
					var sign = o.status,btn='',condate='',contime='',refreshtime='';
					switch(sign){
						case 4: condate=o.secondConsultationDate;btn='第二次下单';break;
						case 5: condate=o.thirdRefreshTime;btn='第三次下单';break;
					}
					ops += '<li class="orderList">\
				    			<div class="headTop box">\
				    				<label>订单ID：'+ o.id +'</label>\
				    				<div class="cols1">'+btn+'</div>\
				    			</div>\
				    			<div class="bodyDiv">\
				    				<dl>\
				    					<dd><label>患者姓名：</label><span>' + o.patientName  + '</span></dd>\
				    					<dd><label>完成时间：</label><span>' + datestring(o.refreshTime)  + '</span></dd>\
				    					<dd><label>就诊时间：</label><span>' + o.conDate  + '</span></dd>\
				    					<dd class="hosp"><label>就诊医院：</label><span>\
				    						<b>' + o.localHosName  + '</b>\
				    						<a href="javascript:void(0)" onclick=tomap("' + o.localHosName  + '","' + o.hosAddress  + '",' + o.lat  + ',' + o.lng  + ')><i class="iconfont">&#xe61d;</i><b>导航</b></a>\
				    						<a href="javascript:void(0)" data-address="' + o.hosAddress + '"><b>详细地址</b></a>\
				    					</span></dd>\
				    					<dd><label>就诊科室：</label><span>' + o.localDepName  + '</span></dd>\
				    					<dt class="split"></dt>\
				    					<dd class="docs"><label>专家信息：</label><span>' + o.expertName  + '\
				    						<b>' + o.duty + '</b>\
				    						<b>' + o.profession+ '</b>\
				    					</span></dd>\
				    					<dd><label>所在医院：</label><span>' + o.hosName + ' &emsp;' + o.depName  + '</span></dd>\
				    				</dl>\
				    			</div>\
				    		</li>';
				}),out_d[0]['_pageNo'] = nextP,d.orders.length >4 && more_d.find('.moreBtn').show()) : (more_d.hide());
				more_d.find('.loading').remove();
				ops && (nextP == 1 ? out_d.html(ops) : out_d.append(ops));
			});
		}
		function datestring(longtime){
			var oDate = new Date(longtime);
			var year=oDate.getFullYear();
			var month=oDate.getMonth()+1;
			month=month< 10 ? "0" + month:month;
			var _date=oDate.getDate();
			var hour=oDate.getHours()< 10 ? "0" + oDate.getHours(): oDate.getHours();
			var min=oDate.getMinutes()< 10 ? "0" + oDate.getMinutes(): oDate.getMinutes();
			var second=oDate.getSeconds()< 10 ? "0" + oDate.getSeconds():oDate.getSeconds();
			return year+"-"+month+"-"+_date+" "+hour+":"+min+":"+second;
		}
	</script>	
	<script src="/js/jweixin-1.0.0.js"></script>
    <script>
    	wx.config({
		    appId: '${appid}',
            timestamp: +'${timestamp}',
            nonceStr: '${nonceStr}',
            signature: '${signature}',
            jsApiList: [
              'openLocation'
            ]
		});
		wx.ready(function(){
			
		});
		wx.error(function(res){
			//alert(JSON.stringify(res));
		});		
		function tomap(name,add,lat,lon){
			wx.openLocation({
			    latitude: lat, // 纬度，浮点数，范围为90 ~ -90
			    longitude: lon, // 经度，浮点数，范围为180 ~ -180。
			    name: name, // 位置名
			    address: add, // 地址详情说明
			    scale: 26, // 地图缩放级别,整形值,范围从1~28。默认为最大
			    infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
			});
			return false;
		}
    </script>
  </body>
</html>
