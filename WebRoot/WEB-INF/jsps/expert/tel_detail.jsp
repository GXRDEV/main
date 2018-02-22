<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String _basePath="https://localhost:8443/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta charset="UTF-8">
		<title>电话订单详情</title>
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
					<div class="reportresult section divblock">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>基本信息
							</span>
						</div>
						<div class="bodyer form-horizontal">
							<div class="control-group">
							    <label class="control-label">姓名：</label>
							    <div class="controls">
							      <span class="readonly">${cinfo.contactName }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">病例：</label>
							    <div class="controls">
							      <span class="readonly">${cinfo.caseName }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">性别：</label>
							    <div class="controls">
							      <span class="readonly"><c:if test="${cinfo.sex != '1' }">女</c:if><c:if test="${cinfo.sex == '1' }">男</c:if></span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">年龄：</label>
							    <div class="controls">
							      <span class="readonly">${cinfo.age }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">电话：</label>
							    <div class="controls">
							      <span class="readonly">${cinfo.telephone }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">身份证：</label>
							    <div class="controls">
							      <span class="readonly">${cinfo.idNumber }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">病情描述：</label>
							    <div class="controls">
							      <span class="readonly">${cinfo.presentIll }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">咨询目的：</label>
							    <div class="controls">
							      <span class="readonly">${cinfo.askProblem }</span>
							    </div>
							</div>
						</div>
					</div>
				</div>
			</div>
					
			<div class="row-fluid">				
				<div class="span12">
					<div class="someimg section divblock">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>病例图片
							</span>
						</div>
						<div class="bodyer">
							<div id="pics" class="diyUpload hasimgview">
								<div class="parentFileBox">
									<ul class="fileBoxUl">	
										<c:forEach items="${images }" var="im">													
											<li data-id="${im.id}" class="browser">
												<div class="viewThumb">	
													<c:choose>
														<c:when test="${fn:contains(im.fileUrl,'://')}">
															<img src="${fn:replace(im.fileUrl,'http://','https://')}"/>
														</c:when>
														<c:otherwise>
															<img src="http://wx.15120.cn/SysApi2/Files/${im.url}" />
														</c:otherwise>
													</c:choose>																
												</div>														
											</li>
										</c:forEach>
									</ul>
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
		<script>
			var _href = '/',_h = _href;
			seajs.use('view/admin/order',function(controller){
				controller.tuwen();
			}); 
		</script>
	</body>
</html>
