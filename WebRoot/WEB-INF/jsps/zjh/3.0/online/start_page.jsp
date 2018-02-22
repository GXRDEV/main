<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//通过request["code"]得到code，通过code得到用户的基本信息
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>
    	<c:choose>
    		<c:when test="${ltype == '1'}">图文问诊</c:when>
    		<c:otherwise>电话问诊</c:otherwise>
    	</c:choose>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
	<link rel="stylesheet" type="text/css" href="/css/wx.css" />
    <link rel="stylesheet" href="/weui/weui.min.css"/>
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
    <link rel="stylesheet" href="/css/view/wxzjh.css"/>
</head>
<body class="twtelBody">
	<div class="twtelIndex">
		<div class="searchbar">
			<form class="searchform" method="get" action="/wzjh/depintodocs">
				<i class="iconfont">&#xe603;</i>
				<input type="search" class="searchinput" placeholder="搜索医生" name="searchContent"/>
				<button type="submit" class="querybtn atarget">搜索</button>
				<input type="hidden" value="${ltype}" name="ltype"/>
				<input type="hidden" value="${openid}" name="openid"/>
			</form>
		</div>
		<h4 class="title">科室医生</h4>
		<dl class="keshis clearfix">
			<c:forEach items="${departs}" var="dep">
				<dd><div class="kelist">
				<a class="href atarget" href="/wzjh/depintodocs?depid=${dep.id }&ltype=${ltype}&openid=${openid}"></a>
				<div class="thumb">
				<c:choose>
					<c:when test="${empty dep.remark }">
						<img style="width: auto;max-width: 100%;" src="//placehold.it/60x60"/>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${fn:contains(dep.remark,'://')}">
								<img src="${fn:replace(dep.remark,'http://','https://')}"/>
							</c:when>
							<c:otherwise>
								<img class="old" src="http://wx.15120.cn/SysApi2/Files/${dep.remark}" />
							</c:otherwise>
						</c:choose>	
					</c:otherwise>
				</c:choose>
				</div>
				<label>${dep.displayName}</label>
			</div></dd>
			</c:forEach>
		</dl>
	</div>	
	<!-- loading toast -->
	<div id="loadingToast" class="weui_loading_toast" style="display:none;">
	    <div class="weui_mask_transparent"></div>
	    <div class="weui_toast">
	        <div class="weui_loading">
	            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
	        </div>
	        <p class="weui_toast_content">数据加载中</p>
	    </div>
	</div>
	<div id="showModel" style="display:none;margin:10px;"></div>
	<script type="text/javascript">
		var _burl = '/',oid = '${openid}';
	</script>
	<script src="/js/jquery-1.11.0.min.js"></script>
    <script src="/js/base.js"></script>
    <script type="text/javascript">
		$(document).ready(function(){
			$('body').delegate('.atarget','click',function(){
				$('#loadingToast').show();
			});
		});
    </script>
</body>
</html>