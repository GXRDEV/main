<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="/">
    
    <title>测试容云聊天</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  	<button onclick="javascript:sendmessage();">发送消息</button>
  	<div id="message">
  		
  	</div>
  	<script src="/js/jquery.min.js"></script>
  	<script src="http://cdn.ronghub.com/RongIMLib-2.2.4.min.js"></script>
  	<script type="text/javascript" src="/imchat/im.js"></script>
  </body>
</html>
