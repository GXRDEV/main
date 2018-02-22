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
	<title>服务信息</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css">
	<link rel="stylesheet" href="/css/view/admin.css" />
	<style>
		.pleft {
			padding-left:60px;
		}
		.mleft {
			margin-left:180px;
		}
		.info-label {
			display: inline-block; min-width: 6em;
		}
	    .info-input {
			width:75% !important;
		}
		.delbtn {
			margin-left:375px;
		}
		.redcolor {
			color: red;
		}
	</style>
</head>
<body class="exportdetail">
	<div class="row-fluid form-horizontal" id="serviceInfo">
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<h3>	
				添加服务
				</h3>								
			</div>
			<div class="bodyer">
				<div class="section">
					<div class="bodyer clearfix">
						<form id="myserviceform">
						 <div class="control-group">
						    <label class="control-label"><span style="color:red;">*</span>服务名称：</label>
						    <div class="controls">
						    	<input type="text" placeholder ="必填项" name="serviceName" value=""/>
			    			</div>
			 			 </div>
			 			 <div class="control-group">
							<label class="control-label">帐户状态：</label>
							<div class="controls">
								<select name="userType">
									<option value="">---请选择---</option>
									<option value="1" >对患者</option>
									<option value="2">对专家</option>
									<option value="3">对医生</option>
									<option value="-1">平台服务</option>
								</select>
							</div>
						</div>
			 			 <div class="control-group">
						    <label class="control-label">服务价格：</label>
						    <div class="controls">
						    	<input type="number" name="servicePrice" value=""/>
			    			</div>
			 			 </div>
						<div class="control-group">
							<label class="control-label">定价参数：</label>
							<div class="controls">
								<input type="text" name="priceParameter" value=""/>
							</div>
						</div>
						 <div class="control-group">
							<label class="control-label">服务状态：</label>
							<div class="controls">
								<select name="status">
									<option value="">---请选择---</option>
									<option value="1" >启用</option>
									<option value="0">未启用</option>
								</select>
							</div>
						</div>
						 <div class="control-group">
							<label class="control-label">服务对象：</label>
							<div class="controls">
								<select name="groupType">
									<option value="">---请选择---</option>
									<option value="1">患者服务</option>
									<option value="2">医生协作</option>
								</select>
							</div>
						</div>
						 <div class="control-group">
							<label class="control-label">描述：</label>
							<div class="controls">
								<input type="text" name="description" value=""/>
							</div>
						</div>
						 <div class="control-group">
							<label class="control-label">专家分成：</label>
							<div class="controls">
								<input type="number" name="expertDivided" value=""/>
							</div>
						</div>
						 <div class="control-group">
							<label class="control-label">医生分成：</label>
							<div class="controls">
								<input type="number" name="doctorDivided" value=""/>
							</div>
						</div>
						 <div class="control-group">
							<label class="control-label">平台分成：</label>
							<div class="controls">
								<input type="number" name="platformDivided" value=""/>
							</div>
						</div>
						 <div class="control-group">
							<label class="control-label">备注：</label>
							<div class="controls doublewidth">
								<input type="text" name="remark" value=""/>
							</div>
						</div>
						 <div class="control-group">
							<label class="control-label">分账说明：</label>
							<div class="controls doublewidth">
								<input type="text" name="serviceNote" value=""/>
							</div>
						</div>
						 <div class="control-group">
							<label class="control-label">服务说明地址：</label>
							<div class="controls doublewidth">
								<input type="text" name="serviceDescUrl" value=""/>
							</div>
						</div>
			 			 <div class="control-group">
						    <label class="control-label">服务图片地址：</label>
							<div class="controls">
								<div id="picsheadImageUrl" class="diyUpload">
									<div class="parentFileBox">
										<ul class="fileBoxUl clearfix">
											<li class="actionAdd">
												<div id="addheadImageUrl"><i class="iconfont">&#xe60e;</i></div>
											</li>
										</ul>
									</div>
									<input type="hidden" id="headImageUrl" name="imageUrl" value=""/>
								</div>
							</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">未用过的服务图片：</label>
							<div class="controls">
								<div id="picsrelatedPics" class="diyUpload">
									<div class="parentFileBox">
										<ul class="fileBoxUl clearfix">
											<li class="actionAdd">
												<div id="addUnnsedImageUrl"><i class="iconfont">&#xe60e;</i></div>
											</li>
										</ul>
									</div>
								<input type="hidden" name="imageUrlUnused" value=""/>
								</div>
							</div>
			 			 </div>
						 <div class="control-group">
							<label class="control-label"><span style="color:red;">*</span>服务类型：</label>
							<div class="controls">
								<select name="multiplePackage" id="selectMultiple">
									<option value="0" selected>单种</option>
									<option value="1">多种</option>
								</select>
							</div>
						</div>
						</form>
						<%--<div id="packagesContent"></div>--%>
						<form id="siglepackform">
							<div class="control-group">
								<div style="padding-left:118px;">单种：</div>
								<div class="control-group pleft">
									<label class="control-label"><span class="redcolor">*</span>包名：</label>
									<div class="controls">
										<input type="text" placeholder ="必填项" name="packageName" value=""/>
									</div>
								</div>
								<div class="control-group pleft">
									<label class="control-label"><span class="redcolor">*</span>价格：</label>
									<div class="controls">
										<input type="number" placeholder ="必填项" name="packagePrice" value=""/>
									</div>
								</div>
								<div class="control-group pleft">
									<label class="control-label">状态：</label>
									<div class="controls">
										<select name="status">
											<option value="1">生效</option>
											<option value="0">不生效</option>
										</select>
									</div>
								</div>
								<div class="control-group pleft">
									<label class="control-label">描述：</label>
									<div class="controls">
										<textarea name="description"></textarea>
									</div>
								</div>
							</div>
						</form>
						<div class="control-group" id="servicepackages">
							<div style="padding-left:118px;">多种：</div>
							<div id="totalpacks"></div>
							<button type="button" class="btn btn-primary mleft" id="addnewpack">点击添加</button>
						</div>
						<!-- Modal -->
						<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
								<h3 id="myModalLabel">添加服务包</h3>
							</div>
							<div class="modal-body">
								<form id="modal-body">
									<div class="control-group">
										<label class="info-label"><span class="redcolor">*</span>排序：</label>
										<input  style="min-width:75%;" type="number" name="rank" placeholder ="请输入1-99的整数" value=""/>
									</div>
									<div class="control-group">
										<label class="info-label"><span class="redcolor">*</span>包名：</label>
										<input type="text" class="info-input" required="required" placeholder ="必填项" name="packageName" value=""/>
									</div>
									<div class="control-group">
										<label class="info-label"><span class="redcolor">*</span>价格：</label>
										<input type="number" class="info-input" placeholder ="必填项" required name="packagePrice" value=""/>
									</div>
									<div class="control-group">
										<label class="info-label">&nbsp;状态：</label>
										<select name="status" style="min-width:75%;" class="modal-select">
											<option value="1">生效</option>
											<option value="0">不生效</option>
										</select>
									</div>
									<div class="control-group">
										<label class="info-label">&nbsp;描述：</label>
										<textarea class="info-input" name="description"></textarea>
									</div>
								</form>
							</div>
							<div class="modal-footer">
								<span style="color: red; margin-right: 2em;" id="tiptop"></span>
								<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
								<button class="btn btn-primary" id="getServiceInfo">添加</button>
							</div>
						</div>
						<div class="form-action">
							<button type="button" class="btn btn-primary" name="submitServiceform">保存</button>
						</div>
				</div>
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
			controller.addserviceinfo();
		});
	</script>
</body>
</html>

