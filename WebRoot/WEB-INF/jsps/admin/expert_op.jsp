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
	<title>专家管理</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
</head>
<body class="exportdetail">
	<form class="row-fluid form-horizontal"  method="post" id="myform">
		<input type="hidden" name="expertid" value="${special.specialId }"/>
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<h3>	
				<c:if test="${empty special.specialId }">添加专家</c:if>
				<c:if test="${!empty special.specialId }">编辑专家</c:if>
				</h3>								
			</div>
			<div class="bodyer">
				<div class="section">
					<div class="bodyer clearfix">
						 <div class="control-group">
						    <label class="control-label">专家姓名：</label>
						    <div class="controls">
						    	<input type="text" name="displayName" value="${special.specialName }"/>
			    			</div>
			 			 </div>
						  <div class="control-group">
						    <label class="control-label">性别：</label>
						    <div class="controls">
						    	<select name="sex">
						    		<option value="1" <c:if test="${special.sex==1}">selected</c:if>>男</option>
						    		<option value="2" <c:if test="${special.sex==2}">selected</c:if>>女</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">电话：</label>
						    <div class="controls">
						    	<input type="text" name="telphone" value="${special.telphone }"/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">身份证号：</label>
						    <div class="controls">
						    	<input type="text" name="idCardNo" value="${special.idCardNo }"/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">职务：</label>
						    <div class="controls">
						    	<select name="duty">
						    		<option value="">---暂无---</option>
						    		<option value="主任医师" <c:if test="${special.duty=='主任医师'}">selected</c:if>>主任医师</option>
						    		<option value="副主任医师" <c:if test="${special.duty=='副主任医师'}">selected</c:if>>副主任医师</option>
						    		<option value="主治医师" <c:if test="${special.duty=='主治医师'}">selected</c:if>>主治医师</option>
						    		<option value="住院医师" <c:if test="${special.duty=='住院医师'}">selected</c:if>>住院医师</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">职称：</label>
						    <div class="controls">
						    	<select name="profession">
						    		<option value="">---暂无---</option>
						    		<option value="教授" <c:if test="${special.profession=='教授'}">selected</c:if>>教授</option>
						    		<option value="副教授" <c:if test="${special.profession=='副教授'}">selected</c:if>>副教授</option>
						    		<option value="讲师" <c:if test="${special.profession=='讲师'}">selected</c:if>>讲师</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">所在省市：</label>
						    <div class="controls">
						    	<input type="hidden" name="distcode" value="${special.distCode }"/>
						    	<select name="pro" class="bindchange" data-type="city" data-val="${special.distCode }">
						    		<option value="">---请选择---</option>
						    	</select>
						    	<select name="city" class="bindchange" data-type="hosid" data-val="${special.distCode }">
						    		<option value="">---请选择---</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">所在医院/科室：</label>
						    <div class="controls">
						    	<select name="hosid" class="bindchange" data-type="depid" data-val="${special.hosId }">
						    		<option value="">---请选择---</option>
						    	</select>
						    	<select name="depid" data-val="${special.depId }">
						    		<option value="">---请选择---</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">地址：</label>
						    <div class="controls doublewidth">
						    	<input type="text" name="position" value="${special.position }"/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">擅长领域：</label>
						    <div class="controls doublewidth">
						    	<textarea name="speciality">${special.specialty }</textarea>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">简介：</label>
						    <div class="controls doublewidth">
						    	<textarea name="profile">${special.profile }</textarea>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">开通服务：</label>
						    <div class="controls">
						    	<div class="serverslist">
						    		<label class="checkbox"><input type="checkbox" name="openVedio" <c:if test="${special.openVedio == '1' || empty(special.specialId) }">checked</c:if> value="1"/><span>视频会诊</span></label>
						    		<div class="input-prepend input-append required" style="<c:if test="${special.openVedio != '1' && !empty(special.specialId) }">display:none</c:if>">
									  <span class="add-on">￥</span>
									  <input class="span4" type="number" name="vedioAmount" value="${special.vedioAmount }"/>
									  <span class="add-on">元<b>*</b></span>
									</div>&emsp;
						    	</div>
								<!--<div class="serverslist">
						    		<label class="checkbox"><input type="checkbox" name="openAsk" <c:if test="${special.openAsk == '1' || empty(special.specialId) }">checked</c:if> value="1"/><span>图文咨询</span></label>
			    					<div class="input-prepend input-append required" style="<c:if test="${special.openAsk != '1' && !empty(special.specialId) }">display:none</c:if>">
									  <span class="add-on">￥</span>
									  <input class="span4" type="number" name="askAmount" value="${special.askAmount }"/>
									  <span class="add-on">元<b>*</b></span>
									</div>
			    				</div>
								<div class="serverslist">
			    					<label class="checkbox"><input type="checkbox" name="openTel" <c:if test="${special.openTel == '1' || empty(special.specialId) }">checked</c:if> value="1"/><span>电话问诊</span></label>
			    					<div class="input-prepend input-append required" style="<c:if test="${special.openTel != '1' && !empty(special.specialId) }">display:none</c:if>">
									  <span class="add-on">￥</span>
									  <input class="span4" type="number" name="telAmount" value="${special.telAmount }"/>
									  <span class="add-on">元<b>*</b></span>
									</div>
			    				</div>-->
								<div class="serverslist">
			    					<label class="checkbox"><input type="checkbox" name="openAdvice" <c:if test="${special.openAdvice == '1' || empty(special.specialId) }">checked</c:if> value="1"/><span>图文会诊</span></label>
			    					<div class="input-prepend input-append required" style="<c:if test="${special.openAdvice != '1' && !empty(special.specialId) }">display:none</c:if>">
									  <span class="add-on">￥</span>
									  <input class="span4" type="number" name="adviceAmount" value="${special.adviceAmount }"/>
									  <span class="add-on">元<b>*</b></span>
									</div>
			    				</div>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">是否推荐：</label>
						    <div class="controls">
						    	<label class="checkbox"><input type="checkbox" name="recommond" <c:if test="${special.recommond == '1' || empty(special.specialId)  }">checked</c:if> value="1"/></label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">专家头像：</label>
						    <div class="controls">
						    	<div id="picsheadImageUrl" class="diyUpload">
									<div class="parentFileBox">
										<ul class="fileBoxUl clearfix">
											<c:if test="${fn:length(special.listSpecialPicture) > 1}">
												<li data-id="${special.listSpecialPicture}" class="browser">
													<div class="viewThumb">
														<img src="${special.listSpecialPicture}" />
													</div>
													<div class="diyCancel"><i class="iconfont" title="删除"></i></div>
												</li>
											</c:if>
											<li class="actionAdd">
												<div id="addheadImageUrl"><i class="iconfont">&#xe60e;</i></div>
											</li>
										</ul>
									</div>	
									<input type="hidden" id="headImageUrl" name="headImageUrl" value="${special.listSpecialPicture}"/>			
								</div>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">相关照片：</label>
						    <div class="controls">
						    	<div id="picsrelatedPics" class="diyUpload">
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
												<div id="addrelatedPics"><i class="iconfont">&#xe60e;</i></div>
											</li>
										</ul>
									</div>
									<input type="hidden" id="relatedPics" name="relatedPics" value="${special.relatedPics}"/>			
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
		var _burl='/',_h = _burl,_id = '';
		seajs.use('view/admin/main',function(controller){
			controller.exportinfo();
		});
	</script>
</body>
</html>

