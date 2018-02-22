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
	<title>医院管理</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
	<script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=7vuYwc8NzPaF1DZi1ZZxXWfWdzhjpXOl&s=l"></script>
</head>
<body class="exportdetail">
	<form class="row-fluid form-horizontal"  method="post" id="myform">
		<input type="hidden" name="hosid" value="${special.id }"/>
		<input type="hidden" name="auditStatus" value="<c:if test="${empty special.id }">1</c:if><c:if test="${!empty special.id }">${special.auditStatus }</c:if>"/>
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<h3>	
					<c:if test="${empty special.id }">添加医院</c:if>
					<c:if test="${!empty special.id }">编辑医院</c:if>
				</h3>
			</div>
			<div class="bodyer">
				<div class="section">
					<div class="bodyer clearfix">
						 <div class="control-group required">
						    <label class="control-label"><b>*</b>医院名称：</label>
						    <div class="controls">
						    	<input type="text" name="displayName" value="${special.displayName }"/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">医院简称：</label>
						    <div class="controls">
						    	<input type="text" name="shortName" value="${special.shortName }"/>
			    			</div>
			 			 </div>			 			 
			 			 <div class="control-group">
						    <label class="control-label">合作医院：</label>
						    <div class="controls">
						    	<label class="checkbox"><input type="checkbox" name="coohos" <c:if test="${special.isCooHospital == '1' }">checked</c:if> value="1"/></label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">对接模式：</label>
						    <div class="controls">
						    	<select name="dockingMode" data-val="${special.dockingMode }">
						    		<option value="">---请选择对接模式---</option>
						    		<option value="1" <c:if test="${special.dockingMode == '1'}">selected</c:if>>简单对接</option>
						    		<option value="2" <c:if test="${special.dockingMode == '2'}">selected</c:if>>深度对接</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group forcoohos <c:if test="${special.isCooHospital == '1' }">required</c:if>">
						    <label class="control-label"><b>*</b>联系人姓名：</label>
						    <div class="controls">
						    	<input type="text" name="contactorName" class="input" value="${special.contactorName }" />
			    			</div>
			 			 </div>
			 			 <div class="control-group forcoohos <c:if test="${special.isCooHospital == '1' }">required</c:if>">
						    <label class="control-label"><b>*</b>联系人电话：</label>
						    <div class="controls">
						    	<input type="text" name="contactorTelphone" class="input" value="${special.contactorTelphone }" />
			    			</div>
			 			 </div>
			 			 <c:if test="${special.isCooHospital == '1' }">				 			 
				 			 <div class="control-group">
							    <label class="control-label">联系人职务：</label>
							    <div class="controls">
							    	<input type="text" name="contactorDuty" class="input" value="${special.contactorDuty }" />
				    			</div>
				 			 </div>
				 			 <div class="control-group">
							    <label class="control-label">联系邮箱：</label>
							    <div class="controls">
							    	<input type="text" name="contactorEmail" class="input" value="${special.contactorEmail }" />
				    			</div>
				 			 </div>
				 		 </c:if>
			 			 <div class="control-group">
						    <label class="control-label">医院等级：</label>
						    <div class="controls">
						    	<select name="hospitalLevel" data-pid="3" class="input js-ajax" data-val="${special.hospitalLevel }"></select>
			    			</div>
			 			 </div>
						 <div class="control-group">
						    <label class="control-label">医院类型：</label>
						    <div class="controls">
						    	<select name="hosType" data-pid="20" class="input js-ajax" data-val="${special.hosType }"></select>
			    			</div>
			 			 </div>
			 			 <c:if test="${special.isCooHospital == '1' }">
							 <div class="control-group">
							    <label class="control-label">医院性质：</label>
							    <div class="controls">
							    	<select name="hosProperty" data-pid="19" class="input js-ajax" data-val="${special.hosProperty }"></select>
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <c:if test="${special.isCooHospital == '1' }">
							 <div class="control-group">
							    <label class="control-label">医院官网：</label>
							    <div class="controls">
							    	<input type="text" name="hosWebSite" class="input"  value="${special.hosWebSite }"/>
				    			</div>
				 			 </div>
							 <div class="control-group w60p">
							    <label class="control-label">日门诊量：</label>
							    <div class="controls">
							    	<input type="number" name="dailyOutpatientNumber" class="input" value="${special.dailyOutpatientNumber }" />
				    			</div>
				 			 </div>
							 <div class="control-group w40p">
							    <label class="control-label">日预约量：</label>
							    <div class="controls">
							    	<input type="number" name="dailyAppointNumber" class="input" value="${special.dailyAppointNumber }" />
				    			</div>
				 			 </div>
							 <div class="control-group">
							    <label class="control-label">上级转诊医院：</label>
							    <div class="controls">
							    	<input type="text" name="externalAppointmentApproach" class="input" value="${special.externalAppointmentApproach }" />
				    			</div>
				 			 </div>
			 			 </c:if>
			 			 <div class="control-group">
						    <label class="control-label">所在省市：</label>
						    <div class="controls">
						    	<input type="hidden" name="distCode" value="${special.distCode }"/>
						    	<select name="pro" class="bindchange" data-type="city" data-val="${special.distCode }">
						    		<option value="">---请选择---</option>
						    	</select>
						    	<select name="city" class="bindchange" data-type="dist" data-val="${special.distCode }">
						    		<option value="">---请选择---</option>
						    	</select>
						    	<select name="dist" class="bindchange" data-type="hosid" data-val="${special.distCode }">
						    		<option value="">---请选择---</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">医院简介：</label>
						    <div class="controls doublewidth">
						    	<textarea name="hosProfile" class="input" style="height:12em;">${special.hosProfile }</textarea>
			    			</div>
			 			 </div>
			 			 <c:if test="${special.isCooHospital == '1' }">
				 			 <div class="control-group">
							    <label class="control-label">特色门诊：</label>
							    <div class="controls doublewidth">
							    	<input type="text" name="characteristicClinic" class="input" value="${special.characteristicClinic }" />
				    			</div>
				 			 </div>	
			 			 </c:if>
			 			 <div class="control-group">
						    <label class="control-label">地址：</label>
						    <div class="controls doublewidth">
						    	<input type="text" name="location" placeholder="输入准确的地址可以解析到经纬度，也可以通过点击地图获取经纬度" value="${special.location }"/>
			    			</div>
			 			 </div>			 			 
			 			 <div class="control-group">
						    <label class="control-label">经纬度：</label>
						    <div class="controls">
						    	<input type="text" name="lat" id="lat" placeholder="经度" value="${special.lat }"/>
						    	<input type="text" name="lng" id="lng" placeholder="纬度" value="${special.lng }"/>
						    	<div id="picklatlng" style="width:60%;height:400px;margin-top:10px;border-radius:4px;border:1px solid #ccc;"></div>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">备注：</label>
						    <div class="controls doublewidth">
						    	<textarea name="remark">${special.remark }</textarea>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">关键词：</label>
						    <div class="controls doublewidth">
						    	<input type="text" name="keywords" class="input" value="${special.keywords }" />
			    			</div>
			 			 </div>
			 			 <c:if test="${special.isCooHospital == '1' }">
				 			 <div class="control-group">
							    <label class="control-label">授权文件：</label>
							    <div class="controls">
							    	<div id="picsauthorizeFile" class="diyUpload">
										<div class="parentFileBox">
											<ul class="fileBoxUl clearfix">
												<c:forEach items="${authorizeFiles}" var="im">
													<li data-id="${im.id}" class="browser">
														<div class="viewThumb">
															<img src="${im.fileUrl}" />
														</div>
														<div class="diyFileName">${im.fileName}</div>
														<div class="diyCancel"><i class="iconfont" title="删除"></i></div>
													</li>
												</c:forEach>
												<li class="actionAdd">
													<div id="addauthorizeFile"><i class="iconfont">&#xe60e;</i></div>
												</li>
											</ul>
										</div>
										<input type="hidden" id="authorizeFile" name="authorizeFile" value="${special.authorizeFile}"/>			
									</div>
				    			</div>
				 			 </div>	
			 			 </c:if>
			 			 <div class="control-group">
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
													<div class="diyCancel"><i class="iconfont" title="删除"></i></div>
												</li>
											</c:forEach>
											<li class="actionAdd">
												<div id="addbigPicture"><i class="iconfont">&#xe60e;</i></div>
											</li>
										</ul>
									</div>
									<input type="hidden" id="bigPicture" name="bigPicture" value="${special.bigPicture}"/>			
								</div>
			    			</div>
			 			 </div>			 			
			 			 <div class="form-action">
			 			 	<button type="button" class="btn btn-primary" name="submitform">保存</button>
			 			 </div>
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
			controller.hosptialinfo();
		});
	</script>
</body>
</html>

