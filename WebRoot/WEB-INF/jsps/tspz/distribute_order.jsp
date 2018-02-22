<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html>
	<head lang="en">
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width,  user-scalable=no">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta content="telephone=no" name="format-detection" />
  <title>等待护士接单 </title>
  <link rel="stylesheet" type="text/css" href="/css/mobile.css" />
  <link rel="stylesheet" type="text/css" href="/css/icon.css" />
  <style>
  	body{background-color:#F4F4F4;margin:6px;}
    .outerDiv{position:relative; background-color:#0CCBD5;border-radius:5px;width:100%; max-width:414px;min-height:480px;margin:0 auto;font-size:14px;}
    .innerDiv{
      width:160px;height:160px;background-color:#fff;text-align:center;z-index:2;
      border-radius:50%;position:absolute;left:0;right:0;bottom:0;top:0;margin:auto;}
    .innerDiv label{font-size:54px;font-weight:600;color:#E96060;display:block;padding:30px 0 10px;}
    .innerDiv span{display:block;padding:0 3em;line-height:1.5em;color:#444;}
    .innerDiv:after,.circleDiv:before,.circleDiv:after{content:""; display:block;border-radius:50%;position:absolute;}
    .innerDiv:after{width:200px;height:200px;border:2px solid #fff;top:-21px;left:-21px;}
    .circleDiv{
      width:240px;height:240px;z-index:1;border-radius:50%;position:absolute;left:0;right:0;bottom:0;top:0;margin:auto;
      transform-origin: center center; animation: spin 1000ms infinite linear;
      -webkit-transform-origin: center center; -webkit-animation: spin 1000ms infinite linear;
    }
    .lc,.rc{content:""; display:block;width:50%;height:100%;position:absolute;top:0;}
    .lc{right:0;background-color:#ccc;background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #28D1D8), color-stop(1, #29D2D9));border-radius:0 120px 120px 0;}
    .rc{left:0;background-color:#fff;background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #28D1D8), color-stop(1, #fff)); border-radius:120px 0 0 120px;}
    .circleDiv:before{width:236px;height:236px;background-color:#0CCBD5;z-index:3;top:2px;left:2px;}
    .circleDiv:after{width:14px;height:14px;background-color:#fff;bottom:-6px;left:50%;margin-left:-7px;z-index:10;}

    .menuDiv{position:relative;}
    .menuDiv > div{position:absolute;top:20px;text-align:center;color:#fff;}
    .menuDiv > div label{display:block;font-size:13px;padding:0.5em 0;}
    .orderDetail{left:20px;}
    .orderCancel{right:20px;}
    .icon-list,.icon-close{position:relative; display:inline-block;width:40px;height:40px;border-radius:50%;border:1px solid #fff;margin:0 auto;}
    .icon-close{transform:rotate(45deg);-webkit-transform:rotate(45deg);}
    .icon-close:after,.icon-close:before{content:"";background-color:#fff;display:block;position:absolute;left:0;top:0;bottom:0;right:0;margin:auto;}
    .icon-close:after{width:2px;height:20px;}
    .icon-close:before{width:20px;height:2px;}
    
    .icon-list b,.icon-list b:after,.icon-list b:before{
      content:"";display:block;width:3px;height:2px;border-left:2px solid #fff;border-right:14px solid #fff;
      position:absolute;left:0;top:0;bottom:0;right:0;margin:auto;
    }
    .icon-list b:after{margin-top:-6px;margin-left:-2px;}
    .icon-list b:before{margin-bottom:-6px;margin-left:-2px;}
    
    .tims{width:100%;font-size:24px;white-space:nowrap;padding-bottom:0.5em;}
    .otherMark{position:absolute;width:100%;bottom:0;text-align:center;color:#fff;font-size:14px;padding:0.5em 0;}
    .otherMark p{visibility:hidden;}
	.otherMark p input{border:0;padding:6px 8px;width:55%;border-radius:3px;}
	.otherMark p button{border:0;background-color:#ddd;padding:6px 20px;border-radius:3px;}
	#newpriceon{opacity:0;width:0;height: 0;}
    #newpriceon:checked ~ p{visibility:visible}
    @keyframes spin {
      0%   { transform: rotate(360deg); }
      100% { transform: rotate(0deg); }
    }
    @-webkit-keyframes spin {
      0%   { -webkit-transform: rotate(360deg); }
      100% { -webkit-transform: rotate(0deg); }
    }
  </style>
  <style>
	  	.dd-lists{display: -moz-box;  display: -webkit-box;  display: box; margin:0;padding:10px 0;}
	  	.dd-lists + .dd-lists{border-top:1px dashed #B7EAED;}
		.thumb{width: 90px; text-align:center;}
		.thumb img{width:70px;border-radius:50%;}
		.content{-webkit-box-flex: 1; box-flex: 1;color:#aaa;font-size:13px;}
		.c_name{font-size:16px;color:#444;font-weight:600;display: inline-block;}
		.c_name:after{content:"";display:inline-block;width:16px;height:16px;vertical-align: sub;margin:0 5px;
			background:url(/img/share/Female_16.png) no-repeat 0 0;background-size:12px 12px;}
		.csex0:after{background-image:url(/img/share/Female_16.png)}
		.csex1:after{background-image:url(/img/share/Male_16.png)}
		.c_zw{display:inline-block;border-radius:10px;border:1px solid #bbb;color:#bbb;padding:1px 6px;margin-right:10px;font-size:11px;}
		.c_p_other{padding:6px 0;border-top:1px solid #eee;border-bottom:1px solid #eee;margin:5px 10px 0 0;color:#888;}
		.c_star_p{padding:10px 0 1px;}
		.c_price_p{color:#666;padding-top:4px;}
		.other{width: 60px; color:#888;font-size:12px;position:relative;text-align:center;}
		.other p{position:absolute;left:0;right:0;top:0;bottom:0;margin:auto;height:64%;}
		.o_price{font-size:18px;color:#E14C6A;}
		.btnClassblue{background-color:#0CCBD3;color:#fff;font-size:13px;border-radius:4px;border:0;padding:5px 10px;margin-top:2px;}

		.icon_stars{display:inline-block;height:10px;width:58px;background-image:url(/img/mobile/kbzs/icon_stars.png);}
		.icon_stars1{background-position:-45px 0;}
		.icon_stars2{background-position:-35px 0;}
		.icon_stars3{background-position:-24px 0;}
		.icon_stars4{background-position:-12px 0;}
		.icon_stars5{background-position:0 0;}
  </style>
  <style>
  	.reault{border:1px solid #059098;background-color:#fff; margin: 10px auto; border-radius: 4px;width:100%; max-width:414px;}
  	.reault dl{margin:0;}
  	.dt{background-color:#059098;font-size:13px;padding:10px 5px;line-height:1.5em; color:#fff}
  </style>
	<script src="/libs/zepto.min.js"></script>
  	<script src="/js/base.js"></script>
  <script>
  	var _b = {
	   href: '/',
	   oid: '${orderid}'
	};
  	var t = 0;//单位秒
    function loadTimer(){
      var d = document.querySelector(".tims"),
      timer = window.setInterval(function(){
      	var h = 0,m = 0;
      	t += 60;
      	if(t > 59 * 60){
      		h = parseInt(t/3600,10);
      		m = parseInt((t/3600 - h) * 60,10);
        	d.innerHTML = '已耗时 '+ (h > 0 && ( h + '小时')) + (m > 0 && ( m + '分钟'));
      	}else{      	
        	d.innerHTML = '已耗时 '+ parseInt(t/60,10) +' 分钟';
      	}
      },60000);      
    }
    function showDetail(){
      location.href="/wtspz/myorders?openid=${openid}";
    }    
    function cancelDetail(){
		_$(_b.href + 'wtspz/cancelOrder.do',{orderid:_b.oid},function(d){
			 _gr(_b.href + 'wtspz/mycancelorders.do?openid=${openid}');
		});
    }    
  </script>
</head>
<body onload="loadTimer()">
  <div class="outerDiv">
    <div class="menuDiv">
      <div class="orderDetail" onclick="showDetail()">
        <i class="icon-list"><b></b></i>
        <label>订单详情</label>
      </div>
      <div class="orderCancel" onclick="cancelDetail()">
        <i class="icon-close"></i>
        <label>取消订单</label>
      </div>      
    </div>
    <div class="innerDiv">
        <label id="renum" class="renum">0</label>
        <span>位医护人员已收到</span>  
    </div>
    <div class="circleDiv">
      <div class="lc"></div>
      <div class="rc"></div>      
    </div>
    <div class="otherMark">
        <div class="tims">已耗时1分钟</div>
        <input type="checkbox" id="newpriceon" name="newpriceon" value="1"/>      
      	<label for="newpriceon">如长时间未有人接单，请调整您的报价 <i class="icon icon-edit" style="color:#fff;font-size:14px;"></i></label>
      	<p style="padding:0.5em 0 0;">
      		<input type="number" name="newprice" id="newprice" placeholder="新的报价"/>
      		<button type="button" class="btncls" id="btnsend" onclick="setPrice()">推送</button>
      	</p>
    </div>
  </div>
  <div class="reault" id="reault">
  	<div class="dt">
  		已有&nbsp;<b id="renum2" style="color:red;">${renum}</b>&nbsp;名医护人员申请接单，请选择一名：
  	</div>
  	<dl>
		<c:forEach items="${nurses}" var="nurse">
	  		<dd class="dd-lists">
				<div class="thumb">
					<img src="/img/share/Nurse.png"/>
					<button class="btnClassblue" onclick="selectedNurse(${nurse.id})">选TA</button>
				</div>
				<div class="content">
					<p>
						<span class="c_name csex1">${nurse.realName }</span>
						<span class="c_zw">${nurse.gradeName}</span>
						<span>${nurse.age}岁</span>
					</p>
					<p class="c_star_p">
						<span class="c_dis">已服务<b style="color:#0CCBD3;">${nurse.serverNumber }</b>次&emsp;</span>
						<span class="icon_stars icon_stars4"></span>
						<span>4.0分</span>
					</p>
	    			<c:if test="${fn:length(nurse.beGoodAt)>0}">
						<p class="c_p_other">
							<span class="c_other">${nurse.beGoodAt}</span>
						</p>
					</c:if>
	    			<c:if test="${fn:length(nurse.beGoodAt)<=0}">
						<p class="c_p_other">
							<span class="c_other">待添加</span>
						</p>
					</c:if>
					<p class="c_price_p">
						服务费用：<span class="o_price">${order.fixedPrice}</span>元
					</p>
				</div>
			</dd>
		</c:forEach>
  	</dl>
  </div>
  <script src="/libs/zepto.min.js"></script>
  <script src="/js/base.js"></script>
  <script>
    var _b = {
   		href: '/',
   		oid: '${orderid}'
   	};
   	function selectedNurse(nurseid){
    	_$(_b.href + 'wtspz/selectedNurse',{orderid:_b.oid,nurseid:nurseid},function(d){
		  	_gr(_b.href + 'wtspz/myorders?openid=${openid}');
		});		
   	}
  	function listerN(){
  		_$(_b.href + 'wtspz/gainNurseDatas.do',{orderid:_b.oid},function(d){
  			$('#renum').text() != d.renum && (document.querySelector('#renum').innerHTML = d.renum);
  			setTimeout(listerN,5000);
  		});
  	}	
  	listerN();
  	
  	function syncNurLis(){
   		$('dd.news').removeClass('news');
   		_$(_b.href + 'wtspz/syncNurLis',{orderid:_b.oid},function(d){
   			var dds = '';
   			if(d.nurses.length>0){
   				$('#renum2').text(d.nurses.length);
   				$('dl').empty();
   			}else{
   				$('#renum2').text(0);
   				$('dl').empty();
   			}
   			//$('#renum2').text(parseInt($('#renum2').text() || $('.dd-lists').size() || 0) + (d.nurses.length || 0));
	  		d.nurses.length && $.each(d.nurses,function(i,o){
	  			dds += '<dd class="dd-lists news">\
							<div class="thumb">\
								<img src="'+ _b.href +'img/share/Nurse.png"/>\
								<button class="btnClassblue" onclick="selectedNurse('+ o.id +')">选TA</button>\
							</div>\
							<div class="content">\
								<p>\
									<span class="c_name csex1">'+ o.realName +'</span>\
									<span class="c_zw">'+ o.gradeName +'</span>\
									<span>'+  o.age +'岁</span>\
								</p>\
								<p class="c_star_p">\
									<span class="c_dis">已服务<b style="color:#0CCBD3;"> '+o.serverNumber+' </b>次&emsp;</span>\
									<span class="icon_stars icon_stars4"></span>\
									<span>4.0分</span>\
								</p>\
								<p class="c_p_other">\
									<span class="c_other">'+(o.beGoodAt || '待添加')+'</span>\
								</p>\
								<p class="c_price_p">\
									服务费用：<span class="o_price">'+ ${order.fixedPrice} +'</span>元\
								</p>\
							</div>\
						</dd>';
	  		});
	  		dds && $('dl').prepend(dds);
	  		setTimeout(syncNurLis,10000);
	  	});
   	}
   	syncNurLis();
   	
   	function setPrice(){
   		var iv = document.querySelector('#newprice'),v = iv.value;
   		v ? _$(_b.href + 'wtspz/adjustprice',{price:v,orderid:_b.oid},function(d){
   			document.querySelector('#newpriceon').checked = false;
   			iv.style.backgroundColor = '#fff';
   			$('.o_price').text(v);
   		}) : (iv.style.backgroundColor = '#FAE0E0');
   	}
  </script>
</body>
</html>