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
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, height=device-height, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0">
  <link rel="stylesheet" href="https://res.wx.qq.com/open/libs/weui/1.1.0/weui.min.css">
  <title>佰医汇</title>
  <script>location.replace('/module/remote.html#/main/${openid}/${docid}')</script>
</head>
<body>
  <div id="app">
    <div class="weui-flex" style="height:100vh;align-items: center; justify-content: center;">
      <div style="text-align:center;"><img src="https://develop.ebaiyihui.com/img/website/icon@2x.png" style="width:100px;" /></div>
    </div>
  </div>
  <script src="//res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</body>
</html>
