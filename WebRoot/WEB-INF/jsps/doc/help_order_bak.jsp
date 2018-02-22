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
	<title>辅助下单</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
</head>
<body class="exportdetail dochelporder">
	<form class="row-fluid form-horizontal"  method="post" id="myform">
		<input type="hidden" name="expertid" value="${special.specialId }"/>
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<h3>辅助下单</h3>								
			</div>
			<div class="bodyer">
				<c:if test="${fn:length(specials)>0}">			
				<div class="section1">
					<div class="bodyer clearfix">						
			 			 <div class="exportslist">
						 	<c:forEach items="${specials}" var="o">
								<div class="doslist" data-id="${o.specialId }">
									<div class="thumb">
										<c:if test="${ fn:length(o.listSpecialPicture) > 0 }">
											<c:if test="${ fn:indexOf(o.listSpecialPicture,'://') != -1}">
												<img src="${fn:replace(o.listSpecialPicture,'http://','https://')}"/>
											</c:if>
											<c:if test="${ fn:indexOf(o.listSpecialPicture,'://') == -1}">
												<img src="http://wx.15120.cn/SysApi2/Files/${o.listSpecialPicture}"/>
											</c:if>
										</c:if>
										<c:if test="${ fn:length(o.listSpecialPicture) <= 0 }">
											<img src="/img/defdoc.jpg"/>
										</c:if>
									</div>
									<div class="dosinfo">
										<p class="fp">${o.specialName}</p>
										<p>${o.duty}/${o.profession}</p>
										<p>${o.hosName}</p>
										<p>${o.depName}</p>
										<p class="lp">擅长：<span>${o.specialty}</span></p>
									</div>
								</div>
							</c:forEach>
			 			 </div>
			 			 <div class="form-action">
			 			 	<button type="button" class="btn btn-primary" name="nextstep" data-step="2">下一步</button>
			 			 	<button type="button" class="btn" name="nextstep" data-step="0">跳过</button>
			 			 </div>
					</div>
				</div>			
				<div class="section2">
					<div class="bodyer clearfix">						
			 			 <div class="doctimes">
						 	<div class="swiper-container">
						    	<div class="swiper-wrapper clearfix"></div>
					    	</div>
			 			 </div>
			 			 <div class="form-action">
			 			 	<button type="button" class="btn" name="nextstep" data-step="10">上一步</button>
			 			 	<button type="button" class="btn btn-primary" name="nextstep" data-step="3">下一步</button>
			 			 </div>
					</div>
				</div>
				</c:if>
				<div class="<c:if test="${fn:length(specials)>0}"> section3 </c:if>">
					<div class="bodyer clearfix" style="padding-top:20px;">
						 <div class="control-group">
						    <label class="control-label">姓名：</label>
						    <div class="controls">
						    	<input type="text" name="username" />
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">电话：</label>
						    <div class="controls">
						    	<input type="text" name="telphone" />
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">身份证：</label>
						    <div class="controls">
						    	<input type="text" name="idcard" />
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">性别：</label>
						    <div class="controls">
						    	<label class="radio"><input type="radio" name="sex" value="1" checked/>男</label>
						    	<label class="radio"><input type="radio" name="sex" value="2"/>女</label>
			    			</div>
			 			 </div>
			 			 <div class="form-action">
			 			 	<input type="hidden" name="expertId"/>
			 			 	<input type="hidden" name="stimeid"/>
			 			 	<c:if test="${fn:length(specials)>0}">
			 			 		<button type="button" class="btn" name="nextstep" data-step="1">上一步</button>
			 			 	</c:if>
			 			 	<button type="button" class="btn btn-primary" name="nextstep" data-step="4">保存</button>
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
			controller.docHelpOrder();
		});
	</script>
</body>
</html>

