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
    <title>进行中的订单</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<style>
		body{background-color: #ddd;}
		header{background-color:#fff;padding:16px 0;margin:10px 0;}		
		.tabs{border:1px solid #26B5ED;border-radius:4px;font-size:0;margin:0 20px;overflow:hidden;}
		.tabs a{display:inline-block;width:33.33%;font-size:14px;white-space:nowrap;border-left:1px solid #26B5ED;color:#26B5ED;text-align:center;box-sizing:border-box;padding:5px 0;font-weight:400}
		.tabs a.current{background-color:#26B5ED;color:#fff;}
		.tabs a:first-child{border-left:0;}
		li{background-color:#fff;margin-bottom:5px;color:#777;}
		.noresult{padding:40px 0;text-align:center;}
		.noresult div{padding:20px 0;}
		.headTop{padding:10px 0;margin:0 15px;font-size:12px;position:relative;}
		.headTop .btnTopGroup{position:absolute;right:-5px;top:4px;}
		.headTop .btnTop{display:inline-block;color:#EA4844;border:1px solid #EA4844;border-radius:3px;padding:3px 5px;background-color:#fff;min-width:4.6em;}
		.headTop .btnTop + .btnTop{margin-left:1px;}
				
		.bodyDiv{border-top:1px solid #ececec;border-bottom:1px solid #e9e9e9;padding:8px 0 10px;}
		.bodyDiv dl{margin:0 10px 0 15px;color:#888;}
		.bodyDiv dt{font-size:16px;font-weight:600;color:#10B5F0;padding:5px 0;}
		.bodyDiv dd{padding:5px 0 5px 5em;}
		.bodyDiv dd span{text-align:right;display:inline-block;width:100%;}
		.bodyDiv dd label{margin-left:-5em;}
		
		.footBottom{font-size:0}
		.footBottom span{display:inline-block;font-size:12px;width:18%;text-align:center;
		padding:50px 1% 20px;position:relative;color:#9CA4A6;overflow:hidden;white-space:nowrap;}		
		.footBottom span:after,.footBottom span:before{content:"";display:inline-block;position:absolute;top:20px;background-color:#9CA4A6;}
		.footBottom .passed{color:#10B5F0;}
		.footBottom .passed:after,.footBottom .passed:before{background-color:#10B5F0;}
		.footBottom span:after{width:100%;height:2px;right:0;top:27px;}
		.footBottom span:first-child:after{width:50%;}
		.footBottom span:last-child:after{width:50%;right:auto;left:0;}
		.footBottom span:before{width:16px;height:16px;border-radius:50%;left:50%;margin-left:-8px;}
		
		#moreA{display:block;text-align:center;padding:12px 0;color:#26B5F1;background-color:#fff;}
		#popupMenu{border-radius:5px;}
		#popupMenu div{font-size:0;border-top:1px solid #AEACAF;margin-top:10px;}
		#popupMenu p{margin:0 10px;padding:15px;color:#444;line-height:1.8em;font-size:14px;}
		#popupMenu div a{ display:inline-block;width:49.5%;text-align:center;padding:16px 0;color:#067EFE;font-size:17px;}
		#popupMenu div a:first-child{border-right:1px solid #AEACAF;}
		@media (max-width: 370px) {	
			.footBottom span{font-size:10px;}
		}
	</style>
  </head>  
  <body>
    <div id="index" class="index">
    	<header>
    		<p class="tabs">
    			<a href="/wtspz/myorders.do?openid=${openid}" class="current">进行中</a>
				<a href="/wtspz/mycompleteorders.do?openid=${openid}">已完成</a>
				<a href="/wtspz/mycancelorders.do?openid=${openid}">已取消</a>
    		</p>
    	</header>
    	<ul id="orderList">
    		<c:forEach items="${orders}" var="order">
    			<c:if test="${order.zhouStatus=='0'}">
    				<li class="orderList tocli" oid="${order.id}" onclick="jump(${order.id})">
    			</c:if>
    			<c:if test="${order.zhouStatus!='0'}">
    				<li class="orderList">
    			</c:if>
	    			<div class="headTop">
	    				<label>更新时间&ensp;${order.refreshTime}</label>
	    				<div class="btnTopGroup">
		    				<c:if test="${order.zhouStatus=='0'}">
		    					<button class="btnTop" type="button" onclick="showPop(${order.id});">取消订单</button>
		    				</c:if>
		    				<c:if test="${order.zhouStatus=='1'}">
		    					<button class="btnTop" type="button" onclick="showPop(${order.id})">取消订单</button>
		    					<button class="btnTop" type="button" onclick="topay(${order.id});">支付</button>
		    				</c:if>
		    				<c:if test="${order.zhouStatus=='2'}">
		    					<button class="btnTop" type="button" onclick="showPop(${order.id})">取消订单</button>
		    				</c:if>
		    				<c:if test="${order.zhouStatus=='4'}">
		    					<a class="btnTop" href="/wtspz/toestimate/${order.id}" >评价</a>
		    				</c:if>	    				
	    				</div>
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
	    			<c:if test="${order.zhouStatus!='0'}">
	    			 <div class="footBottom">
	    				<c:if test="${order.zhouStatus=='1'}">
	    					<span class="passed">已分配</span>
		    				<span>已支付</span>
		    				<span>服务开始</span>
		    				<span>服务结束</span>
	    				</c:if>
	    				<c:if test="${order.zhouStatus=='2'}">
	    					<span class="passed">已分配</span>
		    				<span class="passed">已支付</span>
		    				<span>服务开始</span>
		    				<span>服务结束</span>
	    				</c:if>
	    				<c:if test="${order.zhouStatus=='3'}">
	    					<span class="passed">已分配</span>
		    				<span class="passed">已支付</span>
		    				<span class="passed">服务开始</span>
		    				<span>服务结束</span>
	    				</c:if>
	    				<c:if test="${order.zhouStatus=='4'}">
	    					<span class="passed">已分配</span>
		    				<span class="passed">已支付</span>
		    				<span class="passed">服务开始</span>
		    				<span class="passed">服务结束</span>
	    				</c:if>
		    			<span>已评价</span>
	    			</div></c:if>
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
	<div class="layout_div g_fixed" onclick="hidePop()" style="background:#000;opacity:0.6;height:10000px;z-index:10000;display:none;">&nbsp;</div>	
	<div id="popupMenu" class="popup-div" style="z-index:10000;background-color:#fff;display:none;margin-top:-100px;">
		<p>
			<b>是否确定要取消本次订单？</b><br/>
			取消后，如有付款则所有款项自动退回到您的付款帐户内。
		</p>
		<div>
			<a href="javascript:void(0);" style="color:#353535;" onclick="hidePop()"> 取 消 </a>
			<a href="javascript:void(0);" style="color:#10B5F0;" id="cancelDeal"> 确 定 </a>
		</div>
	</div>
	<form action="/wtspz/topay.do" method="post" id="postform">
		<input type="hidden" name="orderid" id="forderid"/>
	</form>
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
    	$(function(){
    		$('#cancelDeal').click(function(){
    			var oid = $(this).attr('data-id');
    			oid &&( $(this).html('<img src="'+ _b.href +'img/mobile/loading2.gif" alt="" style="width:15px;"/>') ) 
    			&& _b._post('wtspz/cancelOrder.do',{orderid:oid},function(d){
					_gr(_b.href + 'wtspz/mycancelorders.do?openid=${openid}');
				},function(){
					$('#cancelDeal').html('确定');
				});
    		});
    		$('.tocli').click(function(){
    			location.href='/wtspz/todistribute/'+$(this).attr('oid');
    		});
    	});	
    	function jump(orderid){
    		location.href='/wtspz/todistribute/'+orderid;
    	}
    	function topay(orderid){
    		$("#forderid").val(orderid);
    		$("#postform").submit();
    	}
    	
		function showPop(oid){
			$('#cancelDeal').attr('data-id',oid);
			$('#popupMenu').addClass('g_fixed center').show();
			$('.layout_div').show();
		}			
		function hidePop(){
			$('.popup-div').removeClass('g_fixed').hide();
			$('.layout_div').hide();
		}
		function moreInfo(id,more,p){
			var out_d = $('#' + id),more_d = $('#' + more),
				pageno = out_d[0]['_pageNo'] || 1,nextP = parseInt(pageno) + 1;
			more_d.find('.moreBtn').hide().after(_b.loadimg());
			p && p == 1 && (nextP = p);

			_b._post('wtspz/moreorders.do',{openid:_b.oid,flag:_b.flg,pageNo: nextP},function(d){
				var ops = '';
				d.orders.length ? ($.each(d.orders,function(i,o){
					var sign = o.zhouStatus,btn='<button class="btnTop" type="button" onclick="showPop('+o.id+')">取消订单</button>',
						pross='<span class="{1}">已分配</span><span class="{2}">已支付</span><span class="{3}">服务开始</span><span class="{4}">服务结束</span>';
					switch(sign){
						case "1":
							pross=pross.replace(/\{[1]{1}\}/g,'passed');
							btn=btn+'<button class="btnTop" type="button" style="width:60px;" onclick="topay('+o.id+');">支付</button>';
							break;
						case "2":
							pross=pross.replace(/\{[12]{1}\}/g,'passed');
							break;
						case "3":
							pross=pross.replace(/\{[123]{1}\}/g,'passed');
							btn='';
							break;
						case "4":
							pross=pross.replace(/\{[1234]{1}\}/g,'passed');
							btn = '<a class="btnTop" href="/wtspz/toestimate/'+o.id+'" >已评价</a>';
							break;
					}
					if(sign=='0'){
						ops += '<li class="orderList tocli" onclick="jump('+o.id+')">';
						pross='';
					}else{
						ops += '<li class="orderList">'
					}
					var showname='',pzlevel='';
					if(o.serviceName=='排队挂号'){
						showname=o.hospital+'&emsp;'+o.department;
						pzlevel='挂号加号';
					}else if(o.serviceName=='导医陪诊'){
						showname=o.serviceAddress;
						pzlevel='陪诊';
					}
					ops +='<div class="headTop">\
				    				<label>更新时间&ensp;'+ o.refreshTime +'</label>\
				    				'+ btn +'\
				    			</div>\
				    			<div class="bodyDiv">\
				    				<dl>\
				    					<dt>'+ '&emsp;'+ showname+'</dt>\
				    					<dd><label>预约时间</label><span style="color:#26B5ED;">'+ o.orderTime +'</span></dd>\
				    					<dd><label>陪诊级别</label><span>'+ pzlevel +'</span></dd>\
				    				</dl>\
				    			</div>\
				    			<div class="footBottom">';
				    			if(sign!='0'){
				    				ops += pross+'<span>已评价</span>'
				    			}
				    			ops +='</div></li>';
				}),out_d[0]['_pageNo'] = nextP,d.orders.length > 9 && more_d.find('.moreBtn').show()) : (more_d.hide());
				more_d.find('.loading').remove();
				ops && (nextP == 1 ? out_d.html(ops) : out_d.append(ops));
			});
		}
	</script>
  </body>
</html>
