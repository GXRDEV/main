<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html lang="en">
<head>
	<title>基本信息</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
</head>
<body class="baseinfoBody">
	<div class="baseinfoIndex">
		<div class="row-fluid">
			<div class="span3">
				<div class="section">
					<div class="header clearfix">
						<span class="span8 stitle">
							<i class="iconfont">&#xe600;</i>微信公号基本信息
						</span>
						<span class="span4 tright">
							<button class="btn btn-primary" id="saveWX">保存</button>
						</span>
					</div>
					<div class="bodyer clearfix">
						<form id="wxform" name="wxform" class="baseform" autocomplete="off" action="/hospital/wxbasic">
							<label class="controls required">
								<span>应用ID：</span>
								<input type="text" name="appid" autocomplete="off" value="${wt.appId }"/>
							</label>
							<label class="controls required">
								<span>应用密钥：</span>
								<input type="text" name="appsecret" autocomplete="off" onfocus="this.type='password'" value="${wt.appsecret }"/>
							</label>
							<label class="controls required">
								<span>商户号：</span>
								<input type="text" name="partner" autocomplete="off" value="${wt.partner }" />
							</label>
							<label class="controls required">
								<span>商户密钥：</span>
								<input type="text" name="partnerKey" autocomplete="off" onfocus="this.type='password'" value="${wt.partnerKey }" />
							</label>
						</form>
					</div>
				</div>
			</div>
			<div class="span9">
				<div class="section">
					<div class="header clearfix">
						<span class="span8 stitle">
							<i class="iconfont">&#xe600;</i>医院基本信息
						</span>
						<span class="span4 tright">
							<button class="btn btn-primary" id="saveHOS">保存</button>
						</span>
					</div>
					<div class="bodyer clearfix">
						<form id="hosform" name="hosform" class="baseform" action="/hospital/hosbasic">
							<div class="controls required">
								<span>院徽图标：</span>
								<div class="imgupload">
									<div class="thumb prelative">
										<img id="logourl" src="${hos.hospitalLogo }" />
										<div id="addHosIcon"><i class="iconfont">&#xe63f;</i></div>
									</div>
									<input type="hidden" name="logourl" value="${hos.hospitalLogo }"/>
								</div>
							</div>
							<div class="controls required">
								<span>医院简介：</span>
								<script id="introduction" name="introduction" type="text/plain">${hos.hospitalIntroduction }</script>
							</div>
						</form>
					</div>
				</div>
			
			</div>
		</div>
	</div>
	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="/libs/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="/libs/ueditor/ueditor.all.min.js"> </script>
    <script type="text/javascript" charset="utf-8" src="/libs/ueditor/ueditor.config.frazior.js"></script>
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '',hoseditor;
		seajs.use('view/admin/main',function(controller){
			controller.basicSet();
		});
	</script>
</body>
</html>

