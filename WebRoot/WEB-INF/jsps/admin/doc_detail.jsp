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
	<title>医生管理</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
	<!-- <style>
		.inputval{display:inline-block;margin-right:10px;}
	</style> -->
</head>
<body class="exportdetail">
	<form class="row-fluid form-horizontal"  method="post" id="myform">
		<input type="hidden" name="expertid" value="${special.specialId }"/>
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<h3>医生审核</h3>								
			</div>
			<div class="bodyer">
				<div class="section">
					<div class="bodyer clearfix">
						 <div class="control-group">
						    <label class="control-label">医生姓名：</label>
						    <div class="controls">
						    	<label class="inputval">${special.specialName }</label>
			    			</div>
			 			 </div>
						  <div class="control-group">
						    <label class="control-label">身份证号：</label>
						    <div class="controls">
						    	<label class="inputval">${special.idCardNo }</label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">电话：</label>
						    <div class="controls">
						    	<label class="inputval">${special.mobileTelphone }</label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">职务：</label>
						    <div class="controls">
						    	<label class="inputval">${special.duty }</label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">职称：</label>
						    <div class="controls">
						    	<label class="inputval">${special.profession }</label>
						    	<label class="inputval">${special.specialTitle }</label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">所在省市：</label>
						    <div class="controls">
						    	<input type="hidden" name="distcode" value="${special.distCode }"/>
						    	<select name="pro" class="bindchange" data-type="city" data-val="${special.distCode }">
						    	</select>
						    	<select name="city" class="bindchange" data-type="hosid" data-val="${special.distCode }">
						    	</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">所在医院：</label>
						    <div class="controls">
						    	<label class="inputval">${special.hosName }</label>
								<select name="hosid" class="bindchange" data-type="depid">
									<option value="">---请关联医院---</option>
								</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">所在科室：</label>
						    <div class="controls">
						    	<label class="inputval">${special.depName }</label>
								<select name="depid">
									<option value="">---请关联科室---</option>
								</select>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">地址：</label>
						    <div class="controls doublewidth">
						    	<label class="inputval">${special.position }</label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">擅长领域：</label>
						    <div class="controls doublewidth">
						    	<label class="inputval">${special.specialty }</label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">名医介绍：</label>
						    <div class="controls doublewidth">
						    	<label class="inputval">${special.famousDoctor }</label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">简介：</label>
						    <div class="controls doublewidth">
						    	<label class="inputval">${special.profile }</label>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">医生执照/工牌：</label>
						    <div class="controls">
						    	<div id="picsheadImageUrl" class="diyUpload">
									<div class="parentFileBox">
										<ul class="fileBoxUl clearfix">
											<c:if test="${fn:length(special.badgeUrl) > 1}">
												<li data-id="${special.badgeUrl}" class="browser">
													<div class="viewThumb">
														<img src="${special.badgeUrl}" />
													</div>
												</li>
											</c:if>
										</ul>
									</div>	
								</div>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">医生头像：</label>
						    <div class="controls">
						    	<div id="picsheadImageUrl" class="diyUpload">
									<div class="parentFileBox">
										<ul class="fileBoxUl clearfix">
											<c:if test="${fn:length(special.listSpecialPicture) > 1}">
												<li data-id="${special.listSpecialPicture}" class="browser">
													<div class="viewThumb">
														<img src="${special.listSpecialPicture}" />
													</div>
												</li>
											</c:if>
										</ul>
									</div>	
								</div>
			    			</div>
			 			 </div>
			 			 <div class="form-action">
							<button type="button" class="submitform btn btn-primary" name="submitpassform">审核通过</button>
							<button type="button" class="submitform btn btn-danger" data-toggle="modal" data-target="#myModal">审核不通过</button>
			 			 </div>
					</div>					
				</div>
			</div>
		</div>
	</form>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body" style="text-align: center">
					<textarea class="reason" style="width: 95%; height: 150px; resize: none"></textarea>
				</div>
				<div class="modal-footer" style="text-align: center">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary audit" name="submitdefinedform">提交</button>
				</div>
			</div>
		</div>
	</div>
	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '';
		seajs.use('view/admin/main',function(controller){
			controller.exportinfo(true);
			seajs.use('view/viewer',function(view){
	    		view.init('.diyUpload .fileBoxUl',{ 
	    			navbar:true
	    		});
	    	});
			$('body').delegate('[name="submitpassform"]','click',function(){
				auditdoc(1);
			}).delegate('[name="submitdefinedform"]','click',function(){
				auditdoc(-3);
			});
			function auditdoc(v){
				var hosid = '${special.hosId}',depid = '${special.depId}';
				var dist = $('[name="distcode"]').val();
				var _hosid = $('[name="hosid"]').val();
				var _depid = $('[name="depid"]').val();
				var reason = $('.reason').val()
				if(v > 0 && !hosid && !_hosid) return alert('请关联医院'),0;
				if(v > 0 && !depid && !_depid) return alert('请关联科室'),0;
				var obj = v == -3 ? { docid:'${docid}',auditvalue:v, hosid:_hosid || hosid, depid:_depid || depid, refusalReason: reason } :
									{ docid:'${docid}',auditvalue:v, hosid:_hosid || hosid, depid:_depid || depid}
				$.post('/system/auditdoc',obj)
				.done(function(){
					alert('操作成功');
					window.location.href = "/system/docaudit";
				})
			}
		});
	</script>
</body>
</html>

