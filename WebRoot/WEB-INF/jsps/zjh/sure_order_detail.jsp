<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE HTML>
<html>
  <head lang="en">
    <base href="/">
    <meta charset="utf-8">
	<meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
	<meta content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
	<meta content="email=no" name="format-detection">
	<title>顶级医生</title>
	<link rel="stylesheet" type="text/css" href="/css/mobile.css?v=1.0" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<style>	
		body{background-color:#EFEFEF;}    	
		.basecss{border:1px solid #E1E1E1;border-radius:5px;margin:10px;background-color:#fff;overflow:hidden;}
		.basep{margin:5px 10px;color:red;opacity:.6;}
		.controls{position:relative;display:block; margin:0 6px;border-bottom:1px solid #E8E8E8;line-height:4em;color:#999;font-size:13px;}
		.controls span{display:block;}
		.controls .clabel{position:absolute;left:0;top:50%;margin-top:-2em; width:4.4em;text-align:right;color:#333;}
		.controls .contorl{margin-left:5.6em;}
		.controls .contorl input,
		.controls .contorl select{width:100%;height:2em;border:0;outline:0;}
		.select{position:relative;}
		.select .select_input{position:relative; margin-right:40px;z-index:2;}
		.select .select_select{position:absolute;left:0;top:0;height:100%;width:100%;z-index:1;}
		.color605{color:#39D167;}
		.g_fixed {background-color:#EFEFEF;z-index: 10;}
		.g_fixed .cols0{display:block;margin: 6px 10px;}
		.g_fixed .btn2{border:0;background:#00CC99 url();color:#fff;font-weight:600;font-size: 16px;width:100%;height:40px}
		.g_fixed .btn2 span{color:#FEFD65;}
	</style>
  </head>  
  <body>
    <div class="index">
		<div class="forminfo basecss">
			<label class="controls">
				<span class="clabel">姓名</span>
				<span class="contorl">${coodep.userName}</span>
			</label>
			<label class="controls">
				<span class="clabel">联系电话</span>
				<span class="contorl">${coodep.telphone}</span>
			</label>
		</div>
		<div class="mianinfo basecss">
			<label class="controls">
				<span class="clabel">医院</span>
				<span class="contorl box">
					<span class="cols1">${expert.hosName}</span>
					<span class="cols0"><a href="javascript:void(0)" onclick="tomap('','','')" class="iconfont color605">&#xe605;</a></span>
				</span>
			</label>
			<label class="controls">
				<span class="clabel">科室</span>
				<span class="contorl">${cooHos.displayName}</span>
			</label>
			<label class="controls">
				<span class="clabel">医生</span>
				<span class="contorl">${expert.specialName}</span>
			</label>
			<label class="controls">
				<span class="clabel">预约时间</span>
				<span class="contorl">${condate }&emsp;${contime}</span>
			</label>
			<label class="controls">
				<span class="clabel">挂号费</span>
				<span class="contorl" style="color:red;">10元</span>
			</label>
		</div>
		<p class="basep">挂号费用  您实际就医时再缴费。</p>
    </div>
	<script src="/js/jquery-1.11.0.min.js"></script>
	<script src="/js/base.js"></script>
	
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
		function tomap(name,lat,lon){
			wx.openLocation({
			    latitude: lat, // 纬度，浮点数，范围为90 ~ -90
			    longitude: lon, // 经度，浮点数，范围为180 ~ -180。
			    name: name, // 位置名
			    address: '', // 地址详情说明
			    scale: 4, // 地图缩放级别,整形值,范围从1~28。默认为最大
			    infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
			});
			return false;
		}
    </script>
  </body>
</html>
