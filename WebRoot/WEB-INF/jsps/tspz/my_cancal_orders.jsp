<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html>
  <head>
    <base href="/">
    <title>我的取消订单</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<style>
		body{background-color: #ddd;}
		header{background-color:#fff;padding:16px 0;margin:10px 0;}
		.icon_clear{background:url(/img/mobile/kbzs/icon_delete@2x.png) no-repeat;display:inline-block; width:18px;height:16px;vertical-align: sub;background-size:16px 16px;}
		
		.tabs{border:1px solid #26B5ED;border-radius:4px;font-size:0;margin:0 20px;overflow:hidden;}
		.tabs a{display:inline-block;width:33.33%;font-size:14px;white-space:nowrap;border-left:1px solid #26B5ED;
			color:#26B5ED;text-align:center;box-sizing:border-box;padding:5px 0;font-weight:400}
		.tabs a.current{background-color:#26B5ED;color:#fff;}
		.tabs a:first-child{border-left:0;}
		
		li{background-color:#fff;margin-bottom:10px;color:#777;}
		.noresult{padding:40px 0;text-align:center;}
		.noresult div{padding:20px 0;}
		.headTop{padding:10px 0;margin:0 15px;font-size:12px;position:relative;}
		.headTop .stateS{display:inline-block;padding:3px 5px;position:absolute;right:0;top:6px;font-weight:700}
		
		.bodyDiv{border-top:1px solid #ececec;border-bottom:1px solid #e9e9e9;padding:8px 0 10px;}
		.bodyDiv dl{margin:0 10px 0 15px;color:#888;}
		.bodyDiv dt{font-size:16px;font-weight:600;color:#10B5F0;padding:5px 0;}
		.bodyDiv dd{padding:5px 0 5px 5em;}
		.bodyDiv dd span{text-align:right;display:inline-block;width:100%;}
		.bodyDiv dd label{margin-left:-5em;}
		
		.footBottom{font-size:0;margin:0 20px;}
		.footBottom span{display:inline-block;font-size:12px;width:50%;padding:10px 0;position:relative;color:#666;}
		.footBottom span:last-child{text-align:right;}
		#moreA{display:block;text-align:center;padding:12px 0;color:#26B5F1;background-color:#fff;}

	</style>
  </head>  
  <body>
    <div id="index" class="index">
    	<header>
    		<p class="tabs">
    			<a href="/wtspz/myorders.do?openid=${openid}">进行中</a>
				<a href="/wtspz/mycompleteorders.do?openid=${openid}">已完成</a>
				<a href="/wtspz/mycancelorders.do?openid=${openid}" class="current">已取消</a>
    		</p>
    	</header>
    	<ul id="orderList">
    		<c:forEach items="${orders}" var="order">
    		<li class="orderList">
    			<div class="headTop">
    				<label>取消时间&ensp;${order.refreshTime}</label>
    				<span class="stateS">
    					<c:if test="${order.status==52}">
    						患者取消
    					</c:if>
    					<c:if test="${order.status==53}">
    						护士取消
    					</c:if>
    					<c:if test="${order.status==54}">
    						已退款
    					</c:if>
    				</span>
    			</div>
    			<div class="bodyDiv">
    				<dl>
    						<c:if test="${order.serviceName=='排队挂号'}">
	    						<dt>${order.hospital}&emsp;${order.department }</dt>
		    					<dd><label>预约时间</label><span style="color:#26B5ED;">${order.orderTime}</span></dd>
		    					<dd><label>类别</label><span>挂号加号</span></dd>
		    				</c:if>
		    				<c:if test="${order.serviceName=='导医陪诊'}">
	    						<dt>${order.serviceAddress}</dt>
		    					<dd><label>预约时间</label><span style="color:#26B5ED;">${order.orderTime}</span></dd>
		    					<dd><label>类别</label><span>陪诊</span></dd>
		    				</c:if>
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
		<c:if test="${fn:length(orders)>9}">
			<div id="moreLi">
				<a class="morePin moreBtn" id="moreA" onclick="moreInfo('orderList','moreLi')" href="javascript:void(0)">
	   				<span>展开更多订单</span>
	   			</a>
	  		</div>			
		</c:if>	
    </div>
	<script src="/libs/jquery-1.11.0.min.js"></script>
	<script src="/js/base.js"></script>
	<script>
    	var _b = {
    		href: '/',
    		oid:'${openid}',
    		flg:'${pro}',
    		loadimg:function(){
    			return '<a class="loading" style="display:block;font-size:12px;line-height:26px;text-align:center;padding:10px 0;background-color:#fff;"><img src="'+ this.href +'img/mobile/loading2.gif" alt="" style="width:24px;vertical-align: top;"/>正在加载</a>';
    		},
    		_post:function(url,ops,fun){
    			return _$(this.href + url,ops,fun);
    		}
    	};	
		function moreInfo(id,more,p){
			var out_d = $('#' + id),more_d = $('#' + more),
				pageno = out_d[0]['_pageNo'] || 1,nextP = parseInt(pageno) + 1;
			more_d.find('.moreBtn').hide().after(_b.loadimg());
			p && p == 1 && (nextP = p);

			_b._post('wtspz/moreorders.do',{openid:_b.oid,flag:_b.flg,pageNo: nextP},function(d){
				var ops = '';
				d.orders.length ? ($.each(d.orders,function(i,o){
					var showname='',pzlevel='',showtip='';
					if(o.serviceName=='排队挂号'){
						showname=o.hospital+'&emsp;'+o.department;
						pzlevel='挂号加号';
					}else if(o.serviceName=='导医陪诊'){
						showname=o.serviceAddress;
						pzlevel='陪诊';
					}
					
					switch(o.status+''){
						case "52":
							showtip='患者取消';
							break;
						case "53":
							showtip='护士取消';
							break;
						case "54":
							showtip='已退款';
							break;
					}
					ops += '<li class="orderList">\
				    			<div class="headTop">\
				    				<label>取消时间&ensp;'+ o.refreshTime +'</label>\
				    				<span class="stateS">'+showtip+'</span>\
				    			</div>\
				    			<div class="bodyDiv">\
				    				<dl>\
				    					<dt>'+ showname+'</dt>\
				    					<dd><label>预约时间</label><span style="color:#26B5ED;">'+ o.orderTime +'</span></dd>\
				    					<dd><label>陪诊级别</label><span>'+ pzlevel +'</span></dd>\
				    				</dl>\
				    			</div>\
				    		</li>';
				}),out_d[0]['_pageNo'] = nextP,d.orders.length > 9 && more_d.find('.moreBtn').show()) : (more_d.hide());
				more_d.find('.loading').remove();
				ops && (nextP == 1 ? out_d.html(ops) : out_d.append(ops));
			});
		}
	</script>
  </body>
</html>
