
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
			医联体申请-详情
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
		<div class="container-fluid userinfo" style="min-height: 0">
			<div class="row-fluid">				
				<div class="span12">			
					<div class="reportresult section divblock">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>申请信息
							</span>
						</div>
						<div class="bodyer form-horizontal">
							<div class="control-group">
							    <label class="control-label">医联体名称：</label>
							    <div class="controls">
							      <span class="readonly">${hha.yltName}</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">申请人：</label>
							    <div class="controls">
							      <span class="readonly">${hha.applicant}</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">申请人类型：</label>
							    <div class="controls">
							      <span class="readonly">
							      	<c:choose>
							      		<c:when test="${hha.applicantType==5 }">医院管理员</c:when>
							      		<c:otherwise>企业管理员</c:otherwise>
							      	</c:choose>
							      </span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">申请医院：</label>
							    <div class="controls">
							      <span class="readonly">${hha.hosName}</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">申请医院级别：</label>
							    <div class="controls">
							      <span class="readonly">${hha.hosLevel}</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">状态：</label>
							    <div class="controls">
							      <span class="readonly">
							      	<c:choose>
							      		<c:when test="${hha.status==1 }">审核通过</c:when>
							      		<c:when test="${hha.status==-1 }">审核不通过</c:when>
							      		<c:when test="${hha.status==-2 }">下线</c:when>
							      		<c:otherwise>待审核</c:otherwise>
							      	</c:choose>
							      </span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">Logo：</label>
							    <div class="controls">
							      <span class="readonly">
							      	<img alt="" src="${hha.iconUrl }">
							      </span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">简介：</label>
							    <div class="controls">
							      <span class="readonly">
							      	${hha.profile }
							      </span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">擅长：</label>
							    <div class="controls">
							      <span class="readonly">
							      	${hha.speciality }
							      </span>
							    </div>
							</div>
						</div>
						<div class="footer" style="text-align: center; padding-bottom: 20px">
							<a  href="javascript:void(0);" sval="1" class="btn btn-primary hha-audit" style="margin-right: 10px">审核通过</a>			
							<a  href="javascript:void(0);" sval="-1" class="btn btn-danger hha-audit">审核不通过</a>	
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
			$(document).ready(function () {   
				$('body').delegate('.hha-audit','click',function(){
		        	var hid='${hha.id}',sval=$(this).attr("sval");
		        	if(confirm("请确认该医联体的审核状态是否更改？！")){	
		        		$.post('/system/changeHosHealthStatus',{hid:hid,sval:sval},function(data){
		            		location.href='/system/hoshealthaudit';
		            	});
		        	}
		        })
			});
		</script>
	</body>
</html>
