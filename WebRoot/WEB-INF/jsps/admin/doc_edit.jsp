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
	<link rel="stylesheet" href="/sea-modules/select2/css/select2.min.css" type="text/css" />
</head>
<body class="exportdetail">
	<form class="row-fluid form-horizontal"  method="post" id="myform">
		<input type="hidden" name="docid" value="${doc.specialId }"/>
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<h3>	
				编辑医生
				</h3>								
			</div>
			<div class="bodyer">
				<div class="section">
					<div class="bodyer clearfix">
						 <div class="control-group">
						    <label class="control-label">医生姓名：</label>
						    <div class="controls">
						    	<input type="text" name="displayName" value="${doc.specialName }"/>
			    			</div>
			 			 </div>
						 <div class="control-group">
						    <label class="control-label">性别：</label>
						    <div class="controls">
						    	<select name="sex">
						    		<option value="1" <c:if test="${doc.sex==1}">selected</c:if>>男</option>
						    		<option value="2" <c:if test="${doc.sex==2}">selected</c:if>>女</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">电话：</label>
						    <div class="controls">
						    	<input type="text" name="telphone" value="${doc.telphone }"/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">身份证号：</label>
						    <div class="controls">
						    	<input type="text" name="idCardNo" value="${doc.idCardNo }"/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">职务：</label>
						    <div class="controls">
						    	<select name="duty">
									<option value="">---暂无---</option>
						    		<option value="主任医师" <c:if test="${doc.duty=='主任医师'}">selected</c:if>>主任医师</option>
						    		<option value="副主任医师" <c:if test="${doc.duty=='副主任医师'}">selected</c:if>>副主任医师</option>
						    		<option value="主治医师" <c:if test="${doc.duty=='主治医师'}">selected</c:if>>主治医师</option>
						    		<option value="住院医师" <c:if test="${doc.duty=='住院医师'}">selected</c:if>>住院医师</option>
						    		<option value="医师" <c:if test="${doc.duty=='医师'}">selected</c:if>>医师</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">职称：</label>
						    <div class="controls">
						    	<select name="profession">
						    		<option value="">---暂无---</option>
						    		<option value="教授" <c:if test="${doc.profession=='教授'}">selected</c:if>>教授</option>
						    		<option value="副教授" <c:if test="${doc.profession=='副教授'}">selected</c:if>>副教授</option>
						    		<option value="讲师" <c:if test="${doc.profession=='讲师'}">selected</c:if>>讲师</option>
						    		<option value="医生" <c:if test="${doc.profession=='医生'}">selected</c:if>>医生</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">所在省市：</label>
						    <div class="controls">
						    	<input type="hidden" name="distcode" value="${doc.distCode }"/>
						    	<select name="pro" class="bindchange" data-type="city" data-val="${doc.distCode }">
						    		<option value="">---请选择---</option>
						    	</select>
						    	<select name="city" class="bindchange" data-type="hosid" data-val="${doc.distCode }">
						    		<option value="">---请选择---</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">所在医院/科室：</label> 
						    <div class="controls">
						    	<select name="hosid" class="bindchange" data-type="depid" data-val="${doc.hosId }">
						    		<option value="">---请选择---</option>
						    	</select>
						    	<select name="depid" data-val="${doc.depId }">
						    		<option value="">---请选择---</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">地址：</label>
						    <div class="controls doublewidth">
						    	<input type="text" name="position" value="${doc.position }"/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">擅长领域：</label>
						    <div class="controls doublewidth">
						    	<textarea name="speciality">${doc.specialty }</textarea>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">简介：</label>
						    <div class="controls doublewidth">
						    	<textarea name="profile">${doc.profile }</textarea>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">开通服务：</label>
						    <div class="controls">
								<div class="serverslist">
			    					<label class="checkbox"><input type="checkbox" name="openAsk" <c:if test="${doc.openAsk == '1' || empty(doc.specialId) }">checked</c:if> value="1"/><span>图文问诊</span></label>
			    					<div class="input-prepend input-append required" style="<c:if test="${doc.openAsk != '1' && !empty(doc.specialId) }">display:none</c:if>">
										<span class="add-on">￥</span>
										<input class="span4" type="number" name="askAmount" value="${doc.askAmount }"/>
										<span class="add-on">元<b>*</b></span>
									</div>
			    				</div>
								<div class="serverslist">
									<label class="checkbox"><input type="checkbox" name="openTel" <c:if test="${doc.openTel == '1' || empty(doc.specialId) }">checked</c:if> value="1"/><span>电话问诊</span></label>
										<div class="input-prepend input-append required" style="<c:if test="${doc.openTel != '1' && !empty(doc.specialId) }">display:none</c:if>">
											<span class="add-on">￥</span>
											<input class="span4" type="number" name="telAmount" value="${doc.telAmount }"/>
											<span class="add-on">元<b>*</b></span>
										</div>
								</div>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">是否推荐：</label>
						    <div class="controls">
						    	<label class="checkbox"><input type="checkbox" name="recommond" <c:if test="${doc.recommond == '1' || empty(doc.specialId)  }">checked</c:if> value="1"/></label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">医生头像：</label>
						    <div class="controls">
						    	<div id="picsheadImageUrl" class="diyUpload">
									<div class="parentFileBox">
										<ul class="fileBoxUl clearfix">
											<c:if test="${fn:length(doc.listSpecialPicture) > 1}">
												<li data-id="${doc.listSpecialPicture}" class="browser">
													<div class="viewThumb">
														<img src="${doc.listSpecialPicture}" />
													</div>
													<div class="diyCancel"><i class="iconfont" title="删除"></i></div>
												</li>
											</c:if>
											<li class="actionAdd">
												<div id="addheadImageUrl"><i class="iconfont">&#xe60e;</i></div>
											</li>
										</ul>
									</div>	
									<input type="hidden" id="headImageUrl" name="headImageUrl" value="${doc.listSpecialPicture}"/>			
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
	<script type="text/javascript" src="/sea-modules/select2/js/select2.min.js"></script>
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '';
		seajs.use('view/admin/main',function(controller){
			$('[name="hosid"]').select2()
			controller.operatorDocInfo(true);
		});
	</script>
</body>
</html>

