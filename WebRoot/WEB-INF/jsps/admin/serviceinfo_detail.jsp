<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta charset="UTF-8">
		<title>
			服务信息详情
		</title>
    	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" href="/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/fonticon/base/iconfont.css" />
		<link rel="stylesheet" href="/css/view/video.css" />
		<link rel="stylesheet" href="/sea-modules/webuploader/webuploader.css" />
		<style type="text/css">
			body{background:#fafafa;}	
			.divblock{background:#fff;}
			.divblock .bodyer{padding:5px 0;}
			.dialog .bodyer{padding-top:2.8em;height:auto;min-height:100px;}
			.userinfo .row-fluid .span6{width: 49.80617%;}
			.userinfo .row-fluid [class *="span"]{margin-left: 0.12766%;}
			#pics{margin:0 15px;}
		</style>
	</head>
	<body>
		<div class="container-fluid userinfo">
			<div class="row-fluid">				
			<div class="span12">
				<div class="reportresult section">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>服务信息
							</span>
						</div>
						<div class="bodyer form-horizontal">
							<div class="control-group">
								 <label class="control-label">服务名称：</label>
								 <div class="controls">
								    <span class="readonly">${order.serviceName}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">服务所含包名：</label>
								 <div class="controls">
								    <span class="readonly">${order.packageName}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">服务状态：</label>
								 <div class="controls">
								    <span class="readonly">
							      	<c:choose>
							      		<c:when test="${order.status==0 }">未启用</c:when>
							      		<c:when test="${order.status==1 }">启用</c:when>
							      	</c:choose>
							      </span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">医生分成：</label>
								 <div class="controls">
								    <span class="readonly">${order.doctorDivided}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">专家分成：</label>
								 <div class="controls">
								    <span class="readonly">${order.expertDivided}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">平台分成：</label>
								 <div class="controls">
								    <span class="readonly">${order.platformDivided}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">备注：</label>
								 <div class="controls">
								    <span class="readonly">${order.remark}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">分账说明：</label>
								 <div class="controls">
								    <span class="readonly">${order.serviceNote}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">服务类型：</label>
								 <div class="controls">
								    <span class="readonly">
							      	<c:choose>
							      		<c:when test="${order.groupType==1 }">患者服务</c:when>
							      		<c:when test="${order.groupType==2 }">医生协作</c:when>
							      	</c:choose>
							      </span>
								 </div>
							</div>
						</div>	
					</div>	
				</div>
			</div>
		</div>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
	</body>
</html>
