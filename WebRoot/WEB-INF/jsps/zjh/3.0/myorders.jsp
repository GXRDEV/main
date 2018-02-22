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
    <title>我的订单</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/wx.css" />
    <link rel="stylesheet" href="/weui/weui.min.css"/>
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
    <link rel="stylesheet" href="/css/view/wxzjh.css"/>
  </head>  
  <body class="myordersBody">
    <div class="myordersIndex">
    	<div id="navbar">
			<div class="bd" style="height: 100%;">
			    <div class="weui_tab">
			        <div class="weui_navbar">
			            <div data-flag="processing" class="weui_navbar_item weui_bar_item_on">进行中</div>
			            <div data-flag="complete" class="weui_navbar_item">已完成</div>
			            <div data-flag="cancel" class="weui_navbar_item">已取消</div>
			        </div>
			        <div class="weui_tab_bd"></div>
			    </div>
			</div>
		</div>
    	<ul id="orderList" class="newlist"></ul>
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
	<script type="text/javascript">
		var _burl = '/',oid = '${openid}';
	</script>
	<script src="/js/jquery-1.11.0.min.js"></script>
	<script src="/js/infinite.scroll/jquery.endless-scroll-1.3.js"></script>
    <script src="/js/base.js"></script>
	<script src="/js/view/wxzjhorder.js"></script>
  </body>
</html>
