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
    <title>佰医汇</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/wx.css" />
    <link rel="stylesheet" href="/weui/weui.min.css"/>
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
    <link rel="stylesheet" href="/css/view/wxzjh.1.0.css"/>
  </head>  
  <body class="zjh001Body">
    <div class="zjh001Index">
    	<div class="topfilter">
    		<div class="innerfilter">
    			<div class="headerinfo box">
    				<div class="cols0"></div>
    				<div class="cols1">
    					<h3>全国知名卖家远程门诊</h3>
    					<p class="whitespace">借助先进的技术手段和软件系统，“佰医汇”平台……详情</p>
    				</div>
    			</div>
    			<div class="filterOptions">
    				<div class="filterop box">
    					<div class="cols0"><img src="/img/wximg/zjh1.0/zjh001002.png"></div>
    					<div class="cols1">
    						<div class="select">
    							<label class="input" placeholder="请选择所在城市">请选择所在城市</label>
    							<select name="city" id="city">
    								<c:forEach items="${coohos}" var="hos">
										<option value="${hos.id}">${hos.city}</option>
									</c:forEach>
    							</select>
    						</div>
    					</div>
    				</div>
    				<div class="filterop box">
    					<div class="cols0"><img src="/img/wximg/zjh1.0/zjh001003.png"></div>
    					<div class="cols1">
    						<div class="select">
    							<label class="input" placeholder="请选择当地合作医院">请选择当地合作医院</label>
    							<select name="">
    								<option value=""></option>
    							</select>
    						</div>
    					</div>
    				</div>
    				<div class="filterop box">
    					<div class="cols0"><img src="/img/wximg/zjh1.0/zjh001004.png"></div>
    					<div class="cols1">
    						<div class="select">
    							<label class="input" placeholder="请选择您需要就诊科室">请选择您需要就诊科室</label>
    							<select name="">
    								<option value=""></option>
    							</select>
    						</div>
    					</div>
    				</div>
    			</div>
    		</div>
    	</div>
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
    <script src="/js/base.js"></script>
    <script src="/js/jweixin-1.0.0.js"></script>
   	<script>
    	wx.config({
		    appId: '${appid}',
            timestamp: +'${timestamp}',
            nonceStr: '${nonceStr}',
            signature: '${signature}',
            jsApiList: [
              'getLocation'
            ]
		});
		wx.ready(function(){
			wx.getLocation({
			    type: 'wgs84',
			    success: function (res) {
			        var latitude = res.latitude;
			        var longitude = res.longitude;
			        gainLocation(latitude,longitude);
			    },
			    fail: function (res) {
			       //alert(JSON.stringify(res));
			    },
			    complete:function(){}
			});
		});
		wx.error(function(res){
			//alert(JSON.stringify(res));
		});
		
		function gainLocation(latitude,longitude){
			$.post("/wzjh/gainCityByLocation",{latitude:latitude,longitude:longitude},function(data){
				$('#city').prepend('<option value="'+ data.distcode +'">'+ data.city +'</option>');
			});
		}
    </script>
  </body>
</html>
