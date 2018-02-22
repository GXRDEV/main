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
	<title>运营人员操作</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
</head>
<body class="exportdetail">
	<form class="row-fluid form-horizontal"  method="post" id="myform">
		<input type="hidden" name="specialId" value="${operator.specialId}" id="specialId"/>
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<h3>	
				运营人员操作
				</h3>								
			</div>
			<div class="bodyer">
				<div class="section">
					<div class="bodyer clearfix">
						 <div class="control-group">
						    <label class="control-label">姓名：</label>
						    <div class="controls">
						    	<input type="text" name="specialName" value="${operator.specialName}"/>
			    			</div>
			 			 </div>
			 			<div class="control-group">
						    <label class="control-label">性别：</label>
						    <div class="controls">
						    	<select name="sex">
						    		<option value="1">男</option>
						    		<option value="0">女</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">联系电话：</label>
						    <div class="controls">
						    	<input type="text" name="mobileTelphone" value="${operator.mobileTelphone}"/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">所在省市：</label>
						    <div class="controls">
						    	<input type="hidden" name="distCode" value="${operator.distCode }"/>
						    	<select name="pro" class="bindchange" data-type="city" data-val="${operator.distCode }">
						    		<option value="">---请选择---</option>
						    	</select>
						    	<select name="city" class="bindchange" data-type="street" data-val="${operator.distCode }">
						    		<option value="">---请选择---</option>
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">专家头像：</label>
						    <div class="controls">
						    	<div id="picsheadImageUrl" class="diyUpload">
									<div class="parentFileBox">
										<ul class="fileBoxUl clearfix">
											<c:if test="${fn:length(operator.listSpecialPicture) > 1}">
												<li data-id="${operator.listSpecialPicture}" class="browser">
													<div class="viewThumb">
														<img src="${operator.listSpecialPicture}" />
													</div>
													<div class="diyCancel"><i class="iconfont" title="删除"></i></div>
												</li>
											</c:if>
											<li class="actionAdd">
												<div id="addheadImageUrl"><i class="iconfont">&#xe60e;</i></div>
											</li>
										</ul>
									</div>	
									<input type="hidden" id="headImageUrl" name="listSpecialPicture" value="${operator.listSpecialPicture}"/>			
								</div>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">类型：</label>
						    <div class="controls">
						    	<input type="radio" name="usertype" value="7" <c:if test='${operator.userType=="7" }'>checked</c:if> />医院运营人员&nbsp;
						    	<input type="radio" name="usertype" value="8" <c:if test='${operator.userType=="8" }'>checked</c:if> />专家运营人员
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
			controller.operatorinfo();
		});
	</script>
</body>
</html>

