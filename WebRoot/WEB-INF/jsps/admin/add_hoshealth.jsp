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
	<title>创建医联体</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
	<link rel="stylesheet" href="/sea-modules/select2/css/select2.min.css" type="text/css" />
	<style>
		.grade{ margin-left:20px; background:#eee;padding: 4px 10px 6px;border-radius:3px}
	</style>
</head>
<body class="exportdetail">
	<form class="row-fluid form-horizontal"  method="post" id="myform">
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<h3>	
				创建医联体
				</h3>								
			</div>
			<div class="bodyer">
				<div class="section">
					<div class="bodyer clearfix">
						 <div class="control-group">
						    <label class="control-label">医联体名称：</label>
						    <div class="controls">
						    	<input type="text" name="yltName" value="${ylt.yltName}"/>
			    			</div>
			 			 </div>
			 			  <div class="control-group">
						    <label class="control-label">擅长：</label>
						    <div class="controls doublewidth">
						    	<textarea name="speciality">${ylt.speciality}</textarea>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">简介：</label>
						    <div class="controls doublewidth">
						    	<textarea name="profile">${ylt.profile}</textarea>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">logo：</label>
						    <div class="controls">
						    	<div id="picsheadImageUrl" class="diyUpload">
									<div class="parentFileBox">
										<ul class="fileBoxUl clearfix">
											<li id="fileBox_WU_FILE_0" class="browser" style="display:none">
												<div class="viewThumb">
													<img >
												</div>
												<div class="diyCancel">
													<i class="iconfont" title="删除"></i>
												</div>
												<div class="diySuccess" style="display: block"></div>
											</li>
											<li class="actionAdd">
												<div id="addheadImageUrl"><i class="iconfont">&#xe60e;</i></div>
											</li>
										</ul>
									</div>	
									<input type="hidden" id="headImageUrl" name="iconUrl"/>			
								</div>
			    			</div>
			 			 </div>
						 <div class="control-group">
						    <label class="control-label">医院：</label>
						    <div class="controls">
						    	<select id="hospitals" name="hosId">
						    		<option>--选择医院--</option>
						    	</select>
								<span class="grade">三级甲等</span>
			    			</div>
			 			 </div>
			 			 <div class="form-action">
			 			 	<button type="button" class="btn btn-primary" name="sbform">保存</button>
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
			controller.exportinfo();
		});
		
		$(document).ready(function(){
			var text, yltid;
			yltid='${ylt.id}';
			$('.browser').hide();
			if(yltid){
				$("#myform").append('<input type="hidden" name="yltId" value="${ylt.id}"/>')
				$('.grade').text("${ylt.hosLevel}")
				if(!!'${ylt.iconUrl}'){
					$('.browser').show().attr('data-id', '${ylt.iconUrl}').find('img').attr('src', '${ylt.iconUrl}')
				}
			}
			$.ajax({ 
				type:"post",  
				url:"/system/gainhospitalsbycon",
				data:{ status: '10' },
				success:function(data){
					var hosp = data.hospitals;
					var allhosp = "";
					if(yltid){
						var hosid = '${ylt.hospitalId}'
						allhosp ="<option value="+ hosid +" title="+ hosid +">${ylt.hosName}</option>"
					}
					if(hosp!=[]){
						for(var i in hosp){
							allhosp += "<option value="+hosp[i].id+" title="+hosp[i].id+">"+hosp[i].displayName+"</option>"
						}
					}
					$("#hospitals").html(allhosp)
					hospId = $("#hospitals option:selected").attr("title")
					$("#hospitals").select2()
				}
			});
		})
	</script>
</body>
</html>

