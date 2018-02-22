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
    <title>我的病例</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
	<link rel="stylesheet" href="/css/view/mycase.css" />
  	<script type="text/javascript">
  		!function(n){var e=n.document,t=e.documentElement,i=720,d=i/100,o="orientationchange"in n?"orientationchange":"resize",a=function(){var n=t.clientWidth||320;n>720&&(n=720),t.style.fontSize=n/d+"px"};e.addEventListener&&(n.addEventListener(o,a,!1),e.addEventListener("DOMContentLoaded",a,!1))}(window);
  	</script>
  </head>  
  <body>
    <div class="index">
    	<div id="orderList" class="newlist">
    		<c:forEach items="${cases}" var="order">
    			<a href="/wzjh/casedetail?oid=${order.id}" class="orderList box">
		    		<div class="cols0">
		    			<div class="inblock">
			    			<span>${fn:split(order.conDate,' ')[1] }</span>
			    			<span>${fn:split(order.conDate,'-')[1] }/${fn:split(fn:split(order.conDate,'-')[2],' ')[0]}</span>
			    			<span>${fn:split(order.conDate,'-')[0] }</span>
		    			</div>
		    		</div>
		    		<div class="cols1">
		    			<div class="headtxt">${order.localHosName}&emsp;${order.localDepName}</div>
		    			<div class="bodytxt">
		    				检查报告：${order.consultationResult}
		    			</div>
		    			<div class="foottxt box">
		    				<span class="cols1">名医面诊服务</span>
		    				<span class="cols1">${order.expertName}医生</span>
		    			</div>
		    			<span class="todetial iconfont">详情&#xe60a;</span>
		    		</div>
		    	</a>
    		</c:forEach>
    		<c:if test="${fn:length(cases)<=0}">
    			<div class="noresult">
					<img src="/img/mobile/kbzs/icon_noresult@2x.png" style="width:58px" alt=""/>
					<div>您还没有病例信息，到别处去看看。</div>
				</div>
    		</c:if>	
		</div>
		<c:if test="${fn:length(cases)>4}">
			<div id="moreLi">
				<a class="morePin moreBtn" id="moreA" onclick="moreInfo('orderList','moreLi')" href="javascript:void(0)">
	   				<span>展开更多病例信息</span>
	   			</a>
	  		</div>			
		</c:if>	
    </div>
	<script src="/js/jquery-1.11.0.min.js"></script>
	<script type="text/javascript">
		var _url='/';
	</script>
    <script src="/js/base.js"></script>
	<script>
    	var _b = {
    		href: '/',
    		oid:'${openid}',
    		dia: new _dialog(),
    		_post:function(url,ops,fun){
    			return _$(this.href + url,ops,fun);
    		}
    	};	
    	$(function(){
    		
    	});	
		function moreInfo(id,more,p){
			var out_d = $('#' + id),more_d = $('#' + more),
				pageno = out_d[0]['_pageNo'] || 1,nextP = parseInt(pageno) + 1;
			more_d.find('.moreBtn').hide().after(_b.loadimg());
			p && p == 1 && (nextP = p);
			_b._post('wzjh/morecases',{openid:_b.oid,pageNo: nextP},function(d){
				var ops = '';
				d.cases.length ? ($.each(d.cases,function(i,o){
					var cond = o.conDate;
					ops += '<a href="'+ _url +'wzjh/casedetail?oid='+ o.id +'" class="orderList box">\
					    		<div class="cols0">\
					    			<div class="inblock">\
						    			<span>'+ cond.split(' ')[1] +'</span>\
						    			<span>'+ cond.split('-')[1] +'/'+ cond.split(' ')[0].split('-')[2] +'</span>\
						    			<span>'+ cond.split('-')[0] +'</span>\
					    			</div>\
					    		</div>\
					    		<div class="cols1">\
					    			<div class="headtxt">'+ o.localHosName +'&emsp;'+ o.localDepName +'</div>\
					    			<div class="bodytxt">检查报告：'+ o.consultationResult +'</div>\
					    			<div class="foottxt box">\
					    				<span class="cols1">名医面诊服务</span>\
					    				<span class="cols1">'+ o.expertName +'医生</span>\
					    			</div>\
					    			<span class="todetial iconfont">详情&#xe60a;</span>\
					    		</div>\
					    	</a>';
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
  </body>
</html>
