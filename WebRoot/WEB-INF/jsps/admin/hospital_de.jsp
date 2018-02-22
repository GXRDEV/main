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
	<title>医院详情</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=7vuYwc8NzPaF1DZi1ZZxXWfWdzhjpXOl"></script>
</head>
<body class="exportdetail hosdetailpass">
	<form class="row-fluid form-horizontal"  method="post" id="myform">
		<input type="hidden" name="hosid" value="${special.id }"/>
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<h3>	
				<c:if test="${empty special.auditStatus }">入驻医院审核</c:if>
				<c:if test="${!empty special.auditStatus }">医院详情</c:if>
				</h3>								
			</div>
			<div class="bodyer">
				<div class="section">
					<div class="bodyer clearfix">
						 <div class="control-group">
						    <label class="control-label">医院名称：</label>
						    <div class="controls">
						    	<label class="inputval">${special.displayName }</label>
			    			</div>
			 			 </div>
			 			 <c:if test="${!empty special.shortName }">
				 			 <div class="control-group">
							    <label class="control-label">医院简称：</label>
							    <div class="controls">
							    	<label class="inputval">${special.shortName }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.hospitalLevel }">
				 			 <div class="control-group">
							    <label class="control-label">医院等级：</label>
							    <div class="controls">
						    		<select name="hospitalLevel" data-pid="3" class="input js-ajax" data-val="${special.hospitalLevel }" disabled></select>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.hosType }">
				 			 <div class="control-group">
							    <label class="control-label">医院类型：</label>
							    <div class="controls">
						    		<select name="hosType" data-pid="20" class="input js-ajax" data-val="${special.hosType }" disabled></select>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.hosProperty }">
				 			 <div class="control-group">
							    <label class="control-label">医院性质：</label>
							    <div class="controls">
						    		<select name="hosProperty" data-pid="19" class="input js-ajax" data-val="${special.hosProperty }" disabled></select>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.hosWebSite }">
				 			 <div class="control-group">
							    <label class="control-label">医院官网：</label>
							    <div class="controls">
							    	<label class="inputval">${special.hosWebSite }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.dailyOutpatientNumber }">
				 			 <div class="control-group">
							    <label class="control-label">日门诊量：</label>
							    <div class="controls">
							    	<label class="inputval">${special.dailyOutpatientNumber }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.dailyAppointNumber }">
				 			 <div class="control-group">
							    <label class="control-label">日预约量：</label>
							    <div class="controls">
							    	<label class="inputval">${special.dailyAppointNumber }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.externalAppointmentApproach }">
				 			 <div class="control-group">
							    <label class="control-label">上级转诊医院：</label>
							    <div class="controls">
							    	<label class="inputval">${special.externalAppointmentApproach }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.distCode }">	 			 
			 			 <div class="control-group">
						    <label class="control-label">所在省市：</label>
						    <div class="controls">
						    	<input type="hidden" name="distCode" value="${special.distCode }"/>
						    	<select name="pro" class="bindchange" disabled data-type="city" data-val="${special.distCode }">
						    		<option value=""></option>
						    	</select>
						    	<select name="city" class="bindchange" disabled data-val="${special.distCode }">
						    		<option value=""></option>
						    	</select>
			    			</div>
			 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.hosProfile }">
				 			 <div class="control-group">
							    <label class="control-label">医院简介：</label>
							    <div class="controls">
							    	<label class="inputval">${special.hosProfile }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.location }">
				 			 <div class="control-group">
							    <label class="control-label">地址：</label>
							    <div class="controls doublewidth">
							    	<label class="inputval">${special.location }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.lat }">
				 			 <div class="control-group">
							    <label class="control-label">经纬度：</label>
							    <div class="controls">
							    	<label class="inputval">${special.lat }，${special.lng }</label>
				    			</div>
				 			 </div>	
			 			 </c:if>
			 			 <c:if test="${!empty special.characteristicClinic }">		 			 
				 			 <div class="control-group">
							    <label class="control-label">特色门诊：</label>
							    <div class="controls">
							    	<label class="inputval">${special.characteristicClinic }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.contactorName }">
				 			 <div class="control-group">
							    <label class="control-label">联系人姓名：</label>
							    <div class="controls">
							    	<label class="inputval">${special.contactorName }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.contactorDuty }">
				 			 <div class="control-group">
							    <label class="control-label">联系人职务：</label>
							    <div class="controls">
							    	<label class="inputval">${special.contactorDuty }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.contactorTelphone }">
				 			 <div class="control-group">
							    <label class="control-label">联系人电话：</label>
							    <div class="controls">
							    	<label class="inputval">${special.contactorTelphone }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.contactorEmail }">
				 			 <div class="control-group">
							    <label class="control-label">联系邮箱：</label>
							    <div class="controls">
							    	<label class="inputval">${special.contactorEmail }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.remark }">	 			 
				 			 <div class="control-group">
							    <label class="control-label">备注：</label>
							    <div class="controls doublewidth">
							    	<label class="inputval">${special.remark }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${!empty special.keywords }">
				 			 <div class="control-group">
							    <label class="control-label">关键词：</label>
							    <div class="controls doublewidth">
							    	<label class="inputval">${special.keywords }</label>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <div class="control-group">
						    <label class="control-label">合作医院：</label>
						    <div class="controls">
						    	<label class="inputval">
						    		<c:if test="${special.isCooHospital == '1' }">是</c:if>
						    		<c:if test="${special.isCooHospital != '1' }">否</c:if>
						    	</label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">对接模式：</label>
						    <div class="controls">
						    	<c:if test="${special.dockingMode == '1'}">简单对接</c:if>
						    	<c:if test="${special.dockingMode == '2'}">深度对接</c:if>
			    			</div>
			 			 </div>
			 			 <c:if test="${!empty special.authorizeFile }">
				 			 <div class="control-group" style="margin-bottom:20px;">
							    <label class="control-label">授权文件：</label>
							    <div class="controls">
							    	<div id="picsbigPicture" class="diyUpload">
										<div class="parentFileBox">
											<ul class="fileBoxUl clearfix">
												<c:forEach items="${authorizeFiles}" var="im">
													<li data-id="${im.id}" class="browser">
														<div class="viewThumb">
															<img src="${im.fileUrl}" />
														</div>
														<div class="diyFileName">${im.fileName}</div>
													</li>
												</c:forEach>
											</ul>
										</div>
									</div>
				    			</div>
				 			 </div>	
			 			 </c:if>
			 			 <c:if test="${!empty special.bigPicture }">
				 			 <div class="control-group" style="margin-bottom:20px;">
							    <label class="control-label">相关照片：</label>
							    <div class="controls">
							    	<div id="picsbigPicture" class="diyUpload">
										<div class="parentFileBox">
											<ul class="fileBoxUl clearfix">
												<c:forEach items="${images}" var="im">
													<li data-id="${im.id}" class="browser">
														<div class="viewThumb">
															<img src="${im.fileUrl}" />
														</div>
														<div class="diyFileName">${im.fileName}</div>
													</li>
												</c:forEach>
											</ul>
										</div>
									</div>
				    			</div>
				 			 </div>	
			 			 </c:if>
						 <c:if test="${empty special.auditStatus }">		 			
				 			 <div class="form-action">
								<button type="button" class="submitform btn btn-primary" name="submitpassform">审核通过</button>
								<button type="button" class="submitform btn btn-danger" name="submitdefinedform">审核不通过</button>
				 			 </div>
			 			 </c:if>			 			 
					</div>					
				</div>
			</div>
		</div>
	</form>
	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '',iscoo = ${special.isCooHospital == '1' };
		seajs.use('view/admin/main',function(controller){
			controller.hosptialpassed();
		});
	</script>
</body>
</html>

