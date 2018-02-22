<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width,  user-scalable=no">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta content="telephone=no" name="format-detection" />
  <title>护士选择</title>
  <link rel="stylesheet" type="text/css" href="/css/mobile.css" />
  <style>
    .menuDiv{position:relative;height:80px;}
    .menuDiv > div{position:absolute;top:0;text-align:center;color:#03CAE0;}
    .menuDiv > div label{display:block;font-size:13px;padding:0.5em 0;text-shadow:0 0 5px #fff;}
    .menuDiv > .orderCancel label{color:#888;}
    .orderDetail{left:40px;}
    .orderCancel{right:40px;}
    .icon-list,.icon-close{position:relative; display:inline-block;width:40px;height:40px;border-radius:50%;border:1px solid #03CAE0;margin:0 auto;background-color:#fff;}
    .icon-close{transform:rotate(45deg);-webkit-transform:rotate(45deg);border:1px solid #888;}
    .icon-close:after,.icon-close:before{content:"";background-color:#888;display:block;position:absolute;left:0;top:0;bottom:0;right:0;margin:auto;}
    .icon-close:after{width:2px;height:20px;}
    .icon-close:before{width:20px;height:2px;}
    
    .icon-list b,.icon-list b:after,.icon-list b:before{
      content:"";display:block;width:3px;height:2px;border-left:2px solid #03CAE0;border-right:14px solid #03CAE0;
      position:absolute;left:0;top:0;bottom:0;right:0;margin:auto;
    }
    .icon-list b:after{margin-top:-6px;margin-left:-2px;}
    .icon-list b:before{margin-bottom:-6px;margin-left:-2px;}
	
	.dd-lists{display: -moz-box;  display: -webkit-box;  display: box; border-bottom:1px solid #ccc;margin:0 10px;padding:10px 0;}
	.thumb{width: 90px; text-align:center;}
	.thumb img{width:70px;border-radius:50%;}
	.content{-webkit-box-flex: 1; box-flex: 1;color:#aaa;font-size:13px;}
	.c_name{font-size:16px;color:#444;font-weight:600;}
	.c_name:after{content:"";display:inline-block;width:16px;height:16px;vertical-align: sub;margin:0 5px;
		background:url(/img/share/Female_16.png) no-repeat 0 0;}
	.csex0:after{background-image:url(/img/share/Female_16.png)}
	.csex1:after{background-image:url(/img/share/Male_16.png)}
	.c_zw{display:inline-block;border-radius:10px;border:1px solid #bbb;color:#bbb;padding:1px 8px;font-size:10px;}
	.c_p_other{padding:8px 0 3px;border-top:1px solid #ddd;margin:5px 10px 0 0;}
	.c_star_p{padding:5px 0;}
	.c_other:before{content:"技";display:inline-block;width:1.4em;height:1.4em;border-radius:50%;
		background-color:#F7E833;color:#fff;font-size:13px;text-align:center;line-height:1.4em;margin-right:0.5em}
	.other{width: 70px; color:#888;font-size:12px;position:relative;text-align:center;}
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
</head>
<body>
	<div class="index">
		<dl>
			<c:forEach items="${nurses}" var="nurse">
				<dd class="dd-lists">
					<div class="thumb">
						<img src="/img/share/Nurse.png"/>
					</div>
					<div class="content">
						<p>
							<span class="c_name csex1">${nurse.realName}</span>
							<span class="c_zw">${nurse.gradeName}</span>
						</p>
						<p class="c_star_p">
							<span class="icon_stars icon_stars4"></span>
							<span class="c_dis">已服务${nurse.serverNumber}次</span>
						</p>
						<p>
							<span class="c_age">年龄${nurse.age}  &nbsp; 好评率${nurse.overallScore}%</span>
						</p>
						<p class="c_p_other">
							<span class="c_other">${nurse.beGoodAt}</span>
						</p>
					</div>
					<div class="other">
						<p>
							<span class="o_price">${order.fixedPrice}</span>元
							<button class="btnClassblue" onclick="selectedNurse(${nurse.id})">选TA</button>
						</p>
					</div>
				</dd>
			</c:forEach>
		</dl>
	</div>
	<div class="menuDiv g_fixed">
      <div class="orderDetail" onclick="showDetail()">
        <i class="icon-list"><b></b></i>
        <label>订单详情</label>
      </div>
      <div class="orderCancel" onclick="cancelDetail()">
        <i class="icon-close"></i>
        <label>取消订单</label>
      </div>      
    </div>
    <script src="/libs/zepto.min.js"></script>
  	<script src="/js/base.js"></script>
    <script>
    	var _b = {
	   		href: '/',
	   		oid: '${order.id}'
	   	};
    	function showDetail(){
    		location.href="/wtspz/myorders?openid=${openid}";
    	}
    	function selectedNurse(nurseid){
    		_$(_b.href + 'wtspz/selectedNurse',{orderid:_b.oid,nurseid:nurseid},function(d){
		  		_gr(_b.href + 'wtspz/myorders?openid=${openid}');
		  	});
    	}
    	(function syncNurLis(){
    		_$(_b.href + 'wtspz/syncNurLis',{orderid:_b.oid,time:+new Date},function(d){
    			var dds = '';
		  		d.nurses.length && $.each(d.nurses,function(i,o){
		  			dds += '<dd class="dd-lists">\
								<div class="thumb">\
									<img src="'+ _b.href +'img/share/Nurse.png"/>\
								</div>\
								<div class="content">\
									<p>\
										<span class="c_name csex1">'+ o.realName +'</span>\
										<span class="c_zw">'+ o.gradeName +'</span>\
									</p>\
									<p class="c_star_p">\
										<span class="icon_stars icon_stars4"></span>\
										<span class="c_dis">已服务'+o.serverNumber+'次</span>\
									</p>\
									<p>\
										<span class="c_age">年龄'+  o.age +'  &nbsp; 好评率'+o.overallScore+'%</span>\
									</p>\
									<p class="c_p_other">\
										<span class="c_other">'+o.beGoodAt+'</span>\
									</p>\
								</div>\
								<div class="other">\
									<p>\
										<span class="o_price">'+ ${order.fixedPrice} +'</span>元\
										<button class="btnClassblue" onclick="selectedNurse('+ o.id +')">选TA</button>\
									</p>\
								</div>\
							</dd>';
		  		});
		  		dds && $('dl').append(dds);
		  		setTimeout(syncNurLis,60000);
		  	});
    	})();
    </script>
</body>
</html>