
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
		<title>
			医生团队审核详情
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
					<div class="reportresult section divblock">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>医生团队信息
							</span>
						</div>
						<div class="bodyer form-horizontal">
							<div class="control-group">
							    <label class="control-label">团队名称：</label>
							    <div class="controls">
							      <span class="readonly">${dt.teamName }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">申请人：</label>
							    <div class="controls">
							      <span class="readonly">${dt.docName}/${dt.docDuty }/${dt.docProfession }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">申请人类型：</label>
							    <div class="controls">
							      <span class="readonly">
							      	<c:choose>
							      		<c:when test="${dt.applicantType==2 }">专家</c:when>
							      		<c:when test="${dt.applicantType==3 }">医生</c:when>
							      	</c:choose>
							      </span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">申请人医院：</label>
							    <div class="controls">
							      <span class="readonly">${dt.hosName }/${dt.hosLevel }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">申请人科室：</label>
							    <div class="controls">
							      <span class="readonly">${dt.depName }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">团队擅长：</label>
							    <div class="controls">
							      <span class="readonly">${dt.speciality }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">团队简介：</label>
							    <div class="controls">
							      <span class="readonly">${dt.profile }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">关键字：</label>
							    <div class="controls">
							      <span class="readonly">${dt.keywords }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">logo：</label>
							    <div class="controls">
							      <span class="readonly">
							      	<img alt="" src="${dt.logoUrl }" style="width:100px;height:100px;">
							      </span>
							    </div>
							</div>
							
						</div>
					</div>
				</div>
			</div>
			 <div class="form-action">
							<button type="button" class="audit btn btn-primary" sval="1">审核通过</button>
							<button type="button" class="btn btn-danger" sval="-1" data-toggle="modal" data-target="#myModal">审核不通过</button>
			  </div>		
		</div>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none">
				<div class="modal-dialog">
						<div class="modal-content">
								<div class="modal-body" style="text-align: center">
										<textarea rows="10" class="reason" style="width: 95%; resize: none"></textarea>
								</div>
								<div class="modal-footer" style="text-align: center">
										<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
										<button type="button" class="btn btn-primary audit" sval="-1">提交</button>
								</div>
						</div>
				</div>
		</div>						
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
		<script>
			$(document).ready(function () {     
					$('body').delegate('.audit','click',function(){
							var sval=$(this).attr("sval");
							var reason = $('.reason').val()
							var obj =  sval == 1 ? {tid:'${dt.id}',sval:sval} : {tid:'${dt.id}',sval:sval, refusalReason: reason}
							if(confirm("请确认该医生团队是否审核通过？！")){	
									$.post('/system/changedtstatus', obj)
									.done(function(data){
											location.href="/system/doctorteamaudit"
									})
							}
					})
			});
		</script>
	</body>
</html>
