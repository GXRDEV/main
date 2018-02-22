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
  <body class="myordersDetailBody mypingjBody">
    <div id="pingjdetail" class="hashdiv">
    	<div id="twdocinfo" class="topdocdetail hhlist box">
			<div class="cols0"><span class="thumb">				
				<c:if test="${fn:length(special.listSpecialPicture) <= 0 }">				
					<img src="https://develop.ebaiyihui.com:443/img/defdoc.jpg">
				</c:if>
				<c:if test="${fn:length(special.listSpecialPicture) > 0 }">				
					<img src="${special.listSpecialPicture }">
				</c:if>
			</span></div>
			<div class="cols1">
				<p class="whitespace"><b>${special.specialName }</b>&emsp;<span>${special.depName }&emsp;${special.duty }</span></p>
				<p class="whitespace"><span>${special.hosName }</span></p>
			</div>
		</div>
		<form action="/wzjh/saveestimate" method="post" id="myform">
			<input type="hidden" name="oid" value="${oid }"/>
			<input type="hidden" name="ltype" value="${ltype }"/>
			<input type="hidden" name="openid" value="${openid }"/>
			<div class="form-group box">
				<label class="cols0">评价星级：</label>
				<span class="cols1">
					<span class="stars">
						<i class="iconfont icon-xingxing"></i>
						<input type="radio" name="grade" value="1"/>
						<i class="iconfont icon-xingxing"></i>
						<input type="radio" name="grade" value="2"/>
						<i class="iconfont icon-xingxing"></i>
						<input type="radio" name="grade" value="3"/>
						<i class="iconfont icon-xingxing"></i>
						<input type="radio" name="grade" value="4"/>
						<i class="iconfont icon-xingxing"></i>
						<input type="radio" name="grade" value="5" checked/>
					</span>
				</span>
			</div>
			<div class="form-group box" style="margin-top:10px;">
				<label class="cols0">标签：</label>
				<span class="cols1 signs">
					<c:forEach items="${dtags }" var="dtag">
						<label class="sign"><input type="checkbox" name="tagids" value="${dtag.id }"><span>${dtag.content }</span></label>					
					</c:forEach>
				</span>
			</div>
			<div class="form-group">
				<textarea name="content" placeholder="评价内容"></textarea>
			</div>
			<div class="g_fixed" style="background:#FFFFFD;">
				<button type="button" id="pingjme">评价</button>
			</div>
		</form>
    </div>
    <div id="remark">
    	<p>评价一星时，代表差评，请留下您的联系方式，方便我们客服联系您。</p>
    	<p class="form-group box">
    		<label class="cols0">客服电话：</label>
    		<span class="cols1"><a href="tel:400-631-9377">400-631-9377</a><span>(9:00-21:00)</span></span>
    	</p>
    </div>
    <div style="height:50px;">&ensp;</div>
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
	        <p class="weui_toast_content">数据保存中</p>
	    </div>
	</div>
	<script type="text/javascript">
		var _href = '/',_h = _href,
			_config = {
				openid:'${openid}',oid:'${oid}',flag:'complete',type:'${ltype}'
			};
	</script>
	<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
    <script src="/js/base.js"></script>
	<script>
		seajs.use('view/wx/twtelupload',function(aa){
			aa.myorderpj();
		});
	</script>
  </body>
</html>
