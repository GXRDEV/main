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
	<title>会诊案例操作</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
</head>
<body class="exportdetail">
	<form class="row-fluid form-horizontal"  method="post" id="myform">
		<input type="hidden" name="sscId" value="${scase.id}" id="cid"/>
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<h3>	
				会诊案例操作
				</h3>								
			</div>
			<div class="bodyer">
				<div class="section">
					<div class="bodyer clearfix">
						 <div class="control-group">
						    <label class="control-label">标题：</label>
						    <div class="controls">
						    	<input type="text" name="title" value="${scase.title}"/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">概要：</label>
						    <div class="controls">
						    	<input type="text" name="summary" value="${scase.summary}"/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
								<label class="control-label">会诊类型：</label>
								<div class="controls">
									<select name="tagType">
										<c:forEach items="${healthTypes}" var = "htype">
											<option value="${htype.id}" <c:if test="${htype.id == scase.tagType }">selected</c:if> >${htype.typeName}</option>
										</c:forEach>
									</select>
								</div>
							</div>
			 			 <div class="control-group">
						    <label class="control-label">正文链接地址：</label>
						    <div class="controls">
						    	<input type="text" name="textLink" value="${scase.textLink}"/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">背景图片：</label>
						    <div class="controls">
						    	<div id="picsheadImageUrl" class="diyUpload">
									<div class="parentFileBox">
										<ul class="fileBoxUl clearfix">
											<c:if test="${fn:length(scase.backImage) > 1}">
												<li data-id="${scase.backImage}" class="browser">
													<div class="viewThumb">
														<img src="${scase.backImage}" />
													</div>
													<div class="diyCancel"><i class="iconfont" title="删除"></i></div>
												</li>
											</c:if>
											<li class="actionAdd">
												<div id="addheadImageUrl"><i class="iconfont">&#xe60e;</i></div>
											</li>
										</ul>
									</div>	
									<input type="hidden" id="backImage" name="backImage" value="${scase.backImage}"/>			
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
			controller.sysconcaseinfo();
		});
	</script>
</body>
</html>

