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
	<head lang="en">
		<meta charset="UTF-8">
		<title>报告详情</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<style type="text/css">
			.noresult{padding:50px 0;text-align:center;opacity:.6;color:#777}
		</style>
	</head>
	<body>
		<div class="noresult">
			<img src="/img/mobile/kbzs/icon_noresult@2x.png" style="width:58px" alt=""/>
			<h2>我们正在努力建设中o^_^o</h2>
		</div>
	</body>
</html>
