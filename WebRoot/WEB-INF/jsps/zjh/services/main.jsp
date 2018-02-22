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
    <title>六安市便民就医服务</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<link rel="stylesheet" type="text/css" href="/fonticon/tspzmobile/iconfont.css" />
	<style>
		body{background-color:#F0EFED;}		
		section{margin-top:6px;font-size:16px;color:#343434;}
   		.serverlist{position:relative;display:block;padding-right:36px;background:#fff;border-bottom:1px solid #E0E0E0;box-sizing:border-box;line-height:4em;}
   		.serverlist:after{content:"";display:block;position:absolute;right:16px;top:50%;margin-top:-6px;
   			width:10px;height:10px;border:1px solid #979797;transform:rotate(45deg);-webkit-transform:rotate(45deg);border-left:0;border-bottom:0;}
   		.serverlist .sign{position:absolute;right:2.2em;top:50%;font-size:13px; transform:translateY(-50%);color:#888}
   		.serverlist .iconfont{display:inline-block; margin:0 10px 0 15px;color:#45AFAD;text-align:center;}
   		.serverlist:active{background-color:#F6F6F6;}
	</style>
  </head>  
  <body>
  	<div id="index" class="index">
  		<section>
  			<a class="serverlist" href="http://www.taozis.com:80/wkanbzs/updatas/">
				<i class="iconfont" style="color:#3676A4;font-size:20px;vertical-align: -2px;">&#xe609;</i>
				<span>预约挂号</span>
				<span class="sign">免费</span>
			</a>
			<a class="serverlist" href="http://www.taozis.com:80/wkanbzs/myorders?openid=">
				<i class="iconfont" style="color:#139D84;font-size:22px;">&#xe616;</i>
				<span>查看报告单</span>
			</a>
  		</section>
	</div>
	<script src="/js/base.js" type="text/javascript"></script>
  </body>
</html>
